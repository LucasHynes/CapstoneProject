package Scheduling;

import Objects.Order;
import Objects.Truck;

import java.time.LocalDate;

/**
 * This is used to return the values from the different scheduling options together to be viewed within
 * the same chart with standard information base don the class of the object.
 *
 * @author Lucas Hynes
 * @version 1.0.1
 * @since 2/25/2021
 */
public class ScheduleChartObject {

    //initializes local variables for the class to hold
    private final String current_loc;
    private final String target_loc;
    private final String data_group_1;
    private final String data_group_2;
    private final String category;
    private LocalDate time;
    private int minutes;
    private Order order;
    private Truck truck;

    /**
     * The constructor for the schedule chart object, used to build the objects seen as the scheduling
     * options for the user so that the information is correlated and saved within a single defined list.
     * The difference in the object input is reflected in the category, which can be filtered to find the
     * appropriate event and or order, as the truck and order can be added as part of the object to keep
     * track of the effected orders for a given event such as which order is being picked up
     *
     * @param current_loc holds the location the truck is either at or starting at
     * @param target_loc holds the location the truck needs to be or where it's ending at
     * @param data_group_1 holds data relating to the event such as order data or travel info part 1
     * @param data_group_2 holds data relating to the event such as order data or travel info part 2
     * @param category the category of the object being referenced
     * @param time the date of the event
     * @param minutes the minutes needed to complete the event
     */
    public ScheduleChartObject(String current_loc, String target_loc, String data_group_1, String data_group_2,
                               String category, LocalDate time, int minutes) {

        //assigns variables to the internal values
        this.current_loc = current_loc;
        this.target_loc = target_loc;
        this.data_group_1 = data_group_1;
        this.data_group_2 = data_group_2;
        this.category = category;
        this.time = time;
        this.minutes = minutes;
    }

    /**
     * @return the string value for the current location
     */
    public String getCurrent_loc() {
        return current_loc;
    }

    /**
     * @return the string value for the next location
     */
    public String getTarget_loc() {
        return target_loc;
    }

    /**
     * @return the data group string value for info part 1
     */
    public String getData_group_1() {
        return data_group_1;
    }

    /**
     *
     * @return the data group string value for info part 2
     */
    public String getData_group_2() {
        return data_group_2;
    }

    /**
     * @return the category as a string value to match to the object
     */
    public String getCategory() {
        return category;
    }

    /**
     * @return the date of the event
     */
    public LocalDate getTime() {
        return time;
    }

    /**
     * @param time sets to the day of the event
     */
    public void setTime(LocalDate time) {
        this.time = time;
    }

    /**
     * @return the time needed to complete the event
     */
    public int getMinutes() {
        return minutes;
    }

    /**
     * @param minutes sets the value to the minutes needed for the event
     */
    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    /**
     * @return the associated order with the event
     */
    public Order getOrder() {
        return order;
    }

    /**
     * @param order sets the object param to the value of the order associated
     */
    public void setOrder(Order order) {
        this.order = order;
    }

    /**
     * @return the truck associated with the event
     */
    public Truck getTruck() {
        return truck;
    }

    /**
     * @param truck sets the truck to the value representing the truck
     */
    public void setTruck(Truck truck) {
        this.truck = truck;
    }
}
