package RTP_Project.example.Client;

public class JSBridge {
    private final MapView.LocationCallback callback;

    public JSBridge(MapView.LocationCallback callback) {
        this.callback = callback;
    }

    public void sendLocation(double lat, double lng) {
        String coords = lat + "," + lng;
        callback.onLocationSelected(coords); // hantar sebagai string
    }
}