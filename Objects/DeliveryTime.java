package Objects;

import java.time.LocalDate;

/**
 * This class is used to connect the values of the order once the order is completed to be able to analyze
 * the delivery patters
 *
 * @author Lucas Hynes
 * @version 1.0.0
 * @since 2/25/2021
 */
public class DeliveryTime {

    //id of the delivery
    private int delivery_id;

    //store front receiving the order
    private StoreFront store_front;

    //distributor sending the order
    private Distributor distributor;

    //truck that carried the order
    private Truck truck;

    //driver who drove the order
    private Driver driver;

    //order based off of
    private Order order;

    //time that the package was picked-up
    private LocalDate time_pickup;

    //time that the package was dropped off
    private LocalDate time_drop_off;

    //the distance between the stores
    private double distance;

    /**
     * constructor for the delivery time variable
     *
     * @param delivery_id id of delivery (set default if not explicit)
     * @param store_front Object of store front
     * @param distributor Object of distributor
     * @param truck Object of truck
     * @param order the order being carried
     * @param time_received the date the package was received
     * @param time_pickup the date the order was picked up
     * @param time_drop_off the date the order arrived
     * @param distance the distance traveled from store front to distributor
     */
    public DeliveryTime(int delivery_id, StoreFront store_front, Distributor distributor, Truck truck,
                        Order order, LocalDate time_received, LocalDate time_pickup,
                        LocalDate time_drop_off, double distance) {

        //assigns parameters to the internal variables
        this.store_front = store_front;
        this.distributor = distributor;
        this.truck = truck;
        this.order = order;
        this.delivery_id = delivery_id;
        this.time_pickup = time_pickup;
        this.time_drop_off = time_drop_off;
        this.distance = distance;
        this.driver = truck.getDriver();
    }

    /**
     * @return the id of the delivery time object
     */
    public int getDelivery_id() {
        return delivery_id;
    }

    /**
     * @param delivery_id sets the id to this delivery object's id
     */
    public void setDelivery_id(int delivery_id) {
        this.delivery_id = delivery_id;
    }

    /**
     * @return Local date of the order being entered into the program
     */
    public LocalDate getTime_received() {
        return getOrder().getReceiveDate();
    }

    /**
     * @return Local date of the order being picked up onto the truck
     */
    public LocalDate getTime_pickup() {
        return time_pickup;
    }

    /**
     * @param time_pickup method to set the date of the pick up for the object
     */
    public void setTime_pickup(LocalDate time_pickup) {
        this.time_pickup = time_pickup;
    }

    /**
     * @return Local date of the order being dropped off
     */
    public LocalDate getTime_drop_off() {
        return time_drop_off;
    }

    /**
     * @param time_drop_off the time that the order has been dropped off
     */
    public void setTime_drop_off(LocalDate time_drop_off) {
        this.time_drop_off = time_drop_off;
    }

    /**
     * @return the distance between the store and the distributor
     */
    public double getDistance() {
        return distance;
    }

    /**
     * @param distance double to represent the distance between the store and distributor
     */
    public void setDistance(double distance) {
        this.distance = distance;
    }

    /**
     * @return the store front the order was headed to
     */
    public StoreFront getStore_front() {
        return store_front;
    }

    /**
     * @param store_front sets the store the order goes to
     */
    public void setStore_front(StoreFront store_front) {
        this.store_front = store_front;
    }

    /**
     * @return the distributor who sent out the order
     */
    public Distributor getDistributor() {
        return distributor;
    }

    /**
     * @param distributor sets to who the order was placed by
     */
    public void setDistributor(Distributor distributor) {
        this.distributor = distributor;
    }

    /**
     * @return the truck that took the order
     */
    public Truck getTruck() {
        return truck;
    }

    /**
     * @param truck sets truck to the one that carried the order
     */
    public void setTruck(Truck truck) {
        this.truck = truck;
    }

    /**
     * @return the order associated with the delivery, pickup and travel
     */
    public Order getOrder() {
        return order;
    }

    /**
     * @param order the order being analyzed and by which the other values tell about
     */
    public void setOrder(Order order) {
        this.order = order;
    }

    /**
     * @return the driver who delivered the order
     */
    public Driver getDriver() {
        return driver;
    }

    /**
     * @param driver the driver who transports the orders
     */
    public void setDriver(Driver driver) {
        this.driver = driver;
    }
}
