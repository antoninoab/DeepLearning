package deeplearningGroup1;


/**
 * The course that both professor and student participate in
 * @author J.McGuire
 */
public class Course {
// Attributes
	int numStudents; 		// number of students enrolled in the course
	String topic; 			// topic of the essay for student GUI purposes
	String courseName; 		// name of the course
	int minWrdCt; 			// minimum word count for essay
	int maxWrdCt; 			// maximum word count for essay
	String grades[]; 		// array of length numStudents with all grades
	String stuNames[];		// array of student names
	int courseID[]; 		// 4 digit course ID number for students to join
	Boolean essayStatus; 	// boolean value that essay is in progress
	
// Constructor
	/**
	 * Professor constructs a new course that students can join
	 * @param courseID
	 * @param courseName
	 * 
	 */
	public Course(int[] courseID, String courseName) {
		// Initialize Attributes
		numStudents = 0; 	// initially there are no students in a course
		essayStatus = false; 	// initially there is no essay
		grades = new String [25];
		stuNames = new String [25];
		this.courseName = courseName;
		
		// Call Methods
		initializeID(courseID);
		initializeNames();
		resetGrades();
	}
	
// Methods
	
	/**
	 * Counts the number of each grade given by Jarvis
	 * @return counts
	 *
	 */
	public int[] countGrades() {
		int[] counts = new int [5];
		
		for (int n = 0; n < 5; n++) {
			counts[n] = 0;
		}
		
		for(int i = 0; i < 25; i++) {
			if(grades[i] == "A") {
				counts[4]++;
			} else if (grades[i] == "B") {
				counts[3]++;
			} else if (grades[i] == "C") {
				counts[2]++;
			} else if (grades[i] == "D") {
				counts[1]++;
			} else if (grades[i] == "F") {
				counts[0]++;
			}
		}
		
		return counts;
	}
	
	/**
	 * Resets the grades for a new essay
	 */
	public void resetGrades() {
		for(int i = 0; i < 25; i++) {
			grades[i] = " "; 	
		}
	}
	
	/**
	 * Initializes student names array
	 */
	private void initializeNames() {
		for(int i = 0; i < 25; i++) {
			stuNames[i] = " ";
		}
	}
	
	/**
	 * Initializes course ID
	 * @param courseID
	 * 
	 */
	private void initializeID(int[] courseID) {
		this.courseID = new int[4];
		for(int i = 0; i < 4; i++) {
			this.courseID[i] = courseID[i];
		}
	}
	
	/**
	 * Checks that the student is authorized to join the course
	 * @param enteredID
	 * @return right
	 * 
	 */
	public Boolean checkID(int[] enteredID) {
		Boolean right = true;
		for(int i = 0; i < 4; i++) {
			if(courseID[i] != enteredID[i]) {
				right = false;
				i = 4;
			}
		}
		
		return right;
	}
	
	/**
	 * Must be called after sending student their location in the grade & name arrays!
	 */
	public void updateStuCt() {
		numStudents++;
	}
	
	/**
	 * The professor can assign a new essay to the course.
	 * @param chosenTopic
	 * @param minCt
	 * @param maxCt
	 * 
	 */
	public void createNewEssay(String chosenTopic, int minCt, int maxCt) {
		topic = chosenTopic;
		minWrdCt = minCt;
		maxWrdCt = maxCt;
	}
	
// Setters & Getters
	
	public void setEssayStatus(Boolean status) {
		essayStatus = status;
	}
	
	public int getMinWords() {
		return minWrdCt;
	}
	
	public int getMaxWords() {
		return maxWrdCt;
	}
	
	public String getTopic() {
		return topic;
	}
	
	public int getNumStudents() {
		return numStudents;
	}
	
	public Boolean getEssayStatus() {
		return essayStatus;
	}
	
	public String getName() {
		return courseName;
	}
	
	public void setStNamei(int i, String name) {
		stuNames[i] = name;
	}
	
	public String getStNamei(int i) { 	
		return stuNames[i];
	}
	
	public void setGradei(int i, String grade) {
		grades[i] = grade;
	}
	
	public String getGradei(int i) { 	
		return grades[i];
	}
}