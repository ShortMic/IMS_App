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
 * The Modify Product Modal View Controller which handles event and execution logic.
 *
 * @author Michael Short
 * @version 1.0
 */
public class ModifyProductController implements Initializable {

    public Alert alert;
    public TextField modifyProductIDTextField;
    public TextField modifyProductNameTextField;
    public TextField modifyProductInvTextField;
    public TextField modifyProductCostTextField;
    public TextField modifyProductMaxTextField;
    public TextField modifyProductMinTextField;
    public Scene mainScene = IMSApplication.scenes.get("mainScene");
    public static Product currentProduct;

    /**
     * The initialize method (inherited by Initializable) which runs automatically upon loading the controller's
     * associated fxml file in the main application class. Initializes pre-declared table view components and links associated
     * Part collections with their respective field properties to column categories. Populates the associatedPartsTable
     * with the pre-selected Product's associated parts (if applicable).
     *
     * @param url loads the url (automatically inherited and handled by the javafx framework)
     * @param resourceBundle loads the associated resourceBundle (automatically inherited and handled by the javafx framework)
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * A method that should be called upon loading this controller's fxml view to pre-populate its components with
     * the appropriate selected product properties.
     * @param product  A product object passed indirectly by the selection of a product in the partTable component
     */
    @FXML
    public void populateModifiableProductFields(Product product){
        currentProduct = product;
        modifyProductIDTextField.setPromptText(Integer.toString(currentProduct.getId()));
        modifyProductIDTextField.setText(Integer.toString(currentProduct.getId()));
        modifyProductNameTextField.setPromptText(currentProduct.getName());
        modifyProductNameTextField.setText(currentProduct.getName());
        modifyProductInvTextField.setPromptText(Integer.toString(currentProduct.getStock()));
        modifyProductInvTextField.setText(Integer.toString(currentProduct.getStock()));
        modifyProductCostTextField.setPromptText(Double.toString(currentProduct.getPrice()));
        modifyProductCostTextField.setText(Double.toString(currentProduct.getPrice()));
        modifyProductMaxTextField.setPromptText(Integer.toString(currentProduct.getMax()));
        modifyProductMaxTextField.setText(Integer.toString(currentProduct.getMax()));
        modifyProductMinTextField.setPromptText(Integer.toString(currentProduct.getMin()));
        modifyProductMinTextField.setText(Integer.toString(currentProduct.getMin()));
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
     * Event handler/listener that fires from the save button, saves the input data as an updated product object in inventory and
     * redirects back to the main IMSApplication screen. Checks and throws exceptions for incorrect input types and displays
     * a popup error for incorrect inventory ranges and incorrect min/max values.
     * @param e  Pre-generated and auto-handled event argument.
     */
    @FXML
    protected void onSaveButtonClick(ActionEvent e){
        try{
            textFieldDataValidationLogger("Product min", modifyProductMinTextField, "int");
            textFieldDataValidationLogger("Product max", modifyProductMaxTextField, "int");
            textFieldDataValidationLogger("Product inventory", modifyProductInvTextField, "int");
            textFieldDataValidationLogger("Product cost", modifyProductCostTextField, "Double");
            int productId = Integer.parseInt(modifyProductIDTextField.getText());
            int min = Integer.parseInt(modifyProductMinTextField.getText());
            int max = Integer.parseInt(modifyProductMaxTextField.getText());
            int amount = Integer.parseInt(modifyProductInvTextField.getText());
            if(amount >= min && amount <= max && min >= 0){
                Product updatedProduct =  new Product(productId, modifyProductNameTextField.getText(),
                        Double.parseDouble(modifyProductCostTextField.getText()), amount, min, max);
                IMSApplication.inventory.updateProduct(productId,updatedProduct);
                ((Stage)(((Button)e.getSource()).getScene().getWindow())).setScene(mainScene);
            }else{
                alert = new Alert(Alert.AlertType.ERROR,
                        "Minimum must be equal to or below maximum and inventory amount must be within range!");
                alert.setHeaderText("Invalid Input");
                alert.show();
            }
        }catch(Exception exception){
            System.out.println(exception.getMessage());
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
}