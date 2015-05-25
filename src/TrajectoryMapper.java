import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

 
public class TrajectoryMapper extends Application {
 
	//https://vimeo.com/106757336
    @Override public void start(Stage stage) {
        stage.setTitle("Trajectory Mapper");
        
        Rectangle r = new Rectangle();
        r.setX(0);
        r.setY(0);
        r.setFill(Color.TRANSPARENT);
        r.setStroke(Color.BLACK);
        r.setWidth(200);
        r.setHeight(100);
        
        
        //Robot
        final Rectangle rectPath = new Rectangle (0, 0, 40, 40);
        //Rounds edges
        rectPath.setArcHeight(10);
        rectPath.setArcWidth(10);
        //Colors rectangle
        rectPath.setFill(Color.ORANGE);
        
        Path path = new Path();
        path.getElements().add(new MoveTo(20,20));
        path.getElements().add(new CubicCurveTo(380, 0, 380, 120, 200, 120));
        path.getElements().add(new CubicCurveTo(0, 120, 0, 240, 380, 240));
        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(4000));
        pathTransition.setPath(path);
        pathTransition.setNode(rectPath);
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.setCycleCount(Timeline.INDEFINITE);
        pathTransition.setAutoReverse(true);
        pathTransition.play();
        
        StackPane root = new StackPane();
        root.getChildren().add(rectPath);
        root.getChildren().add(r);
        stage.setScene(new Scene(root, 0, 0));
        
        stage.show();
    }
 
    public static void main(String[] args) {
        launch(args);
    }
}