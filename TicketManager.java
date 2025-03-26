import java.util.List;
import java.util.Scanner;

class TicketManager {
    private Scanner scanner;

    public TicketManager() {
        this.scanner = new Scanner(System.in);
    }

    public boolean bookTicket(Customer customer, Flight flight, int numOfTickets) {
        // Validate seat availability
        if (flight.getNoOfSeats() < numOfTickets) {
            System.out.println("Not enough seats available on this flight.");
            return false;
        }

        // Update flight seats
        flight.setNoOfSeatsInTheFlight(flight.getNoOfSeats() - numOfTickets);

        // Add customer to flight if not already added
        addCustomerToFlight(flight, customer);

        // Update customer's flight registration
        updateCustomerFlightRegistration(customer, flight, numOfTickets);

        System.out.printf("\n %50s You've booked %d tickets for Flight \"%5s\"...", "", numOfTickets, flight.getFlightNumber().toUpperCase());
        return true;
    }

    public boolean cancelTicket(Customer customer) {
        List<Flight> registeredFlights = customer.getFlightsRegisteredByUser();

        if (registeredFlights.isEmpty()) {
            System.out.println("No Flights have been registered by you.");
            return false;
        }

        // Display registered flights
        displayRegisteredFlights(customer);

        // Prompt for flight to cancel
        System.out.print("Enter the Flight Number of the Flight you want to cancel: ");
        String flightNum = scanner.nextLine();

        // Find the specific flight
        Flight flightToCancel = findRegisteredFlightByNumber(registeredFlights, flightNum);

        if (flightToCancel == null) {
            System.out.println("ERROR!!! Couldn't find Flight with ID \"" + flightNum.toUpperCase() + "\".....");
            return false;
        }

        int flightIndex = registeredFlights.indexOf(flightToCancel);

        // Prompt for number of tickets to cancel
        System.out.print("Enter the number of tickets to cancel: ");
        int numOfTickets = scanner.nextInt();

        // Validate number of tickets
        List<Integer> ticketsBooked = customer.getNumOfTicketsBookedByUser();
        int numOfTicketsForFlight = ticketsBooked.get(flightIndex);

        while (numOfTickets > numOfTicketsForFlight) {
            System.out.print("ERROR!!! Number of tickets cannot be greater than " + numOfTicketsForFlight + " for this flight. Please enter the number of tickets again: ");
            numOfTickets = scanner.nextInt();
        }

        // Update flight seats
        int ticketsToBeReturned = flightToCancel.getNoOfSeats() + numOfTickets;
        flightToCancel.setNoOfSeatsInTheFlight(ticketsToBeReturned);

        // Update customer's tickets
        updateCustomerTickets(customer, flightIndex, numOfTickets, numOfTicketsForFlight);

        System.out.println("Flight cancellation successful.");
        return true;
    }

    private void addCustomerToFlight(Flight flight, Customer customer) {
        if (!flight.getRegisteredCustomers().contains(customer)) {
            flight.getRegisteredCustomers().add(customer);
        }
    }

    private void updateCustomerFlightRegistration(Customer customer, Flight flight, int numOfTickets) {
        List<Flight> registeredFlights = customer.getFlightsRegisteredByUser();
        List<Integer> ticketsBooked = customer.getNumOfTicketsBookedByUser();

        int existingFlightIndex = findExistingFlightIndex(registeredFlights, flight);

        if (existingFlightIndex != -1) {
            // Update existing flight tickets
            int currentTickets = ticketsBooked.get(existingFlightIndex);
            ticketsBooked.set(existingFlightIndex, currentTickets + numOfTickets);
        } else {
            // Add new flight and tickets
            customer.addNewFlightToCustomerList(flight);
            customer.addNewTicketToNumOfTicketsBookedByUser(numOfTickets);
        }
    }

    private void updateCustomerTickets(Customer customer, int flightIndex, int numOfTickets, int numOfTicketsForFlight) {
        if (numOfTicketsForFlight == numOfTickets) {
            // Remove the entire flight registration
            customer.getFlightsRegisteredByUser().remove(flightIndex);
            customer.getNumOfTicketsBookedByUser().remove(flightIndex);
        } else {
            // Update number of tickets
            customer.getNumOfTicketsBookedByUser().set(flightIndex, numOfTicketsForFlight - numOfTickets);
        }
    }

    private int findExistingFlightIndex(List<Flight> registeredFlights, Flight flight) {
        for (int i = 0; i < registeredFlights.size(); i++) {
            if (registeredFlights.get(i).getFlightNumber().equals(flight.getFlightNumber())) {
                return i;
            }
        }
        return -1;
    }

    private Flight findRegisteredFlightByNumber(List<Flight> registeredFlights, String flightNum) {
        return registeredFlights.stream()
                .filter(flight -> flight.getFlightNumber().equalsIgnoreCase(flightNum))
                .findFirst()
                .orElse(null);
    }

    private void displayRegisteredFlights(Customer customer) {
        System.out.printf("%50s %s Here is the list of all the Flights registered by you %s", " ", "++++++++++++++", "++++++++++++++");

        List<Flight> registeredFlights = customer.getFlightsRegisteredByUser();
        List<Integer> bookedTickets = customer.getNumOfTicketsBookedByUser();

        System.out.println("\nRegistered Flights:");
        for (int i = 0; i < registeredFlights.size(); i++) {
            Flight flight = registeredFlights.get(i);
            int ticketsBooked = bookedTickets.get(i);
            System.out.printf("Flight Number: %s, Booked Tickets: %d\n", flight.getFlightNumber(), ticketsBooked);
        }
    }
}
