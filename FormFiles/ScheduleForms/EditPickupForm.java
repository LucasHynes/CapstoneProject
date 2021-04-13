package FormFiles.ScheduleForms;

import Database.DatabaseAccess;
import FormFiles.ErrorWindow;
import Objects.Order;
import Objects.Truck;
import Scheduling.DeliveryList;
import Scheduling.LinearRegression;
import Scheduling.ScheduleChartObject;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Form used to add a pick-up for the truck based on user input around the details of the pickup
 *
 * @author Lucas Hynes
 * @version 1.0.0
 * @since 2/24/2021
 */
public class EditPickupForm {

    //sets the database access method
    private final DatabaseAccess dba = new DatabaseAccess();

    //holds the delivery list of the active truck
    private DeliveryList active_list;

    private Object current_location;
    private LocalDate date;

    /**
     * Used to return the scene to the user around entering a new pickup for the truck allowing the user to add
     * data to the form to better plan the schedule
     *
     * @param t the active pickup
     * @return the scene for the window
     * @throws SQLException handles invalid data exceptions
     */
    public Scene form(ScheduleChartObject t) throws SQLException {

        //table of the open orders for the user to select from and then allows for multiple selections
        TableView<Order> open_order = unassigned_orders_chart();
        open_order.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        //populates the table with the databases open orders
        if (getDba().get_open_orders(getCurrent_location()).size() > 0) {
            open_order.getItems().addAll(getDba().get_open_orders(getCurrent_location()));
        }

        //time field for the user to specify the day of the pickup
        DatePicker time_field = new DatePicker();
        time_field.setValue(getDate());
        time_field.setPromptText("Date of Pickup");

        //text field for the minutes needed for the pickup
        TextField minutes_needed = new TextField();
        minutes_needed.setPromptText("Minutes Needed");
        

        //adds buttons to the window to allow the user to navigate the application
        Button add_button = new Button("Save");
        Button cancel_button = new Button("Cancel");

        //event for the add button
        EventHandler<MouseEvent> add_event = e -> {

            //gets the list of orders selected by the user
            ObservableList<Order> list_orders = open_order.getSelectionModel().getSelectedItems();
            ObservableList<Integer> index = open_order.getSelectionModel().getSelectedIndices();
            int index_count = 0;
            //gets active list in function
            DeliveryList list = getActive_list();

            //loops through the selected orders
            for(Order o: list_orders) {
                int i = index.get(index_count);
                LocalDate date;
                int min;

                //attempts to update the status of the order to show that the order has been picked up
                try {
                    getDba().updateStatus("Picked up", o.getOrder_id());

                } catch (SQLException throwables) {

                    //displays errors encountered with details
                    new ErrorWindow(new Stage(), String.valueOf(
                            throwables.getErrorCode()), throwables.getMessage());
                }

                //checks for null values, filling with the correct values
                if(list.getTruck() == null) {
                    list.setTruck(new Truck());
                }

                //checks to see if the date has been selected for the pickup
                if (time_field.getValue() == null) {

                    //checks for the minutes field
                    if (minutes_needed.getText().isEmpty()) {

                        //instance to calculate the linear regression estimation for the amount of time needed
                        LinearRegression lr = new LinearRegression();

                        //object to hold x and y values
                        Object[] p;

                        //attempts to retrieve data for the pickup times
                        try { p = getDba().get_historical_stat("Pick-up"); } catch (SQLException throwables) {

                            //displays errors encountered with details
                            new ErrorWindow(new Stage(), String.valueOf(
                                    throwables.getErrorCode()), throwables.getMessage());

                            //creates default list of objects
                            p = new Object[]{new ArrayList<Double>(), new ArrayList<Double>()};
                        }

                        //calculates the minutes needed for the pick up based on linear regression
                        //noinspection unchecked
                        @SuppressWarnings("unchecked") int min_needed = (int) lr.run(((ArrayList<Double>) p[0]), ((ArrayList<Double>) p[1]), o.getCubes());
                        
                        date = LocalDate.parse("0000-01-01");
                        min = min_needed;                        

                        
                    } else {

                        //adds the default date and the user entered minutes needed
                        date = LocalDate.parse("0000-01-01");
                        min = input_int_check(minutes_needed);
                    }
                } else {

                    //checks the minutes field
                    if (minutes_needed.getText().isEmpty()) {

                        //instance to calculate the linear regression estimation for the amount of time needed
                        LinearRegression lr = new LinearRegression();

                        //object to hold x and y values
                        Object[] p;

                        //attempts to retrieve data for the pickup times
                        try { p = getDba().get_historical_stat("Pick-up"); } catch (SQLException throwables) {

                            //displays errors encountered with details
                            new ErrorWindow(new Stage(), String.valueOf(
                                    throwables.getErrorCode()), throwables.getMessage());

                            //creates default list of objects
                            p = new Object[]{new ArrayList<Double>(), new ArrayList<Double>()};
                        }

                        //calculates the minutes needed for the pick up based on linear regression
                        //noinspection unchecked
                        @SuppressWarnings("unchecked") int min_needed = (int) lr.run((
                                (ArrayList<Double>) p[0]), ((ArrayList<Double>) p[1]), o.getCubes());

                        //adds the pickup with selected time and calculated minutes needed
                        date = time_field.getValue();
                        min = min_needed;
                    }
                    else {

                        //adds the default date and the user entered minutes needed
                        date = time_field.getValue();
                        min = input_int_check(minutes_needed);
                    }
                }

                //adds the pickup with given values
                ScheduleChartObject o1 = new ScheduleChartObject(o.getDistributor().getName(),
                o.getStore_front().getName(), o.getCubes() + "c, " 
                + o.getPieces() + "p", o.getWeight() + "lbs", 
                "Pick-up", date, min);

                list.editPickup(o1, i);
            }
            
            //sets the list to the new list with the pickup object added
            setActive_list(list);

            //closes the active window
            ((Stage) add_button.getScene().getWindow()).close();
        };

        //adds the event to the button
        add_button.addEventHandler(MouseEvent.MOUSE_CLICKED, add_event);

        //event for cancel button to close the active window applied to the button
        EventHandler<MouseEvent> cancel_event = e -> ((Stage) add_button.getScene().getWindow()).close();
        cancel_button.addEventHandler(MouseEvent.MOUSE_CLICKED, cancel_event);

        //grid pane for layout of the window
        GridPane g = new GridPane();

        //adds the elements of the window to the grid pane
        g.add(time_field, 0, 0, 1, 1);
        g.add(minutes_needed, 0, 1, 1, 1);
        g.add(open_order, 1, 1, 3, 2);
        g.add(add_button, 3, 4, 1, 1);
        g.add(cancel_button, 2, 4, 1, 1);

        //sets spacing and padding for the grid pane
        g.setHgap(20);
        g.setVgap(20);
        g.setPadding(new Insets(20));

        //returns the scene with the grid pane
        return(new Scene(g));
    }

    /**
     * Defines the table to be viewed by the user, displays the orders that have not yet been assigned
     *
     * @return the table view in the proper format
     */
    @SuppressWarnings("unchecked") private TableView<Order> unassigned_orders_chart() {

        //table view variable to return
        TableView<Order> order_chart = new TableView<>();

        //defines the column for the distributor
        TableColumn<Order, String> distributor = new TableColumn<>("Distributor");
        distributor.setCellValueFactory(new PropertyValueFactory<>("distributor_name"));

        //defines the column for the store front
        TableColumn<Order, String> store_front = new TableColumn<>("Store Front");
        store_front.setCellValueFactory(new PropertyValueFactory<>("store_front_name"));

        //defines the column for the cubes
        TableColumn<Order, Double> cubes = new TableColumn<>("Cubes");
        cubes.setCellValueFactory(new PropertyValueFactory<>("cubes"));

        //defines the column for the weight
        TableColumn<Order, Double> weight = new TableColumn<>("Weight");
        weight.setCellValueFactory(new PropertyValueFactory<>("weight"));

        //defines the column for the pieces
        TableColumn<Order, Double> pieces = new TableColumn<>("Pieces");
        pieces.setCellValueFactory(new PropertyValueFactory<>("pieces"));

        //defines the column for the merch price
        TableColumn<Order, Double> merch_price = new TableColumn<>("Merchandise Price");
        merch_price.setCellValueFactory(new PropertyValueFactory<>("merch_price"));

        //adds all columns to the chart
        //noinspection unchecked
        order_chart.getColumns().addAll(distributor, store_front, cubes, weight, pieces, merch_price);

        //returns the table view
        return order_chart;
    }

    /**
     * @return method access to the database
     */
    public DatabaseAccess getDba() {
        return dba;
    }

    /**
     * @return trucks delivery list
     */
    public DeliveryList getActive_list(){
        return active_list;
    }

    /**
     * sets the list for the method input
     * @param active_list the list to add
     */
    public void setActive_list(DeliveryList active_list) {

        //attempts to assign the value
        try {
            this.active_list = active_list;
        } catch (Exception e) {

            //handles errors displaying the problem to the user
            new ErrorWindow(new Stage(), e.toString(), e.getMessage());
        }
    }

    /**
     * @return the current location for the pickup
     */
    public Object getCurrent_location() {
        return current_location;
    }

    /**
     * @param current_location sets the value to the value of the current location
     */
    public void setCurrent_location(Object current_location) {
        this.current_location = current_location;
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
