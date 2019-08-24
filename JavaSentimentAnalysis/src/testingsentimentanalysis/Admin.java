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
public class Admin extends User{
    
    private int facultyID;
    
    public Admin(String username, String password)
    {
        super(username, password);
        
        if(facultyID < 0) //implements exception handling
        {
            throw new IllegalArgumentException
                    ("Faculty ID must be larger than 0");
        }
        this.facultyID = facultyID;
    }
    
    public int getFacultyID()
    {
        return facultyID;
    }
    
    public void setFacultyID(int facultyID)
    {
        this.facultyID = facultyID;
    }
    
    @Override
    public String purpose()//implements polymorphism
    {
        return ("Manage Files in Database");
    }
}
