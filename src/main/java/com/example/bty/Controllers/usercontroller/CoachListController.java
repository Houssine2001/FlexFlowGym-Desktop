package com.example.bty.Controllers.usercontroller;
import com.example.bty.Controllers.usercontroller.CoachesFormController;

import com.example.bty.Entities.User;
import com.example.bty.Services.IServiceUser;
import com.example.bty.Services.ServiceUser;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import  javafx.scene.Node;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;



public class CoachListController implements Initializable {

    @FXML
    public TextField members_customerId;
    @FXML
    public TextField members_name;
    @FXML
    public TextField members_phoneNum;
    @FXML
    public ComboBox members_status;
    @FXML
    public Button members_addBtn;
    @FXML
    public Button members_updateBtn;
    @FXML
    public Button members_deleteBtn;
    @FXML
    public TableView members_tableView;
    @FXML
    public TableColumn members_col_customerID;
    @FXML
    public TableColumn members_col_name;
    @FXML
    public TableColumn members_col_phoneNum;
    @FXML
    public TableColumn members_col_status;
    @FXML
    public TableColumn members_col_email;

    @FXML
    public Label username;
    @FXML
    public Button dashboard_btn;
    @FXML
    public Button coaches_btn;
    @FXML
    public Button members_btn;
    @FXML
    public Button logout;
    @FXML
    public Button payment_btn;

    // **************** COACHES *****************
    public static int id;

    @FXML
    public TextField coaches_coachID;
    @FXML
    public TextField coaches_name;
    @FXML
    public TextField coaches_phoneNum;
    @FXML
    public Button coaches_createBtn;
    @FXML
    public Button coaches_updateBtn;
    @FXML
    public Button coaches_deleteBtn;
    @FXML
    public TableView<User> coaches_tableView;
    @FXML
    public TableColumn<User, Integer> coaches_col_coachID;
    @FXML
    public TableColumn<User, String> coaches_col_nom;
    @FXML
    public TableColumn<User, String> coaches_col_email;
    @FXML
    public TableColumn<User, String> coaches_col_telephone;
    @FXML
    public TableColumn<User, Boolean> coaches_col_etat;
    @FXML
    public TextField coaches_password;
    @FXML
    public ComboBox coaches_etat;
    @FXML
    public TextField coaches_telephone;
    @FXML
    public TextField coaches_email;
    public Button user_btn;
    public Button cours_btn;
    public Button evenement_btn;
    public Button produit_btn;
    public Button coach_btn;
    public Button reclamation_btn;

    public Label usernameAdmin;
    public Label usernameMembre;
    public Label usernameCoach;
    public Button Events_btn;
    public Button Produtcts_btn;
    public Button Reclamations_btn;
    public Button Cours_btn;
    public Button logoutMembre;
    public Button CoursCoach_btn;
    public Button membreCoach_btn;
    public Button logoutCoach;
    public Button Produits_btn;
    public Button eventsAdmin_btn;
    public Button coursAdmin_btn;
    public Button storeAdmin_btn;
    public Button profileAdmin_btn;
    public Button logout00_btn;
    public Button report_btn;
    public Button commande_btn;
    public Button offre_btn;
    public TableColumn coaches_col_action;
    public StackPane contentPlaceholder;

    public Button coaches_updateBtn1;
    public TextField confirm_password;
    public Button update_img_btn;
    @FXML
    public TextField profil_name;
    @FXML
    public TextField profil_email;
    @FXML
    public TextField profil_telephone;
    public TextField new_password;
    public TableView membres_tableView;
    public TableColumn membres_col_ID;
    public TableColumn membres_col_nom;
    public TableColumn membres_col_email;
    public TableColumn membres_col_telephone;
    public TableColumn membres_col_action;
    public TableColumn membres_col_etat;
    public Button addCoach_btn;


    User user ;

    public static String pathImage ;

    IServiceUser serviceUser00=new ServiceUser();
    private static CoachListController instance;
    public CoachListController() {
        instance = this;
    }

    public static CoachListController getInstance() {
        return instance;
    }



    public void consulterCoaches() {
        // Fetch all coaches
        List<User> coaches = serviceUser00.getAllCoaches();
        System.out.println("fetching coaches");
        // Clear the table
        coaches_tableView.getItems().clear();

        // Define how to populate the columns
        coaches_col_coachID.setCellValueFactory(new PropertyValueFactory<>("id"));
        coaches_col_nom.setCellValueFactory(new PropertyValueFactory<>("name"));
        coaches_col_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        coaches_col_telephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        coaches_col_etat.setCellFactory(tc -> new TableCell<User, Boolean>() {
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
                        User coach = getTableView().getItems().get(getIndex());
                        coach.setEtat(checkBox.isSelected());
                        serviceUser00.update(coach); // Assuming update() method updates the member in the database
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
        coaches_col_etat.setCellValueFactory(new PropertyValueFactory<>("etat"));

        //*************************************************************************************
        coaches_col_action.setCellFactory(param -> new TableCell<User, Void>() {
            final FontAwesomeIconView updateIcon = new FontAwesomeIconView(FontAwesomeIcon.PENCIL_SQUARE);
            final FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
            final HBox pane = new HBox(updateIcon, deleteIcon);

            {
                updateIcon.getStyleClass().add("update-icon");
                deleteIcon.getStyleClass().add("delete-icon");

                updateIcon.setOnMouseClicked(event -> {
//  Get the selected coach
                    User selectedCoach = coaches_tableView.getSelectionModel().getSelectedItem();

                    // Check if a row has been selected
                    if (selectedCoach != null) {
                        id = selectedCoach.getId();
                        System.out.println(id);
//                        coaches_list.setVisible(false);
//                        coaches_list.setManaged(false);

//                        coaches_form.setVisible(true);
//                        coaches_form.setManaged(true);

                        // Populate the form fields with the selected coach's details


                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/CoachesForm.fxml"));
                        try {
                            Parent root = loader.load();
                            CoachesFormController controller = loader.getController();
                            controller.SetId(id);
                            Scene scene = new Scene(root);

                            Stage stage = new Stage();
                            stage.setTitle("Modifier Coach");
                            stage.initModality(Modality.APPLICATION_MODAL);
                            // Ajustez la largeur selon vos besoins
                            // Set the height and width of the stage

                            stage.setScene(scene);
                            stage.showAndWait();

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }



                        /*coaches_name.setText(selectedCoach.getName());
                        coaches_email.setText(selectedCoach.getEmail());
                        coaches_telephone.setText(selectedCoach.getTelephone());
                        coaches_password.setText(selectedCoach.getPassword());

                        coaches_name.setEditable(true);
                        coaches_email.setEditable(true);
                        coaches_telephone.setEditable(true);
                        coaches_password.setEditable(true);*/


                        //     System.out.println(coaches_name.getText() + " " + coaches_email.getText() + " " + coaches_telephone.getText());
                    }
                });
                deleteIcon.setOnMouseClicked(event -> {
                    User selectedCoach = coaches_tableView.getSelectionModel().getSelectedItem();

                    // Create a confirmation dialog
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation Dialog");
                    alert.setHeaderText("Delete Coach");
                    alert.setContentText("Are you sure you want to delete this coach?");

                    // Show the dialog and wait for user's response
                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            // If user confirmed, delete the coach
                            serviceUser00.delete(selectedCoach.getId());

                            // Refresh the table
                            consulterCoaches();
                        }
                    });
                });
            }

            private void openCoachesForm(ActionEvent actionEvent) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/CoachesForm.fxml"));
                    Parent root = loader.load();

                    Scene scene = new Scene(root);

                    Stage stage = new Stage();
                    stage.setTitle("Add Coach");
                    stage.initModality(Modality.APPLICATION_MODAL);
                    // Ajustez la largeur selon vos besoins

                    stage.setScene(scene);
                    stage.showAndWait();
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
        // Add fetched coaches to the table
        coaches_tableView.getItems().addAll(coaches);
    }




    public void dalimtaahoussi(ActionEvent actionEvent) {

    }

    public void dali(ActionEvent actionEvent) {
    }

    public void goToAddForm(ActionEvent actionEvent) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        consulterCoaches();
        addCoach_btn.setOnAction(this::openCoachesForm);

    }

    private void openCoachesForm(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CoachesForm.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            Stage stage = new Stage();
            stage.setTitle("Add Coach");
            stage.initModality(Modality.APPLICATION_MODAL);
            // Ajustez la largeur selon vos besoins

            stage.setScene(scene);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void coachesSelect(MouseEvent mouseEvent) {
    }

    private void loadContent(String fxmlFileName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
            Parent content = loader.load();
            contentPlaceholder.getChildren().clear();
            contentPlaceholder.getChildren().add(content);
        } catch (IOException e) {
            e.printStackTrace();
            // Gérer l'erreur de chargement du fichier FXML
        }
    }
}
