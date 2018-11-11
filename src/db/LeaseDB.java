/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import domain.Lease;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author Daniel
 */
public class LeaseDB {
    PreparedStatement pstmt;
    Connection con;

    public LeaseDB() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/lodging";
        String username = "root";
        String password = "";
        con = DriverManager.getConnection(url, username, password);
    }
    
    public boolean CreateNewLease(Lease l) throws Exception {
        String sql = "Insert into lease(LeaseID,PaymentDueDay,IssueDate,Status,LodgingID) values(?,?,?,?,?)";
        pstmt = con.prepareStatement(sql);
        pstmt.setString(1, l.getLeaseID());
        pstmt.setString(2, l.getDueDay());
        pstmt.setDate(3, l.getIssueDate());
        pstmt.setString(4, l.getStatus());
        pstmt.setString(5, l.getLodgingID());
        int result = pstmt.executeUpdate();
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }
    
    public String NewLeaseID() throws Exception {
        String sql = "select max(LeaseID) as latestID from lease";
        PreparedStatement pstmt = con.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        boolean found = false;
        String newID = "";
        while (rs.next()) {
            String temp = rs.getString("latestID");
            if (temp != null) {
                found = true;
                temp = temp.substring(2);
                int iid = Integer.parseInt(temp) + 1;
                newID = "LS" + String.format("%05d", iid);
            }
            if (!found) {
                newID = "LS00000";
            }
        }
        return newID;
    }
    
    public ArrayList<Lease> GetUserLease(String userID) throws Exception {
        String sql = "select * from lease Ls inner join lodging Lg on Ls.LodgingID=Lg.lodgingId where Lg.userId = ?";
        pstmt = con.prepareStatement(sql);
        pstmt.setString(1, userID);
        ResultSet rs = pstmt.executeQuery();
        ArrayList<Lease> leaseList = new ArrayList<>();
        while (rs.next()) {
            Lease l = new Lease();
            l.setLeaseID(rs.getString("LeaseID"));
            l.setDueDay(rs.getString("PaymentDueDay"));
            l.setIssueDate(rs.getDate("IssueDate"));
            l.setStatus(rs.getString("Status"));
            l.setLodgingID(rs.getString("LodgingID"));

            leaseList.add(l);
        }
        return leaseList;
    }
    
    public Lease GetLease(String leaseID) throws Exception {
        String sql = "select * from lease where LeaseID = ?";
        pstmt = con.prepareStatement(sql);
        pstmt.setString(1, leaseID);
        ResultSet rs = pstmt.executeQuery();
        Lease l = new Lease();
        while (rs.next()) {          
            l.setLeaseID(rs.getString("LeaseID"));
            l.setDueDay(rs.getString("PaymentDueDay"));
            l.setIssueDate(rs.getDate("IssueDate"));
            l.setStatus(rs.getString("Status"));
            l.setLodgingID(rs.getString("LodgingID"));
        }
        return l;
    } 
}
