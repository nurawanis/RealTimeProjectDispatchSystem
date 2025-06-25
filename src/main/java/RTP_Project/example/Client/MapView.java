package RTP_Project.example.Client;

import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class MapView {
    public interface LocationCallback {
        void onLocationSelected(String latLng); // String macam "4.2,101.9"
    }

    public static void showMap(Stage stage, LocationCallback callback) {
        WebView webView = new WebView();
        WebEngine engine = webView.getEngine();

        engine.setOnAlert(event -> {
            String latLng = event.getData(); // from JS alert
            callback.onLocationSelected(latLng);
            stage.close();
        });

        String mapUrl = MapView.class.getResource("/map.html").toExternalForm();
        engine.load(mapUrl);

        Scene scene = new Scene(webView, 600, 400);
        stage.setScene(scene);
        stage.setTitle("Select Location");
        stage.show();
    }
}
