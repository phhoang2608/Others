package testingsentimentanalysis;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Phuong Hoang
 */
public class Analyst extends User {
    
    public Analyst(String username, String password)
    {
        super(username, password);
    }
    
    public void analyzeReview()
    {
        
    }
    
    @Override
    public String purpose()//implements polymorphism
    {
        return ("Analyze Comments from Database");
    }
}
