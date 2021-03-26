package FormFiles;

import FormFiles.AddDatabaseForms.*;
import FormFiles.SelectObjectForms.SelectDriverForm;
import FormFiles.SelectObjectForms.SelectTruckForm;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.SQLException;

/**
 * This application is for the internal use of *Business Company Name* only
 * Any other purpose of use is not allowed and all code, algorithms, and designs are the property
 * of the developer and companies who are receiving the product
 *
 * The purpose of the application is to help facilitate the scheduling and management of the
 * internal working of the logistics for the company. This includes the use of data entry tools for the
 * receipts received, orders received, new drivers, trucks, locations, and rates. There is also tools to
 * help the weekly scheduling of the trucks by adding the activities associated with the operations of
 * the truck including picking up and dropping off of packages and traveling to various associated
 * locations. There is also functionality around the generation of reports for the users of the application
 * which show the information being stored within the database around the different store fronts and
 * distributors with the stats of the different deliveries between the stores and distributors
 *
 * @author Lucas Hynes
 * @version 1.1.0
 * @since 2/20/2021
 */
public class Main extends Application {

    /**
     * Launches the window for the application showing the user the options available to use the
     * application
     *
     * @param args the arguments passed through
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * launches the form for the user to be able to view the window
     *
     * @param primaryStage the stage that is displayed to the user
     */
    @Override
    public void start(Stage primaryStage) {

        //button for user to add an order to the database
        Button addOrderButton = new Button("Add Order");

        //event for the add order button
        EventHandler<MouseEvent> addOrderEvent = e -> {

            //adds instance of the add order form
            AddOrderForm of = new AddOrderForm();

            //new stage to launch the next window
            Stage s = new Stage();

            //attempts to set the scene with the returned form
            try {
                s.setScene(of.form());

            } catch (SQLException throwables) {

                //displays errors encountered with details
                new ErrorWindow(new Stage(), String.valueOf(throwables.getErrorCode()), throwables.getMessage());
            }

            //sets the title for the page and displays
            s.setTitle("Add Order");
            s.showAndWait();
        };

        //applies the event handle to the button
        addOrderButton.addEventHandler(MouseEvent.MOUSE_CLICKED, addOrderEvent);

        //settings for button design
        addOrderButton.setPrefWidth(240);
        addOrderButton.setPrefHeight(40);

        //button to add the receipts to the database
        Button addReceiptButton = new Button("Add Receipt");

        //event for the add receipt button
        EventHandler<MouseEvent> addReceiptEvent = e -> {

            //adds instance of the add receipt form
            AddReceiptForm of = new AddReceiptForm();

            //new stage to launch the next window
            Stage s = new Stage();

            //attempts to set the scene with the returned form
            try {
                s.setScene(of.form());

            } catch (SQLException throwables) {

                //displays errors encountered with details
                new ErrorWindow(new Stage(), String.valueOf(throwables.getErrorCode()), throwables.getMessage());
            }

            //sets the title for the page and displays
            s.setTitle("Add Receipt");
            s.showAndWait();
        };

        //applies the event handle to the button
        addReceiptButton.addEventHandler(MouseEvent.MOUSE_CLICKED, addReceiptEvent);

        //settings for the button design
        addReceiptButton.setPrefWidth(240);
        addReceiptButton.setPrefHeight(40);

        //button to schedule the week and apply to the database
        Button scheduleButton = new Button("Schedule");

        //event for the scheduling of the week
        EventHandler<MouseEvent> scheduleEvent = e -> {
            ScheduleForm of = new ScheduleForm();

            //new stage to launch the next window
            Stage s = new Stage();

            //attempts to set the scene with the returned form
            try {
                s.setScene(of.form());

            } catch (SQLException throwables) {

                //displays errors encountered with details
                new ErrorWindow(new Stage(), String.valueOf(throwables.getErrorCode()), throwables.getMessage());
            }

            //sets the title for the page and displays
            s.setTitle("Schedule");
            s.showAndWait();
        };

        //applies the event handle to the button
        scheduleButton.addEventHandler(MouseEvent.MOUSE_CLICKED, scheduleEvent);

        //settings for the button design
        scheduleButton.setPrefWidth(240);
        scheduleButton.setPrefHeight(40);

        //button to add the location to the database
        Button addLocationButton = new Button("Add Location");

        //event for adding the location
        EventHandler<MouseEvent> addLocationEvent = e -> {

            //class instance for adding a destination to the database
            AddDestinationForm of = new AddDestinationForm();

            //new stage to launch the next window
            Stage s = new Stage();

            //attempts to set the scene with the returned form
            s.setScene(of.form());

            //sets the title for the page and displays
            s.setTitle("Add Location");
            s.showAndWait();
        };

        //applies the event handle to the button
        addLocationButton.addEventHandler(MouseEvent.MOUSE_CLICKED, addLocationEvent);

        //settings for the button design
        addLocationButton.setPrefWidth(240);
        addLocationButton.setPrefHeight(40);

        //button to view and download reports from the database
        Button reportButton = new Button("Reports");

        //event handle for the button
        EventHandler<MouseEvent> reportEvent = e -> {

            //class instance to launch the report form
            ReportForm of = new ReportForm();

            //new stage to launch the next window
            Stage s = new Stage();

            //attempts to set the scene with the returned form
            try {
                s.setScene(of.form());

            } catch (SQLException throwables) {

                //displays errors encountered with details
                new ErrorWindow(new Stage(), String.valueOf(throwables.getErrorCode()), throwables.getMessage());
            }

            //sets the title for the page and displays
            s.setTitle("Reports");
            s.showAndWait();
        };

        //applies the event handle to the button
        reportButton.addEventHandler(MouseEvent.MOUSE_CLICKED, reportEvent);

        //settings for the button design
        reportButton.setPrefWidth(240);
        reportButton.setPrefHeight(40);

        //button to add a driver to the database
        Button addDriverButton = new Button("Edit Drivers");

        //event handle for the button
        EventHandler<MouseEvent> addDriverEvent = e -> {

            //class instance for adding the driver to the database
            SelectDriverForm df = new SelectDriverForm();

            //new stage to launch the next window
            Stage s = new Stage();

            //attempts to set the scene with the returned form
            s.setScene(df.form());

            //sets the title for the page and displays
            s.setTitle("Select Driver");
            s.showAndWait();
        };

        //applies the event handle to the button
        addDriverButton.addEventHandler(MouseEvent.MOUSE_CLICKED, addDriverEvent);

        //settings for the button design
        addDriverButton.setPrefWidth(240);
        addDriverButton.setPrefHeight(40);

        //button to add a truck to the database
        Button selectTruckButton = new Button("Edit Trucks");

        //event handle for the button
        EventHandler<MouseEvent> selectTruckEvent = e -> {

            //class instance to show user the truck form to add truck to database
            SelectTruckForm tf = new SelectTruckForm();

            //new stage to launch the next window
            Stage s = new Stage();

            s.setScene(tf.form());

            //sets the title for the page and displays
            s.setTitle("Select Truck");
            s.showAndWait();
        };

        //applies the event handle to the button
        selectTruckButton.addEventHandler(MouseEvent.MOUSE_CLICKED, selectTruckEvent);

        //setting for the button design
        selectTruckButton.setPrefWidth(240);
        selectTruckButton.setPrefHeight(40);

        //button to add a rate to the database
        Button addRateButton = new Button("Add Rate");

        //event handle for the button
        EventHandler<MouseEvent> addRateEvent = e -> {

            //create instance of the form to view for the user
            AddRateForm rf = new AddRateForm();

            //new stage to launch the next window
            Stage s = new Stage();

            //attempts to set the scene with the returned form
            try {
                s.setScene(rf.form());

            } catch (SQLException throwables) {

                //displays errors encountered with details
                new ErrorWindow(new Stage(), String.valueOf(throwables.getErrorCode()), throwables.getMessage());
            }

            //sets the title for the page and displays
            s.setTitle("Add Rate");
            s.showAndWait();
        };

        //applies the event handle to the button
        addRateButton.addEventHandler(MouseEvent.MOUSE_CLICKED, addRateEvent);

        //setting for the button design
        addRateButton.setPrefWidth(240);
        addRateButton.setPrefHeight(40);

        //grid pane for the layout of the main form
        GridPane gp = new GridPane();

        //adds components to the main form
        gp.add(addOrderButton, 0,0, 1, 1);
        gp.add(addReceiptButton, 1, 0, 1, 1);
        gp.add(scheduleButton, 0, 1, 1, 1);
        gp.add(addLocationButton, 1, 1, 1, 1);
        gp.add(reportButton, 0, 2, 1, 1);
        gp.add(addDriverButton, 1, 2, 1, 1);
        gp.add(selectTruckButton, 0, 3, 1, 1);
        gp.add(addRateButton, 1, 3, 1, 1);

        //sets the spacing and pading for the grid pane
        gp.setVgap(20);
        gp.setHgap(20);
        gp.setPadding(new Insets(20));

        //displays the scene on the stage, setting the correct name
        primaryStage.setScene(new Scene(gp));
        primaryStage.setTitle("Trucking Logistics Application");
        primaryStage.show();
    }
}