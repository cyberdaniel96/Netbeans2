/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import domain.Message;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author johnn
 */
public class MessageDB {

    PreparedStatement pstmt;
    Connection con;

    public MessageDB() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/lodging";
        String username = "root";
        String password = "";
        con = DriverManager.getConnection(url, username, password);
    }

    public ArrayList<Message> GetAllMessage(String lodgingId) throws Exception {
        String sql = "select * from message where lodgingId = ?";
        pstmt = con.prepareStatement(sql);
        pstmt.setString(1, lodgingId);
        ResultSet rs = pstmt.executeQuery();
        ArrayList<Message> ma = new ArrayList<Message>();
        while (rs.next()) {
            Message m = new Message();
            m.setLodgingId(rs.getString("lodgingId"));
            m.setContent(rs.getString("content"));
            m.setMessageId(rs.getString("messageId"));
            m.setSentTime(rs.getString("sentTime"));
            m.setUserId(rs.getString("userId"));
            ma.add(m);
        }
        return ma;
    }

    public String NewMessageId() throws Exception {
        String sql = "select max(messageId) as latestId from message";
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
                newId = "M" + String.format("%06d", iid);
            }
            if (!found) {
                newId = "M000000";
            }
        }
        return newId;
    }

    public boolean CreateMessage(Message m) throws Exception {
        String sql = "insert into message(messageId,lodgingId,content,sentTime,userId) values(?,?,?,?,?)";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, m.getMessageId());
        pstmt.setString(2, m.getLodgingId());
        pstmt.setString(3, m.getContent());
        pstmt.setString(4, m.getSentTime());
        pstmt.setString(5, m.getUserId());
        int result = pstmt.executeUpdate();
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    public Message GetMessage(String messageId) throws Exception {
        String sql = "select * from message where messageId = ? ";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, messageId);
        ResultSet rs = pstmt.executeQuery();
        Message m = new Message();
        if (rs.next()) {
            m.setMessageId(rs.getString("messageId"));
            m.setLodgingId(rs.getString("lodgingId"));
            m.setContent(rs.getString("content"));
            m.setSentTime(rs.getString("sentTime"));
            m.setUserId(rs.getString("userId"));
        } else {
            m = null;
        }
        return m;
    }

    public boolean DeleteMessage(String messageId) throws Exception {
        String sql = "delete from message where messageId=?";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, messageId);
        int result = pstmt.executeUpdate();
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean UpdateMessage(String messageId, String content) throws Exception {
        String sql = "update message set content=? where messageId=?";
        PreparedStatement pstmt = con.prepareStatement(sql);
                pstmt.setString(1,content);
                pstmt.setString(2,messageId);
                int result = pstmt.executeUpdate();

                if (result > 0)
                    return true;
                else
                    return false;
    }
}
