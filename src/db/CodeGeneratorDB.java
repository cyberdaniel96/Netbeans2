/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import domain.CodeGenerator;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Daniel
 */
public class CodeGeneratorDB {
    PreparedStatement pstmt;
    Connection con;

    public CodeGeneratorDB() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/lodging";
        String username = "root";
        String password = "";
        con = DriverManager.getConnection(url, username, password);
    }
    
     public boolean CreateCode(CodeGenerator cg) throws Exception {
        String sql = "INSERT INTO `verification`(`VerificationID`, `IssueDate`, `IssueTime`, `VerifyCode`, `UserID`) VALUES (?,?,?,?,?)";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, cg.getVerificationID());
        pstmt.setString(2, cg.getIssueDate());
        pstmt.setString(3, cg.getIssueTime());
        pstmt.setDouble(4, cg.getVerifyCode());
        pstmt.setString(5, cg.getUserID());
        
        int result = pstmt.executeUpdate();
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }
     
     public String getNewID() throws Exception {
        String sql = "select max(VerificationID) as latestId from verification";
        PreparedStatement pstmt = con.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        boolean found = false;
        String newId = "";
        while (rs.next()) {
            String temp = rs.getString("latestId");
            if (temp != null) {
                found = true;
                temp = temp.substring(2);
                int iid = Integer.parseInt(temp) + 1;
                newId = "V" + String.format("%05d", iid);
            }
            if (!found) {
                newId = "V00000";
            }
        }
        return newId;
    }
     
     public static void main(String[] args) throws Exception {
        CodeGenerator cg = new CodeGenerator("",new Date(),new Date(),123456,"userid");
        
        CodeGeneratorDB db = new CodeGeneratorDB();
        System.out.println(db.getNewID());
        cg.setVerificationID(db.getNewID());
        db.CreateCode(cg);
     }
}
