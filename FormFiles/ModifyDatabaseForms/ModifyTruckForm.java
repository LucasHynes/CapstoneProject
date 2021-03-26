package FormFiles.ModifyDatabaseForms;

import Database.DatabaseAccess;
import FormFiles.ErrorWindow;
import Objects.Driver;
import Objects.Truck;
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
import java.util.ArrayList;

/**
 * This class is used to allow the user to add a truck object to the database
 *
 * @author Lucas Hynes
 * @version 1.0.0
 * @since 2/24/2021
 */
public class ModifyTruckForm {

    //allows for the access of the database
    private final DatabaseAccess dba = new DatabaseAccess();

    /**
     * Used to return the scene allowing user data entry to add a truck to the database
     *
     * @return scene for user to add the truck
     */
    public Scene form(Truck t) throws SQLException {

        //label for user help
        Label truckNameLabel = new Label("Truck Name");

        //text field to hold the trucks name
        TextField truckNameField = new TextField(t.getTruck_name());

        //buttons for user navigation
        Button okButton = new Button("OK");
        Button cancelButton = new Button("Cancel");

        //combo box to choose which driver is associated with the truck
        ComboBox<String> driver_associated = new ComboBox<>();

        //the list to hold the names of the drivers
        ArrayList<String> names = new ArrayList<>();

        //loops through the given drivers, finding their names and add them to the list
        for(Driver d: getDba().get_drivers()) { names.add(d.getDriver_name()); }

        //adds all the names to the combo box
        driver_associated.getItems().addAll(names);

        driver_associated.getSelectionModel().select(t.getDriver().getDriver_name());

        //event handler for the ok button
        EventHandler<MouseEvent> okEvent = e -> {

            //checks for the field to check for null value
            if(!truckNameField.getText().isEmpty()) {

                //attempts to add the truck with specified name to the database
                try {

                    t.setDriver(getDba().get_driver_based_id(getDba().get_driver_id(
                            driver_associated.getSelectionModel().getSelectedItem())));

                    t.setTruckName(truckNameField.getText());

                    //set using database object
                    getDba().update_truck(t);

                } catch (SQLException throwables) {

                    //handles and shows the errors encountered
                    new ErrorWindow(new Stage(), String.valueOf(throwables.getErrorCode()), throwables.getMessage());
                }
            }
            else {

                //attempts to add truck with unspecified and therefore default name to the database
                try {

                    t.setDriver(getDba().get_driver_based_id(getDba().get_driver_id(
                            driver_associated.getSelectionModel().getSelectedItem())));

                    t.setTruckName("Unspecified");

                    //set using database object
                    getDba().update_truck(t);

                } catch (SQLException throwables) {

                    //displays errors encountered with details
                    new ErrorWindow(new Stage(), String.valueOf(throwables.getErrorCode()), throwables.getMessage());
                }
            }

            //closes the window after the commit
            ((Stage) okButton.getScene().getWindow()).close();
        };

        //adds the event to the button
        okButton.addEventHandler(MouseEvent.MOUSE_CLICKED, okEvent);

        //event for cancel button to close the active window
        EventHandler<MouseEvent> cancelEvent = e -> ((Stage) cancelButton.getScene().getWindow()).close();

        //sets event to the button
        cancelButton.addEventHandler(MouseEvent.MOUSE_CLICKED, cancelEvent);

        //grid pane for the layout of the application
        GridPane gp = new GridPane();

        //adds the properties of the window to the grid pane
        gp.add(truckNameLabel, 0, 0, 1, 1);
        gp.add(truckNameField, 1, 0, 2, 1);
        gp.add(driver_associated, 0, 1, 2, 1);
        gp.add(cancelButton, 1, 2, 1, 1);
        gp.add(okButton, 2, 2, 1, 1);

        //sets spacing and padding for the grid pane
        gp.setHgap(20);
        gp.setVgap(20);
        gp.setPadding(new Insets(20));

        //returns the completed scene
        return new Scene(gp);
    }

    /**
     * @return method access to the database
     */
    public DatabaseAccess getDba() {
        return dba;
    }
}
