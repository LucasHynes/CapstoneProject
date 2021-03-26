package FormFiles.ModifyDatabaseForms;

import java.sql.SQLException;

import Database.DatabaseAccess;
import FormFiles.ErrorWindow;
import Objects.Distributor;
import Objects.StoreFront;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import jdk.internal.org.jline.terminal.MouseEvent.Button;

/**
 * This class is used to facilitate the ability of the user to modify a store or distributor and then add back
 * to the database so be able to use in the process of scheduling
 *
 * @author Lucas Hynes
 * @version 1.0
 * @since 3/18/2021
 */
public class ModifyDestinationForm {

    //used to access the database and method calls within
    private final DatabaseAccess dba = new DatabaseAccess();

    /**
     * This method is returning the layout of the scene for the user to be able to add the destination
     * to the database
     *
     * @return Scene layout and data for the user to add a destination
     */
    public Scene form(Object destination) {
        object_loc o = new object_loc();

        if(destination instanceof StoreFront) {

            StoreFront s = ((StoreFront) destination);

            o.name = s.getName();
            o.address = s.getAddress();
            o.city = s.getCity();
            o.state = s.getState();
            o.longitude = String.valueOf(s.getLongitude());
            o.latitude = String.valueOf(s.getLatitude());
            o.id = s.getStore_front_id();

        } else if (destination instanceof Distributor) {

            Distributor s = ((Distributor) destination);

            o.name = s.getName();
            o.address = s.getAddress();
            o.city = s.getCity();
            o.state = s.getState();
            o.longitude = String.valueOf(s.getLongitude());
            o.latitude = String.valueOf(s.getLatitude());
            o.id = s.getDistributor_id();

        } else {

            o.name = "";
            o.address = "";
            o.city = "";
            o.state ="";
            o.longitude = "";
            o.latitude = "";
            o.id = -1;
        }

        //radio buttons for the user to select which type of destination to enter
        RadioButton sfButton = new RadioButton("Store Front");
        RadioButton diButton = new RadioButton("Distributor");

        //grouping the buttons together to handle the user input function
        ToggleGroup sf_di_group = new ToggleGroup();
        sfButton.setToggleGroup(sf_di_group);
        diButton.setToggleGroup(sf_di_group);

        //labels for the form to inform the user about the values to put into the fields
        Label name_label = new Label("Name: ");
        Label address_label = new Label("Address: ");
        Label city_label = new Label("City: ");
        Label state_label = new Label("State: ");
        Label lat_label = new Label(" Latitude: ");
        Label long_label = new Label("Longitude: ");

        //text fields to hold the values that would be input from the user
        TextField name_field = new TextField(o.name);
        TextField address_field = new TextField(o.address);
        TextField city_field = new TextField(o.city);
        TextField state_field = new TextField(o.state);
        TextField lat_field = new TextField(o.latitude);
        TextField long_field = new TextField(o.longitude);

        //adding the buttons to allow for proper navigation by the user
        Button ok_button = new Button("Ok");
        Button cancel_button = new Button("Cancel");

        //grid pane to set the layout for the scene, laying out the elements
        GridPane layout = new GridPane();

        //adding the various elements of the scene to be laid out
        layout.add(sfButton, 0, 0, 1, 1);
        layout.add(diButton, 1, 0, 1, 1);
        layout.add(name_label, 0, 1, 1, 1);
        layout.add(name_field, 1, 1, 1, 1);
        layout.add(address_label, 0, 2, 1, 1);
        layout.add(address_field, 1, 2, 1, 1);
        layout.add(city_label, 0, 3, 1, 1);
        layout.add(city_field, 1, 3, 1, 1);
        layout.add(state_label, 0, 4, 1, 1);
        layout.add(state_field, 1, 4, 1, 1);
        layout.add(lat_label, 0, 5, 1, 1);
        layout.add(lat_field, 1, 5, 1, 1);
        layout.add(long_label, 0, 6, 1, 1);
        layout.add(long_field, 1, 6, 1, 1);
        layout.add(ok_button, 2, 7, 1, 1);
        layout.add(cancel_button, 1, 7, 1, 1);

        //settings for the spacing of the form
        layout.setHgap(20);
        layout.setVgap(20);
        layout.setPadding(new Insets(20));

        //handles the event for the mouse click of the ok button
        EventHandler<MouseEvent> ok_event = e -> {

            //defines the variables to hold the values collected
            String name, address, city, state;
            double longitude, latitude;

            //checks for proper input, replacing values with null values if found to be empty
            if(name_field.getText().isEmpty()){ name = ""; } else { name = name_field.getText(); }
            if(address_field.getText().isEmpty()){ address = ""; } else { address = address_field.getText(); }
            if(city_field.getText().isEmpty()){ city = ""; } else { city = city_field.getText(); }
            if(state_field.getText().isEmpty()){ state = ""; } else { state = state_field.getText(); }

            //checking for the fields to make sure that the values entered for the longitude and latitude to make sure
            //that they can be handled as Double values within the calculations
            if (long_field.getText().isEmpty()) { longitude = 0.0; } else {

                //checks for the string to see if any alphabetic characters
                if (!long_field.getText().matches("\\D")) {

                    longitude = input_double_check(long_field);
                }
                else { longitude = 0.0; }
            }

            if (lat_field.getText().isEmpty()) { latitude = 0.0; } else {

                //checks for the string to see if any alphabetic characters
                if (!lat_field.getText().matches("\\D")) {

                    latitude = input_double_check(lat_field);
                }
                else { latitude = 0.0; }
            }

            //checking for the radio buttons to see which object should be created using the data above
            if (sf_di_group.getSelectedToggle() == diButton) {

                //attempts to set the values of the user input to be a distributor
                try {

                    getDba().updateDistributor(new Distributor(
                            o.id, name, address, city, state, longitude, latitude));
                }
                catch (SQLException throwables) {

                    //launches error window to display to the user the problems with the input
                    new ErrorWindow(new Stage(), String.valueOf(throwables.getErrorCode()), throwables.getMessage());
                }
            } else if(sf_di_group.getSelectedToggle() == sfButton){

                //attempts to set the values of the user input to be a store front
                try {

                    getDba().updateStoreFront(new StoreFront(
                            o.id, name, address, city, state, longitude, latitude));
                } catch (SQLException throwables) {

                    //launches error window to display to the user the problems with the input
                    new ErrorWindow(new Stage(), String.valueOf(throwables.getErrorCode()), throwables.getMessage());
                }
            }

            //after executing the following, closes the scene
            ((Stage) ok_button.getScene().getWindow()).close();
        };

        //adds the event to the button
        ok_button.addEventHandler(MouseEvent.MOUSE_CLICKED, ok_event);

        //event handler for the cancel button, closing the active window
        EventHandler<MouseEvent> cancel_event = e -> ((Stage) cancel_button.getScene().getWindow()).close();

        //adds the event to the button
        cancel_button.addEventHandler(MouseEvent.MOUSE_CLICKED, cancel_event);

        //returns the layout of the page
        return new Scene(layout);
    }

    /**
     * Allows method access to the database to modify the data within
     *
     * @return variable to access the methods
     */
    public DatabaseAccess getDba() {
        return dba;
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

    private static class object_loc {

        public String name;
        public String address;
        public String city;
        public String state;
        public String latitude;
        public String longitude;
        public int id;
    }
}
