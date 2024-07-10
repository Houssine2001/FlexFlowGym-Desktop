package com.example.bty.Views;

import com.example.bty.Controllers.Coach.CoachController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Modality;

public class ViewFactory {
    // Coach Views
    private AnchorPane dashboardView;
    public ViewFactory() {
    }

    public AnchorPane getDashboardView() {
        if (dashboardView == null){
            try {
                dashboardView = new FXMLLoader(getClass().getResource("/Fxml/Coach/DashboardCoach.fxml")).load();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return dashboardView;
    }

    public void showLoginWindow() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/Login.fxml"));
        createStage(fxmlLoader);

    }

    public void showCoachWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Coach/Coach.fxml"));
        CoachController coachController = new CoachController();
        loader.setController(coachController);
           createStage(loader);
    }

    private void createStage(FXMLLoader loader) {
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (Exception e){
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Flex Flow");
        stage.show();

    }


}
