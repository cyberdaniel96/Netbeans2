/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import domain.Tenant;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Daniel
 */

public class TenantDB {
    PreparedStatement pstmt;
    Connection con;

    public TenantDB() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/lodging";
        String username = "root";
        String password = "";
        con = DriverManager.getConnection(url, username, password);
    }
    
    public void AddNewTenant(Tenant t) throws Exception {
        String sql = "Insert into tenant(TenantID,RoomType,Role,LeaseStart,LeaseEnd,Rent,Deposit,Status,BreakDate,Reason,UserID,LeaseID) values(?,?,?,?,?,?,?,?,NULL,NULL,?,?)";
        pstmt = con.prepareStatement(sql);
        pstmt.setString(1, t.getTenantID());
        pstmt.setString(2, t.getRoomType());
        pstmt.setString(3, t.getRole());
        pstmt.setDate(4, t.getLeaseStart());
        pstmt.setDate(5, t.getLeaseEnd());
        pstmt.setDouble(6, t.getRent());
        pstmt.setDouble(7, t.getDeposit());
        pstmt.setString(8, "Pending");
        pstmt.setString(9, t.getUserID());
        pstmt.setString(10, t.getLeaseID());
        pstmt.executeUpdate();
    }
    
    public String NewTenantID() throws Exception {
        String sql = "select max(tenantId) as latestId from tenant";
        PreparedStatement pstmt = con.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        boolean found = false;
        String newID = "";
        while (rs.next()) {
            String temp = rs.getString("latestId");
            if (temp != null) {
                found = true;
                temp = temp.substring(1);
                int iid = Integer.parseInt(temp) + 1;
                newID = "T" + String.format("%05d", iid);
            }
            if (!found) {
                newID = "T00000";
            }
        }
        return newID;
    }
    
    public ArrayList<Tenant> GetAllTenant(String leaseID) throws Exception {
        String sql = "select * from tenant where Status='Active' AND LeaseID = ?";
        pstmt = con.prepareStatement(sql);
        pstmt.setString(1, leaseID);
        ResultSet rs = pstmt.executeQuery();
        ArrayList<Tenant> tenantList = new ArrayList<>();
        while (rs.next()) {
            Tenant t = new Tenant();
            t.setTenantID(rs.getString("TenantID"));
            t.setRoomType(rs.getString("RoomType"));
            t.setRole(rs.getString("Role"));
            t.setLeaseStart(rs.getDate("LeaseStart"));
            t.setLeaseEnd(rs.getDate("LeaseEnd"));
            t.setRent(rs.getDouble("Rent"));
            t.setDeposit(rs.getDouble("Deposit"));
            t.setStatus(rs.getString("Status"));
            if(rs.getString("Status").equals("Terminate")){
                t.setBreakDate(rs.getDate("BreakDate"));
                t.setReason(rs.getString("Reason"));
            }
            t.setUserID(rs.getString("UserID"));
            t.setLeaseID(rs.getString("LeaseID"));

            tenantList.add(t);
        }
        return tenantList;
    }
    
    public boolean UpdateTenant(Tenant t) throws Exception{
        String sql = "";
        if(t.getStatus().equals("Terminated")){
            sql = "Update tenant set RoomType=?,Role=?,LeaseStart=?,LeaseEnd=?,Rent=?,Deposit=?,Status=?,BreakDate=?,Reason=? where TenantID=?";
            pstmt = con.prepareStatement(sql);            
            pstmt.setString(1, t.getRoomType());
            pstmt.setString(2, t.getRole());
            pstmt.setDate(3, t.getLeaseStart());
            pstmt.setDate(4, t.getLeaseEnd());
            pstmt.setDouble(5, t.getRent());
            pstmt.setDouble(6, t.getDeposit());
            pstmt.setString(7, t.getStatus());
            pstmt.setDate(8, t.getBreakDate());
            pstmt.setString(9, t.getReason());
            pstmt.setString(10, t.getTenantID());
        }else{
            sql = "Update tenant set RoomType=?,Role=?,LeaseStart=?,LeaseEnd=?,Rent=?,Deposit=?,Status=? where TenantID=?";
            pstmt = con.prepareStatement(sql);            
            pstmt.setString(1, t.getRoomType());
            pstmt.setString(2, t.getRole());
            pstmt.setDate(3, t.getLeaseStart());
            pstmt.setDate(4, t.getLeaseEnd());
            pstmt.setDouble(5, t.getRent());
            pstmt.setDouble(6, t.getDeposit());
            pstmt.setString(7, t.getStatus());
            pstmt.setString(8, t.getTenantID());
        }    
        
        int result = pstmt.executeUpdate();

        return result > 0;
    }
    
    public Tenant GetSelectedTenant(String tenantID) throws Exception {
        String sql = "select * from tenant where TenantID = ?";
        pstmt = con.prepareStatement(sql);
        pstmt.setString(1, tenantID);
        ResultSet rs = pstmt.executeQuery();
        Tenant t = new Tenant();
        while (rs.next()) {           
            t.setTenantID(rs.getString("TenantID"));
            t.setRoomType(rs.getString("RoomType"));
            t.setRole(rs.getString("Role"));
            t.setLeaseStart(rs.getDate("LeaseStart"));
            t.setLeaseEnd(rs.getDate("LeaseEnd"));
            t.setRent(rs.getDouble("Rent"));
            t.setDeposit(rs.getDouble("Deposit"));
            t.setStatus(rs.getString("Status"));
            t.setBreakDate(rs.getDate("BreakDate"));
            t.setReason(rs.getString("Reason"));
            t.setUserID(rs.getString("UserID"));
            t.setLeaseID(rs.getString("LeaseID"));
        }
        
        return t;
    }
    
    public ArrayList<Tenant> GetTenantViaUserID(String userID) throws Exception{
        String sql = "select * from tenant where (Status != 'Terminated' or Status != 'Inactive') AND UserID = ?";
        pstmt = con.prepareStatement(sql);
        pstmt.setString(1, userID);
        ResultSet rs = pstmt.executeQuery();
        ArrayList<Tenant> tenantList = new ArrayList<>();
        while (rs.next()) {
            Tenant t = new Tenant();
            t.setTenantID(rs.getString("TenantID"));
            t.setRoomType(rs.getString("RoomType"));
            t.setRole(rs.getString("Role"));
            t.setLeaseStart(rs.getDate("LeaseStart"));
            t.setLeaseEnd(rs.getDate("LeaseEnd"));
            t.setRent(rs.getDouble("Rent"));
            t.setDeposit(rs.getDouble("Deposit"));
            t.setStatus(rs.getString("Status"));
            
            t.setBreakDate(rs.getDate("BreakDate"));
            t.setReason(rs.getString("Reason"));
            
            t.setUserID(rs.getString("UserID"));
            t.setLeaseID(rs.getString("LeaseID"));

            tenantList.add(t);
        }
        return tenantList;
    }
    
     public Tenant GetSelectedTenantWithLeaseID(String tenantID,String leaseID) throws Exception {
        String sql = "select * from tenant where UserID = ? AND LeaseID = ?";
        pstmt = con.prepareStatement(sql);
        pstmt.setString(1, tenantID);
        pstmt.setString(2, leaseID);
        ResultSet rs = pstmt.executeQuery();
        Tenant t = new Tenant();
        while (rs.next()) {           
            t.setTenantID(rs.getString("TenantID"));
            t.setRoomType(rs.getString("RoomType"));
            t.setRole(rs.getString("Role"));
            t.setLeaseStart(rs.getDate("LeaseStart"));
            t.setLeaseEnd(rs.getDate("LeaseEnd"));
            t.setRent(rs.getDouble("Rent"));
            t.setDeposit(rs.getDouble("Deposit"));
            t.setStatus(rs.getString("Status"));
            if(rs.getDate("BreakDate") == null){
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date javaDate = sdf.parse("06/10/2013 18:29:09");
                t.setBreakDate(new java.sql.Date(javaDate.getTime()));
            }else{
                t.setBreakDate(rs.getDate("BreakDate"));
            }
            t.setReason(rs.getString("Reason"));
            t.setUserID(rs.getString("UserID"));
            t.setLeaseID(rs.getString("LeaseID"));
        }
        
        return t;
    }
    
     public boolean UpdateTenantStatus(Tenant t) throws Exception{
         String sql = "";
         
         if(t.getStatus().equals("Rejected")){
             sql = "Update tenant set Status=?,Reason=? where UserID=? AND LeaseID = ?";
             pstmt = con.prepareStatement(sql);            
             pstmt.setString(1, t.getStatus());
             pstmt.setString(2, t.getReason());
             pstmt.setString(3, t.getUserID());
             pstmt.setString(4, t.getLeaseID());
         }
         
         int result = pstmt.executeUpdate();

         return result > 0;
     }
}