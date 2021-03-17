package Objects;

import java.time.LocalDate;

public class Order {

    //declares the internal variables for the class
    private int order_id;
    private String primary_distributor_key;
    private String secondary_key;
    private Distributor distributor;
    private String distributor_name;
    private StoreFront store_front;
    private String store_front_name;
    private RateSheet rate;
    private String status;
    private double cubes;
    private double weight;
    private double pieces;
    private double merch_price;
    private String notes;
    private LocalDate receiveDate = LocalDate.now();

    /**
     * Constructor for a order object
     *
     * @param order_id the id of the order
     * @param primary_distributor_key primary key used by the distributor
     * @param secondary_key any other signifiers or keys connected to the order
     * @param distributor the distributor sending out the order
     * @param store_front the store front requesting the order
     * @param rate the rate object associated with the distributor AND store front
     * @param cubes the cubic feet amount for the order
     * @param weight the weight of the order
     * @param pieces the number of pieces the order is comprised of
     * @param merch_price the price of the merchandise that comprises the order
     * @param notes the notes associated with the order
     */
    public Order(int order_id, String primary_distributor_key, String secondary_key, Distributor distributor,
                 StoreFront store_front, RateSheet rate, double cubes, double weight, double pieces,
                 double merch_price, String notes) {

        //adds the parameters to the internal variable values
        this.order_id = order_id;
        this.primary_distributor_key = primary_distributor_key;
        this.secondary_key = secondary_key;
        this.distributor = distributor;
        this.store_front = store_front;
        this.rate = rate;
        this.status = "Order Placed";
        this.cubes = cubes;
        this.weight = weight;
        this.pieces = pieces;
        this.merch_price = merch_price;
        this.notes = notes;

        //checks for the null values, then sets the name to be saved internally within the object
        if(distributor!=null){setDistributor_name();}
        if(store_front!=null){setStore_front_name();}
    }

    public Order() {

        //adds the parameters to the internal variable values
        this.order_id = -1;
        this.primary_distributor_key = "";
        this.secondary_key = "";
        this.distributor = new Distributor();
        this.store_front = new StoreFront();
        this.rate = new RateSheet();
        this.status = "Order Placed";
        this.cubes = cubes;
        this.weight = weight;
        this.pieces = pieces;
        this.merch_price = merch_price;
        this.notes = notes;
    }

    /**
     * @return the id of the order
     */
    public int getOrder_id() {
        return order_id;
    }

    /**
     * @param order_id sets the value to the order id
     */
    @SuppressWarnings("unused")
    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    /**
     * @return the primary key associated with the distributor
     */
    public String getPrimary_distributor_key() {
        return primary_distributor_key;
    }

    /**
     * @param primary_distributor_key sets to the value of the primary key for the distributors system
     */
    public void setPrimary_distributor_key(String primary_distributor_key) {
        this.primary_distributor_key = primary_distributor_key;
    }

    /**
     * @return any secondary keys saved if needed
     */
    @SuppressWarnings("unused")
    public String getSecondary_key() {
        return secondary_key;
    }

    /**
     * @param secondary_key value is set to the value of the secondary key
     */
    @SuppressWarnings("unused")
    public void setSecondary_key(String secondary_key) {
        this.secondary_key = secondary_key;
    }

    /**
     * @return the status of the order
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status sets value to the status of the order
     */
    @SuppressWarnings("unused")
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return cubic feet for the order
     */
    public double getCubes() {
        return cubes;
    }

    /**
     * @param cubes value for the cubic feet of the order
     */
    public void setCubes(double cubes) {
        this.cubes = cubes;
    }

    /**
     * @return weight of the order
     */
    public double getWeight() {
        return weight;
    }

    /**
     * @param weight sets the value to the weight of the order
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }

    /**
     * @return number of pieces in the order
     */
    public double getPieces() {
        return pieces;
    }

    /**
     * @param pieces sets the value to the pieces of the order
     */
    public void setPieces(double pieces) {
        this.pieces = pieces;
    }

    /**
     * @return the price of the merchandise being shipped
     */
    public double getMerch_price() {
        return merch_price;
    }

    /**
     * @param merch_price sets the value to the merchandise price for the order
     */
    public void setMerch_price(double merch_price) {
        this.merch_price = merch_price;
    }

    /**
     * @return the value for the notes associated with the order
     */
    public String getNotes() {
        return notes;
    }

    /**
     * @param notes sets the value to the note going with the order
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * @return distributor object associated with the order
     */
    public Distributor getDistributor() {
        return distributor;
    }

    /**
     * @param distributor sets the value to the distributor for the order
     */
    public void setDistributor(Distributor distributor) {
        this.distributor = distributor;
    }

    /**
     * @return the store front target for the order
     */
    public StoreFront getStore_front() {
        return store_front;
    }

    /**
     * @param store_front object value set to be the store front of the order
     */
    public void setStore_front(StoreFront store_front) {
        this.store_front = store_front;
    }

    /**
     * @return the rate object for details on the rate for the order
     */
    public RateSheet getRate() {
        return rate;
    }

    /**
     * @param rate sets the object to the rate of the order
     */
    public void setRate(RateSheet rate) {
        this.rate = rate;
    }

    /**
     * @return the name of the distributor
     */
    @SuppressWarnings("unused")
    public String getDistributor_name() {
        return distributor_name;
    }

    /**
     * Used to set the name of the distributor from the object
     */
    public void setDistributor_name() {
        this.distributor_name = getDistributor().getName();
    }

    /**
     * @return the name of the store front
     */
    @SuppressWarnings("unused")
    public String getStore_front_name() {
        return store_front_name;
    }

    /**
     * Used to set the value for the store to the value in the object
     */
    public void setStore_front_name() {
        this.store_front_name = getStore_front().getName();
    }

    /**
     * @return the date the order was received/entered in the system
     */
    public LocalDate getReceiveDate() {
        return receiveDate;
    }

    /**
     * @param receiveDate the value is then the date assigned as the recieve date for the order
     */
    public void setReceiveDate(LocalDate receiveDate) {
        this.receiveDate = receiveDate;
    }
}
