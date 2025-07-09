import java.util.*;

class Obstacle {
    int start;
    int end;

    public Obstacle(int start, int end) {
        this.start = start;
        this.end = end;
    }
}

class Snake extends Obstacle {
    public Snake(int start, int end) {
        super(start, end);
    }
}

class Ladder extends Obstacle {
    public Ladder(int start, int end) {
        super(start, end);
    }
}

class Dice {
    int n;

    public Dice(int n) {
        this.n = n;
    }

    public int rollDice() {
        return (int)(Math.random() * n) + 1;  // fix: cast properly and use parentheses
    }
}

class Player {
    int playerId;
    String playerName;
    String playerColor;
    int currentPosition;

    public Player(int id, String name, String color) {
        this.playerId = id;
        this.playerName = name;
        this.playerColor = color;
        this.currentPosition = 0;
    }
}

class Game {
    Map<Integer, Integer> obstacleMap;  // Maps start -> end
    Queue<Player> players;
    Dice dice;
    int boardSize;

    public Game(List<Obstacle> obstacles, List<Player> players, int boardSize, Dice dice) {
        this.obstacleMap = new HashMap<>();
        for (Obstacle obs : obstacles) {
            obstacleMap.put(obs.start, obs.end);
        }
        this.players = new LinkedList<>(players);
        this.boardSize = boardSize;
        this.dice = dice;
    }

    public void start() {
        while (true) {
            Player current = players.poll();
            int roll = dice.rollDice();
            System.out.println(current.playerName + " rolled " + roll);

            int nextPos = current.currentPosition + roll;
            if (nextPos > boardSize) {
                System.out.println(current.playerName + " stays at " + current.currentPosition);
                players.offer(current);
                continue;
            }

            // check for snake or ladder
            if (obstacleMap.containsKey(nextPos)) {
                int end = obstacleMap.get(nextPos);
                if (end < nextPos) {
                    System.out.println("Oops! Snake from " + nextPos + " to " + end);
                } else {
                    System.out.println("Yay! Ladder from " + nextPos + " to " + end);
                }
                nextPos = end;
            }

            current.currentPosition = nextPos;
            System.out.println(current.playerName + " moved to " + nextPos);

            if (nextPos == boardSize) {
                System.out.println(current.playerName + " wins!");
                break;
            }

            players.offer(current);
        }
    }
}
public class SnakeandLadder {
    public static void main(String[] args) {
        List<Obstacle> obstacles = Arrays.asList(
            new Snake(14, 7),
            new Ladder(3, 22),
            new Snake(99, 12),
            new Ladder(10, 90)
        );

        List<Player> players = Arrays.asList(
            new Player(1, "Alice", "Red"),
            new Player(2, "Bob", "Blue")
        );

        Dice dice = new Dice(6);
        Game game = new Game(obstacles, players, 100, dice);
        game.start();
    }
}
