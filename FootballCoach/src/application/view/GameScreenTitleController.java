/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application.view;

import application.Main;
import javafx.fxml.FXML;

/**
 *
 * @author faris
 */
public class GameScreenTitleController implements ViewControllerInterface {
        
    private static Main mainController;
    
    /**
     * Code executed when the view is loaded.
     */
    @FXML
    private void initialize(){
        
    }
    
    @Override
    public void setMainController(Main mainController){
        GameScreenTitleController.mainController = mainController;
    }
    
    @FXML
    private void buttonTitleScreen(){
        mainController.cleanRootLayout();
        mainController.setCenterView("MainMenu");
    }
    
}