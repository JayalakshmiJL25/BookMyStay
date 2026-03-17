import java.util.*;

/* ============================
   UC7 – Add-On Services Module
   ============================ */

class AddOnService {

    private String serviceName;
    private double price;

    public AddOnService(String serviceName, double price) {
        this.serviceName = serviceName;
        this.price = price;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getPrice() {
        return price;
    }
}

class AddOnServiceManager {

    // Map<ReservationID, List<AddOnService>>
    private Map<String, List<AddOnService>> reservationServices;

    public AddOnServiceManager() {
        reservationServices = new HashMap<>();
    }

    public void addService(String reservationId, AddOnService service) {
        reservationServices
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .add(service);

        System.out.println("Service added to Reservation "
                + reservationId + " : " + service.getServiceName());
    }

    public double calculateTotalCost(String reservationId) {

        double total = 0;

        if (reservationServices.containsKey(reservationId)) {
            for (AddOnService service : reservationServices.get(reservationId)) {
                total += service.getPrice();
            }
        }

        return total;
    }

    public void displayServices(String reservationId) {

        System.out.println("\n--- Add-On Services for Reservation "
                + reservationId + " ---");

        if (!reservationServices.containsKey(reservationId)) {
            System.out.println("No services selected.");
            return;
        }

        for (AddOnService service : reservationServices.get(reservationId)) {
            System.out.println(service.getServiceName()
                    + " - $" + service.getPrice());
        }

        System.out.println("Total Additional Cost: $"
                + calculateTotalCost(reservationId));
    }
}

/* ============================
   MAIN CLASS
   ============================ */

public class BookMyStay {

    public static void main(String[] args) {

        System.out.println("UC7 – Add-On Services Demo");
        System.out.println("----------------------------");

        String reservationId = "S101";   // Assume existing reservation

        AddOnServiceManager manager = new AddOnServiceManager();

        manager.addService(reservationId, new AddOnService("Breakfast", 20));
        manager.addService(reservationId, new AddOnService("Spa Access", 50));
        manager.addService(reservationId, new AddOnService("Airport Pickup", 40));

        manager.displayServices(reservationId);

        System.out.println("\nExecution Completed.");
    }
}