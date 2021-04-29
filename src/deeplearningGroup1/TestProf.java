package deeplearningGroup1;

import javafx.stage.Stage;
import javafx.application.Application;


public class TestProf extends Application{
	
	@Override
	public void start(Stage primaryStage) throws Exception{
		CourseList cl = new CourseList();
		ProfHomepage t = new ProfHomepage(cl);
		t.profStage.show();
		ProfStudComTest test = new ProfStudComTest(cl);
		// send the student GUI a copy of itself
		// this is crucial to the student being able to retrieve a
		// course from the course list made by the professors
		test.setStudent(test);
		test.testStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}