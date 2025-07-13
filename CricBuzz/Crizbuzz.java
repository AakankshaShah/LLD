import java.util.*;


class Player {
    int playerId;
    String name;
    boolean isBatsman;
    boolean isBowler;
}

class Team {
    int teamId;
    String name;
    List<Player> players;
}

class Match {
    int matchId;
    Team teamA;
    Team teamB;
    List<Innings> inningsList = new ArrayList<>();
}
class Innings {
    Team battingTeam;
    Team bowlingTeam;
    List<Over> overs = new ArrayList<>();
    Scorecard scorecard = new Scorecard();
}
class Scorecard {
    int totalRuns;
    int totalWickets;
    int totalOvers;
    Map<Player, Integer> batsmanRuns = new HashMap<>();
    Map<Player, Integer> batsmanBalls = new HashMap<>();
    Map<Player, Integer> bowlerWickets = new HashMap<>();
    Map<Player, Integer> bowlerRuns = new HashMap<>();
    Map<Player, Integer> bowlerBalls = new HashMap<>();
}

class Over {
    List<BallDetails> balls = new ArrayList<>();
    Player bowler;
}
class BallDetails {
    Player batsman;
    RunType runType;
    BallType ballType;
    int runs;
    Wicket wicket;
    Player bowler;
}

class Wicket {
    WicketType wicketType;
    Player outPlayer;
    Player bowler;
}

enum RunType {
    NORMAL, FOUR, SIX, LEG_BYE, BYE, NO_BALL, WIDE
}

enum BallType {
    LEGIT, NO_BALL, WIDE
}

enum WicketType {
    BOWLED, CAUGHT, RUN_OUT, LBW, STUMPED
}
interface ScoreUpdaterObserver {
    void update(BallDetails ball, Scorecard scorecard);
}

class BallScoreUpdater implements ScoreUpdaterObserver {
    public void update(BallDetails ball, Scorecard scorecard) {
        Player batsman = ball.batsman;
        scorecard.totalRuns += ball.runs;

        scorecard.batsmanRuns.put(batsman,
            scorecard.batsmanRuns.getOrDefault(batsman, 0) + ball.runs);

        scorecard.batsmanBalls.put(batsman,
            scorecard.batsmanBalls.getOrDefault(batsman, 0) + 1);
    }
}

class BowlingScoreUpdater implements ScoreUpdaterObserver {
    public void update(BallDetails ball, Scorecard scorecard) {
        Player bowler = ball.bowler;

        scorecard.bowlerBalls.put(bowler,
            scorecard.bowlerBalls.getOrDefault(bowler, 0) + 1);

        scorecard.bowlerRuns.put(bowler,
            scorecard.bowlerRuns.getOrDefault(bowler, 0) + ball.runs);

        if (ball.wicket != null && ball.wicket.outPlayer != null) {
            scorecard.totalWickets++;
            scorecard.bowlerWickets.put(bowler,
                scorecard.bowlerWickets.getOrDefault(bowler, 0) + 1);
        }
    }
}
class ScorePublisher {
    private final List<ScoreUpdaterObserver> observers = new ArrayList<>();

    public void register(ScoreUpdaterObserver observer) {
        observers.add(observer);
    }

    public void publish(BallDetails ball, Scorecard scorecard) {
        for (ScoreUpdaterObserver obs : observers) {
            obs.update(ball, scorecard);
        }
    }
}
class BallProcessor {
    private final ScorePublisher publisher;
    private final Innings innings;
    private Over currentOver = new Over();
    private int ballInOver = 0;

    public BallProcessor(Innings innings, ScorePublisher publisher) {
        this.innings = innings;
        this.publisher = publisher;
    }

    public void processBall(BallDetails ball) {
        currentOver.balls.add(ball);
        ballInOver++;

        publisher.publish(ball, innings.scorecard);

        boolean isLegal = ball.ballType == BallType.LEGIT;
        if (isLegal && ballInOver == 6) {
            // Over complete
            currentOver.bowler = ball.bowler;
            innings.overs.add(currentOver);
            innings.scorecard.totalOvers++;

            // Reset for new over
            currentOver = new Over();
            ballInOver = 0;
        }

        // Handle innings end (optional): e.g. if 10 wickets fall
        if (innings.scorecard.totalWickets >= 10) {
            System.out.println("Innings over!");
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Player p1 = new Player(); p1.name = "Kohli";
        Player p2 = new Player(); p2.name = "Bumrah";

        Team teamA = new Team(); teamA.name = "India"; teamA.players = Arrays.asList(p1);
        Team teamB = new Team(); teamB.name = "Australia"; teamB.players = Arrays.asList(p2);

        Innings innings = new Innings();
        innings.battingTeam = teamA;
        innings.bowlingTeam = teamB;

        ScorePublisher publisher = new ScorePublisher();
        publisher.register(new BallScoreUpdater());
        publisher.register(new BowlingScoreUpdater());

        BallProcessor processor = new BallProcessor(innings, publisher);

        // Simulate some balls
        for (int i = 0; i < 6; i++) {
            BallDetails ball = new BallDetails();
            ball.batsman = p1;
            ball.bowler = p2;
            ball.runs = i;  // Varying runs
            ball.ballType = BallType.LEGIT;
            ball.runType = RunType.NORMAL;

            processor.processBall(ball);
        }

        System.out.println("Overs bowled: " + innings.scorecard.totalOvers);
        System.out.println("Total Runs: " + innings.scorecard.totalRuns);
    }
}
// Can add Match Type
