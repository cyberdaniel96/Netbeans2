package db;

import domain.Verification;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/** @author chang **/

public class VerificationDB {
    PreparedStatement pstmt;
    Connection con;
    
    public VerificationDB() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/lodging";
        String username = "root";
        String password = "";
        con = DriverManager.getConnection(url, username, password);
    }
    
    public String VerifyCode(double code) throws Exception{
        String sql = "select * from verification where VerifyCode = ? AND IssueDate = CURDATE() AND (IssueTime + INTERVAL 4 MINUTE) > TIME_FORMAT(CURTIME(),'%H:%i');";
        pstmt = con.prepareStatement(sql);
        pstmt.setDouble(1, code);
        ResultSet rs = pstmt.executeQuery();
        String userID = "";
        while (rs.next()) {
            userID = rs.getString("UserID");
        }
        return userID;
    }
}
