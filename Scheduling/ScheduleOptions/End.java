package Scheduling.ScheduleOptions;

import java.time.LocalDate;

/**
 * This class is used to represent when the driver is done for the day and ends the work day to start again the next
 * day
 *
 * @author Lucas Hynes
 * @version 1.0.0
 * @since 2/24/2021
 */
public class End {

    //holds internal variables for information
    private final LocalDate end_time;
    private final int rest_time_hours;

    /**
     * This is the constructor for the end of the day method
     *
     * @param end_time the date of the end
     * @param rest_time_hours the time needed to rest
     */
    public End(LocalDate end_time, int rest_time_hours) {

        //sets internal variables to assigned parameters
        this.end_time = end_time;
        this.rest_time_hours = rest_time_hours;
    }

    /**
     * @return needed rest time associated for the object
     */
    public int getRest_time_hours() {
        return rest_time_hours;
    }

    /**
     * @return the date associated
     */
    public LocalDate getEnd_time() {
        return end_time;
    }
}
