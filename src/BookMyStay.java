import java.util.HashMap;
import java.util.Map;

/**
 * Use Case 9: Error Handling & Validation
 *
 * Demonstrates:
 * - Input validation
 * - Custom exceptions
 * - Fail-fast design
 * - Guarding system state
 * - Graceful failure handling
 *
 * Compile: javac BookMyStayApp.java
 * Run:     java BookMyStayApp
 *
 * @author YourName
 * @version 1.0
 */


/* ============================
   Custom Exception
   ============================ */

class InvalidBookingException extends Exception {

    public InvalidBookingException(String message) {
        super(message);
    }
}


/* ============================
   Inventory Service
   ============================ */

class RoomInventory {

    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single", 2);
        inventory.put("Double", 1);
        inventory.put("Suite", 0); // Intentionally zero for validation demo
    }

    public boolean isValidRoomType(String roomType) {
        return inventory.containsKey(roomType);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, -1);
    }

    public void decrement(String roomType) throws InvalidBookingException {

        int available = getAvailability(roomType);

        if (available <= 0) {
            throw new InvalidBookingException(
                    "Booking failed: No available rooms for type: " + roomType
            );
        }

        inventory.put(roomType, available - 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (String key : inventory.keySet()) {
            System.out.println(key + " → " + inventory.get(key));
        }
    }
}


/* ============================
   Booking Validator
   ============================ */

class BookingValidator {

    public void validate(String guestName,
                         String roomType,
                         RoomInventory inventory)
            throws InvalidBookingException {

        // Validate guest name
        if (guestName == null || guestName.trim().isEmpty()) {
            throw new InvalidBookingException(
                    "Validation failed: Guest name cannot be empty."
            );
        }

        // Validate room type
        if (!inventory.isValidRoomType(roomType)) {
            throw new InvalidBookingException(
                    "Validation failed: Invalid room type selected."
            );
        }

        // Validate availability
        if (inventory.getAvailability(roomType) <= 0) {
            throw new InvalidBookingException(
                    "Validation failed: Selected room type is fully booked."
            );
        }
    }
}


/* ============================
   Booking Service
   ============================ */

class BookingService {

    private RoomInventory inventory;
    private BookingValidator validator;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        this.validator = new BookingValidator();
    }

    public void bookRoom(String guestName, String roomType) {

        try {
            // Fail-fast validation
            validator.validate(guestName, roomType, inventory);

            // Safe inventory update
            inventory.decrement(roomType);

            System.out.println("Booking successful for "
                    + guestName + " | Room Type: " + roomType);

        } catch (InvalidBookingException e) {
            // Graceful failure handling
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}


/* ============================
   MAIN CLASS (ENTRY POINT)
   ============================ */

public class BookMyStay{

    public static void main(String[] args) {

        System.out.println("Use Case 9 – Error Handling & Validation");
        System.out.println("------------------------------------------");

        RoomInventory inventory = new RoomInventory();
        BookingService bookingService = new BookingService(inventory);

        inventory.displayInventory();

        System.out.println("\n--- Booking Attempts ---");

        // Valid booking
        bookingService.bookRoom("Alice", "Single");

        // Invalid room type
        bookingService.bookRoom("Bob", "Luxury");

        // No availability
        bookingService.bookRoom("Charlie", "Suite");

        // Empty guest name
        bookingService.bookRoom("", "Double");

        inventory.displayInventory();

        System.out.println("\nSystem remains stable after errors.");
    }
}