package db;

import domain.VisitTime;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/** @author chang **/

public class VisitTimeDB {
    PreparedStatement pstmt;
    Connection con;
    
    public VisitTimeDB() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://localhost/lodging";
        String username = "root";
        String password = "abcd1234";
        con = DriverManager.getConnection(url, username, password);
    }
    
    public boolean AddVisitTime(VisitTime vt) throws Exception {
        String sql = "Insert into visittime(TimeID,VisitDateTime,Status,UserID,LodgingID) values(?,?,?,?,?)";
        pstmt = con.prepareStatement(sql);
        pstmt.setString(1, vt.getTimeID());
        pstmt.setTimestamp(2, vt.getVisitDateTime());
        pstmt.setString(3, "Active");
        pstmt.setString(4, vt.getUserID());
        pstmt.setString(5, vt.getLodgingID());
        int result = pstmt.executeUpdate();
        return (result > 0);
    }
    
    public String NewTimeID() throws Exception {
        String sql = "select max(TimeID) as latestId from visittime";
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
                newID = "VT" + String.format("%05d", iid);
            }
            if (!found) {
                newID = "VT00000";
            }
        }
        return newID;
    } 
    
    public ArrayList<VisitTime> GetAllVisitTime(String LodgingID) throws Exception{
        String sql = "select * from visittime where Status='Active' AND LodgingID=? Group by VisitDateTime";
        pstmt = con.prepareStatement(sql);
        pstmt.setString(1, LodgingID);
        ResultSet rs = pstmt.executeQuery();
        ArrayList<VisitTime> timeList = new ArrayList<>();
        while (rs.next()) {
            VisitTime vt = new VisitTime(
                    rs.getString("TimeID"),rs.getString("Status"),rs.getString("UserID"),rs.getString("LodgingID"),rs.getTimestamp("VisitDateTime")
            );
            timeList.add(vt);
        }
        return timeList;
    }
    
    public VisitTime GetVisitTime(String TimeID) throws Exception{
        String sql = "select * from visittime where TimeID=?";
        pstmt = con.prepareStatement(sql);
        pstmt.setString(1, TimeID);
        ResultSet rs = pstmt.executeQuery();
        VisitTime vt = null;
        
        while (rs.next()) {           
            vt = new VisitTime(
                    rs.getString("TimeID"),rs.getString("Status"),rs.getString("UserID"),rs.getString("LodgingID"),rs.getTimestamp("VisitDateTime")
            );
        }
        return vt;
    }
    
    public boolean UpdateVisitTime(VisitTime vt) throws Exception{
        String sql;
        
        if(vt.getStatus().equals("Inactive")){
            sql = "Update visittime set Status='Inactive' where TimeID=?";      
            pstmt = con.prepareStatement(sql); 
            pstmt.setString(1, vt.getTimeID());
        }else{
            sql = "Update visittime set VisitDateTime=? where TimeID=?";      
            pstmt = con.prepareStatement(sql); 
            pstmt.setTimestamp(1, vt.getVisitDateTime());
            pstmt.setString(2, vt.getTimeID());
        }
           
        int result = pstmt.executeUpdate();        
        return (result > 0);
    }
    
    public boolean isTimeExist(VisitTime vt) throws Exception{
        String sql = "Select * from visittime where LodgingID=? AND Status='Active' AND VisitDateTime LIKE ?";      
        pstmt = con.prepareStatement(sql); 
        pstmt.setString(1, vt.getLodgingID());
        pstmt.setTimestamp(2, vt.getVisitDateTime()); 
        ResultSet rs = pstmt.executeQuery();
        
        boolean exist = false;
        while (rs.next()) {
            exist = true;
        }
        
        return exist;      
    }
}
