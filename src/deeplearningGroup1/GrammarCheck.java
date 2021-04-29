package deeplearningGroup1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.languagetool.JLanguageTool;
import org.languagetool.language.AmericanEnglish;
import org.languagetool.rules.Rule;
import org.languagetool.rules.RuleMatch;

/**
 * GrammarCheck makes use of the external LanguageTool library to check the grammar of a 
 * given essay based on a pre-defined set of rules, which are saved in an external excel file.
 * @author Steven Rose
 * @version 2.0
 */
public class GrammarCheck {

	/**
	 * The external library tool for checking grammar.
	 */
	private JLanguageTool langTool;
	
	/**
	 * A list of the rules the essay will be checked against.
	 */
	private List< String > rules;
	
	/**
	 * A list of comments made by the grader
	 */
	private List< String > comments;
	
	/**
	 * A list of positions corresponding to each comment
	 */
	private List< int[] > commentPositions;
	
	/**
	 * The path to the external file that holds the list of grammar rules to check.
	 */
	private String grammarRulesFileName = "resources/grammar_rules.xlsx";
	
	/**
	 * An array of words to use for spell checking.
	 * 
	 * @deprecated As of version 2.0.
	 */
	@Deprecated
	private String[] wordList;

	/**
	 * Automated method for testing which of the 1900 rules are actually useful for grading 
	 * essays on. This is used to significantly reduce the runtime of the program when 
	 * grading essays.
	 * @param args
	 * 		Unused.
	 * @throws IOException
	 * 		Thrown in the event the file is already open in another program, or a similar 
	 * IO Exception occurs.
	 */
	public static void main(String[] args) throws IOException {

		long startTime = System.nanoTime();
		
		GrammarCheck gram = new GrammarCheck();
		gram.testEssays();
		
		long endTime = System.nanoTime();
		System.out.println("Time Elapsed = " + ((endTime - startTime) / 1e9) + " s");
	}
	
	/**
	 * Constructs a new GrammarCheck instance, setting up the {@link #langTool}, turning off 
	 * all the rules, and defining which rules will be used for grading essays on.
	 */
	public GrammarCheck() {
		langTool = new JLanguageTool(new AmericanEnglish());
		rules = new ArrayList<>();
		for (Rule rule : langTool.getAllActiveRules()) {
			langTool.disableRule(rule.getId());
			rules.add(rule.getId());
		}
		
		try {
			defRules();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
	}
	
	/**
	 * Checks the string input against each of the rules defined in {@link #rules} one rule at 
	 * a time, and outputs a Matrix of the results.
	 * @param str
	 * 		String to be checked for grammar.
	 * @return
	 * 		A column Matrix where each entry corresponds to a specific rule in {@link #rules} 
	 * and has a value corresponding to the number of possible typos based on that rule. This 
	 * is the input to the neural net in {@link Jarvis#gradeEssay(Essay)}.
	 */
	public Matrix check(String str) {
		List<RuleMatch> matches = null;
		Matrix input = new Matrix(rules.size(), 1);
		comments = new ArrayList<>();
		commentPositions = new ArrayList<>();
		String rule;
		
		for (int i = 0; i < input.getM(); i++) {
			log("rule # = " + i);
			rule = rules.get(i);
			
			langTool.enableRule(rule);
			try {
				matches = langTool.check(str);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
			langTool.disableRule(rule);
			
			for (RuleMatch match : matches) {
				comments.add(match.getMessage());
				commentPositions.add(new int[] {match.getFromPos(), match.getToPos()});
			}
			input.set(i, 0, matches.size());
		}
		
		return input;
	}
	
	/**
	 * Returns a list of the comments made by the grader.
	 * @return
	 * 		The list of comments.
	 */
	public String[] getComments() {
		String[] str = new String[comments.size()];
		for (int i = 0; i < comments.size(); i++) {
			str[i] = comments.get(i);
		}
		return str;
	}
	
	/**
	 * Returns the positions for the comments.
	 * @return
	 * 		List of positions in the form (start, end), zero initialized.
	 */
	public List< int[] > getCommentPositions() {
		return commentPositions;
	}
	
	/**
	 * Calls {@link #check(String)} on all of the essays in the test file and outputs an 
	 * excel file where each column is the output from one essay and each row corresponds 
	 * to a single rule.  This is used to determine which rules are useful for grading and 
	 * should be included in {@link #rules}.
	 * @throws IOException
	 * 		Thrown in the event the file is already open in another program, or a similar 
	 * IO Exception occurs.
	 */
	public void testEssays() throws IOException {
		FileInputStream fis = new FileInputStream(new File("resources/Essay Data/training_set1.xlsx"));
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		XSSFSheet sheet = workbook.getSheetAt(0);
		
		int maxRowNum = sheet.getLastRowNum();
		for (int i = 0; i < maxRowNum; i++) {
			log("Essay # = " + i);
			save(check(sheet.getRow(i).getCell(2).getStringCellValue()), i);
		}
		
		workbook.close();
		fis.close();
	}

	/**
	 * Saves a column Matrix of data to a given file.  Appends the data to the file in a new sheet.
	 * @param data
	 * 		Column matrix of data to be saved.
	 * @param colNum
	 * 		Which column to save the data to in the file
	 * @throws IOException
	 * 		Thrown in the event the file is already open in another program, or a similar 
	 * IO Exception occurs. Is not thrown for the file not existing at all.
	 */
	private void save(Matrix data, int colNum) throws IOException {
		File file = new File(grammarRulesFileName);
		FileInputStream fis;
		Workbook workbook;
		if (file.exists()) {
			fis = new FileInputStream(file);
			workbook = new XSSFWorkbook(fis);
			fis.close();
		}	else {
			workbook = new XSSFWorkbook();
		}

		Sheet sheet;
		if (colNum == 0) {
			sheet = workbook.createSheet();
		} else {
			sheet = workbook.getSheetAt(workbook.getNumberOfSheets()-1);
		}
		
		Row row;
		Cell cell;

		for (int i = 0; i < data.getM(); i++) {
			if (colNum == 0) {
				row = sheet.createRow(i);
			} else {
				row = sheet.getRow(i);
			}
			cell = row.createCell(colNum);
			cell.setCellValue(data.get(i, 0));
		}

		FileOutputStream fileOut = null;
		fileOut = new FileOutputStream(grammarRulesFileName);
		workbook.write(fileOut);
		fileOut.close();
		workbook.close();
	}
	
	/**
	 * Checks how many times a given string puts a space before a punctuation mark.
	 * @param str
	 * 		String to be checked.
	 * @return
	 * 		The number of mistakes made normalized to the length of the string.
	 * @deprecated As of Version 2.0
	 */
	public double checkSpacesBeforePunctuation(String str) {
		double numBadGrammar = 0;

		// Check for spaces before punctuation
		String punctuation = ",.;:?/!'";
		for (int i = 1; i < str.length(); i++) {
			//log(essay.charAt(i) + " | " + punctuation.indexOf(essay.charAt(i)) + " | " + essay.substring(i-1, i).equals(" "));
			if (punctuation.indexOf(str.charAt(i)) >= 0 && str.substring(i-1, i).equals(" ")) {
				numBadGrammar++;
				//log("{" + essay.substring(i-1, i+1) + "}");
			}
		}
		return numBadGrammar / str.length();
	}
	
	/**
	 * Checks if a given {@link Essay} is within the min and max word limits.
	 * @param e
	 * 		Essay to be checked.
	 * @return
	 * 		True if within word limit, false otherwise.
	 * @deprecated As of Version 2.0
	 */
	public boolean checkWordLimit(Essay e) {
		String essay = e.getEssay();
		return essay.length() >= e.getLength()[0] && essay.length() < e.getLength()[1];
	}
	
	/**
	 * Checks the spelling of all the words in a given string.
	 * @param str
	 * 		String to check.
	 * @return
	 * 		The number of misspelled words normalized to the total number of words in the string.
	 * @deprecated As of Version 2.0 
	 */
	public double checkSpelling(String str) {
		String[] words = str.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
		
		double numMisspelled = 0;
		boolean isRealWord;
		for (int i = 0; i < words.length; i++) {
			isRealWord = Arrays.stream(wordList).anyMatch(words[i]::equals);
			if (!isRealWord) {
				numMisspelled++;
			}
			
		}
		
		return numMisspelled / words.length;
	}
	
	/**
	 * Loads the list of words from an external dictionary file
	 * @deprecated As of Version 2.0
	 */
	public void loadWordList() {
		try {
			Scanner sc = new Scanner(new File("resources/words.txt"));
			List<String> lines = new ArrayList<String>();
			while (sc.hasNextLine()) {
				lines.add(sc.nextLine());
			}
			sc.close();
			
			wordList = lines.toArray(new String[0]);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Defines the rules each string is to be graded on, based on a list in an external excel file.
	 * @throws IOException
	 * 		Thrown in the event the file is already open in another program, or a similar 
	 * IO Exception occurs. 
	 */
	private void defRules() throws IOException {
		FileInputStream fis = new FileInputStream(new File(grammarRulesFileName));
		Workbook workbook = new XSSFWorkbook(fis);
		Sheet sheet = workbook.getSheet("ACTIVE_RULES");
		rules = new ArrayList<>();
		
		int rowMaxNum = sheet.getLastRowNum();
		for (int i = 0; i < rowMaxNum; i++)
			rules.add(sheet.getRow(i).getCell(0).getStringCellValue());
		
		workbook.close();
		fis.close();
	}
	
	/**
	 * Returns the number of rules used for checking strings against.
	 * @return
	 * 		The number of rules in use.
	 */
	public int getNumRules() {
		return rules.size();
	}
	
	/**
	 * Prints a String to the console and writes it to a log file if {@link Jarvis#debugging} 
	 * is set to true.
	 * @param msg
	 * 		Message to be printed.
	 */
	void log(String msg) {
		if (Jarvis.debugging) {
			System.out.println(msg);
			PrintWriter out;
			try {
				out = new PrintWriter(new FileWriter("resources/log_file.txt", true));
				out.println(msg);
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
