package Aplikasi;

import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.sql.*;

public class LoginController {
    @FXML private TextField     txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private Button        btnLogin;

    @FXML
    private void handleLogin() {
        String user = txtUsername.getText().trim();
        String pass = txtPassword.getText().trim();

        if (user.isEmpty() || pass.isEmpty()) {
            alert(Alert.AlertType.WARNING, "Username dan password tidak boleh kosong!");
            return;
        }
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT * FROM user WHERE username=? AND password=?")) {
            ps.setString(1, user); ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Parent root = FXMLLoader.load(getClass().getResource("/Aplikasi/DashboardPage.fxml"));
                Stage stage = (Stage) btnLogin.getScene().getWindow();
                stage.setScene(new Scene(root, 1000, 650));
                stage.setTitle("Dashboard — Antrian Qurban");
            } else {
                alert(Alert.AlertType.ERROR, "Username atau password salah!");
            }
        } catch (Exception e) {
            alert(Alert.AlertType.ERROR, "Koneksi database gagal!\n" + e.getMessage());
        }
    }

    private void alert(Alert.AlertType t, String msg) {
        Alert a = new Alert(t); a.setHeaderText(null); a.setContentText(msg); a.showAndWait();
    }
}
