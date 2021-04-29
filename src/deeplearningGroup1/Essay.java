package deeplearningGroup1;

/**
 * Essay holds all the data pertaining to a single essay written by a student
 * @author Steven Rose
 * @version 2.0
 */
public class Essay {
	
	/**
	 * Min and Max allowable number of words in the essay, has a length of 2.
	 */
	private int[] length;
	
	/**
	 * Max time allotted for writing the essay.
	 */
	private int timeLimit;
	
	/**
	 * Time the student actual took writing the essay, measured in minutes (round down). 
	 * Not implemented as of Version 2.0.
	 */
	private int timeTaken;
	
	/**
	 * Holds the topic of the essay.
	 */
	private String topic;
	
	/**
	 * Holds the essay a student has written.
	 */
	private String essay;
	
	/**
	 * Creates a new Essay template with allowable length, time, and topic.
	 * @param minLength
	 * 		Minimum number of words allowed.
	 * @param maxLength
	 * 		Maximum number of words allowed.
	 * @param timeLimit
	 * 		Time alloted for student to write the essay.
	 * @param topic
	 * 		The topic this essay should pertain to.
	 */
	public Essay(int minLength, int maxLength, int timeLimit, String topic) {
		this.length = new int[]{minLength, maxLength};
		this.timeLimit = timeLimit;
		this.topic = topic;
	}
	
	/**
	 * Creates a new Essay template with allowable length, time, and topic based on another Essay.
	 * @param e
	 * 		Essay to be based on.
	 */
	public Essay(Essay e) {
		this.length = e.length;
		this.timeLimit = e.timeLimit;
		this.timeTaken = e.timeTaken;
		this.topic = e.topic;
	}
	
	/**
	 * Sets the value of the essay, must be called before being graded.
	 * @param essay
	 * 		String the student has written as their essay.
	 */
	public void writeEssay(String essay) {
		this.essay = essay;
	}
	
	/**
	 * Returns the essay the student has written.
	 * @return
	 * 		String of essay.
	 */
	public String getEssay() {
		return essay;
	}
	
	/**
	 * Returns the allowable length of the essay written.
	 * @return
	 * 		Min and max allowable lengths.
	 */
	public int[] getLength() {
		return length;
	}
	
	/**
	 * Returns the allotted time limit for the essay.
	 * @return
	 * 		Allotted time.
	 */
	public int getTimeLimit() {
		return timeLimit;
	}
	
	/**
	 * Sets the time the student actual took to write the essay
	 * @param t
	 * 		Number of minutes student took to write essay.
	 */
	public void setTimeTaken(int t) {
		timeTaken = t;
	}
	
	/**
	 * Returns the time the student took to write the essay
	 * @return
	 * 		Time taken to write the essay.
	 */
	public int getTimeTaken() {
		return timeTaken;
	}
	
}
