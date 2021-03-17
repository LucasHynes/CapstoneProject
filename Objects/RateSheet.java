package Objects;

/**
 * Use this class for defining the rates of the companies. This is the rate of the merchandise that is charged
 * to the store for the cost of the transportation. There is also a minimum value for the rate for what the
 * charge will be for the company no matter what
 *
 * @author Lucas Hynes
 * @version 1.0.0
 * @since 2/25/2021
 */
public class RateSheet {

    //holds the internal variables for the class
    private int rate_id;
    private int distributor_id;
    private int store_front_id;
    private double rate;
    private double minimum;
    private String notes;

    /**
     * This is the constructor for the rate sheet to see how much the freight will cost to deliver
     *
     * @param rate_id the id of the rate
     * @param distributor_id the id of the distributor associated
     * @param store_front_id the id of the store front associated
     * @param rate the percentage value for the rate
     * @param minimum the minimum charge value
     * @param notes the notes associated with the rate
     */
    public RateSheet(int rate_id, int  distributor_id, int store_front_id, double rate, double minimum, String notes) {

        //assigns the parameters to the internal variables
        this.rate_id = rate_id;
        this.distributor_id = distributor_id;
        this.store_front_id = store_front_id;
        this.rate = rate;
        this.minimum = minimum;
        this.notes = notes;
    }

    /**
     * The default constructor for a null rate sheet, has default 10% $100 min for the scheduling approximation
     */
    public RateSheet() {

        //assigns the parameters to the internal variables
        this.rate_id = -1;
        this.distributor_id = -1;
        this.store_front_id = -1;
        this.rate = 0.1;
        this.minimum = 100;
        this.notes = "";
    }

    /**
     * @return the id of the rate
     */
    public int getRate_id() {
        return rate_id;
    }

    /**
     * @param rate_id sets the value to the id of the rate
     */
    @SuppressWarnings("unused")
    public void setRate_id(int rate_id) {
        this.rate_id = rate_id;
    }

    /**
     * @return the distributor's id
     */
    public int getDistributor_id() {
        return distributor_id;
    }

    /**
     * @param distributor_id sets the value to the id of the distributor
     */
    @SuppressWarnings("unused")
    public void setDistributor_id(int distributor_id) {
        this.distributor_id = distributor_id;
    }

    /**
     * @return the id of the store front
     */
    public int getStore_front_id() {
        return store_front_id;
    }

    /**
     * @param store_front_id sets the value to the id of the store front
     */
    @SuppressWarnings("unused")
    public void setStore_front_id(int store_front_id) {
        this.store_front_id = store_front_id;
    }

    /**
     * @return the percentage value for the rate of the distributor
     */
    public double getRate() {
        return rate;
    }

    /**
     * @param rate sets the value to the rate of the combo between the distributor and store front
     */
    public void setRate(double rate) {
        this.rate = rate;
    }

    /**
     * @return the minimum value for the freight price
     */
    public double getMinimum() {
        return minimum;
    }

    /**
     * @param minimum sets the value to the minimum value for the rate
     */
    @SuppressWarnings("unused")
    public void setMinimum(double minimum) {
        this.minimum = minimum;
    }

    /**
     * @return the notes from the rate
     */
    public String getNotes() {
        return notes;
    }

    /**
     * @param notes sets the value to the rate of the combo
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }
}
