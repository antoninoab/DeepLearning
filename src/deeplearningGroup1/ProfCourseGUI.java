package deeplearningGroup1;

import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
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

/**
 * 
 * @author J.McGuire
 * The professor's GUI for each course
 */
public class ProfCourseGUI{
// Attributes
	private Course course;
	private Scene pcScene;
	private Pane pcPane;
	private Text studCtT;
	private Button nwEssyB, backB;
	private ToggleButton grdBkB, statsB;
	private int scale = 10;
	private int essayCt;
	private Group grdBkGroup, statsGroup, namesGroup;
	public Stage courseStage;
	private int Act, Bct, Cct, Dct, Fct;
	private Rectangle Arec, Brec, Crec, Drec, Frec;
	private Text At, Bt, Ct, Dt, Ft;
	
// Constructor
	/**
	 * @author J.McGuire
	 * @param courseID
	 * @param courseName
	 * @param course
	 * Creates the GUI for the professor for each course
	 */
	public ProfCourseGUI(int[] courseID, String courseName, Course course) { 
		// Initialize Attributes
		this.course = course;
		essayCt = 0;
		grdBkGroup = new Group();
		statsGroup = new Group();
		namesGroup = new Group();
		Act = 0;
		Bct = 0;
		Cct = 0;
		Dct = 0;
		Fct = 0;
		
		// Set up GUI
		pcPane = new Pane();
		Image image = new Image ("ERAUlogo.jpeg");
		pcPane.setBackground(new Background(new BackgroundImage(image,BackgroundRepeat.REPEAT,BackgroundRepeat.REPEAT,BackgroundPosition.DEFAULT,BackgroundSize.DEFAULT)));
		pcScene = new Scene(pcPane, 680, 680);
		courseStage = new Stage();
		courseStage.setScene(pcScene);
		courseStage.setTitle("" + courseName + " Homepage");
		
		// Call Methods
		displayCourseName(courseName);
		displayCourseID(courseID);
		displayStudentCount();
		gradeBookGUI();
		statsGUI();
		addButtons();
		
		// Make Buttons Work
		nwEssyB.setOnAction(e -> nwEssyWzrd());
		backB.setOnAction(e -> courseStage.close());
		
		statsB.setOnAction(e -> {grdBkB.setSelected(false);
								 grdBkGroup.setVisible(false);
								 statsGroup.setVisible(statsB.isSelected());
								});
		
		grdBkB.setOnAction(e -> {statsB.setSelected(false);
								 statsGroup.setVisible(false);
								 addGrdBkNames();
								 if (essayCt > 0) {
									 addGrades();
								 }
								 grdBkGroup.setVisible(grdBkB.isSelected());
								});
	}
	
// Methods
	
	/**
	 * @author J.McGuire
	 * @param void 
	 * @return void 
	 * Sets up the statistics GUI
	 */
	private void statsGUI() { 
		
		// Create background
		Rectangle background = new Rectangle(0, scale * 16, 680, 680 - 160);
		background.setFill(Color.WHITE);
		statsGroup.getChildren().add(background);
		
		// Create graph grid
		Line hL = new Line();
		hL.setStartX(3 * scale);
		hL.setStartY(680 - 3 * scale);
		hL.setEndX(680 - 3 * scale);
		hL.setEndY(680 - 3 * scale);
		statsGroup.getChildren().add(hL);
		Line vL = new Line();
		vL.setStartX(3 * scale);
		vL.setStartY(160 + 3 * scale);
		vL.setEndX(3 * scale);
		vL.setEndY(680 - 3 * scale);
		statsGroup.getChildren().add(vL);
		
		// x Axis Labels
		int xLabPad = 15;
		Text fLab = new Text("F");
		fLab.setWrappingWidth(124);
		fLab.setTextAlignment(TextAlignment.CENTER);
		fLab.setX(3 * scale);
		fLab.setY(680 - xLabPad);
		statsGroup.getChildren().add(fLab);
		Text dLab = new Text("D");
		dLab.setWrappingWidth(124);
		dLab.setTextAlignment(TextAlignment.CENTER);
		dLab.setX(3 * scale + 124);
		dLab.setY(680 - xLabPad);
		statsGroup.getChildren().add(dLab);
		Text cLab = new Text("C");
		cLab.setWrappingWidth(124);
		cLab.setTextAlignment(TextAlignment.CENTER);
		cLab.setX(3 * scale + 2 * 124);
		cLab.setY(680 - xLabPad);
		statsGroup.getChildren().add(cLab);
		Text bLab = new Text("B");
		bLab.setWrappingWidth(124);
		bLab.setTextAlignment(TextAlignment.CENTER);
		bLab.setX(3 * scale + 3 * 124);
		bLab.setY(680 - xLabPad);
		statsGroup.getChildren().add(bLab);
		Text aLab = new Text("A");
		aLab.setWrappingWidth(124);
		aLab.setTextAlignment(TextAlignment.CENTER);
		aLab.setX(3 * scale + 4 * 124);
		aLab.setY(680 - xLabPad);
		statsGroup.getChildren().add(aLab);
		
		// y axis labels
		int yGap = 92;
		Text hundredL = new Text("100");
		hundredL.setWrappingWidth(25);
		hundredL.setTextAlignment(TextAlignment.CENTER);
		hundredL.setX(5);
		hundredL.setY(190 + 0 * yGap);
		statsGroup.getChildren().add(hundredL);
		Text eightyL = new Text("80");
		eightyL.setWrappingWidth(25);
		eightyL.setTextAlignment(TextAlignment.CENTER);
		eightyL.setX(5);
		eightyL.setY(190 + 1 * yGap);
		statsGroup.getChildren().add(eightyL);
		Text sixtyL = new Text("60");
		sixtyL.setWrappingWidth(25);
		sixtyL.setTextAlignment(TextAlignment.CENTER);
		sixtyL.setX(5);
		sixtyL.setY(190 + 2 * yGap);
		statsGroup.getChildren().add(sixtyL);
		Text fortyL = new Text("40");
		fortyL.setWrappingWidth(25);
		fortyL.setTextAlignment(TextAlignment.CENTER);
		fortyL.setX(5);
		fortyL.setY(190 + 3 * yGap);
		statsGroup.getChildren().add(fortyL);
		Text twentyL = new Text("20");
		twentyL.setWrappingWidth(25);
		twentyL.setTextAlignment(TextAlignment.CENTER);
		twentyL.setX(5);
		twentyL.setY(190 + 4 * yGap);
		statsGroup.getChildren().add(twentyL);
		
		
		
		// Add dummy rectangles to initial setup
		Arec = new Rectangle();
		statsGroup.getChildren().add(Arec);
		Brec = new Rectangle();
		statsGroup.getChildren().add(Brec);
		Crec = new Rectangle();
		statsGroup.getChildren().add(Crec);
		Drec = new Rectangle();
		statsGroup.getChildren().add(Drec);
		Frec = new Rectangle();
		statsGroup.getChildren().add(Frec);
		
		// Initialize Counts
		Act = 0;
		Bct = 0;
		Cct = 0;
		Dct = 0;
		Fct = 0;
		At = new Text(" " + Act);
		Bt = new Text(" " + Bct);
		Ct = new Text(" " + Cct);
		Dt = new Text(" " + Dct);
		Ft = new Text(" " + Fct);
		statsGroup.getChildren().add(At);
		statsGroup.getChildren().add(Bt);
		statsGroup.getChildren().add(Ct);
		statsGroup.getChildren().add(Dt);
		statsGroup.getChildren().add(Ft);
		
		pcPane.getChildren().add(statsGroup);
		statsGroup.setVisible(false);
	}
	
	/**
	 * @author J.McGuire
	 * @param stats
	 * @return void 
	 * Updates the statistics GUI
	 */
	private void updateStats(int[] stats) {
		// Erase Old Graph
		statsGroup.getChildren().remove(Arec);
		statsGroup.getChildren().remove(Brec);
		statsGroup.getChildren().remove(Crec);
		statsGroup.getChildren().remove(Drec);
		statsGroup.getChildren().remove(Frec);
		
		statsGroup.getChildren().remove(At);
		statsGroup.getChildren().remove(Bt);
		statsGroup.getChildren().remove(Ct);
		statsGroup.getChildren().remove(Dt);
		statsGroup.getChildren().remove(Ft);
		
		// Update Grade Counts
		Fct += stats[0];
		Dct += stats[1];
		Cct += stats[2];
		Bct += stats[3];
		Act += stats[4];
		
		// Update Graph
		double oneSize = 4.6;
		int width = 124;
		
		Arec = new Rectangle();
		Arec.setWidth(width);
		Arec.setHeight(oneSize * Act);
		Arec.setX(30 + 4 * 124);
		Arec.setY(649 - oneSize * Act);
		Arec.setFill(Color.BLUE);
		statsGroup.getChildren().add(Arec);
		Brec = new Rectangle();
		Brec.setWidth(width);
		Brec.setHeight(oneSize * Bct);
		Brec.setX(30 + 3 * 124);
		Brec.setY(649 - oneSize * Bct);
		Brec.setFill(Color.GREEN);
		statsGroup.getChildren().add(Brec);
		Crec = new Rectangle();
		Crec.setWidth(width);
		Crec.setHeight(oneSize * Cct);
		Crec.setX(30 + 2 * 124);
		Crec.setY(649 - oneSize * Cct);
		Crec.setFill(Color.GOLD);
		statsGroup.getChildren().add(Crec);
		Drec = new Rectangle();
		Drec.setWidth(width);
		Drec.setHeight(oneSize * Dct);
		Drec.setX(30 + 124);
		Drec.setY(649 - oneSize * Dct);
		Drec.setFill(Color.ORANGE);
		statsGroup.getChildren().add(Drec);
		Frec = new Rectangle();
		Frec.setWidth(width);
		Frec.setHeight(oneSize * Fct);
		Frec.setX(30);
		Frec.setY(649 - oneSize * Fct);
		Frec.setFill(Color.RED);
		statsGroup.getChildren().add(Frec);
		
		At = new Text("" + Act);
		At.setWrappingWidth(width);
		At.setTextAlignment(TextAlignment.CENTER);
		At.setLayoutX(30 + 4 * 124);
		At.setLayoutY(649 - oneSize * Act);
		statsGroup.getChildren().add(At);
		Bt = new Text("" + Bct);
		Bt.setWrappingWidth(width);
		Bt.setTextAlignment(TextAlignment.CENTER);
		Bt.setLayoutX(30 + 3 * 124);
		Bt.setLayoutY(649 - oneSize * Bct);
		statsGroup.getChildren().add(Bt);
		Ct = new Text("" + Cct);
		Ct.setWrappingWidth(width);
		Ct.setTextAlignment(TextAlignment.CENTER);
		Ct.setLayoutX(30 + 2 * 124);
		Ct.setLayoutY(649 - oneSize * Cct);
		statsGroup.getChildren().add(Ct);
		Dt = new Text("" + Dct);
		Dt.setWrappingWidth(width);
		Dt.setTextAlignment(TextAlignment.CENTER);
		Dt.setLayoutX(30 + 124);
		Dt.setLayoutY(649 - oneSize * Dct);
		statsGroup.getChildren().add(Dt);
		Ft = new Text("" + Fct);
		Ft.setWrappingWidth(width);
		Ft.setTextAlignment(TextAlignment.CENTER);
		Ft.setLayoutX(30);
		Ft.setLayoutY(649 - oneSize * Fct);
		statsGroup.getChildren().add(Ft);
	}
	
	/**
	 * @author J.McGuire
	 * @param void 
	 * @return void
	 * Initializes the grade book GUI
	 */
	private void gradeBookGUI() { 
		// Create background
		Rectangle background = new Rectangle(0, scale * 16, 680, 680 - 160);
		background.setFill(Color.WHITE);
		grdBkGroup.getChildren().add(background);
		
		// Create Table Frame
		double vGap = 680 / 8;
		for(int i = 1; i < 8; i++) {
			Line vL = new Line();
			vL.setStartX(i * vGap);
			vL.setStartY(160);
			vL.setEndX(i * vGap);
			vL.setEndY(680);
			grdBkGroup.getChildren().add(vL);
		}
		
		double hGap = (680 - 160) / 25;
		for(int i = 1; i < 26; i++) {
			Line hL = new Line();
			hL.setStartX(0);
			hL.setStartY(160 + i * hGap);
			hL.setEndX(680);
			hL.setEndY(160 + i * hGap);
			grdBkGroup.getChildren().add(hL);
		}
		
		// Add dummy names to start
		grdBkGroup.getChildren().add(namesGroup);
		
		pcPane.getChildren().add(grdBkGroup);
		grdBkGroup.setVisible(false);
	}
	
	/**
	 * @author J.McGuire
	 * @param void
	 * @return void
	 * Adds grades to the gradebook
	 */
	private void addGrades() {
		double vgap = (680 - 160) / 25;
		double hgap = 680 / 8;
		for(int i = 0; i < 25; i++) {
			String grade = course.getGradei(i);
			Text gradeT = new Text("" + grade);
			gradeT.setWrappingWidth(680 / 8);
			gradeT.setTextAlignment(TextAlignment.CENTER);
			gradeT.setX(essayCt * hgap);
			gradeT.setY(i * vgap + (155 + 2 * vgap));
			grdBkGroup.getChildren().add(gradeT);
		}
	}
	
	/**
	 * @author J.McGuire
	 * @param void
	 * @return void
	 * Adds student names to gradebook
	 */
	private void addGrdBkNames() { 
		// Remove old names
		grdBkGroup.getChildren().remove(namesGroup);
		namesGroup = new Group();
		
		// Add new names to the namesGroup
		double vgap = (680 - 160) / 25;
		for (int i = 0; i < 25; i++) {
			String name = course.getStNamei(i);
			Text nameT = new Text("" + name);
			nameT.setWrappingWidth(680 / 8);
			nameT.setTextAlignment(TextAlignment.CENTER);
			nameT.setX(0);
			nameT.setY(i * vgap + (155 + 2 * vgap));
			namesGroup.getChildren().add(nameT);
		}
		
		// Add namesGroup to grdBkGroup
		grdBkGroup.getChildren().add(namesGroup);	
	}
	
	/**
	 * @author J.McGuire
	 * @param topic
	 * @return void
	 * Adds essay topics to gradebook
	 */
	private void topicToGrdBk(String topic) {
		pcPane.getChildren().remove(grdBkGroup);
		Text label = new Text("" + topic);
		label.setWrappingWidth(680 / 8);
		label.setTextAlignment(TextAlignment.CENTER);
		label.setX(essayCt * 680 / 8);
		label.setY(155 + (680 - 160) / 25);
		grdBkGroup.getChildren().add(label);
		pcPane.getChildren().add(grdBkGroup);
		grdBkGroup.setVisible(false); 
	}
	
	/**
	 * @author J.McGuire
	 * @param topic
	 * @return void
	 * Shows the professor that an essay is in session
	 */
	private void essayInSessionGUI(String topic) {
		// Remove New Essay Button
		pcPane.getChildren().remove(nwEssyB);
		
		Text eInProgress = new Text("" + topic + " essay is in session.");
		eInProgress.setFill(Color.RED);
		eInProgress.setWrappingWidth(678);
		eInProgress.setTextAlignment(TextAlignment.CENTER);
		eInProgress.setX(1);
		eInProgress.setY(scale * 8);
		pcPane.getChildren().add(eInProgress);
		
		Button endEssaySessionB = new Button("End " + topic + " Essay");
		endEssaySessionB.setLayoutX(scale * 1);
		endEssaySessionB.setLayoutY(scale * 13);
		pcPane.getChildren().add(endEssaySessionB);
		
		endEssaySessionB.setOnAction(e -> { course.setEssayStatus(false); 
											pcPane.getChildren().remove(eInProgress);
											pcPane.getChildren().remove(endEssaySessionB);
											addGrades();
											updateStats(course.countGrades());
											course.resetGrades();
											if (essayCt < 7) {
												pcPane.getChildren().add(nwEssyB);
											}
										  });
	}
	
	/**
	 * @author J.McGuire
	 * @param void
	 * @return void
	 * Allows the professor to create a new essay
	 */
	private void nwEssyWzrd() {
		// Create wizard
		Pane nwEssyP = new Pane();
		nwEssyP.setStyle("-fx-background-color: lightskyblue");
		Scene nwEssySc = new Scene(nwEssyP, 400, 300);
		Stage nwEssySt = new Stage();
		nwEssySt.setTitle("New Essay Creation Wizard");
		nwEssySt.setScene(nwEssySc);
		nwEssySt.show();
		
		// Add nodes
		int scale = 20; 	// scale for pane nodes;
		Label topicLabel = new Label("Essay Topic:");
		topicLabel.setLayoutX(3);
		topicLabel.setLayoutY(3);
		nwEssyP.getChildren().add(topicLabel);
		TextField topicInput = new TextField("Not Specified");
		topicInput.setLayoutX(5 * scale);
		topicInput.setLayoutY(1);
		nwEssyP.getChildren().add(topicInput);
		Button crtEssyB = new Button("Assign Essay");
		crtEssyB.setLayoutX(8 * scale);
		crtEssyB.setLayoutY(9 * scale);
		nwEssyP.getChildren().add(crtEssyB);
		
		Label minWordL = new Label("Minimum Word Count: ");
		minWordL.setLayoutX(3);
		minWordL.setLayoutY(2.3 * scale);
		nwEssyP.getChildren().add(minWordL);
		TextField minCtIn = new TextField("0");
		minCtIn.setLayoutX(8 * scale);
		minCtIn.setLayoutY(2 * scale);
		minCtIn.setPrefWidth(60);
		nwEssyP.getChildren().add(minCtIn);
		
		Label maxWordL = new Label("Maximum Word Count: ");
		maxWordL.setLayoutX(3);
		maxWordL.setLayoutY(3.8 * scale);
		nwEssyP.getChildren().add(maxWordL);
		TextField maxCtIn = new TextField("1");
		maxCtIn.setLayoutX(8 * scale);
		maxCtIn.setLayoutY(3.5 * scale);
		maxCtIn.setPrefWidth(60);
		nwEssyP.getChildren().add(maxCtIn);
		
		// Make wizard work
		crtEssyB.setOnAction(e -> { int minCt = Integer.parseInt(minCtIn.getCharacters().toString());
									int maxCt = Integer.parseInt(maxCtIn.getCharacters().toString());
									if (checkWordCount(minCt, maxCt) && !course.getEssayStatus()) {
										nwEssySt.close();
										course.createNewEssay(topicInput.getCharacters().toString(), minCt, maxCt);
										course.setEssayStatus(true); 	// tell course class there is an essay in session
										essayCt++; // update the number of essays in this course
										topicToGrdBk(topicInput.getCharacters().toString());
										essayInSessionGUI(topicInput.getCharacters().toString());
									} else if(!checkWordCount(minCt, maxCt)) {
										Text badCt = new Text("Invalid Word Count Parameters");
										badCt.setFill(Color.RED);
										badCt.setWrappingWidth(398);
										badCt.setTextAlignment(TextAlignment.CENTER);
										badCt.setX(1);
										badCt.setY(scale * 6);
										nwEssyP.getChildren().add(badCt);
									} 
								  });
	}
	
	/**
	 * @author J.McGuire
	 * @param minCt
	 * @param maxCt
	 * @return good
	 * Ensures the professor entered valid word count parameters
	 */
	private Boolean checkWordCount(int minCt, int maxCt) {
		Boolean good = true;
		// 1. Check max is larger than min
		if(minCt >= maxCt) {
			good = false;
		// 2. Check both are positive values
		} else if (minCt < 0) {
			good = false;
		}
		
		return good;
	}
	
	/**
	 * @author J.McGuire
	 * @param void
	 * @return void
	 * Adds basic buttons to course GUI
	 */
	private void addButtons() {
		// Add 3 vertical spaces for far left column
		nwEssyB = new Button("New Essay");
		nwEssyB.setLayoutX(scale * 1);
		nwEssyB.setLayoutY(scale * 1);
		pcPane.getChildren().add(nwEssyB);
		
		grdBkB = new ToggleButton("Gradebook"); 
		grdBkB.setLayoutX(scale * 1);
		grdBkB.setLayoutY(scale * 4);
		pcPane.getChildren().add(grdBkB);
		
		statsB = new ToggleButton("Statistics"); 
		statsB.setLayoutX(scale * 1);
		statsB.setLayoutY(scale * 7);
		pcPane.getChildren().add(statsB);
		
		backB = new Button("Exit Course");
		backB.setLayoutX(scale * 1);
		backB.setLayoutY(scale * 10);
		pcPane.getChildren().add(backB);
		
	}
	
	/**
	 * @author J.McGuire
	 * @param courseName
	 * @return void
	 * Displays the name of the course
	 */
	private void displayCourseName(String courseName) {
		Text title = new Text("" + courseName);
		//title.setFont(new Font("Impact", 60));
		title.setWrappingWidth(678);
		title.setTextAlignment(TextAlignment.CENTER);
		title.setX(1);
		title.setY(scale * 2);
		pcPane.getChildren().add(title);
	}
	
	/**
	 * @author J.McGuire
	 * @param void
	 * @return void
	 * Displays the number of students in the course
	 */
	private void displayStudentCount() {
		int count = course.getNumStudents();
		studCtT = new Text("There are currently " + count + " students in this course.");
		studCtT.setWrappingWidth(678);
		studCtT.setTextAlignment(TextAlignment.CENTER);
		studCtT.setX(1);
		studCtT.setY(scale * 6);
		pcPane.getChildren().add(studCtT);
	}
	
	/**
	 * @author J.McGuire
	 * @param courseID
	 * @return void
	 * Displays the ID of the course
	 */
	private void displayCourseID(int[] courseID) {
		Text text = new Text("ID: " + courseID[0] + courseID[1] + courseID[2] + courseID[3]);
		text.setWrappingWidth(678);
		text.setTextAlignment(TextAlignment.CENTER);
		text.setX(1);
		text.setY(scale * 4);
		pcPane.getChildren().add(text);
	}
	
// Setters & Getters
	
	public void updateStudentCount() {
		pcPane.getChildren().remove(studCtT);
		int count = course.getNumStudents();
		studCtT = new Text("There are currently " + count + " students in this course.");
		studCtT.setWrappingWidth(678);
		studCtT.setTextAlignment(TextAlignment.CENTER);
		studCtT.setX(1);
		studCtT.setY(scale * 6);
		pcPane.getChildren().add(studCtT);
	}
}