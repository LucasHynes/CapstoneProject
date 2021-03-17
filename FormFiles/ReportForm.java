package FormFiles;

import Database.DatabaseAccess;
import Database.ReportOutput;
import Objects.Distributor;
import Objects.StoreFront;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * Class is used to show the user the information stored within the database, output the information
 * into a local file called UserMadeCSV which will add the results of the request for the user's
 * needed information
 *
 * @author Lucas Hymes
 * @version 1.0.0
 * @since 2/26/2021
 */
public class ReportForm {

    //stores method for the database access
    private final DatabaseAccess dba = new DatabaseAccess();

    /**
     * Used to return a form for the user to be able to request reports from the database based on the selections
     * within the form to output reports that are able to be analyzed further, excepting sql hand written query
     * or selections of combo boxes and check boxes to create a query
     *
     * @return Scene allowing the user to select the options
     * @throws SQLException handles invalid data
     */
    public Scene form() throws SQLException {

        //grid pane to hold the options of the location query based on distributors and store fronts
        GridPane location_layout = location_based();

        //grid pane for the user to manually input query into database
        GridPane userInput = by_hand_query();

        //button for the user to be able to exit the window, with the event applied to the button
        Button exitButton = new Button("Exit");
        EventHandler<MouseEvent> exitEvent = e -> ((Stage) exitButton.getScene().getWindow()).close();
        exitButton.addEventHandler(MouseEvent.MOUSE_CLICKED, exitEvent);

        //layout for the window with the input options stacked on top
        VBox layout = new VBox(userInput, location_layout, exitButton);

        //setting the spacing and pading for the grid pane
        layout.setPadding(new Insets(20));
        layout.setSpacing(10);

        //returns the scene using the final layout
        return new Scene(layout);
    }

    /**
     * @return method access to the database through the returned object
     */
    public DatabaseAccess getDba() {
        return dba;
    }

    /**
     * Used to make a grid pane to allow for the user input of selections to filter through the database and pull the
     * requested information based on the previous data stored in the historical loads table
     *
     * @return the grid pane layout to accept the user input
     * @throws SQLException handles the invalid input of data
     */
    private GridPane location_based() throws SQLException {

        //labels for the user to know the layout of the window and which values to input where
        Label distributor_label = new Label("Distributor: ");
        Label store_front_label = new Label("Store Front: ");
        Label fileLabel = new Label("Filename: ");

        //text field for the user to input a file name for the file output
        TextField fileName = new TextField();

        //combo box to choose the distributor setting values from database
        ComboBox<String> distributor = new ComboBox<>();
        distributor.getItems().addAll(getDba().get_distributors_names());

        //combo box to choose the store front setting values from the database
        ComboBox<String> storeFront = new ComboBox<>();
        storeFront.getItems().addAll(getDba().get_store_front_names());

        //date pickers for the start and end of the report filter

        //check boxes for the different options to include in the report output
        CheckBox order_info = new CheckBox("Order Information");
        CheckBox time_info = new CheckBox("Time Information");
        CheckBox driver_info = new CheckBox("Driver");
        CheckBox truck_info = new CheckBox("Truck");
        CheckBox rate_info = new CheckBox("Rate Information");

        //adds all check boxes to a v boc
        VBox checkbox = new VBox(order_info, time_info, driver_info, truck_info, rate_info);
        checkbox.setSpacing(5);

        //button to submit all entries and to have a csv created with the results
        Button queryButton = new Button("Query");

        //event for the button
        EventHandler<MouseEvent> query_event = e -> {

            //holds instance of the stores
            Distributor d;
            StoreFront s;

            //sets the string values for the query
            String select_list, where_list = "", join_list, from;

            //holds the output to write csv
            ReportOutput ro;

            //holds the list of column names
            ArrayList<String> columnName = new ArrayList<>();

            //checks for empty name in the filename section
            if(fileName.getText().isEmpty()) {

                //creates a new file name using a generated algorithm to make it individual if not named
                ro = new ReportOutput("src/UserMadeCSV/User_Queried_Results_" + LocalDate.now() + "_" +
                        LocalTime.now().getHour() + "_" + LocalTime.now().getMinute() + ".csv");
            }
            else {

                //takes the input from the text field and applies the file name to it
                ro = new ReportOutput("src/UserMadeCSV/" + fileName.getText() + ".csv");
            }

            //Attempts to assign the distributor class to the user's selection
            try {

                //checking to see if the user has made a selection
                if(distributor.getSelectionModel().getSelectedItem() != null) {

                    //gets the object based on the name of the selection
                    d = getDba().get_distributor_based_name(distributor.getSelectionModel().getSelectedItem());

                } else {

                    //creates a default distributor with * as the name to pull all distributors
                    d = new Distributor(
                            -1, "*","", "", "", 0.00, 0.00);
                }

            } catch (SQLException throwables) {

                //default value if error encountered pulling all distributors
                d = new Distributor(-1, "*","", "", "", 0.00, 0.00);
            }

            //Attempts to assign the store front to the user's selection
            try {

                //checking to see if the user has made a selection
                if(storeFront.getSelectionModel().getSelectedItem() != null) {

                    //sets the store front to the name matching in the database
                    s = getDba().get_store_front_based_name(storeFront.getSelectionModel().getSelectedItem());

                } else {

                    //sets default to store front with name being * to pull all values
                    s = new StoreFront(
                            -1, "*","", "", "", 0.00, 0.00);
                }

            } catch (SQLException throwables) {

                //default value if error encountered
                s = new StoreFront(-1, "*","", "", "", 0.00, 0.00);
            }

            //checking for the selection of the distributor
            if(!d.getName().equals("*")) {

                //checking for the selection of the store front
                if(!s.getName().equals("*")) {

                    //adds to the list to specify the non * value to filter
                    where_list = " WHERE fleetdata.delivery_times.Distributor_ID = \"" + d.getDistributor_id() +
                            "\" AND fleetdata.delivery_times.StoreFront_ID = \"" + s.getStore_front_id() + "\"";

                } else {

                    //adds to the list the entered distributor to filter
                    where_list = " WHERE fleetdata.delivery_times.Distributor_ID = \"" + d.getDistributor_id() + "\"";
                }
            } else {

                //adds the store front name entered to filter
                if(!s.getName().equals("*")) {
                    where_list = " WHERE fleetdata.delivery_times.StoreFront_ID = \"" + s.getStore_front_id() + "\"";
                }
            }

            //sets the values for the string to start off with the standard entries in the string
            select_list = "SELECT fleetdata.distributor.Company_Name, fleetdata.store_front.Company_Name, " +
                    "fleetdata.delivery_times.Delivery_ID";
            from = " FROM fleetData.delivery_times";
            join_list = " LEFT JOIN fleetdata.distributor ON fleetdata.delivery_times.Distributor_ID = " +
                    "fleetdata.distributor.Distributor_ID LEFT JOIN fleetdata.store_front ON " +
                    "fleetdata.delivery_times.StoreFront_ID = fleetdata.store_front.StoreFront_ID";

            //adds the default columns to the array list
            columnName.add("Distributor");
            columnName.add("Store_Front");
            columnName.add("Delivery_ID");

            //checks if the user wants to view information on the order associated with each delivery
            if(order_info.isSelected()) {

                //adds to select and join list the correct strings to add that information
                select_list += ", fleetdata.orders.Status, fleetdata.orders.Merch_Price, fleetdata.orders.Cubes, " +
                        "fleetdata.orders.Weight, fleetdata.orders.Pieces, fleetdata.orders.Notes";
                join_list += " LEFT JOIN fleetdata.orders ON fleetdata.delivery_times.Order_ID = " +
                        "fleetdata.orders.Order_ID";

                //adds the column names to the array list
                columnName.add("Order_Status");
                columnName.add("Merch_Price");
                columnName.add("Cubes");
                columnName.add("Weight");
                columnName.add("Pieces");
                columnName.add("Notes");
            }

            //checks if the user wants to view information on the time of the orders
            if(time_info.isSelected()) {

                //adds to select and join list the correct strings to add that information
                select_list += ", fleetdata.delivery_times.Time_Received, " +
                    "fleetdata.delivery_times.Time_Pickup, fleetdata.delivery_times.Time_Drop_off";

                //adds the column names to the array list
                columnName.add("Time_Received");
                columnName.add("Time_Pickup");
                columnName.add("Time_Drop_Off");
            }

            //checks if the user wants to view information on the driver of the orders
            if(driver_info.isSelected()) {

                //adds to select and join list the correct strings to add that information
                select_list += ", fleetdata.drivers.Name";
                join_list += " LEFT JOIN fleetdata.drivers ON fleetdata.delivery_times.Drive_ID = " +
                        "fleetdata.drivers.Driver_ID";

                //adds the column names to the array list
                columnName.add("Driver");
            }

            //checks if the user wants to view information on the truck associated with the orders
            if(truck_info.isSelected()) {

                //adds to select and join list the correct strings to add that information
                select_list += ", fleetdata.truck_fleet.Truck_Name";
                join_list += " LEFT JOIN fleetdata.truck_fleet ON fleetdata.delivery_times.Truck_ID = " +
                        "fleetdata.truck_fleet.Truck_ID";

                //adds the column names to the array list
                columnName.add("Truck");
            }

            //checks if the user wants to view information on the rate of the order
            if(rate_info.isSelected()) {

                //adds to select and join list the correct strings to add that information
                select_list += ", fleetdata.rate_sheet.Rate, fleetdata.rate_sheet.Minimum, " +
                "fleetdata.rate_sheet.Notes";
                join_list += " LEFT JOIN fleetdata.rate_sheet ON fleetdata.delivery_times.Rate_ID = " +
                        "fleetdata.rate_sheet.Rate_ID";

                //adds the column names to the array list
                columnName.add("Rate");
                columnName.add("Min");
                columnName.add("Rate_Notes");
            }

            //attempts tp combine all strings into one single
            String result = select_list + from + join_list + where_list;

            //attempts to set the file data to the results of the query passed through the method and the list of
            //columns created above
            try { ro.setFileData(columnName, getDba().reportQuery(result)); }

            catch (SQLException | IOException throwables) {

                //displays errors encountered with details to describe to user error encountered
                new ErrorWindow(new Stage(), String.valueOf(throwables.toString()), throwables.getMessage());
            }
        };

        //applies the event to the button
        queryButton.addEventHandler(MouseEvent.MOUSE_CLICKED, query_event);

        //grid pane for the layout of the window
        GridPane gp = new GridPane();

        //adds the different elements of the window into the grid pane with the proper layout
        gp.add(distributor_label, 0, 0, 1, 1);
        gp.add(distributor, 1, 0, 1, 1);
        gp.add(store_front_label, 0, 1, 1, 1);
        gp.add(storeFront, 1, 1, 1, 1);
        gp.add(checkbox, 2, 0, 1, 4);
        gp.add(fileLabel, 0, 4, 1, 1);
        gp.add(fileName, 1, 4, 1, 1);
        gp.add(queryButton, 3, 5, 1, 1);

        //sets the padding and spacing for the window settings
        gp.setVgap(20);
        gp.setHgap(20);
        gp.setPadding(new Insets(20));

        //returns the structured grid pane
        return gp;
    }

    /**
     * Used to make a grid pane to allow for the user input of the query hand written to then pull the results of the
     * query into a locally saved csv file
     *
     * @return the grid pane structure for the by hand query
     */
    private GridPane by_hand_query() {

        //text field to hold the input query
        TextField queryField = new TextField();

        //button for the user to press to execute the query
        Button queryButton = new Button("Query");

        //event for the query button
        EventHandler<MouseEvent> query_event = e -> {

            //checks to make sure that the value in the field is not empty
            if(!queryField.getText().isEmpty()) {

                //attempts to enter the query into the database
                try {
                    getDba().reportQuery(queryField.getText());

                } catch (SQLException throwables) {

                    //displays errors encountered with details
                    new ErrorWindow(new Stage(), String.valueOf(throwables.getErrorCode()), throwables.getMessage());
                }
            }
        };

        //applies the event to the button
        queryButton.addEventHandler(MouseEvent.MOUSE_CLICKED, query_event);

        //grid pane for the layout of the user entered query
        GridPane gp = new GridPane();

        //adds the elements to the grid pane
        gp.add(queryField, 0, 0, 2, 1);
        gp.add(queryButton, 2, 0, 1, 1);

        //adds spacing and padding to the grid pane
        gp.setHgap(20);
        gp.setVgap(20);
        gp.setPadding(new Insets(20));

        //returns the grid pane
        return gp;
    }
}
