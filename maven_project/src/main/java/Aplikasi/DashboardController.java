package Aplikasi;

import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.sql.*;

public class DashboardController {

    // Label statistik — fx:id harus ada di DashboardPage.fxml
    @FXML private Label lblTotalWarga;
    @FXML private Label lblMenunggu;
    @FXML private Label lblDipanggil;

    // Tabel antrian terbaru
    @FXML private TableView<AntrianModel>              tabelAntrian;
    @FXML private TableColumn<AntrianModel, Integer>   colNomor;
    @FXML private TableColumn<AntrianModel, String>    colNama;
    @FXML private TableColumn<AntrianModel, String>    colWilayah;
    @FXML private TableColumn<AntrianModel, String>    colStatus;
    @FXML private TableColumn<AntrianModel, String>    colWaktu;

    @FXML
    public void initialize() {
        // Setup kolom tabel
        colNomor.setCellValueFactory(new PropertyValueFactory<>("nomorAntrian"));
        colNama.setCellValueFactory(new PropertyValueFactory<>("namaWarga"));
        colWilayah.setCellValueFactory(new PropertyValueFactory<>("namaWilayah"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colWaktu.setCellValueFactory(new PropertyValueFactory<>("waktuDaftar"));

        loadStatistik();
        loadTabelAntrian();
    }

    private void loadStatistik() {
        try (Connection conn = DBConnection.getConnection()) {
            // Total warga
            ResultSet r1 = conn.createStatement()
                .executeQuery("SELECT COUNT(*) FROM data_warga");
            if (r1.next()) lblTotalWarga.setText(String.valueOf(r1.getInt(1)));

            // Menunggu
            ResultSet r2 = conn.createStatement()
                .executeQuery("SELECT COUNT(*) FROM antrian WHERE status='menunggu'");
            if (r2.next()) lblMenunggu.setText(String.valueOf(r2.getInt(1)));

            // Dipanggil
            ResultSet r3 = conn.createStatement()
                .executeQuery("SELECT COUNT(*) FROM antrian WHERE status='dipanggil'");
            if (r3.next()) lblDipanggil.setText(String.valueOf(r3.getInt(1)));

        } catch (Exception e) { e.printStackTrace(); }
    }

    private void loadTabelAntrian() {
        ObservableList<AntrianModel> list = FXCollections.observableArrayList();
        String sql = """
            SELECT a.nomor_antrian, dw.nama_warga, w.nama_wilayah,
                   a.status, a.waktu_daftar, a.waktu_dipanggil
            FROM antrian a
            JOIN data_warga dw ON a.id_warga   = dw.id_warga
            JOIN wilayah    w  ON a.id_wilayah = w.id_wilayah
            ORDER BY a.nomor_antrian DESC LIMIT 10
        """;
        try (Connection conn = DBConnection.getConnection();
             ResultSet rs = conn.createStatement().executeQuery(sql)) {
            while (rs.next()) list.add(new AntrianModel(
                rs.getInt("nomor_antrian"), rs.getString("nama_warga"),
                rs.getString("nama_wilayah"), rs.getString("status"),
                rs.getString("waktu_daftar"), rs.getString("waktu_dipanggil")));
        } catch (Exception e) { e.printStackTrace(); }
        tabelAntrian.setItems(list);
    }

    @FXML private void handleTambahAntrian() { nav("/Aplikasi/AntrianPage.fxml", "Antrian", tabelAntrian); }
    @FXML private void goDataWarga()  { nav("/Aplikasi/DataWargaPage.fxml",     "Data Warga",      tabelAntrian); }
    @FXML private void goAntrian()    { nav("/Aplikasi/AntrianPage.fxml",       "Antrian",         tabelAntrian); }
    @FXML private void goPanggil()    { nav("/Aplikasi/PanggilAntrianPage.fxml","Panggil Antrian", tabelAntrian); }
    @FXML private void goWilayah()    { nav("/Aplikasi/WilayahPage.fxml",       "Wilayah",         tabelAntrian); }

    private void nav(String fxml, String title, javafx.scene.Node node) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = (Stage) node.getScene().getWindow();
            stage.setScene(new Scene(root, 1000, 650));
            stage.setTitle(title + " — Antrian Qurban");
        } catch (Exception e) { e.printStackTrace(); }
    }
}
