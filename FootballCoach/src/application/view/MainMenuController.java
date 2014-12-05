/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application.view;

import application.Main;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import org.controlsfx.dialog.Dialogs;

/**
 *
 * @author faris
 * @author Jochem
 */
public class MainMenuController implements ViewControllerInterface {
    
    private static Main mainController;
    
    //Load FXML elements to edit in code.
    @FXML
    private ImageView logo;
    
    /**
     * Code executed when the view is loaded.
     */
    @FXML
    private void initialize(){
        // Logo start animation
    	FadeTransition fade = new FadeTransition(Duration.millis(6000), logo);
    	fade.setFromValue(0);
    	fade.setToValue(1);
    	fade.setCycleCount(1);
    	fade.play();
    	TranslateTransition translate = new TranslateTransition(Duration.millis(6000), logo);
        translate.setFromY(-50);
        translate.setToY(0);
        translate.setCycleCount(1);
        translate.play();
        
    }
    
    @Override
    public void setMainController(Main mainController){
        MainMenuController.mainController = mainController;
    }
    
    /**
     * Method to switch to the game's HOME screen.
     */
    @FXML
    private void buttonContinue(){
        mainController.setCenterView("GameScreenHOME");
        mainController.setLeftView("GameScreenMenu");
        mainController.setTopView("GameScreenTitle");
    }
    
    
    /**
     * Method to close the application
     */
    @FXML
    private void buttonExit(){
        // show pop-up: are you sure you want to quit? if ok is clicked: quit game, else don't quit game
        System.exit(0);
    }
   
    
    /**
     * current content only for testing !
     */
    @FXML
    private void buttonSettings(){
        //TEST
        if(mainController.createPopup("TEST_POP_UP_TEST", "<<<<<<<<<<<<<<<<<<< notice the picture ||| (title) this is a test", "/application/img/Settings.png"))
            Dialogs.create()
                .title("pop-up closing status")
                .masthead("The pop-up has closed succesfully!")
                .message("The OK button was used to close the pop-up screen!\nCongratulations, now you can update the values entered in the pop-up screen!")
                .showInformation();
        else
            Dialogs.create()
                .title("pop-up closing status")
                .masthead("The pop-up was killed!")
                .message("Unfortunately the OK button has NOT been pushed :(\nAt least there weren't any changes commited, so you won't have to do anything!")
                .showWarning();
        //TEST
    }
}
