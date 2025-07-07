import java.util.*;

enum PieceType {
    X,
    O
}

class PlayingPiece {
    PieceType pieceType;

    public PlayingPiece(PieceType pieceType) {
        this.pieceType = pieceType;
    }

    @Override
    public String toString() {
        return pieceType.name();
    }
}

class PieceX extends PlayingPiece {
    public PieceX() {
        super(PieceType.X);
    }
}

class PieceO extends PlayingPiece {
    public PieceO() {
        super(PieceType.O);
    }
}

class Board {
    int n;
    PlayingPiece[][] board;
    private int[] rows;
    private int[] cols;
    private int diag1, diag2;
    private int totalMoves;

    public Board(int n) {
        this.n = n;
        this.board = new PlayingPiece[n][n];
        this.rows = new int[n];
        this.cols = new int[n];
        this.diag1 = 0;
        this.diag2 = 0;
        this.totalMoves = 0;
    }

    public boolean placePiece(int row, int col, PlayingPiece piece) {
        if (row < 0 || row >= n || col < 0 || col >= n || board[row][col] != null) {
            return false;
        }

        board[row][col] = piece;

        int mark = piece.pieceType == PieceType.X ? 1 : -1;
        rows[row] += mark;
        cols[col] += mark;
        if (row == col) diag1 += mark;
        if (row + col == n - 1) diag2 += mark;

        totalMoves++;
        return true;
    }

    public boolean checkWinner(int row, int col, String markStr) {
        int mark = markStr.equals("X") ? 1 : -1;

        return Math.abs(rows[row]) == n ||
               Math.abs(cols[col]) == n ||
               Math.abs(diag1) == n ||
               Math.abs(diag2) == n;
    }

    public boolean isFull() {
        return totalMoves == n * n;
    }

    public void printBoard() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == null)
                    System.out.print("_ ");
                else
                    System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }
}

class Player {
    private String playerName;
    private PlayingPiece playingPiece;

    public Player(String playerName, PlayingPiece playingPiece) {
        this.playerName = playerName;
        this.playingPiece = playingPiece;
    }

    public String getPlayerName() {
        return playerName;
    }

    public PlayingPiece getPlayingPiece() {
        return playingPiece;
    }
}

class Game {
    private Board board;
    private Queue<Player> players;

    public Game(int boardSize, Player player1, Player player2) {
        board = new Board(boardSize);
        players = new LinkedList<>();
        players.add(player1);
        players.add(player2);
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        boolean gameOver = false;

        while (!gameOver) {
            board.printBoard();
            Player currentPlayer = players.poll();

            System.out.println(
                currentPlayer.getPlayerName() + "'s turn (" + currentPlayer.getPlayingPiece() + ")"
            );
            System.out.print("Enter row and column (e.g., 0 1): ");
            int row = scanner.nextInt();
            int col = scanner.nextInt();

            boolean placed = board.placePiece(row, col, currentPlayer.getPlayingPiece());

            if (!placed) {
                System.out.println("Invalid move! Try again.");
                players.add(currentPlayer); // Retry same player
                continue;
            }

            if (board.checkWinner(row, col, currentPlayer.getPlayingPiece().toString())) {
                board.printBoard();
                System.out.println(currentPlayer.getPlayerName() + " wins!");
                gameOver = true;
            } else if (board.isFull()) {
                board.printBoard();
                System.out.println("It's a draw!");
                gameOver = true;
            } else {
                players.add(currentPlayer); // Next turn
            }
        }

        scanner.close();
    }
}

public class TicTacToe {
    public static void main(String[] args) {
        Player player1 = new Player("Alice", new PieceX());
        Player player2 = new Player("Bob", new PieceO());

        Game game = new Game(3, player1, player2);
        game.start();
    }
}
// public boolean checkWinner(int row, int col, PieceType playerPiece) {
//     // Check row
//     boolean win = true;
//     for (int j = 0; j < n; j++) {
//         if (board[row][j] == null || board[row][j].pieceType != playerPiece) {
//             win = false;
//             break;
//         }
//     }
//     if (win) return true;

//     // Check column
//     win = true;
//     for (int i = 0; i < n; i++) {
//         if (board[i][col] == null || board[i][col].pieceType != playerPiece) {
//             win = false;
//             break;
//         }
//     }
//     if (win) return true;

//     // Check main diagonal
//     if (row == col) {
//         win = true;
//         for (int i = 0; i < n; i++) {
//             if (board[i][i] == null || board[i][i].pieceType != playerPiece) {
//                 win = false;
//                 break;
//             }
//         }
//         if (win) return true;
//     }

//     // Check anti-diagonal
//     if (row + col == n - 1) {
//         win = true;
//         for (int i = 0; i < n; i++) {
//             if (board[i][n - 1 - i] == null || board[i][n - 1 - i].pieceType != playerPiece) {
//                 win = false;
//                 break;
//             }
//         }
//         if (win) return true;
//     }

//     return false;
// }
