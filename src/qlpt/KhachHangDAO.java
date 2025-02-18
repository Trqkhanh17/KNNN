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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import qlpt.MyConnection;

/**
 *
 * @author LENOVO
 */
public class KhachHangDAO {
    public static void loadKhachHangData(JTable table){
        String sql = "SELECT * FROM KhachHang"; // Truy vấn dữ liệu từ bảng
        
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
                    rs.getInt("IDKhachhang"),
                    rs.getString("HoVaTen"),
                    rs.getString("DiaChi"),
                    rs.getString("SDT"),
                    rs.getString("CCCD"),
                    rs.getString("HinhAnhCCCD"),
                    
                });
            }

        } catch (SQLException e) {
            System.out.println("Lỗi khi lấy dữ liệu: " + e.getMessage());
        }
    }
    
    // them du lieu
    public void themKhachHang(String hoVaTen, String diaChi, String sDT, String cCCD, String hinhAnhCCCD) {
        String sql = "INSERT INTO KhachHang (HoVaTen, DiaChi, SDT, CCCD, HinhAnhCCCD) VALUES (?, ?, ?, ?, ?)";

        try ( Connection conn = MyConnection.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set giá trị vào SQL
            stmt.setString(1, hoVaTen);
            stmt.setString(2, diaChi);
            stmt.setString(3, sDT);
            stmt.setString(4,cCCD);
            stmt.setString(5,hinhAnhCCCD);

            // Thực thi lệnh SQL
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Thêm thành công!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi thêm vào CSDL: " + e.getMessage());
        }
    }
    
    // Sửa dữ liệu
    public void suaKhachHang(int id, String hoVaTen, String diaChi, String sDT, String cCCD, String hinhAnhCCCD) {
        if (id == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn một dòng để sửa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "UPDATE KhachHang SET HoVaTen = ?, DiaChi = ?, SDT = ?, CCCD = ?, HinhAnhCCCD = ? WHERE IDKhachHang = ?";

        try ( Connection conn = MyConnection.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Đặt các giá trị vào câu lệnh SQL
            stmt.setString(1, hoVaTen);
            stmt.setString(2, diaChi);
            stmt.setString(3, sDT);
            stmt.setString(4, cCCD);
            stmt.setString(5, hinhAnhCCCD);
            stmt.setInt(6, id); // ID của dòng cần sửa

            // Thực thi câu lệnh cập nhật
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "Cập nhật thành công!");
            } else {
                JOptionPane.showMessageDialog(null, "Không có dữ liệu nào được cập nhật!", "Thông báo",
                        JOptionPane.WARNING_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi cập nhật: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Xóa dữ liệu
    public void xoaKhachHang(int maKhachHang) {
        String sql = "DELETE FROM KhachHang WHERE IDKhachHang = ?";

        try ( Connection conn = MyConnection.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, maKhachHang);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Xóa thành công!");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi xóa: " + e.getMessage());
        }
    }
}
