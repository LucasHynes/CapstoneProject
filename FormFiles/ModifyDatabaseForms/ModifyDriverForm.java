package FormFiles.ModifyDatabaseForms;

import Database.DatabaseAccess;
import FormFiles.ErrorWindow;
import Objects.Driver;
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

/**
 * This class is used to modify a driver to the database based on the user's input of the features the database takes into
 * account. The main function is the form() function returning the scene layout and data for a given use.
 *
 * @author Lucas Hynes
 * @version 1.0
 * @since 2/24/2021
 */
public class ModifyDriverForm {

    //variable to hold object method access to the database of the application
    private final DatabaseAccess dba = new DatabaseAccess();

    /**
     * Used to return the window layout for the user to be able to insert the needed data to allow for the addition of
     * the driver within the database
     *
     * @return scene to show the user the ability to add drivers
     */
    public Scene form(Driver d) {

        //label to hold the name signifier
        Label nameLabel = new Label("Name: ");

        //used for user input of the name
        TextField nameField = new TextField(d.getDriver_name());

        //buttons to allow for the user to navigate through the application
        Button okButton = new Button("Ok");
        Button cancelButton = new Button("Cancel");

        //event handler for the ok button
        EventHandler<MouseEvent> ok_event = e -> {

            //attempts to add the user inserted value to the database
            try {

                //checking for the user input null check
                if(!nameLabel.getText().isEmpty()) {

                    d.setDriverName(nameField.getText());

                    //sets the user input to the name
                    getDba().update_driver(d);
                }
                else {

                    //sets the name for the new driver to a default value
                    getDba().set_driver("Unspecified");
                }
            } catch (SQLException throwables) {

                //outputs an error window with the information about the error
                new ErrorWindow(new Stage(), String.valueOf(throwables.getErrorCode()), throwables.getMessage());
            }

            //closes the window after code execution
            ((Stage) okButton.getScene().getWindow()).close();
        };

        //assigns the event to the button
        okButton.addEventHandler(MouseEvent.MOUSE_CLICKED, ok_event);

        //creates the event for the cancel button to close the active window
        EventHandler<MouseEvent> cancelEvent = e -> ((Stage) cancelButton.getScene().getWindow()).close();
        cancelButton.addEventHandler(MouseEvent.MOUSE_CLICKED, cancelEvent);

        //grid pane to organize the layout of the window
        GridPane gp = new GridPane();

        //adding the individual components of the window to the grid pane
        gp.add(nameLabel, 0, 0, 1, 1);
        gp.add(nameField, 1, 0, 2, 1);
        gp.add(cancelButton, 1, 1, 1, 1);
        gp.add(okButton, 2, 1, 1, 1);

        //settings for the layout of the grid pane
        gp.setHgap(20);
        gp.setVgap(20);
        gp.setPadding(new Insets(20));

        //returns the properly laid out and functional scene
        return new Scene(gp);
    }

    /**
     * @return object for method access to being able to change the values within the database
     */
    public DatabaseAccess getDba() {
        return dba;
    }
}
