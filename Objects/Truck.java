package Objects;

/**
 * This is the class for the trucks in the application. they have associated orders
 * and information around the delivery of the orders
 *
 * @author Lucas Hynes
 * @version 1.2.0
 * @since 2/25/2021
 */
public class Truck {
    private final int truck_id;
    private final String truck_name;
    private double avg_time_per_dist;
    private Driver driver;

    /**
     * The constructor for the driver class
     *
     * @param truck_id id of the truck
     * @param truck_name truck name
     * @param avg_time_per_dist the average time needed for the truck *currently unused*
     */
    public Truck(int truck_id, String truck_name, double avg_time_per_dist) {

        //sets the values passed through to the internal variable values
        this.truck_id = truck_id;
        this.truck_name = truck_name;
        this.avg_time_per_dist = avg_time_per_dist;
    }

    /**
     * Default constructor for null appearances
     */
    public Truck() {

        //sets internal values to the default values for a new null object
        truck_id = -1;
        truck_name = "";
        avg_time_per_dist = -1;
        driver = new Driver();
    }

    /**
     * @return the id of the truck
     */
    public int getTruck_id() {
        return truck_id;
    }

    /**
     * @return the name of the truck
     */
    public String getTruck_name() {
        return truck_name;
    }

    /**
     * @return the average time needed to go 1 mile
     */
    @SuppressWarnings("unused")
    public double getAvg_time_per_dist() {
        return avg_time_per_dist;
    }

    /**
     * @param avg_time_per_dist sets the value for how much time is needed for the truck to go 1 mi
     */
    public void setAvg_time_per_dist(double avg_time_per_dist) {
        this.avg_time_per_dist = avg_time_per_dist;
    }

    /**
     * @return the max amount of cubes for the truck
     */
    @SuppressWarnings("unused")
    public double getMax_cubes() {
        return 1400.00;
    }

    /**
     * @return the max amount of weight for the truck
     */
    @SuppressWarnings("unused")
    public double getMax_weight() {
        return 7000.00;
    }

    /**
     * @return the driver associated
     */
    public Driver getDriver() {
        return driver;
    }

    /**
     * @param driver sets the value to the driver associated
     */
    public void setDriver(Driver driver) {
        this.driver = driver;
    }
}
