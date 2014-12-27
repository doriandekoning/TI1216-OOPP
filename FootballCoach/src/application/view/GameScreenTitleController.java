/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application.view;

import application.Main;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

/**
 *
 * @author Faris
 * @author Jochem
 */
public class GameScreenTitleController implements ViewControllerInterface {

    @FXML
    private Text textBudget;
    @FXML
    private Text textWelcome;
    @FXML
    private Text textRound;

    private static Main mainController;

    /**
     * Code executed when the view is loaded.
     */
    @FXML
    private void initialize() {
        // set this as the title controller class in the Main class
        Main.setTitleController(this);
        
        // add texts
        refreshMoney();
        setRound();
        textWelcome.setText("Welcome, " + Main.getChosenName());
    }

    @Override
    public void setMainController(Main mainController) {
        GameScreenTitleController.mainController = mainController;
    }

    /**
     * Method that clears the rootlayout and changes the centerview to mainmenu
     * (return to mainmenu..)
     */
    @FXML
    private void buttonTitleScreen() {
        mainController.cleanRootLayout();
        mainController.setCenterView("MainMenu");
    }

    /**
     * Method that opens the settings popup.
     */
    @FXML
    private void buttonSettings() {
        mainController.createPopup("PopupSETTINGS", "Settings");
    }
    
    /**
     * Method that opens the save game popup.
     */
    @FXML 
    private void buttonSaveGame() {
        mainController.createPopup("PopupSAVEGAME", "Save Game");
    }
    
    /**
     * Method that opens the play match screen.
     */
    @FXML
    private void buttonNextMatch() {
        if(!Main.getMenuController().getCurrentMenuField().getText().equals("Play Match")){
            Main.getMenuController().getCurrentMenuField().setText("Play Match");
            mainController.setCenterView("GameScreenPLAYMATCH");
        }
    }
    
    public void refreshMoney(){
        textBudget.setText("$ " + Main.getCompetition().getTeamByName(Main.getChosenTeamName()).getBudget());
    }
    
    public void setRound(){
        int round = Main.getCompetition().getRound();
        if(round != -1)
            textRound.setText("ROUND " + round);
        else
            textRound.setText("COMPLETED");
    }
}
