import java.util.Scanner;


enum Color { WHITE, BLACK; }

class Position {
    int row, col;
    Position(int r, int c) { this.row = r; this.col = c; }
}

abstract class Piece {
    Color color;
    char symbol;

    Piece(Color color, char symbol) {
        this.color = color;
        this.symbol = symbol;
    }

    abstract boolean canMove(Board board, Position from, Position to);
}

// King
class King extends Piece {
    King(Color color) { super(color, 'K'); }

    @Override
    boolean canMove(Board board, Position from, Position to) {
        int dr = Math.abs(from.row - to.row);
        int dc = Math.abs(from.col - to.col);
        return dr <= 1 && dc <= 1;
    }
}

// Queen
class Queen extends Piece {
    Queen(Color color) { super(color, 'Q'); }

    @Override
    boolean canMove(Board board, Position from, Position to) {
        return (from.row == to.row || from.col == to.col ||
                Math.abs(from.row - to.row) == Math.abs(from.col - to.col));
    }
}

// Board
class Board {
    Piece[][] grid = new Piece[8][8];

    Board() {
        grid[0][4] = new King(Color.WHITE);
        grid[7][4] = new King(Color.BLACK);
        grid[0][3] = new Queen(Color.WHITE);
        grid[7][3] = new Queen(Color.BLACK);
    }

    Piece getPiece(Position pos) { return grid[pos.row][pos.col]; }

    boolean movePiece(Position from, Position to, Color turn) {
        Piece piece = getPiece(from);
        if (piece == null || piece.color != turn) return false;

        if (!piece.canMove(this, from, to)) return false;

        grid[to.row][to.col] = piece; // place piece in new square
        grid[from.row][from.col] = null; // clear old square
        return true;
    }

    void printBoard() {
        for (int i = 7; i >= 0; i--) {
            for (int j = 0; j < 8; j++) {
                Piece p = grid[i][j];
                if (p == null) System.out.print(". ");
                else System.out.print((p.color == Color.WHITE ? p.symbol : Character.toLowerCase(p.symbol)) + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}


class Player {
    String name;
    Color color;

    Player(String name, Color color) {
        this.name = name;
        this.color = color;
    }
}

class Game {
    Board board = new Board();
    Player whitePlayer, blackPlayer;
    Player currentPlayer;

    Game(Player white, Player black) {
        this.whitePlayer = white;
        this.blackPlayer = black;
        this.currentPlayer = whitePlayer;
    }

    void start() {
        Scanner sc = new Scanner(System.in);
        System.out.println("♟️ Chess Started!");
        board.printBoard();

        while (true) {
            System.out.println(currentPlayer.name + " (" + currentPlayer.color + ")'s turn. Enter move (r1 c1 r2 c2): ");
            int r1 = sc.nextInt(), c1 = sc.nextInt();
            int r2 = sc.nextInt(), c2 = sc.nextInt();

            if (board.movePiece(new Position(r1, c1), new Position(r2, c2), currentPlayer.color)) {
                board.printBoard();
                switchTurn();
            } else {
                System.out.println("❌ Invalid move, try again!");
            }
        }
    }

    void switchTurn() {
        currentPlayer = (currentPlayer == whitePlayer ? blackPlayer : whitePlayer);
    }
}

// Main
public class ChessWithPlayers {
    public static void main(String[] args) {
        Player white = new Player("Alice", Color.WHITE);
        Player black = new Player("Bob", Color.BLACK);

        Game game = new Game(white, black);
        game.start();
    }
}
