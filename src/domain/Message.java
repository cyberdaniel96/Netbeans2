package domain;

import java.io.Serializable;


public class Message implements Serializable{
    private String messageId;
    private String lodgingId;
    private String content;
    private String sentTime;
    private String userId;

    public Message() {
    }

    public Message(String messageId, String content, String sentTime) {
        this.messageId = messageId;
        this.content = content;
        this.sentTime = sentTime;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getLodgingId() {
        return lodgingId;
    }

    public void setLodgingId(String lodgingId) {
        this.lodgingId = lodgingId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSentTime() {
        return sentTime;
    }

    public void setSentTime(String sentTime) {
        this.sentTime = sentTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        String result = "MessageID : " + this.messageId + "\n" +
                "Content : " + this.content + "\n" +
                "Sent Time : " + this.sentTime + "\n";
                
        return result;
    }
    
    
}
