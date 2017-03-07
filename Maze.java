package robot;

public class Maze {
	public int width=0;
	public int height=0;
	public double persent=0;
	public int[][] maze;
	public void setMaze(int w,int h,double p){
		this.width=w;
		this.height=h;
		this.persent=p;
	}
	public int getWidth(){
		return this.width;
	}
	public int getHeight(){
		return this.height;
	}
	public void generateMaze(){
		maze=new int[width][height];
		for(int i=0;i<width;i++)
			for(int j=0;j<height;j++){
				maze[i][j]=(Math.random()>persent?1:0);
			}
	}
	public void initialMaze(int start_x,int start_y,int des_x,int des_y){
		maze[start_x][start_y]=1;
		maze[des_x][des_y]=1;
	}
	public int[][] getMaze(){
		return this.maze;
	}
}
