/******************************************************************************

Lucas Teltow
Graph.java

*******************************************************************************/
//imports
import java.io.*;
import java.lang.*;
import java.util.Scanner;
import java.util.ArrayList;

class Graph
{
    //variables
    int currentSize;                        //for the current size of the graph
    ArrayList<Node> vertices;               //for holding the vertices

    //constructors
    //defualt
    public Graph()
    {
        currentSize = 0;
        vertices = new ArrayList<Node>();
    }//end of defualt constructor
    
    
    //getters and setters
    public int getCurrentSize()
    {
        return currentSize;
    }//end of getCurrentSize
    
    public ArrayList<Node> getVertices()
    {
        return vertices;
    }//end of getCurrentSize
    
    
    public void setCurrentSize(int i)
    {
        currentSize = i;
    }//end of setCurrentSize
    
    public Node getVertex(int i)
    {
        return vertices.get(i);
    }//end of getVertex
    
    public void addVertex(Node n)
    {
        vertices.add(n);
        currentSize++;
    }//end of setVertex
    
    
    //other functions
    //isEmpty checks if the graph is empty and returns true if its empty
    //takes in no arguments, and returns a bool
    public boolean isEmpty()
    {
        if(currentSize == 0)
            return true;
        return false;
    }//end of isEmpty
    
    //createGraph makes a graph from an inputed file by filling the adjacncyMatrix
    //takes in an Scanner and a max size, and returns a graph Object
    public Graph createGraph(Scanner input)
    {
        //variables
        Graph output = new Graph();         //for storing the graph being editted and outputed
        String line = "";                   //for storing the current line of input
        String stringTemp = "";             //for temporarily storing a string value
        int i = 0;                          //for iterating through the lines
        int j = 0;                          //for iterating through the lines
        boolean duplicateFlag1 = false;     //a flag for checking if certain vertices are duplicates
        boolean duplicateFlag2 = false;     //a flag for checking if certain vertices are duplicates
        
        //going through the whole file and filling the adjacncyMatrix of output
        while(input.hasNextLine())
        {
            line = input.nextLine();

            //grabbing the name of the vertex
            for(j = 0; j < line.length(); j++)
            {
                if(line.charAt(j) == ' ')
                    break;
            }//end of grabbing the name of the vertex
            
            stringTemp = line.substring(0, j);
            
            //System.out.println("Hello 2 " + output.getVertices().size());
            //creating the current vertex
            Node newPlace = new Node(stringTemp);
            
            
            //checking to see if this vertex already exists
            duplicateFlag1 = false;
            for(int n = 0; n < output.getVertices().size(); n++)
            {
                //System.out.println("The vertex: " + output.getVertices().get(n).getPlace() + "     The new place: " + stringTemp);
                if(output.getVertices().get(n).getPlace().equals(stringTemp))
                    duplicateFlag1 = true;
                if(duplicateFlag1)
                    break;
            }//end of checking if this is a duplicate
                
            if(!duplicateFlag1)
                output.addVertex(newPlace);
            else
            {
                for(int n = 0; n < output.getVertices().size(); n++)
                    if(output.getVertices().get(n).getPlace().equals(stringTemp))
                        {
                            newPlace = output.getVertices().get(n);
                            break;
                        }//end of updating newPlace
            }//end of setting the newPlace pointer to the correct object
            
            //goes through each line and grabs all the other places its adjacent to
            j++;
            i = j;
            j = 0;
            
            //each loop grabs one place and the weight of the trip
            while(i < line.length())
            {
                //grabbing the next place
                j = i;
                while(j < line.length() && line.charAt(j) != ',')
                    j++;
                if(j == line.length())
                    break;
                
                //grabbing the name of the place
                stringTemp = line.substring(i, j);
                
                //System.out.println("Line: " + newPlace.getPlace() + ", " + stringTemp);
                
                Node connectedPlace = new Node(stringTemp);
                
                //checking to see if this vertex already exists
                duplicateFlag2 = false;
                for(int n = 0; n < output.getVertices().size(); n++)
                {
                    if(output.getVertices().get(n).getPlace().equals(stringTemp))
                        duplicateFlag2 = true;
                    if(duplicateFlag2)
                        break;
                }//end of checking if this is a duplicate
                
                //adding or updating connectedPlace
                if(duplicateFlag2)
                {
                    for(int n = 0; n < output.getVertices().size(); n++)
                    {
                        if(output.getVertices().get(n).getPlace().equals(stringTemp))
                        {
                            connectedPlace = output.getVertices().get(n);
                            break;
                        }//end of updating connectedPlace
                    }//end of looping throught to udpate connectedPlace correctly
                }//end of checking to see if the current vertex connection is already on the graph
                else output.addVertex(connectedPlace);
                
                //creating the edge object if neccessary
                //checking to avoid duplicate edges
                if(connectedPlace.compareTo(newPlace) == 0)
                {
                    i = j + 3;
                    continue;
                }

                duplicateFlag1 = false;
                for(int l = 0; l < output.getVertices().size(); l++)
                {
                    for(int k = 0; k < output.getVertices().get(l).getConnections().size(); k++)
                    {
                        //System.out.println("Comparing: " + output.getVertices().get(l).getConnections().get(k).getLeft().getPlace() + " and " + output.getVertices().get(l).getConnections().get(k).getRight().getPlace() + " to " + newPlace.getPlace() + " and " + connectedPlace.getPlace());
                        if(output.getVertices().get(l).getConnections().get(k).getLeft().getPlace().equals(newPlace.getPlace()) && output.getVertices().get(l).getConnections().get(k).getRight().getPlace().equals(connectedPlace.getPlace()))
                            duplicateFlag1 = true;
                        if(output.getVertices().get(l).getConnections().get(k).getRight().getPlace().equals(newPlace.getPlace()) && output.getVertices().get(l).getConnections().get(k).getLeft().getPlace().equals(connectedPlace.getPlace()))
                            duplicateFlag1 = true;
                        if(duplicateFlag1)
                            break;
                    }//end of looping through the connected vertices
                    if(duplicateFlag1)
                        break;
                }//end of looping through the vertices
                if(duplicateFlag1)
                {
                    i = j + 3;
                    continue;
                }
                
                //creating the edge object with weight given
                j++;
                int temp = j;
                
                while(temp < line.length() && Character.isDigit(line.charAt(temp)))
                    temp++;
                Edge newConnection = new Edge(Integer.parseInt(line.substring(j, temp)));
                
                newConnection.setLeft(newPlace);
                newConnection.setRight(connectedPlace);
                
                //updating the edge lists for the 2 nodes
                newPlace.addEdge(newConnection);
                connectedPlace.addEdge(newConnection);
                
                //System.out.println("The new edge is " + newPlace.getPlace() + " and " + connectedPlace.getPlace());
                
                temp--;
                i = temp + 2;
            }//end of looping through the individual line
        }//end of while loop reading each line of the input file
        return output;
    }//end of createGraph
    
    //tostring function
    //takes in no input and outputs a string
    public String toString()
    {
        String output = "Vertices: " + vertices.size() + "\n";
        //checking each vertex to print it out
        for(int i = 0; i < vertices.size(); i++)
        {
            output = output + vertices.get(i).toString() + "\n";
        }//end of checking each vertex in the node
        
        return output;
    }//end of toString method
}//end of graph class

//for acting as the nodes of the graph
class Node
{
    //variables
    String place;                   //the name of this place
    ArrayList<Edge> connections;    //the edges that are used to connect this place to other places
    
    //constructors
    //defualt
    public Node()
    {
        place = "";
        connections = new ArrayList<Edge>();
    }//end of defualt constructor
    
    //overloaded to take a string input
    public Node(String d)
    {
        place = d;
        connections = new ArrayList<Edge>();
    }//end of overloaded(driver) constructor
    
    
    //getters and setters
    public String getPlace()
    {
        return place;
    }//end of getPlace
    
    public ArrayList<Edge> getConnections()
    {
        return connections;
    }//end of getConnections
    
    public Edge getConnection(int i)
    {
        return connections.get(i);
    }//end of getConnections
    
    public void setPlace(String n)
    {
        place = n;
    }//end of setPlace
    
    public void addEdge(Edge n)
    {
        connections.add(n);
    }//end of addEdge
    
    
    //various other functions
    //isAdjacent checks if this node is adjacent to a given node
    //takes in a node and returns a boolean
    public boolean isAdjacent(Node n)
    {
        for(int i = 0; i < connections.size(); i++)
        {
            if(connections.get(i).getRight().compareTo(n) == 0 && connections.get(i).getLeft().compareTo(this) == 0)
                return true;
            if(connections.get(i).getRight().compareTo(this) == 0 && connections.get(i).getLeft().compareTo(n) == 0)
                return true;
        }//end of looping through and checking if any edge has the correct vertex on the other side
        return false;
    }//end of isAdjacent
    
    //a compare function to check if 2 vertices are the same
    //takes in a Node and returns an int, positive if the first one is greater, negative if its less than, and 0 otherwise
    public int compareTo(Node n)
    {
        return place.compareTo(n.getPlace());
    }//end of compareTo
    
    //a toString function for printing the node and all its connections
    //takes in no input and returns a string
    public String toString()
    {
        String output = place;
        //checking each vertex to print it out
        for(int i = 0; i < connections.size(); i++)
        {
            if(connections.get(i).getLeft().getPlace().equals(place))
                output = output + " " + connections.get(i).getRight().getPlace();
            else output = output + " " + connections.get(i).getLeft().getPlace();
        }//end of checking each vertex in the node
        
        return output;
    }//end of toString function
}//end of class node

//for storing the edges of the graph
class Edge
{
    //variables
    int weight;         //for storing the weight of this edge
    Node right;         //for the vertex on the right of the edge
    Node left;          //for the vertex on the left of the edge
    
    
    //constructors
    //defualt
    public Edge()
    {
        weight = 0;
        right = null;
        left = null;
    }//end of defualt constructor
    
    //overloaded constructor for an int weight
    public Edge(int n)
    {
        weight = n;
        right = null;
        left = null;
    }//end of overloaded(int) constructor
    
    
    //getters and setters
    public int getWeight()
    {
        return weight;
    }//end of getWeight
    
    public Node getRight()
    {
        return right;
    }//end of getRight
    
    public Node getLeft()
    {
        return left;
    }//end of getLeft
    
    public void setWeight(int n)
    {
        weight = n;
    }//end of setWeight
    
    public void setRight(Node n)
    {
        right = n;
    }//end of setRight
    
    public void setLeft(Node n)
    {
        left = n;
    }//end of setLeft
    
    
    //miscellaneous functions
    //a function for checking if the given edge connects a pair of nodes
    //takes in 2 nodes and returns a boolean
    public boolean contains(Node n1, Node n2)
    {
        if(right.compareTo(n1) == 0 && left.compareTo(n2) == 0)
            return true;
            
        if(right.compareTo(n2) == 0 && left.compareTo(n1) == 0)
            return true;
            
        return false;
    }//end of contains function
}//end of class Edge
