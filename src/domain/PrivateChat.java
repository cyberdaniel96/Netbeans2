/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import MQTT.Converter;

/**
 *
 * @author Daniel
 */
public class PrivateChat extends Message{
    private String senderID;
    private String receiverID;
    
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

    @Override
    public String toString() {
        String result = "Sender ID : " + this.senderID + "\n" +
                "Receiver ID : " + this.receiverID + "\n\n";
        return super.toString() + result;
    }
    
    public static void main(String[] args) {
        int[] src = new int[]{1, 2, 3, 4, 5};
        int[] dest = new int[6];

        System.arraycopy(src, 0, dest, 0, src.length);
        dest[5] = 7;
        for(int i: dest){
            System.out.println(i);
        }
        
    }
}
