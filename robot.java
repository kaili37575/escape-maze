package robot;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import robot.Rover;
import robot.Maze;

public class robot {
	final static int MAZE_HEIGHT = 30;
	final static int MAZE_WIDTH = 30;

	BufferedImage img_maze = null;
	BufferedImage img_database = null;
	int[][] mazedata = new int[MAZE_WIDTH][MAZE_HEIGHT];
	int[][] database = new int[MAZE_WIDTH][MAZE_HEIGHT];
	Rover rover = new Rover();
	Maze maze = new Maze();

	public void display() {
		dspimg show = new dspimg();

		JFrame frame = new JFrame();
		JPanel panel = new JPanel();
		JTextField f1 = new JTextField();
		JButton go = new JButton("Go");

		frame.getContentPane().add(panel, BorderLayout.NORTH);
		frame.add(show);

		f1.setText("Input");
		f1.setBounds(0, 0, 50, 30);
		go.setBounds(0, 30, 50, 30);
		panel.add(go);
		panel.add(f1);

		frame.setVisible(true);
		frame.setSize(700, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		go.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int con = 5000;
				int rows = MAZE_WIDTH;
				int cols = MAZE_HEIGHT;
				int start_x = 4;
				int start_y = 4;
				int des_x = rows - 2;
				int des_y = cols - 2;
				int[] currentLoc = new int[3];
				int obstacleRate = 0;
				obstacleRate = Integer.valueOf(f1.getText());
				rover.initalDatabase(rows, cols);
				rover.setInitalState(start_x, start_y, 5);
				rover.setDestination(des_x, des_y);

				maze.setMaze(rows, cols, obstacleRate / 100.0);
				maze.generateMaze();
				maze.initialMaze(start_x, start_y, des_x, des_y);
				mazedata = maze.getMaze();
				File savedata = new File("output.txt");

				while (con > 0) {
					try {
						Thread.currentThread();
						Thread.sleep(30);

					} catch (Exception er) {
					}
					show.paintImmediately(0, 0, 700, 400);

					rover.go(mazedata);
					database = rover.getDatabase();

					currentLoc = rover.getCurrentState();

					int[] imgsrc_maze = new int[rows * cols];
					int[] imgsrc_database = new int[rows * cols];

					int count = 0;
					for (int i = 0; i < rows; i++)
						for (int j = 0; j < cols; j++) {// set free point to be
														// white (255,255,255)
							if (mazedata[i][j] != 0)
								imgsrc_maze[count] = 255 << 24 | 255 << 16 | 255 << 8 | 255;
							if (database[i][j] != 0)
								imgsrc_database[count] = 255 << 24 | 255 << 16 | 255 << 8 | 255;
							if (database[i][j] == 2) {// set robot path to be
														// yellow
								imgsrc_database[count] = 255 << 24 | 255 << 16 | 255 << 8 | 0;
								imgsrc_maze[count] = 255 << 24 | 255 << 16 | 255 << 8 | 0;
							}
							if (database[i][j] == 3) {
								imgsrc_database[count] = 255 << 24 | 170 << 16 | 170 << 8 | 0;
								imgsrc_maze[count] = 255 << 24 | 170 << 16 | 170 << 8 | 0;
							}
							if (i == start_x && j == start_y) {// set start
																// point to be
																// red (255,0,0)
								imgsrc_maze[count] = 255 << 24 | 255 << 16 | 0 << 8 | 0;
								imgsrc_database[count] = 255 << 24 | 255 << 16 | 0 << 8 | 0;
							} else if (i == des_x && j == des_y) {// set
																	// destination
																	// point to
																	// be green
																	// (0,255,0)
								imgsrc_maze[count] = 255 << 24 | 0 << 16 | 255 << 8 | 0;
								imgsrc_database[count] = 255 << 24 | 0 << 16 | 255 << 8 | 0;
							}
							if (i == currentLoc[1] && j == currentLoc[0]) {
								imgsrc_maze[count] = 200 << 24 | 0 << 16 | 200 << 8 | 0;
								imgsrc_database[count] = 200 << 24 | 0 << 16 | 200 << 8 | 0;
							}

							count++;
						}

					img_maze = new BufferedImage(rows, cols, BufferedImage.TYPE_INT_BGR);
					img_database = new BufferedImage(rows, cols, BufferedImage.TYPE_INT_BGR);
					img_maze.setRGB(0, 0, rows, cols, imgsrc_maze, 0, rows);
					img_database.setRGB(0, 0, rows, cols, imgsrc_database, 0, rows);
					con--;

					try {
						savedata.createNewFile();
						BufferedWriter out = new BufferedWriter(new FileWriter(savedata, true));
						out.write("Location(" + currentLoc[0] + "," + currentLoc[1] + ")  Orientation:" + currentLoc[2]
								+ "\r\n");
						out.flush();
						out.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					if (currentLoc[0] == des_x && currentLoc[1] == des_y) {

						System.out.println("Find a way out!");

						break;
					}
					if (con == 0) {
						System.out.println("Trapped in maze!");
						break;
					}
				}
			}

		});

		for (int i = 0; i < rover.path.size(); i++) {
			System.out.println(rover.path.get(i)[0] + "---" + rover.path.get(i)[1] + "---" + rover.path.get(i)[2]);
		}

	}

	public static void main(String[] args) {
		robot r = new robot();
		r.display();

	}

	public class dspimg extends JPanel {
		// show the image
		public dspimg() {
		}

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Color colour = Color.blue;
			int m = MAZE_WIDTH;
			int n = MAZE_HEIGHT;
			int resize_factor = 10;
			g.drawImage(img_maze, 0, 0, m * resize_factor, n * resize_factor, null);
			g.drawImage(img_database, m * resize_factor + 50, 0, m * resize_factor, n * resize_factor, null);

			int w = 0;
			int h = 0;
			g.setColor(colour);
			for (int i = 1; i <= n; i++) {
				for (int j = 1; j <= m; j++) {
					g.drawRect(w, h, resize_factor, resize_factor);
					g.drawRect(w + m * resize_factor + 50, h, resize_factor, resize_factor);
					w = w + resize_factor;
				}
				h = h + resize_factor;
				w = 0;
			}
			repaint();
		}
	}

}
