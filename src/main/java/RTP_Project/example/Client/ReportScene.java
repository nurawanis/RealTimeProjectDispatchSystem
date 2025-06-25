package RTP_Project.example.Client;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ReportScene {
    private Scene scene;
    private String[] selectedLocation = {""};

    public ReportScene(Stage stage) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(15));
        root.setStyle("-fx-background-color: #c54844;");

        Label descLabel = new Label("Incident Description:");
        descLabel.setStyle("-fx-text-fill: white;");
        TextField descField = new TextField();

        Label locationDisplay = new Label("No location selected");
        locationDisplay.setStyle("-fx-text-fill: white;");
        Button mapButton = new Button("Pick from Map");
        mapButton.setStyle("-fx-background-color: #000000; -fx-text-fill: white;");
        mapButton.setOnAction(e -> {
            MapView.showMap(stage, (coords) -> {
                locationDisplay.setText("📍 " + coords);
                selectedLocation[0] = coords;
            });
        });

        HBox locationBox = new HBox(10, mapButton, locationDisplay);
        locationBox.setAlignment(Pos.CENTER_LEFT);

        Label dateLabel = new Label("Date:");
        dateLabel.setStyle("-fx-text-fill: white;");
        DatePicker datePicker = new DatePicker(LocalDate.now());

        Label timeLabel = new Label("Time (HH:mm):");
        timeLabel.setStyle("-fx-text-fill: white;");
        TextField timeField = new TextField(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));

        Label unitLabel = new Label("Units to Dispatch:");
        unitLabel.setStyle("-fx-text-fill: white;");
        CheckBox policeBox = new CheckBox("Police");
        CheckBox fireBox = new CheckBox("Fire");
        CheckBox medicalBox = new CheckBox("Medical");

        // Putih kan teks dalam checkbox
        policeBox.setStyle("-fx-text-fill: white;");
        fireBox.setStyle("-fx-text-fill: white;");
        medicalBox.setStyle("-fx-text-fill: white;");

        VBox unitBox = new VBox(5, policeBox, fireBox, medicalBox);
        unitBox.setPadding(new Insets(0, 0, 0, 10));

        Button submitButton = new Button("Submit Incident");
        submitButton.setStyle("-fx-background-color: #000000; -fx-text-fill: white;");

        TextArea statusArea = new TextArea();
        statusArea.setEditable(false);
        statusArea.setWrapText(true);
        statusArea.setPrefHeight(250);

        ScrollPane scrollPane = new ScrollPane(statusArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #000000; -fx-text-fill: white;");
        backButton.setOnAction(e -> stage.setScene(new HomeScene(stage, "Dispatcher").getScene()));

        submitButton.setOnAction(e -> {
            String desc = descField.getText().trim();
            String location = selectedLocation[0];
            LocalDate date = datePicker.getValue();
            String time = timeField.getText().trim();

            List<String> selectedUnits = new ArrayList<>();
            if (policeBox.isSelected()) selectedUnits.add("Police");
            if (fireBox.isSelected()) selectedUnits.add("Fire");
            if (medicalBox.isSelected()) selectedUnits.add("Medical");

            if (desc.isEmpty() || location.isEmpty() || time.isEmpty() || selectedUnits.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please fill in all fields.", ButtonType.OK);
                alert.showAndWait();
                return;
            }

            statusArea.appendText("📝 Incident: " + desc + " 📍 " + location + " | " + date + " " + time + "\n");
            for (String unit : selectedUnits) {
                statusArea.appendText("🚨 Dispatching " + unit + "...\n");
            }
        });

        HBox dateTimeBox = new HBox(10, new VBox(dateLabel, datePicker), new VBox(timeLabel, timeField));
        Label logLabel = new Label("Status Log:");
        logLabel.setStyle("-fx-text-fill: white;");

        root.getChildren().addAll(
                descLabel, descField,
                locationDisplay, locationBox,
                dateTimeBox,
                unitLabel, unitBox,
                submitButton,
                logLabel, scrollPane,
                backButton
        );

        scene = new Scene(root, 500, 600);
    }

    public Scene getScene() {
        return scene;
    }
}
