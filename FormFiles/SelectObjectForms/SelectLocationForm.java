package FormFiles.SelectObjectForms;

import java.sql.SQLException;
import java.util.ArrayList;

import Database.DatabaseAccess;
import FormFiles.AddDatabaseForms.AddDestinationForm;
import FormFiles.ModifyDatabaseForms.ModifyDestinationForm;
import Objects.Distributor;
import Objects.StoreFront;
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

public class SelectLocationForm {

    private final DatabaseAccess dba = new DatabaseAccess();

    public Scene form() throws SQLException {
        TableView<location> location_select = new TableView<>();

        ArrayList<location> locations = new ArrayList<>();

        for(Object o: getDba().get_destinations()){

            location dest = new location();

            if(o instanceof StoreFront){
                dest.location_name = ((StoreFront) o).getName();
                dest.category = "StoreFront";
                dest.state = ((StoreFront) o).getState();
            
            } else if (o instanceof Distributor) {
                dest.location_name = ((Distributor) o).getName();
                dest.category = "Distributor";
                dest.state = ((Distributor) o).getState();
            
            } else {
                dest.location_name = o.getClass().getName();
                dest.category = "Non-Store";
                dest.state = "N/A";
            }

            dest.associatedObject = o;

            locations.add(dest);
        }

        TableColumn<location, String> location_col = new TableColumn<>("Location Name");
        location_col.setCellValueFactory(new PropertyValueFactory<>("location_name"));

        TableColumn<location, String> category_col = new TableColumn<>("Category");
        category_col.setCellValueFactory(new PropertyValueFactory<>("category"));

        TableColumn<location, String> state_col = new TableColumn<>("State");
        state_col.setCellValueFactory(new PropertyValueFactory<>("state"));

        location_select.getColumns().add(location_col);
        location_select.getColumns().add(category_col);
        location_select.getColumns().add(state_col);

        location_select.getItems().addAll(locations);

        Button modifyButton = new Button("Modify Location");
        
        EventHandler<MouseEvent> modifyEvent = e -> {
            if(location_select.getSelectionModel().getSelectedItem() != null) {
                
                location d = location_select.getSelectionModel().getSelectedItem();

                //launch form to modify selected location
                ModifyDestinationForm mdf = new ModifyDestinationForm();
                
                Stage s = new Stage();
                s.setScene(mdf.form(d));
                s.show();
            }
            
            //closes active window
            ((Stage) modifyButton.getScene().getWindow()).close();
        };

        modifyButton.addEventHandler(MouseEvent.MOUSE_CLICKED, modifyEvent);

        Button addButton = new Button("Add Location");

        EventHandler<MouseEvent> addEvent = e -> {
            //launch form to add location
            AddDestinationForm adf = new AddDestinationForm();
                
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

        gp.add(location_select, 0, 0, 3, 3);
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

class location {
    public String location_name;
    public String category;
    public String state;
    public Object associatedObject;
}
