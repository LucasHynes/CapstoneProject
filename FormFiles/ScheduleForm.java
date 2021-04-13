package FormFiles;

import Database.DatabaseAccess;
import FormFiles.ScheduleForms.AddDropOffForm;
import FormFiles.ScheduleForms.AddPickupForm;
import FormFiles.ScheduleForms.EditPickupForm;
import FormFiles.ScheduleForms.TravelForm;
import Objects.*;
import Scheduling.DeliveryList;
import Scheduling.ScheduleChartObject;
import Scheduling.ScheduleOptions.DropOff;
import Scheduling.ScheduleOptions.Pickup;
import Scheduling.ScheduleOptions.Travel;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * The use of this class is to allow the user to be able to add and pull up deliveries within the database
 * and to add the activities to the trucks such as picking up and dropping off orders as well as traveling
 * to the different locations. The stats are used within the tabs to show the active users the levels of
 * product on each of the trucks in the order that is correct with a mix of pick ups and drop offs. The use
 * of the save button is to then close the program, and to be able to reopen with the schedule still there, 
 * while the end week button will clear the current schedule and remove the files, as they are marked 
 * delivered at the end of the week and the delivery times are submitted to the historical loads and 
 * delivery times tables to use in reports and linear regression analysis.
 * 
 * @author Lucas Hynes
 * @version 1.1.2
 * @since 2/18/2021
 */
public class ScheduleForm {

    //holds the list of the class objects for all the different trucks
    private final ArrayList<DeliveryList> deliveryListList = new ArrayList<>();
    
    //holds method access to the database for the use of methods throughout
    private final DatabaseAccess dba = new DatabaseAccess();
    
    //holds the current location of the truck, used for travel and drop off settings
    private Object current_loc;

    //holds the last entered date, default to the day of use
    private LocalDate current_date = LocalDate.now();

    /**
     * This method returns the window holding the information to the user around what is being scheduled for 
     * each truck in use to assign all of the packages to the correct destinations
     * 
     * @return the scene to show the user how to schedule
     * @throws SQLException handles invalid data
     */
    public Scene form() throws SQLException {

        //list to hold the tabs of the different trucks
        ArrayList<Tab> truck_tabs = new ArrayList<>();


        //holds the categories for the user to select to add to the schedule
        ComboBox<String> category = new ComboBox<>();
        category.getItems().addAll("Start", "End", "Pick-up", "Drop-off", "Travel");

        //initializes all data for each of the trucks by looping through each truck by name
        for(String truck_name: getDba().get_truck_string()) {

            //table to hold the information around the schedule for the truck format in internal method
            TableView<ScheduleChartObject> truck_delivery_list = truck_delivery_chart();
            
            //sets the id of the table to find the truck's table
            truck_delivery_list.setId("truck_table_" + truck_name);

            //holds the list to use in the parameters
            ArrayList<ScheduleChartObject> a = getDba().get_truck_load(getDba().get_truck_based_name(truck_name));

            //adds all of the data to the chart, pulling info from the database
            truck_delivery_list.getItems().addAll(a);

            //adds the list saved in the database to the variable saved within the class
            deliveryListList.add(getDL(a, truck_name));

            //holds settings around the table
            truck_delivery_list.setPrefSize(800, 500);

            
            //gets the combo box for the destinations available for the truck to travel to
            ComboBox<String> c = new ComboBox<>();
            c.getItems().addAll(getDba().get_destination_names());

            //checks the size of the delivery list
            if((deliveryListList.size() > 0) && (deliveryListList.get(
                    deliveryListList.size() - 1).get_chart_fill_1().size() > 0)) {

                //selects the most recent location
                c.getSelectionModel().select(deliveryListList.get(
                        deliveryListList.size() - 1).get_chart_fill_1().get(deliveryListList.get(
                                deliveryListList.size() - 1).get_chart_fill_1().size() - 1).getTarget_loc());
            }

            //sets the id for the combo box to be used later
            c.setId("combo_" + truck_name);

            //gets the current stats for the trucks
            Object[] q = findCurrentStats(truck_delivery_list.getItems());
            
            //adds the label and id for each of the stats showing the current loads of the truck
            //shows current cube levels
            Label total_cubes = new Label(String.valueOf(q[0]));
            total_cubes.setId("cube"+truck_name);

            //shows current weight values
            Label total_weight = new Label(String.valueOf(q[1]));
            total_weight.setId("lbs"+truck_name);

            //shows current piece count
            Label total_pieces = new Label(String.valueOf(q[2]));
            total_pieces.setId("piece"+truck_name);

            //shows current merch price of load
            Label total_merch_price = new Label(String.valueOf(q[3]));
            total_merch_price.setId("merch"+truck_name);

            //shows current freight amount for the load
            Label total_freight_charge = new Label(String.valueOf(q[4]));
            total_freight_charge.setId("freight"+truck_name);

            //shows the current miles traveled
            Label miles = new Label(String.valueOf(q[5]));
            miles.setId("miles"+truck_name);


            //labels to help show context to the user around data entry
            Label cube_label = new Label("Cubes:");
            Label weight_label = new Label("Weight:");
            Label piece_label = new Label("Pieces:");
            Label merch_label = new Label("Merch Price:");
            Label freight_label = new Label("Freight Fee: ");
            Label miles_label = new Label("Miles:");


            //Grid pane to hold the layout of the window
            GridPane gp = new GridPane();

            //adds the elements of the window to the grid pane by the layout
            gp.add(cube_label, 0, 0, 1, 1);
            gp.add(total_cubes, 1, 0, 1, 1);
            gp.add(weight_label, 2, 0, 1, 1);
            gp.add(total_weight, 3, 0, 1, 1);
            gp.add(piece_label, 4, 0, 1, 1);
            gp.add(total_pieces, 5, 0, 1, 1);
            gp.add(merch_label, 0, 1, 1, 1);
            gp.add(total_merch_price, 1, 1, 1, 1);
            gp.add(freight_label, 2, 1, 1, 1);
            gp.add(total_freight_charge, 3, 1, 1, 1);
            gp.add(miles_label, 4, 1, 1, 1);
            gp.add(miles, 5, 1, 1, 1);

            //sets spacing and padding for the grid pane
            gp.setPadding(new Insets(20));
            gp.setHgap(20);
            gp.setVgap(20);
            gp.setAlignment(Pos.BOTTOM_CENTER);


            //layout for the truck views
            VBox layout = new VBox(c, truck_delivery_list, gp);

            //spacing and padding for the truck tabs
            layout.setSpacing(20);
            layout.setPadding(new Insets(20));

            //adds the tab to the tab list to be able to create the final product of tabs for each of the trucks
            truck_tabs.add(new Tab(truck_name, layout));
        }


        //holds the pane for the trucks
        TabPane truck_select = new TabPane();
        truck_select.getTabs().addAll(truck_tabs);

        //add button to add the next activity to the schedule
        Button add_button = new Button("Add");

        //add the event for the add button
        @SuppressWarnings("unchecked") EventHandler<MouseEvent> add_event = e -> {

            //node to find the tab that is being used
            Node n_1 = (truck_select.getSelectionModel().getSelectedItem().getContent());

            //index of the node to be able to insert back into the list and to reference
            int i = truck_select.getSelectionModel().getSelectedIndex();

            //holds the name of the truck actively having an activity added to it's schedule
            String truckName = truck_select.getSelectionModel().getSelectedItem().getText();

            //delivery list of the active truck
            DeliveryList d;

            //checks for the delivery list already set
            if(getDeliveryListList().size() > i){

                //returns the actively used delivery list
                d = getDeliveryListList().get(i);

                //attempts to retrieve the truck object matched with truck name
                try {
                    d.setTruck(getDba().get_truck_based_name(truckName));
                    d.getTruck().setDriver(getDba().get_truck_based_name((truckName)).getDriver());

                } catch (SQLException throwables) {

                    //launches error window with explanation around the nature of the error
                    new ErrorWindow(new Stage(), String.valueOf(throwables.getErrorCode()), throwables.getMessage());
                }

            } else {

                //attempts to create a new delivery list with the associated truck
                try {
                    d = new DeliveryList(new ArrayList<>(), getDba().get_truck_based_name(truckName));

                    //sets the truck based on the name associated
                    d.setTruck(getDba().get_truck_based_name(truckName));
                    d.getTruck().setDriver(getDba().get_truck_based_name((truckName)).getDriver());

                } catch (SQLException throwables) {

                    //launches error window with explanation around the nature of the error
                    new ErrorWindow(new Stage(), String.valueOf(throwables.getErrorCode()), throwables.getMessage());

                    //holds new null default delivery list
                    d = new DeliveryList(new ArrayList<>(), new Truck());
                }
            }

            //gets the active table from the user
            @SuppressWarnings("unchecked") TableView<ScheduleChartObject> t = (
                    TableView<ScheduleChartObject>) n_1.lookup("#truck_table_" + truckName);

            //attempts to get the current location based on the entry to the combo box
            try {

                //noinspection unchecked
                setCurrent_loc(getDba().get_destination_object(((ComboBox<String>) n_1.lookup(
                        "#combo_" + truckName)).getValue()));

            } catch (SQLException throwables) {

                //launches error window with explanation around the nature of the error
                new ErrorWindow(new Stage(), String.valueOf(throwables.getErrorCode()), throwables.getMessage());
            }

            //gets the total list of delivery lists
            ArrayList<DeliveryList> al = getDeliveryListList();

            //defines the stage  to show the user the options around adding to the schedule
            Stage s = new Stage();

            //checking for the combo box to see which type of value should be added to the schedule
            switch (category.getValue()) {

                case "Start":

                    //adds the start event to the schedule
                    d.add_start(LocalDate.parse("0000-01-01"), 0);
                    break;

                case "Pick-up":

                    //adds the pickup form instance
                    AddPickupForm pickupForm = new AddPickupForm();

                    //sets the active list to the currently selected list
                    pickupForm.setActive_list(d);

                    //adds date to pickup form
                    pickupForm.setDate(getCurrent_date());

                    pickupForm.setCurrent_location(current_loc);

                    try {

                        //attempts to start the scene to add the pickup
                        s.setScene(pickupForm.form(getDba().get_truck_based_name(truckName)));

                        //sets the window to be viewed
                        s.setTitle("Add Pickup Order");
                        s.showAndWait();

                    } catch (SQLException throwables) {

                        //displays the error encountered to the user, does not allow the window to open
                        new ErrorWindow(new Stage(), String.valueOf(throwables.getErrorCode()), throwables.getMessage());
                    }

                    //retrieves the active delivery list updated from the window
                    d = pickupForm.getActive_list();

                    setCurrent_date(pickupForm.getDate());

                    //updates the statistics at the bottom of the screen
                    pickupStatUpdate(n_1, truckName, d);

                    break;

                case "Drop-off":

                    //adds the drop off by the user
                    AddDropOffForm dropOffForm = new AddDropOffForm();

                    dropOffForm.setDate(getCurrent_date());

                    //sets the list from the drop off form to the current list
                    dropOffForm.setList(d);

                    //checks for the instance of the object, then sets the target to the value found
                    if (getCurrent_loc() instanceof StoreFront) {

                        dropOffForm.setTarget_name(((StoreFront) getCurrent_loc()).getName());
                    }
                    else if (getCurrent_loc() instanceof Distributor){

                        dropOffForm.setTarget_name(((Distributor) getCurrent_loc()).getName());
                    }
                    else {

                        //set the default value
                        dropOffForm.setTarget_name("Unknown");
                    }

                    //settings for the window, applying the scene to a new window
                    s.setScene(dropOffForm.form());
                    s.setTitle("Add Drop-off Order");
                    s.showAndWait();

                    //gets the list from the window, after the completion from the process
                    d = dropOffForm.getList();

                    setCurrent_date(dropOffForm.getDate());

                    //updates the stats at the bottom of the page
                    dropOffStatUpdate(n_1, truckName, d);
                    
                    break;
                    
                case "Travel":

                    //sets the instance for the travel form
                    TravelForm tf = new TravelForm();

                    //sets the values before launching to set the correct internal variables
                    tf.setCurrent_loc(getCurrent_loc());
                    tf.setDl(d);
                    tf.setDate(getCurrent_date());

                    //attempts to set the scene with the one returned from the form method call
                    try {

                        s.setScene(tf.form());

                    } catch (SQLException throwables) {

                        //displays the error to the user explaining the error in more detail for the user
                        new ErrorWindow(new Stage(), String.valueOf(
                                throwables.getErrorCode()), throwables.getMessage());
                    }

                    //settings for the pop up
                    s.setTitle("Add Travel");
                    s.showAndWait();

                    //gets value for the new location post travel
                    setCurrent_loc(tf.getNextLoc());

                    setCurrent_date(tf.getDate());

                    //gets the name of the location and sets it to the selected value of the combo box
                    //noinspection unchecked
                    ((ComboBox<String>) n_1.lookup(
                            "#combo_" + truckName)
                    ).getSelectionModel().select(tf.getNextLoc_String());

                    //updates the delivery list being used
                    d = tf.getDl();

                    //updates the stats used
                    travelStatUpdate(n_1, truckName, d);
                    
                    break;
                    
                case "End":

                    //adds the end value to the scheduling form
                    d.add_end(LocalDate.parse("0000-01-01"), 0);
                    break;
                    
                default:
                    break;
            }

            //check to see if the chart is empty or not
            if(!t.getItems().isEmpty()) {

                //clear the items in the table
                t.getItems().clear();
                t.getItems().removeAll();
            }

            //checks wether to add or set the value within the list, based on the size to be editing the correct list
            if(getDeliveryListList().size() > i) { al.set(i, d); } else { al.add(i, d); }

            //adds the new items to the chart with the correct format (notice get chart fill 2 returns observable list
            t.getItems().addAll(getDeliveryListList().get(i).get_chart_fill_2());
        };

        //applies the event to the button
        add_button.addEventHandler(MouseEvent.MOUSE_CLICKED, add_event);

        Button edit_button = new Button("Edit Event");

        EventHandler<MouseEvent> edit_event = e -> {

            //node to find the tab that is being used
            Node n_1 = (truck_select.getSelectionModel().getSelectedItem().getContent());

            //index of the node to be able to insert back into the list and to reference
            @SuppressWarnings("unused") int i = truck_select.getSelectionModel().getSelectedIndex();

            //holds the name of the truck actively having an activity added to it's schedule
            String truckName = truck_select.getSelectionModel().getSelectedItem().getText();
            
            //gets the active table from the user
            @SuppressWarnings("unchecked") TableView<ScheduleChartObject> t = (
                    TableView<ScheduleChartObject>) n_1.lookup("#truck_table_" + truckName);

            switch (((ScheduleChartObject) t.getSelectionModel().getSelectedItem()).getCategory()) {
                case "Start":        
                    break;
                case "Pickup":

                    EditPickupForm epf = new EditPickupForm();

                    Scene s;

                    try {
                        s = epf.form((ScheduleChartObject) t.getSelectionModel().getSelectedItem());
                        Stage s1 = new Stage();
                        s1.setScene(s);
                        s1.showAndWait();

                    } catch (SQLException e1) {
                        //displays the error encountered to the user, does not allow the window to open
                        new ErrorWindow(new Stage(), String.valueOf(e1.getErrorCode()), e1.getMessage());
                    }

                    break;
                case "Drop Off":
                    break;
                case "Travel":
                    break;
                case "End":
                    break;
                default:
                    break;
            }
        };

        edit_button.addEventHandler(MouseEvent.MOUSE_CLICKED, edit_event);

        //exit button used to escape from the window, going to the main menu and closing the active window (no save)
        Button exit_button = new Button("Exit");
        EventHandler<MouseEvent> exit_event = e -> ((Stage) exit_button.getScene().getWindow()).close();
        exit_button.addEventHandler(MouseEvent.MOUSE_CLICKED, exit_event);

        //save button used to commit the data within the table to the database for the selected driver
        Button save_button = new Button("Save");

        //event handler for the save button
        EventHandler<MouseEvent> save_event = e -> {

            //attempts to clear the data from the table to go through and add the rest of the data from the
            //window back into the database
            try {
                getDba().deleteData("loads");

            } catch (SQLException throwables) {

                //handles errors displaying the details to the user
                new ErrorWindow(new Stage(), String.valueOf(throwables.getErrorCode()), throwables.getMessage());
            }

            //loops through the tabs to save all the information within the window
            for(Tab n_1:truck_select.getTabs()) {
                //gets the selected tab

                //gets the selected tab's schedule table
                @SuppressWarnings("unchecked") TableView<ScheduleChartObject> t = (
                        TableView<ScheduleChartObject>) n_1.getContent().lookup("#truck_table_" +
                        n_1.getText());

                //retrieves the objects from the table
                for (ScheduleChartObject o : t.getItems()) {

                    //checks for the truck to not be null
                    if (o.getTruck() == null) {

                        //attempts to set the truck to the truck with the matching name of the tabs within the database
                        try {
                            o.setTruck(getDba().get_truck_based_name(
                                    truck_select.getSelectionModel().getSelectedItem().getText()));

                        } catch (SQLException throwables) {

                            //throws the error window to the user with the problem
                            new ErrorWindow(new Stage(), String.valueOf(
                                    throwables.getErrorCode()), throwables.getMessage());
                        }
                    }

                    //checks the order of the truck, and if null adds a default class to avoid null pointer exceptions
                    if (o.getOrder() == null) {

                        o.setOrder(new Order(-1, "", "", new Distributor(),
                                null, null, 0, 0, 0, 0, ""));
                    }

                    //attempts to set the load of the current object into the database
                    try {
                        getDba().set_load(o);

                    } catch (SQLException throwables) {

                        //handles errors displaying the details to the user
                        new ErrorWindow(new Stage(), String.valueOf(throwables.getErrorCode()), throwables.getMessage());
                    }
                }
            }

            //closes the active window
            ((Stage) save_button.getScene().getWindow()).close();
        };

        //applies the event to the button
        save_button.addEventHandler(MouseEvent.MOUSE_CLICKED, save_event);

        //end week button to currently commit the data within all of the tables to the database as historical loads
        //setting up the database to begin the next week with the new values to input
        Button end_week = new Button("End Week");

        //adds the event to the end week button
        EventHandler<MouseEvent> end_week_event = e -> {

            //loops through all of the individual tabs
            for(Tab tab:truck_select.getTabs()) {

                //node set to the content within the tab
                Node n = tab.getContent();

                //index of the truck currently being saved
                int i = truck_select.getTabs().indexOf(tab);

                //list to fill with the results of the week
                DeliveryList d;

                //checks for the size of the delivery list to ensure accurate submissions
                if(getDeliveryListList().size() > i){

                    //assigns the value found within the delivery-list list
                    d = getDeliveryListList().get(i);

                    //attempts to assign the truck to the delivery list based on the name of the tab
                    try {

                        d.setTruck(getDba().get_truck_based_name((tab.getText())));
                        d.getTruck().setDriver(getDba().get_truck_based_name((tab.getText())).getDriver());

                    } catch (SQLException throwables) {

                        //handles errors, throwing a pop up window to the user to explain the nature of the error
                        new ErrorWindow(new Stage(), String.valueOf(throwables.getErrorCode()), throwables.getMessage());
                    }

                } else {

                    //adds a new value to the delivery list to handle the new data
                    try {

                        d = new DeliveryList(new ArrayList<>(), getDba().get_truck_based_name(tab.getText()));

                    } catch (SQLException throwables) {

                        //launches error window with error details
                        new ErrorWindow(new Stage(), String.valueOf(
                                throwables.getErrorCode()), throwables.getMessage());

                        //starts with empty and null values
                        d = new DeliveryList(new ArrayList<>(), new Truck());
                    }
                }

                //goes through the values of the active delivery list
                for(ScheduleChartObject o: d.get_chart_fill_1()){

                    //checks for the null value of the truck
                    if(o.getTruck() == null){

                        //attempts to assign the truck to the truck in database with matching name
                        try {

                            o.setTruck(getDba().get_truck_based_name(tab.getText()));

                        } catch (SQLException throwables) {

                            //launches error window with error details
                            new ErrorWindow(new Stage(), String.valueOf(
                                    throwables.getErrorCode()), throwables.getMessage());

                            o.setTruck(new Truck());
                        }
                    }

                    //checking for null order, making it default and empty
                    if(o.getOrder() == null) {
                       o.setOrder(new Order(-1, "", "", new Distributor(),
                               new StoreFront(), new RateSheet(), 0, 0, 0, 0, ""));
                    }

                    //adds the order to the historic load table in database
                    try {

                        getDba().set_historical_load(o);

                    } catch (SQLException throwables) {

                        //launches error window with error details
                        new ErrorWindow(new Stage(), String.valueOf(
                                throwables.getErrorCode()), throwables.getMessage());
                    }
                }

                //hoes through the delivery times for the orders in the week
                for(DeliveryTime dt: d.getDeliverTimes()) {

                    try {

                        //checks for null entries
                        if (dt!=null) {

                            //adds all of the delivery time values to the database
                            getDba().set_delivery_time(dt);
                        }

                    } catch (SQLException throwables) {

                        //launches error window with error details
                        new ErrorWindow(new Stage(), String.valueOf(
                                throwables.getErrorCode()), throwables.getMessage());
                    }
                }

                //gets the table of the truck with the associated tab name
                @SuppressWarnings("unchecked") TableView<ScheduleChartObject> t = (
                        TableView<ScheduleChartObject>) n.lookup("#truck_table_" + tab.getText());

                //clears the values and the table
                t.getItems().removeAll();
                t.getItems().clear();
                t.refresh();
            }

            //goes through the loads in the loads table, deleting all entries, but not the structure
            try {
                getDba().deleteData("loads");

            } catch (SQLException throwables) {

                //launches error window with error details
                new ErrorWindow(new Stage(), String.valueOf(throwables.getErrorCode()), throwables.getMessage());
            }

            //closes the window at the end of the execution of the code
            ((Stage) end_week.getScene().getWindow()).close();
        };

        //applies the listener to the button
        end_week.addEventHandler(MouseEvent.MOUSE_CLICKED, end_week_event);

        //layout of the window
        GridPane layout = new GridPane();

        //adds the elements to the pane
        layout.add(new VBox(category, add_button), 0, 4, 1, 1);
        layout.add(truck_select, 1, 0, 5, 5);
        layout.add(exit_button, 2, 5, 1, 1);
        layout.add(save_button, 3, 5, 1, 1);
        layout.add(end_week, 4, 5, 1, 1);
        layout.setAlignment(Pos.BOTTOM_CENTER);

        //sets spacing and padding for the pane
        layout.setVgap(20);
        layout.setHgap(20);
        layout.setPadding(new Insets(20));

        //returns the scene to be able to interact
        return new Scene(layout);
    }

    /**
     * Used to return the structure of the table used within the tabs to explain the activity of the truck
     * and to show the user what the plan is for the truck through out the week
     *
     * @return the structure of the table for the form
     */
    @SuppressWarnings("unchecked") private TableView<ScheduleChartObject> truck_delivery_chart() {

        //variable to hold the new table
        TableView<ScheduleChartObject> truck_delivery_list = new TableView<>();

        //holds activity column
        TableColumn<ScheduleChartObject, String> category = new TableColumn<>("Activity");
        category.setCellValueFactory(new PropertyValueFactory<>("category"));

        //holds current location column
        TableColumn<ScheduleChartObject, String> current_loc = new TableColumn<>("Current Location");
        current_loc.setCellValueFactory(new PropertyValueFactory<>("current_loc"));

        //holds target location column
        TableColumn<ScheduleChartObject, String> target_loc = new TableColumn<>("Target Location");
        target_loc.setCellValueFactory(new PropertyValueFactory<>("target_loc"));

        //holds the first data column
        TableColumn<ScheduleChartObject, String> data_group_1 = new TableColumn<>("Info p1");
        data_group_1.setCellValueFactory(new PropertyValueFactory<>("data_group_1"));

        //holds the second data column
        TableColumn<ScheduleChartObject, String> data_group_2 = new TableColumn<>("Info p2");
        data_group_2.setCellValueFactory(new PropertyValueFactory<>("data_group_2"));

        //holds the date of the activity
        TableColumn<ScheduleChartObject, LocalDate> time = new TableColumn<>("Date");
        time.setCellValueFactory(new PropertyValueFactory<>("time"));

        //holds the minutes spent doing the process
        TableColumn<ScheduleChartObject, String> minutes_spent = new TableColumn<>("Minutes Spent");
        minutes_spent.setCellValueFactory(new PropertyValueFactory<>("minutes"));

        //adds all of the columns to the final table view
        //noinspection unchecked
        truck_delivery_list.getColumns().addAll(category, current_loc, target_loc, data_group_1, data_group_2,
                time, minutes_spent);

        //return the table view
        return truck_delivery_list;
    }

    /**
     * @return method database access through the variable returned
     */
    public DatabaseAccess getDba() {
        return dba;
    }

    /**
     * @return the object representing the trucks current location according to the order of the schedule
     */
    public Object getCurrent_loc() {
        return current_loc;
    }

    /**
     * @param current_loc the param is set to be the new current location
     */
    public void setCurrent_loc(Object current_loc) {
        this.current_loc = current_loc;
    }

    /**
     * @return the list of delivery lists associated with the form
     */
    public ArrayList<DeliveryList> getDeliveryListList() {
        return deliveryListList;
    }

    /**
     * Updates the values of the labels within the form to be accurate with the new values added to the
     * schedule based on the associated order
     *
     * @param n_1 the node for the tab selected
     * @param truckName the name of the truck being scheduled
     * @param d the active delivery list
     */
    private void pickupStatUpdate(Node n_1, String truckName, DeliveryList d) {

        if(d.getDeliveryList().get(d.getDeliveryList().size() - 1) instanceof Pickup) {
            //return the label for the associated cube label of the truck
            Label cube_label = ((Label) n_1.lookup("#cube" + truckName));

            //sets the value to be updated to reflect the newest value of the delivery list being added on
            cube_label.setText(String.valueOf(Double.parseDouble(
                    cube_label.getText()) + ((Pickup) d.getDeliveryList().get(
                    d.getDeliveryList().size() - 1)).getOrder().getCubes()));

            //return the label for the associated weight label of the truck
            Label weight_label = ((Label) n_1.lookup("#lbs" + truckName));

            //sets the value to be updated to reflect the newest value of the delivery list being added on
            weight_label.setText(String.valueOf(Double.parseDouble(
                    weight_label.getText()) + ((Pickup) d.getDeliveryList().get(
                    d.getDeliveryList().size() - 1)).getOrder().getWeight()));

            //return the label for the associated piece label of the truck
            Label piece_label = ((Label) n_1.lookup("#piece" + truckName));

            //sets the value to be updated to reflect the newest value of the delivery list being added on
            piece_label.setText(String.valueOf(Double.parseDouble(
                    piece_label.getText()) + ((Pickup) d.getDeliveryList().get(
                    d.getDeliveryList().size() - 1)).getOrder().getPieces()));

            //return the label for the associated merch label of the truck
            Label merch_label = ((Label) n_1.lookup("#merch" + truckName));

            //sets the value to be updated to reflect the newest value of the delivery list being added on
            merch_label.setText(String.valueOf(Double.parseDouble(
                    merch_label.getText()) + ((Pickup) d.getDeliveryList().get(
                    d.getDeliveryList().size() - 1)).getOrder().getMerch_price()));

            //return the label for the associated cube freight of the truck
            Label freight_label = ((Label) n_1.lookup("#freight" + truckName));

            //gets the order associated with the most recent pick up (order being added to the stats)
            Order o = ((Pickup) d.getDeliveryList().get(d.getDeliveryList().size() - 1)).getOrder();

            //checks the rate of the company and set the amount to the money pricing structure
            double charge_amt = Math.max(
                    (o.getMerch_price() * o.getRate().getRate()), o.getRate().getMinimum());

            //sets the label of the freight value to match the value found added on
            freight_label.setText(String.valueOf(Double.parseDouble(
                    freight_label.getText()) + charge_amt));
        }
    }

    /**
     * Updates the values of the labels within the form to be accurate with the new values added to the
     * schedule based on the associated order
     *
     * @param n_1 the node for the tab selected
     * @param truckName the name of the truck being scheduled
     * @param d the active delivery list
     */
    private void dropOffStatUpdate(Node n_1, String truckName, DeliveryList d) {

        //return the label for the associated cube label of the truck
        Label cube_label = ((Label) n_1.lookup("#cube"+truckName));

        //sets the value to be updated to reflect the newest value of the delivery list being dropped
        cube_label.setText(String.valueOf(Double.parseDouble(
                cube_label.getText()) - ((DropOff) d.getDeliveryList().get(
                d.getDeliveryList().size() - 1)).getOrder().getCubes()));

        //return the label for the associated weight label of the truck
        Label weight_label = ((Label) n_1.lookup("#lbs"+truckName));

        //sets the value to be updated to reflect the newest value of the delivery list being dropped
        weight_label.setText(String.valueOf(Double.parseDouble(
                weight_label.getText()) - ((DropOff) d.getDeliveryList().get(
                d.getDeliveryList().size() - 1)).getOrder().getWeight()));

        //return the label for the associated piece label of the truck
        Label piece_label = ((Label) n_1.lookup("#piece"+truckName));

        //sets the value to be updated to reflect the newest value of the delivery list being dropped
        piece_label.setText(String.valueOf(Double.parseDouble(
                piece_label.getText()) - ((DropOff) d.getDeliveryList().get(
                d.getDeliveryList().size() - 1)).getOrder().getPieces()));

        //return the label for the associated merch label of the truck
        Label merch_label = ((Label) n_1.lookup("#merch"+truckName));

        //sets the value to be updated to reflect the newest value of the delivery list being dropped
        merch_label.setText(String.valueOf(Double.parseDouble(
                merch_label.getText()) - ((DropOff) d.getDeliveryList().get(
                d.getDeliveryList().size() - 1)).getOrder().getMerch_price()));

        //NOTE: does not subtract the freight value, because the value will end the week with
        //the total amount of freight made for each of the trucks
    }

    /**
     * Updates the values of the labels within the form to be accurate with the new values added to the
     * schedule based on the associated order
     *
     * @param n_1 the node for the tab selected
     * @param truckName the name of the truck being scheduled
     * @param d the active delivery list
     */
    private void travelStatUpdate(Node n_1, String truckName, DeliveryList d) {
        Label miles_label = ((Label) n_1.lookup("#miles"+truckName));

        miles_label.setText(String.valueOf(Double.parseDouble(
                miles_label.getText()) + ((Travel) d.getDeliveryList().get(
                        d.getDeliveryList().size() -1)).getDistance()));

    }

    /**
     * Adds the values from the table of the tab and sets the current values
     * for the stats so that the pre-loaded values are able to accurately be
     * accounted for within the statistics
     *
     * @param t the list of objects to find the stats for
     * @return the list of Objects with the correlating values of cubes weight, pieces, merch, freight and miles
     */
    private Object[] findCurrentStats(ObservableList<ScheduleChartObject> t) {

        //defines the default values for the values being added up
        double cubes = 0, weight = 0, pieces = 0, merch = 0, freight = 0, miles = 0;
        
        //loops through the values of the list of items returned from a table
        for(ScheduleChartObject o: t){

            //goes through the cases of the objects within the delivery list
            switch (o.getCategory()) {

                case "Pick-up": {

                    //gets the order from the object in the chart
                    Order w = o.getOrder();

                    //adds the values from the order to the statistics
                    cubes += w.getCubes();
                    weight += w.getWeight();
                    pieces += w.getPieces();
                    merch += w.getMerch_price();

                    //calculates the freight price
                    freight += w.getMerch_price() * w.getRate().getRate();

                    break;
                }

                case "Drop Off": {

                    //gets the order from the object in the chart
                    Order w = o.getOrder();

                    //subtracts the values from the order to the statistics
                    cubes -= w.getCubes();
                    weight -= w.getWeight();
                    pieces -= w.getPieces();
                    merch -= w.getMerch_price();

                    break;
                }

                case "Travel":

                    //dissects the value of the travel data group 2 and splits the non
                    // number values to get the distance
                    miles += Double.parseDouble((o.getData_group_2().split("m"))[0]);

                    break;
            }
        }

        //returns the array object with the defined values from the method
        return new Object[]{cubes, weight, pieces, merch, freight, miles};
    }

    /**
     * Used to get the delivery list from the values of the chart with the truck included. This is
     * used at the beginning to get the preset values from the database, the values are cycled through
     * and added to the variable so that the updates are able to reflect both the database objects,
     * and the new user created objects
     *
     * @param list_of_objects the list of items from the table to turn to delivery list
     * @param truckName the name of the truck
     * @return the delivery list to represent the data from the table
     */
    private DeliveryList getDL(ArrayList<ScheduleChartObject> list_of_objects, String truckName) {

        //variable to hold the new delivery list
        DeliveryList dl;

        //attempts to set a new delivery list to the matching name in the database
        try {
            dl = new DeliveryList(new ArrayList<>(), getDba().get_truck_based_name(truckName));

        } catch (SQLException throwables) {

            //attempts to check for the list ot see if there is an object to pull the truck from
            if(!list_of_objects.isEmpty()) {

                //attempts to pull the truck data from the first value in the list
                dl = new DeliveryList(new ArrayList<>(), list_of_objects.get(0).getTruck());

            } else {

                //if the list is empty, adds a new empty list, with null truck value
                dl = new DeliveryList(new ArrayList<>(), new Truck());

                //displays the error information to the user if the values cannot be pulled through the truck
                new ErrorWindow(new Stage(), String.valueOf(throwables.getErrorCode()), throwables.getMessage());
            }
        }

        //loops through the list of objects returned from the chart
        for(ScheduleChartObject s: list_of_objects){

            //switch to pick the category of the object to understand which object to add to the delivery list
            switch (s.getCategory()) {
                case "Pick-up":

                    //adds the pickup values with the values found from the table input
                    dl.add_pickup(s.getOrder(), s.getTime(), s.getMinutes());

                    break;

                case "Drop Off":

                    //adds the delivery values with the values found from the table input
                    dl.add_delivery(s.getOrder(), s.getTime(), s.getMinutes());

                    break;

                case "Travel":

                    //attempts to add the travel value to the delivery list with the given values from
                    //the chart object, with the pieces of the string values held parsed to match the
                    //values to add the travel object
                    try {
                        dl.add_travel(getDba().get_destination_object(s.getCurrent_loc()),
                                getDba().get_destination_object(s.getTarget_loc()),
                                s.getData_group_1(), Double.parseDouble(s.getData_group_2().split("m")[0]),
                                s.getTime(), s.getMinutes());

                    } catch (SQLException throwables) {

                        //handles invalid input found within the dl, allows skip of the addition
                        new ErrorWindow(new Stage(), String.valueOf(
                                throwables.getErrorCode()), throwables.getMessage());
                    }

                    break;
            }
        }

        //returns the finished delivery list
        return dl;
    }

    /**
     * @return the current date of schedule so far
     */
    public LocalDate getCurrent_date() {
        return current_date;
    }

    /**
     * @param current_date the date of the value is set to the internal current date
     */
    public void setCurrent_date(LocalDate current_date) {
        this.current_date = current_date;
    }
}
