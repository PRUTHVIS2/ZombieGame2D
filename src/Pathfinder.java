import java.awt.Point;
import java.util.List;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Comparator;

public class Pathfinder {
    private static final int[][] DIRECTIONS = {
        {0, 1}, {1, 0}, {0, -1}, {-1, 0}, // 4-directional movement
        {1, 1}, {1, -1}, {-1, 1}, {-1, -1} // diagonal movement
    };
    
    private static final float STRAIGHT_COST = 1.0f;
    private static final float DIAGONAL_COST = 1.414f; // sqrt(2)
    
    // Inner class to represent a node in the pathfinding algorithm
    private static class Node {
        Point point;
        float gScore; // Cost from start
        float hScore; // Heuristic cost to goal
        float fScore; // gScore + hScore

        Node(Point point, float gScore, float hScore) {
            this.point = point;
            this.gScore = gScore;
            this.hScore = hScore;
            this.fScore = gScore + hScore;
        }
    }
    
    public Pathfinder() {
    }

    public List<Point> findPath(Point start, Point goal, int[][] collisionMap) {
        List<Point> path = new ArrayList<>();

        if (collisionMap == null || start == null || goal == null) {
            return path;
        }

        int mapHeight = collisionMap.length;
        int mapWidth = collisionMap[0].length;

        // Clamp coordinates to valid range
        int startX = Math.max(0, Math.min(start.x, mapWidth - 1));
        int startY = Math.max(0, Math.min(start.y, mapHeight - 1));
        int goalX = Math.max(0, Math.min(goal.x, mapWidth - 1));
        int goalY = Math.max(0, Math.min(goal.y, mapHeight - 1));

        // If start or goal is on a collision tile, return empty path
        if (collisionMap[startY][startX] != 0 || collisionMap[goalY][goalX] != 0) {
            return path;
        }

        Point startPoint = new Point(startX, startY);
        Point goalPoint = new Point(goalX, goalY);

        // Open set - nodes to be evaluated
        PriorityQueue<Node> openSet = new PriorityQueue<>((n1, n2) -> Float.compare(n1.fScore, n2.fScore));
        // Closed set - nodes already evaluated
        HashSet<String> closedSet = new HashSet<>();
        // Track the best path
        HashMap<String, Node> cameFrom = new HashMap<>();

        Node startNode = new Node(startPoint, 0, heuristic(startPoint, goalPoint));
        openSet.add(startNode);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();
            
            if (current.point.equals(goalPoint)) {
                // Reconstruct path
                path = reconstructPath(cameFrom, current);
                return path;
            }

            String currentKey = current.point.x + "," + current.point.y;
            closedSet.add(currentKey);

            // Check all neighbors
            for (int i = 0; i < DIRECTIONS.length; i++) {
                int newX = current.point.x + DIRECTIONS[i][0];
                int newY = current.point.y + DIRECTIONS[i][1];

                // Check bounds
                if (newX < 0 || newX >= mapWidth || newY < 0 || newY >= mapHeight) {
                    continue;
                }

                // Check if walkable
                if (collisionMap[newY][newX] != 0) {
                    continue;
                }

                String neighborKey = newX + "," + newY;
                if (closedSet.contains(neighborKey)) {
                    continue;
                }

                float cost = (i < 4) ? STRAIGHT_COST : DIAGONAL_COST;
                float gScore = current.gScore + cost;
                Point neighborPoint = new Point(newX, newY);
                float hScore = heuristic(neighborPoint, goalPoint);
                Node neighbor = new Node(neighborPoint, gScore, hScore);

                // Check if this path is better than any previous path to this neighbor
                boolean isNewNode = true;
                for (Node node : openSet) {
                    if (node.point.equals(neighborPoint)) {
                        if (gScore < node.gScore) {
                            openSet.remove(node);
                            openSet.add(neighbor);
                            cameFrom.put(neighborKey, current);
                        }
                        isNewNode = false;
                        break;
                    }
                }

                if (isNewNode) {
                    openSet.add(neighbor);
                    cameFrom.put(neighborKey, current);
                }
            }
        }

        // No path found
        return path;
    }

    private List<Point> reconstructPath(HashMap<String, Node> cameFrom, Node current) {
        List<Point> path = new ArrayList<>();
        path.add(current.point);

        String currentKey = current.point.x + "," + current.point.y;
        while (cameFrom.containsKey(currentKey)) {
            current = cameFrom.get(currentKey);
            path.add(0, current.point);
            currentKey = current.point.x + "," + current.point.y;
        }

        return path;
    }

    private float heuristic(Point a, Point b) {
        // Manhattan distance heuristic
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }
}
