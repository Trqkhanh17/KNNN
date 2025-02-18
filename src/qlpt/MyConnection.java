/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package qlpt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=QLPT;encrypt=true;trustServerCertificate=true";
    private static final String USER = "sa";
    private static final String PASSWORD = "123456";
    private static Connection conn;

    // Hàm lấy kết nối (Singleton + Kiểm tra trạng thái kết nối)
    public static Connection getConnection() {
        try {
            // Nếu kết nối chưa được tạo hoặc đã bị đóng, tạo lại kết nối mới
            if (conn == null || conn.isClosed()) {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                conn = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Kết nối SQL Server thành công!");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Lỗi: Không tìm thấy driver SQL Server!");
        } catch (SQLException e) {
            System.out.println("Lỗi kết nối SQL Server: " + e.getMessage());
        }
        return conn;
    }

    // Hàm đóng kết nối an toàn
    public static void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
                conn = null; // Đặt về null để có thể mở lại khi cần
                System.out.println("Kết nối SQL Server đã đóng.");
            } catch (SQLException e) {
                System.out.println("Lỗi khi đóng kết nối: " + e.getMessage());
            }
        }
    }
}
