/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application.view;

import application.Main;
import application.model.Competition;
import application.model.Match;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

/**
 * This is the controller class of the PLAYMATCH screen of the main view of the
 * game screen.
 *
 * @author Faris
 */
public class GameScreenPLAYMATCHController implements ViewControllerInterface {

    private static Main mainController;
    private static String opponent;

    @FXML
    private Text score;
    @FXML
    private Button positionsButton;
    @FXML
    private Label nameLeft;
    @FXML
    private Label rankLeft;
    @FXML
    private Label nameRight;
    @FXML
    private Label rankRight;
    @FXML
    private Label homeVisLeft;
    @FXML
    private Label homeVisRight;
    @FXML
    private ImageView leftLogo;
    @FXML
    private ImageView rightLogo;

    /**
     * This code is executed when the view is loaded. It sets the main texts of
     * this view.
     */
    @FXML
    private void initialize() {

        if (Main.getCompetition().getRound() == -1) {
            return; //if the competition is done, don't set the texts
        }
        String playersTeam = Main.getChosenTeamName();

        Competition competition = Main.getCompetition();

        int round = competition.getRound();
        Match[] matches = competition.getRound(round - 1);

        boolean playerHome = true;
        Match match = null;

        // get the player match and if he plays at home
        for (int i = 0; i < matches.length; i++) {
            if (matches[i].getHomeTeam().getName().equals(playersTeam)) {
                match = matches[i];
                playerHome = true;
            } else if (matches[i].getVisitorTeam().getName().equals(playersTeam)) {
                match = matches[i];
                playerHome = false;
            }
        }

        if (match == null) {
            System.out.println("couldn't find the players match!");
            return;
        }

        // set the home/visitor team
        homeVisLeft.setText(playerHome ? "Home Team" : "Visitor Team");
        homeVisRight.setText(playerHome ? "Visitor Team" : "Home Team");

        // set the ranks
        rankLeft.setText(Integer.toString(playerHome ? competition.getRank(match.getHomeTeam()) : competition.getRank(match.getVisitorTeam())));
        rankRight.setText(Integer.toString(playerHome ? competition.getRank(match.getVisitorTeam()) : competition.getRank(match.getHomeTeam())));

        // set the team names
        nameLeft.setText(playersTeam);
        String rightTeamName = playerHome ? match.getVisitorTeam().getName() : match.getHomeTeam().getName();
        nameRight.setText(rightTeamName);
        opponent = rightTeamName;

        // set the team logos
        try {
            java.io.FileInputStream leftImageLoader = new FileInputStream("XML/Savegames/" + Main.getCompetition().getSaveGameId() + "/images/" + playersTeam + ".png");
            Image leftImage = new Image(leftImageLoader, 200, 200, true, false);
            leftLogo.setImage(leftImage);
            leftImageLoader.close();

            java.io.FileInputStream rightImageLoader = new FileInputStream("XML/Savegames/" + Main.getCompetition().getSaveGameId() + "/images/" + rightTeamName + ".png");
            Image rightImage = new Image(rightImageLoader, 200, 200, true, false);
            rightLogo.setImage(rightImage);
            rightImageLoader.close();

        } catch (FileNotFoundException ex) {
            System.out.println("failed to get image from image file");
        } catch (IOException ex) {
            Logger.getLogger(GameScreenPLAYMATCHController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * This gives this class a reference to the main class
     *
     * @param mainController the main class
     */
    @Override
    public void setMainController(Main mainController) {
        this.mainController = mainController;
    }

    /**
     * This method is called when the change positions button is clicked. It
     * changes the view to the change positions screen.
     */
    @FXML
    private void positionsButton() {
        Main.getMenuController().getCurrentMenuField().setText("Select positions");
        mainController.setCenterView("GameScreenChoosePositions");
    }

    public static String getOpponent() {
        return opponent;
    }

    public void showResultLastMatch() {

        Competition comp = Main.getCompetition();
        int prevRound = comp.getRound() - 1;
        if (prevRound == -2) {
            prevRound = 34;
        }
        String teamName = Main.getChosenTeamName();
        Match results = null;
        boolean playerHome = true;

        for (Match m : comp.getRound(prevRound - 1)) {
            if (m.getHomeTeam().getName().equals(teamName)) {
                results = m;
            } else if (m.getVisitorTeam().getName().equals(teamName)) {
                results = m;
                playerHome = false;
            }
        }

        positionsButton.setDisable(true);

        if (results != null) {
            int pointsLeft = results.getHomeTeam().getName().equals(nameLeft.getText()) ? results.getPointsHomeTeam() : results.getPointsVisitorTeam();
            int pointsRight = results.getHomeTeam().getName().equals(nameLeft.getText()) ? results.getPointsVisitorTeam() : results.getPointsHomeTeam();
            score.setText(pointsLeft + " - " + pointsRight);
        } else {
            return;
        }

        // set the home/visitor team
        homeVisLeft.setText(playerHome ? "Home Team" : "Visitor Team");
        homeVisRight.setText(playerHome ? "Visitor Team" : "Home Team");

        // set the ranks
        rankLeft.setText(Integer.toString(playerHome ? comp.getRank(results.getHomeTeam()) : comp.getRank(results.getVisitorTeam())));
        rankRight.setText(Integer.toString(playerHome ? comp.getRank(results.getVisitorTeam()) : comp.getRank(results.getHomeTeam())));

        // set the team names
        nameLeft.setText(teamName);
        String rightTeamName = playerHome ? results.getVisitorTeam().getName() : results.getHomeTeam().getName();
        nameRight.setText(rightTeamName);
        opponent = rightTeamName;

        // set the team logos
        try {
            java.io.FileInputStream leftImageLoader = new FileInputStream("XML/Savegames/" + Main.getCompetition().getSaveGameId() + "/images/" + teamName + ".png");
            Image leftImage = new Image(leftImageLoader, 200, 200, true, false);
            leftLogo.setImage(leftImage);
            leftImageLoader.close();

            java.io.FileInputStream rightImageLoader = new FileInputStream("XML/Savegames/" + Main.getCompetition().getSaveGameId() + "/images/" + rightTeamName + ".png");
            Image rightImage = new Image(rightImageLoader, 200, 200, true, false);
            rightLogo.setImage(rightImage);
            rightImageLoader.close();

        } catch (FileNotFoundException ex) {
            System.out.println("failed to get image from image file");
        } catch (IOException ex) {
            Logger.getLogger(GameScreenPLAYMATCHController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
