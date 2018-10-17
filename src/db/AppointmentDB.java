/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import domain.Appointment;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Daniel
 */
public class AppointmentDB {
    PreparedStatement pstmt;
    Connection con;

    public AppointmentDB() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/lodging";
        String username = "root";
        String password = "";
        con = DriverManager.getConnection(url, username, password);
    }
    
    public boolean AddAppointment(Appointment app) throws Exception {
        String sql = "INSERT INTO `appointment`(`appointmentID`, `dateTime`, `reason`,`state` ,`priority`, `comment`, `status`, `lodgingID`, `tenantID`, `ownerID`) VALUES (?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, app.getAppointmentID());
        pstmt.setString(2, app.getDateTime());
        pstmt.setString(3, app.getReason());
        pstmt.setString(4, app.getState());
        pstmt.setString(5, app.getPriority());
        pstmt.setString(6, app.getComment());
        pstmt.setString(7, app.getStatus());
        pstmt.setString(8, app.getLodgingID());
        pstmt.setString(9, app.getTenantID());
        pstmt.setString(10, app.getOwnerID());
        
        int result = pstmt.executeUpdate();
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }
    
    public String NewAppointmentID() throws Exception {
        String sql = "select max(appointmentID) as latestId from appointment";
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
                newId = "AP" + String.format("%05d", iid);
            }
            if (!found) {
                newId = "AP00000";
            }
        }
        return newId;
    }
    
     public List<Appointment> GetAllAppointment(String userID) throws Exception{
        String sql = "SELECT * FROM `appointment` WHERE tenantID = ? " ;
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, userID);
        
        ResultSet rs = pstmt.executeQuery();
        List<Appointment> list = new ArrayList<>();
        while(rs.next()){
            Appointment app = new Appointment(
            rs.getString(1),
            rs.getString(2),
            rs.getString(3),
            rs.getString(4),
            rs.getString(5),
            rs.getString(6),
            rs.getString(7),
            rs.getString(8),
            rs.getString(9),
            rs.getString(10));
            list.add(app);
        }
        if(list.isEmpty()){
            return list;
        }
        return list;
    }
    
     public boolean CancelAppointment(String appointmentID) throws Exception{
         String sql = "UPDATE `appointment` SET status = ? where appointmentID = ?";
         PreparedStatement pstmt = con.prepareStatement(sql);
         pstmt.setString(1, "cancel");
         pstmt.setString(2, appointmentID);

         int result = pstmt.executeUpdate();

         return result > 0;
     }
     
      public boolean UpdateAppointment(Appointment app) throws Exception{
          String sql = "UPDATE `appointment` SET `dateTime`= ? ,`reason`= ? ,`state`= ?,`priority`= ?,`comment`=?,`status`=?,`lodgingID`=?,`tenantID`=?,`ownerID`=? WHERE `appointmentID`= ? ";
          PreparedStatement pstmt = con.prepareStatement(sql);
          pstmt.setString(1, app.getDateTime());
          pstmt.setString(2, app.getReason());
          pstmt.setString(3, app.getState());
          pstmt.setString(4, app.getPriority());
          pstmt.setString(5, app.getComment());
          pstmt.setString(6, app.getStatus());
          pstmt.setString(7, app.getLodgingID());
          pstmt.setString(8, app.getTenantID());
          pstmt.setString(9, app.getOwnerID());
          pstmt.setString(10, app.getAppointmentID());

          int result = pstmt.executeUpdate();

          return result > 0;
     }
    
    public static void main(String[] args) throws Exception {
      //  Appointment app = new Appointment("AP00005","30-09-2018AND11:10:45","Gothing","Penang","10","I am interesting your room","pending","L00000","1610480","johnny96");
      //  AppointmentDB db = new AppointmentDB();
      //  db.UpdateAppointment(app);
    }
}
