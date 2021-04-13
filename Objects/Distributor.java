package Objects;

public class Distributor {

    //local variables for the class
    private int distributor_id;
    private String name;
    private String address;
    private String city;
    private String state;
    private double longitude;
    private double latitude;

    /**
     * This is the constructor for the distributor class to make an object that would have orders made to
     * get those orders to the designated store fronts. This structure can only send orders and cannot
     * receive any orders
     *
     * @param distributor_id the id of the distributor
     * @param name the name of the distributor
     * @param address the address for the store
     * @param city the city of the store
     * @param state the state of the store
     * @param longitude the locational coordinate for the store to be able to calculate distance
     * @param latitude the locational coordinate for the store to be able to calculate distance
     */
    public Distributor(int distributor_id, String name, String address, String city, String state, double longitude,
                      double latitude) {

        //assigns the parameters passed into the constructor are applied to the internal variables
        this.distributor_id = distributor_id;
        this.name = name;
        this.address = address;
        this.city = city;
        this.state = state;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Distributor() {
        //assigns the parameters passed into the constructor are applied to the internal variables
        this.distributor_id = -1;
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
    public int getDistributor_id() {
        return distributor_id;
    }

    /**
     * @param distributor_id sets the value of the id for the store
     */
    public void setDistributor_id(int distributor_id) {
        this.distributor_id = distributor_id;
    }

    /**
     * @return the name of the distributor
     */
    public String getName() {
        return name;
    }

    /**
     * @param name sets the name to the param
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the address of the distributor
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address sets to address for distributor
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the city of the distributor
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city sets to the city of the distributor
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return the state of the distributor
     */
    public String getState() {
        return state;
    }

    /**
     * @param state sets to the state of the distributor
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * @return the longitude value of the distributor
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * @param longitude sets to the longitude value of the distributor
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * @return the latitude value of the distributor
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * @param latitude sets to the latitude value of the distributor
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
