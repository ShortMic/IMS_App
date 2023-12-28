package com.example.ims_app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Extends the pre-supplied class Part.java. An initializable class meant to represent a Product object
 * and uniquely holds the associatedParts collection property and their accessor/mutator methods.
 *
 * @author Michael Short
 */

public class Product extends Part{

    private ObservableList<Part>associatedParts;
//    private int id;
//    private String name;
//    private double price;
//    private int stock;
//    private int min;
//    private int max;


    public Product(int id, String name, double price, int stock, int min, int max) {
        super(id, name, price, stock, min, max);
        associatedParts = FXCollections.observableArrayList();
    }

    public Product(String name, int stock) {
        super(IMSApplication.uniqueId++, name, 0, stock, 0, Integer.MAX_VALUE);
        associatedParts = FXCollections.observableArrayList();
    }

    /**
     * Adds an associated part to the product, thus linking the product to one or more parts.
     * @param part  Part object to link to the products associated parts list
     */
    public void addAssociatedPart(Part part){
        associatedParts.add(part);
    }

    /**
     * Deletes an associated part from the product's associated parts list.
     * @param selectedAssociatedPart  Part object to remove from the products associated parts list
     * @return boolean of whether the part was removed from the products associated parts list
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart){
        return associatedParts.remove(selectedAssociatedPart);
    }

    /**
     * Accessor method to an associated part from the product's associated parts list.
     * @return ObservableList  The product's associated part list
     */
    public ObservableList<Part> getAllAssociatedParts(){
        return associatedParts;
    }
}
