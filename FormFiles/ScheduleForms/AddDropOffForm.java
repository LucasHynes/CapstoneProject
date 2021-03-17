package FormFiles.ScheduleForms;

import Database.DatabaseAccess;
import FormFiles.ErrorWindow;
import Scheduling.DeliveryList;
import Scheduling.LinearRegression;
import Scheduling.ScheduleChartObject;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Used to add a drop off to the trucks schedule through a user interface to display the information. Based on trucks
 * current location and the list of packages currently associated with the trucks to figure out which orders need to be
 * dropped off. Only works with the order's target location being the current location of the truck
 *
 * @author Lucas Hynes
 * @version 1.0.0
 * @since 2/24/2021
 */
public class AddDropOffForm {

    //active list for the truck
    private DeliveryList list;

    //name of the location the truck is currently at
    private String target_name;

    //allows method calls which access the application's associated database
    private final DatabaseAccess dba = new DatabaseAccess();

    //holds the date of the drop off
    private LocalDate date;

    /**
     * Used to return the scene allowing the user to see which orders would be getting dropped off and then
     * entering the estimated amount of time it would take to drop off the amount of material being dropped off
     *
     * @return the scene allowing the user to see and enter information about the drop off
     */
    public Scene form() {

        //defines the grid to hold the elements of the window
        GridPane layout = new GridPane();

        //label to show context to the user
        Label dropOffLabel = new Label("Dropping the following things:");

        //adds the label to the grid pane
        layout.add(dropOffLabel, 0, 0, 1, 1);

        // defines the text field for user input
        TextField minutes_needed = new TextField();

        //label to help user understand
        Label minutes_label = new Label("Minutes Needed");

        //count of the rows in the grid pane
        int count = 1;

        //list of the orders associated with the current location
        ArrayList<ScheduleChartObject> l = dropOffTable(getTarget_name());

        //goes through the returned objects
        for(ScheduleChartObject o:l){

            //attempts to update the status of the order to show that the order has been picked up
            try {
                getDba().updateStatus("Order Complete", o.getOrder().getOrder_id());

            } catch (SQLException throwables) {

                //displays errors encountered with details
                new ErrorWindow(new Stage(), String.valueOf(
                        throwables.getErrorCode()), throwables.getMessage());
            }

            //adds a label per object including the information about the order to the user
            layout.add(new Label(
                    o.getCurrent_loc() + " to " + o.getTarget_loc() + "(" +
                    o.getOrder().getCubes() + "c, " + o.getOrder().getWeight() + " lbs, " +  o.getOrder().getPieces() +
                    "p)"), 0, count, 2, 1);

            //adds to the count to be able to know the size of the grid pane
            count += 1;
        }

        //adds the elements to the grid pane with the spacing needed as according to the number of
        // orders being dropped off
        layout.add(minutes_needed, 1, count+1, 1, 1);
        layout.add(minutes_label, 0, count+1, 1, 1);

        //defines ok button
        Button ok_button = new Button("OK");

        //defines event for the ok button
        EventHandler<MouseEvent> ok_event = e -> {

            //holds the minutes needed for the drop off
            int minute_needed = 0;

            //checks if the field is empty
            if (minutes_needed.getText().isEmpty()) {

                //because empty: use linear regression to predict the time needed for the drop off
                LinearRegression lr = new LinearRegression();

                //to hold the x, y values needed for regression
                Object[] p;

                //holds the list of objects being dropped off
                ArrayList<ScheduleChartObject> l1 = dropOffTable(getTarget_name());

                //loops through given orders as the time is per order not per drop off
                for(ScheduleChartObject o:l1) {

                    //attempts to return the stats needed for the regression
                    try { p = getDba().get_historical_stat("Drop Off"); } catch (SQLException throwables) {

                        //displays errors encountered with details
                        new ErrorWindow(new Stage(), String.valueOf(throwables.getErrorCode()), throwables.getMessage());

                        //creates default object
                        p = new Object[]{new ArrayList<Double>(), new ArrayList<Double>()};
                    }

                    //finds the result based on input values found from database
                    //noinspection unchecked
                    minute_needed += (int) lr.run((
                            (ArrayList<Double>) p[0]), ((ArrayList<Double>) p[1]), o.getOrder().getCubes());
                }
            }
            else {

                //parses the user input values
                minute_needed = input_int_check(minutes_needed);
            }

            //adds the drop off to the delivery list to be updated in the main form
            setList(add_drop_to_list(dropOffTable(getTarget_name()), minute_needed));

            //closes active window
            ((Stage)ok_button.getScene().getWindow()).close();
        };

        //adds event to the button
        ok_button.addEventHandler(MouseEvent.MOUSE_CLICKED, ok_event);

        //defines cancel button, with event applied to close window at the event
        Button cancelButton = new Button("Cancel");
        EventHandler<MouseEvent> cancel_event = e -> ((Stage) cancelButton.getScene().getWindow()).close();
        cancelButton.addEventHandler(MouseEvent.MOUSE_CLICKED, cancel_event);

        //adds the parts of the window (final) to the grid at the end to make sure the object are at the very bottom
        layout.add(ok_button, 4, count+2, 1, 1);
        layout.add(cancelButton, 3, count+2, 1, 1);

        //sets spacing and padding for the grid
        layout.setVgap(20);
        layout.setHgap(20);
        layout.setPadding(new Insets(20));

        //returns the interface as a scene
        return new Scene(layout);
    }

    /**
     * @return method access to the database
     */
    private DatabaseAccess getDba() {
        return dba;
    }

    /**
     * @return active delivery list
     */
    public DeliveryList getList() {
        return list;
    }

    /**
     * @param list sets active list to passed parameter
     */
    public void setList(DeliveryList list) {
        this.list = list;
    }


    /**
     * Function used to find the orders that have been picked up and not yet dropped off, and adds them to a
     * list to be returned to the user to be able to see what is being dropped off at the current location
     *
     * @param target_name the name of the current location
     * @return the list of orders to be dropped off at the location
     */
    private ArrayList<ScheduleChartObject> dropOffTable(String target_name) {

        //retrieves the truck's delivery list to be used
        DeliveryList l = getList();

        //list to hold the objects
        ArrayList<ScheduleChartObject> r = new ArrayList<>();

        //loops through an array list of the schedule objects
        for(ScheduleChartObject o:l.get_chart_fill_1()){

            //finds if the object is for a pickup
            if(o.getCategory().equals("Pick-up")) {

                //checks if the pickup target location is the same as the current location
                if(o.getTarget_loc().equals(target_name)){

                    //adds the object to the list and sets the order associated to the list to deep copy the instance
                    r.add(o);
                    r.get(r.size()-1).setOrder(o.getOrder());
                }
            }
        }

        //returns the list of orders to be dropped off
        return r;
    }

    /**
     * @return string value of current location
     */
    public String getTarget_name() {
        return target_name;
    }

    /**
     * @param target_name sets the current location for the class
     */
    public void setTarget_name(String target_name) {
        this.target_name = target_name;
    }

    /**
     * Takes the list of object's to add an associated drop off with, and the time needed, and adds the
     * information gathered from the form to create the drop off object
     *
     * @param to_drop list of elements to make associated drop off with (list of pickup's w/ associated orders)
     * @param minutes_needed number of minutes needed for the drop off
     * @return the updated delivery list with the new drop off event
     */
    private DeliveryList add_drop_to_list(ArrayList<ScheduleChartObject> to_drop, int minutes_needed){

        //list associated with active truck
        DeliveryList l = getList();

        //attempts to loop through the objects, adding the drop off objects per order
        try {
            for (ScheduleChartObject o : to_drop) { l.add_delivery(o.getOrder(), o.getTime(), minutes_needed); }
        } catch (Exception e) {

            //displays the error window with error messages for the user
            new ErrorWindow(new Stage(), e.toString(), e.getMessage());
        }

        //returns the new list for the delivery list
        return l;
    }

    /**
     * @return the date of the pickup
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * @param date sets the date of the pickup
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Checks the values in the field to make sure that they are integers and are able to be parsed
     *
     * @param t text field to check
     * @return the int value if available, otherwise 0
     */
    private int input_int_check(TextField t) {
        if(t.getText().isEmpty()) {
            return 0;
        } else {
            try {
                return Integer.parseInt(t.getText());

            } catch (NumberFormatException e1) {
                return 0;
            }
        }
    }
}
