package testingsentimentanalysis;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author edeasis
 */

import DB_Connection.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import javax.swing.JComboBox;
import net.proteanit.sql.DbUtils;
import testingsentimentanalysis.TestingSentimentAnalysis;

public class Database {
    
    private final DB_Connection obj_DB_Connection = new DB_Connection();
    private final Connection conn = obj_DB_Connection.get_connection();
    private final PreparedStatement ps = null;
    
    public boolean checkAdmin(String username, String password){
        try{
            Statement myStmt = conn.createStatement();

            ResultSet myRs = myStmt.executeQuery("SELECT * FROM Admin WHERE '"
            + username + "' = USERNAME AND '" + password + "' = PASSWORD");

            if(myRs.next()){
                return true;
            }
        }
        catch(SQLException e){
            System.err.println(e);
        }
        return false;
    }

    public boolean checkStudent(String username, String password){
        try{
            Statement myStmt = conn.createStatement();

            ResultSet myRs = myStmt.executeQuery("SELECT * FROM Student WHERE '"
            + username + "' = USERNAME AND '" + password + "' = PASSWORD");

            if(myRs.next()){
                return true;
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    
    public int returnStudentID(String username, String password){
        int id = 0;
        try{
            Statement myStmt = conn.createStatement();
            
            ResultSet Rs2 = myStmt.executeQuery("SELECT id FROM Student WHERE '"
            + username + "' = USERNAME AND '" + password + "' = PASSWORD");

            while(Rs2.next()){
                id = Rs2.getInt("id");
            }
            return id;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return 0;
    }
    
    public boolean checkAnalyst(String username, String password){
        try{
            Statement myStmt = conn.createStatement();

            ResultSet myRs = myStmt.executeQuery("SELECT * FROM Analyst WHERE '"
            + username + "' = USERNAME AND '" + password + "' = PASSWORD");

            if(myRs.next()){
                return true;
            }
        }
        catch(SQLException e){
            System.err.println(e);
        }
        return false;
    }
    
    public void signUp(String username, String password){
        try{
            Statement myStmt = conn.createStatement();

            myStmt.executeUpdate("INSERT INTO Student(username, password "
                    + ") VALUES ('" + username + "', '" + password 
                    + "')");
        }
        catch(SQLException e){
            System.err.println(e);
        }
    }
    
    public String queryReviews(String topic){
        
        ResultSet myRs = null;
        String str = "";
        
        try{
            Statement myStmt = conn.createStatement();

            myRs = myStmt.executeQuery("SELECT studentID, Review FROM " + topic);
            while(myRs.next()){
                str += System.getProperty("line.separator") +
                        "Student: " + myRs.getString("studentID") + " " 
                        + myRs.getString("Review");
            }
            
            return str;
        }
        catch(SQLException e){
            System.err.println(e);
        }
        return str;
    }
    
    
    public int queryTotal(String topic){
        
        ResultSet myRs = null, myRs2 = null, myRs3 = null, myRs4 = null;
        int total = 0, accumulate = 0, accumulate2 = 0, accumulate3 = 0, 
                accumulate4 = 0;
        try{
            Statement myStmt = conn.createStatement();
            Statement myStmt2 = conn.createStatement();
            Statement myStmt3 = conn.createStatement();
            Statement myStmt4 = conn.createStatement();

            myRs = myStmt.executeQuery("SELECT Total FROM ReviewRanks WHERE"
                    + " Topic = '" + topic +"'");
            
            while(myRs.next()){
                total = (Integer) myRs.getObject("Total");;
            }
            
            myRs = myStmt.executeQuery("SELECT One "
                    + "FROM ReviewRanks WHERE"
                    + " Topic = '" + topic +"'");
            myRs2 = myStmt2.executeQuery("SELECT Two "
                    + "FROM ReviewRanks WHERE"
                    + " Topic = '" + topic +"'");
            myRs3 = myStmt3.executeQuery("SELECT Three "
                    + "FROM ReviewRanks WHERE"
                    + " Topic = '" + topic +"'");
            myRs4 = myStmt4.executeQuery("SELECT Four "
                    + "FROM ReviewRanks WHERE"
                    + " Topic = '" + topic +"'");
            
            while(myRs.next()){
                accumulate = (Integer) myRs.getObject("One");
            }
            while(myRs2.next()){
                accumulate2 = (Integer) myRs2.getObject("Two");
            }
            while(myRs3.next()){
                accumulate3 = (Integer) myRs3.getObject("Three");
            }
            while(myRs4.next()){
                accumulate4 = (Integer) myRs4.getObject("Four");
            }
            
            if(total != 0){
                total = (accumulate + (accumulate2*2) + (accumulate3*3) + 
                        (accumulate4*4))%total;
            }
            
            return total;
        }
        catch(SQLException e){
            System.err.println(e);
        }
        return total;
    }
    
    public int queryOne(String rowName){
          ResultSet myRs = null;
          int total = 0;
          try{
            Statement myStmt = conn.createStatement();
            myRs = myStmt.executeQuery("SELECT One FROM ReviewRanks WHERE"
                    + " Topic = '" + rowName +"'");
            
            while(myRs.next()){
                total = (Integer) myRs.getObject("One");;
            }
            return total;
          }catch(SQLException e){
            System.err.println(e);
          }
          return 0;
    }
    
    public int queryTwo(String rowName){
          ResultSet myRs = null;
          int total = 0;
          try{
            Statement myStmt = conn.createStatement();
            myRs = myStmt.executeQuery("SELECT Two FROM ReviewRanks WHERE"
                    + " Topic = '" + rowName +"'");
            
            while(myRs.next()){
                total = (Integer) myRs.getObject("Two");;
            }
            return total;
          }catch(SQLException e){
            System.err.println(e);
          }
          return 0;
    }
    
    public int queryThree(String rowName){
          ResultSet myRs = null;
          int total = 0;
          try{
            Statement myStmt = conn.createStatement();
            myRs = myStmt.executeQuery("SELECT Three FROM ReviewRanks WHERE"
                    + " Topic = '" + rowName +"'");
            
            while(myRs.next()){
                total = (Integer) myRs.getObject("Three");;
            }
            return total;
          }catch(SQLException e){
            System.err.println(e);
          }
          return 0;
    }
    
    public int queryFour(String rowName){
          ResultSet myRs = null;
          int total = 0;
          try{
            Statement myStmt = conn.createStatement();
            myRs = myStmt.executeQuery("SELECT Four FROM ReviewRanks WHERE"
                    + " Topic = '" + rowName +"'");
            
            while(myRs.next()){
                total = (Integer) myRs.getObject("Four");;
            }
            return total;
          }catch(SQLException e){
            System.err.println(e);
          }
          return 0;
    }
    
    public int queryTotalAccum(String rowName){
          ResultSet myRs = null;
          int total = 0;
          try{
            Statement myStmt = conn.createStatement();
            myRs = myStmt.executeQuery("SELECT Total FROM ReviewRanks WHERE"
                    + " Topic = '" + rowName +"'");
            
            while(myRs.next()){
                total = (Integer) myRs.getObject("Total");;
            }
            return total;
          }catch(SQLException e){
            System.err.println(e);
          }
          return 0;
    }
    
    public void delete(String review, int studentID)throws SQLException{
        
            Statement myStmt = conn.createStatement();
            
            myStmt.executeUpdate("DELETE FROM Environment WHERE review = '"
            + review + "' AND studentID = " + studentID);
            
            myStmt.executeUpdate("DELETE FROM Classes WHERE review = '"
            + review + "' AND studentID = " + studentID);
            
            myStmt.executeUpdate("DELETE FROM Food WHERE review = '"
            + review + "' AND studentID = " + studentID);
            
            myStmt.executeUpdate("DELETE FROM Professor WHERE review = '"
            + review + "' AND studentID = " + studentID);
            
            myStmt.executeUpdate("DELETE FROM Housing WHERE review = '"
            + review + "' AND studentID = " + studentID);
            
            myStmt.executeUpdate("DELETE FROM Sports WHERE review = '"
            + review + "' AND studentID = " + studentID);
            
            myStmt.executeUpdate("DELETE FROM AllApplied WHERE review = '"
            + review + "' AND studentID = " + studentID);
    }
    
    public String comboBoxQuery(JComboBox comboBox, Object selected){
        
        String outputHolder = "";
                
        switch (selected.toString()) {
            case "Professor":
                outputHolder = queryReviews("Professor");
                break;
            case "Classes":
                outputHolder = queryReviews("Classes");
                break;
            case "Environment":
                outputHolder = queryReviews("Environment");
                break;
            case "Housing":
                outputHolder = queryReviews("Housing");
                break;
            case "Sports":
                outputHolder = queryReviews("Sports");
                break;
            case "Food":
                outputHolder = queryReviews("Food");
                break;
            case "All":
                outputHolder = queryReviews("AllApplied");
                break;
            default:
                break;
        }
        return outputHolder;
    }
    
    public void submitReview(JComboBox comboBox, Object selected, String text,
             int studentID)throws SQLException{
        
        Statement myStmt = conn.createStatement();
        
        switch (selected.toString()) {
            case "Professor":
                myStmt.executeUpdate("INSERT INTO Professor(review, studentID)"
                        + " VALUES('" + text + "', " + studentID + ")");
                break;
            case "Classes":
                myStmt.executeUpdate("INSERT INTO Classes(review, studentID)"
                        + " VALUES('" + text + "', " + studentID + ")");
                break;
            case "Environment":
                myStmt.executeUpdate("INSERT INTO Environment(review, studentID)"
                        + " VALUES('" + text + "', " + studentID + ")");
                break;
            case "Housing":
                myStmt.executeUpdate("INSERT INTO Housing(review, studentID)"
                        + " VALUES('" + text + "', " + studentID + ")");
                break;
            case "Sports":
                myStmt.executeUpdate("INSERT INTO Sports(review, studentID)"
                        + " VALUES('" + text + "', " + studentID + ")");
                break;
            case "Food":
                myStmt.executeUpdate("INSERT INTO Food(review, studentID)"
                        + " VALUES('" + text + "', " + studentID + ")");
                break;
            case "All":
                myStmt.executeUpdate("INSERT INTO AllApplied(review, studentID)"
                        + " VALUES('" + text + "', " + studentID + ")");
                break;
            default:
                break;
        }
        
         try {
            TestingSentimentAnalysis testJuan = new TestingSentimentAnalysis();
            int rank = testJuan.checkSentiment(text);
            switch (rank) {
                case 1:
                    myStmt.executeUpdate("UPDATE ReviewRanks SET One = "
                            + "One+1, Total = Total+1 WHERE Topic = '"
                            + selected.toString() + "'");
                    break;
                case 2:
                    myStmt.executeUpdate("UPDATE ReviewRanks SET Two = "
                            + "Two+1, Total = Total+1 WHERE Topic = '"
                            + selected.toString() + "'");
                    break;
                case 3:
                    myStmt.executeUpdate("UPDATE ReviewRanks SET Three = "
                            + "Three+1, Total = Total+1 WHERE Topic = '"
                            + selected.toString() + "'");
                    break;
                case 4:
                    myStmt.executeUpdate("UPDATE ReviewRanks SET Four = "
                            + "Four+1, Total = Total+1 WHERE Topic = '"
                            + selected.toString() + "'");
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
