package FormFiles.SelectObjectForms;

import java.sql.SQLException;

import Database.DatabaseAccess;
import FormFiles.ErrorWindow;
import FormFiles.AddDatabaseForms.AddTruckForm;
import FormFiles.ModifyDatabaseForms.ModifyTruckForm;
import Objects.Truck;
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

public class SelectTruckForm {

    private final DatabaseAccess dba = new DatabaseAccess();

    public Scene form() {
        TableView<Truck> truck_select = new TableView<>();

        TableColumn<Truck, String> truck_name_col = new TableColumn<>("Truck Name");
        truck_name_col.setCellValueFactory(new PropertyValueFactory<>("truck_name"));
        

        TableColumn<Truck, String> driver_col = new TableColumn<>("Driver Name");
        driver_col.setCellValueFactory(new PropertyValueFactory<>("driver_name"));

        truck_select.getColumns().add(truck_name_col);
        truck_select.getColumns().add(driver_col);

        try {
            truck_select.getItems().addAll(getDba().get_trucks());
        } catch (SQLException e) {
            
            //launches error window to display to the user the problems with the input
            new ErrorWindow(new Stage(), e.getMessage(), e.getLocalizedMessage());
        }

        Button modifyButton = new Button("Modify Truck");
        
        EventHandler<MouseEvent> modifyEvent = e -> {
            if(truck_select.getSelectionModel().getSelectedItem() != null) {
                
                Truck t = truck_select.getSelectionModel().getSelectedItem();

                //launch form to modify selected truck
                ModifyTruckForm mtf = new ModifyTruckForm();
                
                try {
                    Stage s = new Stage();
                    s.setScene(mtf.form(t));
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

        Button addTruckButton = new Button("Add Truck");

        EventHandler<MouseEvent> addTruckEvent = e -> {
            //launch form to modify selected truck
            AddTruckForm mtf = new AddTruckForm();
                
            try {
                Stage s = new Stage();
                s.setScene(mtf.form());
                s.show();

            } catch (SQLException e1) {
                
                //launches error window to display to the user the problems with the input
                new ErrorWindow(new Stage(), e1.getMessage(), e1.getLocalizedMessage());
            }
        };

        addTruckButton.addEventHandler(MouseEvent.MOUSE_CLICKED, addTruckEvent);
        
        Button exitButton = new Button("Exit");
        
        EventHandler<MouseEvent> exitEvent = e -> ((Stage) exitButton.getScene().getWindow()).close();
    
        exitButton.addEventHandler(MouseEvent.MOUSE_CLICKED, exitEvent);

        GridPane gp = new GridPane();

        gp.add(truck_select, 0, 0, 3, 3);
        gp.add(addTruckButton, 0, 3, 1, 1);
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
