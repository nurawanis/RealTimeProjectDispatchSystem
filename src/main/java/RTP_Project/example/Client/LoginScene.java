package RTP_Project.example.Client;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class LoginScene {
    private Scene scene;

    public LoginScene(Stage stage, ClientMain clientMain) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #c54844;");

        Label title = new Label("Login to Dispatch System");
        title.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #000000; -fx-text-fill: white;");
        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: white;");

        loginButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

            if (UserDatabase.authenticate(username, password)) {
                messageLabel.setText("✅ Login successful!");
                clientMain.setCurrentUsername(username);
                clientMain.goToHome();
            } else {
                messageLabel.setText("❌ Invalid username or password.");
            }
        });

        root.getChildren().addAll(title, usernameField, passwordField, loginButton, messageLabel);
        this.scene = new Scene(root, 350, 200);
    }

    public Scene getScene() {
        return scene;
    }
}



