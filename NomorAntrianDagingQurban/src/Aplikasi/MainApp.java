

import Aplikasi.DBConnection;

import java.sql.Connection;

public static void main(String[] args) {

    Connection conn = DBConnection.getConnection();
    if (conn != null) {
        System.out.println("✅ KONEKSI BERHASIL! Database terhubung.");
    } else {
        System.out.println("❌ KONEKSI GAGAL! Cek MySQL dan DBConnection.java");
    }
}