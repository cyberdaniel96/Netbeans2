package domain;

import java.sql.Timestamp;

/** @author chang **/

public class VisitTime {
    private String TimeID, Status, UserID, LodgingID;
    private Timestamp VisitDateTime;

    public VisitTime() {
    }

    public VisitTime(String TimeID, String Status, String UserID, String LodgingID, Timestamp VisitDateTime) {
        this.TimeID = TimeID;
        this.Status = Status;
        this.UserID = UserID;
        this.LodgingID = LodgingID;
        this.VisitDateTime = VisitDateTime;
    }

    public String getTimeID() {
        return TimeID;
    }

    public void setTimeID(String TimeID) {
        this.TimeID = TimeID;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String UserID) {
        this.UserID = UserID;
    }

    public String getLodgingID() {
        return LodgingID;
    }

    public void setLodgingID(String LodgingID) {
        this.LodgingID = LodgingID;
    }

    public Timestamp getVisitDateTime() {
        return VisitDateTime;
    }

    public void setVisitDateTime(Timestamp VisitDateTime) {
        this.VisitDateTime = VisitDateTime;
    }
  
}
