package FormFiles.SelectObjectForms;

import java.sql.SQLException;

import Database.DatabaseAccess;
import FormFiles.ErrorWindow;
import FormFiles.AddDatabaseForms.AddReceiptForm;
import FormFiles.ModifyDatabaseForms.ModifyReceiptForm;
import Objects.Receipt;
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

public class SelectReceiptForm {

    private final DatabaseAccess dba = new DatabaseAccess();

    public Scene form() {
        TableView<Receipt> receipt_select = new TableView<>();

        TableColumn<Receipt, String> receipt_col = new TableColumn<>("Receipt Name");
        receipt_col.setCellValueFactory(new PropertyValueFactory<>("receipt_name"));

        receipt_select.getColumns().add(receipt_col);

        try {
            receipt_select.getItems().addAll(getDba().get_receipts());
        } catch (SQLException e) {
            
            //launches error window to display to the user the problems with the input
            new ErrorWindow(new Stage(), e.getMessage(), e.getLocalizedMessage());
        }

        Button modifyButton = new Button("Modify Receipt");
        
        EventHandler<MouseEvent> modifyEvent = e -> {
            if(receipt_select.getSelectionModel().getSelectedItem() != null) {
                
                Receipt d = receipt_select.getSelectionModel().getSelectedItem();

                //launch form to modify selected receipt
                ModifyReceiptForm mdf = new ModifyReceiptForm();
                
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

        Button addButton = new Button("Add Receipt");

        EventHandler<MouseEvent> addEvent = e -> {
            //launch form to add receipt
            AddReceiptForm adf = new AddReceiptForm();
                
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

        gp.add(receipt_select, 0, 0, 3, 3);
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
