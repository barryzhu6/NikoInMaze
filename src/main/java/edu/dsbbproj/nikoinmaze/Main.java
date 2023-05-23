package edu.dsbbproj.nikoinmaze;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    private static Maze maze;

    public static Maze getMaze() {
        return maze;
    }

    public static void main (String[] args) {
        try {
            Scanner in = new Scanner(System.in);
            int n = in.nextInt();
            int m = in.nextInt();
            int e = in.nextInt();
            maze = new Maze(n, m, e);
            int[][] mazeValue = new int[n][m];

            for (int i = 0; i < n; i++)
                for (int j = 0; j < m; j++)
                    mazeValue[i][j] = in.nextInt();

            maze.setMaze(mazeValue);
            maze.setP(in.nextInt());

            for (int i = 0; i < maze.getP(); i++) {
                int t = in.nextInt();
                int x = in.nextInt();
                int y = in.nextInt();
                maze.setAttacks(t,x,y);
            }

            maze.setK(in.nextInt());

            for (int i = 0; i < maze.getK(); i++) maze.setAskTimes(in.nextInt());
            if (args.length != 0 && args[0].toLowerCase(Locale.ROOT).equals("terminal")){
                ARAStarMazeSolver solver = new ARAStarMazeSolver(maze);
                Point end = new Point(n-1, m-1);
                while (maze.getE() >= 0) {
                    maze.magic();
                    Point start = new Point(maze.getX(), maze.getY());
                    List<Point> path = solver.findPath(start, end);
                    maze.answerKun(path);
                    if (path.size() > 1) {
                        start = new Point(path.get(1).x,path.get(1).y);
                        maze.move(start.x, start.y);
                    }
                    if (start.x == n-1 && start.y == m-1) {
                        break;
                    }
                }
                System.out.println(maze.currentPath.size());
                for (int i = 0; i < maze.currentPath.size(); i++)
                    System.out.print(maze.currentPath.get(i).x + " " + maze.currentPath.get(i).y + " ");
            }
            else if (args.length != 0 && args[0].toLowerCase(Locale.ROOT).equals("gui")) {
                Application.launch(MazeApplication.class, args);
            }
            else
                throw new IllegalArgumentException("Choose Mode Terminal or GUI");

        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }
}

