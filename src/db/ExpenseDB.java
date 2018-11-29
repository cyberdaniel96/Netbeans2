package db;

import domain.Expense;
import domain.Rental;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/** @author chang **/

public class ExpenseDB {
   PreparedStatement pstmt;
    Connection con;
    
    public ExpenseDB() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://localhost/lodging";
        String username = "root";
        String password = "abcd1234";
        con = DriverManager.getConnection(url, username, password);
    }
    
    public boolean AddNewExpense(Expense e) throws Exception {
        String sql = "Insert into expense(ExpenseID,Category,Amount,PayDate,Description,Status,RentalID) values(?,?,?,?,?,?,?)";
        pstmt = con.prepareStatement(sql);
        pstmt.setString(1, e.getExpenseID());
        pstmt.setString(2, e.getCategory());
        pstmt.setDouble(3, e.getAmount());
        pstmt.setDate(4, e.getPayDate());
        pstmt.setString(5, e.getDescription());
        pstmt.setString(6, e.getStatus());
        pstmt.setString(7, e.getRentalID());
        
        int result = pstmt.executeUpdate();      
        return (result > 0); 
    }
    
    public String NewExpenseID() throws Exception {
        String sql = "select max(ExpenseID) as latestId from expense";
        PreparedStatement pstmt = con.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        boolean found = false;
        String newID = "";
        while (rs.next()) {
            String temp = rs.getString("latestId");
            if (temp != null) {
                found = true;
                temp = temp.substring(2);
                int iid = Integer.parseInt(temp) + 1;
                newID = "EP" + String.format("%05d", iid);
            }
            if (!found) {
                newID = "EP00000";
            }
        }
        return newID;
    } 
    
    public ArrayList<Expense> GetAllExpenses(String rentalID) throws Exception{
        String sql = "select * from expense where Status='Active' AND RentalID=?";
        pstmt = con.prepareStatement(sql);
        pstmt.setString(1, rentalID);
        ResultSet rs = pstmt.executeQuery();
        ArrayList<Expense> expenseList = new ArrayList<>();
        while (rs.next()) {
            Expense e = new Expense(
                rs.getString("ExpenseID"),rs.getString("Category"),rs.getString("Description"),rs.getString("Status"),
                rs.getString("RentalID"),rs.getDate("PayDate"),Double.parseDouble(rs.getString("Amount"))    
            );             
            expenseList.add(e);
        }
        return expenseList;
    }
    
    public boolean UpdateExpense(Expense e) throws Exception{
        String sql = "Update expense set Category=?,Amount=?,PayDate=?,Description=?,Status=? where ExpenseID=?";      
        pstmt = con.prepareStatement(sql); 
        pstmt.setString(1, e.getCategory());
        pstmt.setDouble(2, e.getAmount());
        pstmt.setDate(3, e.getPayDate());
        pstmt.setString(4, e.getDescription());
        pstmt.setString(5, e.getStatus());
        pstmt.setString(6, e.getExpenseID());
        
        int result = pstmt.executeUpdate();        
        return (result > 0);
    }
    
    public double GetTotalExpense(String rentalID) throws Exception{
        String sql = "select Sum(Amount) as total_amt from expense where Status='Active' AND RentalID=?";
        pstmt = con.prepareStatement(sql);
        pstmt.setString(1, rentalID);
        ResultSet result = pstmt.executeQuery();
        result.next();
        
        double sum = Double.parseDouble(result.getString("total_amt"));
        return sum;
    }
    
}
