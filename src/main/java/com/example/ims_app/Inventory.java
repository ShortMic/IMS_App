package com.example.ims_app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

/**
 * The Inventory class which is a collection data type that stores Part and Product objects in ObservableLists
 * and provides accessor and mutator methods to manipulate and control the collection.
 *
 * @author Michael Short
 * @version 1.0
 */
public class Inventory{

    private ObservableList<Part> allParts;
    FilteredList<Part> filteredParts;
    private ObservableList<Product> allProducts;

    public Inventory(int id, String name, double price, int stock, int min, int max) {
        allParts = FXCollections.observableArrayList();
        allProducts = FXCollections.observableArrayList();
        addProduct(new Product(id, name, price, stock, min, max));
    }

    public Inventory(Product product){
        allParts = FXCollections.observableArrayList();
        allProducts = FXCollections.observableArrayList();
        addProduct(product);
    }

    public Inventory(Part part){
        allParts = FXCollections.observableArrayList();
        allProducts = FXCollections.observableArrayList();
        addPart(part);
    }

    /**
     * Mutator method which adds a new part to the inventory's parts list.
     * @param newPart the new part object to add to the inventory's parts list.
     */
    public void addPart(Part newPart){
        allParts.add(newPart);
    }

    /**
     * Mutator method which adds a new product to the inventory's products list.
     * @param newProduct the new product object to add to the inventory's products list.
     */
    public void addProduct(Product newProduct){
        allProducts.add(newProduct);
    }

    /**
     * Accessor method which gets product by product Id.
     * @param productId  int of the product Id
     * @return a product with the matching product Id.
     */
    public Product lookupProduct(int productId){
        return allProducts.get(productId);
    }

    /**
     * Queries all products in inventory for productName search term and matches by either product Id or product name.
     * @param productName  String of the search query in the associated text field
     * @return An observable products list of any/all query matching products
     */
    public ObservableList<Product> lookupProduct(String productName) throws NumberFormatException{
        productName = productName.toUpperCase();
        String finalProductName = productName;
        int productId = -1;
        try{
            productId = Integer.parseInt(finalProductName);
        }catch(Exception exception){
            System.out.println("Product search query is not an int.");
        }
        if(productId == -1){
            return allProducts.filtered(i -> i.getName().toUpperCase().equals(finalProductName)
                    || i.getName().toUpperCase().contains(finalProductName));
        }else{
            return allProducts.filtered(i -> Integer.parseInt(finalProductName) == i.getId());
        }
    }

    /**
     * Updates the associated Id's product field properties in the inventory list.
     * @param index  The associated product's Id (not necessarily the product's "index" in inventory)
     * @param newProduct The updated product object to replace the previous id's product
     */
    public void updateProduct(int index, Product newProduct){
        for(int i = 0; i < allProducts.size(); i++){
            if(allProducts.get(i).getId() == index){
                allProducts.set(i, newProduct);
            }
        }
    }

    /**
     * Deletes the part from inventory parts list.
     * @param selectedPart  The part object to be deleted
     * @return boolean on whether the part was deleted from inventory parts list
     */
    public boolean deletePart(Part selectedPart){
        return allParts.remove(selectedPart);
    }

    /**
     * Deletes the product from inventory product list.
     * @param selectedProduct  The product object to be deleted
     * @return boolean on whether the product was deleted from inventory products list
     */
    public boolean deleteProduct(Product selectedProduct){
        return allProducts.remove(selectedProduct);
    }

    /**
     * Accessor method for inventory parts list.
     * @return inventory parts list
     */
    public ObservableList<Part> getAllParts(){
        return allParts;
    }

    /**
     * Accessor method for inventory products list.
     * @return inventory products list
     */
    public ObservableList<Product>getAllProducts(){
        return allProducts;
    }


}
