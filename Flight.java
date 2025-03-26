import java.util.*;

public class Flight implements IFlight {
    private final String flightSchedule;
    private final String flightNumber;
    private final String fromWhichCity;
    private final String toWhichCity;
    private final String gate;
    private final double distanceInMiles;
    private final double distanceInKm;
    private final String flightTime;
    private int numOfSeatsInTheFlight;
    private final List<Customer> registeredCustomers;

    private final IDistanceCalculator distanceCalculator;
    private final IFlightTimeCalculator timeCalculator;

    // Static list of flights
    private static final List<Flight> flightList = new ArrayList<>();

    // Factory method for creating flights
    public static Flight createFlight(
            IDistanceCalculator distanceCalculator,
            IFlightTimeCalculator timeCalculator,
            RandomGenerator randomGenerator
    ) {
        String[][] chosenDestinations = randomGenerator.randomDestinations();
        String[] distanceBetweenTheCities = distanceCalculator.calculateDistance(
                Double.parseDouble(chosenDestinations[0][1]),
                Double.parseDouble(chosenDestinations[0][2]),
                Double.parseDouble(chosenDestinations[1][1]),
                Double.parseDouble(chosenDestinations[1][2])
        );

        IFlightScheduler scheduler = new FlightSchedulerService();
        String flightSchedule = scheduler.createNewFlightSchedule();
        String flightNumber = randomGenerator.randomFlightNumbGen(2, 1).toUpperCase();
        int numOfSeatsInTheFlight = randomGenerator.randomNumOfSeats();
        String gate = randomGenerator.randomFlightNumbGen(1, 30).toUpperCase();

        return new Flight(
                flightSchedule,
                flightNumber,
                numOfSeatsInTheFlight,
                chosenDestinations,
                distanceBetweenTheCities,
                gate,
                distanceCalculator,
                timeCalculator
        );
    }

    // Private constructor
    private Flight(
            String flightSchedule,
            String flightNumber,
            int numOfSeatsInTheFlight,
            String[][] chosenDestinations,
            String[] distanceBetweenTheCities,
            String gate,
            IDistanceCalculator distanceCalculator,
            IFlightTimeCalculator timeCalculator
    ) {
        this.flightSchedule = flightSchedule;
        this.flightNumber = flightNumber;
        this.numOfSeatsInTheFlight = numOfSeatsInTheFlight;
        this.fromWhichCity = chosenDestinations[0][0];
        this.toWhichCity = chosenDestinations[1][0];
        this.distanceInMiles = Double.parseDouble(distanceBetweenTheCities[0]);
        this.distanceInKm = Double.parseDouble(distanceBetweenTheCities[1]);
        this.gate = gate;
        this.registeredCustomers = new ArrayList<>();

        this.distanceCalculator = distanceCalculator;
        this.timeCalculator = timeCalculator;

        this.flightTime = timeCalculator.calculateFlightTime(distanceInMiles);
    }

    public static List<Flight> getFlightList() {
        return flightList;
    }

    // Method to generate flight schedule
    public void generateFlightSchedule(int numOfFlights, RandomGenerator randomGenerator) {
        IDistanceCalculator distCalc = new HaversineDistanceCalculator();
        IFlightTimeCalculator timeCalc = new FlightTimeCalculator();

        for (int i = 0; i < numOfFlights; i++) {
            Flight flight = createFlight(distCalc, timeCalc, randomGenerator);
            flightList.add(flight);
        }
    }

    // Getters and other methods remain similar to the original implementation
    // ... (implement all methods from IFlight interface)

    // Display flight schedule method
    public void displayFlightSchedule() {
        // Similar to original implementation
    }

    // Existing getters and other methods...
    @Override
    public String getFlightNumber() { return flightNumber; }
    @Override
    public String getFlightSchedule() { return flightSchedule; }
    @Override
    public String getFromWhichCity() { return fromWhichCity; }
    @Override
    public String getToWhichCity() { return toWhichCity; }
    @Override
    public String getGate() { return gate; }
    @Override
    public String getFlightTime() { return flightTime; }
    @Override
    public int getNoOfSeats() { return numOfSeatsInTheFlight; }
    @Override
    public List<Customer> getRegisteredCustomers() { return registeredCustomers; }

    public void setNoOfSeatsInTheFlight(int numOfSeatsInTheFlight) {
        this.numOfSeatsInTheFlight = numOfSeatsInTheFlight;
    }


}
