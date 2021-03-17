package Scheduling.ScheduleOptions;

import Objects.Order;

import java.time.LocalDate;

/**
 * This object is for the drop off option for an order, taking the order object, time and minutes needed
 *
 * @author Lucas Hynes
 * @version 1.0.0
 * @since 2/25/2021
 */
public class DropOff {

    //internal variable declaration
    private Order order;
    private LocalDate date;
    private final int minutes_needed;

    /**
     * This is the constructor for a drop off event object
     *
     * @param order the order being dropped off
     * @param date the day the order is being dropped off
     * @param minutes_needed the minutes needed to complete the drop off
     */
    public DropOff(Order order, LocalDate date, int minutes_needed) {

        //sets the parameter values to the assigned values
        this.order = order;
        this.date = date;
        this.minutes_needed = minutes_needed;
    }

    /**
     * @return the associated order
     */
    public Order getOrder() {
        return order;
    }

    /**
     * @param order sets the object to be the order getting dropped off
     */
    public void setOrder(Order order) {
        this.order = order;
    }

    /**
     * @return the date the drop off happens
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * @param date sets the date for the drop off
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * @return the minutes needed to complete the drop off
     */
    public int getMinutes_needed() {
        return minutes_needed;
    }
}
