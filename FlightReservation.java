import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class FlightReservation implements DisplayClass {

    private final CustomerManagementService customerManagementService;
    private final FlightService flightService;

    public FlightReservation() {
        this.customerManagementService = new CustomerManagementService();
        this.flightService = new FlightService();
    }

    /**
     * Book the numOfTickets for said flight for the specified user. Update the available seats in main system by
     * Subtracting the numOfTickets from the main system. If a new customer registers for the flight, then it adds
     * the customer to that flight, else if the user is already added to that flight, then it just updates the
     * numOfSeats of that flight.
     *
     * @param flightNo     FlightID of the flight to be booked
     * @param numOfTickets number of tickets to be booked
     * @param userID       userID of the user which is booking the flight
     */
    void bookFlight(String flightNo, int numOfTickets, String userID) {
        Flight selectedFlight = flightService.findFlightByNumber(flightNo);
        Customer selectedCustomer = findCustomerByUserID(userID);

        if (selectedFlight == null) {
            System.out.println("Invalid Flight Number...! No flight with the ID \"" + flightNo + "\" was found...");
            return;
        }

        if (selectedCustomer == null) {
            System.out.println("Invalid User ID...! No customer with the ID \"" + userID + "\" was found...");
            return;
        }

        // Check if flight has enough seats
        if (selectedFlight.getNoOfSeats() < numOfTickets) {
            System.out.println("Not enough seats available on this flight.");
            return;
        }

        // Update flight seats
        selectedFlight.setNoOfSeatsInTheFlight(selectedFlight.getNoOfSeats() - numOfTickets);

        // Add customer to flight if not already added
        customerManagementService.addCustomerToFlight(selectedFlight, selectedCustomer);

        // Update customer's flight registration
        updateCustomerFlightRegistration(selectedCustomer, selectedFlight, numOfTickets);

        System.out.printf("\n %50s You've booked %d tickets for Flight \"%5s\"...", "", numOfTickets, flightNo.toUpperCase());
    }

    /**
     * Cancels the flight for a particular user and return/add the numOfTickets back to
     * the main flight scheduler.
     *
     * @param userID    ID of the user for whom the flight is to be cancelled
     */
    void cancelFlight(String userID) {
        Customer selectedCustomer = findCustomerByUserID(userID);

        if (selectedCustomer == null) {
            System.out.println("Invalid User ID...! No customer with the ID \"" + userID + "\" was found...");
            return;
        }

        List<Flight> registeredFlights = selectedCustomer.getFlightsRegisteredByUser();

        if (registeredFlights.isEmpty()) {
            System.out.println("No Flights have been registered by you.");
            return;
        }

        Scanner read = new Scanner(System.in);

        // Display registered flights
        System.out.printf("%50s %s Here is the list of all the Flights registered by you %s", " ", "++++++++++++++", "++++++++++++++");
        displayFlightsRegisteredByOneUser(userID);

        // Prompt for flight to cancel
        System.out.print("Enter the Flight Number of the Flight you want to cancel: ");
        String flightNum = read.nextLine();

        // Find the specific flight
        Flight flightToCancel = null;
        int flightIndex = -1;
        for (int i = 0; i < registeredFlights.size(); i++) {
            if (registeredFlights.get(i).getFlightNumber().equalsIgnoreCase(flightNum)) {
                flightToCancel = registeredFlights.get(i);
                flightIndex = i;
                break;
            }
        }

        if (flightToCancel == null) {
            System.out.println("ERROR!!! Couldn't find Flight with ID \"" + flightNum.toUpperCase() + "\".....");
            return;
        }

        // Prompt for number of tickets to cancel
        System.out.print("Enter the number of tickets to cancel: ");
        int numOfTickets = read.nextInt();

        // Validate number of tickets
        List<Integer> ticketsBooked = selectedCustomer.getNumOfTicketsBookedByUser();
        int numOfTicketsForFlight = ticketsBooked.get(flightIndex);

        while (numOfTickets > numOfTicketsForFlight) {
            System.out.print("ERROR!!! Number of tickets cannot be greater than " + numOfTicketsForFlight + " for this flight. Please enter the number of tickets again: ");
            numOfTickets = read.nextInt();
        }

        // Update flight seats
        int ticketsToBeReturned = flightToCancel.getNoOfSeats() + numOfTickets;
        flightToCancel.setNoOfSeatsInTheFlight(ticketsToBeReturned);

        // Update customer's tickets
        if (numOfTicketsForFlight == numOfTickets) {
            // Remove the entire flight registration
            selectedCustomer.getFlightsRegisteredByUser().remove(flightIndex);
            selectedCustomer.getNumOfTicketsBookedByUser().remove(flightIndex);
        } else {
            // Update number of tickets
            selectedCustomer.getNumOfTicketsBookedByUser().set(flightIndex, numOfTicketsForFlight - numOfTickets);
        }

        System.out.println("Flight cancellation successful.");
    }

    private void updateCustomerFlightRegistration(Customer customer, Flight flight, int numOfTickets) {
        // Check if flight is already in customer's registered flights
        List<Flight> registeredFlights = customer.getFlightsRegisteredByUser();
        List<Integer> ticketsBooked = customer.getNumOfTicketsBookedByUser();

        int existingFlightIndex = -1;
        for (int i = 0; i < registeredFlights.size(); i++) {
            if (registeredFlights.get(i).getFlightNumber().equals(flight.getFlightNumber())) {
                existingFlightIndex = i;
                break;
            }
        }

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

    private Customer findCustomerByUserID(String userID) {
        for (Customer customer : Customer.customerCollection) {
            if (customer.getUserID().equals(userID)) {
                return customer;
            }
        }
        return null;
    }

    // Existing methods from DisplayClass interface remain the same as in the previous implementation
    @Override
    public void displayFlightsRegisteredByOneUser(String userID) {
        System.out.println();
        System.out.print("+------+-------------------------------------------+-----------+------------------+-----------------------+------------------------+---------------------------+-------------+--------+-----------------+\n");
        System.out.printf("| Num  | FLIGHT SCHEDULE\t\t\t   | FLIGHT NO |  Booked Tickets  | \tFROM ====>>       | \t====>> TO\t   | \t    ARRIVAL TIME       | FLIGHT TIME |  GATE  |  FLIGHT STATUS  |%n");
        System.out.print("+------+-------------------------------------------+-----------+------------------+-----------------------+------------------------+---------------------------+-------------+--------+-----------------+\n");

        for (Customer customer : Customer.customerCollection) {
            if (userID.equals(customer.getUserID())) {
                List<Flight> registeredFlights = customer.getFlightsRegisteredByUser();
                List<Integer> bookedTickets = customer.getNumOfTicketsBookedByUser();

                for (int i = 0; i < registeredFlights.size(); i++) {
                    Flight flight = registeredFlights.get(i);
                    int ticketsBooked = bookedTickets.get(i);

                    System.out.println(toString((i + 1), flight, customer, ticketsBooked));
                    System.out.print("+------+-------------------------------------------+-----------+------------------+-----------------------+------------------------+---------------------------+-------------+--------+-----------------+\n");
                }
            }
        }
    }

    // Updated toString method to include number of booked tickets as a parameter
    public String toString(int serialNum, Flight flights, Customer customer, int bookedTickets) {
        return String.format("| %-5d| %-41s | %-9s | \t%-9d | %-21s | %-22s | %-10s  |   %-6sHrs |  %-4s  | %-10s |",
                serialNum,
                flights.getFlightSchedule(),
                flights.getFlightNumber(),
                bookedTickets,
                flights.getFromWhichCity(),
                flights.getToWhichCity(),
                flightService.fetchArrivalTime(flights),
                flights.getFlightTime(),
                flights.getGate(),
                flightStatus(flights)
        );
    }

    // Other methods like displayHeaderForUsers, displayRegisteredUsersForAllFlight, etc. 
    // would need similar updates to work with the new architecture

    // Existing methods from the previous implementation
    String flightStatus(Flight flight) {
        boolean isFlightAvailable = Flight.getFlightList().contains(flight);
        return isFlightAvailable ? "As Per Schedule" : "   Cancelled   ";
    }

    // Other methods from DisplayClass interface would remain similar to the previous implementation
    @Override
    public void displayRegisteredUsersForAllFlight() {
        System.out.println();
        for (Flight flight : Flight.getFlightList()) {
            List<Customer> registeredCustomers = flight.getRegisteredCustomers();
            if (!registeredCustomers.isEmpty()) {
                displayHeaderForUsers(flight, registeredCustomers);
            }
        }
    }

    @Override
    public void displayRegisteredUsersForASpecificFlight(String flightNum) {
        System.out.println();
        for (Flight flight : Flight.getFlightList()) {
            if (flight.getFlightNumber().equalsIgnoreCase(flightNum)) {
                List<Customer> registeredCustomers = flight.getRegisteredCustomers();
                displayHeaderForUsers(flight, registeredCustomers);
                break;
            }
        }
    }

    @Override
    public void displayHeaderForUsers(Flight flight, List<Customer> customers) {
        System.out.printf("\n%65s Displaying Registered Customers for Flight No. \"%-6s\" %s \n\n", "+++++++++++++", flight.getFlightNumber(), "+++++++++++++");
        System.out.printf("%10s+------------+------------+----------------------------------+---------+-----------------------------+-------------------------------------+-------------------------+----------------+\n", "");
        System.out.printf("%10s| SerialNum  |   UserID   | Passenger Names                  | Age     | EmailID\t\t       | Home Address\t\t\t     | Phone Number\t       | Booked Tickets |%n", "");
        System.out.printf("%10s+------------+------------+----------------------------------+---------+-----------------------------+-------------------------------------+-------------------------+----------------+\n", "");

        for (int i = 0; i < customers.size(); i++) {
            Customer customer = customers.get(i);
            int ticketsBooked = customer.getNumOfTicketsBookedByUser().get(
                    customer.getFlightsRegisteredByUser().indexOf(flight)
            );

            System.out.println(toString(i, customer, ticketsBooked));
            System.out.printf("%10s+------------+------------+----------------------------------+---------+-----------------------------+-------------------------------------+-------------------------+----------------+\n", "");
        }
    }

    // Helper method for displayHeaderForUsers
    public String toString(int serialNum, Customer customer, int bookedTickets) {
        return String.format("%10s| %-10d | %-10s | %-32s | %-7s | %-27s | %-35s | %-23s |       %-7s  |",
                "",
                (serialNum + 1),
                customer.randomIDDisplay(customer.getUserID()),
                customer.getName(),
                customer.getAge(),
                customer.getEmail(),
                customer.getAddress(),
                customer.getPhone(),
                bookedTickets
        );
    }
}