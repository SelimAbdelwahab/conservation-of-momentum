/* *****************************************************************************
 *  Name:    Selim Abdelwahab
 *
 *  Description: This class is the Controllor for the home screen. Responsible for minor calculations and switching to the next scene.
 *
 *  Written:       23/11/2021
 *  Last updated:  26/11/2021
 **************************************************************************** */

import java.io.IOException;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 * Home controller class, handles all events and logic for the home screen.
 */
public class HomeController {
   @FXML
   StackPane sp_root;

   @FXML
   HBox hb_container;

   @FXML
   Button btn_simulate;

   @FXML
   /**
    * This method will call switch to sort with the correct algorithim name.
    * 
    * @throws IOException Will throw an error if the FXML file is unable to load.
    */
   void loadSimulator() throws IOException {
      Parent nextScene = App.loadFXML("simulator");
      sp_root.getChildren().add(nextScene);
      nextScene.translateYProperty().set(sp_root.getScene().getHeight());

      // Create a timeline
      Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(1), new KeyValue(nextScene.translateYProperty(), 0, Interpolator.EASE_BOTH)),
            new KeyFrame(Duration.seconds(1),
                  new KeyValue(hb_container.translateYProperty(), -sp_root.getHeight(), Interpolator.EASE_BOTH)));

      // Play ani
      timeline.play();

      timeline.setOnFinished((e) -> {
         try {
            App.setRoot("simulator");
         } catch (IOException err) {
            err.printStackTrace();
         }
      });

      timeline.play();
   }

}