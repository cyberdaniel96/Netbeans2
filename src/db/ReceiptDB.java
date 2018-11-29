package db;

import domain.Receipt;
import domain.Rental;
import domain.Tenant;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/** @author chang **/

public class ReceiptDB {
    PreparedStatement pstmt;
    Connection con;
    
    public ReceiptDB() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://localhost/lodging";
        String username = "root";
        String password = "abcd1234";
        con = DriverManager.getConnection(url, username, password);
    }
    
    public void AddNewReceipt(Receipt r) throws Exception {
        String sql = "Insert into receipt(ReceiptID,Amount,Image,PayStatus,ReceiveStatus,Reason,Status,DateTime,TenantID,RentalID) values(?,?,NULL,?,?,'N/A',?,NULL,?,?)";
        pstmt = con.prepareStatement(sql);
        pstmt.setString(1, r.getReceiptID());
        pstmt.setDouble(2, r.getAmount());
        pstmt.setString(3, "UnPaid");
        pstmt.setString(4, "NotReceive");
        pstmt.setString(5, "Pending");
        pstmt.setString(6, r.getTenantID());
        pstmt.setString(7, r.getRentalID());
        pstmt.executeUpdate();
    }
    
    public String NewReceiptID() throws Exception {
        String sql = "select max(ReceiptID) as latestId from receipt";
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
                newID = "RC" + String.format("%05d", iid);
            }
            if (!found) {
                newID = "RC00000";
            }
        }
        return newID;
    }
    
    public ArrayList<Receipt> GetAllReceipt(String rentalID) throws Exception {
        String sql = "select * from receipt where RentalID = ?";
        pstmt = con.prepareStatement(sql);
        pstmt.setString(1, rentalID);
        ResultSet rs = pstmt.executeQuery();
        ArrayList<Receipt> receiptList = new ArrayList<>();
        while (rs.next()) {
            Receipt r = new Receipt(
                    rs.getString("ReceiptID"),rs.getString("Image"),rs.getString("PayStatus"),rs.getString("ReceiveStatus"),rs.getString("Reason"),
                    rs.getString("Status"),rs.getString("TenantID"),rs.getString("RentalID"),rs.getDouble("Amount"), new Timestamp(Calendar.getInstance().getTime().getTime())
            );
            receiptList.add(r);
        }
        return receiptList;
    }
    
    public boolean UpdateReceipt(Receipt r) throws Exception{
        String sql = "Update receipt set Amount=?,Image=?,PayStatus=?,ReceiveStatus=?,Reason=?,Status=?,DateTime=?,TenantID=?,RentalID=? where ReceiptID=?";
        pstmt = con.prepareStatement(sql);            
        pstmt.setDouble(1, r.getAmount());
        pstmt.setString(2, r.getImage());
        pstmt.setString(3, r.getPayStatus());
        pstmt.setString(4, r.getReceiveStatus());
        pstmt.setString(5, r.getReason());
        pstmt.setString(6, r.getStatus());
        pstmt.setTimestamp(7, r.getDateTime());
        pstmt.setString(8, r.getTenantID());
        pstmt.setString(9, r.getRentalID());
        pstmt.setString(10, r.getReceiptID());   
        
        int result = pstmt.executeUpdate();
        return (result > 0);
    }  
   
    public double UpdateAmount(List<Tenant> tenantList, double sum, Rental r) throws Exception{
        String sql = "";
        double totalRental = sum;
        double fee = sum/tenantList.size();
        for(int i = 0; i < tenantList.size(); i++){
            sql = "Update receipt set Amount=? where TenantID=? AND RentalID=?";
            pstmt = con.prepareStatement(sql);            
            pstmt.setDouble(1, tenantList.get(i).getRent() + fee);
            pstmt.setString(2, tenantList.get(i).getTenantID());
            pstmt.setString(3, r.getRentalID());
            pstmt.executeUpdate();
            
            totalRental += tenantList.get(i).getRent();
        } 
        return totalRental;
    }  
    
    public Receipt GetReceipt(String tenantID, String rentalID) throws Exception {
        String sql = "select * from receipt where TenantID=? AND RentalID = ?";
        pstmt = con.prepareStatement(sql);
        pstmt.setString(1, tenantID);
        pstmt.setString(2, rentalID);
        ResultSet rs = pstmt.executeQuery();
        Receipt r = null;
        while (rs.next()) {           
            if(rs.getString("PayStatus").equals("Paid")){
                r = new Receipt(
                    rs.getString("ReceiptID"),rs.getString("Image"),rs.getString("PayStatus"),rs.getString("ReceiveStatus"),rs.getString("Reason"),
                    rs.getString("Status"),rs.getString("TenantID"),rs.getString("RentalID"),rs.getDouble("Amount"),rs.getTimestamp("DateTime")
                );
            }else{
                r = new Receipt(
                    rs.getString("ReceiptID"),rs.getString("Image"),rs.getString("PayStatus"),rs.getString("ReceiveStatus"),rs.getString("Reason"),
                    rs.getString("Status"),rs.getString("TenantID"),rs.getString("RentalID"),rs.getDouble("Amount"), new Timestamp(Calendar.getInstance().getTime().getTime())
                );
            }           
        }
        
        return r;
    } 
    
    public Receipt GetSelectedReceipt(String receiptID) throws Exception {
        String sql = "select * from receipt where ReceiptID = ?";
        pstmt = con.prepareStatement(sql);
        pstmt.setString(1, receiptID);
        ResultSet rs = pstmt.executeQuery();
        Receipt r = new Receipt();
        while (rs.next()) {           
            r.setReceiptID(rs.getString("ReceiptID"));
            r.setAmount(rs.getDouble("Amount"));
            r.setImage(rs.getString("Image"));
            r.setPayStatus(rs.getString("PayStatus"));
            r.setReceiveStatus(rs.getString("ReceiveStatus"));
            r.setReason(rs.getString("Reason"));
            r.setStatus(rs.getString("Status"));
            r.setTenantID(rs.getString("TenantID"));
            r.setRentalID(rs.getString("RentalID"));
        }
        
        return r;
    } //never use
    
    public boolean UpdateTenantReceipt(Receipt r) throws Exception{
        String sql = "";

        sql = "Update receipt set Image = ?, PayStatus = ?, DateTime = CURDATE() where ReceiptID = ?";
        pstmt = con.prepareStatement(sql);
        
        pstmt.setString(1, r.getImage());
        pstmt.setString(2, r.getPayStatus());
        pstmt.setString(3, r.getReceiptID());
        int result = pstmt.executeUpdate();

        return result > 0;
    }

    
    
}
