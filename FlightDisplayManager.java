import java.util.List;

class FlightDisplayManager {
    private FlightDisplayFormatter formatter;

    public FlightDisplayManager() {
        this.formatter = new FlightDisplayFormatter();
    }

    public void displayFlightsRegisteredByOneUser(String userID) {
        Customer customer = findCustomerByUserID(userID);
        if (customer != null) {
            printFlightTableHeader();

            List<Flight> registeredFlights = customer.getFlightsRegisteredByUser();
            List<Integer> bookedTickets = customer.getNumOfTicketsBookedByUser();

            for (int i = 0; i < registeredFlights.size(); i++) {
                Flight flight = registeredFlights.get(i);
                int ticketsBooked = bookedTickets.get(i);

                System.out.println(formatter.formatFlightForUser((i + 1), flight, customer, ticketsBooked));
                printFlightTableSeparator();
            }
        }
    }

    public void displayRegisteredUsersForAllFlight() {
        Flight.getFlightList().stream()
                .filter(flight -> !flight.getRegisteredCustomers().isEmpty())
                .forEach(this::displayHeaderForUsers);
    }

    public void displayRegisteredUsersForASpecificFlight(String flightNum) {
        Flight flight = findFlightByNumber(flightNum);
        if (flight != null) {
            displayHeaderForUsers(flight);
        }
    }

    private void displayHeaderForUsers(Flight flight) {
        System.out.printf("\n%65s Displaying Registered Customers for Flight No. \"%-6s\" %s \n\n", "+++++++++++++", flight.getFlightNumber(), "+++++++++++++");
        printUserTableHeader();

        List<Customer> customers = flight.getRegisteredCustomers();
        for (int i = 0; i < customers.size(); i++) {
            Customer customer = customers.get(i);
            int ticketsBooked = customer.getNumOfTicketsBookedByUser().get(
                    customer.getFlightsRegisteredByUser().indexOf(flight)
            );

            System.out.println(formatter.formatUserForFlight(i, customer, ticketsBooked));
            printUserTableSeparator();
        }
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

    private void printFlightTableHeader() {
        System.out.print("+------+-------------------------------------------+-----------+------------------+-----------------------+------------------------+---------------------------+-------------+--------+-----------------+\n");
        System.out.printf("| Num  | FLIGHT SCHEDULE\t\t\t   | FLIGHT NO |  Booked Tickets  | \tFROM ====>>       | \t====>> TO\t   | \t    ARRIVAL TIME       | FLIGHT TIME |  GATE  |  FLIGHT STATUS  |%n");
        System.out.print("+------+-------------------------------------------+-----------+------------------+-----------------------+------------------------+---------------------------+-------------+--------+-----------------+\n");
    }

    private void printFlightTableSeparator() {
        System.out.print("+------+-------------------------------------------+-----------+------------------+-----------------------+------------------------+---------------------------+-------------+--------+-----------------+\n");
    }

    private void printUserTableHeader() {
        System.out.printf("%10s+------------+------------+----------------------------------+---------+-----------------------------+-------------------------------------+-------------------------+----------------+\n", "");
        System.out.printf("%10s| SerialNum  |   UserID   | Passenger Names                  | Age     | EmailID\t\t       | Home Address\t\t\t     | Phone Number\t       | Booked Tickets |%n", "");
        System.out.printf("%10s+------------+------------+----------------------------------+---------+-----------------------------+-------------------------------------+-------------------------+----------------+\n", "");
    }

    private void printUserTableSeparator() {
        System.out.printf("%10s+------------+------------+----------------------------------+---------+-----------------------------+-------------------------------------+-------------------------+----------------+\n", "");
    }
}
