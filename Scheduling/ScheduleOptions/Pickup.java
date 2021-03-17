package Scheduling.ScheduleOptions;

import Objects.Order;

import java.time.LocalDate;

/**
 * Defines the pick up object to be added to the schedule so that the truck knows to
 * add an order to the delivery list
 *
 * @author Lucas Hynes
 * @version 1.0.1
 * @since 2/25/2021
 */
public class Pickup {

    //sets the internal variable structure
    private Order order;
    private LocalDate date;
    private final int minutes_needed;

    /**
     * Defines the constructor for the pick up event
     *
     * @param order the order associated with the event
     * @param date the date of the event
     * @param minutes_needed minutes needed to complete the pickup
     */
    public Pickup(Order order, LocalDate date,int minutes_needed) {

        //assigns the parameters to the internal variables
        this.order = order;
        this.date = date;
        this.minutes_needed = minutes_needed;
    }

    /**
     * @return the associated order for the pick up
     */
    public Order getOrder() {
        return order;
    }

    /**
     * @param order sets the value as the order to pick up
     */
    public void setOrder(Order order) {
        this.order = order;
    }

    /**
     * @return the date of the pick up
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * @param date sets the value to the date of the pickup
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * @return the minutes needed to complete the pick up
     */
    public int getMinutes_needed() {
        return minutes_needed;
    }

}
