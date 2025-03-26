import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

class FlightDisplayFormatter {
    public String formatFlightForUser(int serialNum, Flight flight, Customer customer, int bookedTickets) {
        return String.format("| %-5d| %-41s | %-9s | \t%-9d | %-21s | %-22s | %-10s  |   %-6sHrs |  %-4s  | %-10s |",
                serialNum,
                flight.getFlightSchedule(),
                flight.getFlightNumber(),
                bookedTickets,
                flight.getFromWhichCity(),
                flight.getToWhichCity(),
                fetchArrivalTime(flight),
                flight.getFlightTime(),
                flight.getGate(),
                determineFlightStatus(flight)
        );
    }

    public String formatUserForFlight(int serialNum, Customer customer, int bookedTickets) {
        return String.format("%10s| %-10d | %-10s | %-32s | %-7s | %-27s | %-35s | %-23s |       %-7s  |",
                "",
                (serialNum + 1),
                randomIDDisplay(customer.getUserID()),
                customer.getName(),
                customer.getAge(),
                customer.getEmail(),
                customer.getAddress(),
                customer.getPhone(),
                bookedTickets
        );
    }

    private String fetchArrivalTime(Flight flight) {
        // Placeholder method - replace with actual arrival time logic
        return "00:00";
    }

    private String randomIDDisplay(String userID) {
        // Placeholder method - replace with actual ID display logic
        return userID;
    }

    private String determineFlightStatus(Flight flight) {
        return Flight.getFlightList().contains(flight) ? "As Per Schedule" : "   Cancelled   ";
    }
}
