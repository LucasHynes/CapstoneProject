package Objects;

/**
 * This is the class definition for the store front within the application which holds
 * location information and is the destination for orders headed from distributors
 *
 * @author Lucas Hynes
 * @version 1.0.0
 * @since 2/20/2021
 */
public class StoreFront {

    //variables to hold the internal values
    private final int store_front_id;
    private String name;
    private final String address;
    private final String city;
    private String state;
    private final double longitude;
    private final double latitude;

    /**
     * This is the constructor for the store front option
     *
     * @param store_front_id the id of the store
     * @param name the name of the store
     * @param address the address of the store
     * @param city the city of the store
     * @param state the state of the store
     * @param longitude the longitude of the store
     * @param latitude the latitude of the store
     */
    public StoreFront(int store_front_id, String name, String address, String city, String state, double longitude,
                      double latitude) {

        //sets the parameter values to the internal variables
        this.store_front_id = store_front_id;
        this.name = name;
        this.address = address;
        this.city = city;
        this.state = state;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * Default constructor for null Object return
     */
    public StoreFront() {

        //sets default values to the internal variables
        this.store_front_id = -1;
        this.name = "";
        this.address = "";
        this.city = "";
        this.state = "";
        this.longitude = 0.00;
        this.latitude = 0.00;

    }

    /**
     * @return the id of the store
     */
    public int getStore_front_id() {
        return store_front_id;
    }

    /**
     * @return the name of the store
     */
    public String getName() {
        return name;
    }

    /**
     * @param name sets the value to the name of the store
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the address of the store
     */
    public String getAddress() {
        return address;
    }

    /**
     * @return the city of the store
     */
    public String getCity() {
        return city;
    }

    /**
     * @return the state of the store
     */
    public String getState() {
        return state;
    }

    /**
     * @param state sets the value to the state of the store
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * @return the longitude value of the store
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * @return the latitude value of the store
     */
    public double getLatitude() {
        return latitude;
    }

}
