import java.util.LinkedList;
import java.util.Queue;

/* Reservation Class */

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


/* Booking Request Queue */

class BookingRequestQueue {

    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    // Add booking request
    public void addRequest(Reservation reservation) {
        requestQueue.add(reservation);
        System.out.println("Booking request added for "
                + reservation.getGuestName()
                + " (" + reservation.getRoomType() + ")");
    }

    // Display queued requests
    public void displayRequests() {

        System.out.println("\n--- Booking Request Queue ---");

        for (Reservation r : requestQueue) {
            System.out.println(
                    "Guest: " + r.getGuestName()
                            + " | Room Type: " + r.getRoomType()
            );
        }
    }
}


/* Application Entry */

public class BookMyStay {

    public static void main(String[] args) {

        System.out.println("Welcome to Book My Stay!");
        System.out.println("Hotel Booking Management System v1.0");

        System.out.println("--------------------------------");

        // Initialize booking queue
        BookingRequestQueue queue = new BookingRequestQueue();

        // Guests submit booking requests
        queue.addRequest(new Reservation("Alice", "Single Room"));
        queue.addRequest(new Reservation("Bob", "Double Room"));
        queue.addRequest(new Reservation("Charlie", "Suite Room"));

        // Display queued requests
        queue.displayRequests();

        System.out.println("--------------------------------");
        System.out.println("Requests are waiting for allocation.");
    }
}