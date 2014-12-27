package application;

import application.animation.CalculateMatch.MainAIController;
import application.animation.ContainerPackage.AnimatedMatch;
import application.animation.ContainerPackage.CurrentPositions;
import application.animation.ContainerPackage.ExactPosition;
import application.animation.ContainerPackage.PlayerInfo;
import application.animation.Playmatch.AnimateFootballMatch;
import application.model.Goalkeeper;
import application.model.Match;
import application.model.Player;
import application.model.Team;
import java.util.ArrayList;

    /*
    Some performance information:
    (tested on 27-12-2014)
    -Generating 800 matches (without animating them of course), took a little less
    than 2 minutes (@ 2.4 GHz i7), so around 0.15 seconds per match.
    -After generating 800 matches, the program was using less than 200 MB of memory,
    peaking at around 240 MB, so there are no (major) memory leaks.
    
    Comparison to without cleaning the match data:
    (tested on 27-12-2014)
    -Generating 800 matches (without animating them), took 1 and a half minute
    (@ 2.4 GHz i7), so around 0.11 seconds per match.
    -After generating 800 matches, the program was using around 550 MB of memory,
    soon after starting with generating the  matches, the memory usage peaked at
    almost 800 MB of memory usage, then afterwards stabalized at around 500 MB
    of memory usage, but sometimes jumping up to 650 MB.
    
    In conclusion the java garbage collector seems to be pretty good, but for
    optimal performance (in exceptional cases) it does need some help.
    */

public class PlayAnimation{

    public static Match playAnimation(Team team1, Goalkeeper keeperTeam1, ArrayList<Player> playersTeam1, 
            Team team2, Goalkeeper keeperTeam2, ArrayList<Player> playersTeam2){
        
        // TODO: select team composition
        
        //ONLY FOR TESTING: *******************************
        //ally team:
        PlayerInfo p1 = new PlayerInfo(95,95, new ExactPosition(60 , 381));
        
        ArrayList<PlayerInfo> defense1 = new ArrayList<>();
        defense1.add(new PlayerInfo(95,95,95, new ExactPosition(330, 160)));
        defense1.add(new PlayerInfo(95,95,95, new ExactPosition(288, 290)));
        defense1.add(new PlayerInfo(95,95,95, new ExactPosition(288, 467)));
        defense1.add(new PlayerInfo(95,95,95, new ExactPosition(330, 605)));
        
        ArrayList<PlayerInfo> midfield1 = new ArrayList<>();
        midfield1.add(new PlayerInfo(95,95,95, new ExactPosition(550, 250)));
        midfield1.add(new PlayerInfo(95,95,95, new ExactPosition(450, 385)));
        midfield1.add(new PlayerInfo(95,95,95, new ExactPosition(550, 513)));
        
        ArrayList<PlayerInfo> attack1 = new ArrayList<>();
        attack1.add(new PlayerInfo(95,95,95, new ExactPosition(713, 259)));
        attack1.add(new PlayerInfo(95,95,95, new ExactPosition(785, 385)));
        attack1.add(new PlayerInfo(95,95,95, new ExactPosition(719, 494)));
        
        //enemy team:
        PlayerInfo p2 = new PlayerInfo(95,95, new ExactPosition(963, 381));
        
        ArrayList<PlayerInfo> defense2 = new ArrayList<>();
        defense2.add(new PlayerInfo(95,95,95, new ExactPosition(700, 160)));
        defense2.add(new PlayerInfo(95,95,95, new ExactPosition(730, 290)));
        defense2.add(new PlayerInfo(95,95,95, new ExactPosition(730, 467)));
        defense2.add(new PlayerInfo(95,95,95, new ExactPosition(700, 605)));
        
        ArrayList<PlayerInfo> midfield2 = new ArrayList<>();
        midfield2.add(new PlayerInfo(95,95,95, new ExactPosition(461, 250)));
        midfield2.add(new PlayerInfo(95,95,95, new ExactPosition(562, 385)));
        midfield2.add(new PlayerInfo(95,95,95, new ExactPosition(461, 513)));
        
        ArrayList<PlayerInfo> attack2 = new ArrayList<>();
        attack2.add(new PlayerInfo(95,95,95, new ExactPosition(306, 259)));
        attack2.add(new PlayerInfo(95,95,95, new ExactPosition(233, 385)));
        attack2.add(new PlayerInfo(95,95,95, new ExactPosition(306, 494)));
        //************************************************* ^ ONLY FOR TESTING
        
        // generate the match
        AnimatedMatch testMatch = (new MainAIController()).createMatch(p1, defense1, midfield1, 
                                                            attack1, p2, defense2, midfield2, attack2);

        // play the generated match's animation
        AnimateFootballMatch.playMatch(testMatch);
        
        // get the scores
        int scoreLeft = testMatch.getPositions(testMatch.amoutOfSlices()-1).getScoreLeft();
        int scoreRight = testMatch.getPositions(testMatch.amoutOfSlices()-1).getScoreRight();
        
        // reset score
        CurrentPositions.reset();
        
        // reset animation variables
        AnimateFootballMatch.reset();
        
        // Clear the memory used to store the match.
        // This does make a huge difference (see tests information in the comments above)
        testMatch = null;
        System.gc();
        
        return new Match(team1, team2, scoreLeft, scoreRight);
    }
    
}