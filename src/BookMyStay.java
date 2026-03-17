import java.util.*;

/**
 * BookMyStayApp
 *
 * Use Case 10:
 * Booking Cancellation & Inventory Rollback
 *
 * Demonstrates controlled state reversal using Stack (LIFO)
 * while maintaining inventory consistency.
 *
 * @author Student
 * @version 1.0
 */
public class BookMyStay{

    /* ===============================
       DOMAIN MODEL
    =============================== */

    static class Reservation {
        private String reservationId;
        private String roomType;
        private String roomId;
        private boolean cancelled;

        public Reservation(String reservationId, String roomType, String roomId) {
            this.reservationId = reservationId;
            this.roomType = roomType;
            this.roomId = roomId;
            this.cancelled = false;
        }

        public String getReservationId() { return reservationId; }
        public String getRoomType() { return roomType; }
        public String getRoomId() { return roomId; }
        public boolean isCancelled() { return cancelled; }

        public void cancel() { this.cancelled = true; }

        @Override
        public String toString() {
            return "Reservation ID: " + reservationId +
                    ", Room Type: " + roomType +
                    ", Room ID: " + roomId +
                    ", Status: " + (cancelled ? "CANCELLED" : "CONFIRMED");
        }
    }

    /* ===============================
       INVENTORY SERVICE
    =============================== */

    static class RoomInventory {
        private Map<String, Integer> availability = new HashMap<>();

        public RoomInventory() {
            availability.put("Single", 2);
            availability.put("Double", 2);
        }

        public int getAvailability(String roomType) {
            return availability.getOrDefault(roomType, 0);
        }

        public void decrement(String roomType) {
            availability.put(roomType, getAvailability(roomType) - 1);
        }

        public void increment(String roomType) {
            availability.put(roomType, getAvailability(roomType) + 1);
        }

        public void displayInventory() {
            System.out.println("\nCurrent Inventory:");
            for (Map.Entry<String, Integer> entry : availability.entrySet()) {
                System.out.println(entry.getKey() + " Rooms Available: " + entry.getValue());
            }
        }
    }

    /* ===============================
       BOOKING + CANCELLATION SERVICE
    =============================== */

    static class BookingService {

        private RoomInventory inventory;
        private Map<String, Reservation> confirmedBookings = new HashMap<>();
        private Stack<String> rollbackStack = new Stack<>();
        private int reservationCounter = 1;

        public BookingService(RoomInventory inventory) {
            this.inventory = inventory;
        }

        // Confirm booking
        public void confirmBooking(String roomType) {

            if (inventory.getAvailability(roomType) <= 0) {
                System.out.println("Booking failed: No rooms available.");
                return;
            }

            String reservationId = "RES" + reservationCounter++;
            String roomId = roomType.substring(0,1).toUpperCase() + reservationId;

            inventory.decrement(roomType);

            Reservation reservation =
                    new Reservation(reservationId, roomType, roomId);

            confirmedBookings.put(reservationId, reservation);

            System.out.println("Booking Confirmed → " + reservation);
        }

        // Cancel booking with rollback
        public void cancelBooking(String reservationId) {

            if (!confirmedBookings.containsKey(reservationId)) {
                System.out.println("Cancellation Failed: Reservation does not exist.");
                return;
            }

            Reservation reservation = confirmedBookings.get(reservationId);

            if (reservation.isCancelled()) {
                System.out.println("Cancellation Failed: Already cancelled.");
                return;
            }

            // Step 1: Record room ID in rollback stack
            rollbackStack.push(reservation.getRoomId());

            // Step 2: Restore inventory
            inventory.increment(reservation.getRoomType());

            // Step 3: Mark reservation cancelled
            reservation.cancel();

            System.out.println("Cancellation Successful → " + reservation);

            System.out.println("Rollback Stack (LIFO): " + rollbackStack);
        }

        public void displayBookings() {
            System.out.println("\nBooking History:");
            for (Reservation r : confirmedBookings.values()) {
                System.out.println(r);
            }
        }
    }

    /* ===============================
       MAIN METHOD
    =============================== */

    public static void main(String[] args) {

        System.out.println("=== BookMyStayApp v1.0 ===");
        System.out.println("UC10: Booking Cancellation & Inventory Rollback");

        RoomInventory inventory = new RoomInventory();
        BookingService bookingService = new BookingService(inventory);

        // Confirm some bookings
        bookingService.confirmBooking("Single");
        bookingService.confirmBooking("Double");

        inventory.displayInventory();

        // Cancel booking
        bookingService.cancelBooking("RES1");

        inventory.displayInventory();

        bookingService.displayBookings();
    }
}