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
        String sql = "insert into privatechat(messageId,content,sentTime,senderID,receiverID) values(?,?,?,?,?,?)";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, m.getMessageId());
        pstmt.setString(2, m.getContent());
        pstmt.setString(3, m.getSentTime());
        pstmt.setString(4, m.getSenderID());
        pstmt.setString(5, m.getReceiverID());
        pstmt.setString(6, "NOTHING");
        int result = pstmt.executeUpdate();
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }
    
    public List<Message> GetAllMessage(String senderID, String receiverID) throws Exception{
        String sql = "SELECT * FROM privatechat WHERE ( senderID = ? AND receiverID = ? ) or ( senderID = ? AND receiverID = ? ) or senderID = ?" ;
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, senderID);
        pstmt.setString(2, receiverID);
        pstmt.setString(3, receiverID);
        pstmt.setString(4, senderID);
        pstmt.setString(5, senderID);
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
    
     public boolean UpdateMessage(String status, String[] messageID) throws Exception {
        String sql = getUpdateQuery(messageID);
        PreparedStatement pstmt = con.prepareStatement(sql);
                pstmt.setString(1,status);
                setParam(pstmt, messageID);
                int result = pstmt.executeUpdate();

                if (result > 0)
                    return true;
                else
                    return false;
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
    
    private String getUpdateQuery(String[] messageID){
        StringBuilder sql = new StringBuilder("UPDATE `privatechat` SET delStatus = ? WHERE messageID IN ");
        sql.append("(");
        for (int index = 0; index < messageID.length; index++) {
            if(!(index == messageID.length-1)){
                 sql.append("?,");
            }else{
                sql.append("?");
            }
        }
        sql.append(")");
        return sql.toString();
    }
    
    public void setParam(PreparedStatement pstmt, String[] messageID) throws SQLException{
        int index = 0;
        for(int i = 2; i <= messageID.length+1; i++){
            pstmt.setString(i, messageID[index]);
            index++;
        }
    }
    
    public static void main(String[] args) throws Exception {
        PrivateChatDB db = new PrivateChatDB();
        String[] arr = {"PM000001","PM000002","PM000003","PM000004","PM000005"};
        System.out.println(db.UpdateMessage("NOTHING", arr));
        
    }
}
