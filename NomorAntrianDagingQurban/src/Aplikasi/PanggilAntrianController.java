package Aplikasi;

import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.sql.*;

public class PanggilAntrianController {

    @FXML private Label  lblNomor;
    @FXML private Label  lblNamaWarga;
    @FXML private Label  lblWilayah;
    @FXML private TableView<AntrianModel>            tabelMenunggu;
    @FXML private TableColumn<AntrianModel, Integer> colNomor;
    @FXML private TableColumn<AntrianModel, String>  colNama, colWil;

    private int idAntrianAktif = -1;

    @FXML
    public void initialize() {
        colNomor.setCellValueFactory(new PropertyValueFactory<>("nomorAntrian"));
        colNama.setCellValueFactory(new PropertyValueFactory<>("namaWarga"));
        colWil.setCellValueFactory(new PropertyValueFactory<>("namaWilayah"));
        loadAktif();
        loadMenunggu();
    }

    private void loadAktif() {
        String sql = """
            SELECT a.id_antrian, a.nomor_antrian, dw.nama_warga,
                   w.nama_wilayah, a.waktu_dipanggil
            FROM antrian a
            JOIN data_warga dw ON a.id_warga   = dw.id_warga
            JOIN wilayah    w  ON a.id_wilayah = w.id_wilayah
            WHERE a.status = 'dipanggil'
            ORDER BY a.waktu_dipanggil DESC LIMIT 1
        """;
        try (Connection c = DBConnection.getConnection();
             ResultSet rs = c.createStatement().executeQuery(sql)) {
            if (rs.next()) {
                idAntrianAktif = rs.getInt("id_antrian");
                lblNomor.setText(String.format("%03d", rs.getInt("nomor_antrian")));
                lblNamaWarga.setText(rs.getString("nama_warga"));
                String waktu = rs.getString("waktu_dipanggil");
                lblWilayah.setText(rs.getString("nama_wilayah") + " — " +
                    (waktu != null ? waktu.substring(11, 16) + " WIB" : "-"));
            } else {
                idAntrianAktif = -1;
                lblNomor.setText("---");
                lblNamaWarga.setText("Belum ada yang dipanggil");
                lblWilayah.setText("-");
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void loadMenunggu() {
        ObservableList<AntrianModel> list = FXCollections.observableArrayList();
        String sql = """
            SELECT a.nomor_antrian, dw.nama_warga, w.nama_wilayah
            FROM antrian a
            JOIN data_warga dw ON a.id_warga   = dw.id_warga
            JOIN wilayah    w  ON a.id_wilayah = w.id_wilayah
            WHERE a.status = 'menunggu'
            ORDER BY a.nomor_antrian
        """;
        try (Connection c = DBConnection.getConnection();
             ResultSet rs = c.createStatement().executeQuery(sql)) {
            while (rs.next()) list.add(new AntrianModel(
                rs.getInt(1), rs.getString(2), rs.getString(3), "menunggu", "", ""));
        } catch (Exception e) { e.printStackTrace(); }
        tabelMenunggu.setItems(list);
    }

    @FXML
    private void handlePanggil() {
        String sql = "SELECT id_antrian FROM antrian WHERE status='menunggu' ORDER BY nomor_antrian LIMIT 1";
        try (Connection c = DBConnection.getConnection();
             ResultSet rs = c.createStatement().executeQuery(sql)) {
            if (rs.next()) {
                PreparedStatement ps = c.prepareStatement(
                    "UPDATE antrian SET status='dipanggil', waktu_dipanggil=NOW() WHERE id_antrian=?");
                ps.setInt(1, rs.getInt("id_antrian"));
                ps.executeUpdate();
                loadAktif(); loadMenunggu();
            } else {
                info("Info", "Tidak ada antrian yang menunggu!");
            }
        } catch (Exception e) { err("Gagal: " + e.getMessage()); }
    }

    @FXML
    private void handleSelesai() {
        if (idAntrianAktif == -1) { err("Tidak ada antrian yang aktif!"); return; }
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(
                 "UPDATE antrian SET status='selesai' WHERE id_antrian=?")) {
            ps.setInt(1, idAntrianAktif);
            ps.executeUpdate();
            idAntrianAktif = -1;
            loadAktif(); loadMenunggu();
        } catch (Exception e) { err("Gagal: " + e.getMessage()); }
    }

    @FXML
    private void handleLewati() {
        if (idAntrianAktif == -1) { err("Tidak ada antrian yang aktif!"); return; }
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(
                 "UPDATE antrian SET status='menunggu', waktu_dipanggil=NULL WHERE id_antrian=?")) {
            ps.setInt(1, idAntrianAktif);
            ps.executeUpdate();
            idAntrianAktif = -1;
            loadAktif(); loadMenunggu();
        } catch (Exception e) { err("Gagal: " + e.getMessage()); }
    }

    @FXML private void goDashboard() { nav("/Aplikasi/DashboardPage.fxml",  "Dashboard"); }
    @FXML private void goDataWarga() { nav("/Aplikasi/DataWargaPage.fxml",  "Data Warga"); }
    @FXML private void goAntrian()   { nav("/Aplikasi/AntrianPage.fxml",    "Antrian"); }
    @FXML private void goWilayah()   { nav("/Aplikasi/WilayahPage.fxml",    "Wilayah"); }

    private void nav(String fxml, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = (Stage) lblNomor.getScene().getWindow();
            stage.setScene(new Scene(root, 1000, 650));
            stage.setTitle(title + " — Antrian Qurban");
        } catch (Exception e) { e.printStackTrace(); }
    }
    private void err(String m) { new Alert(Alert.AlertType.ERROR, m, ButtonType.OK).showAndWait(); }
    private void info(String t, String m) {
        Alert a = new Alert(Alert.AlertType.INFORMATION); a.setTitle(t); a.setHeaderText(null); a.setContentText(m); a.showAndWait();
    }
}
