public interface IDistanceCalculator {
    String[] calculateDistance(double lat1, double lon1, double lat2, double lon2);
    double convertDegreesToRadians(double degrees);
    double convertRadiansToDegrees(double radians);
}
