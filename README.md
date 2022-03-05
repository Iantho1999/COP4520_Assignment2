# COP4520_Assignment2

## Compilation Instructions
Go to the respective folder (Part1 or Part2)

Enter the following:
```
javac Program.java
java Program
```

## Documentation

### Part 1
The problem proposed in this part is similar to the prisoner problem shown in class. In this case, the best strategy is to have a master guest with a counter for how many guests have entered the maze. If the maze has a cupcake present and the guest traversing it has not yet eaten one, they should eat the cupcake. Otherwise, they should do nothing. Each time the master guest traverses the maze, it keeps track of whether or not the cupcake is currently present; if it’s not, then increment the counter by one and request a new cupcake. Once this counter is equal to the number of guests, you can safely assume that all guests have entered the maze. In my program, the guests are each represented with their own thread.

Upon successful compilation, the output should look like this:
![Part 1 Output](/Images/part1Output.PNG)

The number of guests is configurable in Program.java as the first parameter in the Maze object.

Upon evaluation, increasing the number of guests to 100 has a negligible impact on the time taken to run the program on my system. 

### Part 2
I have decided in this part to implement the third strategy shown, in which the guests line up in a queue. One drawback to this is that the guests have to wait in line until it is their turn and cannot freely roam the castle. However, the advantage to using a queue-based lock is that it provides first-come-first-serve fairness for each guest. Since I have a set number for the number of guests, I have decided to use an array-based queue. Similar to the first part, each guest is run on their own thread.

Upon successful compilation, the output should look like this:
![Part 2 Output](/Images/part2Output.PNG)

The number of guests as well as the time in seconds are configurable in Program.java as the first and second parameters of the ShowRoom object respectively.

Since the program is run on a set time, increasing or decreasing the number of guests has no effect on the time taken to run the program.
