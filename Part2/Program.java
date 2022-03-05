import java.util.concurrent.atomic.*;
import java.util.Date;

// Array-Based Queue Lock
class ALock
{
    ThreadLocal<Integer> mySlotIndex = new ThreadLocal<Integer>()
    {
        protected Integer intialValue()
        {
            return 0;
        }
    };
    AtomicInteger tail;
    volatile boolean[] flag;
    int size;

    // Constructor
    public ALock(int capacity)
    {
        size = capacity;
        tail = new AtomicInteger(0);
        flag = new boolean[capacity];
        flag[0] = true;
    }

    public void lock()
    {
        int slot = tail.getAndIncrement() % size;
        mySlotIndex.set(slot);
        while (flag[slot] == false)
        {
            // Spin
        };
    }

    public void unlock()
    {
        int slot = mySlotIndex.get();
        flag[slot] = false;
        flag[(slot + 1) % size] = true;
    }
}

// Class to represent guests in the castle
class Guest extends Thread
{
    ShowRoom room;
    int num;
    boolean seenVase = false;

    // Constructor
    Guest(int number, ShowRoom s)
    {
        room = s;
        num = number;
    }

    public void enterShowRoom()
    {
        seenVase = true;
    }

    @Override
    public void run()
    {
        while (room.finished == false)
        {
            room.lock.lock();
            enterShowRoom();
            room.lock.unlock();
        }
    }
}

// Class to represent the showroom in which the vase is located
class ShowRoom
{
    public static int numGuests;
    public static int seconds;
    public static boolean finished = false;

    ALock lock;

    ShowRoom (int guests, int sec)
    {
        numGuests = guests;
        seconds = sec;
        lock = new ALock(guests);
    }

    public void takeTime()
    {
        long startTime = System.currentTimeMillis();
        long elapsedTime = 0L;

        while (elapsedTime < 1000 * seconds) 
        {
            elapsedTime = (new Date()).getTime() - startTime;
        }

        finished = true;
    }
}

public class Program
{
    public static void main(String[] args)
    {
        int i;

        // Instantiate the ShowRoom class with a given number of guests and a time limit in seconds
        ShowRoom room = new ShowRoom(3, 10);
        System.out.println("Number of Guests: " + room.numGuests);

        Guest[] guests = new Guest[room.numGuests];

        // Start the threads
        for (i = 0; i < room.numGuests; i++)
        {
            Guest g = new Guest(i + 1, room);
            guests[i] = g;
            g.start();
        }

        System.out.println("The Minotaur has dedicated " + room.seconds + " seconds to vase viewing, starting now.");
        room.takeTime();

        // Attempt to join the threads
        try 
        {
            for (i = 0; i < room.numGuests; i++)
            {
                guests[i].join();
            }
        } 
        catch(InterruptedException e) 
        {
        }

        System.out.println("Time's up, it's past the Minotaur's bedtime.");
    }
}