package deeplearningGroup1;

import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

// package deeplearningGroup1;

/**
* The list of available courses for the student
* @author J.McGuire
* 
*/

public class CourseList{
// Attributes
	private ScrollPane sPane;
	private Pane pane;
	private Scene scene;
	public Stage clStage;
	private int numCourses;
	private int[] subID;
	private ProfStudComTest student;
	
// Constructor
	/**
	 * Creates initial course list object
	 * @param void
	 * 
	 */
	public CourseList() {
		// Initialize Attributes
		numCourses = 0;
		
		// Create background
		sPane = new ScrollPane();
		pane = new Pane();
		Image image = new Image ("ERAUlogo.jpeg");
		pane.setBackground(new Background(new BackgroundImage(image,BackgroundRepeat.REPEAT,BackgroundRepeat.REPEAT,BackgroundPosition.DEFAULT,BackgroundSize.DEFAULT)));
		sPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		sPane.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
		sPane.setContent(pane);
		
		// Create scene and stage
		scene = new Scene(pane, 400, 300);
		clStage = new Stage();
		clStage.setScene(scene);
		clStage.setTitle("Available Courses");
	}
	
//Public  Methods
	
	/**
	 * Allows the professor to add a course to the catalog
	 * @param course
	 * 
	 */
	public void newCourse(Course course) {
		// Create the button for the course list
		Button courseB = new Button("" + course.getName());
		courseB.setPrefSize(400, 30);
		courseB.setLayoutX(0);
		courseB.setLayoutY(30 * numCourses);
		pane.getChildren().add(courseB);
		numCourses++;
		
		// Make the button work
		courseB.setOnAction(e -> { clStage.close();
								   joinCourseWizard(course);
								 });
	}
	
	/**
	 * Lets the list know which student is trying to add a course
	 * @param student
	 * 
	 */
	public void setStudent(ProfStudComTest student) {
		this.student = student;
	}
	
	
// Private Methods
	
	/**
	 * Shows join course wizard to student
	 * @param course
	 * 
	 */
	private void joinCourseWizard(Course course) {
		// Make join course window
		int scale = 20; 	// scale for pane nodes;
		// Create join course wizard
		Pane jpane = new Pane();
		Scene jscene = new Scene(jpane, 300,150);
		Stage jstage = new Stage();
		jstage.setTitle("Join Course Wizard");
		jstage.setScene(jscene);
		jstage.show();
				
		// Add nodes to wizard
		Label nameLabel = new Label("Your Name:");
		nameLabel.setLayoutX(3);
		nameLabel.setLayoutY(3);
		jpane.getChildren().add(nameLabel);
		TextField nameInput = new TextField();
		nameInput.setLayoutX(5 * scale);
		nameInput.setLayoutY(1);
		jpane.getChildren().add(nameInput);
				
		Label secIDL = new Label("Course ID: ");
		secIDL.setLayoutX(3);
		secIDL.setLayoutY(scale + 15);
		jpane.getChildren().add(secIDL);
		TextField secIn1 = new TextField();
		secIn1.setMaxWidth(25);
		secIn1.setLayoutX(100);
		secIn1.setLayoutY(30);
		jpane.getChildren().add(secIn1);
		TextField secIn2 = new TextField();
		secIn2.setMaxWidth(25);
		secIn2.setLayoutX(130);
		secIn2.setLayoutY(30);
		jpane.getChildren().add(secIn2);
		TextField secIn3 = new TextField();
		secIn3.setMaxWidth(25);
		secIn3.setLayoutX(160);
		secIn3.setLayoutY(30);
		jpane.getChildren().add(secIn3);
		TextField secIn4 = new TextField();
		secIn4.setMaxWidth(25);
		secIn4.setLayoutX(190);
		secIn4.setLayoutY(30);
		jpane.getChildren().add(secIn4);
				
		Button jCourseB = new Button("Join");
		jCourseB.setLayoutX(5 * scale);
		jCourseB.setLayoutY(3 * scale);
		jpane.getChildren().add(jCourseB);
				
		// Make wizard work
		jCourseB.setOnAction(e -> {  subID = new int[4];
									 subID[0] = Integer.parseInt(secIn1.getCharacters().toString());
									 subID[1] = Integer.parseInt(secIn2.getCharacters().toString());
									 subID[2] = Integer.parseInt(secIn3.getCharacters().toString());
									 subID[3] = Integer.parseInt(secIn4.getCharacters().toString());
									 if (course.checkID(subID) && course.getNumStudents() < 25) {
										 jstage.close();
										 course.setStNamei(course.getNumStudents(), nameInput.getCharacters().toString()); 
										 giveStudentCourse(course);
									 } else if (course.getNumStudents() >= 25) {
										 Text fullT = new Text("Course is Full"); 
										 fullT.setWrappingWidth(300);
										 fullT.setTextAlignment(TextAlignment.CENTER);
										 fullT.setFill(Color.RED);
										 fullT.setX(0);
										 fullT.setY(140);
										 jpane.getChildren().add(fullT);
									 } else {
										Text wrongIDT = new Text("Incorrect Course ID Entered"); 
										wrongIDT.setWrappingWidth(300);
										wrongIDT.setTextAlignment(TextAlignment.CENTER);
										wrongIDT.setFill(Color.RED);
										wrongIDT.setX(0);
										wrongIDT.setY(140);
										jpane.getChildren().add(wrongIDT);
									 }
								  });
	}
	
	/**
	 * Gives the correct course object to the student
	 * @param course
	 * 
	 */
	private void giveStudentCourse(Course course) {
		student.addCourse(course, course.getNumStudents());
		course.updateStuCt();
	}
	
}