package domain;

import java.sql.Date;

/** @author chang **/

public class Rental {
    private String rentalID, status, leaseID;
    private Date issueDate, dueDate;
    private double totalAmount;

    public Rental() {
    }

    public Rental(String rentalID, String status, String leaseID, Date issueDate, Date dueDate, double totalAmount) {
        this.rentalID = rentalID;
        this.status = status;
        this.leaseID = leaseID;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.totalAmount = totalAmount;
    }

    public String getRentalID() {
        return rentalID;
    }

    public void setRentalID(String rentalID) {
        this.rentalID = rentalID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLeaseID() {
        return leaseID;
    }

    public void setLeaseID(String leaseID) {
        this.leaseID = leaseID;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }


}
