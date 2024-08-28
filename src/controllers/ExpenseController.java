package controllers;

import entities.Expense;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExpenseController {
    private Connection connection;

    public ExpenseController(Connection connection) {
        this.connection = connection;
    }

    public void addExpense(Expense expense){
        String sql = "INSERT INTO expense (patientId, amount, date)" +
                    "VALUES (?, ?, ?)";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setInt(1, expense.getPatientId());
            pstmt.setDouble(2, expense.getAmount());
            pstmt.setString(3, expense.getDate());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Expense> expenseList(){
        List<Expense> expenses = new ArrayList<>();
        String sql = "SELECT * FROM expense";

        try(Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()){
                Expense expense = new Expense();
                expense.setId(rs.getInt("id"));
                expense.setPatientId(rs.getInt("patientId"));
                expense.setAmount(rs.getDouble("amount"));
                expense.setDate(rs.getString("date"));
                expenses.add(expense);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return expenses;
    }
}
