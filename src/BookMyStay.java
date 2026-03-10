
abstract class Room {

    private int beds;
    private int size;
    private double price;

    public Room(int beds, int size, double price) {
        this.beds = beds;
        this.size = size;
        this.price = price;
    }

    public int getBeds() {
        return beds;
    }

    public int getSize() {
        return size;
    }

    public double getPrice() {
        return price;
    }

    public abstract String getRoomType();
}


class SingleRoom extends Room {

    public SingleRoom() {
        super(1, 200, 100.0);
    }

    public String getRoomType() {
        return "Single Room";
    }
}


class DoubleRoom extends Room {

    public DoubleRoom() {
        super(2, 350, 180.0);
    }

    public String getRoomType() {
        return "Double Room";
    }
}


class SuiteRoom extends Room {

    public SuiteRoom() {
        super(3, 500, 350.0);
    }

    public String getRoomType() {
        return "Suite Room";
    }
}


public class BookMyStay {

    public static void main(String[] args) {

        System.out.println("Welcome to Book My Stay!");
        System.out.println("Application: Hotel Booking Management System");
        System.out.println("Version: 1.0");

        System.out.println("----------------------------------");
        System.out.println("Available Room Types");
        System.out.println("----------------------------------");

        // Creating room objects (Polymorphism)
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Static availability variables
        int singleAvailable = 5;
        int doubleAvailable = 3;
        int suiteAvailable = 2;

        // Display room details
        displayRoom(single, singleAvailable);
        displayRoom(doubleRoom, doubleAvailable);
        displayRoom(suite, suiteAvailable);

        System.out.println("----------------------------------");
        System.out.println("Application execution completed.");
    }

    public static void displayRoom(Room room, int availability) {

        System.out.println("Room Type : " + room.getRoomType());
        System.out.println("Beds      : " + room.getBeds());
        System.out.println("Size      : " + room.getSize() + " sq.ft");
        System.out.println("Price     : $" + room.getPrice());
        System.out.println("Available : " + availability);
        System.out.println();
    }
}