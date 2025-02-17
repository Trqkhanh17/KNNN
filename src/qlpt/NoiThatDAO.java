package qlpt;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import qlpt.MyConnection;

public class NoiThatDAO {

    public void loadNoiThatData(JTable table) {
        String sql = "SELECT * FROM NoiThat"; // Truy vấn dữ liệu từ bảng

        try ( Connection conn = MyConnection.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql);  ResultSet rs = stmt.executeQuery()) {

            // Lấy mô hình bảng từ JTable
            DefaultTableModel model = (DefaultTableModel) table.getModel();

            // Xóa dữ liệu cũ trong bảng
            model.setRowCount(0);

            // Định dạng ngày từ yyyy-MM-dd -> dd/MM/yy
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            // Lặp qua kết quả và thêm vào bảng
            while (rs.next()) {
                // Lấy dữ liệu ngày từ SQL (java.sql.Date)
                Date sqlDate = rs.getDate("HanBaoHanh");
                String formattedDate = (sqlDate != null) ? dateFormat.format(sqlDate) : ""; // Chuyển đổi sang dd/MM/yy

                // Thêm dữ liệu vào bảng
                model.addRow(new Object[]{
                    rs.getInt("IDNoiThat"),
                    rs.getString("TenNoiThat"),
                    rs.getDouble("Gia"),
                    formattedDate // Hiển thị ngày đã chuyển đổi
                });
            }

        } catch (SQLException e) {
            System.out.println("Lỗi khi lấy dữ liệu: " + e.getMessage());
        }
    }

    public void themNoiThat(String tenNoiThat, double gia, String hanBaoHanh) {
        String sql = "INSERT INTO NoiThat (TenNoiThat, gia, HanBaoHanh) VALUES (?, ?, ?)";

        try ( Connection conn = MyConnection.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Chuyển đổi String "dd/MM/yy" sang java.sql.Date "yyyy-MM-dd"
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yy");
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date utilDate = inputFormat.parse(hanBaoHanh);
            String formattedDate = outputFormat.format(utilDate);
            Date sqlDate = Date.valueOf(formattedDate);

            // Set giá trị vào SQL
            stmt.setString(1, tenNoiThat);
            stmt.setDouble(2, gia);
            stmt.setDate(3, sqlDate);

            // Thực thi lệnh SQL
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Thêm thành công!");
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(null, "Lỗi định dạng ngày! Định dạng đúng: dd/MM/yy");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi thêm vào CSDL: " + e.getMessage());
        }
    }

    // Sửa dữ liệu
    public void suaNoiThat(int id, String tenNoiThat, double gia, String ngayStr) {
        if (id == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn một dòng để sửa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "UPDATE NoiThat SET TenNoiThat = ?, Gia = ?, HanBaoHanh = ? WHERE IDNoiThat = ?";

        try ( Connection conn = MyConnection.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Đặt các giá trị vào câu lệnh SQL
            stmt.setString(1, tenNoiThat);
            stmt.setDouble(2, gia);

            // Chuyển đổi ngày từ chuỗi (dd/MM/yy) sang java.sql.Date
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
            java.util.Date utilDate = sdf.parse(ngayStr);
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            stmt.setDate(3, sqlDate);

            stmt.setInt(4, id); // ID của dòng cần sửa

            // Thực thi câu lệnh cập nhật
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "Cập nhật thành công!");
            } else {
                JOptionPane.showMessageDialog(null, "Không có dữ liệu nào được cập nhật!", "Thông báo",
                        JOptionPane.WARNING_MESSAGE);
            }

        } catch (SQLException | ParseException e) {
            JOptionPane.showMessageDialog(null, "Lỗi cập nhật: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Xóa dữ liệu
    public void xoaNoiThat(int maNoiThat) {
        String sql = "DELETE FROM NoiThat WHERE IDNoiThat=?";

        try ( Connection conn = MyConnection.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, maNoiThat);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Xóa thành công!");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi xóa: " + e.getMessage());
        }
    }
}
