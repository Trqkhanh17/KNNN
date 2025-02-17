/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package qlpt;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author khanh
 */
public class QuanLiPhongDAO {

    public void loadPhong(JTable table) {
        String sql = "SELECT * FROM Phong"; // Truy vấn dữ liệu từ bảng

        try (Connection conn = MyConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            // Lấy mô hình bảng từ JTable
            DefaultTableModel model = (DefaultTableModel) table.getModel();

            // Xóa dữ liệu cũ trong bảng
            model.setRowCount(0);
            // Lặp qua kết quả và thêm vào bảng
            while (rs.next()) {
                // Thêm dữ liệu vào bảng
                model.addRow(new Object[] {
                        rs.getInt("IDPhong"),
                        rs.getString("TenPhong"),
                        rs.getString("Loai"),
                        rs.getDouble("Gia"),
                        rs.getString("TrangThai")
                });
            }

        } catch (SQLException e) {
            System.out.println("Lỗi khi lấy dữ liệu: " + e.getMessage());
        }
    }

    public void themPhong(String tenPhong, double gia, String loaiPhong, String trangThai) {
        String sql = "INSERT INTO Phong (TenPhong, Gia, Loai, TrangThai) VALUES (?, ?, ?, ?)";

        try (Connection conn = MyConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tenPhong);
            stmt.setDouble(2, gia);
            stmt.setString(3,loaiPhong);
            stmt.setString(4, trangThai);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(null, "Thêm phòng thành công!", "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Không thể thêm phòng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi thêm phòng: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

}
