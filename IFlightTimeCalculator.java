public interface IFlightTimeCalculator {
    String calculateFlightTime(double distanceBetweenCities);
    String calculateArrivalTime(String departureTime, String flightTime);
}
