package RTP_Project.example.Client;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class HomeScene {
    private Scene scene;

    public HomeScene(Stage stage, String username) {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));

        Label welcomeLabel = new Label("Welcome, " + username);
        Button reportButton = new Button("Report Incident");

        reportButton.setOnAction(e -> {
            ReportScene report = new ReportScene(stage);
            stage.setScene(report.getScene());
        });

        root.getChildren().addAll(welcomeLabel, reportButton);
        this.scene = new Scene(root, 400, 200);
    }

    public Scene getScene() {
        return scene;
    }
}
