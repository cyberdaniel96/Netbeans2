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
        String url = "jdbc:mysql://localhostlodging";
        String username = "root";
        String password = "abcd1234";
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
