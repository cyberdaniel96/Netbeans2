package db;

import domain.Lodging;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/** @author chang **/

public class LodgingDB {

    PreparedStatement pstmt;
    Connection con;

    public LodgingDB() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://localhost/lodging";
        String username = "root";
        String password = "abcd1234";
        con = DriverManager.getConnection(url, username, password);
    }

    public ArrayList<Lodging> GetAllLodging() throws Exception {
        String sql = "select * from lodging where status='Active'";
        pstmt = con.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        ArrayList<Lodging> la = new ArrayList<Lodging>();
        while (rs.next()) {
            Lodging l = new Lodging();
            l.setLodgingId(rs.getString("lodgingId"));
            l.setTitle(rs.getString("title"));
            l.setAddress(rs.getString("address"));
            l.setPrice(rs.getDouble("price"));
            l.setFacility(rs.getString("facility"));
            l.setLodgingType(rs.getString("lodgingType"));
            l.setDescription(rs.getString("description"));
            l.setExpireDate(rs.getDate("expireDate"));
            l.setUserId(rs.getString("userId"));
            l.setImage(rs.getString("image"));
            l.setStatus(rs.getString("status"));
            la.add(l);
        }
        return la;
    }

    public boolean CreateLodging(Lodging l) throws Exception {
        String sql = "insert into lodging(lodgingId,title,address,price,facility,lodgingType,description,expireDate,userId,image,message,status) values(?,?,?,?,?,?,?,?,?,?,?,'Active')";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, l.getLodgingId());
        pstmt.setString(2, l.getTitle());
        pstmt.setString(3, l.getAddress());
        pstmt.setDouble(4, l.getPrice());
        pstmt.setString(5, l.getFacility());
        pstmt.setString(6, l.getLodgingType());
        pstmt.setString(7, l.getDescription());
        pstmt.setDate(8, l.getExpireDate());
        pstmt.setString(9, l.getUserId());
        pstmt.setString(10, l.getImage());
        pstmt.setInt(11, 0);
        
        int result = pstmt.executeUpdate();
        return (result > 0);
    }

    public String NewLodgingId() throws Exception {
        String sql = "select max(lodgingId) as latestId from lodging";
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
                newId = "L" + String.format("%05d", iid);
            }
            if (!found) {
                newId = "L00000";
            }
        }
        return newId;
    }

    public Lodging GetLodging(String lodgingId) throws Exception {
        String sql = "select * from lodging where lodgingId = ?";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, lodgingId);
        ResultSet rs = pstmt.executeQuery();
        Lodging l = new Lodging();
        if (rs.next()) {
            l.setLodgingId(rs.getString("lodgingId"));
            l.setTitle(rs.getString("title"));
            l.setAddress(rs.getString("address"));
            l.setPrice(rs.getDouble("price"));
            l.setFacility(rs.getString("facility"));
            l.setLodgingType(rs.getString("lodgingType"));
            l.setDescription(rs.getString("description"));
            l.setExpireDate(rs.getDate("expireDate"));
            l.setUserId(rs.getString("userId"));
            l.setImage(rs.getString("image"));
            l.setMessage(rs.getInt("message"));
            l.setStatus(rs.getString("status"));
            l.setUserId(rs.getString("userId"));
        } else {
            l = null;
        }
        return l;
    }

    public ArrayList<Lodging> GetUserLodging(String userId) throws Exception {
        String sql = "select * from lodging where status Not In('Inactive') AND userId = ?";
        pstmt = con.prepareStatement(sql);
        pstmt.setString(1, userId);
        ResultSet rs = pstmt.executeQuery();
        ArrayList<Lodging> la = new ArrayList<Lodging>();
        while (rs.next()) {
            Lodging l = new Lodging();
            l.setLodgingId(rs.getString("lodgingId"));
            l.setTitle(rs.getString("title"));
            l.setAddress(rs.getString("address"));
            l.setPrice(rs.getDouble("price"));
            l.setFacility(rs.getString("facility"));
            l.setLodgingType(rs.getString("lodgingType"));
            l.setDescription(rs.getString("description"));
            l.setExpireDate(rs.getDate("expireDate"));
            l.setUserId(rs.getString("userId"));
            l.setImage(rs.getString("image"));
            l.setMessage(rs.getInt("message"));
            l.setStatus(rs.getString("status"));
            la.add(l);
        }
        return la;
    }

    public boolean UpdateLodging(Lodging l) throws Exception {
        String sql = "update lodging set title=?,address=?,price=?,facility=?,lodgingType=?,description=?,expireDate=?,status=? where lodgingId=?";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, l.getTitle());
        pstmt.setString(2, l.getAddress());
        pstmt.setDouble(3, l.getPrice());
        pstmt.setString(4, l.getFacility());
        pstmt.setString(5, l.getLodgingType());
        pstmt.setString(6, l.getDescription());
        pstmt.setDate(7, l.getExpireDate());
        pstmt.setString(8, l.getStatus());
        pstmt.setString(9, l.getLodgingId());
        
        int result = pstmt.executeUpdate();
        return (result > 0);
    }
    
    public ArrayList<Lodging> GetSomeLodging(String address) throws Exception {
        String sql = "select * from lodging where address like ?";
        pstmt = con.prepareStatement(sql);
        pstmt.setString(1, "%"+address+"%");
        ResultSet rs = pstmt.executeQuery();
        ArrayList<Lodging> la = new ArrayList<Lodging>();
        while (rs.next()) {
            Lodging l = new Lodging();
            l.setLodgingId(rs.getString("lodgingId"));
            l.setTitle(rs.getString("title"));
            l.setAddress(rs.getString("address"));
            l.setPrice(rs.getDouble("price"));
            l.setFacility(rs.getString("facility"));
            l.setLodgingType(rs.getString("lodgingType"));
            l.setDescription(rs.getString("description"));
            l.setExpireDate(rs.getDate("expireDate"));
            l.setUserId(rs.getString("userId"));
            l.setImage(rs.getString("image"));
            l.setStatus(rs.getString("status"));
            la.add(l);
        }
        return la;
    }
    
    public boolean UpdateLodgingMessage(String lodgingId, int message) throws Exception {
        String sql = "update lodging set message=? where lodgingId=?";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setInt(1, message);
        pstmt.setString(2, lodgingId);
        int result = pstmt.executeUpdate();
        return (result > 0);
    }
    
    public void IsExpiryCheck()throws Exception {
        String sql = "update lodging set status='Expired' where status='Active' AND expireDate<=CURDATE()";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.executeUpdate();
    }
    
    public boolean RenewLodging(Lodging l) throws Exception{
        String sql = "update lodging set expireDate=?,status='Active' where lodgingId=?";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setDate(1, l.getExpireDate());
        pstmt.setString(2, l.getLodgingId());
        int result = pstmt.executeUpdate();        
        return (result > 0);
    }
    
    public boolean RemoveLodging(String lodgingID) throws Exception{
        String sql = "update lodging set status='Inactive' where lodgingId=?";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, lodgingID);
        int result = pstmt.executeUpdate();
        return (result > 0);
    }
}
