public class HaversineDistanceCalculator implements IDistanceCalculator {
    @Override
    public String[] calculateDistance(double lat1, double lon1, double lat2, double lon2) {
            final double EARTH_RADIUS_MILES = 3958.8; // Radius of Earth in miles
            final double EARTH_RADIUS_KM = 6371.0; // Radius of Earth in km
            final double EARTH_RADIUS_NAUTICAL_MILES = 3440.1; // Radius in nautical miles

            double latDistance = Math.toRadians(lat2 - lat1);
            double lonDistance = Math.toRadians(lon2 - lon1);

            double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                    + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                    * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

            // Calculate distances in different units
            double distanceMiles = EARTH_RADIUS_MILES * c;
            double distanceKm = EARTH_RADIUS_KM * c;
            double distanceNauticalMiles = EARTH_RADIUS_NAUTICAL_MILES * c;

            // Format the results
            return new String[]{
                    String.format("%.2f", distanceMiles),
                    String.format("%.2f", distanceKm),
                    String.format("%.2f", distanceNauticalMiles)
            };
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
