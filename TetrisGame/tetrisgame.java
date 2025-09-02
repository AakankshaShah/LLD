interface IRotationStrategy {
    void rotate(Tetromino tetromino, GameBoard board);
}

// Strategy for Standard Rotation (90-degree)
class StandardRotationStrategy implements IRotationStrategy {
    @Override
    public void rotate(Tetromino tetromino, GameBoard board) {
        // Algorithm to rotate a tetromino 90 degrees clockwise
        // Rotate 2D array representation of tetromino
        int[][] shape = tetromino.getShape();
        int N = shape.length;
        int[][] rotated = new int[N][N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                rotated[j][N - i - 1] = shape[i][j];
            }
        }

        if (!board.isCollision(rotated, tetromino.getX(), tetromino.getY())) {
            tetromino.setShape(rotated);
        }
    }
}

// Abstract class for Tetromino
abstract class Tetromino {
    protected int[][] shape;
    protected int x, y; // position on the board
    protected IRotationStrategy rotationStrategy;

    public Tetromino(IRotationStrategy strategy) {
        this.rotationStrategy = strategy;
    }

    public void rotate(GameBoard board) {
        rotationStrategy.rotate(this, board);
    }

    public void moveLeft(GameBoard board) {
        if (!board.isCollision(shape, x - 1, y)) {
            x--;
        }
    }

    public void moveRight(GameBoard board) {
        if (!board.isCollision(shape, x + 1, y)) {
            x++;
        }
    }

    public void moveDown(GameBoard board) {
        if (!board.isCollision(shape, x, y + 1)) {
            y++;
        } else {
            board.placeTetromino(this);
        }
    }

    public int[][] getShape() {
        return shape;
    }

    public void setShape(int[][] shape) {
        this.shape = shape;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

// L-shaped Tetromino
class LTetromino extends Tetromino {
    public LTetromino() {
        super(new StandardRotationStrategy());
        this.shape = new int[][] {
            {1, 0},
            {1, 0},
            {1, 1}
        };
        this.x = 3;
        this.y = 0;
    }
}

// Factory to create Tetrominoes
class TetrominoFactory {
    public static Tetromino createTetromino(char type) {
        switch (type) {
            case 'L': return new LTetromino();
            case 'T': return new TTetromino();
            case 'I': return new ITetromino();
            // Add more tetromino types
            default: throw new IllegalArgumentException("Unknown Tetromino type");
        }
    }
}

// Singleton for GameBoard
class GameBoard {
    private static final int WIDTH = 10;
    private static final int HEIGHT = 20;
    private static GameBoard instance = null;
    private int[][] board;
    private int score;

    private GameBoard() {
        this.board = new int[HEIGHT][WIDTH];
    }

    public static GameBoard getInstance() {
        if (instance == null) {
            instance = new GameBoard();
        }
        return instance;
    }

    public boolean isCollision(int[][] shape, int x, int y) {
        // Check for collisions with walls, floor, or other blocks
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] != 0) {
                    if (y + i >= HEIGHT || x + j < 0 || x + j >= WIDTH || board[y + i][x + j] != 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void placeTetromino(Tetromino tetromino) {
        int[][] shape = tetromino.getShape();
        int x = tetromino.getX();
        int y = tetromino.getY();

        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] != 0) {
                    board[y + i][x + j] = shape[i][j];
                }
            }
        }
        checkLineCompletion();
    }

    private void checkLineCompletion() {
        for (int i = 0; i < HEIGHT; i++) {
            boolean isFullLine = true;
            for (int j = 0; j < WIDTH; j++) {
                if (board[i][j] == 0) {
                    isFullLine = false;
                    break;
                }
            }
            if (isFullLine) {
                clearLine(i);
                score += 100;
            }
        }
    }

    private void clearLine(int line) {
        for (int i = line; i > 0; i--) {
            System.arraycopy(board[i - 1], 0, board[i], 0, WIDTH);
        }
        // Clear top line
        for (int j = 0; j < WIDTH; j++) {
            board[0][j] = 0;
        }
    }

    public void renderBoard() {
        // Render the board to console
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                System.out.print(board[i][j] == 0 ? "." : "#");
            }
            System.out.println();
        }
        System.out.println("Score: " + score);
    }
}

// Game class
class TetrisGame {
    private GameBoard board;
    private Tetromino currentTetromino;

    public TetrisGame() {
        board = GameBoard.getInstance();
        currentTetromino = TetrominoFactory.createTetromino('L');  // Start with an 'L' Tetromino
    }

    public void startGame() {
        while (true) {
            board.renderBoard();
            // Handle user inputs for moving the Tetromino (omitted for brevity)
        }
    }
}

// Command Interface for handling user inputs
interface ICommand {
    void execute();
}

class MoveLeftCommand implements ICommand {
    private Tetromino tetromino;
    private GameBoard board;

    public MoveLeftCommand(Tetromino tetromino, GameBoard board) {
        this.tetromino = tetromino;
        this.board = board;
    }

    @Override
    public void execute() {
        tetromino.moveLeft(board);
    }
}

class MoveRightCommand implements ICommand {
    private Tetromino tetromino;
    private GameBoard board;

    public MoveRightCommand(Tetromino tetromino, GameBoard board) {
        this.tetromino = tetromino;
        this.board = board;
    }

    @Override
    public void execute() {
        tetromino.moveRight(board);
    }
}

// Main Class
public class Main {
    public static void main(String[] args) {
        TetrisGame game = new TetrisGame();
        game.startGame();
    }
}