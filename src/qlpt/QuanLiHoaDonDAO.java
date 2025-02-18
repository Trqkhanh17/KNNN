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
import java.util.HashMap;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author khanh
 */
public class QuanLiHoaDonDAO {

    private HashMap<String, Integer> phongMap = new HashMap<>();

    public void loadPhongToComboBox(JComboBox<String> cbxPhong) {
        String sql = "SELECT IDPhong, TenPhong FROM Phong";
        phongMap.clear(); // Xóa dữ liệu cũ trong HashMap

        try (Connection conn = MyConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            cbxPhong.removeAllItems(); // Xóa dữ liệu cũ trong ComboBox
            while (rs.next()) {
                int idPhong = rs.getInt("IDPhong");
                String tenPhong = rs.getString("TenPhong");

                cbxPhong.addItem(tenPhong);
                phongMap.put(tenPhong, idPhong); // Lưu ID vào HashMap
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MyConnection.closeConnection();
        }
    }

    // Lấy ID của phòng khi chọn từ ComboBox
    public int getSelectedPhongID(JComboBox<String> cbxPhong) {
        String selectedPhong = (String) cbxPhong.getSelectedItem();
        return phongMap.getOrDefault(selectedPhong, -1); // Trả về -1 nếu không tìm thấy
    }

    public void loadHoaDonData(JTable table) {
        String sql = "SELECT h.IDHoaDon, p.TenPhong, h.NgayLap, h.TienDien, h.TienNuoc, h.PhiDichVu, h.TongTien, h.MoTa, h.TrangThai,p.IDPhong "
                + "FROM HoaDon h JOIN Phong p ON h.IDPhong = p.IDPhong";

        try (Connection conn = MyConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            // Lấy mô hình bảng từ JTable
            DefaultTableModel model = (DefaultTableModel) table.getModel();

            // Xóa dữ liệu cũ trong bảng
            model.setRowCount(0);

            // Định dạng ngày từ yyyy-MM-dd -> dd/MM/yyyy
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            // Lặp qua kết quả và thêm vào bảng
            while (rs.next()) {
                // Lấy dữ liệu ngày từ SQL (java.sql.Date)
                Date sqlDate = rs.getDate("NgayLap");
                String formattedDate = (sqlDate != null) ? dateFormat.format(sqlDate) : ""; // Chuyển đổi sang
                // dd/MM/yyyy

                // Thêm dữ liệu vào bảng
                model.addRow(new Object[] {
                        rs.getInt("IDHoaDon"), // ID hóa đơn
                        rs.getString("TenPhong"), // Tên phòng
                        formattedDate, // Ngày lập (đã format)
                        rs.getDouble("TienDien"), // Tiền điện
                        rs.getDouble("TienNuoc"), // Tiền nước
                        rs.getDouble("PhiDichVu"), // Phí dịch vụ
                        rs.getDouble("TongTien"), // Tổng tiền
                        rs.getString("MoTa"),
                        rs.getString("TrangThai"), // Mô tả
                        rs.getInt("IDPhong")
                });
            }
            if (table.getColumnModel().getColumnCount() > 0) {
                int lastColumnIndex = table.getColumnCount() - 1;
                table.getColumnModel().getColumn(lastColumnIndex).setMinWidth(0);
                table.getColumnModel().getColumn(lastColumnIndex).setMaxWidth(0);
                table.getColumnModel().getColumn(lastColumnIndex).setWidth(0);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi lấy dữ liệu hóa đơn: " + e.getMessage());
        } finally {
            MyConnection.closeConnection();
        }
    }

    public void themHoaDon(int idPhong, Date ngayLap, double tienDien, double tienNuoc, double phiDichVu,
            double tongTien, String moTa, String trangThai) {
        String sql = "INSERT INTO HoaDon (idPhong, ngayLap, tienDien, tienNuoc, phiDichVu, tongTien, moTa,TrangThai) VALUES (?,?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = MyConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Chuyển java.util.Date thành java.sql.Date
            java.sql.Date sqlDate = new java.sql.Date(ngayLap.getTime());

            // Set giá trị vào SQL
            stmt.setInt(1, idPhong);
            stmt.setDate(2, sqlDate); // Sử dụng đối tượng java.sql.Date
            stmt.setDouble(3, tienDien);
            stmt.setDouble(4, tienNuoc);
            stmt.setDouble(5, phiDichVu);
            stmt.setDouble(6, tongTien);
            stmt.setString(7, moTa);
            stmt.setString(8, trangThai);
            // Thực thi lệnh SQL
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Thêm hóa đơn thành công!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi thêm vào CSDL: " + e.getMessage());
        }
    }

    public double getGiaPhong(int idPhong) {
        double giaPhong = 0;
        String sql = "SELECT Gia FROM Phong WHERE idPhong = ?";

        try (Connection conn = MyConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPhong);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    giaPhong = rs.getDouble("Gia");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi truy vấn giá phòng: " + e.getMessage());
        }
        MyConnection.closeConnection();

        return giaPhong;
    }

    public void suaHoaDon(int id, java.sql.Date ngayLap, double tienDien, double tienNuoc, double phiDichVu,
            double tongTien, String moTa, String trangThai) {
        String sql = "UPDATE HoaDon SET ngayLap = ?, tienDien = ?, tienNuoc = ?, phiDichVu = ?, tongTien = ?, moTa = ?, trangThai = ? WHERE idPhong = ?";

        try (Connection conn = MyConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, ngayLap);
            stmt.setDouble(2, tienDien);
            stmt.setDouble(3, tienNuoc);
            stmt.setDouble(4, phiDichVu);
            stmt.setDouble(5, tongTien);
            stmt.setString(6, moTa);
            stmt.setString(7, trangThai);
            stmt.setInt(8, id);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void xoaHoaDon(JTable table) {
        int selectedRow = table.getSelectedRow(); // Lấy dòng được chọn

        if (selectedRow == -1) { // Kiểm tra nếu chưa chọn dòng nào
            JOptionPane.showMessageDialog(null, "Vui lòng chọn một hóa đơn để xóa!", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        DefaultTableModel model = (DefaultTableModel) table.getModel();
        int idHoaDon = Integer.parseInt(model.getValueAt(selectedRow, 0).toString()); // Lấy ID hóa đơn từ cột 0

        int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn xóa hóa đơn này?", "Xác nhận",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM HoaDon WHERE IDHoaDon = ?";

            try (Connection conn = MyConnection.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, idHoaDon);
                int affectedRows = stmt.executeUpdate(); // Thực hiện lệnh xóa

                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(null, "Xóa hóa đơn thành công!", "Thông báo",
                            JOptionPane.INFORMATION_MESSAGE);
                    model.removeRow(selectedRow); // Xóa dòng khỏi bảng
                } else {
                    JOptionPane.showMessageDialog(null, "Không thể xóa hóa đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Lỗi khi xóa hóa đơn: " + e.getMessage(), "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            } finally {
                MyConnection.closeConnection();
            }
        }
    }

}
