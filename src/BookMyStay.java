import java.util.HashMap;
import java.util.Map;

class RoomInventory {

    private HashMap<String, Integer> inventory;

    // Constructor initializes room availability
    public RoomInventory() {
        inventory = new HashMap<>();

        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    // Method to get availability
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Method to update availability
    public void updateAvailability(String roomType, int newCount) {
        inventory.put(roomType, newCount);
    }

    // Display current inventory
    public void displayInventory() {
        System.out.println("----- Current Room Inventory -----");

        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}



public class BookMyStay {

    public static void main(String[] args) {

        // UC1
        System.out.println("Welcome to Book My Stay!");
        System.out.println("Application: Hotel Booking Management System");
        System.out.println("Version: 1.0");

        System.out.println("----------------------------------");

        // UC3: Initialize Inventory
        RoomInventory inventory = new RoomInventory();

        // Display current inventory
        inventory.displayInventory();

        System.out.println("----------------------------------");

        // Example availability lookup
        System.out.println("Available Single Rooms: "
                + inventory.getAvailability("Single Room"));

        // Update availability example
        inventory.updateAvailability("Single Room", 4);

        System.out.println("Updated Single Room Availability: "
                + inventory.getAvailability("Single Room"));
    }
}