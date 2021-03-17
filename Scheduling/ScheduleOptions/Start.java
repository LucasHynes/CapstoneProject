package Scheduling.ScheduleOptions;

import java.time.LocalDate;

/**
 * Class used to signify the start of the day for a truck
 *
 * @author Lucas Hynes
 * @version 1.0.2
 * @since 2/25/2021
 */
public class Start {
    private final LocalDate start_time;
    private final int minutes_to_first_step;

    /**
     * Constructor for the start object to be built
     *
     * @param start_time the time to start
     * @param minutes_to_first_step the minutes left to the next step
     */
    public Start(LocalDate start_time, int minutes_to_first_step) {

        //assigns the parameters to the internal variables
        this.start_time = start_time;
        this.minutes_to_first_step = minutes_to_first_step;
    }

    /**
     * @return the date of the start event
     */
    public LocalDate getStart_time() {
        return start_time;
    }

    /**
     * @return the minutes needed to get to the next event
     */
    public int getMinutes_to_first_step() {
        return minutes_to_first_step;
    }
}

