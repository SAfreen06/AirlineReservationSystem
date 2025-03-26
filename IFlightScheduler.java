import java.time.LocalDateTime;

public interface IFlightScheduler {
    String createNewFlightSchedule();
    LocalDateTime getNearestHourQuarter(LocalDateTime datetime);
}
