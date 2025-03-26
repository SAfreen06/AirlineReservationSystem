import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

public class FlightSchedulerService implements IFlightScheduler {
    private static int nextFlightDay = 0;

    @Override
    public String createNewFlightSchedule() {
        Calendar c = Calendar.getInstance();
        nextFlightDay += Math.random() * 7;
        c.add(Calendar.DATE, nextFlightDay);
        c.add(Calendar.HOUR, nextFlightDay);
        c.set(Calendar.MINUTE, ((c.get(Calendar.MINUTE) * 3) - (int) (Math.random() * 45)));

        Date myDateObj = c.getTime();
        LocalDateTime date = Instant.ofEpochMilli(myDateObj.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        date = getNearestHourQuarter(date);
        return date.format(DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy, HH:mm a "));
    }

    @Override
    public LocalDateTime getNearestHourQuarter(LocalDateTime datetime) {
        int minutes = datetime.getMinute();
        int mod = minutes % 15;
        LocalDateTime newDatetime;

        if (mod < 8) {
            newDatetime = datetime.minusMinutes(mod);
        } else {
            newDatetime = datetime.plusMinutes(15 - mod);
        }

        return newDatetime.truncatedTo(ChronoUnit.MINUTES);
    }
}