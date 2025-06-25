package RTP_Project.example.Client;

import java.io.*;
import java.net.*;

public class ClientConnection {
    public static void sendIncident(Incident incident) {
        try (Socket socket = new Socket("localhost", 5000)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(incident);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}