/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application.view;

import application.Main;
import application.model.Goalkeeper;
import application.model.Team;
import javafx.fxml.FXML;
import javafx.scene.control.PopupControl;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author faris
 */
public class PopupMOREINFOGOALKEEPERController implements PopupControllerInterface {

    private boolean isOkClicked = false;
    private static PopupControl popupControl;
    
    @FXML
    Text firstName;
    @FXML
    Text lastName;
    @FXML
    Text playerNumber;
    @FXML
    Text playerType;
    @FXML
    Text stopPower;
    @FXML
    Text penaltyStopPower;
    @FXML
    Text timeNotAvailable;
    @FXML
    Text kindOfCard;
    @FXML
    Text kindOfInjury;
    @FXML
    Text playerTeam;
    @FXML
    Text estimatedValue;
    
    
    @Override
    public boolean isOkClicked() {
        return isOkClicked;
    }

    @Override
        public void setPopupStage(PopupControl popupControl) {
        this.popupControl = popupControl;
    }
        
    @FXML
    private void initialize(){
        Goalkeeper selectedPlayer = (Goalkeeper) Main.getSelectedPlayer();
        
        firstName.setText(selectedPlayer.getName());
        lastName.setText(selectedPlayer.getSurName());
        playerNumber.setText(Integer.toString(selectedPlayer.getNumber()));
        playerType.setText(selectedPlayer.getKind());
        stopPower.setText(Integer.toString(selectedPlayer.getStopPower()));
        penaltyStopPower.setText(Integer.toString(selectedPlayer.getPenaltyStopPower()));
        timeNotAvailable.setText(selectedPlayer.getTimeNotAvailable() == 0 ? "None" : Integer.toString(selectedPlayer.getTimeNotAvailable()) + " days");
        kindOfCard.setText("Not implemented yet");
        kindOfInjury.setText(selectedPlayer.getReason().toString());
        for(Team teams : Main.getCompetition().getTeams())
            if(teams.getPlayers().contains(selectedPlayer))
                playerTeam.setText(teams.getName());
        estimatedValue.setText(formatPrice(selectedPlayer.getPrice()));
    }
    
    /**
     * Format a large number to make it more readable
     * @param number    the number to format
     * @return          a string containing the formatted number
     */
    private String formatPrice(int number){
        String estimatedPriceString = Integer.toString(number);
        String formattedEstimatedPrice = "";
        int j =0;
        for(int i=estimatedPriceString.length()-1; i>=0; i--, j++){
            if(j%3 == 0 && j != 0)
                formattedEstimatedPrice = "," + formattedEstimatedPrice;
            formattedEstimatedPrice = estimatedPriceString.charAt(i) + formattedEstimatedPrice;
        }
        return "$ " + formattedEstimatedPrice;
    }
    
    @FXML
    private void buttonOK() {
        isOkClicked = true;
        popupControl.hide();
    }
    
}