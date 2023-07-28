/* *****************************************************************************
 *  Name:    Selim Abdelwahab
 *
 *  Description: This class is the Controller for the Sort scene. All the beauty happens in this class, sorting, animations, UI, pretty much the whole program.
 *
 *  Written:       23/11/2021
 *  Last updated:  26/11/2021
 **************************************************************************** */

import java.io.IOError;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

/**
 * Sort controller class
 */
public class Simulator {
   static final double SCALE = 0.5; // 1000m
   double multiplier = 1;

   @FXML
   HBox hb_container;

   @FXML
   StackPane sp_car1, sp_car2;

   @FXML
   Pane pn_car1, pn_car2;

   @FXML
   Circle circle_1, circle_2;

   @FXML
   ImageView img_arrow1, img_arrow2, img_home;

   @FXML
   Slider sldr_m1, sldr_v1, sldr_angle1, sldr_m2, sldr_v2, sldr_angle2;

   @FXML
   Label lbl_m1, lbl_v1, lbl_angle1, lbl_ke1, lbl_m2, lbl_v2, lbl_angle2, lbl_ke2;

   @FXML
   TextField tf_x1, tf_y1, tf_x2, tf_y2;

   @FXML
   Button btn_runSimulation;

   @FXML
   CheckBox cb_elastic;

   Mass car1, car2;

   Timeline timeline;
   boolean beganSimulation = false;
   boolean isElastic = true;

   Map<String, Double> dataBeforeRun1 = new HashMap<>();
   Map<String, Double> dataBeforeRun2 = new HashMap<>();

   boolean autopause = false;

   @FXML
   public void initialize() {
      car1 = new Mass(this, circle_1, 1, 0, 0);
      car2 = new Mass(this, circle_2, 1, 0, 0);

      circle_1.setOnMouseClicked(new EventHandler<MouseEvent>() {

         @Override
         public void handle(MouseEvent event) {
            showCar1Data();
         }

      });

      circle_1.setOnMouseDragged(new EventHandler<MouseEvent>() {
         @Override
         public void handle(MouseEvent event) {
            circle_1.setLayoutX(event.getSceneX());
            circle_1.setLayoutY(event.getSceneY());

            tf_x1.setText(Double.toString(circle_1.getLayoutX()));
            tf_y1.setText(Double.toString(circle_1.getLayoutY()));

            img_arrow1.setLayoutX(circle_1.getLayoutX() - img_arrow1.getFitWidth() / 2
                  + (100 + circle_1.getRadius() - 25) * Math.cos(Math.toRadians(img_arrow1.getRotate())));
            img_arrow1.setLayoutY(circle_1.getLayoutY() - img_arrow1.getFitHeight() / 2
                  + (100 + circle_1.getRadius() - 25) * Math.sin(Math.toRadians(img_arrow1.getRotate())));

            showCar1Data();
         }

      });

      sldr_m1.valueProperty().addListener(new ChangeListener<Number>() {

         @Override
         public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            car1.setMass((double) newValue);

            lbl_m1.setText(Double.toString((double) newValue));

            circle_1.setRadius(25 + (double) newValue);
            circle_1.setStrokeWidth((25 + (double) newValue) / 5);

            img_arrow1.setLayoutX(circle_1.getLayoutX() - img_arrow1.getFitWidth() / 2
                  + (100 + circle_1.getRadius() - 25) * Math.cos(car1.getAngle()));
            img_arrow1.setLayoutY(circle_1.getLayoutY() - img_arrow1.getFitHeight() / 2
                  + (100 + circle_1.getRadius() - 25) * Math.sin(car1.getAngle()));
         }

      });

      sldr_v1.valueProperty().addListener(new ChangeListener<Number>() {

         @Override
         public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            car1.setVelocity((double) newValue);

            lbl_v1.setText(Double.toString((double) newValue));
            double ek = 0.5 * car1.getMass() * Math.pow(((double) newValue), 2);

            lbl_ke1.setText(Double.toString(ek));

            // img_arrow1.setScaleX(1 + ((double) newValue / 25));
         }

      });

      sldr_angle1.valueProperty().addListener(new ChangeListener<Number>() {

         @Override
         public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            double angle = -Math.toRadians((double) newValue);

            car1.setAngle((double) newValue, sldr_v1.getValue());

            lbl_angle1.setText(Double.toString((double) newValue));
            img_arrow1.setRotate(-(double) newValue);

            img_arrow1.setLayoutX(circle_1.getLayoutX() - img_arrow1.getFitWidth() / 2
                  + (100 + circle_1.getRadius() - 25) * Math.cos(angle));
            img_arrow1.setLayoutY(circle_1.getLayoutY() - img_arrow1.getFitHeight() / 2
                  + (100 + circle_1.getRadius() - 25) * Math.sin(angle));
         }

      });

      circle_2.setOnMouseClicked(new EventHandler<MouseEvent>() {

         @Override
         public void handle(MouseEvent event) {
            showCar2Data();
         }

      });

      circle_2.setOnMouseDragged(new EventHandler<MouseEvent>() {

         @Override
         public void handle(MouseEvent event) {
            circle_2.setLayoutX(event.getSceneX());
            circle_2.setLayoutY(event.getSceneY());

            tf_x2.setText(Double.toString(circle_2.getLayoutX()));
            tf_y2.setText(Double.toString(circle_2.getLayoutY()));

            img_arrow2.setLayoutX(circle_2.getLayoutX() - img_arrow2.getFitWidth() / 2
                  + (100 + circle_2.getRadius() - 25) * Math.cos(Math.toRadians(img_arrow2.getRotate())));
            img_arrow2.setLayoutY(circle_2.getLayoutY() - img_arrow2.getFitHeight() / 2
                  + (100 + circle_2.getRadius() - 25) * Math.sin(Math.toRadians(img_arrow2.getRotate())));

            showCar2Data();
         }

      });

      sldr_m2.valueProperty().addListener(new ChangeListener<Number>() {

         @Override
         public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            car2.setMass((double) newValue);

            lbl_m2.setText(Double.toString((double) newValue));

            circle_2.setRadius(25 + (double) newValue);
            circle_2.setStrokeWidth((25 + (double) newValue) / 5);
         }

      });

      sldr_v2.valueProperty().addListener(new ChangeListener<Number>() {

         @Override
         public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            car2.setVelocity((double) newValue);

            lbl_v2.setText(Double.toString((double) newValue));
            double ek = 0.5 * car1.getMass() * Math.pow(((double) newValue), 2);

            lbl_ke2.setText(Double.toString(ek));
         }

      });

      sldr_angle2.valueProperty().addListener(new ChangeListener<Number>() {

         @Override
         public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            double angle = -Math.toRadians((double) newValue);

            car2.setAngle((double) newValue, sldr_v2.getValue());

            lbl_angle2.setText(Double.toString((double) newValue));

            img_arrow2.setRotate(-(double) newValue);

            img_arrow2.setLayoutX(circle_2.getLayoutX() - img_arrow2.getFitWidth() / 2
                  + (100 + circle_2.getRadius() - 25) * Math.cos(angle));
            img_arrow2.setLayoutY(circle_2.getLayoutY() - img_arrow1.getFitHeight() / 2
                  + (100 + circle_2.getRadius() - 25) * Math.sin(angle));
         }

      });

      cb_elastic.selectedProperty().addListener(new ChangeListener<Boolean>() {

         @Override
         public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            isElastic = newValue;
         }
      });

      timeline = new Timeline(new KeyFrame(Duration.millis(10), f -> {
         car1.act();
         img_arrow1.setLayoutX(circle_1.getLayoutX() - img_arrow1.getFitWidth() / 2
               + (100 + circle_1.getRadius() - 25) * Math.cos(car1.getAngle()));
         img_arrow1.setLayoutY(circle_1.getLayoutY() - img_arrow1.getFitHeight() / 2
               - (100 + circle_1.getRadius() - 25) * Math.sin(car1.getAngle()));

         car2.act();
         img_arrow2.setLayoutX(circle_2.getLayoutX() - img_arrow2.getFitWidth() / 2
               + (100 + circle_2.getRadius() - 25) * Math.cos(car2.getAngle()));
         img_arrow2.setLayoutY(circle_2.getLayoutY() - img_arrow2.getFitHeight() / 2
               - (100 + circle_2.getRadius() - 25) * Math.sin(car2.getAngle()));

         if (!car1.getCollided() && Math.sqrt(Math.pow(circle_1.getLayoutX() - circle_2.getLayoutX(), 2)
               + Math.pow(circle_1.getLayoutY() - circle_2.getLayoutY(), 2)) <= circle_1.getRadius()
                     + circle_2.getRadius()) {
            // calculate centre mass
            double theta = Math.atan2((circle_1.getLayoutY()) - (circle_2.getLayoutY()),
                  Math.abs(circle_2.getLayoutX() - circle_1.getLayoutX()));

            double mass1 = car1.getMass();
            double mass2 = car2.getMass();

            double vel1 = Math.sqrt(Math.pow(car1.getVelocityX(), 2) + Math.pow(car1.getVelocityY(), 2));
            double vel2 = Math.sqrt(Math.pow(car2.getVelocityX(), 2) + Math.pow(car2.getVelocityY(), 2));

            if (isElastic) {
               double v1primeX = ((vel1 * Math.cos(car1.getAngle() - theta) * (mass1 - mass2)
                     + 2 * mass2 * vel2 * Math.cos(car2.getAngle() - theta)) / (mass1 + mass2)) * Math.cos(theta)
                     + vel1 * Math.sin(car1.getAngle() - theta) * Math.cos(theta + Math.PI / 2);

               double v1primeY = ((vel1 * Math.cos(car1.getAngle() - theta) * (mass1 - mass2)
                     + 2 * mass2 * vel2 * Math.cos(car2.getAngle() - theta)) / (mass1 + mass2)) * Math.sin(theta)
                     + vel1 * Math.sin(car1.getAngle() - theta) * Math.sin(theta + Math.PI / 2);

               double v2primeX = ((vel2 * Math.cos(car2.getAngle() - theta) * (mass2 - mass1)
                     + 2 * mass1 * vel1 * Math.cos(car1.getAngle() - theta)) / (mass2 + mass1)) * Math.cos(theta)
                     + vel2 * Math.sin(car2.getAngle() - theta) * Math.cos(theta + Math.PI / 2);

               double v2primeY = ((vel2 * Math.cos(car2.getAngle() - theta) * (mass2 - mass1)
                     + 2 * mass1 * vel1 * Math.cos(car1.getAngle() - theta)) / (mass2 + mass1)) * Math.sin(theta)
                     + vel2 * Math.sin(car2.getAngle() - theta) * Math.sin(theta + Math.PI / 2);

               // if (circle_1.getLayoutY() > circle_2.getLayoutY())
               // v2primeY *= -1;
               car1.setXVelocity(v1primeX);
               car1.setYVelocity(v1primeY);

               car2.setXVelocity(v2primeX);
               car2.setYVelocity(v2primeY);

               // double v1primeX = vel1 - (2 * mass2) / (mass1 + mass2)

            } else {
               // m1v1 + m2v2 = (m1 + m2)v2
               // v2 = (m1v1 + m2v2) / (m1 + m2)
               double vel2px = (mass1 * vel1 * Math.cos(theta) + mass2 * vel2 * Math.sin(theta + Math.PI / 2))
                     / (mass1 + mass2);
               double vel2py = (mass1 * vel1 * Math.sin(theta) + mass2 * vel2 * Math.sin(theta + Math.PI / 2))
                     / (mass1 + mass2);

               car1.setXVelocity(vel2px);
               car2.setXVelocity(vel2px);

               car1.setYVelocity(vel2py);
               car2.setYVelocity(vel2py);
            }

            // car1.setYVelocity(-car1.getVelocityY());

            double car1V = Math.sqrt(Math.pow(car1.getVelocityX(), 2) + Math.pow(car1.getVelocityY(), 2));
            double car1ke = 0.5 * car1.getMass() * Math.pow(car1V, 2);
            double theta1 = Math.atan2(car1.getVelocityY(), car1.getVelocityX());

            sldr_v1.setValue(car1V);
            sldr_angle1.setValue(Math.toDegrees(theta1));

            lbl_v1.setText(Double.toString(App.round(car1V, 2)));
            lbl_ke1.setText(Double.toString(App.round(car1ke, 2)));
            lbl_angle1.setText(Double.toString(App.round(Math.toDegrees(theta1), 2)));

            double car2V = Math.sqrt(Math.pow(car2.getVelocityX(), 2) + Math.pow(car2.getVelocityY(), 2));
            double car2ke = 0.5 * car2.getMass() * Math.pow(car2V, 2);
            double theta2 = Math.atan2(car2.getVelocityY(), car2.getVelocityX());

            sldr_v2.setValue(car2V);
            sldr_angle2.setValue(Math.toDegrees(theta2));

            lbl_v2.setText(Double.toString(App.round(car2V, 2)));
            lbl_ke2.setText(Double.toString(App.round(car2ke, 2)));
            lbl_angle2.setText(Double.toString(App.round(Math.toDegrees(theta2), 2)));

            car1.setCollided(true);

            autopause = true;
            timeline.pause();
         }
      }));

      timeline.setCycleCount(Animation.INDEFINITE);
   }

   public double getRealValue(double val) {
      return (val * SCALE);
   }

   @FXML
   private void playSimulation() {
      timeline.play();

      if (!autopause) {
         dataBeforeRun1.clear();
         dataBeforeRun1.put("x", circle_1.getLayoutX());
         dataBeforeRun1.put("y", circle_1.getLayoutY());
         dataBeforeRun1.put("angle", car1.getAngle());
         dataBeforeRun1.put("mass", car1.getMass());
         dataBeforeRun1.put("velocity", car1.getVelocity());

         dataBeforeRun2.clear();
         dataBeforeRun2.put("x", circle_2.getLayoutX());
         dataBeforeRun2.put("y", circle_2.getLayoutY());
         dataBeforeRun2.put("angle", car2.getAngle());
         dataBeforeRun2.put("mass", car2.getMass());
         dataBeforeRun2.put("velocity", car2.getVelocity());
      }

      System.out.println("S1: " + dataBeforeRun1);
      System.out.println("S2: " + dataBeforeRun2);

   }

   @FXML
   private void restSimulation() {
      autopause = false;
      timeline.pause();

      car1.setCollided(false);

      sldr_m1.setValue(dataBeforeRun1.get("mass"));
      sldr_v1.setValue(dataBeforeRun1.get("velocity"));
      sldr_angle1.setValue(dataBeforeRun1.get("angle"));
      circle_1.setLayoutX(dataBeforeRun1.get("x"));
      circle_1.setLayoutY(dataBeforeRun1.get("y"));

      sldr_m2.setValue(dataBeforeRun2.get("mass"));
      sldr_v2.setValue(dataBeforeRun2.get("velocity"));
      sldr_angle2.setValue(dataBeforeRun2.get("angle"));
      circle_2.setLayoutX(dataBeforeRun2.get("x"));
      circle_2.setLayoutY(dataBeforeRun2.get("y"));

      img_arrow1.setLayoutX(circle_1.getLayoutX() - img_arrow1.getFitWidth() / 2
            + (100 + circle_1.getRadius() - 25) * Math.cos(car1.getAngle()));
      img_arrow1.setLayoutY(circle_1.getLayoutY() - img_arrow1.getFitHeight() / 2
            + (100 + circle_1.getRadius() - 25) * Math.sin(car1.getAngle()));

      img_arrow2.setLayoutX(circle_2.getLayoutX() - img_arrow2.getFitWidth() / 2
            + (100 + circle_2.getRadius() - 25) * Math.cos(car2.getAngle()));
      img_arrow2.setLayoutY(circle_2.getLayoutY() - img_arrow2.getFitHeight() / 2
            + (100 + circle_2.getRadius() - 25) * Math.sin(car2.getAngle()));

      System.out.println("R1: " + dataBeforeRun1);
      System.out.println("R2: " + dataBeforeRun2);

   }

   @FXML
   private void showCar1Data() {
      pn_car1.setVisible(true);
      pn_car2.setVisible(false);

      sp_car1.setViewOrder(1);
      sp_car2.setViewOrder(10);
   }

   @FXML
   private void showCar2Data() {
      pn_car2.setVisible(true);
      pn_car1.setVisible(false);

      sp_car2.setViewOrder(1);
      sp_car1.setViewOrder(10);
   }

   @FXML private void goToHome() throws IOException {
      App.setRoot("home");
   }
}