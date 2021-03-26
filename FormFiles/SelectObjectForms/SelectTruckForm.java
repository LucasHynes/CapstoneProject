package FormFiles.SelectObjectForms;

import Database.DatabaseAccess;
import Objects.Truck;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class SelectTruckForm {

    private final DatabaseAccess dba = new DatabaseAccess();

    public Scene form() {
        TableView<Truck> truck_select = new TableView<>();

        TableColumn<Truck, String> truck_name_col = new TableColumn<>("Truck Name");
        truck_name_col.setCellValueFactory(new PropertyValueFactory<>("truck_name"));



        return null;
    }

    public DatabaseAccess getDba() {
        return dba;
    }
}
