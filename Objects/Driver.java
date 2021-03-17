package Objects;

public class Driver {

    //internal variables for the class
    private final int driver_id;
    private final String driver_name;
    private double avg_time_per_dist;

    /**
     * The constructor for the driver class
     *
     * @param driver_id id of the driver
     * @param driver_name driver name
     * @param avg_time_per_dist the average time needed for the driver *currently unused*
     */
    public Driver(int driver_id, String driver_name, double avg_time_per_dist) {

        //assigns the parameters
        this.driver_id = driver_id;
        this.driver_name = driver_name;
        this.avg_time_per_dist = avg_time_per_dist;
    }

    /**
     * Default constructor for the driver to handle null occurrences
     */
    public Driver() {

        //sets internal variables to default values
        driver_id = -1;
        driver_name = "";
        avg_time_per_dist = -1;
    }

    /**
     * @return the id of the driver
     */
    public int getDriver_id() {
        return driver_id;
    }

    /**
     * @return the name of the driver
     */
    public String getDriver_name() {
        return driver_name;
    }

    /**
     * @return the average time needed per mile traveled for the truck
     */
    @SuppressWarnings("unused")
    public double getAvg_time_per_dist() {
        return avg_time_per_dist;
    }

    /**
     * @param avg_time_per_dist sets to the average time needed per mile traveled for the truck
     */
    public void setAvg_time_per_dist(double avg_time_per_dist) {
        this.avg_time_per_dist = avg_time_per_dist;
    }
}
