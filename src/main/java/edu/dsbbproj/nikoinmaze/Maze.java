package edu.dsbbproj.nikoinmaze;

import java.util.*;

class Point {
    int x;
    int y;
    double f;
    double g;
    double h;
    Point parent;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
        this.g = Double.POSITIVE_INFINITY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point point)) return false;
        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}

public class Maze {
    private class MagicAttack {
        private int x;
        private int y;
        private int t;
        public MagicAttack(int t, int x, int y) {
            this.t = t;
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getT() {
            return t;
        }
    }
    private int[][] maze;
    private int e; //e: ARA*;
    private int p, k, t; // p: magic times; k: ask times; t: current time
    private final Queue<MagicAttack> attacks = new PriorityQueue<>(Comparator.comparingInt(MagicAttack::getT).reversed());
    private final Queue<Integer> askTimes = new PriorityQueue<>((a, b) -> Integer.compare(b,a));
    private int x, y; //current position
    public ArrayList<Point> currentPath = new ArrayList<>();

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    //initialize
    Maze(int n, int m, int e) {
        maze = new int[n][m];
        this.e = e;
        this.p = 0;
        this.k = 0;
        this.t = 0;
        this.x = 0;
        this.y = 0;
        currentPath.add(new Point(0,0));
    }

    //Test maze set
    public void setMaze(int[][] maze) { //Only for test
        this.maze = maze;
    }

    //get & set & +- method
    public int[][] getMaze() {
        return maze;
    }

    public int getE() {
        return e;
    }

    public void eMinus() {
        e--;
    }

    public int getP() {
        return p;
    }

    public void setP(int p) {
        this.p = p;
    }

    public void pMinus() {
        p--;
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }

    public int getT() {
        return t;
    }

    public void setAttacks(int t, int x, int y) {
        attacks.add(new MagicAttack(t, x, y));
    }

    public void setAskTimes(int t) {
        askTimes.add(t);
    }
    public int[] magic() { //set barrier
        if (attacks.isEmpty()) return null;
        if (attacks.peek().t != e) {
            return null;
        }
        MagicAttack attack = attacks.poll();
        if (attack != null) {
            int ax = attack.getX();
            int ay = attack.getY();
            try {
                if (ax != x || ay != y) {
                    maze[ax][ay] = 1;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                return null;
            }
            return new int[]{ax, ay};
        } else return null;
    }

    public void answerKun(List<Point> path) { //answer KUNKUN
        if (askTimes.isEmpty()) {
            return;
        }
        if (askTimes.peek() != e){
            return;
        }
        askTimes.poll();
        if (path != null) {
            System.out.println( path.size() );

            for (Point point : path) {
                System.out.print(point.x + " " + point.y + " ");
            }
            System.out.println();
        }
    }

    public void outputMaze() {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++)
                if(i==x && j==y){
                    System.out.print("* ");
                } else System.out.printf("%d ", maze[i][j]);
            System.out.println();
        }
    }

    public void move(int x, int y) {
        //move: change x or y
        this.x = x;
        this.y = y;
        currentPath.add(new Point(x, y));
        e--;
        t++;
    }

    public boolean answerKunGui() {
        if (askTimes.isEmpty()) {
            return false;
        }
        if (askTimes.peek() != e){
            return false;
        }
        askTimes.poll();
        return true;
    }
}