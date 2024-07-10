package com.example.bty.Controllers.ProduitController;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.util.function.Consumer;

public class ActionButtonTableCell<S> extends TableCell<S, Void> {
    private final Button deleteButton = createButton("/com/example/bty/imagesModuleProduit/delete.png");
    private final Button editButton = createButton("/com/example/bty/imagesModuleProduit/edit.png");

    public ActionButtonTableCell(Consumer<S> deleteAction, Consumer<S> editAction) {
        deleteButton.setOnAction(event -> {
            S rowData = getTableRow().getItem();
            if (rowData != null) {
                deleteAction.accept(rowData);
            }
        });

        editButton.setOnAction(event -> {
            S rowData = getTableRow().getItem();
            if (rowData != null) {
                editAction.accept(rowData);
            }
        });

        setGraphic(createButtonBar());
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        setAlignment(Pos.CENTER);
    }




    private Button createButton(String iconPath) {
        Image image = new Image(getClass().getResourceAsStream(iconPath));
        Button button = new Button();
        button.setGraphic(new ImageView(image));
        button.getStyleClass().add("action-button");
        return button;
    }

    private HBox createButtonBar() {
        HBox buttonBar = new HBox(4);
        buttonBar.setAlignment(Pos.CENTER);
        buttonBar.getChildren().addAll(deleteButton, editButton);
        return buttonBar;
    }



    @Override
    protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);
        setGraphic(empty ? null : createButtonBar());
    }
}