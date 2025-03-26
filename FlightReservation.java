import java.util.Scanner;

public class FlightReservation {
    private TicketManager ticketManager;
    private FlightDisplayManager displayManager;
    private Scanner scanner;

    public FlightReservation() {
        this.ticketManager = new TicketManager();
        this.displayManager = new FlightDisplayManager();
        this.scanner = new Scanner(System.in);
    }

    public boolean bookFlight(String flightNo, int numOfTickets, String userID) {
        // Find flight and customer
        Customer selectedCustomer = findCustomerByUserID(userID);
        Flight selectedFlight = findFlightByNumber(flightNo);

        if (selectedFlight == null) {
            System.out.println("Invalid Flight Number...! No flight with the ID \"" + flightNo + "\" was found...");
            return false;
        }

        if (selectedCustomer == null) {
            System.out.println("Invalid User ID...! No customer with the ID \"" + userID + "\" was found...");
            return false;
        }

        // Delegate booking to TicketManager
        return ticketManager.bookTicket(selectedCustomer, selectedFlight, numOfTickets);
    }

    public boolean cancelFlight(String userID) {
        // Find customer
        Customer selectedCustomer = findCustomerByUserID(userID);

        if (selectedCustomer == null) {
            System.out.println("Invalid User ID...! No customer with the ID \"" + userID + "\" was found...");
            return false;
        }

        // Delegate cancellation to TicketManager
        return ticketManager.cancelTicket(selectedCustomer);
    }

    public void displayFlightsRegisteredByOneUser(String userID) {
        displayManager.displayFlightsRegisteredByOneUser(userID);
    }

    public void displayRegisteredUsersForAllFlight() {
        displayManager.displayRegisteredUsersForAllFlight();
    }

    public void displayRegisteredUsersForASpecificFlight(String flightNum) {
        displayManager.displayRegisteredUsersForASpecificFlight(flightNum);
    }

    private Customer findCustomerByUserID(String userID) {
        return Customer.customerCollection.stream()
                .filter(customer -> customer.getUserID().equals(userID))
                .findFirst()
                .orElse(null);
    }

    private Flight findFlightByNumber(String flightNo) {
        return Flight.getFlightList().stream()
                .filter(flight -> flight.getFlightNumber().equals(flightNo))
                .findFirst()
                .orElse(null);
    }
}