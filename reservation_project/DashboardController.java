import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.converter.*;
import javafx.scene.Node;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.TextFormatter.Change;

import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.function.UnaryOperator;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class DashboardController implements Initializable {

    /*
    @FXML
    private TextField tfTitle;

    @FXML
    void btnOKClicked(ActionEvent event) {
        Stage mainWindow = (Stage) tfTitle.getScene().getWindow();
        String title = tfTitle.getText();
        mainWindow.setTitle(title);
    }
    */

    @Override
    public void initialize (URL arg0, ResourceBundle arg1) {
        roomTypeChoiceBox.setItems(getRoomChoices());
        if(getRoomChoices().isEmpty())
            roomTypeChoiceBox.setValue("No Rooms Available");
        else
            roomTypeChoiceBox.setValue("Select Room Type");
        prepaidChoiceBox.getItems().addAll(prepaidYesNoChoice);
        prepaidChoiceBox.setValue("Select Prepaid");
        tGuests.setTextFormatter(
        new TextFormatter<>(new LongStringConverter(), null, integerFilter));
        tPhone.setTextFormatter(
        new TextFormatter<>(new LongStringConverter(), null, integerFilter));
    }

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Button bDashboard;
    @FXML
    private Button bEdit;
    @FXML
    private Button bHistory;

    @FXML
    private ChoiceBox<String> roomTypeChoiceBox;
    @FXML
    private ChoiceBox<String> prepaidChoiceBox;

    @FXML
    private TextField tFullName;
    @FXML
    private TextField tGuests;
    @FXML
    private DatePicker dateCheckIn;
    @FXML
    private DatePicker dateCheckOut;
    @FXML
    private TextField tPhone;

    @FXML
    private Label lIndicator;

    private int standardsLeft = 125;
    private int deluxeLeft = 25;
    private int presidentialLeft = 5;

    ObservableList<String> getRoomChoices() {
        List<String> roomTypeList = new ArrayList<>();
        {
            try {
                Scanner standardFile = new Scanner(new File("C:\\Users\\tianmx\\Desktop\\projects\\reservation\\standard.txt"));
                Scanner deluxeFile = new Scanner(new File("C:\\Users\\tianmx\\Desktop\\projects\\reservation\\deluxe.txt"));
                Scanner presidentialFile = new Scanner(new File("C:\\Users\\tianmx\\Desktop\\projects\\reservation\\presidential.txt"));
                standardsLeft = standardFile.nextInt();
                standardFile.close();
                deluxeLeft = deluxeFile.nextInt();
                deluxeFile.close();
                presidentialLeft = presidentialFile.nextInt();
                presidentialFile.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if (standardsLeft > 0) {
                roomTypeList.add("Standard");
            }
            if (deluxeLeft > 0) {
                roomTypeList.add("Deluxe");
            }
            if (presidentialLeft > 0) {
                roomTypeList.add("Presidential");
            }
        }
        return FXCollections.observableList(roomTypeList);
    }
    private String[] prepaidYesNoChoice = {"Yes", "No"};

    UnaryOperator<Change> integerFilter = change -> {
        String newText = change.getControlNewText();
        if (newText.matches("([1-9][0-9]*)?")) { 
            return change;
        }
        return null;
    };

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

    public void reservationDone(ActionEvent event) throws IOException, ParseException {
        FadeTransition fade = new FadeTransition();
        fade.setNode(lIndicator);
        fade.setDuration(Duration.millis(1000));
        fade.setCycleCount(1);
        fade.setInterpolator(Interpolator.LINEAR);
        fade.setFromValue(1);
        fade.setToValue(0);

        String fullName;
        String roomType;
        int guests;
        LocalDate dateIn;
        LocalDate dateOut;
        long phone;
        String prepaid;

        try {
            fullName = tFullName.getText();
            if (fullName.equals("")) throw new Exception();
            roomType = roomTypeChoiceBox.getValue();
            if (roomType.equals("Select Room Type") || roomType.equals("No Rooms Available")) throw new Exception();
            guests = Integer.parseInt(tGuests.getText());
            dateIn = dateCheckIn.getValue();
            dateOut = dateCheckOut.getValue();
            if (dateIn.isAfter(dateOut)) throw new Exception();
            phone = Long.parseLong(tPhone.getText());
            prepaid = prepaidChoiceBox.getValue();
            if (prepaid.equals("Select Prepaid")) throw new Exception();
        } catch (Exception e) {
            lIndicator.setStyle("-fx-text-fill: red;");
            lIndicator.setText("Failed!");
            fade.play();
            return;
        }

        switch(roomType) {
            case "Standard":
                FileWriter standardFile = new FileWriter("standard.txt");
                System.out.println("standard = " + standardsLeft);
                standardsLeft -= 1;
                System.out.println("standard - 1 = " + standardsLeft);
                standardFile.write(String.valueOf(standardsLeft));
                standardFile.flush();
                standardFile.close();
                roomTypeChoiceBox.setItems(getRoomChoices());
                roomTypeChoiceBox.setValue("Select Room Type");
                break;
            case "Deluxe":
                FileWriter deluxeFile = new FileWriter("deluxe.txt");
                deluxeLeft -= 1;
                deluxeFile.write(String.valueOf(deluxeLeft));
                deluxeFile.flush();
                deluxeFile.close();
                roomTypeChoiceBox.setItems(getRoomChoices());
                roomTypeChoiceBox.setValue("Select Room Type");
                break;
            case "Presidential":
                FileWriter presidentialFile = new FileWriter("presidential.txt");
                presidentialLeft -= 1;
                presidentialFile.write(String.valueOf(presidentialLeft));
                presidentialFile.flush();
                presidentialFile.close();
                roomTypeChoiceBox.setItems(getRoomChoices());
                roomTypeChoiceBox.setValue("Select Room Type");
                break;
        }

        if (standardsLeft == 0 && deluxeLeft == 0 && presidentialLeft == 0) roomTypeChoiceBox.setValue("No Rooms Available");

        JSONArray existingReservationArray = new JSONArray();

        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader("reservations.json")) {
            existingReservationArray = (JSONArray) jsonParser.parse(reader);
            System.out.println("reservation = " + existingReservationArray);
        } catch (IOException e) {
            e.printStackTrace();
        }

        long roomNum = 1;
        if (!existingReservationArray.isEmpty()) {
            JSONObject lastRes = (JSONObject) existingReservationArray.get(existingReservationArray.size()-1);
            JSONObject lastResObj = (JSONObject) lastRes.get("reservation");
            roomNum = (long) lastResObj.get("roomNum") + 1;
        }
        System.out.println("room num = " + roomNum);

        JSONObject newReservationObject = new JSONObject();
        newReservationObject.put("roomNum", roomNum);
        newReservationObject.put("fullName", fullName);
        newReservationObject.put("roomType", roomType);
        newReservationObject.put("guests", guests);
        newReservationObject.put("dateIn", String.valueOf(dateIn));
        newReservationObject.put("dateOut", String.valueOf(dateOut));
        newReservationObject.put("phone", phone);
        newReservationObject.put("prepaid", prepaid);

        JSONObject newReservation = new JSONObject();
        newReservation.put("reservation", newReservationObject);

        try {
            JSONArray newReservationsArray = new JSONArray();
            Iterator i = existingReservationArray.iterator();
            while (i.hasNext()) {
                JSONObject currReservation = (JSONObject) i.next();
                newReservationsArray.add(currReservation);
            }
            newReservationsArray.add(newReservation);
            System.out.println("resJSON = " + newReservation);
            FileWriter file = new FileWriter("reservations.json");
            file.write(newReservationsArray.toJSONString());
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        tFullName.setText(null);
        tGuests.setText(null);
        dateCheckIn.setValue(null);
        dateCheckOut.setValue(null);
        tPhone.setText(null);
        prepaidChoiceBox.setValue("Select Prepaid");
        
        lIndicator.setStyle("-fx-text-fill: green;");
        lIndicator.setText("Success! Rm: " + roomNum);
        fade.play();
    }
}
