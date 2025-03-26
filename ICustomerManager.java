import java.util.List;

public interface ICustomerManager {
    boolean addCustomerToFlight(Flight flight, Customer customer);
    boolean isCustomerAlreadyAdded(List<Customer> customersList, Customer customer);
}
