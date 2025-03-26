public class HaversineDistanceCalculator implements IDistanceCalculator {
    @Override
    public String[] calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double distance = Math.sin(convertDegreesToRadians(lat1)) * Math.sin(convertDegreesToRadians(lat2))
                + Math.cos(convertDegreesToRadians(lat1)) * Math.cos(convertDegreesToRadians(lat2))
                * Math.cos(convertDegreesToRadians(theta));
        distance = Math.acos(distance);
        distance = convertRadiansToDegrees(distance);
        distance = distance * 60 * 1.1515;

        String[] distanceString = new String[3];
        distanceString[0] = String.format("%.2f", distance * 0.8684);  // Miles
        distanceString[1] = String.format("%.2f", distance * 1.609344);  // Kilometers
        distanceString[2] = Double.toString(Math.round(distance * 100.0) / 100.0);  // Knots
        return distanceString;
    }

    @Override
    public double convertDegreesToRadians(double degrees) {
        return (degrees * Math.PI / 180.0);
    }

    @Override
    public double convertRadiansToDegrees(double radians) {
        return (radians * 180.0 / Math.PI);
    }
}
