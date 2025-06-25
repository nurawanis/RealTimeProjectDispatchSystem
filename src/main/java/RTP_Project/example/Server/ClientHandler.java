package RTP_Project.example.Server;

import java.io.*;
import java.net.*;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class ClientHandler implements Runnable {
    private static Set<String> activeUsers = ConcurrentHashMap.newKeySet();
    private final Socket socket;
    private final BlockingQueue<Incident> incidentQueue;

    public ClientHandler(Socket socket, BlockingQueue<Incident> incidentQueue) {
        this.socket = socket;
        this.incidentQueue = incidentQueue;
    }

    public void run() {
        try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
            Object obj = in.readObject();
            if (obj instanceof Incident) {
                Incident incident = (Incident) obj;
                incidentQueue.put(incident);
                System.out.println("🆕 Incident received: " + incident.getDescription());
            }
        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}