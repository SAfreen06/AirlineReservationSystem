import java.util.List;

public class FlightService {
    // Use the static flight list from Flight class
    public Flight findFlightByNumber(String flightNumber) {
        for (Flight flight : Flight.getFlightList()) {
            if (flight.getFlightNumber().equalsIgnoreCase(flightNumber)) {
                return flight;
            }
        }
        return null;
    }

    public List<Flight> getAllFlights() {
        return Flight.getFlightList();
    }

    public String fetchArrivalTime(Flight flight) {
        // You might want to implement this method or use an existing method from FlightTimeCalculator
        IFlightTimeCalculator timeCalculator = new FlightTimeCalculator();
        return timeCalculator.calculateArrivalTime(flight.getFlightSchedule(), flight.getFlightTime());
    }

    public void updateFlightSeats(Flight flight, int seatsToRemove) {
        int currentSeats = flight.getNoOfSeats();
        flight.setNoOfSeatsInTheFlight(currentSeats - seatsToRemove);
    }

    public boolean hasEnoughSeats(Flight flight, int requestedSeats) {
        return flight.getNoOfSeats() >= requestedSeats;
    }
}
