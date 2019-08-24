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
public class Student extends User {
    
    private int numberOfFiles;
    
    public Student(String username, String password)
    {
        super(username, password);
        
        if(numberOfFiles <= 0) //implements exception handling
        {
            throw new IllegalArgumentException
                    ("Number of files must be larger or equal to 0");
        }
        this.numberOfFiles = numberOfFiles;
    }
    public int getNumberOfFiles()
    {
        return numberOfFiles;
    }
    
    public void setNumberOfFiles(int numberOfFiles)
    {
        this.numberOfFiles = numberOfFiles;
    }
    
    @Override
    public String purpose()//implements polymorphism
    {
        return ("Left Comment about Campus");
    }
    
}
