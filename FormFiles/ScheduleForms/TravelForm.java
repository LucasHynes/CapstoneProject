package FormFiles.ScheduleForms;

import Database.DatabaseAccess;
import FormFiles.ErrorWindow;
import Objects.Distributor;
import Objects.Order;
import Objects.StoreFront;
import Scheduling.DeliveryList;
import Scheduling.DirectionDistance;
import Scheduling.LinearRegression;
import Scheduling.ScheduleChartObject;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * This class is used by the user to be able to add a travel object changing the trucks current location allow
 * for the viewing stats around the travel selection and filtering travel options to only show the distributors
 * and the store fronts that have loaded cargo as these are the only two locations to go to
 *
 * @author Lucas Hynes
 * @version 1.0.0
 * @since 2/24/2021
 */
public class TravelForm {

    //holds the location and target location for selected truck
    private Object current_loc;
    private Object nextLoc;

    //holds the active delivery list
    private DeliveryList dl;

    //access to methods of the database through the object
    private final DatabaseAccess dba = new DatabaseAccess();

    //date for the travel
    private LocalDate date;

    /**
     * Used to return the scene for the window to display the options to the user around the travel selections
     * for the truck
     *
     * @return the scene for the user interactions
     * @throws SQLException handles invalid data exceptions
     */
    public Scene form() throws SQLException {

        //text field to hold the location name
        TextField loc_label;

        //used to get input from the user around the day of the event
        Label time_label = new Label("Date of Travel");
        DatePicker time_field = new DatePicker();
        time_field.setValue(getDate());

        //defined to get the minutes needed for the travel
        TextField minutes_needed_field = new TextField();

        //checks if the object is a store front or a distributor
        if (current_loc instanceof StoreFront) {

            //sets the name to the objects name
            loc_label = new TextField((((StoreFront) current_loc).getName()));
        } else if (current_loc instanceof Distributor) {

            //sets the name to the objects name
            loc_label = new TextField(((Distributor) current_loc).getName());

        } else {
            loc_label = new TextField(" ");
        }

        //user cannot change current location within this form
        loc_label.setEditable(false);

        //combo box to hold possible locations for the trucks to travel to
        ComboBox<String> destination_choice = new ComboBox<>();
        destination_choice.getItems().addAll(destinationOptions());
        destination_choice.setEditable(true);

        //declares ok button for the window
        Button ok_button = new Button("OK");

        //defines event for the ok button
        @SuppressWarnings("unchecked") EventHandler<MouseEvent> ok_event = e -> {

            //holds the location variables for the two locations
            double lat_1;
            double long_1;
            double lat_2;
            double long_2;

            //empty object to hold the destination
            Object x = null;

            //attempts to assign x to the selected destination
            try {
                x = getDba().get_destination_object(destination_choice.getSelectionModel().getSelectedItem());

            } catch (SQLException throwables) {

                //displays errors encountered with details
                new ErrorWindow(new Stage(), String.valueOf(
                        throwables.getErrorCode()), throwables.getMessage());
            }

            //checks for the instance type of the truck's current location
            if (current_loc instanceof StoreFront) {

                //retrieves the long and lat for the current location
                lat_1 = ((StoreFront) current_loc).getLatitude();
                long_1 = ((StoreFront) current_loc).getLongitude();

            } else  if (current_loc instanceof Distributor) {

                //retrieves the long anf lat for the current location
                lat_1 = ((Distributor) current_loc).getLatitude();
                long_1 = ((Distributor) current_loc).getLongitude();

            } else {

                //default values
                lat_1 = 0;
                long_1 = 0;
            }

            //sets the location selected to the internal variable
            setNextLoc(x);

            //check for proper input
            if(x != null) {

                //checking for the instance type for the object
                if (x instanceof StoreFront) {

                    //retrieves the long and lat for the destination location
                    lat_2 = ((StoreFront) x).getLatitude();
                    long_2 = ((StoreFront) x).getLongitude();

                } else  if (x instanceof Distributor){

                    //retrieves the long and lat for the destination location
                    lat_2 = ((Distributor) x).getLatitude();
                    long_2 = ((Distributor) x).getLongitude();
                } else {

                    //default values
                    long_2 = 0;
                    lat_2 = 0;
                }

            } else {

                //default values to have no distance calculations necessary
                lat_2 = lat_1;
                long_2 = long_1;
            }

            //sets the class instance for finding the distance and direction for the travel
            DirectionDistance dd = new DirectionDistance(long_1, lat_1, long_2, lat_2);

            //holds the date of the travel
            LocalDate time;

            //holds the number of minutes needed to complete the travel
            int minutes;

            //checks valid input values
            if(time_field.getValue() == null){

                //assigns time to default value
                time = LocalDate.parse("0000-01-01");

            } else {

                //assigns the time to the selected value
                time = time_field.getValue();
            }

            //checking for the need of linear regression
            if(minutes_needed_field.getText().isEmpty()) {

                //new instance to find the linear regression estimation
                LinearRegression lr = new LinearRegression();

                //holds the data to use during the regression process
                Object[] p;

                //attempts to connect and retrieve the proper travel stats from historical loads
                try {
                    p = getDba().get_historical_stat("Travel");

                } catch (SQLException throwables) {

                    //throws error window to the user to show the user error details
                    new ErrorWindow(new Stage(), String.valueOf(throwables.getErrorCode()), throwables.getMessage());

                    //default value
                    p = new Object[]{new ArrayList<Double>(), new ArrayList<Double>()};
                }

                //finds the linear regression calculation
                //noinspection unchecked
                minutes = (int) lr.run(((ArrayList<Double>) p[0]), ((ArrayList<Double>) p[1]), dd.get_distance());

            } else {

                //the minutes needed is filled and the answer is taken as input
                minutes = input_int_check(minutes_needed_field);
            }

            //retrieves the delivery list
            DeliveryList dl = getDl();

            //attempts to add the travel object to the delivery list
            try {
                dl.add_travel(current_loc,
                        getDba().get_destination_object(destination_choice.getSelectionModel().getSelectedItem()),
                        dd.get_direction(), dd.get_distance(), time, minutes);

            } catch (SQLException throwables) {

                //throws error information for the user
                new ErrorWindow(new Stage(), String.valueOf(throwables.getErrorCode()), throwables.getMessage());
            }

            //updates to the newest delivery list
            setDl(dl);

            //closes the active window
            ((Stage) ok_button.getScene().getWindow()).close();
        };

        //applies the event to the button
        ok_button.addEventHandler(MouseEvent.MOUSE_CLICKED, ok_event);

        //the cancel button, with the event applied to the button to close the window
        Button cancel_button = new Button("Cancel");
        EventHandler<MouseEvent> cancel_event = e -> ((Stage) cancel_button.getScene().getWindow()).close();
        cancel_button.addEventHandler(MouseEvent.MOUSE_CLICKED, cancel_event);

        //the grid for the layout of the window
        GridPane gridPane = new GridPane();

        //adds the elements of the window to the grid for proper layout
        gridPane.add(loc_label, 0, 0, 3, 1);
        gridPane.add(destination_choice, 0, 1, 3, 1);
        gridPane.add(time_label, 0, 2, 1, 1);
        gridPane.add(time_field, 1, 2, 1, 1);
        gridPane.add(minutes_needed_field, 2, 2, 1, 1);
        gridPane.add(cancel_button, 1, 3, 1, 1);
        gridPane.add(ok_button, 2, 3, 1, 1);

        //sets the spacing and padding for the pane
        gridPane.setVgap(20);
        gridPane.setHgap(20);
        gridPane.setPadding(new Insets(20));


        //attempts to return the scene with grid layout
        try {
            return new Scene(gridPane);

        } catch (Exception e) {

            //throws error window for invalid input
            new ErrorWindow(new Stage(), e.toString(), e.getMessage());

            //closes the active window
            ((Stage) cancel_button.getScene().getWindow()).close();

            //returns null if the scene is unable to launch
            return null;
        }
    }

    /**
     * @param current_loc set the object passed into the current location variable
     */
    public void setCurrent_loc(Object current_loc) {
        this.current_loc = current_loc;
    }

    /**
     * @return method access to the database through object
     */
    public DeliveryList getDl() {
        return dl;
    }

    /**
     * Sets the delivery list, or throws an error in case of failure
     *
     * @param dl the delivery list being set
     */
    public void setDl(DeliveryList dl) {

        //attempts to assign the list
        try {
            this.dl = dl;

        } catch (Exception e) {

            //throws error window with information to the user around the error
            new ErrorWindow(new Stage(), e.toString(), e.getMessage());
        }
    }

    /**
     * @return gets the database in use for method calls
     */
    public DatabaseAccess getDba() {
        return dba;
    }

    /**
     * @return the name of the location being visited next depending on class instance
     */
    public String getNextLoc_String() {

        //gets the next location object
        Object x = getNextLoc();

        //checks the instance of the object and returns the name of the object
        if (x instanceof StoreFront){
            return ((StoreFront) x).getName();
        } else if (x instanceof Distributor) {
            return ((Distributor) x).getName();
        } else {
            return "";
        }
    }

    /**
     * @return object that is the next location, either Distributor or Store Front
     */
    public Object getNextLoc() {
        return nextLoc;
    }

    /**
     * @param o the object to set as the next location, either Distributor or Store Front
     */
    public void setNextLoc(Object o) {
        this.nextLoc = o;
    }

    /**
     * Used to get the available locations for the truck to travel to, only store fronts with a loaded associated
     * order and all distributors to better limit user options
     *
     * @return list of available locations to travel to
     */
    private ArrayList<String> destinationOptions() throws SQLException {

        //sets the delivery list
        DeliveryList dl = getDl();

        //list to hold the names of the destination options
        ArrayList<String> s = getDba().get_distributors_names();

        //list to hold the objects
        ArrayList<Order> r = new ArrayList<>();

        //loops through an array list of the schedule objects
        for(ScheduleChartObject o: dl.get_chart_fill_1()) {

            //finds if the object is for a pickup
            if (o.getCategory().equals("Pick-up")) {

                //adds the order if it is a pick up, because it is still an option
                r.add(o.getOrder());
            }
            else if (o.getCategory().equals("Drop Off")) {

                //loops through the stored order
                for(Order or: r) {

                    //checks for matching id
                    if(or.getOrder_id() == o.getOrder().getOrder_id()){

                        //removes the order because it's been delivered
                        r.remove(or);
                        break;
                    }
                }
            }
        }

        //loops through the resulting orders getting the store front names
        for (Order o: r) {

            //if the location is not already within the list, add it to the list
            if(!s.contains(o.getStore_front().getName())) { s.add(o.getStore_front().getName()); }
        }

        //returns the string list
        return s;
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
