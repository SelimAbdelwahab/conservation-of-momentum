import javafx.geometry.Bounds;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

public class Mass {
   private Circle img;
   private double mass;
   private double velocityX = 0;
   private double velocityY = 0;
   private double theta = 0;
   private Simulator sim;
   private boolean collided = false;

   public Mass(Simulator sim, Circle c, double m, double vx, double vy) {
      this.sim = sim;

      img = c;
      mass = m;

      velocityX = vx;
      velocityY = vy;

      theta = Math.atan2(vy, vx);
   }

   public double getMass() {
      return mass;
   }

   public void act() {
      img.setLayoutX(img.getLayoutX() + sim.getRealValue(velocityX));
      img.setLayoutY(img.getLayoutY() - sim.getRealValue(velocityY));

      if (Math.abs(Math.toDegrees(theta) - img.getRotate()) > 1) {
         img.setRotate(img.getRotate() + 1);
      }
   }

   public Bounds getBounds() {
      return img.getBoundsInParent();
   }

   public double getVelocityX() {
      return velocityX;
   }

   public double getVelocityY() {
      return velocityY;
   }

   public void setXVelocity(double vel2px) {
      velocityX = vel2px;
   }

   public void setYVelocity(double vel2py) {
      velocityY = vel2py;
   }

   public boolean getCollided() {
      return collided;
   }

   public void setCollided(boolean b) {
      collided = b;
   }

   public void setMass(double newValue) {
      mass = newValue;
   }

   public void setVelocity(double newValue) {
      velocityX = newValue * Math.cos(theta);
      velocityY = newValue * Math.sin(theta);
   }

   public void setAngle(double d, double vel) {
      img.setRotate(d);
      theta = d * Math.PI / 180;

      velocityX = vel * Math.cos(theta);
      velocityY = vel * Math.sin(theta);
   }

   public void setAngle(double d) {
      img.setRotate(d);
      theta = d * Math.PI / 180;
   }

   public void setLocation(int x, int y) {
      img.setLayoutX(x);
      img.setLayoutY(y);
   }

   public double getAngle() {
      return theta;
   }

   public Double getVelocity() {
      return Math.sqrt(Math.pow(velocityX, 2) + Math.pow(velocityY, 2));
   }
}
