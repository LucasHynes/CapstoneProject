package FormFiles.SelectObjectForms;

import java.sql.SQLException;

import Database.DatabaseAccess;
import FormFiles.ErrorWindow;
import FormFiles.AddDatabaseForms.AddDriverForm;
import FormFiles.ModifyDatabaseForms.ModifyDriverForm;
import Objects.Driver;
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

public class SelectDriverForm {

    private final DatabaseAccess dba = new DatabaseAccess();

    public Scene form() {
        TableView<Driver> driver_select = new TableView<>();

        TableColumn<Driver, String> driver_col = new TableColumn<>("Driver Name");
        driver_col.setCellValueFactory(new PropertyValueFactory<>("driver_name"));

        driver_select.getColumns().add(driver_col);

        try {
            driver_select.getItems().addAll(getDba().get_drivers());
        } catch (SQLException e) {
            
            //launches error window to display to the user the problems with the input
            new ErrorWindow(new Stage(), e.getMessage(), e.getLocalizedMessage());
        }

        Button modifyButton = new Button("Modify Driver");
        
        EventHandler<MouseEvent> modifyEvent = e -> {
            if(driver_select.getSelectionModel().getSelectedItem() != null) {
                
                Driver d = driver_select.getSelectionModel().getSelectedItem();

                //launch form to modify selected driver
                ModifyDriverForm mdf = new ModifyDriverForm();
                
                Stage s = new Stage();
                s.setScene(mdf.form(d));
                s.show();
            }
            
            //closes active window
            ((Stage) modifyButton.getScene().getWindow()).close();
        };

        modifyButton.addEventHandler(MouseEvent.MOUSE_CLICKED, modifyEvent);

        Button addButton = new Button("Add Driver");

        EventHandler<MouseEvent> addEvent = e -> {
            //launch form to add driver
            AddDriverForm adf = new AddDriverForm();
                
            Stage s = new Stage();
            s.setScene(adf.form());
            s.show();

            //closes active window
            ((Stage) modifyButton.getScene().getWindow()).close();
        };

        addButton.addEventHandler(MouseEvent.MOUSE_CLICKED, addEvent);
        
        Button exitButton = new Button("Exit");
        
        EventHandler<MouseEvent> exitEvent = e -> ((Stage) exitButton.getScene().getWindow()).close();
    
        exitButton.addEventHandler(MouseEvent.MOUSE_CLICKED, exitEvent);

        GridPane gp = new GridPane();

        gp.add(driver_select, 0, 0, 3, 3);
        gp.add(addButton, 0, 3, 1, 1);
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
