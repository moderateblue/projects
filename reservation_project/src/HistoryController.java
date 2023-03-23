import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

public class HistoryController /*implements Initializable*/ {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Button bDashboard;
    @FXML
    private Button bEdit;
    @FXML
    private Button bHistory;

    /*
    @Override
    public void initialize (URL arg0, ResourceBundle arg1){
        roomTypeChoiceBox.getItems().addAll(roomTypesAvailChoice);
        prepaidChoiceBox.getItems().addAll(prepaidYesNoChoice);
    }

    @FXML
    private ChoiceBox<String> roomTypeChoiceBox;
    @FXML
    private ChoiceBox<String> prepaidChoiceBox;

    private String[] roomTypesAvailChoice = {"Standard", "Deluxe", "Presidential"};
    private String[] prepaidYesNoChoice = {"Yes", "No"};
    */

    public void switchToDashboard(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("DashboardScene.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    public void switchToEdit(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("EditScene.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToHistory(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("HistoryScene.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
