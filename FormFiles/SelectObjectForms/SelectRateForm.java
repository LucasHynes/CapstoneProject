package FormFiles.SelectObjectForms;

import java.sql.SQLException;
import Database.DatabaseAccess;
import FormFiles.ErrorWindow;
import FormFiles.AddDatabaseForms.AddRateForm;
import FormFiles.ModifyDatabaseForms.ModifyRateForm;
import Objects.RateSheet;
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

public class SelectRateForm {

    private DatabaseAccess dba = new DatabaseAccess();

    public Scene form() {
        TableView<RateSheet> rate_select = new TableView<>();

        TableColumn<RateSheet, String> distributor_col = new TableColumn<>("Distributor");
        distributor_col.setCellValueFactory(new PropertyValueFactory<>("d"));

        TableColumn<RateSheet, String> storeFront_col = new TableColumn<>("Store Front");
        storeFront_col.setCellValueFactory(new PropertyValueFactory<>("s"));

        TableColumn<RateSheet, Double> rate_col = new TableColumn<>("Rate");
        rate_col.setCellValueFactory(new PropertyValueFactory<>("rate"));

        TableColumn<RateSheet, Double> min_col = new TableColumn<>("Minimum");
        min_col.setCellValueFactory(new PropertyValueFactory<>("minimum"));

        rate_select.getColumns().add(distributor_col);
        rate_select.getColumns().add(storeFront_col);
        rate_select.getColumns().add(rate_col);
        rate_select.getColumns().add(min_col);

        try {
            rate_select.getItems().addAll(getDba().get_rates());
        } catch (SQLException e) {
            
            //launches error window to display to the user the problems with the input
            new ErrorWindow(new Stage(), e.getMessage(), e.getLocalizedMessage());
        }

        Button modifyButton = new Button("Modify Rate");
        
        EventHandler<MouseEvent> modifyEvent = e -> {
            if(rate_select.getSelectionModel().getSelectedItem() != null) {
                
                RateSheet d = rate_select.getSelectionModel().getSelectedItem();

                //launch form to modify selected rate
                ModifyRateForm mdf = new ModifyRateForm();
                
                Stage s = new Stage();
                try {
                    s.setScene(mdf.form(d));
                } catch (SQLException e1) {
                    
                    //launches error window to display to the user the problems with the input
                    new ErrorWindow(new Stage(), e1.getMessage(), e1.getLocalizedMessage());
                }
                s.show();
            }
            
            //closes active window
            ((Stage) modifyButton.getScene().getWindow()).close();
        };

        modifyButton.addEventHandler(MouseEvent.MOUSE_CLICKED, modifyEvent);

        Button addButton = new Button("Add Rate");

        EventHandler<MouseEvent> addEvent = e -> {
            //launch form to add rate
            AddRateForm adf = new AddRateForm();
                
            Stage s = new Stage();
            try {
                s.setScene(adf.form());
            } catch (SQLException e1) {
                
                    //launches error window to display to the user the problems with the input
                    new ErrorWindow(new Stage(), e1.getMessage(), e1.getLocalizedMessage());
            }
            s.show();

            //closes active window
            ((Stage) modifyButton.getScene().getWindow()).close();
        };

        addButton.addEventHandler(MouseEvent.MOUSE_CLICKED, addEvent);
        
        Button exitButton = new Button("Exit");
        
        EventHandler<MouseEvent> exitEvent = e -> ((Stage) exitButton.getScene().getWindow()).close();
    
        exitButton.addEventHandler(MouseEvent.MOUSE_CLICKED, exitEvent);

        GridPane gp = new GridPane();

        gp.add(rate_select, 0, 0, 3, 3);
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
