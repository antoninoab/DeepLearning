package deeplearningGroup1;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class StudentHomePage {
	
	StudentAccount student=new StudentAccount(); //writing essays 
	private Jarvis jarvis;
	
	public Stage fifthOne;
	private Scene scene3;
	//private GridPane pane1;
	private Button Number1;
	private int numClasses = 0;
	private int scale = 10;
	//private TextField enterEssayTopic;
	//private TextArea enterTextField;
	//private Text essayTopic, enterText;
	//private Jarvis jarvis;
	//public Stage fourthOne;
	private Scene scene5;
	private Pane pane5;
	private BorderPane borderPane1;
	public StudentHomePage() {
		
	}
	
	
	public void StudentHomePageStarting()
	{
		/**
		 * Adding a menu bar to the homepage
		 */
		
		
		
		//***************************************************************
		
		jarvis = new Jarvis();
		Image image2 = new Image ("redbackground.jpg");
		//***********************Buttons**********************************
		Button Button1 = new Button("Courses");
		Button Course1 = new Button("Course One");
		Button Course2 = new Button("Course Two");
		Button Course3 = new Button("Course Three");
		
		StackPane stackPane1 = new StackPane();
		StackPane stackPane2 = new StackPane();
		StackPane stackPane3 = new StackPane();
		
		stackPane1.getChildren().addAll(Course1);
		stackPane2.getChildren().addAll(Course2);
		stackPane3.getChildren().addAll(Course3);
		
		Button1.setMinHeight(100);
		Button1.setMinWidth(100);
		Button1.setOnAction(e1->{
			
			Course1.setMinHeight(100);
			Course1.setMinWidth(100);
			
			
			Course2.setMinHeight(100);
			Course2.setMinWidth(100);
			
			
			Course3.setMinHeight(100);
			Course3.setMinWidth(100);
			
			ToolBar toolbarMenuCourses = new ToolBar();
			toolbarMenuCourses.setStyle("-fx-background-color: transparent;");
			toolbarMenuCourses.setBackground(new Background(new BackgroundImage(image2,BackgroundRepeat.REPEAT,BackgroundRepeat.REPEAT,BackgroundPosition.DEFAULT,BackgroundSize.DEFAULT)));
			toolbarMenuCourses.getItems().add(stackPane1);
			toolbarMenuCourses.getItems().add(stackPane2);
			toolbarMenuCourses.getItems().add(stackPane3);
			toolbarMenuCourses.setOrientation(Orientation.VERTICAL);
			borderPane1.setCenter(toolbarMenuCourses);
		});	
		/**
		 * @Courses and Stuff adding a menu bar to the homepage
		 */
		Label gradeCourseOne = new Label("A");
		Label gradeCourseTwo = new Label("C");
		Label gradeCourseThree = new Label("C-");
		ToggleButton Button2 = new ToggleButton("Grades");
		
		
			Button2.setOnAction(e2->{
				
			boolean isSelected = Button2.isSelected();
			if (isSelected == true)
			{
			
		
			gradeCourseOne.setPadding(new Insets(85,10,0.0, 0));
			gradeCourseTwo.setPadding(new Insets(85,10,0.0, 0));
			gradeCourseThree.setPadding(new Insets(85,10,0.0, 0));
			stackPane1.getChildren().addAll(gradeCourseOne);
			stackPane2.getChildren().addAll(gradeCourseTwo);
			stackPane3.getChildren().addAll(gradeCourseThree);
			
			
			ToolBar toolbarMenuCourses = new ToolBar();
			toolbarMenuCourses.setStyle("-fx-background-color: transparent;");
			toolbarMenuCourses.setBackground(new Background(new BackgroundImage(image2,BackgroundRepeat.REPEAT,BackgroundRepeat.REPEAT,BackgroundPosition.DEFAULT,BackgroundSize.DEFAULT)));
			toolbarMenuCourses.getItems().add(stackPane1);
			toolbarMenuCourses.getItems().add(stackPane2);
			toolbarMenuCourses.getItems().add(stackPane3);
			toolbarMenuCourses.setOrientation(Orientation.VERTICAL);
			borderPane1.setCenter(toolbarMenuCourses);
	
		} else if(isSelected == false)
		{
			stackPane1.getChildren().removeAll(gradeCourseOne);
			stackPane2.getChildren().removeAll(gradeCourseTwo);
			stackPane3.getChildren().removeAll(gradeCourseThree);
		}
			});
		Button2.setMinHeight(20);
		Button2.setMinWidth(100);
		MenuBar menuBar = new MenuBar();
		Menu tools = new Menu("    Tools");
		
		MenuItem writteEssay = new MenuItem("Write Essays");
		writteEssay.setOnAction(e -> {
			
			StudentGUI studentGui = new StudentGUI(jarvis);
			studentGui.StudentBox();
		});
		MenuItem lookAtEssay = new MenuItem("Previous Graded Essays");
		lookAtEssay.setOnAction(e-> {
			
			ChoosingFiles Choose = new ChoosingFiles();
			Choose.ChoosingFiles2();
			
		});
		
		MenuItem StudentJoinCourse = new MenuItem("Join Course");
		StudentJoinCourse.setOnAction(e ->{
			
			CourseList cl = new CourseList();
			ProfHomepage t = new ProfHomepage(cl);
			ProfStudComTest test = new ProfStudComTest(cl);
			
			// send the student GUI a copy of itself
			// this is crucial to the student being able to retrieve a
			// course from the course list made by the professors
			test.setStudent(test);
			test.testStage.show();
			
		});
		tools.getItems().add(writteEssay);
		tools.getItems().add(lookAtEssay);
		tools.getItems().add(StudentJoinCourse);
		menuBar.getMenus().add(tools);
		menuBar.setMinHeight(20);
		menuBar.setMinWidth(100);
		//*********************************************************
		
			
		
		//**********************************************************
		ToolBar toolbarMenu = new ToolBar();
		toolbarMenu.setBackground(new Background(new BackgroundImage(image2,BackgroundRepeat.REPEAT,BackgroundRepeat.REPEAT,BackgroundPosition.DEFAULT,BackgroundSize.DEFAULT)));
		toolbarMenu.getItems().add(Button1);
		toolbarMenu.getItems().add(Button2);
		toolbarMenu.getItems().add(menuBar);
		toolbarMenu.setOrientation(Orientation.VERTICAL);
		borderPane1 = new BorderPane();
		borderPane1.setLeft(toolbarMenu);
		
		borderPane1.setBackground(new Background(new BackgroundImage(image2,BackgroundRepeat.REPEAT,BackgroundRepeat.REPEAT,BackgroundPosition.DEFAULT,BackgroundSize.DEFAULT)));
		
		scene5 = new Scene(borderPane1, 900, 600);
		fifthOne = new Stage();
		fifthOne.setTitle("Welcome"); 
		
		fifthOne.setScene(scene5);
		fifthOne.show();
		
		
		
		
	}
	
	
	

	public static void main(String[] args) {
	
	}

}
