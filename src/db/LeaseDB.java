package db;

import domain.Lease;
import domain.Tenant;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/** @author chang **/

public class LeaseDB {
    PreparedStatement pstmt;
    Connection con;

    public LeaseDB() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://localhost/lodging";
        String username = "root";
        String password = "abcd1234";
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
        return (result > 0);
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
        String sql = "select * from lease Ls inner join lodging Lg on Ls.LodgingID=Lg.lodgingId where Ls.Status='Active' AND Lg.userId = ?";
        pstmt = con.prepareStatement(sql);
        pstmt.setString(1, userID);
        ResultSet rs = pstmt.executeQuery();
        ArrayList<Lease> leaseList = new ArrayList<>();
        while (rs.next()) {
            Lease l = new Lease(
                rs.getString("LeaseID"),rs.getString("PaymentDueDay"),rs.getString("Status"),rs.getString("LodgingID"),rs.getDate("IssueDate")
            );
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
    
    public boolean UpdateLease(Lease l) throws Exception{
        String sql = "Update lease set PaymentDueDay=?,Status=? where LeaseID=?";
        pstmt = con.prepareStatement(sql);            
        pstmt.setString(1, l.getDueDay());
        pstmt.setString(2, l.getStatus());
        pstmt.setString(3, l.getLeaseID());
        
        int result = pstmt.executeUpdate();
        return (result > 0);
    }
    
    public int GetLeaseCount(String lodgingID) throws Exception {
        String sql = "select COUNT(*) as noOfLease from lease where Status='Active' AND LodgingID = ?";
        pstmt = con.prepareStatement(sql);
        pstmt.setString(1, lodgingID);
        ResultSet rs = pstmt.executeQuery();
        int noOfLease = 0;
        while (rs.next()) {          
            noOfLease = rs.getInt("noOfLease");
        }
        return noOfLease;
    } 
    
    public ArrayList<Tenant> isLeaseExpiry() throws Exception{
        String sql = "select * from tenant where Status='Active' AND datediff(DATE_ADD(CURDATE(), INTERVAL 1 MONTH),LeaseEnd)=0";
        pstmt = con.prepareStatement(sql); 
        ArrayList<Tenant> tenantList = new ArrayList<>();
        ResultSet rs = pstmt.executeQuery();
        while(rs.next()){
            java.util.Date strDate = new SimpleDateFormat("yyyy-MM-dd").parse("01-01-2018");
            Tenant t = new Tenant(
                rs.getString("TenantID"),rs.getString("RoomType"),rs.getString("Role"),rs.getString("Status"),rs.getString("Reason"),
                rs.getString("LeaseID"),rs.getString("UserID"),rs.getDouble("Rent"),rs.getDouble("Deposit"),rs.getDate("LeaseStart"),
                rs.getDate("LeaseEnd"),new java.sql.Date(strDate.getTime())
            );
            tenantList.add(t);
        }
        
        return tenantList;
    }
}
