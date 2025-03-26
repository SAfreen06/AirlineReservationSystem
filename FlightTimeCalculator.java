import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FlightTimeCalculator implements IFlightTimeCalculator {
    private static final double GROUND_SPEED = 450; // knots

    @Override
    public String calculateFlightTime(double distanceBetweenCities) {
        double time = (distanceBetweenCities / GROUND_SPEED);
        String timeInString = String.format("%.4s", time);
        String[] timeArray = timeInString.replace('.', ':').split(":");

        int hours = Integer.parseInt(timeArray[0]);
        int minutes = Integer.parseInt(timeArray[1]);

        int modulus = minutes % 5;
        if (modulus < 3) {
            minutes -= modulus;
        } else {
            minutes += 5 - modulus;
        }

        if (minutes >= 60) {
            minutes -= 60;
            hours++;
        }

        return formatTime(hours, minutes);
    }

    private String formatTime(int hours, int minutes) {
        if (hours <= 9 && Integer.toString(minutes).length() == 1) {
            return String.format("0%s:%s0", hours, minutes);
        } else if (hours <= 9) {
            return String.format("0%s:%s", hours, minutes);
        } else if (Integer.toString(minutes).length() == 1) {
            return String.format("%s:%s0", hours, minutes);
        } else {
            return String.format("%s:%s", hours, minutes);
        }
    }

    @Override
    public String calculateArrivalTime(String departureTime, String flightTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy, HH:mm a");
        LocalDateTime departureDateTime = LocalDateTime.parse(departureTime, formatter);

        String[] time = flightTime.split(":");
        int hours = Integer.parseInt(time[0]);
        int minutes = Integer.parseInt(time[1]);

        LocalDateTime arrivalTime = departureDateTime.plusHours(hours).plusMinutes(minutes);
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("EE, dd-MM-yyyy HH:mm a");
        return arrivalTime.format(formatter1);
    }
}
