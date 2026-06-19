package Aplikasi;

import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.sql.*;

public class WilayahController {

    @FXML private TableView<WilayahModel>            tabelWilayah;
    @FXML private TableColumn<WilayahModel, Integer> colNo, colJumlah;
    @FXML private TableColumn<WilayahModel, String>  colNama, colKet;

    private final ObservableList<WilayahModel> list = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colNo.setCellValueFactory(new PropertyValueFactory<>("idWilayah"));
        colNama.setCellValueFactory(new PropertyValueFactory<>("namaWilayah"));
        colKet.setCellValueFactory(new PropertyValueFactory<>("keterangan"));
        colJumlah.setCellValueFactory(new PropertyValueFactory<>("jumlahWarga"));
        load();
    }

    private void load() {
        list.clear();
        String sql = """
            SELECT w.id_wilayah, w.nama_wilayah, w.keterangan,
                   COUNT(dw.id_warga) AS jumlah
            FROM wilayah w
            LEFT JOIN data_warga dw ON w.id_wilayah = dw.id_wilayah
            GROUP BY w.id_wilayah ORDER BY w.id_wilayah
        """;
        try (Connection c = DBConnection.getConnection();
             ResultSet rs = c.createStatement().executeQuery(sql)) {
            while (rs.next()) list.add(new WilayahModel(
                rs.getInt("id_wilayah"), rs.getString("nama_wilayah"),
                rs.getString("keterangan"), rs.getInt("jumlah")));
        } catch (Exception e) { e.printStackTrace(); }
        tabelWilayah.setItems(list);
    }

    @FXML
    private void handleTambah() {
        formDialog("Tambah Wilayah", null).showAndWait().ifPresent(w -> {
            try (Connection c = DBConnection.getConnection();
                 PreparedStatement ps = c.prepareStatement(
                     "INSERT INTO wilayah (nama_wilayah,keterangan) VALUES(?,?)")) {
                ps.setString(1, w.getNamaWilayah()); ps.setString(2, w.getKeterangan());
                ps.executeUpdate();
                info("Berhasil", "Wilayah berhasil ditambahkan!"); load();
            } catch (Exception e) { err("Gagal: " + e.getMessage()); }
        });
    }

    @FXML
    private void handleEdit() {
        WilayahModel sel = tabelWilayah.getSelectionModel().getSelectedItem();
        if (sel == null) { err("Pilih wilayah yang ingin diedit!"); return; }
        formDialog("Edit Wilayah", sel).showAndWait().ifPresent(w -> {
            try (Connection c = DBConnection.getConnection();
                 PreparedStatement ps = c.prepareStatement(
                     "UPDATE wilayah SET nama_wilayah=?,keterangan=? WHERE id_wilayah=?")) {
                ps.setString(1, w.getNamaWilayah()); ps.setString(2, w.getKeterangan());
                ps.setInt(3, sel.getIdWilayah());
                ps.executeUpdate();
                info("Berhasil", "Wilayah berhasil diupdate!"); load();
            } catch (Exception e) { err("Gagal: " + e.getMessage()); }
        });
    }

    @FXML
    private void handleHapus() {
        WilayahModel sel = tabelWilayah.getSelectionModel().getSelectedItem();
        if (sel == null) { err("Pilih wilayah yang ingin dihapus!"); return; }
        Alert konfirm = new Alert(Alert.AlertType.CONFIRMATION,
            "Hapus wilayah: " + sel.getNamaWilayah() + "?", ButtonType.YES, ButtonType.NO);
        konfirm.setHeaderText(null);
        konfirm.showAndWait().filter(r -> r == ButtonType.YES).ifPresent(r -> {
            try (Connection c = DBConnection.getConnection();
                 PreparedStatement ps = c.prepareStatement("DELETE FROM wilayah WHERE id_wilayah=?")) {
                ps.setInt(1, sel.getIdWilayah());
                ps.executeUpdate();
                info("Berhasil", "Wilayah berhasil dihapus!"); load();
            } catch (Exception e) { err("Gagal hapus (pastikan tidak ada warga di wilayah ini): " + e.getMessage()); }
        });
    }

    private Dialog<WilayahModel> formDialog(String judul, WilayahModel existing) {
        Dialog<WilayahModel> dialog = new Dialog<>();
        dialog.setTitle(judul); dialog.setHeaderText(null);
        ButtonType simpan = new ButtonType("Simpan", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(simpan, ButtonType.CANCEL);
        dialog.getDialogPane().setStyle("-fx-background-color: #FAF3E0;");

        GridPane g = new GridPane(); g.setHgap(10); g.setVgap(10);
        g.setStyle("-fx-padding: 20;");
        String ls = "-fx-font-weight:bold;-fx-text-fill:#5C2E0E;";

        TextField fNama = new TextField(); fNama.setPromptText("Contoh: RT 01/RW 01");
        TextField fKet  = new TextField(); fKet.setPromptText("Keterangan lokasi");
        if (existing != null) { fNama.setText(existing.getNamaWilayah()); fKet.setText(existing.getKeterangan()); }

        g.add(new Label("Nama Wilayah:") {{ setStyle(ls); }}, 0, 0); g.add(fNama, 1, 0);
        g.add(new Label("Keterangan:")   {{ setStyle(ls); }}, 0, 1); g.add(fKet,  1, 1);

        dialog.getDialogPane().setContent(g);
        dialog.setResultConverter(btn -> btn == simpan
            ? new WilayahModel(0, fNama.getText(), fKet.getText(), 0) : null);
        return dialog;
    }

    @FXML private void goDashboard() { nav("/Aplikasi/DashboardPage.fxml",     "Dashboard"); }
    @FXML private void goDataWarga() { nav("/Aplikasi/DataWargaPage.fxml",     "Data Warga"); }
    @FXML private void goAntrian()   { nav("/Aplikasi/AntrianPage.fxml",       "Antrian"); }
    @FXML private void goPanggil()   { nav("/Aplikasi/PanggilAntrianPage.fxml","Panggil Antrian"); }

    private void nav(String fxml, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = (Stage) tabelWilayah.getScene().getWindow();
            stage.setScene(new Scene(root, 1000, 650));
            stage.setTitle(title + " — Antrian Qurban");
        } catch (Exception e) { e.printStackTrace(); }
    }
    private void err(String m) { new Alert(Alert.AlertType.ERROR, m, ButtonType.OK).showAndWait(); }
    private void info(String t, String m) {
        Alert a = new Alert(Alert.AlertType.INFORMATION); a.setTitle(t); a.setHeaderText(null); a.setContentText(m); a.showAndWait();
    }
}
