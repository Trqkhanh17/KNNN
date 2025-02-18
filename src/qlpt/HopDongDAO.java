/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package qlpt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author LENOVO
 */
public class HopDongDAO {
    public static void loadHopDongData(JTable table){
        String sql = "SELECT * FROM HopDong"; // Truy vấn dữ liệu từ bảng
        
        try ( Connection conn = MyConnection.getConnection();  
                PreparedStatement stmt = conn.prepareStatement(sql);  
                ResultSet rs = stmt.executeQuery()) {

            // Lấy mô hình bảng từ JTable
            DefaultTableModel model = (DefaultTableModel) table.getModel();

            // Xóa dữ liệu cũ trong bảng
            model.setRowCount(0);

            // Định dạng ngày từ yyyy-MM-dd -> dd/MM/yy
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            // Lặp qua kết quả và thêm vào bảng
            while (rs.next()) {
                // Thêm dữ liệu vào bảng
                model.addRow(new Object[]{
                    rs.getInt("IDHopDong"),
                    rs.getString("TenPhong"),
                    rs.getString("TenKhachHang"),
                    rs.getString("TienCoc"),
                    rs.getString("ThoiHan"),
                    rs.getString("SoNguoi"),
                    rs.getString("SoLuongXe"),
                    rs.getString("TrangThai"),
                    rs.getString("NgayLap"),
                    rs.getString("HinhAnhDinhKem"),
                });
            }

        } catch (SQLException e) {
            System.out.println("Lỗi khi lấy dữ liệu: " + e.getMessage());
        }
    }
        // them du lieu
    public void themHopDong( int idPhong, int idKhachHang, String ngayLap, String tienCoc, String thoiHan, String soNguoiO, String soLuongXe, String hinhAnhDinhKem) {
        String sql = "INSERT INTO HopDong (IDPhong, IDKhachHang, NgayLap, TienCoc, String ThoiHan, String SoNguoiO, String SoLuongXe, String HinhAnhDinhKem) VALUES (?, ?, ?, ?, ?, ?, ? ,?)";

        try ( Connection conn = MyConnection.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set giá trị vào SQL
            stmt.setInt(1, idPhong);
            stmt.setInt(2, idKhachHang);
            stmt.setString(3, ngayLap);
            stmt.setString(4,tienCoc);
            stmt.setString(5,thoiHan);
            stmt.setString(6,soNguoiO);
            stmt.setString(7,soLuongXe);
            stmt.setString(8,hinhAnhDinhKem);

            // Thực thi lệnh SQL
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Thêm thành công!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi thêm vào CSDL: " + e.getMessage());
        }
    }
    
    // Sửa dữ liệu
    public void suaHopDong(int idHopDong, String tenPhong, String tenKhachHang, String ngayLap, String tienCoc, String thoiHan, String soNguoiO, String soLuongXe, String trangThai) {
    String sql = "UPDATE HopDong SET Phong = ?, KhachHang = ?, NgayLap = ?, TienCoc = ?, ThoiHan = ?, SoNguoiO = ?, SoLuongXe = ?, TrangThai = ? WHERE IDHopDong = ?";

    try (Connection conn = MyConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, tenPhong);
        stmt.setString(2, tenKhachHang);
        stmt.setString(3, ngayLap);
        stmt.setString(4, tienCoc);
        stmt.setString(5, thoiHan);
        stmt.setString(6, soNguoiO);
        stmt.setString(7, soLuongXe);
        stmt.setString(8, trangThai);
        stmt.setInt(9, idHopDong);

        stmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
    
    // Xóa dữ liệu
    public void xoaHopDong(int idHopDong) {
    String sql = "DELETE FROM HopDong WHERE IDHopDong = ?";

    try (Connection conn = MyConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, idHopDong);
        stmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
}
