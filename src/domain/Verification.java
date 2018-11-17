package domain;

import java.sql.Date;
import java.sql.Time;

public class Verification {
    private String verificationID, userID;
    private double verifyCode;
    private Date issueDate;
    private Time issueTime;

    public Verification() {
    }

    public Verification(String verificationID, String userID, double verifyCode, Date issueDate, Time issueTime) {
        this.verificationID = verificationID;
        this.userID = userID;
        this.verifyCode = verifyCode;
        this.issueDate = issueDate;
        this.issueTime = issueTime;
    }

    public String getVerificationID() {
        return verificationID;
    }

    public void setVerificationID(String verificationID) {
        this.verificationID = verificationID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public double getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(double verifyCode) {
        this.verifyCode = verifyCode;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public Time getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(Time issueTime) {
        this.issueTime = issueTime;
    }
    
}
