# escape-maze


### Requriments<br>
* Initial start location is (4,4), destination is (28,28);
* The maze is generated randomly with a N percent obstacles;
* Each rotation is 45 degree and robot can only see three directions at one orientation;
* The robot only know the location of destination;
* Each decision can only based on the database(not maze information) of the robot.
* Set a threshold, if can't arrive to the destination after 5000 times try, then the robot is trapped in maze.

### Key Data Structure
* Database: To store information about the maze. each move will update the database;
* Path: To store the path which the robot already go through;
* Orientation: from 1-8 represent 8 direction; 1:   2:    3:       etc.
* Flag: It is a switch for robot to be Normal mode or Pass-obstacle mode;
* TurnOrder: determine rotateRight or rotateLeft when meet obstacle


### Results
* Left figure: robot's movement in known maze data
* Right figure: robot's movement in actual environment(robot database)
* Red point: start point
* Green point: destination point
* White point: Free road which hasn't been go through
* Black point: obstacles
* Bright yellow point: the path of robot, priority lower than white point
* dark yellow point: bad-road, only go back method can go through this point

