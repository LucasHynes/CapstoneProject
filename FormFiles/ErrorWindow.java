package FormFiles;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The error window to be able to show the user the nature of the error and any information about the error
 *
 * @author Lucas Hynes
 * @version 1.0.0
 * @since 3/1/2021
 */
public class ErrorWindow {

    /**
     * The error window sets the scene and shows the result to the user
     *
     * @param s is the stage for the error window
     * @param errorMessage the message for the top of the error
     * @param errorDetails the details for the user to better understand the error
     */
    public ErrorWindow(Stage s, String errorMessage, String errorDetails) {

        //sets and shows the scene returned through the method
        s.setScene(errorWindow(errorMessage, errorDetails));
        s.getIcons().add(new Image("FormFiles\\ImageFiles\\IconImage\\Carrier3TransportLogo.png"));
        s.show();
        s.toFront();
    }

    /**
     * Used to prepare the scene of the window for the error to be displayed
     * @param errorMessage the message at the top
     * @param errorDetails the details at the bottom
     * @return the scene for the error window
     */
    private Scene errorWindow(String errorMessage, String errorDetails){

        //the button to navigate, closing the window, applied to the event
        Button okButton = new Button("OK");
        EventHandler<MouseEvent> okEvent = e -> ((Stage) okButton.getScene().getWindow()).close();
        okButton.addEventHandler(MouseEvent.MOUSE_CLICKED, okEvent);

        //labels to show the user the message and details
        Label errorLabel = new Label(errorMessage);
        Label errorNotes = new Label(errorDetails);

        //the v box layout for the window to show the information clearly to the user
        VBox layout = new VBox(errorLabel, errorNotes, okButton);

        //sets spacing and padding for the grid
        layout.setSpacing(20);
        layout.setPadding(new Insets(20));

        //returns the scene layout for the window
        return new Scene(layout);
    }
}
