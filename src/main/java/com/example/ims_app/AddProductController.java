package com.example.ims_app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * The Add Product Modal View Controller which handles event and execution logic.
 *
 * @author Michael Short
 * @version 1.0
 */
public class AddProductController implements Initializable {

    public Alert alert;
    public TextField AddProductIDTextField;
    public TextField AddProductNameTextField;
    public TextField AddProductInvTextField;
    public TextField AddProductCostTextField;
    public TextField AddProductMaxTextField;
    public TextField AddProductMinTextField;
    public Scene mainScene = IMSApplication.scenes.get("mainScene");

    /**
     * The initialize method (inherited by Initializable) which runs automatically upon loading the controller's
     * associated fxml file in the main application class. Initializes pre-declared table view components and links associated
     * Part collections with their respective field properties to column categories.
     *
     * @param url loads the url (automatically inherited and handled by the javafx framework)
     * @param resourceBundle loads the associated resourceBundle (automatically inherited and handled by the javafx framework)
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //PythonInterpreter pythonInterpreter = new PythonInterpreter();
        //pythonInterpreter.exec("print('Hello world!')");
    }

    /**
     * Event handler/listener that fires from the cancel button and redirects back to the main IMSApplication screen.
     * @param e  Pre-generated and auto-handled event argument.
     */
    @FXML
    protected void onCancelButtonClick(ActionEvent e){
        ((Stage)(((Button)e.getSource()).getScene().getWindow())).setScene(mainScene);
    }

    /**
     * Event handler/listener that fires from the save button, saves the input data as a new product object in inventory and
     * redirects back to the main IMSApplication screen. Checks and throws exceptions for incorrect input types and displays
     * a popup error for incorrect inventory ranges and incorrect min/max values.
     * @param e  Pre-generated and auto-handled event argument.
     */
    @FXML
    public void onSaveButtonClick(ActionEvent e) {
        Product newProduct = null;
        try{
            textFieldDataValidationLogger("Product min", AddProductMinTextField, "int");
            textFieldDataValidationLogger("Product max", AddProductMaxTextField, "int");
            textFieldDataValidationLogger("Product inventory", AddProductInvTextField, "int");
            textFieldDataValidationLogger("Product cost", AddProductCostTextField, "Double");
            int min = Integer.parseInt(AddProductMinTextField.getText());
            int max = Integer.parseInt(AddProductMaxTextField.getText());
            int amount = Integer.parseInt(AddProductInvTextField.getText());
            if(amount >= min && amount <= max && min >= 0){
                newProduct = new Product(IMSApplication.uniqueId++, AddProductNameTextField.getText(),
                        Double.parseDouble(AddProductCostTextField.getText()), amount, min, max);
                IMSApplication.inventory.addProduct(newProduct);
                ((Stage)(((Button)e.getSource()).getScene().getWindow())).setScene(mainScene);
            }else{
                alert = new Alert(Alert.AlertType.ERROR,
                        "Minimum must be equal to or below maximum and inventory amount must be within range!");
                alert.setHeaderText("Invalid Input");
                alert.show();
            }
        }catch(Exception exception){
            //System.out.println(exception.getMessage());
        }
    }

    /**
     * Private helper method that logs conversion error messages for the associated text fields and their expected types
     * @param textFieldLabel    The currently asserted text field label
     * @param textField         The currently asserted text field object
     * @param expectedType      The expected datatype to assert for
     */
    private void textFieldDataValidationLogger(String textFieldLabel, TextField textField, String expectedType){
        String message = "";
        switch(expectedType){
            case "int":
                if(!tryParseInt(textField.getText())){
                    message = "Invalid type for "+textFieldLabel+" field: "+"\""+textField.getText()+"\" is not an "+expectedType;
                }
                break;
            case "Double":
                if(!tryParseDouble(textField.getText())){
                    message = "Invalid type for "+textFieldLabel+": "+"\""+textField.getText()+"\" is not a "+expectedType;
                }
                break;
            default:
                break;
        }
        if(!message.equals("")){
            System.out.println(message);
        }
    }

    /**
     * Private helper method which returns whether a string is convertible to an int.
     * @param text  The text to try converting into an int
     * @return boolean
     */
    private boolean tryParseInt(String text){
        try{
            Integer.parseInt(text);
        }catch(Exception exception){
            return false;
        }
        return true;
    }

    /**
     * Private helper method which returns whether a string is convertible to a double.
     * @param text  The text to try converting into a double
     * @return boolean
     */
    private boolean tryParseDouble(String text){
        try{
            Double.parseDouble(text);
        }catch(Exception exception){
            return false;
        }
        return true;
    }

    /**
     * Private helper method that keeps the associated table's placeholder text accurate to the current context of the table
     * For use in table search query methods and table delete methods.
     * @param label Associated table's placeholder label
     * @param type String of associated object type: Part or Product
     */
    private void tableLabelUpdater(Label label, String type){
        boolean isEmpty = type.equals("Part") ? IMSApplication.inventory.getAllParts().isEmpty(): IMSApplication.inventory.getAllProducts().isEmpty();
        if(isEmpty){
            label.setText(type+" Inventory Empty");
        }else{
            label.setText(type+" Name/Id not found");
        }
    }

}
