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
import java.util.Optional;

public class DataWargaController {

    @FXML private TableView<WargaModel>            tabelWarga;
    @FXML private TableColumn<WargaModel, Integer> colNo;
    @FXML private TableColumn<WargaModel, String>  colNik, colNama, colHp, colWilayah;

    private final ObservableList<WargaModel> list = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colNo.setCellValueFactory(new PropertyValueFactory<>("idWarga"));
        colNik.setCellValueFactory(new PropertyValueFactory<>("nik"));
        colNama.setCellValueFactory(new PropertyValueFactory<>("namaWarga"));
        colHp.setCellValueFactory(new PropertyValueFactory<>("noHp"));
        colWilayah.setCellValueFactory(new PropertyValueFactory<>("namaWilayah"));
        load();
    }

    private void load() {
        list.clear();
        String sql = """
            SELECT dw.id_warga, dw.nik, dw.nama_warga, dw.no_hp,
                   w.nama_wilayah, dw.id_wilayah
            FROM data_warga dw
            JOIN wilayah w ON dw.id_wilayah = w.id_wilayah
            ORDER BY dw.id_warga
        """;
        try (Connection c = DBConnection.getConnection();
             ResultSet rs  = c.createStatement().executeQuery(sql)) {
            while (rs.next()) list.add(new WargaModel(
                rs.getInt("id_warga"), rs.getString("nik"),
                rs.getString("nama_warga"), rs.getString("no_hp"),
                rs.getString("nama_wilayah"), rs.getInt("id_wilayah")));
        } catch (Exception e) { e.printStackTrace(); }
        tabelWarga.setItems(list);
    }

    @FXML
    private void handleTambah() {
        formDialog("Tambah Warga", null).showAndWait().ifPresent(w -> {
            try (Connection c = DBConnection.getConnection();
                 PreparedStatement ps = c.prepareStatement(
                     "INSERT INTO data_warga (nik,nama_warga,no_hp,id_wilayah) VALUES(?,?,?,?)")) {
                ps.setString(1, w.getNik()); ps.setString(2, w.getNamaWarga());
                ps.setString(3, w.getNoHp()); ps.setInt(4, w.getIdWilayah());
                ps.executeUpdate();
                info("Berhasil", "Warga berhasil ditambahkan!"); load();
            } catch (Exception e) { err("Gagal tambah: " + e.getMessage()); }
        });
    }

    @FXML
    private void handleEdit() {
        WargaModel sel = tabelWarga.getSelectionModel().getSelectedItem();
        if (sel == null) { err("Pilih data warga yang ingin diedit!"); return; }
        formDialog("Edit Warga", sel).showAndWait().ifPresent(w -> {
            try (Connection c = DBConnection.getConnection();
                 PreparedStatement ps = c.prepareStatement(
                     "UPDATE data_warga SET nik=?,nama_warga=?,no_hp=?,id_wilayah=? WHERE id_warga=?")) {
                ps.setString(1, w.getNik()); ps.setString(2, w.getNamaWarga());
                ps.setString(3, w.getNoHp()); ps.setInt(4, w.getIdWilayah());
                ps.setInt(5, sel.getIdWarga());
                ps.executeUpdate();
                info("Berhasil", "Data warga berhasil diupdate!"); load();
            } catch (Exception e) { err("Gagal update: " + e.getMessage()); }
        });
    }

    @FXML
    private void handleHapus() {
        WargaModel sel = tabelWarga.getSelectionModel().getSelectedItem();
        if (sel == null) { err("Pilih data warga yang ingin dihapus!"); return; }
        Alert konfirm = new Alert(Alert.AlertType.CONFIRMATION,
            "Hapus warga: " + sel.getNamaWarga() + "?", ButtonType.YES, ButtonType.NO);
        konfirm.setHeaderText(null);
        konfirm.showAndWait().filter(r -> r == ButtonType.YES).ifPresent(r -> {
            try (Connection c = DBConnection.getConnection();
                 PreparedStatement ps = c.prepareStatement(
                     "DELETE FROM data_warga WHERE id_warga=?")) {
                ps.setInt(1, sel.getIdWarga());
                ps.executeUpdate();
                info("Berhasil", "Warga berhasil dihapus!"); load();
            } catch (Exception e) { err("Gagal hapus (pastikan warga tidak punya antrian aktif): " + e.getMessage()); }
        });
    }

    private Dialog<WargaModel> formDialog(String judul, WargaModel existing) {
        Dialog<WargaModel> dialog = new Dialog<>();
        dialog.setTitle(judul); dialog.setHeaderText(null);
        ButtonType simpan = new ButtonType("Simpan", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(simpan, ButtonType.CANCEL);
        dialog.getDialogPane().setStyle("-fx-background-color: #FAF3E0;");

        GridPane g = new GridPane(); g.setHgap(10); g.setVgap(10);
        g.setStyle("-fx-padding: 20;");
        String ls = "-fx-font-weight:bold;-fx-text-fill:#5C2E0E;";

        TextField fNik  = new TextField(); fNik.setPromptText("16 digit NIK");
        TextField fNama = new TextField(); fNama.setPromptText("Nama lengkap");
        TextField fHp   = new TextField(); fHp.setPromptText("Nomor HP");
        ComboBox<WilayahModel> cbWil = new ComboBox<>();

        // Load wilayah
        try (Connection c = DBConnection.getConnection();
             ResultSet rs = c.createStatement().executeQuery("SELECT * FROM wilayah ORDER BY id_wilayah")) {
            while (rs.next()) cbWil.getItems().add(
                new WilayahModel(rs.getInt(1), rs.getString(2), rs.getString(3), 0));
        } catch (Exception e) { e.printStackTrace(); }

        if (existing != null) {
            fNik.setText(existing.getNik()); fNama.setText(existing.getNamaWarga());
            fHp.setText(existing.getNoHp());
            cbWil.getItems().stream()
                .filter(w -> w.getIdWilayah() == existing.getIdWilayah())
                .findFirst().ifPresent(cbWil::setValue);
        }

        g.add(new Label("NIK:")         {{ setStyle(ls); }}, 0, 0); g.add(fNik,  1, 0);
        g.add(new Label("Nama Warga:")  {{ setStyle(ls); }}, 0, 1); g.add(fNama, 1, 1);
        g.add(new Label("No HP:")       {{ setStyle(ls); }}, 0, 2); g.add(fHp,   1, 2);
        g.add(new Label("Wilayah:")     {{ setStyle(ls); }}, 0, 3); g.add(cbWil, 1, 3);

        dialog.getDialogPane().setContent(g);
        dialog.setResultConverter(btn -> {
            if (btn == simpan) {
                if (cbWil.getValue() == null) { err("Pilih wilayah!"); return null; }
                return new WargaModel(0, fNik.getText(), fNama.getText(),
                    fHp.getText(), cbWil.getValue().getNamaWilayah(), cbWil.getValue().getIdWilayah());
            }
            return null;
        });
        return dialog;
    }

    @FXML private void goDashboard() { nav("/Aplikasi/DashboardPage.fxml",     "Dashboard"); }
    @FXML private void goAntrian()   { nav("/Aplikasi/AntrianPage.fxml",       "Antrian"); }
    @FXML private void goPanggil()   { nav("/Aplikasi/PanggilAntrianPage.fxml","Panggil Antrian"); }
    @FXML private void goWilayah()   { nav("/Aplikasi/WilayahPage.fxml",       "Wilayah"); }

    private void nav(String fxml, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = (Stage) tabelWarga.getScene().getWindow();
            stage.setScene(new Scene(root, 1000, 650));
            stage.setTitle(title + " — Antrian Qurban");
        } catch (Exception e) { e.printStackTrace(); }
    }
    private void err(String m)          { new Alert(Alert.AlertType.ERROR, m, ButtonType.OK).showAndWait(); }
    private void info(String t, String m) {
        Alert a = new Alert(Alert.AlertType.INFORMATION); a.setTitle(t); a.setHeaderText(null); a.setContentText(m); a.showAndWait();
    }
}
