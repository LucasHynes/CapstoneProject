package Scheduling.ScheduleOptions;

import java.time.LocalDate;

/**
 * Holds the object for when the truck is traveling from one location to another with the knowledge that both
 * the to and from can both be either the store front or the distributor, and finding the distance and direction
 * for the traveling to take place
 */
public class Travel {

    //holds the internal values for the travel object
    private final Object start_loc;
    private Object end_loc;
    private String direction;
    private double distance;
    private LocalDate time;
    private final int minutes_needed;

    /**
     * This is the constructor for the travel object which signifies that the truck has moved from location
     * to another with the following associated information about the travel
     *
     * @param start_loc the object for where the truck is at
     * @param end_loc the object for where the user wants the truck to end up
     * @param direction the direction being traveled
     * @param distance the distance being covered (through long and lat)
     * @param time the day of the travel
     * @param minutes_needed the time needed for the trip
     */
    public Travel(Object start_loc, Object end_loc, String direction, double distance, LocalDate time, int minutes_needed) {

        //sets the parameter values to the internal variables within the object
        this.start_loc = start_loc;
        this.end_loc = end_loc;
        this.direction = direction;
        this.distance = distance;
        this.time = time;
        this.minutes_needed = minutes_needed;
    }

    /**
     * @return the direction being traveled
     */
    public String getDirection() {
        return direction;
    }

    /**
     * @param direction sets the value to signify which direction is being traveled
     */
    public void setDirection(String direction) {
        this.direction = direction;
    }

    /**
     * @return the distance being traveled
     */
    public double getDistance() {
        return distance;
    }

    /**
     * @param distance the distance being traveled
     */
    public void setDistance(double distance) {
        this.distance = distance;
    }

    /**
     * @return object for where the truck started the travel
     */
    public Object getStart_loc() {
        return start_loc;
    }

    /**
     * @return object for where the truck ended the travel
     */
    public Object getEnd_loc() {
        return end_loc;
    }

    /**
     * @param end_loc the target location for the travel
     */
    public void setEnd_loc(Object end_loc) {
        this.end_loc = end_loc;
    }

    /**
     * @return the date of the traveling
     */
    public LocalDate getTime() {
        return time;
    }

    /**
     * @param time holds the date being set for the travel
     */
    public void setTime(LocalDate time) {
        this.time = time;
    }

    /**
     * @return the minutes needed to complete the travel
     */
    public int getMinutes_needed() {
        return minutes_needed;
    }
}
