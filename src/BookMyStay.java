import java.io.*;
import java.util.*;

/**
 * BookMyStayApp
 *
 * Use Case 12:
 * Data Persistence & System Recovery
 *
 * Demonstrates saving and restoring inventory and booking
 * history using Java Serialization.
 *
 * @author Student
 * @version 1.0
 */
public class BookMyStay{

    /* ===============================
       RESERVATION MODEL
    =============================== */
    static class Reservation implements Serializable {
        private static final long serialVersionUID = 1L;

        private String reservationId;
        private String guestName;
        private String roomType;

        public Reservation(String reservationId, String guestName, String roomType) {
            this.reservationId = reservationId;
            this.guestName = guestName;
            this.roomType = roomType;
        }

        public String getReservationId() { return reservationId; }
        public String getGuestName() { return guestName; }
        public String getRoomType() { return roomType; }

        @Override
        public String toString() {
            return reservationId + " | " + guestName + " | " + roomType;
        }
    }

    /* ===============================
       INVENTORY
    =============================== */
    static class RoomInventory implements Serializable {
        private static final long serialVersionUID = 1L;

        private Map<String, Integer> availability = new HashMap<>();

        public RoomInventory() {
            availability.put("Single", 2);
            availability.put("Double", 2);
        }

        public void decrement(String roomType) {
            availability.put(roomType, availability.get(roomType) - 1);
        }

        public void increment(String roomType) {
            availability.put(roomType, availability.get(roomType) + 1);
        }

        public Map<String, Integer> getAvailability() {
            return availability;
        }

        public void display() {
            System.out.println("\nInventory State:");
            for (Map.Entry<String, Integer> entry : availability.entrySet()) {
                System.out.println(entry.getKey() + " → " + entry.getValue());
            }
        }
    }

    /* ===============================
       PERSISTENCE SERVICE
    =============================== */
    static class PersistenceService {

        private static final String FILE_NAME = "bookmystay_data.ser";

        // Save system state
        public static void save(RoomInventory inventory, List<Reservation> history) {

            try (ObjectOutputStream oos =
                         new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

                oos.writeObject(inventory);
                oos.writeObject(history);

                System.out.println("\nSystem state saved successfully.");

            } catch (IOException e) {
                System.out.println("Error saving system state: " + e.getMessage());
            }
        }

        // Load system state
        public static Object[] load() {

            File file = new File(FILE_NAME);

            if (!file.exists()) {
                System.out.println("No persistence file found. Starting fresh.");
                return null;
            }

            try (ObjectInputStream ois =
                         new ObjectInputStream(new FileInputStream(FILE_NAME))) {

                RoomInventory inventory = (RoomInventory) ois.readObject();
                List<Reservation> history =
                        (List<Reservation>) ois.readObject();

                System.out.println("System state restored successfully.");

                return new Object[]{inventory, history};

            } catch (Exception e) {
                System.out.println("Persistence file corrupted. Starting fresh.");
                return null;
            }
        }
    }

    /* ===============================
       MAIN METHOD
    =============================== */
    public static void main(String[] args) {

        System.out.println("=== BookMyStayApp v1.0 ===");
        System.out.println("UC12: Data Persistence & Recovery\n");

        RoomInventory inventory;
        List<Reservation> bookingHistory;

        // Attempt recovery
        Object[] restored = PersistenceService.load();

        if (restored != null) {
            inventory = (RoomInventory) restored[0];
            bookingHistory = (List<Reservation>) restored[1];
        } else {
            inventory = new RoomInventory();
            bookingHistory = new ArrayList<>();
        }

        // Simulate booking
        Reservation r1 = new Reservation("R101", "Alice", "Single");
        bookingHistory.add(r1);
        inventory.decrement("Single");

        System.out.println("\nNew booking confirmed:");
        System.out.println(r1);

        // Display current state
        inventory.display();

        // Save state before shutdown
        PersistenceService.save(inventory, bookingHistory);

        System.out.println("\nApplication shutting down safely...");
    }
}