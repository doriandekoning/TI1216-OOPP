package application;

import application.animation.CalculateMatch.MainAIController;
import application.animation.Container.CalculatedMatch;
import application.animation.Container.CurrentPositions;
import application.animation.Container.Position;
import application.animation.Container.PlayerInfo;
import application.animation.PlayMatch.AnimateFootballMatch;
import application.model.Competition;
import application.model.Match;
import application.model.Team;
import java.util.ArrayList;

/**
 * This class' playMatch method can be used to handle everything which has
 * something to do with generating a football match, animating a football match
 * and choosing the players positions on the football field. The only thing it
 * needs are 2 teams with at least 11 players each.
 *
 * Some performance information: (tested on 27-12-2014) -Generating 800 matches
 * (without animating them of course), took a little less than 2 minutes (@ 2.4
 * GHz i7), so around 0.15 seconds per match. -After generating 800 matches, the
 * program was using less than 200 MB of memory, peaking at around 240 MB, so
 * there are no (major) memory leaks.
 *
 * Comparison to without cleaning the match data: (tested on 27-12-2014)
 * -Generating 800 matches (without animating them), took 1 and a half minute (@
 * 2.4 GHz i7), so around 0.11 seconds per match. -After generating 800 matches,
 * the program was using around 550 MB of memory, soon after starting with
 * generating the matches, the memory usage peaked at almost 800 MB of memory
 * usage, then afterwards stabalized at around 500 MB of memory usage, but
 * sometimes jumping up to 650 MB.
 *
 * In conclusion the java garbage collector seems to be pretty good, but for
 * optimal performance (in exceptional cases) it does need some help.
 */
public class PlayAnimation {

    private final static Object lockGeneration = new Object();
    private final static Object lockAnimation = new Object();
    private final static Object lockUpdateResults = new Object();
    private static int count;

    /**
     * Plays all matches of the current round and adjusts them in the
     * competition
     *
     * @return the match of the player
     */
    public static Match playMatches() {
        Competition competition = Main.getCompetition();
        String playerTeam = competition.getChosenTeamName();
        int round = competition.getRound();
        Match[] matches = competition.getRound(round - 1);

        count = 0;

        // start a thread which will calculate the results of the other teams, while
        // the player is playing his own match
        Thread matchThread = new Thread() {
            @Override
            public void run() {
                // for all matches: if it is NOT the players match, generate it without animation and store it.
                for (int i = 0; i < matches.length; i++) {
                    if (!matches[i].getHomeTeam().getName().equals(playerTeam) && !matches[i].getVisitorTeam().getName().equals(playerTeam)) {
                        matches[i] = playMatch(matches[i].getHomeTeam(), matches[i].getVisitorTeam(), false);
                    }
                }
                synchronized (lockUpdateResults) {
                    if (count > 0) {
                        competition.updateResults();
                    } else {
                        count++;
                    }
                }
            }
        };

        matchThread.start();

        // for all matches: if it IS the players match, generate, animation and return it.
        for (int i = 0; i < matches.length; i++) {
            if (matches[i].getHomeTeam().getName().equals(playerTeam) || matches[i].getVisitorTeam().getName().equals(playerTeam)) {
                matches[i] = playMatch(matches[i].getHomeTeam(), matches[i].getVisitorTeam(), true);
                synchronized (lockUpdateResults) {
                    if (count > 0) {
                        competition.updateResults();
                    } else {
                        count++;
                    }
                }
                return matches[i];
            }
        }

        return null; //return null if the play had no match (which shouldn't be possible)
    }

    /**
     * This method will make sure a football match will get generated and
     * animated, based on the 2 teams which are supplied by the parameters. It
     * will also let the player choose the positions of the players of his team.
     * Afterwards it will return a Match object, containing the results of the
     * generated match.
     *
     * @param homeTeam The home team
     * @param visitorTeam The visitor team
     * @param shouldAnimate If the match should be stored and animated, or only
     * generated
     * @return A Match object containing the generated match
     */
    private static Match playMatch(Team homeTeam, Team visitorTeam, boolean shouldAnimate) {

        // TODO: select team composition instead of the part for testing below this
        //ONLY FOR TESTING: *******************************
        //ally team:
        //enemy team:
        Object[] leftTeam = getDefaultLeftPositions(homeTeam);
        Object[] rightTeam = getDefaultRightPositions(visitorTeam);
        //************************************************* ^ ONLY FOR TESTING

        CalculatedMatch testMatch;
        // Make sure at most 1 thread can generate a match at a time (because a lot
        // of variables are static
        synchronized (lockGeneration) {
            System.out.println(!shouldAnimate ? "Thread generating" : "Main generating");
            // generate the match
            testMatch = (new MainAIController()).createMatch((PlayerInfo) leftTeam[0], (ArrayList) leftTeam[1], (ArrayList) leftTeam[2],
                    (ArrayList) leftTeam[3], (PlayerInfo) rightTeam[0], (ArrayList) rightTeam[1], (ArrayList) rightTeam[2], (ArrayList) rightTeam[3],
                    shouldAnimate);

            // reset generation variables
            CurrentPositions.reset();
        }
        System.out.println(!shouldAnimate ? "Thread done generating" : "Main done generating");

        // play the generated match's animation
        if (shouldAnimate) //only the players match should be animated, but to be safe, also synchroniza this
        {
            synchronized (lockAnimation) {
                System.out.println(!shouldAnimate ? "Thread animating" : "Main animating");
                AnimateFootballMatch.playMatch(testMatch);

                // reset animation variables
                AnimateFootballMatch.reset();
                System.out.println(!shouldAnimate ? "Thread done animating" : "Main done animating");
            }
        }

        // get the scores
        int scoreLeft = testMatch.getPosition(testMatch.amoutOfFrames() - 1).getScoreLeft();
        int scoreRight = testMatch.getPosition(testMatch.amoutOfFrames() - 1).getScoreRight();

        // Clear the memory used to store the match.
        // This does make a huge difference (see tests information in the comments above)
        testMatch = null;
        System.gc();

        return new Match(homeTeam, visitorTeam, scoreLeft, scoreRight);
    }

    /**
     * Get the default positions of the left team based on a team parameter
     *
     * @param t1 the team playing at the left side
     * @return Object[]: [0] = keeper, [1] = ArrayList defenders, [2] =
     * ArrayList midfielders, [3] = ArrayList defenders
     */
    private static Object[] getDefaultLeftPositions(Team t1) {
        Object[] result = new Object[4];
        result[0] = new PlayerInfo(70, 70, new Position(60, 381));

        // add default defender positions
        result[1] = new ArrayList<>();
        ((ArrayList) result[1]).add(new PlayerInfo(70, 70, 70, new Position(330, 160)));
        ((ArrayList) result[1]).add(new PlayerInfo(70, 70, 70, new Position(288, 290)));
        ((ArrayList) result[1]).add(new PlayerInfo(70, 70, 70, new Position(288, 467)));
        ((ArrayList) result[1]).add(new PlayerInfo(70, 70, 70, new Position(330, 605)));

        // add default midfielder positions
        result[2] = new ArrayList<>();
        ((ArrayList) result[2]).add(new PlayerInfo(70, 70, 70, new Position(550, 250)));
        ((ArrayList) result[2]).add(new PlayerInfo(70, 70, 70, new Position(450, 385)));
        ((ArrayList) result[2]).add(new PlayerInfo(70, 70, 70, new Position(550, 513)));

        // add default midfielder positions
        result[3] = new ArrayList<>();
        ((ArrayList) result[3]).add(new PlayerInfo(70, 70, 70, new Position(713, 259)));
        ((ArrayList) result[3]).add(new PlayerInfo(70, 70, 70, new Position(785, 385)));
        ((ArrayList) result[3]).add(new PlayerInfo(70, 70, 70, new Position(719, 494)));

        return result;
    }

    /**
     * Get the default positions of the right team based on a team parameter
     *
     * @param t1 the team playing at the right side
     * @return Object[]: [0] = keeper, [1] = ArrayList defenders, [2] =
     * ArrayList midfielders, [3] = ArrayList defenders
     */
    private static Object[] getDefaultRightPositions(Team t1) {
        Object[] result = new Object[4];
        result[0] = new PlayerInfo(70, 70, new Position(963, 381));

        // add default defender positions
        result[1] = new ArrayList<>();
        ((ArrayList) result[1]).add(new PlayerInfo(70, 70, 70, new Position(700, 160)));
        ((ArrayList) result[1]).add(new PlayerInfo(70, 70, 70, new Position(730, 290)));
        ((ArrayList) result[1]).add(new PlayerInfo(70, 70, 70, new Position(730, 467)));
        ((ArrayList) result[1]).add(new PlayerInfo(70, 70, 70, new Position(700, 605)));

        // add default midfielder positions
        result[2] = new ArrayList<>();
        ((ArrayList) result[2]).add(new PlayerInfo(70, 70, 70, new Position(461, 250)));
        ((ArrayList) result[2]).add(new PlayerInfo(70, 70, 70, new Position(562, 385)));
        ((ArrayList) result[2]).add(new PlayerInfo(70, 70, 70, new Position(461, 513)));

        // add default midfielder positions
        result[3] = new ArrayList<>();
        ((ArrayList) result[3]).add(new PlayerInfo(70, 70, 70, new Position(306, 259)));
        ((ArrayList) result[3]).add(new PlayerInfo(70, 70, 70, new Position(233, 385)));
        ((ArrayList) result[3]).add(new PlayerInfo(70, 70, 70, new Position(306, 494)));

        return result;
    }
}
