import java.util.*;

/**
 * BookMyStayApp
 *
 * Use Case 11:
 * Concurrent Booking Simulation (Thread Safety)
 *
 * Demonstrates handling multiple booking requests safely using
 * synchronized methods and shared data structures.
 *
 * @author Student
 * @version 1.0
 */
public class BookMyStay{

    /* ===============================
       RESERVATION MODEL
    =============================== */
    static class Reservation {
        private String guestName;
        private String roomType;

        public Reservation(String guestName, String roomType) {
            this.guestName = guestName;
            this.roomType = roomType;
        }

        public String getGuestName() { return guestName; }
        public String getRoomType() { return roomType; }
    }

    /* ===============================
       SHARED INVENTORY (THREAD-SAFE)
    =============================== */
    static class RoomInventory {

        private Map<String, Integer> availability = new HashMap<>();

        public RoomInventory() {
            availability.put("Single", 1); // intentionally small for race condition demo
            availability.put("Double", 1);
        }

        // synchronized ensures only one thread updates at a time
        public synchronized boolean allocateRoom(String roomType) {

            int count = availability.getOrDefault(roomType, 0);

            if (count > 0) {
                availability.put(roomType, count - 1);
                return true;
            }
            return false;
        }

        public void displayInventory() {
            System.out.println("\nFinal Inventory:");
            for (Map.Entry<String, Integer> entry : availability.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        }
    }

    /* ===============================
       SHARED BOOKING QUEUE
    =============================== */
    static class BookingQueue {

        private Queue<Reservation> queue = new LinkedList<>();

        public synchronized void addRequest(Reservation r) {
            queue.add(r);
        }

        public synchronized Reservation getRequest() {
            return queue.poll();
        }
    }

    /* ===============================
       CONCURRENT PROCESSOR (THREAD)
    =============================== */
    static class BookingProcessor extends Thread {

        private BookingQueue queue;
        private RoomInventory inventory;

        public BookingProcessor(BookingQueue queue, RoomInventory inventory, String name) {
            super(name);
            this.queue = queue;
            this.inventory = inventory;
        }

        @Override
        public void run() {

            while (true) {

                Reservation r;

                // Critical section: safely fetch request
                synchronized (queue) {
                    r = queue.getRequest();
                }

                if (r == null) break;

                // Critical section: allocate room safely
                boolean success = inventory.allocateRoom(r.getRoomType());

                if (success) {
                    System.out.println(Thread.currentThread().getName()
                            + " SUCCESS → " + r.getGuestName()
                            + " booked " + r.getRoomType());
                } else {
                    System.out.println(Thread.currentThread().getName()
                            + " FAILED → No " + r.getRoomType()
                            + " room for " + r.getGuestName());
                }
            }
        }
    }

    /* ===============================
       MAIN METHOD
    =============================== */
    public static void main(String[] args) {

        System.out.println("=== BookMyStayApp v1.0 ===");
        System.out.println("UC11: Concurrent Booking Simulation\n");

        RoomInventory inventory = new RoomInventory();
        BookingQueue queue = new BookingQueue();

        // Simulating multiple guests (same room type → race condition scenario)
        queue.addRequest(new Reservation("Guest1", "Single"));
        queue.addRequest(new Reservation("Guest2", "Single"));
        queue.addRequest(new Reservation("Guest3", "Single"));

        // Multiple threads processing same queue
        BookingProcessor t1 = new BookingProcessor(queue, inventory, "Thread-1");
        BookingProcessor t2 = new BookingProcessor(queue, inventory, "Thread-2");

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        inventory.displayInventory();
    }
}