package qlpt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * Quản lý nội thất phòng DAO
 */
public class QuanLiNoiThatPhongDAO {

    public void loadDataToTable(JTable table) {
        String sql = "SELECT *FROM NoiThat_Phong";

        try ( Connection conn = MyConnection.getConnection();  PreparedStatement pstmt = conn.prepareStatement(sql);  ResultSet rs = pstmt.executeQuery()) {

            // Lấy model của table để cập nhật dữ liệu
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0); // Xóa dữ liệu cũ trong bảng

            // Đổ dữ liệu từ ResultSet vào table
            while (rs.next()) {
                int idNoiThuoc = rs.getInt("IDNoiThuoc");
                int idNoiThat = rs.getInt("IDNoiThat");
                int idPhong = rs.getInt("IDPhong");
                int soLuong = rs.getInt("SoLuong");
                String trangThai = rs.getString("TrangThai");

                model.addRow(new Object[]{idNoiThuoc, idNoiThat, idPhong, soLuong, trangThai});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private HashMap<String, Integer> phongMap = new HashMap<>();
    private HashMap<String, Integer> noiThatMap = new HashMap<>();

    // Load danh sách phòng vào ComboBox
    public void loadPhongToComboBox(JComboBox<String> cbxPhong) {
        String sql = "SELECT IDPhong, TenPhong FROM Phong";
        phongMap.clear(); // Xóa dữ liệu cũ trong HashMap

        try ( Connection conn = MyConnection.getConnection();  PreparedStatement pstmt = conn.prepareStatement(sql);  ResultSet rs = pstmt.executeQuery()) {

            cbxPhong.removeAllItems(); // Xóa dữ liệu cũ trong ComboBox
            while (rs.next()) {
                int idPhong = rs.getInt("IDPhong");
                String tenPhong = rs.getString("TenPhong");

                cbxPhong.addItem(tenPhong);
                phongMap.put(tenPhong, idPhong); // Lưu ID vào HashMap
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Lấy ID của phòng khi chọn từ ComboBox
    public int getSelectedPhongID(JComboBox<String> cbxPhong) {
        String selectedPhong = (String) cbxPhong.getSelectedItem();
        return phongMap.getOrDefault(selectedPhong, -1); // Trả về -1 nếu không tìm thấy
    }

    // Load danh sách nội thất vào ComboBox dựa theo ID phòng
    public void loadNoiThatToComboBox(JComboBox<String> cbxNoiThat) {
        String sql = "SELECT IDNoiThat, TenNoiThat FROM NoiThat";
        noiThatMap.clear(); // Xóa dữ liệu cũ trong HashMap

        try ( Connection conn = MyConnection.getConnection();  PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();

            cbxNoiThat.removeAllItems(); // Xóa dữ liệu cũ trong ComboBox
            while (rs.next()) {
                int idNoiThat = rs.getInt("IDNoiThat");
                String tenNoiThat = rs.getString("TenNoiThat");

                cbxNoiThat.addItem(tenNoiThat);
                noiThatMap.put(tenNoiThat, idNoiThat); // Lưu ID vào HashMap
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Lấy ID của nội thất khi chọn từ ComboBox
    public int getSelectedNoiThatID(JComboBox<String> cbxNoiThat) {
        String selectedNoiThat = (String) cbxNoiThat.getSelectedItem();
        return noiThatMap.getOrDefault(selectedNoiThat, -1); // Trả về -1 nếu không tìm thấy
    }

    public void themNoiThatPhong(int idNoiThat, int idPhong, int soLuong, String trangThai) {
        String sql = "INSERT INTO NoiThat_Phong (IDNoiThat, IDPhong, SoLuong, TrangThai) VALUES (?, ?, ?, ?)";

        try ( Connection conn = MyConnection.getConnection();  PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idNoiThat);
            pstmt.setInt(2, idPhong);
            pstmt.setInt(3, soLuong);
            pstmt.setString(4, trangThai);

            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Thêm nội thất vào phòng thành công!", "Thành công",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi thêm nội thất: " + e.getMessage(), "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void suaNoiThatPhong(int idNoiThatPhong, int idNoiThat, int idPhong, int soLuong, String trangThai) {
        String sql = "UPDATE NoiThat_Phong SET IDNoiThat = ?, IDPhong = ?, SoLuong = ?, TrangThai = ? WHERE IDNoiThuoc = ?";

        try ( Connection conn = MyConnection.getConnection();  PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idNoiThat);
            pstmt.setInt(2, idPhong);
            pstmt.setInt(3, soLuong);
            pstmt.setString(4, trangThai);
            pstmt.setInt(5, idNoiThatPhong); // Điều kiện WHERE

            int rowsUpdated = pstmt.executeUpdate(); // Số hàng bị ảnh hưởng
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "Cập nhật nội thất trong phòng thành công!", "Thành công",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Không tìm thấy nội thất để cập nhật!", "Thông báo",
                        JOptionPane.WARNING_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi cập nhật nội thất: " + e.getMessage(), "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void xoaNoiThatPhong(int idNoiThatPhong) {
        String sql = "DELETE FROM NoiThat_Phong WHERE IDNoiThuoc = ?";

        try ( Connection conn = MyConnection.getConnection();  PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idNoiThatPhong); // Gán giá trị ID cần xóa

            int rowsDeleted = pstmt.executeUpdate(); // Thực hiện lệnh DELETE
            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(null, "Xóa nội thất trong phòng thành công!", "Thành công",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Không tìm thấy nội thất để xóa!", "Thông báo",
                        JOptionPane.WARNING_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi xóa nội thất: " + e.getMessage(), "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

}
