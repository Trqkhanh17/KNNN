/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package qlpt;

import java.sql.Connection;
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
import javax.swing.table.TableColumnModel;

/**
 *
 * @author LENOVO
 */
public class HopDongDAO {
    public static void loadHopDongData(JTable table) {
        // Câu truy vấn sử dụng JOIN để lấy dữ liệu từ các bảng liên quan
        String sql = "SELECT h.IDHopDong, p.TenPhong, k.HoVaTen, k.CCCD, " +
                "h.NgayLap, h.TienCoc, h.ThoiHan, h.SoNguoiO, h.SoLuongXe, h.TrangThai, h.HinhAnhDinhKem, " +
                "h.IDPhong, h.IDKhachHang " +
                "FROM HopDong h " +
                "JOIN KhachHang k ON h.IDKhachHang = k.IDKhachHang " +
                "JOIN Phong p ON h.IDPhong = p.IDPhong";

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
                model.addRow(new Object[] {
                        rs.getInt("IDHopDong"), // ID Hợp đồng
                        rs.getString("TenPhong"), // Tên phòng
                        rs.getString("HoVaTen"), // Tên khách hàng
                        rs.getString("CCCD"), // CCCD khách hàng
                        dateFormat.format(rs.getDate("NgayLap")), // Ngày lập (định dạng)
                        rs.getString("TienCoc"), // Tiền cọc
                        dateFormat.format(rs.getDate("ThoiHan")), // Thời hạn
                        rs.getInt("SoNguoiO"), // Số người ở
                        rs.getInt("SoLuongXe"), // Số lượng xe
                        rs.getString("TrangThai"), // Trạng thái hợp đồng
                        rs.getString("HinhAnhDinhKem"), // Hình ảnh đính kèm
                        rs.getInt("IDPhong"), // ID Phòng
                        rs.getInt("IDKhachHang") // ID Khách hàng
                });
            }

            // Ẩn cột IDPhong và IDKhachHang trên JTable
            TableColumnModel columnModel = table.getColumnModel();
            columnModel.getColumn(11).setMinWidth(0);
            columnModel.getColumn(11).setMaxWidth(0);
            columnModel.getColumn(11).setWidth(0);
            columnModel.getColumn(12).setMinWidth(0);
            columnModel.getColumn(12).setMaxWidth(0);
            columnModel.getColumn(12).setWidth(0);
        } catch (SQLException e) {
            System.out.println("Lỗi khi lấy dữ liệu hợp đồng: " + e.getMessage());
        }
    }

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

    private HashMap<String, Integer> khachHangMap = new HashMap<>();

    public void loadKhachHangToComboBox(JComboBox<String> cbxKhachHang) {
        String sql = "SELECT IDKhachHang, CCCD FROM KhachHang";
        khachHangMap.clear(); // Xóa dữ liệu cũ trong HashMap

        try (Connection conn = MyConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            cbxKhachHang.removeAllItems(); // Xóa dữ liệu cũ trong ComboBox
            while (rs.next()) {
                int idKhachHang = rs.getInt("IDKhachHang");
                String cccd = rs.getString("CCCD");

                cbxKhachHang.addItem(cccd);
                khachHangMap.put(cccd, idKhachHang); // Lưu ID vào HashMap
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

    // Lấy ID của phòng khi chọn từ ComboBox
    public int getSelectedKhachHangID(JComboBox<String> cbxKhachHang) {
        String selectedKhachHang = (String) cbxKhachHang.getSelectedItem();
        return khachHangMap.getOrDefault(selectedKhachHang, -1); // Trả về -1 nếu không tìm thấy
    }

    // them du lieu
    public void themHopDong(int idPhong, int idKhachHang, java.sql.Date ngayLap, String tienCoc,
            java.sql.Date thoiHan, int soNguoiO, int soLuongXe,
            String trangThai, String hinhAnh) {
        String sql = "INSERT INTO HopDong (IDPhong, IDKhachHang, NgayLap, TienCoc, ThoiHan, SoNguoiO, SoLuongXe, TrangThai, HinhAnhDinhKem) "
                +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = MyConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPhong);
            stmt.setInt(2, idKhachHang);
            stmt.setDate(3, ngayLap); // Truyền vào kiểu Date hợp lệ
            stmt.setString(4, tienCoc);
            stmt.setDate(5, thoiHan); // Truyền vào kiểu Date hợp lệ
            stmt.setInt(6, soNguoiO);
            stmt.setInt(7, soLuongXe);
            stmt.setString(8, trangThai);
            stmt.setString(9, hinhAnh);

            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(null, "Thêm hợp đồng thành công!", "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Không thể thêm hợp đồng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi thêm hợp đồng: " + e.getMessage(), "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Chuyển đổi ngày từ chuỗi sang java.sql.Date, hỗ trợ cả dd/MM/yyyy và
     * yyyy-MM-dd.
     */
    // private java.sql.Date convertToDate(String dateString) throws ParseException
    // {
    // SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
    // SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
    //
    // java.util.Date utilDate;
    // if (dateString.contains("/")) {
    // utilDate = format1.parse(dateString); // Định dạng dd/MM/yyyy
    // } else {
    // utilDate = format2.parse(dateString); // Định dạng yyyy-MM-dd
    // }
    //
    // return new java.sql.Date(utilDate.getTime());
    // }

    // Sửa dữ liệu
    public void suaHopDong(int idHopDong, int idPhong, int idKhachHang, java.sql.Date ngayLap,
            String tienCoc, java.sql.Date thoiHan, int soNguoiO, int soLuongXe,
            String trangThai, String hinhAnh) {
        String sql = "UPDATE HopDong SET IDPhong = ?, IDKhachHang = ?, NgayLap = ?, TienCoc = ?, "
                + "ThoiHan = ?, SoNguoiO = ?, SoLuongXe = ?, TrangThai = ?, HinhAnhDinhKem = ? "
                + "WHERE IDHopDong = ?";

        try (Connection conn = MyConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPhong);
            stmt.setInt(2, idKhachHang);
            stmt.setDate(3, ngayLap);
            stmt.setString(4, tienCoc);
            stmt.setDate(5, thoiHan);
            stmt.setInt(6, soNguoiO);
            stmt.setInt(7, soLuongXe);
            stmt.setString(8, trangThai);
            stmt.setString(9, hinhAnh);
            stmt.setInt(10, idHopDong);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "Cập nhật hợp đồng thành công!", "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Không thể cập nhật hợp đồng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi cập nhật hợp đồng: " + e.getMessage(), "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Xóa dữ liệu
    public void xoaHopDong(JTable table) {
        int selectedRow = table.getSelectedRow(); // Lấy dòng được chọn

        if (selectedRow == -1) { // Kiểm tra nếu chưa chọn dòng nào
            JOptionPane.showMessageDialog(null, "Vui lòng chọn một hợp đồng để xóa!", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        DefaultTableModel model = (DefaultTableModel) table.getModel();
        int idHopDong = Integer.parseInt(model.getValueAt(selectedRow, 0).toString()); // Lấy ID hợp đồng từ cột 0

        int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn xóa hợp đồng này?", "Xác nhận",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM HopDong WHERE IDHopDong = ?";

            try (Connection conn = MyConnection.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, idHopDong);
                int affectedRows = stmt.executeUpdate(); // Thực hiện lệnh xóa

                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(null, "Xóa hợp đồng thành công!", "Thông báo",
                            JOptionPane.INFORMATION_MESSAGE);
                    model.removeRow(selectedRow); // Xóa dòng khỏi bảng
                } else {
                    JOptionPane.showMessageDialog(null, "Không thể xóa hợp đồng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Lỗi khi xóa hợp đồng: " + e.getMessage(), "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            } finally {
                MyConnection.closeConnection();
            }
        }
    }

}
