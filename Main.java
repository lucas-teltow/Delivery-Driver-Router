/******************************************************************************

Lucas Teltow, lkt190000
Project 4, Main.java

*******************************************************************************/
//imports
import java.io.*;
import java.lang.*;
import java.util.Scanner;
import java.util.ArrayList;

public class Main
{
    //non main functions
    //a function for handling the drivers, mostly to keep main very tidy
    //takes in a scanner and a graph and returns void (all returns to the console)
    private static void driverHandler(Scanner input, Graph data)
    {
        //various variables
        int i = 0;                                              //counter variable
        int j = 0;                                              //second counter variable
        String name = "";                                       //for storing the names of the drivers
        String currentLine = "";                                //for storing the current line of input
        ArrayList<Driver> drivers = new ArrayList<Driver>();    //for storing the valid drivers
        int mindex = 0;                                         //for storing the current lowest value in the sorting section
        boolean alreadyFound = false;                           //for checking if a given vertex has already been found

        //taking each line and creating and dealing with the driver line by line
        while(input.hasNextLine())
        {
            //preliminary stuff
            currentLine = input.nextLine();
            i = 0;
            j = 0;
            
            //grabbing the name of the driver
            while(currentLine.charAt(i) != ' ')
                i++;
            
            name = currentLine.substring(0, i);
            
            //creating a driver
            Driver tempDriver = new Driver(name);
            
            //checking its route
            i++;
            routeCheck(tempDriver, data, currentLine.substring(i));
            
            //adding the driver to the storage arrayList
            drivers.add(tempDriver);
        }//end of looping through and grabbing each line of input
        
        //printing the drivers
        //first getting the correct order
        int[] indices = new int[drivers.size()];
        for(int k = 0; k < drivers.size(); k++)
            indices[k] = -1;
        //grabbing the correct order of the drivers based on weight
        for(int k = 0; k < drivers.size(); k++)
        {
            mindex = 0;
            while(true)
            {
                for(i = 0; i < indices.length; i++)
                {
                    if(indices[i] == mindex)
                    {
                        mindex++;
                        break;
                    }
                }
                
                if(i == indices.length)
                    break;
            }
                
            if(mindex == indices.length)
            {
               indices[k] = mindex - 1;
               return;
            }
            for(int n = 0; n < drivers.size(); n++)
            {
                alreadyFound = false;
                for(int m = 0; m < indices.length; m++)
                {
                    if(indices[m] == n)
                    {
                        alreadyFound = true;
                        break;
                    }//end of if the current value already exists
                }//end of checking if that value already exists
                if(alreadyFound)
                    continue;
                if(drivers.get(n).compareTo(drivers.get(mindex)) < 0)
                    mindex = n;
            }//end of inner loop grabbing this iteration's lowest value
            
            indices[k] = mindex;
        }//end of looping through the drivers
        
        //printing each line
        for(int k = 0; k < drivers.size(); k++)
        {
            System.out.println(drivers.get(indices[k]).toString());
        }
    }//end of driverHandler function
    
    //for checking a driver's route for summing weight and getting validity
    //takes in a driver object, a graph, and the input string and returns void (modifing the driver object itself)
    private static void routeCheck(Driver person, Graph data, String input)
    {
        //various variables
        String name = "";                   //for storing the current name being checked
        Node previous = null;               //for storing the prior vertex in the route
        int j = 0;                          //counter variable for getting names
        boolean isValid = true;             //a flag for checking validity
        int k = 0;                          //counter variable
        String[] places = input.split(" "); //for the names of all the places

        //grabbing the inital vertex
        name = places[0];
        
        //System.out.println(person.getName() + " " + input);
        
        //checking if this graph has the starting vertex
        for(k = 0; k < data.getVertices().size(); k++)
        {
            if(data.getVertices().get(k).getPlace().equals(name))
            {
                previous = data.getVertices().get(k);
                //System.out.print(previous.getPlace());
                break;
            }
        }//end of checking if the initial vertex exists
        //if the inital vertex doesnt exits, then the route doesnt exist
        if(k >= data.getVertices().size())
        {
            person.setValidity(false);
            person.setWeight(0);
            return;
        }//end of checking if the first vertex is there
        
        //looping through and taking each name of a connection to check for its weight one at a time
        for(int i = 1; i < places.length; i++)
        {
            name = places[i];
            //checking if the previous vertex is connected to this one, and if so grabbing the weight
            isValid = false;
            for(k = 0; k < previous.getConnections().size(); k++)
            {
                if(previous.getConnections().get(k).getLeft().getPlace().equals(name) || previous.getConnections().get(k).getRight().getPlace().equals(name))
                {
                    int temp = person.getWeight();
                    temp = temp + previous.getConnections().get(k).getWeight();
                    person.setWeight(temp);
                    isValid = true;
                    
                    if(previous.getConnections().get(k).getLeft().getPlace().equals(name))
                        previous = previous.getConnections().get(k).getLeft();
                    else previous = previous.getConnections().get(k).getRight();
                    
                    break;
                }
            }//end of checking if the pervious vertex is connected to this vertex
            
            //if the vertex wasnt connected, then its not a valid route
            if(!isValid)
            {
                person.setValidity(false);
                person.setWeight(0);
                return;
            }//end of checking the connection from the previous vertex
        }//end of looping through the line
    }//end of routeCheck


    //main function, needs no explanation
	public static void main(String[] args) throws IOException
	{
		//variables
        Scanner in = new Scanner(System.in);    //scanner for the console inputs
        String fileName;                        //for storing the name of the input files
        Graph places = new Graph();             //the tree with the data in it
        
        //getting the file name
        System.out.println("Input Graph File Name");
        fileName = in.nextLine();

        //opens input file and checks to see if it is valid
        File inFile = new File(fileName);
        if(!inFile.exists())
            return;
        Scanner inFileScanner = new Scanner(inFile);
        
        //filling the graph tree
        places = places.createGraph(inFileScanner);
        
        //printing the graph
        //System.out.println(places.toString());
        
        //grabbing the driver file and checking to see if its valid
        System.out.println("Input Driver File Name");
        fileName = in.nextLine();
        File driverFile = new File(fileName);
        if(!driverFile.exists())
            return;
        Scanner driverScanner = new Scanner(driverFile);
        
        //handling the rest of the input (the drivers)
        driverHandler(driverScanner, places);
        
        //closing the I/O files
        inFileScanner.close();
        driverScanner.close();
	}//end of main function
}//end of main class

//driver class for storing information about a driver
class Driver
{
    //variables
    boolean validity;                   //for telling if the given driver has a route is valid or invalid
    int weight;                         //for storing the weight of their route
    String name;                        //for storing the name of the driver
    Driver next;                        //for making a janky linked list so they can be sorted alphabetically
    
    
    //constructors
    //default constructor
    public Driver()
    {
        validity = true;
        weight = 0;
        name = "";
        next = null;
    }//end of default constructor
    
    //overloaded constructor for taking a name
    public Driver(String n)
    {
        validity = true;
        weight = 0;
        name = n;
        next = null;
    }//end of overloaded(String) constructor
    
    
    //getters and setters
    public boolean getValidity()
    {
        return validity;
    }//end of getValidity
    
    public int getWeight()
    {
        return weight;
    }//end of getWeight
    
    public String getName()
    {
        return name;
    }//end of getName
    
    public void setValidity(boolean n)
    {
        validity = n;
    }//end of setValidity
    
    public void setWeight(int n)
    {
        weight = n;
    }//end of setWeight
    
    public void setName(String n)
    {
        name = n;
    }//end of setName
    
    public Driver getNext()
    {
        return next;
    }//end of getNext
    
    public void setNext(Driver n)
    {
        next = n;
    }//end of setNext
    
    
    //other general functions
    //tostring function
    public String toString()
    {
        if(validity)
            return name + "\t" + weight + "\tvalid";
        else return name + "\t" + 0 + "\tinvalid";
    }//end of toString
    
    //compareTo function
    public int compareTo(Driver d2)
    {
        if(weight > d2.getWeight())
            return 1;
        if(weight < d2.getWeight())
            return -1;
        if(name.compareTo(d2.getName()) > 0)
            return 1;
        if(name.compareTo(d2.getName()) < 0)
            return -1;
        return 0;
    }//end of compareTo
}//end of driver class
