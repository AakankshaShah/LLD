import java.util.*;

class Snake {
    private Deque<Pair> body;
    private Map<Pair, Boolean> positionMap;

    public Snake() {
        this.body = new LinkedList<>();
        this.positionMap = new HashMap<>();
        Pair initialPos = new Pair(0, 0);
        this.body.offerFirst(initialPos);
        this.positionMap.put(initialPos, true);
    }

    public Pair getHead() {
        return body.peekFirst();
    }

    public Pair getTail() {
        return body.peekLast();
    }

    public void addHead(Pair newHead) {
        body.addFirst(newHead);
        positionMap.put(newHead, true);
    }

    public void removeTail() {
        Pair tail = body.pollLast();
        if (tail != null) {
            positionMap.remove(tail);
        }

    public boolean contains(Pair p) {
        return positionMap.containsKey(p);
    }

    public int size() {
        return body.size();
    }
}

class Food {
    private int[][] foodPositions;
    private int currentFoodIndex;

    public Food(int[][] foodPositions) {
        this.foodPositions = foodPositions;
        this.currentFoodIndex = 0;
    }
}

class Pair {
    int row;
    int col;

    public Pair(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}

interface MovementStrategy {
    Pair getNextPosition(Pair currentHead, String direction);
}

class HumanMovementStrategy implements MovementStrategy {
    @Override
    public Pair getNextPosition(Pair currentHead, String direction) {
        int row = currentHead.getKey();
        int col = currentHead.getValue();
        switch (direction) {
            case "U":
                return new Pair<>(row - 1, col);
            case "D":
                return new Pair<>(row + 1, col);
            case "L":
                return new Pair<>(row, col - 1);
            case "R":
                return new Pair<>(row, col + 1);
            default:
                return currentHead;
        }
    }
}

class AIMovementStrategy implements MovementStrategy {
    @Override
    public Pair getNextPosition(Pair currentHead, String direction) {
        return currentHead;
    }
}

abstract class FoodItem {
    protected int row, column;
    protected int points;

    public FoodItem(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public int getPoints() {
        return points;
    }
}

class NormalFood extends FoodItem {
    public NormalFood(int row, int column) {
        super(row, column);
        this.points = 1;
    }
}

class BonusFood extends FoodItem {
    public BonusFood(int row, int column) {
        super(row, column);
        this.points = 3;
    }
}

class FoodFactory {
    public static FoodItem createFood(int[] position, String type) {
        if ("bonus".equals(type)) {
            return new BonusFood(position[0], position[1]);
        }
        return new NormalFood(position[0], position[1]);
    }
}

public class GameBoard {
    private static GameBoard instance;
    private int width;
    private int height;

    private GameBoard(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public static GameBoard getInstance(int width, int height) {
        if (instance == null) {
            instance = new GameBoard(width, height);
        }
        return instance;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}

class SnakeGame {
    private GameBoard board;
    private Snake snake;
    private int[][] food;
    private int foodIndex;
    private MovementStrategy movementStrategy;

    public SnakeGame(int width, int height, int[][] food) {
        this.board = GameBoard.getInstance(width, height);
        this.food = food;
        this.foodIndex = 0;
        this.snake = new Snake();
        this.movementStrategy = new HumanMovementStrategy();
    }

    public int move(String direction) {
        Pair currentHead = snake.getHead();
        Pair newHead = movementStrategy.getNextPosition(currentHead, direction);

        int newRow = newHead.getRow();
        int newCol = newHead.getCol();

        boolean crossesBoundary = newRow < 0 || newRow >= board.getHeight()
                || newCol < 0 || newCol >= board.getWidth();

        boolean bitesItself = snake.contains(newHead) && !newHead.equals(snake.getTail());

        if (crossesBoundary || bitesItself) {
            return -1;
        }

        // Eat food?
        boolean ateFood = (foodIndex < food.length) &&
                (food[foodIndex][0] == newRow) &&
                (food[foodIndex][1] == newCol);

        if (!ateFood) {
            snake.removeTail();
        } else {
            foodIndex++;
        }

        snake.addHead(newHead);
        return snake.size() - 1; // score
    }
}
