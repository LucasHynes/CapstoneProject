package FormFiles.AddDatabaseForms;

import Database.DatabaseAccess;
import FormFiles.ErrorWindow;
import Objects.Driver;
import Objects.Receipt;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Class used to add receipts to the database through the selection of the user and the use of the input fields
 * used within the scene to control data entries
 *
 * @author Lucas Hynes
 * @version 1.0.0
 * @since 2/24/2021
 */
public class AddReceiptForm {

    //declare method access to database
    private final DatabaseAccess dba = new DatabaseAccess();

    /**
     * Used to return the proper form and information for the scene that would allow the user to add receipts
     *
     * @return scene to display interface for the data entry
     * @throws SQLException handles invalid data
     */
    public Scene form() throws SQLException {

        //labels to display the information to the user around data entry
        Label amount_label = new Label("Amount:");
        Label card_no_label = new Label("Card No:");
        Label city_label = new Label("City:");
        Label state_label = new Label("State:");
        Label zip_label = new Label("Zip Code:");

        //combo box for the driver
        ComboBox<String> driver_choice = new ComboBox<>();

        //adds the names for all the drivers
        driver_choice.getItems().addAll(get_driver_names());
        driver_choice.setEditable(true);
        driver_choice.setPromptText("Driver");

        //adds the combo box to retrieve the companies for the receipts
        ComboBox<String> company_choice = new ComboBox<>();
        company_choice.getItems().addAll(getDba().get_receipt_companies());
        company_choice.setEditable(true);
        company_choice.setPromptText("Company");

        //holds the money amount
        TextField amount_field = new TextField();
        amount_field.setPromptText("No need for the $");

        //holds the card no used
        TextField card_no_field = new TextField();
        card_no_field.setPromptText("XXXX");

        //date for the receipt
        DatePicker datePicker = new DatePicker();

        //text fields for the location associated with the receipt
        TextField city_field = new TextField();
        TextField state_field = new TextField();
        TextField zip_field = new TextField();

        //combo box to select the category of receipt
        ComboBox<String> category_choice = new ComboBox<>();
        category_choice.getItems().addAll(getDba().get_receipt_categories());
        category_choice.setEditable(true);
        category_choice.setPromptText("Category");

        //defines the save button
        Button save_button = new Button("Save");

        //event handles save button
        EventHandler<MouseEvent> save_event = e -> {

            //attempts to declare new receipt object with the values from the window entered
            Receipt r = new Receipt(-1, getDba().get_driver_id(driver_choice.getValue()),
                    company_choice.getValue(), input_double_check(amount_field),
                    input_int_check(card_no_field), datePicker.getValue(), city_field.getText(),
                    state_field.getText(), input_int_check(zip_field), category_choice.getValue());

            //attempts to add the receipt to the database
            try {

                //method call to add receipt
                getDba().add_receipt(r);

            } catch (SQLException throwables) {

                //outputs an error window with the information about the error
                new ErrorWindow(new Stage(), String.valueOf(throwables.getErrorCode()), throwables.getMessage());
            }

            //call to close the active window
            ((Stage) save_button.getScene().getWindow()).close();
        };

        //adds the event to the button
        save_button.addEventHandler(MouseEvent.MOUSE_CLICKED, save_event);

        //defines the cancel button
        Button cancel_button = new Button("Cancel");

        //defines event for the cancel button to close the active window
        EventHandler<MouseEvent> cancel_event = e -> ((Stage) cancel_button.getScene().getWindow()).close();

        //applies the event to the button
        cancel_button.addEventHandler(MouseEvent.MOUSE_CLICKED, cancel_event);

        //defines the next button, similar to the save button but allows for the user to continue and add another
        //receipt to the database
        Button next_button = new Button("Next");
        EventHandler<MouseEvent> next_event = e -> {

            //attempts to declare new receipt object with the values from the window entered
            Receipt r = new Receipt(-1, getDba().get_driver_id(driver_choice.getValue()),
                    company_choice.getValue(), Double.parseDouble(amount_field.getText()),
                    Integer.parseInt(card_no_field.getText()), datePicker.getValue(), city_field.getText(),
                    state_field.getText(), Integer.parseInt(zip_field.getText()),
                    category_choice.getValue());

            //attempts to add the receipt to the database
            try {

                //method call to add receipt
                getDba().add_receipt(r);

            } catch (SQLException throwables) {

                //outputs an error window with the information about the error
                new ErrorWindow(new Stage(), String.valueOf(throwables.getErrorCode()), throwables.getMessage());
            }

            //addition to the save event to then launch the window again to input the next receipt
            Stage s = new Stage();

            //instance for the new form
            AddReceiptForm rf = new AddReceiptForm();

            //attempts to launch the new form
            try {

                //setting scene
                s.setScene(rf.form());

            } catch (SQLException throwables) {

                //handles invalid errors in sql displaying error information to the user
                new ErrorWindow(new Stage(), String.valueOf(throwables.getErrorCode()), throwables.getMessage());
            }

            //displays the new window
            s.show();

            //after code execution, close active window leaving only the new window, setting the loop for the user
            ((Stage) next_button.getScene().getWindow()).close();
        };

        //applies the event to the button
        next_button.addEventHandler(MouseEvent.MOUSE_CLICKED, next_event);

        //stacks the elements of the buttons to be grouped appropriately
        HBox button_group = new HBox(cancel_button, save_button, next_button);

        //sets proper spacing for buttons
        button_group.setSpacing(5);

        //sets grid pane for the window
        GridPane data_entry_layout = new GridPane();

        //applies window elements to the grid pane to have correct layout
        data_entry_layout.add(amount_label, 0, 0, 1, 1);
        data_entry_layout.add(card_no_label, 0, 1, 1, 1);
        data_entry_layout.add(city_label, 0, 2, 1, 1);
        data_entry_layout.add(state_label, 0, 3, 1, 1);
        data_entry_layout.add(zip_label, 0, 4, 1, 1);
        data_entry_layout.add(amount_field, 1, 0, 1, 1);
        data_entry_layout.add(card_no_field, 1, 1, 1, 1);
        data_entry_layout.add(city_field, 1, 2, 1, 1);
        data_entry_layout.add(state_field, 1, 3, 1, 1);
        data_entry_layout.add(zip_field, 1, 4, 1, 1);
        data_entry_layout.add(driver_choice, 2, 0, 1, 1);
        data_entry_layout.add(company_choice, 2, 1, 1, 1);
        data_entry_layout.add(category_choice, 2, 2, 1, 1);
        data_entry_layout.add(datePicker, 2, 3, 1, 1);
        data_entry_layout.add(button_group, 2, 5, 2, 1);

        //sets the spacing and padding for the grid pane for correct display settings
        data_entry_layout.setVgap(20);
        data_entry_layout.setHgap(20);
        data_entry_layout.setPadding(new Insets(20));

        //returns the complete scene to be able to launch the scene
        return new Scene(data_entry_layout);
    }

    /**
     * @return method access to database retrieval and setting
     */
    public DatabaseAccess getDba() {
        return dba;
    }

    /**
     * Works to return all driver names stored within the database
     *
     * @return list string of the driver names stored within the database
     * @throws SQLException handles invalid data
     */
    private ArrayList<String> get_driver_names() throws SQLException {

        //gets all available driver objects from database
        ArrayList<Driver> drivers = getDba().get_drivers();

        //empty list to hold names
        ArrayList<String> driver_names = new ArrayList<>();

        //loop through the drivers adding names to the list
        for (Driver d: drivers) { driver_names.add(d.getDriver_name()); }

        //return list of names
        return driver_names;
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


    /**
     * Checks the values in the field to make sure that they are Doubles and are able to be parsed
     *
     * @param t text field to check
     * @return the double value if available, otherwise 0
     */
    private double input_double_check(TextField t) {
        if(t.getText().isEmpty()) {
            return 0.0;
        } else {
            try {
                return Double.parseDouble(t.getText());

            } catch (NumberFormatException e1) {
                return 0.0;
            }
        }
    }
}
