package RTP_Project.example.Client;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ClientMain extends Application {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Thread listenerThread;

    private String currentUsername;
    public static boolean skipLogin = false;

    private Stage primaryStage;
    private Scene menuScene, reportScene;
    private TextArea statusArea;
    private String selectedLocation = "";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        try {
            socket = new Socket("localhost", 12345);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            startListening();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.primaryStage = stage;
        primaryStage.setTitle("RapidResQ System");

        if (skipLogin) {
            goToHome();
        } else {
            LoginScene login = new LoginScene(stage, this);
            primaryStage.setScene(login.getScene());
        }

        primaryStage.show();
    }

    public void goToHome() {
        initMenuScene();
        initReportScene();
        primaryStage.setScene(menuScene);
    }

    private void initMenuScene() {
        VBox menuLayout = new VBox(15);
        menuLayout.setPadding(new Insets(20));
        menuLayout.setStyle("-fx-background-color: #c54844;");

        Label welcomeLabel = new Label("Welcome " + currentUsername + " to RapidResQ System");
        welcomeLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");

        Button reportButton = new Button("Report Incident");
        reportButton.setStyle("-fx-background-color: #000000; -fx-font-weight: bold; -fx-text-fill: white;");
        reportButton.setOnAction(e -> primaryStage.setScene(reportScene));

        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-background-color: #000000; -fx-font-weight: bold; -fx-text-fill: white;");
        logoutButton.setOnAction(e -> {
            currentUsername = null;
            LoginScene login = new LoginScene(primaryStage, this);
            primaryStage.setScene(login.getScene());
        });

        menuLayout.getChildren().addAll(welcomeLabel, reportButton, logoutButton);
        menuScene = new Scene(menuLayout, 400, 250);
    }

    private void initReportScene() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(15));
        root.setStyle("-fx-background-color: #c54844;");

        Label descLabel = new Label("Incident Description:");
        descLabel.setStyle("-fx-text-fill: white;");
        TextField descField = new TextField();
        descField.setPromptText("e.g. 2 car crash, 4 injured.");
        descField.setPrefWidth(250);

        Label locationLabel = new Label("Location:");
        locationLabel.setStyle("-fx-text-fill: white;");
        Label locationDisplay = new Label("No location selected");
        locationDisplay.setStyle("-fx-text-fill: white;");
        Button selectMapButton = new Button("Open Map");
        selectMapButton.setStyle("-fx-background-color: #000000; -fx-text-fill: white;");
        selectMapButton.setOnAction(e -> {
            MapView.showMap(new Stage(), location -> {
                selectedLocation = location;
                locationDisplay.setText("📍 " + location);
            });
        });

        HBox locationBox = new HBox(10, locationDisplay, selectMapButton);
        locationBox.setAlignment(Pos.CENTER_LEFT);

        Label dateLabel = new Label("Date:");
        dateLabel.setStyle("-fx-text-fill: white;");
        DatePicker datePicker = new DatePicker(LocalDate.now());

        Label timeLabel = new Label("Time (HH:mm):");
        timeLabel.setStyle("-fx-text-fill: white;");
        TextField timeField = new TextField(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));

        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(10);
        inputGrid.add(descLabel, 0, 0);
        inputGrid.add(descField, 1, 0);
        inputGrid.add(dateLabel, 2, 0);
        inputGrid.add(datePicker, 3, 0);
        inputGrid.add(locationLabel, 0, 1);
        inputGrid.add(locationBox, 1, 1, 1, 1);
        inputGrid.add(timeLabel, 2, 1);
        inputGrid.add(timeField, 3, 1);

        CheckBox policeBox = new CheckBox("Police");
        CheckBox fireBox = new CheckBox("Fire");
        CheckBox medicalBox = new CheckBox("Medical");
        policeBox.setStyle("-fx-text-fill: white;");
        fireBox.setStyle("-fx-text-fill: white;");
        medicalBox.setStyle("-fx-text-fill: white;");

        VBox unitBox = new VBox(5, policeBox, fireBox, medicalBox);
        unitBox.setPadding(new Insets(10));

        Button submitButton = new Button("Submit Incident");
        submitButton.setStyle("-fx-background-color: #000000; -fx-text-fill: white;");

        statusArea = new TextArea();
        statusArea.setEditable(false);
        statusArea.setWrapText(true);
        statusArea.setPrefHeight(200);

        Button backButton = new Button("Back to Menu");
        backButton.setStyle("-fx-background-color: #000000; -fx-text-fill: white;");
        backButton.setOnAction(e -> primaryStage.setScene(menuScene));

        submitButton.setOnAction(e -> {
            String description = descField.getText().trim();
            String location = selectedLocation.trim();
            LocalDate date = datePicker.getValue();
            String time = timeField.getText().trim();

            List<String> selectedUnits = new ArrayList<>();
            if (policeBox.isSelected()) selectedUnits.add("Police");
            if (fireBox.isSelected()) selectedUnits.add("Fire");
            if (medicalBox.isSelected()) selectedUnits.add("Medical");

            if (description.isEmpty() || location.isEmpty() || time.isEmpty() || selectedUnits.isEmpty()) {
                showAlert("Please fill in all fields before submitting.");
                return;
            }

            int incidentId = (int) (Math.random() * 900 + 100);
            StringBuilder sb = new StringBuilder();
            sb.append("📝 Incident Reported: ID ").append(incidentId).append(" - ").append(description)
                    .append("      |      📍 Location: ").append(location).append("\n");
            sb.append("🗓Date: ").append(date)
                    .append("      |      🕓Time: ").append(time).append("\n");
            for (String unit : selectedUnits) {
                sb.append("🚨 Dispatching ").append(unit).append(" unit to incident ID ").append(incidentId).append("...\n");
                sb.append("✅ ").append(unit).append(" unit resolved incident ID ").append(incidentId).append("\n");
            }

            try {
                out.writeObject(sb.toString());
                out.flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            descField.clear();
            selectedLocation = "";
            locationDisplay.setText("No location selected");
            datePicker.setValue(LocalDate.now());
            timeField.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
            policeBox.setSelected(false);
            fireBox.setSelected(false);
            medicalBox.setSelected(false);
        });

        root.getChildren().addAll(inputGrid, new Label("Units to Dispatch:"), unitBox, submitButton,
                new Label("Status Log:"), statusArea, backButton);
        reportScene = new Scene(root, 750, 600);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setCurrentUsername(String username) {
        this.currentUsername = username;
    }

    private void startListening() {
        listenerThread = new Thread(() -> {
            try {
                Object input;
                while ((input = in.readObject()) != null) {
                    if (input instanceof String) {
                        String message = (String) input;
                        javafx.application.Platform.runLater(() -> statusArea.appendText(message + "\n"));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        listenerThread.setDaemon(true);
        listenerThread.start();
    }
}
