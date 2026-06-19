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

public class AntrianController {

    @FXML private TableView<AntrianModel>            tabelAntrian;
    @FXML private TableColumn<AntrianModel, Integer> colNomor;
    @FXML private TableColumn<AntrianModel, String>  colNama, colWilayah, colStatus, colDaftar, colPanggil;

    private final ObservableList<AntrianModel> list = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colNomor.setCellValueFactory(new PropertyValueFactory<>("nomorAntrian"));
        colNama.setCellValueFactory(new PropertyValueFactory<>("namaWarga"));
        colWilayah.setCellValueFactory(new PropertyValueFactory<>("namaWilayah"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colDaftar.setCellValueFactory(new PropertyValueFactory<>("waktuDaftar"));
        colPanggil.setCellValueFactory(new PropertyValueFactory<>("waktuDipanggil"));
        load();
    }

    private void load() {
        list.clear();
        String sql = """
            SELECT a.nomor_antrian, dw.nama_warga, w.nama_wilayah,
                   a.status, a.waktu_daftar, a.waktu_dipanggil
            FROM antrian a
            JOIN data_warga dw ON a.id_warga   = dw.id_warga
            JOIN wilayah    w  ON a.id_wilayah = w.id_wilayah
            ORDER BY a.nomor_antrian
        """;
        try (Connection c = DBConnection.getConnection();
             ResultSet rs = c.createStatement().executeQuery(sql)) {
            while (rs.next()) list.add(new AntrianModel(
                rs.getInt("nomor_antrian"), rs.getString("nama_warga"),
                rs.getString("nama_wilayah"), rs.getString("status"),
                rs.getString("waktu_daftar"), rs.getString("waktu_dipanggil")));
        } catch (Exception e) { e.printStackTrace(); }
        tabelAntrian.setItems(list);
    }

    @FXML
    private void handleTambah() {
        Dialog<int[]> dialog = new Dialog<>();
        dialog.setTitle("Tambah Antrian"); dialog.setHeaderText(null);
        ButtonType simpan = new ButtonType("Simpan", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(simpan, ButtonType.CANCEL);
        dialog.getDialogPane().setStyle("-fx-background-color: #FAF3E0;");

        GridPane g = new GridPane(); g.setHgap(10); g.setVgap(10);
        g.setStyle("-fx-padding: 20;");
        String ls = "-fx-font-weight:bold;-fx-text-fill:#5C2E0E;";

        ComboBox<WargaModel>   cbWarga = new ComboBox<>();
        ComboBox<WilayahModel> cbWil   = new ComboBox<>();

        try (Connection c = DBConnection.getConnection()) {
            ResultSet rw = c.createStatement().executeQuery(
                "SELECT dw.id_warga, dw.nama_warga, w.nama_wilayah, dw.id_wilayah " +
                "FROM data_warga dw JOIN wilayah w ON dw.id_wilayah=w.id_wilayah");
            while (rw.next()) cbWarga.getItems().add(
                new WargaModel(rw.getInt(1), "", rw.getString(2), "", rw.getString(3), rw.getInt(4)));

            ResultSet rwil = c.createStatement().executeQuery("SELECT * FROM wilayah");
            while (rwil.next()) cbWil.getItems().add(
                new WilayahModel(rwil.getInt(1), rwil.getString(2), rwil.getString(3), 0));
        } catch (Exception e) { e.printStackTrace(); }

        // Auto isi wilayah saat pilih warga
        cbWarga.setOnAction(e -> {
            WargaModel sel = cbWarga.getValue();
            if (sel != null) cbWil.getItems().stream()
                .filter(w -> w.getIdWilayah() == sel.getIdWilayah())
                .findFirst().ifPresent(cbWil::setValue);
        });

        g.add(new Label("Warga:")   {{ setStyle(ls); }}, 0, 0); g.add(cbWarga, 1, 0);
        g.add(new Label("Wilayah:") {{ setStyle(ls); }}, 0, 1); g.add(cbWil,   1, 1);

        dialog.getDialogPane().setContent(g);
        dialog.setResultConverter(btn -> {
            if (btn == simpan) {
                if (cbWarga.getValue() == null || cbWil.getValue() == null) {
                    err("Pilih warga dan wilayah!"); return null;
                }
                return new int[]{cbWarga.getValue().getIdWarga(), cbWil.getValue().getIdWilayah()};
            }
            return null;
        });

        dialog.showAndWait().ifPresent(ids -> {
            try (Connection c = DBConnection.getConnection()) {
                ResultSet rs = c.createStatement()
                    .executeQuery("SELECT IFNULL(MAX(nomor_antrian),0)+1 FROM antrian");
                int next = rs.next() ? rs.getInt(1) : 1;
                PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO antrian (nomor_antrian,id_warga,id_wilayah,status) VALUES(?,?,?,'menunggu')");
                ps.setInt(1, next); ps.setInt(2, ids[0]); ps.setInt(3, ids[1]);
                ps.executeUpdate();
                info("Berhasil", "Antrian No." + String.format("%03d", next) + " berhasil ditambahkan!");
                load();
            } catch (Exception e) { err("Gagal: " + e.getMessage()); }
        });
    }

    @FXML private void goDashboard() { nav("/Aplikasi/DashboardPage.fxml",     "Dashboard"); }
    @FXML private void goDataWarga() { nav("/Aplikasi/DataWargaPage.fxml",     "Data Warga"); }
    @FXML private void goPanggil()   { nav("/Aplikasi/PanggilAntrianPage.fxml","Panggil Antrian"); }
    @FXML private void goWilayah()   { nav("/Aplikasi/WilayahPage.fxml",       "Wilayah"); }

    private void nav(String fxml, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = (Stage) tabelAntrian.getScene().getWindow();
            stage.setScene(new Scene(root, 1000, 650));
            stage.setTitle(title + " — Antrian Qurban");
        } catch (Exception e) { e.printStackTrace(); }
    }
    private void err(String m) { new Alert(Alert.AlertType.ERROR, m, ButtonType.OK).showAndWait(); }
    private void info(String t, String m) {
        Alert a = new Alert(Alert.AlertType.INFORMATION); a.setTitle(t); a.setHeaderText(null); a.setContentText(m); a.showAndWait();
    }
}
