package FormFiles.ModifyDatabaseForms;

import Database.DatabaseAccess;
import FormFiles.ErrorWindow;
import Objects.Distributor;
import Objects.RateSheet;
import Objects.StoreFront;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.SQLException;

/**
 * Method used to create a scene to display the interface to allow for the addition of rates within the database
 * allowing for the user to modify the rates and set the settings for the rates
 *
 * @author Lucas Hynes
 * @version 1.0.0
 * @since 2/24/2021
 */
public class ModifyRateForm {

    //allows for access of database methods through object
    private final DatabaseAccess dba = new DatabaseAccess();

    /**
     * Method allows for the building and return of a form showing the user the options for the rate
     *
     * @return the scene object with the fields allowing the user to navigate and add a rate to the database
     * @throws SQLException handles invalid data
     */
    public Scene form(RateSheet rs) throws SQLException {

        //combo box for the choices of stores
        ComboBox<String> distributorChoice = new ComboBox<>();
        ComboBox<String> storeFrontChoice = new ComboBox<>();

        //text fields for the details of the rate
        TextField rateField = new TextField(String.valueOf(rs.getRate()));
        TextField minField = new TextField(String.valueOf(rs.getMinimum()));

        //labels to display the information to the user
        Label rateLabel = new Label("Rate: ");
        Label minLabel = new Label("Minimum: ");
        Label distLabel = new Label("Distributor: ");
        Label stfrLabel = new Label("Store Front: ");

        //adds buttons to the window to allow navigation
        Button okButton = new Button("OK");
        Button cancelButton = new Button("Cancel");

        //adds the items of names from the stores retrieved from the database
        distributorChoice.getItems().addAll(getDba().get_distributors_names());
        distributorChoice.getSelectionModel().select(
                getDba().get_distributor_based_id(rs.getDistributor_id()).getName());

        storeFrontChoice.getItems().addAll(getDba().get_store_front_names());
        storeFrontChoice.getSelectionModel().select(
                getDba().get_store_front_based_id(rs.getStore_front_id()).getName());

        //event handle for the key button
        EventHandler<MouseEvent> okEvent = e -> {

            //sets values for the variables to be assigned
            Distributor d;
            StoreFront s;
            double r;
            double m;

            //checks for the numbers to be within the text field
            if(minField.getText().matches("\\D")){

                //parses text to double to the value stored
                m = input_double_check(minField);

            } else {

                //otherwise default value
                m = 100;
            }

            //checks for the numbers to be within the text field
            if(rateField.getText().matches("\\D")) {

                //parses text to double from field (expecting percentage as decimal (i.e. 0.5 instead of 50%))
                r = input_double_check(rateField);

            } else {

                //else default value (10%)
                r = 0.1;
            }

            //attempts to match the values for the stores then adds the rate
            try {

                //finding the matching stores
                d = getDba().get_distributor_based_name(distributorChoice.getValue());
                s = getDba().get_store_front_based_name(storeFrontChoice.getValue());

                rs.setDistributor_id(d.getDistributor_id());
                rs.setStore_front_id(s.getStore_front_id());
                rs.setRate(r);
                rs.setMinimum(m);

                //adds all values found to the database
                getDba().update_rateSheet(rs);

            } catch (SQLException throwables) {

                //handles invalid data displaying window with information
                new ErrorWindow(new Stage(), String.valueOf(throwables.getErrorCode()), throwables.getMessage());
            }

            //closes the active window
            ((Stage) okButton.getScene().getWindow()).close();
        };

        //applies the event to the button
        okButton.addEventHandler(MouseEvent.MOUSE_CLICKED, okEvent);

        //event for cancel button to close the window
        EventHandler<MouseEvent> cancelEvent = e -> ((Stage) cancelButton.getScene().getWindow()).close();

        //applies the event to the button
        cancelButton.addEventHandler(MouseEvent.MOUSE_CLICKED, cancelEvent);

        //grid pane for the layout of the window
        GridPane gp = new GridPane();

        //sets the parts of the scene to a grid pane for the proper layout
        gp.add(distLabel, 0, 0, 1, 1);
        gp.add(stfrLabel, 0, 1, 1,1);
        gp.add(distributorChoice, 1, 0, 1, 1);
        gp.add(storeFrontChoice, 1, 1, 1, 1);
        gp.add(rateLabel, 0, 2, 1, 1);
        gp.add(rateField, 1, 2, 1, 1);
        gp.add(minLabel, 0, 3, 1, 1);
        gp.add(minField, 1, 3, 1, 1);
        gp.add(cancelButton, 1, 4, 1, 1);
        gp.add(okButton, 2, 4, 1, 1);

        //adds the spacing and padding for the grid pane
        gp.setHgap(20);
        gp.setVgap(20);
        gp.setPadding(new Insets(20));

        //returns the final layout of the scene
        return new Scene(gp);
    }

    /**
     * @return method access to database
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
}
