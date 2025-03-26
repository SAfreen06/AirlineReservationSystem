import java.util.List;

public interface IFlight {
    String getFlightNumber();
    String getFlightSchedule();
    String getFromWhichCity();
    String getToWhichCity();
    String getGate();
    String getFlightTime();
    int getNoOfSeats();
    List<Customer> getRegisteredCustomers();
}


