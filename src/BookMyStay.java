import java.util.*;

/* Reservation Model */

class Reservation {

    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}


/* Booking Request Queue (FIFO) */

class BookingRequestQueue {

    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.add(r);
        System.out.println("Request added: " + r.getGuestName() + " -> " + r.getRoomType());
    }

    public Reservation getNextRequest() {
        return queue.poll(); // FIFO
    }

    public boolean hasRequests() {
        return !queue.isEmpty();
    }
}


/* Inventory Service */

class RoomInventory {

    private HashMap<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {

        inventory.put("Single", 2);
        inventory.put("Double", 2);
        inventory.put("Suite", 1);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void decrementRoom(String roomType) {

        int current = inventory.get(roomType);
        inventory.put(roomType, current - 1);
    }
}


/* Booking Service */

class BookingService {

    // Prevent duplicate room IDs
    private Set<String> allocatedRoomIds = new HashSet<>();

    // Map room types to allocated room IDs
    private HashMap<String, Set<String>> allocatedRooms = new HashMap<>();

    public void processBookings(BookingRequestQueue queue, RoomInventory inventory) {

        while (queue.hasRequests()) {

            Reservation request = queue.getNextRequest();
            String roomType = request.getRoomType();

            System.out.println("\nProcessing request for " + request.getGuestName());

            int available = inventory.getAvailability(roomType);

            if (available > 0) {

                // Generate unique room ID
                String roomId = generateRoomId(roomType);

                // Ensure uniqueness
                while (allocatedRoomIds.contains(roomId)) {
                    roomId = generateRoomId(roomType);
                }

                allocatedRoomIds.add(roomId);

                // Map room type -> allocated IDs
                allocatedRooms
                        .computeIfAbsent(roomType, k -> new HashSet<>())
                        .add(roomId);

                // Update inventory immediately
                inventory.decrementRoom(roomType);

                System.out.println("Booking Confirmed!");
                System.out.println("Guest: " + request.getGuestName());
                System.out.println("Room Type: " + roomType);
                System.out.println("Assigned Room ID: " + roomId);

            } else {

                System.out.println("Booking Failed - No rooms available for " + roomType);
            }
        }
    }

    private String generateRoomId(String roomType) {

        int number = (int)(Math.random() * 100);
        return roomType.substring(0,1).toUpperCase() + number;
    }
}


/* Main Application */

public class BookMyStay{

    public static void main(String[] args) {

        System.out.println("Welcome to Book My Stay");
        System.out.println("Hotel Booking Management System v1.0");
        System.out.println("--------------------------------");

        BookingRequestQueue queue = new BookingRequestQueue();

        // Guests submit booking requests
        queue.addRequest(new Reservation("Alice", "Single"));
        queue.addRequest(new Reservation("Bob", "Double"));
        queue.addRequest(new Reservation("Charlie", "Single"));
        queue.addRequest(new Reservation("David", "Suite"));
        queue.addRequest(new Reservation("Eva", "Suite"));

        RoomInventory inventory = new RoomInventory();

        BookingService bookingService = new BookingService();

        // Process bookings
        bookingService.processBookings(queue, inventory);

        System.out.println("\nAll requests processed.");
    }
}