package RTP_Project.example.Server;

public class EmergencyUnit {
    private final String id;
    private boolean available = true;

    public EmergencyUnit(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean status) {
        this.available = status;
    }

    // No internal thread anymore, logic is handled by Dispatcher
    public void respondToIncident(Incident incident) {
        incident.setStatus("DISPATCHED");
        System.out.println("Unit " + id + " dispatched to incident " + incident.getId());
    }

    public void resolveIncident(Incident incident) {
        incident.setStatus("RESOLVED");
        System.out.println("Unit " + id + " resolved incident " + incident.getId());
    }
}
