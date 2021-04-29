package deeplearningGroup1;

import javafx.scene.control.TextField;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * The homepage of the professor GUI
 * @author J.McGuire
 * 
 */
public class ProfHomepage{
	
	public Stage profStage; 	
	private Scene scene;
	private Pane homePane;
	private Button nwClssB, eraseB;
	private int numClasses = 0;
	private int scale = 10;
	private CourseList cList;
	
// Constructor
	/**
	 * Creates the professor homepage GUI
	 * @author J.McGuire
	 * @param cList
	 * 
	 */
	public ProfHomepage(CourseList cList){
		// Initialize attributes
		this.cList = cList;
		
		// Create background
		homePane = new Pane();
		Image image = new Image ("ERAUlogo.jpeg");
		homePane.setBackground(new Background(new BackgroundImage(image,BackgroundRepeat.REPEAT,BackgroundRepeat.REPEAT,BackgroundPosition.DEFAULT,BackgroundSize.DEFAULT)));
		
		// Create Buttons
		nwClssB = new Button("Create New Section");
		nwClssB.setLayoutX(scale * 1);
		nwClssB.setLayoutY(scale * 2);
		homePane.getChildren().add(nwClssB);
		
		eraseB = new Button("End Semester");
		eraseB.setLayoutX(scale * 1);
		eraseB.setLayoutY(scale * 63);
		homePane.getChildren().add(eraseB);
		
		// Place scene in stage
		scene = new Scene(homePane, 680, 680);
		profStage = new Stage();
		profStage.setTitle(" Welcome Professor ");
		profStage.setScene(scene);
		
		// Assign actions to buttons
		nwClssB.setOnAction(e -> { if(numClasses < 9) {
								   	 createCourseWizard();
								   }
								 });
		eraseB.setOnAction(e -> endSemester());
	}
	
// Methods
	/**
	 * Erases all courses the professor has made
	 * @author J.McGuire
	 * @param void
	 * @return void 
	 * 
	 */
	private void endSemester() {
		homePane.getChildren().clear();
		numClasses = 0;
		
		nwClssB = new Button("Create New Section");
		nwClssB.setLayoutX(scale * 1);
		nwClssB.setLayoutY(scale * 2);
		homePane.getChildren().add(nwClssB);
				
		eraseB = new Button("End Semester");
		eraseB.setLayoutX(scale * 1);
		eraseB.setLayoutY(scale * 63);
		homePane.getChildren().add(eraseB);
	}
	
	/**
	 * Creates a new section for
	 * students to join.
	 * @author J.McGuire
	 * @param none
	 * @return void
	 * 
	 */
	private void createCourseWizard() {
		int scale = 20; 	// scale for pane nodes;
		// Create Section Creation Wizard
		Pane ncPane = new Pane();
		Scene ncScene = new Scene(ncPane, 300,100);
		Stage ncStage = new Stage();
		ncStage.setTitle("New Section Creation Wizard");
		ncStage.setScene(ncScene);
		ncStage.show();
		
		// Add nodes to wizard
		Label courseLabel = new Label("Course Name:");
		courseLabel.setLayoutX(3);
		courseLabel.setLayoutY(3);
		ncPane.getChildren().add(courseLabel);
		TextField secInput = new TextField("Course " + (numClasses + 1) + "");
		secInput.setLayoutX(5 * scale);
		secInput.setLayoutY(1);
		ncPane.getChildren().add(secInput);
		Button crtCourseB = new Button("Create");
		crtCourseB.setLayoutX(5 * scale);
		crtCourseB.setLayoutY(3 * scale);
		ncPane.getChildren().add(crtCourseB);
		
		// Make wizard work
		crtCourseB.setOnAction(e -> {ncStage.close();
								  	 numClasses++; 	
								  	 createCourseBttn(secInput.getCharacters().toString());
								  	});
	}
	
	
	/**
	 * Creates a button for a section.
	 * Assigns section ID to section.
	 * Allows user to open Course page.
	 * @author J.McGuire
	 * @param courseName
	 * @return void
	 * 
	 */
	private void createCourseBttn(String courseName) {
		int xCo;
		int yCo;
		if(numClasses <= 3) {
			xCo = (numClasses - 1) * 226;
			yCo = 136;
		} else if(numClasses <= 6) {
			xCo = (numClasses - 4) * 226;
			yCo = 136 + 125;
		} else {
			xCo = (numClasses - 7) * 226;
			yCo = 136 + 250;
		}
		Button courseB = new Button(courseName);
		courseB.setMinSize(225, 125);
		courseB.setLayoutX(xCo);
		courseB.setLayoutY(yCo);
		homePane.getChildren().add(courseB);
		// Generate section ID
		int[] courseID = new int[4];
		for(int i = 0; i < 4; i++) {
			courseID[i] = randomFrom(0,9);
		}
		// Creation of the course object
		Course course = new Course(courseID, courseName);
		// Add course to course list for student to join
		cList.newCourse(course);
		// Make the course GUI 
		ProfCourseGUI courseGUI = new ProfCourseGUI(courseID, courseName, course);   
		// go to course GUI when button clicked
		courseB.setOnAction(e -> { courseGUI.updateStudentCount();
								   courseGUI.courseStage.show();
								 }); 
	}
	
	
	// Random Number Generator
	private int randomFrom(int low, int high) {
		int randNum = 0;
		randNum = (int)(Math.random() * (high - low) + low);
		return randNum;
	}
	
}