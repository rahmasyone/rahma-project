package Kelas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;

public class koneksi {
 private static Connection mysqlConfig;

 public static Connection configDB() throws SQLException {
  try {
   //Sesuaikan "kasir_restoran" dengan nama database yang kamu buat di MySQL
   String url = "jdbc:mysql://localhost:3306/kasir_restoran";
   String user = "root";
   String password = ""; // Kosongkan jika pakai XAMPP default

   DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
   mysqlConfig = DriverManager.getConnection(url, user, password);

  } catch (SQLException ex) {
   System.err.println("Koneksi Gagal: " + ex.getMessage());
  }
  return mysqlConfig;
 }
}