package RTP_Project.example.Server;

import java.io.*;
import java.net.*;
import java.util.*;

public class ServerMain {
    private static final int PORT = 12345;
    private static final List<ObjectOutputStream> clients = new ArrayList<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected");

                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

                synchronized (clients) {
                    clients.add(out);
                }

                new Thread(() -> handleClient(in, out)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(ObjectInputStream in, ObjectOutputStream out) {
        try {
            Object input;
            while ((input = in.readObject()) != null) {
                if (input instanceof String) {
                    String message = (String) input;
                    broadcast(message);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            synchronized (clients) {
                clients.remove(out);
            }
        }
    }

    private static void broadcast(String message) {
        synchronized (clients) {
            for (ObjectOutputStream out : clients) {
                try {
                    out.writeObject(message);
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
