package Aplikasi;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(
            getClass().getResource("/Aplikasi/LandingPage.fxml"));
        primaryStage.setTitle("Antrian Pengambilan Daging Qurban");
        primaryStage.setScene(new Scene(root, 900, 600));
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        java.sql.Connection conn = DBConnection.getConnection();
        if (conn != null) {
            System.out.println("KONEKSI DATABASE BERHASIL!");
        } else {
            System.out.println("KONEKSI GAGAL! Cek XAMPP MySQL.");
        }
        launch(args);
    }
}
