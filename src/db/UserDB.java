package db;

import domain.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDB {

    PreparedStatement pstmt;
    Connection con;

    public UserDB() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://localhost/lodging";
        String username = "root";
        String password = "abcd1234";
        con = DriverManager.getConnection(url, username, password);
    }

    public boolean CreateNewUser(User u) throws Exception {
        String sql = "insert into user(userId,name,icNo,contactNo,email,password,userType,image) values(?,?,?,?,?,?,?,?)";
        pstmt = con.prepareStatement(sql);
        pstmt.setString(1, u.getUserId());
        pstmt.setString(2, u.getName());
        pstmt.setString(3, u.getIcNo());
        pstmt.setString(4, u.getContactNo());
        pstmt.setString(5, u.getEmail());
        pstmt.setString(6, u.getPassword());
        pstmt.setString(7, u.getUserType());
        pstmt.setString(8, u.getImage());
        int result = pstmt.executeUpdate();
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean UserIDValidation(String userId) throws Exception {
        String sql = "select * from user where userId = ?";
        pstmt = con.prepareStatement(sql);
        pstmt.setString(1, userId);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return false;
        } else {
            return true;
        }
    }

    public User GetUser(String userId) throws Exception {
        String sql = "select * from user where userId = ?";
        pstmt = con.prepareStatement(sql);
        pstmt.setString(1, userId);;
        ResultSet rs = pstmt.executeQuery();
        User u = new User();
        if (rs.next()) {
            u.setUserId(rs.getString("userId"));
            u.setName(rs.getString("name"));
            u.setIcNo(rs.getString("icNo"));
            u.setContactNo(rs.getString("contactNo"));
            u.setEmail(rs.getString("email"));
            u.setPassword(rs.getString("password"));
            u.setUserType(rs.getString("userType"));
            u.setImage(rs.getString("image"));
            return u;
        } else {
            return null;
        }
    }

    public boolean UpdateUser(User u) throws Exception {
        String sql = "update user set name=?,icNo=?,contactNo=?,email=?,password=?,userType=? where userId=?";
        pstmt = con.prepareStatement(sql);
        pstmt.setString(1, u.getName());
        pstmt.setString(2, u.getIcNo());
        pstmt.setString(3, u.getContactNo());
        pstmt.setString(4, u.getEmail());
        pstmt.setString(5, u.getPassword());
        pstmt.setString(6, u.getUserType());
        pstmt.setString(7, u.getUserId());
        int result = pstmt.executeUpdate();

        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }
}
