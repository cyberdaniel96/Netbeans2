package db;

import domain.Favourite;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class FavouriteDB {

    PreparedStatement pstmt;
    Connection con;

    public FavouriteDB() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://localhost/lodging";
        String username = "root";
        String password = "abcd1234";
        con = DriverManager.getConnection(url, username, password);
    }

    public ArrayList<Favourite> GetUserFavourite(String userId) throws Exception {
        String sql = "select * from favourite F inner join lodging L on F.lodgingId=L.lodgingId where L.status Not In('Inactive') AND F.userId = ?";
        pstmt = con.prepareStatement(sql);
        pstmt.setString(1, userId);
        ResultSet rs = pstmt.executeQuery();
        ArrayList<Favourite> fa = new ArrayList<Favourite>();
        while (rs.next()) {
            Favourite f = new Favourite();
            f.setFavouriteId(rs.getString("favouriteId"));
            f.setLodgingId(rs.getString("lodgingId"));
            f.setUserId(rs.getString("userId"));
            fa.add(f);
        }
        return fa;
    }

    public String NewFavouriteId() throws Exception {
        String sql = "select max(favouriteId) as latestId from favourite";
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
                newId = "F" + String.format("%05d", iid);
            }
            if (!found) {
                newId = "F00000";
            }
        }
        return newId;
    }

    public boolean AddFavourite(Favourite f) throws Exception {
        String sql = "insert into favourite(favouriteId,lodgingId,userId) values(?,?,?)";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, f.getFavouriteId());
        pstmt.setString(2, f.getLodgingId());
        pstmt.setString(3, f.getUserId());
        int result = pstmt.executeUpdate();
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }
    
    public boolean IsExist(String lodgingId, String userId) throws Exception{
        String sql = "select * from favourite where lodgingId = ? and userId = ?";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, lodgingId);
        pstmt.setString(2, userId);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return true;
        } else {
            return false;
        }
    }
    
    public boolean DeleteFavourite(String lodgingId, String userId) throws Exception {
        String sql = "delete from favourite where lodgingId=? and userId=?";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, lodgingId);
        pstmt.setString(2, userId);
        int result = pstmt.executeUpdate();
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

}
