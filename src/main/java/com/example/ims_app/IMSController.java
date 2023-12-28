package com.example.ims_app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
/**
 * The Main Modal View Controller which handles event and execution logic.
 * Handles redirects to other fxml views as well.
 *
 * @author Michael Short
 * @version 1.0
 */
public class IMSController implements Initializable {

    public Alert alert;
    public TextField productsSearchBar;
    public Label productsTablePlaceholderLabel;
    public Button fileBrowserButton;
    @FXML private TableView<Product> productsTable;
    public TableColumn<Product, Integer> productId;
    public TableColumn<Product, String> productName;
    public TableColumn<Product, Integer> productInvLvl;
    public Scene mainScene = IMSApplication.scenes.get("mainScene");
    public Label exitButton;
    public TextField imageAddressBar;
    public FileChooser fileChooser = new FileChooser();

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
        File initialRootFile = new File(System.getProperty("user.dir")+"\\src\\main\\java\\com\\example\\ims_app\\yolov5\\data\\images\\inventory_images\\IMG_2256.jpeg");
        System.out.println("Opening file explorer to default: "+initialRootFile.toString());
        fileChooser.setInitialDirectory(initialRootFile.getParentFile());
        productId.setCellValueFactory(new PropertyValueFactory<Product, Integer>("id"));
        productName.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        productInvLvl.setCellValueFactory(new PropertyValueFactory<Product, Integer>("stock"));
        productsTable.setItems(IMSApplication.inventory.getAllProducts());
    }

    public void onBrowseFileExplorer(ActionEvent actionEvent) {
        File file = fileChooser.showOpenDialog(new Stage());
        imageAddressBar.setText(file.getPath().toString());
    }

    /**
     * Event handler/listener that fires from the exit button and closes the program.
     * @param e  Pre-generated and auto-handled event argument.
     */
    @FXML
    protected void onExitButtonClick(ActionEvent e) {
        ((Stage)(((Button)e.getSource()).getScene().getWindow())).close();
    }

    @FXML
    protected void onImportFromImage(ActionEvent e){
        String imgAddress = imageAddressBar.getText();
        YoloPythonWorker.run(imgAddress);
        YoloPythonWorker.latestResultsCollection.entrySet().stream()
                .map(element -> new Product(element.getKey(),element.getValue()))
                .forEach(IMSApplication.inventory.getAllProducts()::add);
    }

    /**
     * Event handler/listener that fires from the add parts button and loads the add product modal window.
     * @param e  Pre-generated and auto-handled event argument.
     */
    @FXML
    protected void onAddProductsButtonClick(ActionEvent e) {
        Scene addProduct = IMSApplication.scenes.get("addProduct");
        ((Stage)(((Button)e.getSource()).getScene().getWindow())).setScene(addProduct);
    }

    /**
     * Event handler/listener that fires from the delete products button and opens a confirmation alertbox.
     * On confirmation, it deletes the selected product on the table or throws an exception on unexpected failure.
     * If the user attempts to delete a product with any associated parts, a popup error alertbox will display and prevent
     * the deletion of the product.
     * @param e  Pre-generated and auto-handled event argument.
     */
    @FXML
    protected void onDeleteProductsButtonClick(ActionEvent e) {
        try{

            if(productsTable.getSelectionModel().getSelectedItem() != null){
                if(!productsTable.getSelectionModel().getSelectedItem().getAllAssociatedParts().isEmpty()){
                    alert = new Alert(Alert.AlertType.ERROR,
                            "Cannot delete a product with associated parts!");
                    alert.setHeaderText("Invalid Selection");
                    alert.show();
                }else{
                    alert = new Alert(Alert.AlertType.CONFIRMATION,
                            "Do you want to delete this product?");
                    alert.setHeaderText("Delete");
                    alert.showAndWait().ifPresent(
                            response -> {
                                if(response == ButtonType.OK){
                                    IMSApplication.inventory.deleteProduct(productsTable.getSelectionModel().getSelectedItem());
                                    tableLabelUpdater(productsTablePlaceholderLabel, "Product");
                                }
                            }
                    );
                }
            }else{
                alert = new Alert(Alert.AlertType.ERROR,
                        "You have not selected a valid product to delete!");
                alert.setHeaderText("Invalid Selection");
                alert.show();
            }
        }catch(Exception exception){
            System.out.println(exception.getMessage());
        }
    }

    /**
     * Event handler/listener that fires from the modify products button and loads the modify product modal window
     * @param e  Pre-generated and auto-handled event argument.
     */
    @FXML
    protected void onModifyProductsButtonClick(ActionEvent e) throws IOException {
        //Open Modify Products modal pane window
        Product selectedProduct = productsTable.getSelectionModel().getSelectedItem();
        if(selectedProduct != null){
            FXMLLoader fxmlLoader = new FXMLLoader(IMSApplication.class.getResource("ims-modifyProductView.fxml"));
            IMSApplication.scenes.put("modifyProduct", new Scene(fxmlLoader.load(), 387, 438));
            Scene modifyProduct = IMSApplication.scenes.get("modifyProduct");
            ((Stage)(((Button)e.getSource()).getScene().getWindow())).setScene(modifyProduct);
            ((ModifyProductController)fxmlLoader.getController()).populateModifiableProductFields(selectedProduct);
        }else{
            alert = new Alert(Alert.AlertType.ERROR,
                    "You have not selected a valid product to modify!");
            alert.setHeaderText("Invalid Selection");
            alert.show();
        }
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

    /**
     * Event handler/listener for the search text field that filters the associated Product tableview below
     * @param keyEvent  Pre-generated and auto-handled event argument.
     */
    @FXML
    public void onSearchProductsQuery(KeyEvent keyEvent) {
        String query = productsSearchBar.getText();
        if(!query.equals("")){
            try{
                tableLabelUpdater(productsTablePlaceholderLabel, "Product");
                productsTable.setItems(IMSApplication.inventory.lookupProduct(query));
            }catch(Exception e){
                tableLabelUpdater(productsTablePlaceholderLabel, "Product");
                productsTable.setItems(null);
            }
        }else{
            tableLabelUpdater(productsTablePlaceholderLabel, "Product");
            productsTable.setItems(IMSApplication.inventory.getAllProducts());
        }
    }
}