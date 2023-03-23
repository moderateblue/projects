import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

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
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.function.UnaryOperator;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class EditController implements Initializable {

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
    private TableView<Reservation> reservationView;
    @FXML
    private TableColumn<Reservation, String> roomNumCol;
    @FXML
    private TableColumn<Reservation, String> nameCol;
    @FXML
    private TableColumn<Reservation, String> roomTypeCol;
    @FXML
    private TableColumn<Reservation, String> guestsCol;
    @FXML
    private TableColumn<Reservation, String> dateInCol;
    @FXML
    private TableColumn<Reservation, String> dateOutCol;
    @FXML
    private TableColumn<Reservation, String> phoneCol;
    @FXML
    private TableColumn<Reservation, String> prepaidCol;

    ObservableList<Reservation> list = FXCollections.observableArrayList();

    @Override
    public void initialize (URL arg0, ResourceBundle arg1){
        initiateCols();
        displayJSON();
    }

    public void displayJSON() {
        list.removeAll(list);
        JSONArray existingReservationArray = new JSONArray();
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader("reservations.json")) {
            existingReservationArray = (JSONArray) jsonParser.parse(reader);
            existingReservationArray.forEach( res -> parseResObject((JSONObject) res));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        reservationView.getItems().addAll(list);
    }

    public void parseResObject(JSONObject reservation) {
        JSONObject resObject = (JSONObject) reservation.get("reservation");
        list.addAll(new Reservation(String.valueOf(resObject.get("roomNum")), (String) resObject.get("fullName"), (String) resObject.get("roomType"), String.valueOf(resObject.get("guests")), (String) resObject.get("dateIn"), (String) resObject.get("dateOut"), String.valueOf(resObject.get("phone")), (String) resObject.get("prepaid")));
    }

    public void initiateCols() {
        roomNumCol.setCellValueFactory(new PropertyValueFactory<>("rmNum"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        roomTypeCol.setCellValueFactory(new PropertyValueFactory<>("rmType"));
        guestsCol.setCellValueFactory(new PropertyValueFactory<>("guests"));
        dateInCol.setCellValueFactory(new PropertyValueFactory<>("dateIn"));
        dateOutCol.setCellValueFactory(new PropertyValueFactory<>("dateOut"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        prepaidCol.setCellValueFactory(new PropertyValueFactory<>("prepaid"));
    }

        /*
        Iterator i = existingReservationArray.iterator();
        while (i.hasNext()) {
            JSONObject currRes = (JSONObject) existingReservationArray.get(iterator());
        }
        if (!existingReservationArray.isEmpty()) {
            JSONObject lastRes = (JSONObject) existingReservationArray.get(existingReservationArray.size()-1);
            for (Iterator iterator = lastRes.keySet().iterator(); iterator.hasNext(); ) {
                roomNum = Integer.parseInt((String) iterator.next()) + 1;
            }
        }
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

    public int findResObject(JSONArray JSONArr, long roomNum) {
        for (int i = 0; i < JSONArr.size(); i++) {
            JSONObject curr = (JSONObject) JSONArr.get(i);
            JSONObject current = (JSONObject) curr.get("reservation");
            long rmNum = (long) current.get("roomNum");
            if(rmNum == roomNum) {
                return i;
            }
        }
        return -1;
    }

    public void openPopUpEdit(MouseEvent event) throws IOException {
        Reservation res = reservationView.getSelectionModel().getSelectedItem();
        if (res == null) {}
        else {
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(stage);
            
            dialog.setTitle("Edit Reservation");

            UnaryOperator<Change> integerFilter = change -> {
                String newText = change.getControlNewText();
                if (newText.matches("([1-9][0-9]*)?")) { 
                    return change;
                }
                return null;
            };

            VBox vbox = new VBox(5);
            vbox.setAlignment(Pos.CENTER);

            Label nameLabel = new Label();
            nameLabel.setText("New Full Name");

            TextField nameField = new TextField();
            nameField.setMaxWidth(125);
            nameField.setText(res.getName());

            Label roomTypeLabel = new Label();
            roomTypeLabel.setText("New Room Type");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("DashboardScene.fxml"));
            root = loader.load();
            DashboardController dashController = loader.getController();
            ChoiceBox roomTypeBox = new ChoiceBox(dashController.getRoomChoices());
            roomTypeBox.setValue(res.getRmType());

            Label guestsLabel = new Label();
            guestsLabel.setText("New # of Guests");

            TextField guestsField = new TextField();
            guestsField.setMaxWidth(75);
            guestsField.setTextFormatter(new TextFormatter<>(new LongStringConverter(), null, integerFilter));
            guestsField.setText(res.getGuests());
            
            Label dateInLabel = new Label();
            dateInLabel.setText("New Check-In Date");

            DatePicker dateInDatePicker = new DatePicker();
            LocalDate in = LocalDate.parse(res.getDateIn());
            dateInDatePicker.setValue(in);

            Label dateOutLabel = new Label();
            dateOutLabel.setText("New Check-Out Date");

            DatePicker dateOutDatePicker = new DatePicker();
            LocalDate out = LocalDate.parse(res.getDateOut());
            dateOutDatePicker.setValue(out);

            Label phoneLabel = new Label();
            phoneLabel.setText("New Phone #");

            TextField phoneField = new TextField();
            phoneField.setMaxWidth(100);
            phoneField.setTextFormatter(new TextFormatter<>(new LongStringConverter(), null, integerFilter));
            phoneField.setText(res.getPhone());

            Label prepaidLabel = new Label();
            prepaidLabel.setText("Prepaid?");

            ChoiceBox prepaidBox = new ChoiceBox<String>();
            prepaidBox.getItems().addAll("Yes","No");
            prepaidBox.setValue(res.getPrepaid());

            HBox choices = new HBox(20);
            choices.setAlignment(Pos.CENTER);

            Label result = new Label();
            result.setMaxWidth(125);
            
            Button done = new Button("Done");
            done.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent arg0) {

                    try (Scanner standardFile = new Scanner(new File("C:\\Users\\tianmx\\Desktop\\projects\\reservation\\standard.txt"))) {
                        Scanner deluxeFile = new Scanner(new File("C:\\Users\\tianmx\\Desktop\\projects\\reservation\\deluxe.txt"));
                        Scanner presidentialFile = new Scanner(new File("C:\\Users\\tianmx\\Desktop\\projects\\reservation\\presidential.txt"));
                        int standardsLeft = standardFile.nextInt();
                        standardFile.close();
                        int deluxeLeft = deluxeFile.nextInt();
                        deluxeFile.close();
                        int presidentialLeft = presidentialFile.nextInt();
                        presidentialFile.close();

                        String fullName;
                        String roomType;
                        int guests;
                        LocalDate dateIn;
                        LocalDate dateOut;
                        long phone;
                        String prepaid;

                        try {
                            fullName = nameField.getText();
                            if (fullName.equals("")) throw new Exception();
                            roomType = (String) roomTypeBox.getValue();
                            guests = Integer.parseInt(guestsField.getText());
                            dateIn = dateInDatePicker.getValue();
                            dateOut = dateOutDatePicker.getValue();
                            if (dateIn.isAfter(dateOut)) throw new Exception();
                            phone = Long.parseLong(phoneField.getText());
                            prepaid = (String) prepaidBox.getValue();
                            if (prepaid.equals("Select Prepaid")) throw new Exception();
                        } catch (Exception e) {
                            result.setStyle("-fx-text-fill: red;");
                            result.setText("Failed!");
                            return;
                        }

                        Reservation res = reservationView.getSelectionModel().getSelectedItem();
                        String typeRoom = res.getRmType();
                        FileWriter standardtxt = new FileWriter("standard.txt");
                        FileWriter deluxetxt = new FileWriter("deluxe.txt");
                        FileWriter presidentialtxt = new FileWriter("presidential.txt");
                        switch(roomType) {
                            case "Standard":
                                standardsLeft -= 1;
                                switch(typeRoom) {
                                    case "Standard":
                                        standardsLeft += 1;
                                        standardtxt.write(String.valueOf(standardsLeft));
                                        deluxetxt.write(String.valueOf(deluxeLeft));
                                        presidentialtxt.write(String.valueOf(presidentialLeft));
                                        break;
                                    case "Deluxe":
                                        deluxeLeft += 1;
                                        standardtxt.write(String.valueOf(standardsLeft));
                                        deluxetxt.write(String.valueOf(deluxeLeft));
                                        presidentialtxt.write(String.valueOf(presidentialLeft));
                                        break;
                                    case "Presidential":
                                        presidentialLeft += 1;
                                        standardtxt.write(String.valueOf(standardsLeft));
                                        deluxetxt.write(String.valueOf(deluxeLeft));
                                        presidentialtxt.write(String.valueOf(presidentialLeft));
                                        break;
                                }
                                break;
                            case "Deluxe":
                                deluxeLeft -= 1;
                                switch(typeRoom) {
                                    case "Standard":
                                        standardsLeft += 1;
                                        standardtxt.write(String.valueOf(standardsLeft));
                                        deluxetxt.write(String.valueOf(deluxeLeft));
                                        presidentialtxt.write(String.valueOf(presidentialLeft));
                                        break;
                                    case "Deluxe":
                                        deluxeLeft += 1;
                                        standardtxt.write(String.valueOf(standardsLeft));
                                        deluxetxt.write(String.valueOf(deluxeLeft));
                                        presidentialtxt.write(String.valueOf(presidentialLeft));
                                        break;
                                    case "Presidential":
                                        presidentialLeft += 1;
                                        standardtxt.write(String.valueOf(standardsLeft));
                                        deluxetxt.write(String.valueOf(deluxeLeft));
                                        presidentialtxt.write(String.valueOf(presidentialLeft));
                                        break;
                                }
                                break;
                            case "Presidential":
                                presidentialLeft -= 1;
                                switch(typeRoom) {
                                    case "Standard":
                                        standardsLeft += 1;
                                        standardtxt.write(String.valueOf(standardsLeft));
                                        deluxetxt.write(String.valueOf(deluxeLeft));
                                        presidentialtxt.write(String.valueOf(presidentialLeft));
                                        break;
                                    case "Deluxe":
                                        deluxeLeft += 1;
                                        standardtxt.write(String.valueOf(standardsLeft));
                                        deluxetxt.write(String.valueOf(deluxeLeft));
                                        presidentialtxt.write(String.valueOf(presidentialLeft));
                                        break;
                                    case "Presidential":
                                        presidentialLeft += 1;
                                        standardtxt.write(String.valueOf(standardsLeft));
                                        deluxetxt.write(String.valueOf(deluxeLeft));
                                        presidentialtxt.write(String.valueOf(presidentialLeft));
                                        break;
                                }
                                break;
                        }
                        standardtxt.flush();
                        standardtxt.close();
                        deluxetxt.flush();
                        deluxetxt.close();
                        presidentialtxt.flush();
                        presidentialtxt.close();

                        JSONArray existingReservationArray = new JSONArray();
                        JSONParser jsonParser = new JSONParser();
                        try (FileReader reader = new FileReader("reservations.json")) {
                            existingReservationArray = (JSONArray) jsonParser.parse(reader);
                            int index = findResObject(existingReservationArray, Long.parseLong(res.getRmNum()));
                            if (index > -1) {
                                existingReservationArray.remove(index);
                                JSONObject newReservationObject = new JSONObject();
                                newReservationObject.put("roomNum", Long.parseLong(res.getRmNum()));
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
                                    reservationView.getItems().clear();
                                    initiateCols();
                                    displayJSON();
                                    dialog.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                result.setText("Something went wrong");
                            }
                        } catch (IOException | ParseException e) {
                            e.printStackTrace();
                        }
                    } catch (NumberFormatException | IOException e) {
                        e.printStackTrace();
                    }
                }

            });

            Button delete = new Button("Delete");
            delete.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent arg0) {
                    JSONArray existingReservationArray = new JSONArray();
                    JSONParser jsonParser = new JSONParser();
                    try (FileReader reader = new FileReader("reservations.json")) {
                        existingReservationArray = (JSONArray) jsonParser.parse(reader);
                        int index = findResObject(existingReservationArray, Long.parseLong(res.getRmNum()));
                        if (index > -1) {
                            String roomType = res.getRmType();
                            switch(roomType) {
                                case "Standard":
                                    Scanner standardFile = new Scanner(new File("C:\\Users\\tianmx\\Desktop\\projects\\reservation\\standard.txt"));
                                    int standardsLeft = standardFile.nextInt();
                                    standardFile.close();
                                    FileWriter standardtxt = new FileWriter("standard.txt");
                                    standardtxt.write(String.valueOf(standardsLeft + 1));
                                    standardtxt.flush();
                                    standardtxt.close();
                                    break;
                                case "Deluxe":
                                    Scanner deluxeFile = new Scanner(new File("C:\\Users\\tianmx\\Desktop\\projects\\reservation\\deluxe.txt"));
                                    int deluxeLeft = deluxeFile.nextInt();
                                    deluxeFile.close();
                                    FileWriter deluxetxt = new FileWriter("deluxe.txt");
                                    deluxetxt.write(String.valueOf(deluxeLeft + 1));
                                    deluxetxt.flush();
                                    deluxetxt.close();
                                    break;
                                case "Presidential":
                                    Scanner presidentialFile = new Scanner(new File("C:\\Users\\tianmx\\Desktop\\projects\\reservation\\presidential.txt"));
                                    int presidentialLeft = presidentialFile.nextInt();
                                    presidentialFile.close();
                                    FileWriter presidentialtxt = new FileWriter("presidential.txt");
                                    presidentialtxt.write(String.valueOf(presidentialLeft + 1));
                                    presidentialtxt.flush();
                                    presidentialtxt.close();
                                    break;
                            }
                            existingReservationArray.remove(index);
                            FileWriter file = new FileWriter("reservations.json");
                            file.write(existingReservationArray.toJSONString());
                            file.flush();
                            file.close();
                            reservationView.getItems().clear();
                            initiateCols();
                            displayJSON();
                            dialog.close();
                        } else {
                            result.setText("Something Went Wrong");
                        }
                    } catch (IOException | ParseException e) {
                        e.printStackTrace();
                    }
                }
            });

            choices.getChildren().addAll(done, delete);

            vbox.getChildren().addAll(nameLabel, nameField, roomTypeLabel, roomTypeBox, guestsLabel, guestsField, dateInLabel, dateInDatePicker, dateOutLabel, dateOutDatePicker, phoneLabel, phoneField, prepaidLabel, prepaidBox, choices, result);
            Scene dialogScene = new Scene(vbox, 500, 500);
            dialog.setScene(dialogScene);
            dialog.show();
        }
    }
}
