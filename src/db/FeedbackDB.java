/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import domain.Feedback;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author johnn
 */
public class FeedbackDB {
    
    PreparedStatement pstmt;
    Connection con;

    public FeedbackDB() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/lodging";
        String username = "root";
        String password = "";
        con = DriverManager.getConnection(url, username, password);
    }
    
    public String NewFeedbackId() throws Exception {
        String sql = "select max(feedbackId) as latestId from feedback";
        PreparedStatement pstmt = con.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        boolean found = false;
        String newId = "";
        while (rs.next()) {
            String temp = rs.getString("latestId");
            if (temp != null) {
                found = true;
                temp = temp.substring(1);
                int iid = Integer.parseInt(temp) + 1;
                newId = "B" + String.format("%05d", iid);
            }
            if (!found) {
                newId = "B00000";
            }
        }
        return newId;
    }
    
    public boolean AddFeedback(Feedback f) throws Exception {
        String sql = "insert into feedback(feedbackId,email,feedbackType,feedback) values(?,?,?,?)";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, f.getFeedbackId());
        pstmt.setString(2, f.getEmail());
        pstmt.setString(3, f.getFeedbackType());
        pstmt.setString(4, f.getFeedback());
        int result = pstmt.executeUpdate();
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }
    
}
