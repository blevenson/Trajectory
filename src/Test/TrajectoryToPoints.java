package Test;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;


public class TrajectoryToPoints {
	private static ArrayList<String> files = new ArrayList<String>();
	
	public static void main(String[] args){
		listFilesForFolder(new File("/Users/Blevenson/Desktop/Robotics/Trajectroy/"));
		
		for(String fileName : files){
		ArrayList<Double> list = new ArrayList<Double>();
		try {
			Scanner scan = new Scanner(new FileReader(new File("/Users/Blevenson/Desktop/Robotics/Trajectroy/" + fileName)));
			
			String pathName = scan.nextLine();
			System.out.println(pathName);  //Removes Trajectory name
			System.out.println(scan.nextLine());  //Removes number of points
			
			while(scan.hasNext()){
				for(int i = 0; i < 6; i++){
					scan.next();
				}
				list.add(scan.nextDouble());  //x
				list.add(scan.nextDouble());  //y
			}
			
			for(double i : list){
				System.out.println(i);
			}
			
			FileWriter fw = new FileWriter(new File("/Users/Blevenson/Desktop/Code/Octave/Files/" + pathName + ".txt"));
			
			for(int i = 0; i < list.size()-1;i += 2){
				fw.write(list.get(i) + " " + list.get(i+1) + "\n");
			}
			
			
			fw.close();
			scan.close();
			System.out.println("File Succesfully writen");
		} catch (Exception e) {
			System.out.println("File could not be found");
		}
		}
	}
	
	public static void listFilesForFolder(final File folder) {
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            listFilesForFolder(fileEntry);
	        } else {
	            files.add(fileEntry.getName());
	        }
	    }
	}
}
