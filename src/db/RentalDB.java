package db;

import domain.Receipt;
import domain.Rental;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/** @author chang **/

public class RentalDB {
    PreparedStatement pstmt;
    Connection con;
    
    public RentalDB() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://localhost/lodging";
        String username = "root";
        String password = "abcd1234";
        con = DriverManager.getConnection(url, username, password);
    }
    
    public boolean CreateNewRental(Rental r) throws Exception {
        String sql = "Insert into rental(RentalID,IssueDate,DueDate,TotalAmount,Status,LeaseID) values(?,?,?,?,?,?)";
        pstmt = con.prepareStatement(sql);
        pstmt.setString(1, r.getRentalID());
        pstmt.setDate(2, r.getIssueDate());
        pstmt.setDate(3, r.getDueDate());
        pstmt.setDouble(4, r.getTotalAmount());
        pstmt.setString(5, r.getStatus());
        pstmt.setString(6, r.getLeaseID());
        
        int result = pstmt.executeUpdate();       
        return (result > 0);
    }
    
    public String NewRentalID() throws Exception {
        String sql = "select max(RentalID) as latestId from rental";
        PreparedStatement pstmt = con.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        boolean found = false;
        String newID = "";
        while (rs.next()) {
            String temp = rs.getString("latestId");
            if (temp != null) {
                found = true;
                temp = temp.substring(2);
                int iid = Integer.parseInt(temp) + 1;
                newID = "RT" + String.format("%05d", iid);
            }
            if (!found) {
                newID = "RT00000";
            }
        }
        return newID;
    }
    
    public ArrayList<Rental> GetAllRental(String leaseID) throws Exception {
        String sql = "select * from rental where LeaseID=?";
        pstmt = con.prepareStatement(sql);
        pstmt.setString(1, leaseID);
        ResultSet rs = pstmt.executeQuery();
        ArrayList<Rental> rentalList = new ArrayList<>();
        while (rs.next()) {
            Rental r = new Rental(
                rs.getString("RentalID"),rs.getString("Status"),rs.getString("LeaseID"),rs.getDate("IssueDate"),rs.getDate("DueDate"),Double.parseDouble(rs.getString("TotalAmount"))
            );                   
            rentalList.add(r);
        }
        return rentalList;
    }
    
    public Rental GetRental(String rentalID) throws Exception {
        String sql = "select * from rental where RentalID=?";
        pstmt = con.prepareStatement(sql);
        pstmt.setString(1, rentalID);
        ResultSet rs = pstmt.executeQuery();
        Rental r = null;
        while (rs.next()) {         
            r = new Rental(
                rs.getString("RentalID"),rs.getString("Status"),rs.getString("LeaseID"),rs.getDate("IssueDate"),rs.getDate("DueDate"),Double.parseDouble(rs.getString("TotalAmount"))
            );                     
        }
        return r;
    }
    
    public boolean UpdateRental(Rental r) throws Exception{
        String sql = "Update rental set IssueDate=?,TotalAmount=?,Status=?,LeaseID=? where RentalID=?";
        pstmt = con.prepareStatement(sql);            
        pstmt.setDate(1, r.getIssueDate());
        pstmt.setDouble(2, r.getTotalAmount());
        pstmt.setString(3, r.getStatus());
        pstmt.setString(4, r.getLeaseID()); 
        pstmt.setString(5, r.getRentalID());
        
        int result = pstmt.executeUpdate();
        return (result > 0);
    }
    
    public ArrayList<Receipt> isRentalExipy() throws Exception{
        String sql = "select * from receipt Rc inner join rental R where Rc.RentalID=R.RentalID AND R.Status='Active' AND datediff(R.DueDate,curdate())<=4 AND Rc.PayStatus='UnPaid'";
        pstmt = con.prepareStatement(sql); 
        ArrayList<Receipt> receiptList = new ArrayList<>();
        ResultSet rs = pstmt.executeQuery();
        while(rs.next()){
            Receipt rc = new Receipt(
                    rs.getString("ReceiptID"),rs.getString("Image"),rs.getString("PayStatus"),rs.getString("ReceiveStatus"),
                    "",rs.getString("Status"),rs.getString("TenantID"),rs.getString("RentalID"),rs.getDouble("Amount"),rs.getTimestamp("DateTime")
            );
            receiptList.add(rc);
        }
        return receiptList;
    }
    
     public ArrayList GetRentals(String leaseID) throws Exception {
        String sql = "select * from rental where LeaseID=?";
        pstmt = con.prepareStatement(sql);
        pstmt.setString(1, leaseID);
        ResultSet rs = pstmt.executeQuery();
        Rental r = null;
        ArrayList<Rental> list = new ArrayList<>();
        while (rs.next()) {         
            r = new Rental(
                rs.getString("RentalID"),rs.getString("Status"),rs.getString("LeaseID"),rs.getDate("IssueDate"),rs.getDate("DueDate"),Double.parseDouble(rs.getString("TotalAmount"))
            );
            list.add(r);
        }
        return list;
    }
}
