package FormFiles.AddDatabaseForms;

import Database.DatabaseAccess;
import FormFiles.ErrorWindow;
import Objects.Distributor;
import Objects.Order;
import Objects.RateSheet;
import Objects.StoreFront;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * This class is used to be able to add a user specified order to the database through the navigation of the
 * application
 *
 * @author Lucas Hynes
 * @version 1.1.0
 * @since 2/24/2021
 */
public class AddOrderForm {

    //holds object access to method calls to edit the database
    private final DatabaseAccess dba = new DatabaseAccess();

    //holds the count of rows within the window
    private int counter = 1;

    //layout for the scene
    private GridPane pane;

    /**
     * This method returns the window for the user to be able to add an order or multiple orders to the database
     * based on the distributor and assigning new orders based on different store fronts, with the window being
     * able to grow if there are too many orders being placed at one time to fit within the window
     *
     * @return the scene to allow the user to navigate the application
     * @throws SQLException handles invalid data
     */
    public Scene form() throws SQLException {

        //sets the label to explain the distributor
        Label distributor_label = new Label("Distributor");

        //allows the user to select a date for the order to be made on
        DatePicker date = new DatePicker();

        //labels to explain stats to user
        Label cube_label = new Label("Cubes:");
        Label weight_label = new Label("Weight:");
        Label piece_label = new Label("Pieces:");
        Label merch_label = new Label("Merch Price:");

        //data to update the user on the totals of the order
        Label cube_amount = new Label("0.00");
        Label weight_amount = new Label("0.00");
        Label piece_amount = new Label("0.00");
        Label merch_price = new Label("0.00");

        //box for user to select the distributor associated with the order
        ComboBox<String> company_choice = new ComboBox<>();
        company_choice.getItems().addAll(getDba().get_distributors_names());
        company_choice.setEditable(true);
        company_choice.setPromptText("Distributor");

        //labels to explain the columns to be input by the user.
        Label storeFront_col_label = new Label("Store Front");
        Label primary_col_label = new Label("ID#");
        Label cubes_col_label = new Label("Cube");
        Label weight_col_label = new Label("Weight");
        Label pieces_col_label = new Label("Pieces");
        Label merch_price_col_label = new Label("Merch Price");

        //scroll pane for the order input to allow for better navigation of the application
        ScrollPane pane = new ScrollPane();

        //setting for the width of the pane
        pane.setPrefWidth(1000);

        //grid pane to hold the layout of the window
        GridPane chart = new GridPane();

        //adds the components of the window to the grid
        chart.add(storeFront_col_label, 0, 0 ,1, 1);
        chart.add(primary_col_label, 1, 0, 1, 1);
        chart.add(cubes_col_label, 2, 0, 1, 1);
        chart.add(weight_col_label, 3, 0, 1, 1);
        chart.add(pieces_col_label, 4, 0, 1, 1);
        chart.add(merch_price_col_label, 5, 0, 1, 1);

        //sets the spacing for the grid pane
        chart.setHgap(10);
        chart.setVgap(10);
        chart.setPadding(new Insets(20));

        //sets the chart to the scroll pane so that it is contained properly
        setPane(chart);
        pane.setContent(getPane());
        pane.setFitToWidth(true);

        //attempts to add a row to the window for the first order input
        try {

            //adds the row to the pane
            setPane(add_row(getPane()));

        } catch (SQLException throwables) {

            //adds error window with the error message
            new ErrorWindow(new Stage(), String.valueOf(throwables.getErrorCode()), throwables.getMessage());
        }

        //button to allow the user to add another store to the order set if the distributor has orders from
        //multiple stores
        Button add_store = new Button("Add Another Store");

        //Event handler for the add store button
        EventHandler<MouseEvent> add_store_event = e -> {

            //updates the statistics at the bottom of the page, with the new numbers in the window
            cube_amount.setText(get_cubes(getPane(), 2, cube_amount).getText());
            weight_amount.setText(get_cubes(getPane(), 3, weight_amount).getText());
            piece_amount.setText(get_cubes(getPane(), 4, piece_amount).getText());
            merch_price.setText(get_cubes(getPane(), 5, merch_price).getText());

            //attempts to add another order to the window through the insert of a new row in the grid pane
            try {

                //adds the row to pane
                setPane(add_row(getPane()));

            } catch (SQLException throwables) {

                //adds the error window with explanation of error
                new ErrorWindow(new Stage(), String.valueOf(throwables.getErrorCode()), throwables.getMessage());
            }
        };

        //applies the events to the button
        add_store.addEventHandler(MouseEvent.MOUSE_CLICKED, add_store_event);

        //defines the save button for the window
        Button save_button = new Button("Save");

        //defines the event for the button
        EventHandler<MouseEvent> save_event = event -> {

            //attempts to add all orders to the database
            try {

                //method call to add the orders
                commit(company_choice.getValue(), date.getValue());

            } catch (SQLException throwables) {

                //displays error window with the details of the error
                new ErrorWindow(new Stage(), String.valueOf(throwables.getErrorCode()), throwables.getMessage());
            }

            ((Stage) save_button.getScene().getWindow()).close();
        };

        //applies the event to the button
        save_button.addEventHandler(MouseEvent.MOUSE_CLICKED, save_event);

        //defines the cancel button
        Button cancel_button = new Button("Cancel");

        //defines the event of the cancel button to close window
        EventHandler<MouseEvent> cancel_event = e -> ((Stage) cancel_button.getScene().getWindow()).close();

        //adds the event to the button
        cancel_button.addEventHandler(MouseEvent.MOUSE_CLICKED, cancel_event);

        //sets the grid for the window
        GridPane layout = new GridPane();

        //adds the various parts of the window to the grid pane
        layout.add(distributor_label, 0, 0, 1, 1);
        layout.add(company_choice, 1, 0, 1, 1);
        layout.add(date, 0, 1, 1, 1);
        layout.add(cube_label, 0, 2, 1, 1);
        layout.add(weight_label, 0, 3, 1, 1);
        layout.add(piece_label, 0, 4, 1, 1);
        layout.add(merch_label, 0, 5, 1, 1);
        layout.add(cube_amount, 1, 2, 1, 1);
        layout.add(weight_amount, 1, 3, 1 ,1);
        layout.add(piece_amount, 1, 4, 1, 1);
        layout.add(merch_price, 1, 5, 1, 1);
        layout.add(pane, 2, 1, 5, 6);
        layout.add(add_store, 5, 7, 1, 1);
        layout.add(cancel_button, 5, 8, 1, 1);
        layout.add(save_button, 6, 8, 1, 1);

        //sets the spacing and padding for the elements in the window
        layout.setVgap(20);
        layout.setHgap(20);
        layout.setPadding(new Insets(20));
        layout.setPrefWidth(1500);

        //sets the alignment for the window
        layout.setAlignment(Pos.CENTER_RIGHT);

        //returns the final scene of the window
        return new Scene(layout);
    }

    /**
     * @return database method access through object returned
     */
    public DatabaseAccess getDba() {
        return dba;
    }

    /**
     * Takes the input grid pane and adds another row of matching elements to the grid pane to allow for the user
     * to repeat the data entering steps for the stores and their different orders to be able to spend the time used
     * in a more effective manner
     *
     * @param pane the grid pane to add the elements to
     * @return the new version of the grid pane
     * @throws SQLException handles invalid data
     */
    public GridPane add_row(GridPane pane) throws SQLException {

        //defines the combo box to have the user select the correct store front
        ComboBox<String> store_select = new ComboBox<>();

        //settings for the combo box
        store_select.setEditable(true);

        //accessing the database to get all store front names
        store_select.getItems().addAll(getDba().get_store_front_names());

        //defines the text fields for the user to be able to input the information
        TextField id_num = new TextField();
        TextField cubes = new TextField();
        TextField weight = new TextField();
        TextField pieces = new TextField();
        TextField merch_price = new TextField();

        //gets the number of rows currently in the grid pane
        int row_index = getCounter();

        //adds the new elements to the pane
        pane.add(store_select, 0, row_index, 1, 1);
        pane.add(id_num, 1, row_index, 1, 1);
        pane.add(cubes, 2, row_index, 1, 1);
        pane.add(weight, 3, row_index, 1, 1);
        pane.add(pieces, 4, row_index, 1, 1);
        pane.add(merch_price, 5, row_index, 1, 1);

        //sets the counter to be increased to keep the appropriate count of rows
        setCounter(row_index+1);

        //returns the new pane
        return pane;
    }

    /**
     * @return the number of orders being applied to the form +1 for the column header in the count
     */
    public int getCounter() {
        return counter;
    }

    /**
     * @param counter sets the count number to the param passed (int)
     */
    public void setCounter(int counter) {
        this.counter = counter;
    }

    /**
     * @return the grid pane for the rows of orders (NOT MAIN LAYOUT PANE)
     */
    public GridPane getPane() {
        return pane;
    }

    /**
     * @param pane the grid pane for the rows of orders (NOT MAIN LAYOUT PANE)
     */
    public void setPane(GridPane pane) {
        this.pane = pane;
    }

    /**
     * Able to calculate the new value of the stats at the bottom of the page for the new numbers based on the
     * input of the user
     *
     * @param pane the grid pane for the rows of orders (NOT MAIN LAYOUT PANE)
     * @param col the column of the state being calculated
     * @param amount_field the field to update
     * @return the new label (to replace the passed in amount_field)
     */
    public Label get_cubes(GridPane pane, int col, Label amount_field) {

        //checking for empty label (default is 0.00 so lack of else call for error protect & future bugs
        if(!amount_field.getText().equals("")){

            //loops through the different elements of the grid pane
            for (Node o : pane.getChildren()) {

                //checking for the right column and then the correct row to calculate to only get the new amount
                //from the grid pane
                if((GridPane.getColumnIndex(o) == col) && (GridPane.getRowIndex(o) == getCounter() - 1)) {

                    //checks for the text field to make sure it is not a null entry
                    if (!((TextField) o).getText().equals("")) {

                        //holds the count of the amount field and the new value in the grid pane
                        double count = Double.parseDouble(amount_field.getText()) +
                                input_double_check((TextField) o);

                        //round the count to display
                        count = round(count, 2);

                        //sets the value of the field to the found value calculated
                        amount_field.setText(String.valueOf(count));
                    }
                }
            }

            //returns the new edited label
            return amount_field;
        }

        //if the amount field is empty just return the field unchanged
        return amount_field;
    }


    /**
     * Used to round the results of the distance calculation to return a better looking result for the graph
     *
     * @param value value to truncate
     * @param places the places to truncate till
     * @return the result of the rounding calculation
     */
    public double round(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }

    /**
     * used to apply the found order values to the database committing the data input
     *
     * @param dist_name holds the name of the distributor
     * @throws SQLException handles invalid data
     */
    @SuppressWarnings("unchecked") private void commit(String dist_name, LocalDate date) throws SQLException {

        //pane of the orders being entered
        GridPane pane = getPane();

        //will hold the created orders
        ArrayList<Order> orders = new ArrayList<>();

        Distributor d = getDba().get_distributor_based_name(dist_name);

        //loop to add null value orders to be edited
        for(int i = 0; i < getCounter() - 1; i++) {
            orders.add(new Order(-1, "", "", d,
                    new StoreFront(),new RateSheet(), -1, -1, -1, -1, ""));

            orders.get(orders.size()-1).setReceiveDate(date);
        }

        //goes through the nodes of the grid pane
        for(Node n: pane.getChildren()){

            //checking to make sure it is not the labels of the row being checked
            if(GridPane.getRowIndex(n) != 0) {

                //gets the corresponding order
                Order o = orders.get(GridPane.getRowIndex(n) - 1);

                //switched based on the column index to check which stat is held
                switch (GridPane.getColumnIndex(n)) {

                    //store front combo box
                    case 0:

                        //noinspection unchecked
                        o.setStore_front(getDba().get_store_front_based_name(((ComboBox<String>) n).getValue()));

                        break;

                    //text field for primary key
                    case 1:

                        //checking for empty value
                        if(!((TextField) n).getText().isEmpty()) {

                            //sets the value to the key field
                            o.setPrimary_distributor_key(((TextField) n).getText());
                        }

                        break;

                    //text field for cubes
                    case 2:

                        //checking for empty values
                        if(!((TextField) n).getText().isEmpty()) {

                            //sets the cube values
                            o.setCubes(input_double_check((TextField) n));
                        }

                        break;

                    //text field for weight
                    case 3:

                        //checking for empty values
                        if(!((TextField) n).getText().isEmpty()) {

                            //sets the weight value
                            o.setWeight(input_double_check((TextField) n));
                        }

                        break;

                    //text field for pieces
                    case 4:

                        //checking for empty values
                        if(!((TextField) n).getText().isEmpty()) {

                            //sets the pieces value
                            o.setPieces(input_double_check((TextField) n));
                        }

                        break;

                    //text field for merch price
                    case 5:

                        //checking for empty values
                        if(!((TextField) n).getText().isEmpty()) {

                            //sets the merch price value
                            o.setMerch_price(input_double_check((TextField) n));
                        }

                        break;
                }
            }
        }

        //loops through the orders after the edits made
        for(Order o: orders){

            //checking for null values before applying
            if((o.getStore_front() != null) && (o.getDistributor() != null)) {

                //sets the rate through the distributor and store front ids'
                o.setRate(getDba().findRate(o.getDistributor().getDistributor_id(),
                        o.getStore_front().getStore_front_id()));

                //applies the order to the database
                getDba().add_order(o);
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
