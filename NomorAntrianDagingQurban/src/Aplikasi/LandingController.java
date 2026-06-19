package Aplikasi;

import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class LandingController {
    @FXML private Button btnMasuk;

    @FXML
    private void handleMasuk() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Aplikasi/LoginPage.fxml"));
            Stage stage = (Stage) btnMasuk.getScene().getWindow();
            stage.setScene(new Scene(root, 900, 600));
            stage.setTitle("Login — Antrian Qurban");
        } catch (Exception e) { e.printStackTrace(); }
    }
}
