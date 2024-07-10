package com.example.bty.Controllers.usercontroller;

import com.example.bty.Entities.User;
import com.example.bty.Services.IServiceUser;
import com.example.bty.Services.ServiceUser;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MembreListController implements Initializable {
    public TableView members_tableView;
    public TableColumn membres_col_ID;
    public TableColumn membres_col_nom;
    public TableColumn membres_col_email;
    public TableColumn membres_col_telephone;
    public TableColumn membres_col_etat ;
    public TableColumn membres_col_action;

    IServiceUser serviceUser00=new ServiceUser();

    public void consulterMembers() {
        // Fetch all members
        List<User> members = serviceUser00.getAllMembers();

        // Clear the table
        members_tableView.getItems().clear();

        // Define how to populate the columns
        membres_col_ID.setCellValueFactory(new PropertyValueFactory<>("id"));
        membres_col_nom.setCellValueFactory(new PropertyValueFactory<>("name"));
        membres_col_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        membres_col_telephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        membres_col_etat.setCellFactory(tc -> new TableCell<User, Boolean>() {
            private CheckBox checkBox = new CheckBox();
            private Label label = new Label();

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    checkBox.setSelected(item);
                    checkBox.setOnAction(e -> {
                        User member = getTableView().getItems().get(getIndex());
                        member.setEtat(checkBox.isSelected());
                        serviceUser00.update(member); // Assuming update() method updates the member in the database
                        label.setText(checkBox.isSelected() ? "Actif" : "Inactif");
                        label.getStyleClass().clear();
                        label.getStyleClass().add(checkBox.isSelected() ? "label-active" : "label-inactive");
                    });
                    label.setText(item ? "Actif" : "Inactif");
                    label.getStyleClass().add(item ? "label-active" : "label-inactive");
                    HBox hBox = new HBox(checkBox, label);
                    hBox.setSpacing(10);
                    setGraphic(hBox);
                }
            }
        });

        membres_col_etat.setCellValueFactory(new PropertyValueFactory<>("etat"));
        membres_col_action.setCellFactory(param -> new TableCell<User, Void>() {
            //**********
            final FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
            final HBox pane = new HBox(deleteIcon);

            {
                deleteIcon.getStyleClass().add("delete-icon");

                deleteIcon.setOnMouseClicked(event -> {
                    User selectedMember = (User) members_tableView.getSelectionModel().getSelectedItem();
                    // Create a confirmation dialog
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation Dialog");
                    alert.setHeaderText("Delete Coach");
                    alert.setContentText("Are you sure you want to delete this coach?");

                    // Show the dialog and wait for user's response
                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            // If user confirmed, delete the coach
                            serviceUser00.delete(selectedMember.getId());

                            // Refresh the table
                            consulterMembers();
                        }
                    });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(pane);
                }
            }
        });
        members_tableView.getItems().addAll(members);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        consulterMembers();
    }

    public void dalimtaahoussi(ActionEvent actionEvent) {
    }
}