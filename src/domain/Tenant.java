package domain;

import java.sql.Date;

/** @author chang **/

public class Tenant {
    private String tenantID, roomType, role, status, reason, leaseID, userID;
    private double rent, deposit;
    private Date leaseStart, leaseEnd, breakDate;

    public Tenant() {
    }

    public Tenant(String tenantID, String roomType, String role, String status, String reason, String leaseID, String userID, double rent, double deposit, Date leaseStart, Date leaseEnd, Date breakDate) {
        this.tenantID = tenantID;
        this.roomType = roomType;
        this.role = role;
        this.status = status;
        this.reason = reason;
        this.leaseID = leaseID;
        this.userID = userID;
        this.rent = rent;
        this.deposit = deposit;
        this.leaseStart = leaseStart;
        this.leaseEnd = leaseEnd;
        this.breakDate = breakDate;
    }

    public String getTenantID() {
        return tenantID;
    }

    public void setTenantID(String tenantID) {
        this.tenantID = tenantID;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getLeaseID() {
        return leaseID;
    }

    public void setLeaseID(String leaseID) {
        this.leaseID = leaseID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public double getRent() {
        return rent;
    }

    public void setRent(double rent) {
        this.rent = rent;
    }

    public double getDeposit() {
        return deposit;
    }

    public void setDeposit(double deposit) {
        this.deposit = deposit;
    }

    public Date getLeaseStart() {
        return leaseStart;
    }

    public void setLeaseStart(Date leaseStart) {
        this.leaseStart = leaseStart;
    }

    public Date getLeaseEnd() {
        return leaseEnd;
    }

    public void setLeaseEnd(Date leaseEnd) {
        this.leaseEnd = leaseEnd;
    }

    public Date getBreakDate() {
        return breakDate;
    }

    public void setBreakDate(Date breakDate) {
        this.breakDate = breakDate;
    }
}
