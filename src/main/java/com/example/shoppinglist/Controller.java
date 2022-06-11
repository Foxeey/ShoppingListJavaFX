package com.example.shoppinglist;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.w3c.dom.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable{
    @FXML
    private Button buttonAdd, buttonDelete, buttonUpdate, buttonSearch;
    @FXML
    private Label myLabel;
    @FXML
    private ListView<String> myListView;
    ArrayList<String> data = new ArrayList<>();
    String selectedItem;
    int selectedIndex;
    @FXML
    private TextField textFieldProduct, textFieldAmount;
    public void addRecord(ActionEvent event) {
        if(!textFieldProduct.getText().trim().isEmpty() && !textFieldAmount.getText().trim().isEmpty()) {
            JDBCShoppingList.AddNewRecord(textFieldProduct.getText(), textFieldAmount.getText());
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.show();
        }
        myListView.getItems().add(textFieldProduct.getText());
        refresh();
    }
    public void deleteRecord(ActionEvent event) {
        JDBCShoppingList.recordDelete(selectedItem);
        myListView.getItems().remove(selectedIndex);
        refresh();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        JDBCShoppingList.getData(data);
        for(int i =0; i<data.size(); i++) {
            myListView.getItems().add(data.get(i));
        }
        myListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                selectedItem = myListView.getSelectionModel().getSelectedItem();
                myLabel.setText("Ilość: " + JDBCShoppingList.getAmount(selectedItem));
                selectedIndex = myListView.getSelectionModel().getSelectedIndex();
            }
        });
    }
    public void refresh() {
        data.clear();
        JDBCShoppingList.getData(data);
        myListView.refresh();
    }
    public void updateRecord(ActionEvent event) {
        if(!textFieldAmount.getText().trim().isEmpty()) {
            JDBCShoppingList.recordUpdate(selectedItem, textFieldAmount.getText());
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.show();
        }
        refresh();
    }
    public void searchRecord(ActionEvent event) {
        myListView.getItems().clear();
        data.clear();
        JDBCShoppingList.searchData(textFieldProduct.getText(), data);
        for(int i =0; i<data.size(); i++) {
            myListView.getItems().add(data.get(i));
        }
        refresh();
    }
}