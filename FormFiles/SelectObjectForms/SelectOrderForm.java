package FormFiles.SelectObjectForms;

import java.sql.SQLException;

import Database.DatabaseAccess;
import FormFiles.ErrorWindow;
import FormFiles.AddDatabaseForms.AddOrderForm;
import FormFiles.ModifyDatabaseForms.ModifyOrderForm;
import Objects.Order;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class SelectOrderForm {

    private final DatabaseAccess dba = new DatabaseAccess();

    @SuppressWarnings("unchecked") public Scene form() {
        //table view variable to return
        TableView<Order> order_chart = new TableView<>();

        //defines the column for the distributor
        TableColumn<Order, String> distributor = new TableColumn<>("Distributor");
        distributor.setCellValueFactory(new PropertyValueFactory<>("distributor_name"));

        //defines the column for the store front
        TableColumn<Order, String> store_front = new TableColumn<>("Store Front");
        store_front.setCellValueFactory(new PropertyValueFactory<>("store_front_name"));

        //defines the column for the cubes
        TableColumn<Order, Double> cubes = new TableColumn<>("Cubes");
        cubes.setCellValueFactory(new PropertyValueFactory<>("cubes"));

        //defines the column for the weight
        TableColumn<Order, Double> weight = new TableColumn<>("Weight");
        weight.setCellValueFactory(new PropertyValueFactory<>("weight"));

        //defines the column for the pieces
        TableColumn<Order, Double> pieces = new TableColumn<>("Pieces");
        pieces.setCellValueFactory(new PropertyValueFactory<>("pieces"));

        //defines the column for the merch price
        TableColumn<Order, Double> merch_price = new TableColumn<>("Merchandise Price");
        merch_price.setCellValueFactory(new PropertyValueFactory<>("merch_price"));

        //adds all columns to the chart
        //noinspection unchecked
        order_chart.getColumns().addAll(distributor, store_front, cubes, weight, pieces, merch_price);
 
        try {
            order_chart.getItems().addAll(getDba().get_all_orders());
        } catch (SQLException e) {
            
            //launches error window to display to the user the problems with the input
            new ErrorWindow(new Stage(), e.getMessage(), e.getLocalizedMessage());
        }

        Button modifyButton = new Button("Modify Order");
        
        EventHandler<MouseEvent> modifyEvent = e -> {
            if(order_chart.getSelectionModel().getSelectedItem() != null) {
                
                Order t = order_chart.getSelectionModel().getSelectedItem();

                //launch form to modify selected truck
                ModifyOrderForm mof = new ModifyOrderForm();
                
                try {
                    Stage s = new Stage();
                    s.setScene(mof.form(t));
                    s.show();

                } catch (SQLException e1) {
                    
                    //launches error window to display to the user the problems with the input
                    new ErrorWindow(new Stage(), e1.getMessage(), e1.getLocalizedMessage());
                }
            }
            
            //closes active window
            ((Stage) modifyButton.getScene().getWindow()).close();
        };

        modifyButton.addEventHandler(MouseEvent.MOUSE_CLICKED, modifyEvent);

        Button addOrderButton = new Button("Add Order(s)");

        EventHandler<MouseEvent> addOrderEvent = e -> {
            //launch form to modify selected Order
            AddOrderForm mtf = new AddOrderForm();
                
            try {
                Stage s = new Stage();
                s.setScene(mtf.form());
                s.show();

            } catch (SQLException e1) {
                
                //launches error window to display to the user the problems with the input
                new ErrorWindow(new Stage(), e1.getMessage(), e1.getLocalizedMessage());
            }

            ((Stage) addOrderButton.getScene().getWindow()).close();
        };

        addOrderButton.addEventHandler(MouseEvent.MOUSE_CLICKED, addOrderEvent);
        
        Button exitButton = new Button("Exit");
        
        EventHandler<MouseEvent> exitEvent = e -> ((Stage) exitButton.getScene().getWindow()).close();
    
        exitButton.addEventHandler(MouseEvent.MOUSE_CLICKED, exitEvent);

        GridPane gp = new GridPane();

        gp.add(order_chart, 0, 0, 3, 3);
        gp.add(addOrderButton, 0, 3, 1, 1);
        gp.add(modifyButton, 1, 3, 1, 1);
        gp.add(exitButton, 2, 3, 1, 1);
        gp.setHgap(20);
        gp.setVgap(20);
        gp.setPadding(new Insets(20));

        return new Scene(gp);
    }

    public DatabaseAccess getDba() {
        return dba;
    }
}
