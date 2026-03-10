import java.util.HashMap;
import java.util.Map;

/* Room Domain Model */

abstract class Room {

    private int beds;
    private double price;

    public Room(int beds, double price) {
        this.beds = beds;
        this.price = price;
    }

    public int getBeds() {
        return beds;
    }

    public double getPrice() {
        return price;
    }

    public abstract String getRoomType();
}


class SingleRoom extends Room {

    public SingleRoom() {
        super(1, 100);
    }

    public String getRoomType() {
        return "Single Room";
    }
}


class DoubleRoom extends Room {

    public DoubleRoom() {
        super(2, 180);
    }

    public String getRoomType() {
        return "Double Room";
    }
}


class SuiteRoom extends Room {

    public SuiteRoom() {
        super(3, 350);
    }

    public String getRoomType() {
        return "Suite Room";
    }
}


/* Inventory Management (UC3) */

class RoomInventory {

    private HashMap<String, Integer> inventory;

    public RoomInventory() {

        inventory = new HashMap<>();

        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 0); // Example unavailable room
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public Map<String, Integer> getInventory() {
        return inventory;
    }
}


/* Search Service (UC4) */

class SearchService {

    public void searchAvailableRooms(RoomInventory inventory, Room[] rooms) {

        System.out.println("------ Available Rooms ------");

        for (Room room : rooms) {

            int available = inventory.getAvailability(room.getRoomType());

            if (available > 0) {

                System.out.println("Room Type : " + room.getRoomType());
                System.out.println("Beds      : " + room.getBeds());
                System.out.println("Price     : $" + room.getPrice());
                System.out.println("Available : " + available);
                System.out.println();
            }
        }
    }
}


/* Application Entry */

public class BookMyStay {

    public static void main(String[] args) {

        System.out.println("Welcome to Book My Stay!");
        System.out.println("Hotel Booking Management System v1.0");
        System.out.println("----------------------------------");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Room domain objects
        Room[] rooms = {
                new SingleRoom(),
                new DoubleRoom(),
                new SuiteRoom()
        };

        // Search operation
        SearchService search = new SearchService();
        search.searchAvailableRooms(inventory, rooms);
    }
}