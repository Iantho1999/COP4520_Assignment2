import java.util.concurrent.locks.ReentrantLock;

// Class to represent guests in the maze
class Guest extends Thread
{
    Maze maze;
    int num;
    boolean ateCupcake = false;

    // Constructor
    Guest(int number, Maze m)
    {
        maze = m;
        num = number;
    }

    public void eatCupcake()
    {
        ateCupcake = true;
        maze.cupcakePresent = false;
    }

    public void enterMaze(Maze maze)
    {
        // Return immediately if it has been declared that every guest entered the maze
        if (maze.finished)
        {
            return;
        }

        // If the cupcake is present and you have not eaten it yet, eat it
        if (maze.cupcakePresent == true && ateCupcake == false)
        {
            eatCupcake();
        }
    }

    @Override
    public void run()
    {
        while (maze.finished == false)
        {
            maze.lock.lock();
            enterMaze(maze);
            maze.lock.unlock();
        }
    }
}

// Class to represent the master guest, which keeps track of the number of guests that entered the maze
class MasterGuest extends Guest
{
    private int counter = 0;

    // Constructor
    MasterGuest(int number, Maze m)
    {
        super(number, m);
    }

    public void requestCupcake()
    {
        maze.cupcakePresent = true;
    }

    @Override
    public void enterMaze(Maze maze)
    {
        // If the cupcake is present and you have not eaten it yet, eat it. Also increment the counter
        if (maze.cupcakePresent == true && ateCupcake == false)
        {
            eatCupcake();
        }
        else if (maze.cupcakePresent == false)
        {
            // Increment the counter, and request a new cupcake
            counter++;
            requestCupcake();
        }

        if (counter >= Maze.numGuests)
        {
            Maze.finished = true;
        }
    }
}

// Class to represent the Minotaur Maze
class Maze
{
    public static int numGuests;

    public static boolean cupcakePresent = true;

    public static boolean finished = false;

    ReentrantLock lock = new ReentrantLock();

    // Constructor
    Maze(int guests)
    {
        numGuests = guests;
    }
}

public class Program
{
    public static void main(String[] args)
    {
        int i;

        // Instantiate the Maze class with a given number of guests, and print
        Maze maze = new Maze(3);
        System.out.println("Number of Guests: " + Maze.numGuests);

        Guest[] guests = new Guest[maze.numGuests];

        MasterGuest m = new MasterGuest(1, maze);
        
        // Start the threads, beginning with the master guest
        guests[0] = m;
        m.start();

        for (i = 1; i < maze.numGuests; i++)
        {
            Guest g = new Guest(i + 1, maze);
            guests[i] = g;
            g.start();
        }

        // Attempt to join the threads
        try 
        {
            for (i = 0; i < maze.numGuests; i++)
            {
                guests[i].join();
            }
        } 
        catch(InterruptedException e) 
        {
        }

        System.out.println("All of the guests have entered the maze.");
    }
}