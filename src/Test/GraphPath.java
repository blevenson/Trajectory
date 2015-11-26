package Test;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class GraphPath extends Application {
	private Desktop desktop = Desktop.getDesktop();
	final NumberAxis xAxis = new NumberAxis();
	final NumberAxis yAxis = new NumberAxis();
	final LineChart<Number, Number> lineChart = new LineChart<Number, Number>(
			xAxis, yAxis);

	@Override
	public void start(Stage stage) {
		stage.setTitle("Trajectory Path");
		// defining the axes
		// final NumberAxis xAxis = new NumberAxis();
		// final NumberAxis yAxis = new NumberAxis();
		yAxis.setLabel("");
		xAxis.setLabel("Length");
		// creating the chart
		// final LineChart<Number,Number> lineChart =
		// new LineChart<Number,Number>(xAxis,yAxis);

		//Get Directory
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("Open Trajectory Folder");
		directoryChooser.setInitialDirectory(new File("ProcessedTrajectories/"));
		
		ObservableList<String> options = FXCollections.observableArrayList(getFiles(directoryChooser.showDialog(stage)));
		final ComboBox<String> comboBox = new ComboBox<String>(options);
		comboBox.setValue("Center Lane Path Far");
		comboBox.setTooltip(new Tooltip("Select Path"));

		comboBox.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
				updateGraph(newValue);
			}
		});

		System.out.println(comboBox.getValue());
		while (comboBox.getValue() == null) {
			System.out.println("Looping");
		}
		File file = new File(getFilePath(new File(
				"ProcessedTrajectories/"),
				comboBox.getValue()));
		// FileChooser fileChooser = new FileChooser();
		// fileChooser.setTitle("Open Trajectory File");
		//
		// File file = fileChooser.showOpenDialog(stage);

		XYChart.Series leftSeries = new XYChart.Series();
		XYChart.Series rightSeries = new XYChart.Series();

		try {
			Scanner scan = new Scanner(file);
			lineChart
					.setTitle("Trajectory Path: " + spaceCaps(scan.nextLine()));
			int size = scan.nextInt();

			leftSeries.setName("Left Motors");
			rightSeries.setName("Right Motors");

			for (int i = 0; i < size; i++) {
				leftSeries.getData().add(new XYChart.Data(scan.nextDouble(), scan.nextDouble()));
			}
			for (int i = 0; i < size; i++) {
				// System.out.println(scan.nextInt() + ", " + scan.nextInt());
				rightSeries.getData().add(new XYChart.Data(scan.nextDouble(), scan.nextDouble()));
			}

			scan.close();
		} catch (FileNotFoundException e) {
			System.out.println("Failed to read file");
		}

		GridPane grid = new GridPane();
		grid.add(comboBox, 10, 5);
		grid.add(lineChart, 0, 10);

		// Scene scene = new Scene(lineChart,800,600);
		Scene scene = new Scene(grid, 800, 600);
		lineChart.getData().add(leftSeries);
		lineChart.getData().add(rightSeries);

		stage.setScene(scene);
		stage.show();
	}

	protected void updateGraph(String newValue) {
		File file = new File(getFilePath(new File(
				"ProcessedTrajectories/"),
				newValue));

		XYChart.Series leftSeries = new XYChart.Series();
		XYChart.Series rightSeries = new XYChart.Series();

		try {
			Scanner scan = new Scanner(file);
			lineChart
					.setTitle("Trajectory Path: " + spaceCaps(scan.nextLine()));
			int size = scan.nextInt();
			leftSeries.setName("Left Motors");
			rightSeries.setName("Right Motors");

			for (int i = 0; i < size; i++) {
				leftSeries.getData().add(
						new XYChart.Data(scan.nextDouble(), scan.nextDouble()));
			}
			for (int i = 0; i < size; i++) {
				// System.out.println(scan.nextInt() + ", " + scan.nextInt());
				rightSeries.getData().add(
						new XYChart.Data(scan.nextDouble(), scan.nextDouble()));
			}

			scan.close();
		} catch (FileNotFoundException e) {
			System.out.println("Failed to read file");
		}
		// Removing this stacks the plots
		lineChart.getData().clear();

		lineChart.getData().add(leftSeries);
		lineChart.getData().add(rightSeries);

	}

	public static void main(String[] args) {
		launch(args);
	}

	private void openFile(File file) {
		try {
			desktop.open(file);
		} catch (IOException ex) {
			System.out.println("Error");
		}
	}

	/*
	 * Turns ThisIsACamelCaseWord into This Is A Camel Case Word
	 */
	private String spaceCaps(String in) {
		String out = "" + in.charAt(0);
		for (int i = 1; i < in.length(); i++) {
			int c = (int) in.charAt(i);
			if (c >= 65 && c <= 90) {
				out += " ";
			}
			out += (char) c;
		}
		return out;
	}

	private String reverseSpaceCaps(String in) {
		return in.replaceAll(" ", "") + ".txt";
	}

	private ArrayList<String> getFiles(String path) {
		ArrayList<String> out = new ArrayList<String>();
		File folder = new File(path);
		for (File in : folder.listFiles()) {
			if (in.isDirectory()) {
				out.addAll(getFiles(in.getAbsolutePath()));
			} else {
				out.add(spaceCaps(in.getName().substring(0,
						in.getName().length() - 4)));
			}
		}
		return out;
	}
	
	private ArrayList<String> getFiles(File folder) {
		ArrayList<String> out = new ArrayList<String>();
		for (File in : folder.listFiles()) {
			if (in.isDirectory()) {
				out.addAll(getFiles(in.getAbsolutePath()));
			} else {
				out.add(spaceCaps(in.getName().substring(0,
						in.getName().length() - 4)));
			}
		}
		return out;
	}

	private String getFilePath(final File folder, String name) {
		if (!name.contains(".txt"))
			name = reverseSpaceCaps(name);
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				getFilePath(fileEntry, name);
			} else {
				if (fileEntry.getName().equals(name)) {
					return fileEntry.getAbsolutePath();
				}
			}
		}
		return null;
	}
}