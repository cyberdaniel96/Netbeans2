/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import MQTT.Converter;
import domain.Message;
/**
 *
 * @author Daniel
 */
public class PrivateChat extends Message{
    private String senderID;
    private String receiverID;
    private String delStatus = "NOTHING";
    public PrivateChat(String messageID, String content, String sentTime,String senderID, String receiverID) {
        super(messageID, content, sentTime);
        this.senderID = senderID;
        this.receiverID = receiverID;
        
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(String receiverID) {
        this.receiverID = receiverID;
    }
    
    public void setDelStatus(String delStatus){
        this.delStatus = delStatus;
    }
    
    public String getDelStatus(){
        return delStatus;
    }
    
    public String getHexResultWithSlash(){
        String result = "";
        Converter convert = new Converter();
        result = convert.convertToHex(new String[]{getMessageId(), getContent(), getSentTime(), senderID, receiverID, delStatus});
        return result;
    }

    @Override
    public String toString() {
        String result = "Sender ID : " + this.senderID + "\n" +
                "Receiver ID : " + this.receiverID + "\n\n";
        return super.toString() + result;
    }
   
}