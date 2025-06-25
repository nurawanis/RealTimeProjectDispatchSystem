package RTP_Project.example.Server;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class Dispatcher implements Runnable {
    private final BlockingQueue<Incident> incidentQueue;
    private final List<EmergencyUnit> units;
    private final ReentrantLock lock = new ReentrantLock();

    public Dispatcher(BlockingQueue<Incident> incidentQueue, List<EmergencyUnit> units) {
        this.incidentQueue = incidentQueue;
        this.units = units;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Incident incident = incidentQueue.take();

                // Use parallelStream to filter available units
                List<EmergencyUnit> availableUnits = units.parallelStream()
                        .filter(EmergencyUnit::isAvailable)
                        .collect(Collectors.toList());

                // Liveness problem simulation: starvation
                if (availableUnits.isEmpty()) {
                    System.out.println("[!] No available units. Incident " + incident.getId() + " is waiting...");
                    incidentQueue.put(incident); // Requeue incident to simulate waiting/starvation
                    Thread.sleep(1000); // simulate wait time before retry
                    continue;
                }

                for (EmergencyUnit unit : availableUnits) {
                    lock.lock();
                    try {
                        if (unit.isAvailable()) {
                            unit.respondToIncident(incident);

                            // Simulate unit doing some work using a thread
                            Thread unitThread = new Thread(() -> {
                                try {
                                    Thread.sleep(1000); // simulate time taken to resolve
                                    unit.setAvailable(true); // mark as available again
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            });

                            unit.setAvailable(false); // immediately mark as busy
                            unitThread.start();

                            try {
                                unitThread.join(); // wait for the unit to finish
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            break;
                        }
                    } finally {
                        lock.unlock();
                    }
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
