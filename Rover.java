package robot;

import java.util.ArrayList;
import java.util.List;

public class Rover {
	public int x;// x direction
	public int y;// y direction
	public int orientation;// 1-8 represent 8 orientations
	public int[][] database;// store maze information
	public int[] state = new int[3];
	public int[] destination = new int[2];
	public List<int[]> path = new ArrayList<int[]>();// store the rover's path,
	// store x,y,orientation
	int turnorder = 0;
	int flag = 0;
	int leftcount = 0;
	int rightcount = 0;

	public int[][] getDatabase() {
		return this.database;
	}

	public void setInitalState(int xx, int yy, int ori) {
		state[0] = xx;
		state[1] = yy;
		state[2] = ori;
		x = yy;
		y = xx;
		path.clear();
		path.add(0, state);
		database[xx][yy] = 1;
		this.updateCurrentState(path);
	}

	public void setCurrentState(int xx, int yy, int ori) {
		state[0] = xx;
		state[1] = yy;
		state[2] = ori;
		path.set(path.size() - 1, state);
	}

	public void setDestination(int xx, int yy) {
		destination[0] = xx;
		destination[1] = yy;
		database[xx][yy] = 1;
	}

	public void updateCurrentState(List<int[]> path) {
		x = path.get(path.size() - 1)[0];
		y = path.get(path.size() - 1)[1];
		orientation = path.get(path.size() - 1)[2];
		state[0] = x;
		state[1] = y;
		state[2] = orientation;
	}

	public int[] getCurrentState() {
		int[] currentLoc = new int[3];
		currentLoc[0] = path.get(path.size() - 1)[0];
		currentLoc[1] = path.get(path.size() - 1)[1];
		currentLoc[2] = path.get(path.size() - 1)[2];
		return currentLoc;
	}

	public void goStright(List<int[]> path) {
		System.out.println("go Stright!");
		int[] nextState = new int[3];

		switch (orientation) {
		case 1:
			nextState[0] = x;
			nextState[1] = y - 1;
			nextState[2] = orientation;
			break;
		case 2:
			nextState[0] = x + 1;
			nextState[1] = y - 1;
			nextState[2] = orientation;
			break;
		case 3:
			nextState[0] = x + 1;
			nextState[1] = y;
			nextState[2] = orientation;
			break;
		case 4:
			nextState[0] = x + 1;
			nextState[1] = y + 1;
			nextState[2] = orientation;
			break;
		case 5:
			nextState[0] = x;
			nextState[1] = y + 1;
			nextState[2] = orientation;
			break;
		case 6:
			nextState[0] = x - 1;
			nextState[1] = y + 1;
			nextState[2] = orientation;
			break;
		case 7:
			nextState[0] = x - 1;
			nextState[1] = y;
			nextState[2] = orientation;
			break;
		case 8:
			nextState[0] = x - 1;
			nextState[1] = y - 1;
			nextState[2] = orientation;
			break;
		default:
			break;
		}

		path.add(nextState);
		database[nextState[1]][nextState[0]] = 2;
		this.updateCurrentState(path);
		flag = 0;
		rightcount = 0;
		leftcount = 0;
	}

	public void goBack(List<int[]> path, int[][] maze) {
		if (path.size() > 1) {
			System.out.println("go Back!");
			int[] preState = new int[3];
			int preIndex = path.size() - 1;

			preState[0] = path.get(preIndex)[0];
			preState[1] = path.get(preIndex)[1];
			preState[2] = path.get(preIndex)[2];

			database[preState[1]][preState[0]] = 3;
			path.remove(path.size() - 1);
			this.updateCurrentState(path);

		} else {
			this.rotateRight(maze);
		}
		rightcount = 0;
		leftcount = 0;
        if(turnorder==1)turnorder=0;
        else if(turnorder==0)turnorder=1;

		flag = 0;

	}

	public void rotateRight(int[][] maze) {
		System.out.println("rotateRight");
		if (orientation == 8) {
			orientation = 1;
		} else {
			orientation++;
		}
		leftcount--;
		rightcount++;
		this.updateDatabse(maze);

	}

	public void rotateLeft(int[][] maze) {
		System.out.println("rotateLeft");
		if (orientation == 1) {
			orientation = 8;
		} else {
			orientation--;
		}
		leftcount++;
		rightcount--;
		this.updateDatabse(maze);
	}

	public void initalDatabase(int w, int h) {
		database = new int[w][h];
		for (int i = 0; i < w; i++)
			for (int j = 0; j < h; j++) {
				database[i][j] = 0;
			}
	}

	public void updateDatabse(int[][] maze) {// border of database

		switch (orientation) {
		case 1:// can see direction 8,1,2
			this.see(8, maze);
			this.see(1, maze);
			this.see(2, maze);
			break;
		case 2:// can see direction 1,2,3
			this.see(1, maze);
			this.see(2, maze);
			this.see(3, maze);
			break;
		case 3:// can see direction 2,3,4
			this.see(2, maze);
			this.see(3, maze);
			this.see(4, maze);
			break;
		case 4:// can see direction 3,4,5
			this.see(3, maze);
			this.see(4, maze);
			this.see(5, maze);
			break;
		case 5:// can see direction 4,5,6
			this.see(4, maze);
			this.see(5, maze);
			this.see(6, maze);
			break;
		case 6:// can see direction 5,6,7
			this.see(5, maze);
			this.see(6, maze);
			this.see(7, maze);
			break;
		case 7:// can see direction 6,7,8
			this.see(6, maze);
			this.see(7, maze);
			this.see(8, maze);
			break;
		case 8:// can see direction 7,8,1
			this.see(7, maze);
			this.see(8, maze);
			this.see(1, maze);
			break;
		default:
			break;
		}
		// System.out.println("Update Database" + orientation);
	}

	public void see(int direction, int[][] maze) {

		int w = maze.length;
		int h = maze[0].length;
		switch (direction) {
		case 1:
			for (int i = 0; y - i >= 0; i++) {// collect data from direction 1
				if (maze[y - i][x] != 0) {// obstacles
					if (database[y - i][x] != 2 && database[y - i][x] != 3)
						database[y - i][x] = 1;

				} else {
					break;
				}
			}
			break;
		case 2:
			for (int i = 0; (x + i) < w && (y - i) >= 0; i++) {// collect data
																// from
																// direction 2
				if (maze[y - i][x + i] != 0) {// obstacles
					if (database[y - i][x + i] != 2 && database[y - i][x + i] != 3)
						database[y - i][x + i] = 1;
				} else {
					break;
				}
			}
			break;
		case 3:
			for (int i = 0; (x + i) < w; i++) {// collect data from direction 3
				if (maze[y][x + i] != 0) {// obstacles
					if (database[y][x + i] != 2 && database[y][x + i] != 3)
						database[y][x + i] = 1;
				} else {
					break;
				}
			}
			break;
		case 4:
			for (int i = 0; (x + i) < w && (y + i) < h; i++) {// collect data
																// from
																// direction 4
				if (maze[y + i][x + i] != 0) {// obstacles
					if (database[y + i][x + i] != 2 && database[y + i][x + i] != 3)
						database[y + i][x + i] = 1;
				} else {
					break;
				}
			}
			break;
		case 5:
			for (int i = 0; (y + i) < h; i++) {// collect data from direction 5
				if (maze[y + i][x] != 0) {// obstacles
					if (database[y + i][x] != 2 && database[y + i][x] != 3)
						database[y + i][x] = 1;
				} else {
					break;
				}
			}
			break;
		case 6:
			for (int i = 0; (x - i) >= 0 && (y + i) < h; i++) {// collect data
																// from
																// direction 6
				if (maze[y + i][x - i] != 0) {// obstacles
					if (database[y + i][x - i] != 2 && database[y + i][x - i] != 3)
						database[y + i][x - i] = 1;
				} else {
					break;
				}
			}
			break;
		case 7:
			for (int i = 0; (x - i) >= 0; i++) {// collect data from direction 7
				if (maze[y][x - i] != 0) {// obstacles
					if (database[y][x - i] != 2 && database[y][x - i] != 3)
						database[y][x - i] = 1;
				} else {
					break;
				}
			}
			break;
		case 8:
			for (int i = 0; ((x - i) >= 0 && (y - i) >= 0); i++) {// collect
																	// data from
																	// direction
																	// 8
				if (maze[y - i][x - i] != 0) {// obstacles
					if (database[y - i][x - i] != 2 && database[y - i][x - i] != 3)
						database[y - i][x - i] = 1;
				} else {
					break;
				}
			}
			break;
		default:
			break;
		}
	}

	public int computeOrient() {// compute orientation
		int orient = 1;
		// this.updateCurrentState(path);
		double degree = 0;
		if ((y - destination[0]) != 0) {
			degree = Math.toDegrees(Math.atan((x - destination[1]) / (y - destination[0])));
		} else {
			degree = 90;
		}
		orient = (int) Math.floor(5 - degree / 45);

		return orient;
	};

	public void makeDecision(int[][] maze, List<int[]> path) {
		System.out.println(turnorder + "--right:" + rightcount + "--left:" + leftcount);

		int maxX = maze.length;
		int maxY = maze[0].length;
		int orient = this.computeOrient();
		int diff = orientation - orient;
		int preOri = path.get(path.size() - 1)[2];
		if (rightcount == 7 || leftcount == 7)
			this.goBack(path, maze);

		if (Math.abs(orientation - preOri) == 4) {
			if (turnorder == 0)
				this.rotateRight(maze);
			else
				this.rotateLeft(maze);
			return;
		}

		// Adjust the rotation
		// flag=0:normal states,go toward to destination
		// flag=1:meet obstacle, find way out
		// (y-1,x-1) (y-1, x ) (y-1,x+1)
		// ( y ,x-1) ( y , x ) ( y ,x+1)
		// (y+1,x-1) (y+1, x ) (y+1,x+1)

		while (diff != 0 && flag == 0) {
			System.out.println("Adjust orientation: " + orientation);
			if (diff > 0) {
				diff--;
				this.rotateLeft(maze);
			}
			if (diff < 0) {
				diff++;
				this.rotateRight(maze);
			}
		}

		// check orientation value
		if (orientation == 9) {
			orientation = 1;
		} else if (orientation == 0) {
			orientation = 8;
		}

		switch (orientation) {
		case 1:
			if (x == 0 || y == 0 || x == maxX - 1) {
				if (turnorder == 0)
					this.rotateRight(maze);
				else
					this.rotateLeft(maze);
				flag = 1;
				break;
			}
			for (int i = 1; i <= 2; i++) {
				if (database[y - 1][x] == i && roadcheck(x, y - 1, orientation)) {
					this.goStright(path);
					this.updateDatabse(maze);
					return;

				} else if (database[y - 1][x + 1] == i && roadcheck(x - 1, y + 1, orientation + 1)) {
					this.rotateRight(maze);
					this.goStright(path);
					this.updateDatabse(maze);
					return;

				} else if (database[y - 1][x - 1] == i && roadcheck(x - 1, y - 1, orientation - 1)) {
					this.rotateLeft(maze);
					this.goStright(path);
					this.updateDatabse(maze);
					return;

				}
			}
			if (this.deadRode(x, y) || rightcount == 7 || leftcount == 7)
				this.goBack(path, maze);
			else {
				if (turnorder == 0)
					this.rotateRight(maze);
				else
					this.rotateLeft(maze);
			}
			this.updateDatabse(maze);
			flag = 1;

			break;
		case 2:
			if (x == maxX - 1 || y == 0) {
				if (turnorder == 0)
					this.rotateRight(maze);
				else
					this.rotateLeft(maze);
				flag = 1;
				break;
			}
			for (int i = 1; i <= 2; i++) {
				if (database[y - 1][x + 1] == i && roadcheck(x + 1, y - 1, orientation)) {
					this.goStright(path);
					this.updateDatabse(maze);
					return;

				} else if (database[y][x + 1] == i && roadcheck(x + 1, y, orientation + 1)) {
					this.rotateRight(maze);
					this.goStright(path);
					this.updateDatabse(maze);
					return;

				} else if (database[y - 1][x] == i && roadcheck(x, y - 1, orientation - 1)) {
					this.rotateLeft(maze);
					this.goStright(path);
					this.updateDatabse(maze);
					return;

				}
			}
			if (this.deadRode(x, y) || rightcount == 7 || leftcount == 7)
				this.goBack(path, maze);
			else {
				if (turnorder == 0)
					this.rotateRight(maze);
				else
					this.rotateLeft(maze);
			}
			this.updateDatabse(maze);
			flag = 1;

			break;
		case 3:
			if (x == maxX - 1 || y == 0 || y == maxY - 1) {
				if (turnorder == 0)
					this.rotateRight(maze);
				else
					this.rotateLeft(maze);
				flag = 1;
				break;
			}
			for (int i = 1; i <= 2; i++) {
				if (database[y][x + 1] == i && roadcheck(x + 1, y, orientation)) {
					this.goStright(path);
					this.updateDatabse(maze);
					return;

				} else if (database[y + 1][x + 1] == i && roadcheck(x + 1, y + 1, orientation + 1)) {
					this.rotateRight(maze);
					this.goStright(path);
					this.updateDatabse(maze);
					return;
				} else if (database[y - 1][x + 1] == i && roadcheck(x + 1, y - 1, orientation - 1)) {
					this.rotateLeft(maze);
					this.goStright(path);
					this.updateDatabse(maze);
					return;
				}
			}
			if (this.deadRode(x, y) || rightcount == 7 || leftcount == 7)
				this.goBack(path, maze);
			else {
				if (turnorder == 0)
					this.rotateRight(maze);
				else
					this.rotateLeft(maze);
			}
			this.updateDatabse(maze);
			flag = 1;

			break;
		case 4:
			if (x == maxX - 1 || y == maxY - 1) {
				if (turnorder == 0)
					this.rotateRight(maze);
				else
					this.rotateLeft(maze);
				flag = 1;
				break;
			}
			for (int i = 1; i <= 2; i++) {
				if (database[y + 1][x + 1] == i && roadcheck(x + 1, y + 1, orientation)) {
					this.goStright(path);
					this.updateDatabse(maze);
					return;

				} else if (database[y + 1][x] == i && roadcheck(x, y + 1, orientation + 1)) {
					this.rotateRight(maze);
					this.goStright(path);
					this.updateDatabse(maze);
					return;

				} else if (database[y][x + 1] == i && roadcheck(x + 1, y, orientation - 1)) {
					this.rotateLeft(maze);
					this.goStright(path);
					this.updateDatabse(maze);
					return;

				}
			}
			if (this.deadRode(x, y) || rightcount == 7 || leftcount == 7)
				this.goBack(path, maze);
			else {
				if (turnorder == 0)
					this.rotateRight(maze);
				else
					this.rotateLeft(maze);
			}
			this.updateDatabse(maze);
			flag = 1;
			break;
		case 5:
			if (x == maxX - 1 || x == 0 || y == maxY - 1) {
				if (turnorder == 0)
					this.rotateRight(maze);
				else
					this.rotateLeft(maze);
				flag = 1;
				break;
			}
			for (int i = 1; i <= 2; i++) {
				if (database[y + 1][x] == i && roadcheck(x, y + 1, orientation)) {
					this.goStright(path);
					this.updateDatabse(maze);
					return;

				} else if (database[y + 1][x - 1] == i && roadcheck(x - 1, y + 1, orientation + 1)) {
					this.rotateRight(maze);
					this.goStright(path);
					this.updateDatabse(maze);
					return;

				} else if (database[y + 1][x + 1] == i && roadcheck(x + 1, y + 1, orientation - 1)) {
					this.rotateLeft(maze);
					this.goStright(path);
					this.updateDatabse(maze);
					return;

				}
			}
			if (this.deadRode(x, y) || rightcount == 7 || leftcount == 7)
				this.goBack(path, maze);
			else {
				if (turnorder == 0)
					this.rotateRight(maze);
				else
					this.rotateLeft(maze);
			}
			this.updateDatabse(maze);
			flag = 1;

			break;
		case 6:
			if (x == 0 || y == maxY - 1) {
				if (turnorder == 0)
					this.rotateRight(maze);
				else
					this.rotateLeft(maze);
				flag = 1;
				break;
			}
			for (int i = 1; i <= 2; i++) {
				if (database[y + 1][x - 1] == i && roadcheck(x - 1, y + 1, orientation)) {
					this.goStright(path);
					this.updateDatabse(maze);
					return;

				} else if (database[y][x - 1] == i && roadcheck(x - 1, y, orientation + 1)) {
					this.rotateRight(maze);
					this.goStright(path);
					this.updateDatabse(maze);
					return;

				} else if (database[y + 1][x] == i && roadcheck(x, y + 1, orientation - 1)) {
					this.rotateLeft(maze);
					this.goStright(path);
					this.updateDatabse(maze);
					return;

				}
			}
			if (this.deadRode(x, y) || rightcount == 7 || leftcount == 7)
				this.goBack(path, maze);
			else {
				if (turnorder == 0)
					this.rotateRight(maze);
				else
					this.rotateLeft(maze);
			}
			this.updateDatabse(maze);
			flag = 1;

			break;
		case 7:
			if (x == 0 || y == 0 || y == maxY - 1) {
				if (turnorder == 0)
					this.rotateRight(maze);
				else
					this.rotateLeft(maze);
				flag = 1;
				break;
			}
			for (int i = 1; i <= 2; i++) {
				if (database[y][x - 1] == i && roadcheck(x - 1, y, orientation)) {
					this.goStright(path);
					this.updateDatabse(maze);
					return;

				} else if (database[y - 1][x - 1] == i && roadcheck(x - 1, y - 1, orientation + 1)) {
					this.rotateRight(maze);
					this.goStright(path);
					this.updateDatabse(maze);
					return;
				} else if (database[y + 1][x - 1] == i && roadcheck(x - 1, y + 1, orientation - 1)) {
					this.rotateLeft(maze);
					this.goStright(path);
					this.updateDatabse(maze);
					return;

				}
			}

			if (this.deadRode(x, y) || rightcount == 7 || leftcount == 7)
				this.goBack(path, maze);
			else {
				if (turnorder == 0)
					this.rotateRight(maze);
				else
					this.rotateLeft(maze);
			}
			this.updateDatabse(maze);
			flag = 1;

			break;
		case 8:
			if (x == 0 || y == 0) {
				if (turnorder == 0)
					this.rotateRight(maze);
				else
					this.rotateLeft(maze);
				flag = 1;
				break;
			}
			for (int i = 1; i <= 2; i++) {
				if (database[y - 1][x - 1] == i && roadcheck(x - 1, y - 1, orientation)) {
					this.goStright(path);
					this.updateDatabse(maze);
					return;

				} else if (database[y - 1][x] == i && roadcheck(x, y - 1, orientation + 1)) {
					this.rotateRight(maze);
					this.goStright(path);
					this.updateDatabse(maze);
					return;

				} else if (database[y][x - 1] == i && roadcheck(x - 1, y, orientation - 1)) {
					this.rotateLeft(maze);
					this.goStright(path);
					this.updateDatabse(maze);
					return;

				}
			}

			if (this.deadRode(x, y) || rightcount == 7 || leftcount == 7)
				this.goBack(path, maze);
			else {
				if (turnorder == 0)
					this.rotateRight(maze);
				else
					this.rotateLeft(maze);
			}
			this.updateDatabse(maze);
			flag = 1;

			break;
		default:
			break;

		}

	}

	public boolean roadcheck(int x, int y, int ori) {
		if (ori == 9)
			ori = 1;
		if (ori == 0)
			ori = 8;

		for (int i = 0; i < path.size(); i++) {
			if (path.get(i)[0] == x && path.get(i)[1] == y && path.get(i)[2] == ori) {

				return false;
			}
		}
		return true;

	}

	public boolean deadRode(int x, int y) {
		int count = 0;
		if (x == 0 || x == database.length - 1 || y == 0 || y == database[0].length - 1) {
			orientation++;
			if (orientation == 9)
				orientation = 1;
		} else {
			for (int i = -1; i <= 1; i++)
				for (int j = -1; j <= 1; j++) {
					if (database[y + i][x + i] == 0 || database[y + i][x + i] == 3)
						count++;
				}
		}
		if (count >= 7)
			return true;
		return false;

	}

	public void go(int[][] maze) {

		this.updateDatabse(maze);
		this.makeDecision(database, path);

		System.out.println("Location(" + x + "," + y + ")  Orientation:" + orientation);

	}

}
