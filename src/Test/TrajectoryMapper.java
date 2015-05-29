package Test;

import java.util.ArrayList;


//JavaFx
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

//Test
import Generator.Runner;

public class TrajectoryMapper extends Application {

	private double lastX = -1;
	private double lastY = -1;
	private boolean done = false;
	private boolean saveDone = false;
	private boolean nameAdded = false;

	private Button button;
	private Button saveButton;
	private Button submit;
	private final Rectangle rectPath = new Rectangle(0, 0, 40, 40);
	private Rectangle r;
	private TextField name;
	private Button run;
	private TextField cordinates;

	private ArrayList<Point> points = new ArrayList<Point>();
	private String pathName = "TestName";

	// Field
	// 27 feet tall
	// 26 feet wide

	// https://vimeo.com/106757336
	@Override
	public void start(Stage stage) {
		stage.setTitle("Trajectory Mapper");

		r = new Rectangle();
		r.setX(0);
		r.setY(0);
		r.setFill(Color.TRANSPARENT);
		r.setStroke(Color.BLACK);
		r.setWidth(650);
		r.setHeight(675);

		// Rounds edges
		rectPath.setArcHeight(10);
		rectPath.setArcWidth(10);
		// Colors rectangle
		rectPath.setFill(Color.ORANGE);

//		Path path = new Path();
//		path.getElements().add(new MoveTo(20, 20));
//		path.getElements().add(new CubicCurveTo(380, 0, 380, 120, 200, 120));
//		path.getElements().add(new CubicCurveTo(0, 120, 0, 240, 380, 240));
//		PathTransition pathTransition = new PathTransition();
//		pathTransition.setDuration(Duration.millis(4000));
//		pathTransition.setPath(path);
//		pathTransition.setNode(rectPath);
//		pathTransition
//				.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
//		pathTransition.setCycleCount(Timeline.INDEFINITE);
//		pathTransition.setAutoReverse(true);
//		pathTransition.play();

		// StackPane root = new StackPane();
		AnchorPane root = new AnchorPane();

		button = new Button("Pause Path");
		button.setLayoutX(750);
		button.setLayoutY(100);

		saveButton = new Button("Save Path");
		saveButton.setLayoutX(750);
		saveButton.setLayoutY(150);

		saveButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				if (nameAdded) {
					saveDone = !saveDone;
					if (saveDone) {
						saveButton.setText("Clear Path");
						lastX = lastY = -1;
						savePath();
					} else {
						saveButton.setText("Save Path");
						root.getChildren().clear();
						reDraw(root);
					}
				}else{
					System.out.println("Put name in folder");
					name.setText("Submit Name");
				}
			}
		});

		button.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				done = !done;
				if (done)
					button.setText("Continue Path");
				else
					button.setText("Pause Path");
			}
		});
		root.getChildren().add(button);
		root.getChildren().add(saveButton);

		/*
		 * 1. Fills an arrayList of Points every time you click on the screen -
		 * Need to modify the grid so that the scaling isn't so large 2. Have a
		 * button, that when pressed sends the new points arrayList, and the
		 * name of the new trajectory to the trajectory generator
		 */
		r.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click) {
				System.out.println(click.getSceneX() + "," + click.getSceneY());
				if (!done && !saveDone) {
					root.getChildren()
							.add(new Circle(click.getSceneX(), click
									.getSceneY(), 10));
					if (lastX != -1 && lastY != -1) {
						root.getChildren().add(
								new Line(lastX, lastY, click.getSceneX(), click
										.getSceneY()));
					}

					// Add point to list
					if (points.size() <= 10)
						points.add(new Point(click.getSceneX() / 25d, click
								.getSceneY() / 25d));
					else
						System.out
								.println("Can only take a maximum of 10 points");
					System.out.println(points);

					lastX = click.getSceneX();
					lastY = click.getSceneY();
				}
			}
		});
		
		cordinates = new TextField();
		cordinates.setLayoutX(750);
		cordinates.setLayoutY(250);
		
		r.setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click) {
				cordinates.setText("(" + click.getSceneX()/25d + "," + click.getSceneY()/25d + ")");
			}
		});

		name = new TextField();
		name.setLayoutX(750);
		name.setLayoutY(50);
		submit = new Button("Submit");
		submit.setLayoutX(850);
		submit.setLayoutY(50);

		submit.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				name.setEditable(false);
				pathName = name.getText();
				nameAdded = true;
			}
		});
		
		run = new Button("Run");
		run.setLayoutX(750);
		run.setLayoutY(200);
		
		run.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				ArrayList<Point> drawPoints = points;
				
				for(int i = 0; i < drawPoints.size(); i++){
					drawPoints.get(i).setX(drawPoints.get(i).getX() * 25d);
					drawPoints.get(i).setY(drawPoints.get(i).getY() * 25d);
				}
				
				Path path = new Path();
				
				path.getElements().add(new MoveTo(drawPoints.get(0).getX(), drawPoints.remove(0).getY()));
				while(drawPoints.size() > 0){
					path.getElements().add(new LineTo(drawPoints.get(0).getX(), drawPoints.remove(0).getY()));
				}
				/*
				path.getElements().add(new MoveTo(drawPoints.get(0).getX(), drawPoints.remove(0).getY()));
				while(drawPoints.size() >= 3){
					path.getElements().add(new CubicCurveTo(drawPoints.get(0).getX(), drawPoints.remove(0).getY(), 
							drawPoints.get(0).getX(), drawPoints.remove(0).getY(),
							drawPoints.get(0).getX(), drawPoints.remove(0).getY()));
				}
				while(drawPoints.size() > 0){
					path.getElements().add(new LineTo(drawPoints.get(0).getX(), drawPoints.remove(0).getY()));
				}
				*/
				PathTransition pathTransition = new PathTransition();
				pathTransition.setDuration(Duration.millis(4000));
				pathTransition.setPath(path);
				pathTransition.setNode(rectPath);
				pathTransition
						.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
				pathTransition.setCycleCount(Timeline.INDEFINITE);
				pathTransition.setAutoReverse(true);
				pathTransition.play();
			}
		});
		
		root.getChildren().add(run);
		root.getChildren().add(cordinates);

		root.getChildren().add(name);
		root.getChildren().add(submit);

		root.getChildren().add(rectPath);
		root.getChildren().add(r);

		stage.setScene(new Scene(root, 0, 0));
		stage.setWidth(900);
		stage.setHeight(700);
		stage.show();
	}

	protected void reDraw(AnchorPane root) {
		root.getChildren().add(button);
		root.getChildren().add(saveButton);
		root.getChildren().add(rectPath);
		root.getChildren().add(r);
		root.getChildren().add(name);
		name.setEditable(true);
		nameAdded = false;
		root.getChildren().add(submit);
		root.getChildren().add(run);
		root.getChildren().add(cordinates);

	}

	protected void savePath() {
		pathName = pathName.replaceAll(" ", "");
		Runner.points = points;
		Runner.name = pathName;
		Runner.main(new String[0]);
		System.out.println("Path Saved");
		points.clear();

	}

	public static void main(String[] args) {
		launch(args);
	}

	public class Point {
		private double x;
		private double y;

		public Point() {
			this(0d, 0d);
		}

		public Point(double spot) {
			this(spot, spot);
		}

		public Point(double x, double y) {
			this.x = x;
			this.y = y;
		}

		public double getX() {
			return x;
		}

		public void setX(double x) {
			this.x = x;
		}

		public double getY() {
			return y;
		}

		public void setY(double y) {
			this.y = y;
		}

		public String toString() {
			return "(" + x + "," + y + ")";
		}
	}
}