import java.util.*;

/* ============================
   UC8 – Booking History Module
   ============================ */

/* Reservation Model (Simplified for History Tracking) */
class Reservation {

    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    @Override
    public String toString() {
        return "ReservationID: " + reservationId +
                " | Guest: " + guestName +
                " | Room Type: " + roomType;
    }
}


/* ============================
   Booking History Storage
   ============================ */

class BookingHistory {

    // Ordered storage
    private List<Reservation> confirmedBookings;

    public BookingHistory() {
        confirmedBookings = new ArrayList<>();
    }

    // Store confirmed reservation
    public void addReservation(Reservation reservation) {
        confirmedBookings.add(reservation);
        System.out.println("Reservation stored in history: "
                + reservation.getReservationId());
    }

    // Retrieve history (read-only exposure)
    public List<Reservation> getAllReservations() {
        return Collections.unmodifiableList(confirmedBookings);
    }
}


/* ============================
   Reporting Service
   ============================ */

class BookingReportService {

    // Display all bookings
    public void generateFullReport(List<Reservation> reservations) {

        System.out.println("\n===== BOOKING HISTORY REPORT =====");

        if (reservations.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        for (Reservation r : reservations) {
            System.out.println(r);
        }

        System.out.println("Total Confirmed Bookings: "
                + reservations.size());
    }

    // Summary by Room Type
    public void generateRoomTypeSummary(List<Reservation> reservations) {

        Map<String, Integer> summary = new HashMap<>();

        for (Reservation r : reservations) {
            summary.put(
                    r.getRoomType(),
                    summary.getOrDefault(r.getRoomType(), 0) + 1
            );
        }

        System.out.println("\n===== ROOM TYPE SUMMARY =====");

        for (String type : summary.keySet()) {
            System.out.println(type + " → " + summary.get(type));
        }
    }
}


/* ============================
   MAIN CLASS – UC8 DEMO
   ============================ */

public class BookMyStay{

    public static void main(String[] args) {

        System.out.println("UC8 – Booking History & Reporting Demo");
        System.out.println("----------------------------------------");

        BookingHistory history = new BookingHistory();
        BookingReportService reportService = new BookingReportService();

        // Simulate confirmed bookings
        Reservation r1 = new Reservation("S101", "Alice", "Single");
        Reservation r2 = new Reservation("D102", "Bob", "Double");
        Reservation r3 = new Reservation("S103", "Charlie", "Single");

        history.addReservation(r1);
        history.addReservation(r2);
        history.addReservation(r3);

        // Admin requests reports
        reportService.generateFullReport(history.getAllReservations());
        reportService.generateRoomTypeSummary(history.getAllReservations());

        System.out.println("\nExecution Completed.");
    }
}