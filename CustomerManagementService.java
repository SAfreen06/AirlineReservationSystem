import java.util.List;

public class CustomerManagementService implements ICustomerManager {
    @Override
    public boolean addCustomerToFlight(Flight flight, Customer customer) {
        if (!isCustomerAlreadyAdded(flight.getRegisteredCustomers(), customer)) {
            flight.getRegisteredCustomers().add(customer);
            return true;
        }
        return false;
    }

    @Override
    public boolean isCustomerAlreadyAdded(List<Customer> customersList, Customer customer) {
        return customersList.stream()
                .anyMatch(c -> c.getUserID().equals(customer.getUserID()));
    }
}
