/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import MQTT.Converter;
import domain.Message;
import domain.PrivateChat;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Daniel
 */
public class PrivateChatDB {
     PreparedStatement pstmt;
    Connection con;
    
    public PrivateChatDB() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/lodging";
        String username = "root";
        String password = "";
        con = DriverManager.getConnection(url, username, password);
    }
    
    public boolean CreateMessage(PrivateChat m) throws Exception {
        String sql = "insert into privatechat(messageId,content,sentTime,senderID,receiverID, delStatus) values(?,?,?,?,?,?)";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, m.getMessageId());
        pstmt.setString(2, m.getContent());
        pstmt.setString(3, m.getSentTime());
        pstmt.setString(4, m.getSenderID());
        pstmt.setString(5, m.getReceiverID());
        pstmt.setString(6, "NOTHINGANDNOTHING");
        int result = pstmt.executeUpdate();
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }
    
    public List<Message> GetAllMessage(String senderID, String receiverID) throws Exception{
        String sql = "SELECT * FROM privatechat WHERE ( senderID = ? AND receiverID = ? ) or ( senderID = ? AND receiverID = ? ) or senderID = ? or receiverID = ?" ;
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, senderID);
        pstmt.setString(2, receiverID);
        pstmt.setString(3, receiverID);
        pstmt.setString(4, senderID);
        pstmt.setString(5, senderID);
        pstmt.setString(6, senderID);
        ResultSet rs = pstmt.executeQuery();
        List<Message> list = new ArrayList<>();
        while(rs.next()){
            Message message =new PrivateChat(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5));
           ((PrivateChat)message).setDelStatus(rs.getString(6));
            //Alternative: 
           /*
            PrivateChat chat = (PrivateChat)message;
            chat.setDelStatus("MyStatus");
           */
            list.add(message);
        }
        if(list.isEmpty()){
            return list;
        }
        return list;
    }
    
    public boolean DeletePrivateChat(String sender, String receiver, String role, String status) throws Exception{
        if(role.equals("TENANT")){
            return DeletePrivateChatStudent(sender, receiver, status);
        }else if(role.equals("OWNER")){
            return DeletePrivateChatOwner(sender, receiver, status);
        }
        System.err.println("Unable to delete private chat");
        return false;
        
    }
    
    private boolean DeletePrivateChatStudent(String sender, String receiver, String status) throws Exception{
        String sql = "UPDATE PRIVATECHAT SET delStatus = ? WHERE (senderID = ? AND receiverID = ?)"
                + " OR (senderID = ? AND receiverID = ?)";
        System.out.println(sender +" "+receiver+" "+status);
        PreparedStatement pstmt = con.prepareStatement(sql);
        String temp[] = status.split("AND");
        String col1 = "";
        if(temp[1].equals(receiver)){
            col1 = sender + "AND" + receiver; 
        }else if(temp[1].equals("NOTHING")){
            col1 = sender + "AND" + "NOTHING";
        }
        pstmt.setString(1,col1);
        pstmt.setString(2,sender);
        pstmt.setString(3,receiver);
        pstmt.setString(4,receiver);
        pstmt.setString(5,sender);
        int result = pstmt.executeUpdate();
        
        return result > 0;
    }
    
    private boolean DeletePrivateChatOwner(String sender, String receiver, String status) throws SQLException{
         String sql = "UPDATE PRIVATECHAT SET delStatus = ? WHERE (senderID = ? AND receiverID = ?)"
                + " OR (senderID = ? AND receiverID = ?)";
        PreparedStatement pstmt = con.prepareStatement(sql);
        String temp[] = status.split("AND");
        String col1 = "";
        if(temp[0].equals(receiver)){
            col1 = receiver + "AND" + sender; 
        }else if(temp[0].equals("NOTHING")){
            col1 = "NOTHING'" + "AND" + sender;
        }
        pstmt.setString(1,col1);
        pstmt.setString(2,sender);
        pstmt.setString(3,receiver);
        pstmt.setString(4,receiver);
        pstmt.setString(5,sender);
        int result = pstmt.executeUpdate();

        return result > 0;
    }
    
    public String getNewID() throws Exception{

        
        String sql = "select max(messageID) as latestID from PRIVATECHAT";
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
                newId = "PM" + String.format("%06d", iid);
            }
            if (!found) {
                newId = "PM000000";
            }
        }
        return newId;
    }
    
    public static void main(String[] args) throws Exception {
        
    }
}
