package edu.dsbbproj.nikoinmaze;

import java.util.*;

public class ARAStarMazeSolver {
    private static final int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
    private final Maze maze;
    private final int rows;
    private final int cols;
    private final double[][] gValues;

    public ARAStarMazeSolver(Maze maze) { //maze
        this.maze = maze;
        this.rows = this.maze.getMaze().length;
        this.cols = this.maze.getMaze()[0].length;
        this.gValues = new double[rows][cols];
    }

    public List<Point> findPath(Point start, Point end) {
        if (isNotValidPoint(start) || isNotValidPoint(end)) {
            return null;
        }

        if (maze.getMaze()[start.x][start.y] == 1 || maze.getMaze()[end.x][end.y] == 1) {
            return null;
        }

        PriorityQueue<Point> openSet = new PriorityQueue<>(Comparator.comparingDouble(a -> a.f));
        Set<Point> closedSet = new HashSet<>();

        initializeGValues();
        start.g = 0;
        start.f = maze.getE() * calculateHeuristic(start,end);
        openSet.add(start);

        while (!openSet.isEmpty()) {
            Point current = openSet.poll();
            closedSet.add(current);

            if (current.x == end.x && current.y == end.y) {
                return reconstructPath(current);
            }

            for (int[] direction : DIRECTIONS) {
                int newX = current.x + direction[0];
                int newY = current.y + direction[1];

                Point neighbor = new Point(newX, newY);
                if (isNotValidPoint(neighbor) || maze.getMaze()[newX][newY] == 1 || closedSet.contains(neighbor)) {
                    continue;
                }

                double tentativeG = current.g + getCost(current, neighbor);

                if (tentativeG < gValues[newX][newY]) {
                    gValues[newX][newY] = tentativeG;
                    neighbor.g = tentativeG;
                    neighbor.h = calculateHeuristic(neighbor, end);
                    neighbor.f = neighbor.g + maze.getE() * neighbor.h;
                    neighbor.parent = current;

                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                    }
                }
            }
        }

        return null;
    }

    private boolean isNotValidPoint(Point point) {
        return point.x < 0 || point.x >= rows || point.y < 0 || point.y >= cols;
    }

    private void initializeGValues() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                gValues[i][j] = Double.POSITIVE_INFINITY;
            }
        }
    }

    private double getCost(Point current, Point neighbor) {
        int dx = Math.abs(current.x - neighbor.x);
        int dy = Math.abs(current.y - neighbor.y);

        // 对角线移动的代价为根号2
        if (dx == 1 && dy == 1) {
            return Math.sqrt(2);
        }

        return 1;
    }

    private double calculateHeuristic(Point start, Point end) {
        return Math.hypot(start.x-end.x,start.y-end.y);
    }

    private List<Point> reconstructPath(Point current) {
        List<Point> path = new ArrayList<>();
        while (current != null) {
            path.add(0, current);
            current = current.parent;
        }
        return path;
    }
}

