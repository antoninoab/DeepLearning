package deeplearningGroup1;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ProfStudComTest{
// Attributes
	private Pane testPane;
	private Scene testScene;
	public Stage testStage;
	private Button joinB;
	private ProfStudComTest student;
	
// Constructor
	ProfStudComTest(CourseList cl){
		testPane = new Pane();
		joinB = new Button("Join Course");
		testPane.getChildren().add(joinB);
		
		// Make buttons work
		joinB.setOnAction(e -> { cl.setStudent(student); 
								 cl.clStage.show(); 
							   });
		
		// Create stage
		testScene = new Scene(testPane, 680, 680);
		testStage = new Stage();
		testStage.setTitle(" Student Test ");
		testStage.setScene(testScene);
	}
	
// Methods
	public void addCourse(Course course, int studNum) { 	// TODO must send studNum and course object to student 
		createCourseButton(course, studNum);				// create a new button for the course they just joined
	}
	
	private void createCourseButton(Course course, int studNum) { 		// TODO used to create button corresponding to course
		Button gradeEssyB = new Button("Grade " + course.getName() + " essay");
		gradeEssyB.setLayoutX(340);
		gradeEssyB.setLayoutY(340);
		testPane.getChildren().add(gradeEssyB);
		gradeEssyB.setOnAction(e -> { if(course.getEssayStatus()) {		// show that there is an essay assigned
										course.setGradei(studNum, "C"); // TODO send the grade to the course object so the professor can see it
									  }
									});
	}
	
	public void setStudent(ProfStudComTest student) {
		this.student = student;
	}
	
}