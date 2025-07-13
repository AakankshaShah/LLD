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
class Main {
    public static void main(String[] args) {
        Player p1 = new Player(); p1.name = "Kohli";
        Player p2 = new Player(); p2.name = "Bumrah";

        Scorecard scorecard = new Scorecard();
        ScorePublisher publisher = new ScorePublisher();
        publisher.register(new BallScoreUpdater());
        publisher.register(new BowlingScoreUpdater());

        BallDetails ball = new BallDetails();
        ball.batsman = p1;
        ball.bowler = p2;
        ball.runs = 4;
        ball.runType = RunType.FOUR;
        ball.ballType = BallType.LEGIT;
        publisher.publish(ball, scorecard);

        BallDetails ball2 = new BallDetails();
        ball2.batsman = p1;
        ball2.bowler = p2;
        ball2.runs = 0;
        ball2.ballType = BallType.LEGIT;
        ball2.wicket = new Wicket();
        ball2.wicket.outPlayer = p1;
        ball2.wicket.bowler = p2;
        ball2.wicket.wicketType = WicketType.BOWLED;

        // publish wicket
        publisher.publish(ball2, scorecard);

        System.out.println("Total Runs: " + scorecard.totalRuns);
        System.out.println("Total Wickets: " + scorecard.totalWickets);
        System.out.println("Batting Stats:");
        for (Map.Entry<Player, Integer> entry : scorecard.batsmanRuns.entrySet()) {
            System.out.println(entry.getKey().name + ": " + entry.getValue() + " runs in " + scorecard.batsmanBalls.get(entry.getKey()) + " balls");
        }
        System.out.println("Bowling Stats:");
        for (Map.Entry<Player, Integer> entry : scorecard.bowlerWickets.entrySet()) {
            System.out.println(entry.getKey().name + ": " + entry.getValue() + " wickets, " + scorecard.bowlerRuns.get(entry.getKey()) + " runs in " + scorecard.bowlerBalls.get(entry.getKey()) + " balls");
        }
    }
}
// Can add Match Type
