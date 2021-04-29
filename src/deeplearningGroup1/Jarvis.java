package deeplearningGroup1;

import javafx.util.Pair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ComparisonOperator;
import org.apache.poi.ss.usermodel.ConditionalFormattingRule;
import org.apache.poi.ss.usermodel.FontFormatting;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PatternFormatting;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.SheetConditionalFormatting;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import scala.collection.mutable.HashMap;

/**
 * Jarvis is the neural network class which grades essays through the use of a trained neural network, 
 * consisting of a list of Neural Layers with associated weights and biases.  It has the ability to train 
 * the neural net through the use of backpropagation, and it can also test the neural net to see how close 
 * to the expected outcome it is.  All training and testing data comes from external excel files, and the 
 * results of the testing are output as excel files under /resources.
 * @author Steven Rose
 * @version 2.0
 * @see NeuralLayer
 * @see GrammarCheck
 * @see Essay
 */
@SuppressWarnings("restriction")
public class Jarvis {

	/**
	 * A list of all the layers of the neural network which hold the weights and biases of each layer.
	 */
	private List<NeuralLayer> neuralLayers;
	
	/**
	 * The relative path to the excel file holding all the weights and bias data.
	 */
	private final static String jarvisWeightsBiases = "resources/neural_net_weights_biases.xlsx";

	/**
	 * An external grammar checking tool.
	 */
	private GrammarCheck gram;
	
	/**
	 * Sets whether all of the logs should be output to the console and saved to an external file.  If set 
	 * to true, significantly increases runtime.
	 */
	static boolean debugging = false;
	
	/**
	 * Used to number the copies of the weights and biases file as it changes
	 */
	private int excelNum = 0;

	/**
	 * Automated method for training and testing the neural net on data from a number of external files. It 
	 * will test first, output the results of that test, then train and test again, outputting the results of 
	 * that test as well for comparison.
	 * @param args
	 * 		Unused.
	 */
	public static void main(String[] args) {
		long startTime = System.nanoTime();
		DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Jarvis j = new Jarvis();
		j.log("----------START----------", true);
		j.log("Start Time: " + sdf.format(new Date()), true);
		
		if (true) {
			try {
				//j.test("resources/Essay Data/testing_set1.xlsx");
				j.train("resources/Essay Data/training_set1.xlsx", 10, 0);
				j.train("resources/Essay Data/training_set7.xlsx", 10, 0);
				j.train("resources/Essay Data/training_set8.xlsx", 10, 0);
				j.test("resources/Essay Data/testing_set1.xlsx");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		j.log("End Time: " + sdf.format(new Date()), true);
		j.log("----------END----------", true);
		long endTime = System.nanoTime();
		j.log("Time Elapsed = " + ((endTime - startTime) / 1e9) + " s", true);
	}

	/**
	 * Creates a new Jarvis, sets the weights and biases of the neural network based on data from a file, 
	 * then creates the neural network from those weights/biases.  It also initializes the {@link GrammarCheck} 
	 * gram.
	 */
	public Jarvis() {
		gram = new GrammarCheck();
		String[] layerNames = {"input", "hidden 0", "hidden 1",
				"hidden 2", "hidden 3", "hidden 4", "hidden 5", "output"};

		Pair< List <List <Matrix>>, Boolean > savedData = null;
		try {
			savedData = loadWeightsBiases(jarvisWeightsBiases);
		} catch (IOException e) {
			e.printStackTrace();
		}
		List <Matrix> weights = savedData.getKey().get(0);
		List <Matrix> biases = savedData.getKey().get(1);
		
		createNeuralNet(layerNames, weights, biases);
		
		if (savedData.getValue() == false)
			try {
				saveWeightsBiases(jarvisWeightsBiases);
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	/**
	 * Creates and populates the neuralLayers List.
	 * @param names
	 * 		Array of strings for ids of each neural layer.
	 * @param weights
	 * 		List of Matrices of weights from one layer to the next, each Matrix of size [output x input].
	 * @param biases
	 * 		List of column Matrices of biases for each layer, each Matrix of size [output x 1].
	 */
	private void createNeuralNet(String[] names, List< Matrix > weights, List< Matrix > biases) {
		if (weights.size() != biases.size()) {
			log("ERROR: # of weights and biases do not match:", true);
			log("# of weight matrices = " + weights.size(), true);
			log("# of bias matrices = " + biases.size(), true);
			System.exit(1);
		}

		neuralLayers = new ArrayList<>();
		for (int i = 0; i < weights.size(); i++) {
			neuralLayers.add(new NeuralLayer(names[i], weights.get(i), biases.get(i)));
		}
	}

	/**
	 * Grades an essay.
	 * @param essay
	 * 		Essay to be graded.
	 * @return
	 * 		Grade for the essay.
	 */
	public String gradeEssay(Essay essay) {
		Matrix input = gram.check(essay.getEssay());
		Matrix output = calculateOutput(input, neuralLayers.size()-1);
		
		output.print();
		log("\ninput:", debugging);
		input.print();
		log("\nweights:", debugging);
		neuralLayers.get(0).getWeights().print();
		log("\nbiases:", debugging);
		neuralLayers.get(0).getBias().print();
		log("\nweighted input:", debugging);
		neuralLayers.get(0).getWeightedInput().print();
		log("\nactivations:", debugging);
		neuralLayers.get(0).getActivations().print();
		log("\nlayers: " + neuralLayers.size(), debugging);

		return matrixToGrade(output);
	}

	/**
	 * Iteratively calculates output of each layer in the network based on the output of the previous layer.
	 * @param input
	 * 		Column Matrix of input values from previous layer.
	 * @param i
	 * 		Which layer the output is being calculated for.
	 * @return
	 * 		Column Matrix of output for current layer.
	 */
	private Matrix calculateOutput(Matrix input, int i) {
		if (i == 0) {
			return neuralLayers.get(i).calculateSigmoid(input);
		} else {
			return neuralLayers.get(i).calculateSigmoid(calculateOutput(input, i-1));
		}
	}

	/**
	 * Trains the neural net based on data from a given file.
	 * @param fileName
	 * 		Relative path to file.
	 * @param batchSize
	 * 		How many essays to train at once, also controls how often the weights and biases are saved during 
	 * training (lower = saves more often, but is less efficient at learning).
	 * @throws IOException
	 * 		Thrown in the event the file is already open in another program, or a similar IO Exception occurs.
	 */
	private void train(String fileName, int batchSize, int rowNum) throws IOException {
		log("Training on " + fileName, true);

		FileInputStream fis = new FileInputStream(new File(fileName));
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		XSSFSheet sheet = workbook.getSheetAt(0);

		Row row;
		int maxRowNum = sheet.getLastRowNum();
		log("maxRowNum = " + maxRowNum, debugging);

		for (int batch = rowNum/batchSize; batch < maxRowNum-1; batch += batchSize) {
			log("batch = " + batch, debugging);
			int batchSizeActual = Math.min(batchSize, maxRowNum - batch);
			log("batchSizeActual = " + batchSizeActual, debugging);
			String[] essays = new String[batchSizeActual];
			Matrix[] grades = new Matrix[batchSizeActual];
			double numGrade;
			for (int i = 0; i < batchSizeActual; i++) {
				row = sheet.getRow(rowNum);
				essays[i] = row.getCell(2).getStringCellValue();
				log("essay =\n" + essays[i], true);
				numGrade = row.getCell(6).getNumericCellValue();
				grades[i] = gradeToMatrix(numGrade);
				rowNum++;
			}

			log("row # = " + rowNum, debugging);

			List< Matrix[][] > gradient = new ArrayList<>();
			for (int i = 0; i < batchSizeActual; i++) {
				log("Essay # = " + (rowNum - batchSizeActual + i), true);
				gradient.add(calculateGradient(essays[i], grades[i]));
			}

			log("\n----------------------------\nGradient:\n", debugging);
			Matrix[][] avgGradient = new Matrix[2][neuralLayers.size()];
			for (int i = 0; i < batchSizeActual; i++) {
				log("\nessay #: " + (i+batch*batchSize), debugging);
				for (int j = 0; j < neuralLayers.size(); j++) {
					if (i == 0) {
						avgGradient[0][j] = new Matrix(gradient.get(i)[0][j].getM(), 1);
						avgGradient[1][j] = new Matrix(gradient.get(i)[1][j].getM(), gradient.get(i)[1][j].getN());
					}

					log("bias error:", debugging);
					gradient.get(i)[0][j].print();
					log("weights error:", debugging);
					gradient.get(i)[1][j].print();
					debugging = false;
					gradient.get(i)[0][j].fixNaN();
					gradient.get(i)[1][j].fixNaN();
					avgGradient[0][j] = avgGradient[0][j].plus(gradient.get(i)[0][j]);
					avgGradient[1][j] = avgGradient[1][j].plus(gradient.get(i)[1][j]);
				}

			}

			for (int j = 0; j < neuralLayers.size(); j++) {
				log("\navgGradient[0][" + j + "]:", debugging);
				avgGradient[0][j].print();
				log("\navgGradient[1][" + j + "]:", debugging);
				avgGradient[1][j].print();
				log("batchSize = " + batchSize, debugging);
				
				avgGradient[0][j] = avgGradient[0][j].divide(batchSize);
				avgGradient[1][j] = avgGradient[1][j].divide(batchSize);
				
				log("\navgGradient[0][" + j + "]:", debugging);
				avgGradient[0][j].print();
				log("\navgGradient[1][" + j + "]:", debugging);
				avgGradient[1][j].print();
			}

			NeuralLayer nl;
			log("\n-----------------------\ncurrent neural net:", debugging);
			for (int i = 0; i < neuralLayers.size(); i++) {
				log("neural layer #" + (i+1), debugging);
				nl = neuralLayers.get(i);
				nl.print();
			}

			log("\n\naverage bias errors:", debugging);
			for (int j = 0; j < neuralLayers.size(); j++) {
				avgGradient[0][j].print();
				log("", debugging);
			}

			log("\naverage weights errors:", debugging);
			for (int j = 0; j < neuralLayers.size(); j++) {
				avgGradient[1][j].print();
				log("", debugging);
			}

			log("-----------------------\nnew neural net:", debugging);
			for (int i = 0; i < neuralLayers.size(); i++) {
				log("neural layer #" + (i+1), debugging);
				nl = neuralLayers.get(i);
				nl.changeBias(avgGradient[0][i]);
				nl.changeWeights(avgGradient[1][i]);
				nl.print();
			}

			saveWeightsBiases("resources/train_weights_biases/weights_biases_" + excelNum + ".xlsx");
			log("\tExcel # = " + excelNum + "\n", true);
			excelNum++;
		}

		workbook.close();
		fis.close();
	}

	/**
	 * Transforms a grade given as a number from 1-12 to an alphabetical grade.  Useful for training, as the data is given with numerical grades from 1-12.
	 * @param d
	 * 		Numerical representation of grade, [1-12].
	 * @return
	 * 		Returns a column Matrix representation of that grade for comparison to neural net output.
	 */
	private Matrix gradeToMatrix(double d) {
		Matrix m = new Matrix(neuralLayers.get(neuralLayers.size()-1).getBias().getM(), 1);
		if (d > 9) {
			m.set(0, 0, 1); //A
		} else if (d > 6) {
			m.set(1, 0, 1); //B
		} else if (d > 3) {
			m.set(2, 0, 1); //C
		} else {
			m.set(3, 0, 1); //D
		}
		return m;
	}
	
	/**
	 * Transforms a column Matrix representation of a grade to its corresponding string.
	 * @param m
	 * 		Column Matrix of grade from output of neural net.
	 * @return
	 * 		Returns a string of [A-D].
	 */
	private String matrixToGrade(Matrix m) {
		String[] grades = {"A", "B", "C", "D"};
		String grade = "No Grade";
		double gradeNumber = 0;
		for (int i = 0; i < m.getM(); i++) {
			if (m.get(i, 0) > gradeNumber) {
				grade = grades[i];
				gradeNumber = m.get(i, 0);
			}
		}
		return grade;
	}

	/**
	 * Calculates the gradient of the cost function for each weight and bias.  This is used for training the neural net using backpropagation.
	 * @param essay
	 * 		The essay to be trained on.
	 * @param desiredOutput
	 * 		Matrix representation of the desired grade for the essay.
	 * @return
	 * 		A Matrix array of the gradient for this essay.
	 * @see #matrixToGrade(Matrix)
	 */
	private Matrix[][] calculateGradient(String essay, Matrix desiredOutput) {
		Matrix input = gram.check(essay);
		Matrix output = calculateOutput(input, neuralLayers.size()-1);
		Matrix delC_A = output.minus(desiredOutput).multiply(-2);

		log("\n----------------\nTraining...", debugging);
		log("input:", debugging);
		input.print();
		log("output:", debugging);
		output.print();
		log("desired output:", debugging);
		desiredOutput.print();
		log("--------------------", debugging);

		NeuralLayer nl, nlNext;
		for (int i = neuralLayers.size()-1; i >= 0; i--) {
			nl = neuralLayers.get(i);
			if (i == neuralLayers.size()-1) {
				nl.calculateError(delC_A);
				nl.calculateCost(desiredOutput);
			} else {
				nlNext = neuralLayers.get(i+1);
				nl.calculateError(nlNext.getWeights(), nlNext.getBiasError());
			}
			if (i == 0) {
				nl.calculateWeightError(input);
			} else {
				nl.calculateWeightError(neuralLayers.get(i-1).getActivations());
			}

		}

		Matrix[][] grad = new Matrix[2][neuralLayers.size()];
		for (int i = 0; i < neuralLayers.size(); i++) {
			nl = neuralLayers.get(i);
			log("neural layer #" + (i+1), debugging);
			nl.printAll();
			grad[0][i] = new Matrix(nl.getBiasError().getData());
			grad[1][i] = new Matrix(nl.getWeightsError().getData());
		}

		return grad;
	}

	/**
	 * Loads the weights and biases from a file, or if necessary generates random weights and biases to be used for the neural net.
	 * @param fileName
	 * 		The file the data is to be pulled from.
	 * @return
	 * 		An array of Matrices holding the data from the file, and a boolean for whether the file existed or not
	 * @throws IOException
	 * 		Thrown in the event the file is open in another program or a similar IO Exception occurs. Is not thrown for the file not existing at all.
	 * @see javafx.util.Pair#Pair(Object, Object)
	 */
	private Pair< List <List <Matrix>>, Boolean > loadWeightsBiases(String fileName) throws IOException {
		File file = new File(fileName);
		List <Matrix> weights = new ArrayList<>();
		List <Matrix> biases = new ArrayList<>();
		Boolean fileExists;
		
		if (file.exists()) {
			fileExists = true;
			FileInputStream fis = new FileInputStream(file);
			Workbook workbook = new XSSFWorkbook(fis);
			Sheet sheet;
			Row row;
			
			double[][] data;
			int x,y;
			for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
				sheet = workbook.getSheetAt(i);
				y = sheet.getLastRowNum()+1;
				x = sheet.getRow(0).getLastCellNum();
				data = new double[y][x];
				for (int j = 0; j < y; j++) {
					row = sheet.getRow(j);
					for (int k = 0; k < x; k++) {
						data[j][k] = row.getCell(k).getNumericCellValue();
					}
				}

				if (i % 2 == 0) {
					weights.add(new Matrix(data));
				} else {
					biases.add(new Matrix(data));
				}
			}

			workbook.close();
			fis.close();
		} else {
			fileExists = false;
			int randMax = 10;
			weights.add(Matrix.random(24, gram.getNumRules(), randMax));
			weights.add(Matrix.random(24, 24, randMax));
			weights.add(Matrix.random(24, 24, randMax));
			weights.add(Matrix.random(24, 24, randMax));
			weights.add(Matrix.random(24, 24, randMax));
			weights.add(Matrix.random(24, 24, randMax));
			weights.add(Matrix.random(4, 24, randMax));

			biases.add(Matrix.random(24, 1, randMax));
			biases.add(Matrix.random(24, 1, randMax));
			biases.add(Matrix.random(24, 1, randMax));
			biases.add(Matrix.random(24, 1, randMax));
			biases.add(Matrix.random(24, 1, randMax));
			biases.add(Matrix.random(24, 1, randMax));
			biases.add(Matrix.random(4, 1, randMax));
		}
		
		List <List <Matrix>> result = new ArrayList<>();
		result.add(weights);
		result.add(biases);
		return new Pair< List <List <Matrix>>, Boolean >(result, fileExists);
	}

	/**
	 * Saves the weights and biases of the neural net to the specified file, overwriting any existing data.
	 * @param fileName
	 * 		File to be saved to.
	 * @throws IOException
	 * 		Thrown in the event the file is open in another program or a similar IO Exception occurs.
	 */
	private void saveWeightsBiases(String fileName) throws IOException {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet;
		Row row;
		Cell cell;
		Matrix weights, biases;

		for (int i = 0; i < neuralLayers.size(); i++) {
			//Save the Weights for this layer
			sheet = workbook.createSheet("weights " + i);
			weights = neuralLayers.get(i).getWeights();
			for (int j = 0; j < weights.getM(); j++) {
				row = sheet.createRow(j);
				for (int k = 0; k < weights.getN(); k++) {
					cell = row.createCell(k);
					cell.setCellValue(weights.get(j, k));
				}
			}

			//Save the Biases for this layer
			sheet = workbook.createSheet("biases " + i);
			biases = neuralLayers.get(i).getBias();
			for (int j = 0; j < biases.getM(); j++) {
				row = sheet.createRow(j);
				for (int k = 0; k < biases.getN(); k++) {
					cell = row.createCell(k);
					cell.setCellValue(biases.get(j, k));
				}
			}
		}

		log("Saved new Weights/Biases!", true);

		FileOutputStream fileOut = new FileOutputStream(fileName);
		workbook.write(fileOut);
		fileOut.close();
		workbook.close();
		
		loadWeightsBiases(fileName);
	}

	/**
	 * Tests the neural net based on data from a given file.
	 * @param fileName
	 * 		File to use for testing.
	 * @throws IOException
	 * 		Thrown in the event the file is already open in another program, or a similar IO Exception occurs.
	 */
	private void test(String fileName) throws IOException {
		log("Testing on " + fileName, true);
		
		FileInputStream fis = new FileInputStream(new File(fileName));
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		XSSFSheet sheet = workbook.getSheetAt(0);

		Row row;
		int maxRowNum = sheet.getLastRowNum();
		//maxRowNum = 5;
		log("maxRowNum = " + maxRowNum, debugging);
		Matrix[] gradesExpected = new Matrix[maxRowNum];
		Matrix[] gradesActual = new Matrix[maxRowNum];
		Matrix input;
		String[] essays = new String[maxRowNum];

		for (int i = 0; i < maxRowNum; i++) {
			log("Essay # = " + i, true);
			row = sheet.getRow(i);
			essays[i] = row.getCell(2).getStringCellValue();
			gradesExpected[i] = gradeToMatrix(row.getCell(3).getNumericCellValue());
			input = gram.check(essays[i]);
			gradesActual[i] = calculateOutput(input, neuralLayers.size()-1);
		}

		workbook.close();
		fis.close();
		
		String fileNum = fileName.substring(21, 33);

		saveTestData(gradesExpected, gradesActual, fileNum);
		saveTestResults(essays, gradesExpected, gradesActual, fileNum);
	}
	
	/**
	 * Saves the test data in an excel file to compare the raw output for each essay being tested to its expected output.
	 * @param gradesExpected
	 * 		Column Matrix of expected grade for essay
	 * @param gradesActual
	 * 		Column Matrix of grade calculated by neural net for essay.
	 * @throws IOException
	 * 		Thrown in the event the file is already open in another program, or a similar IO Exception occurs.
	 */
	private void saveTestData(Matrix[] gradesExpected, Matrix[] gradesActual, String fileNum) throws IOException {
		String fileName = "resources/test_results_data.xlsx";
		File file = new File(fileName);
		Workbook workbook;
		
		//Open workbook
		if (file.exists()) {
			FileInputStream fis = new FileInputStream(file);
			workbook = new XSSFWorkbook(fis);
			fis.close();
		} else {
			workbook = new XSSFWorkbook();
		}
		
		//Setup sheet
		String[] grades = {"A", "B", "C", "D", "F"};
		int testNum = workbook.getNumberOfSheets();
		Sheet sheet = workbook.createSheet("Test Data " + testNum);

		//Setup conditional formatting
		SheetConditionalFormatting formatLayer = sheet.getSheetConditionalFormatting();
		ConditionalFormattingRule ruleGreen = formatLayer.createConditionalFormattingRule(ComparisonOperator.GE, "0.8");

		//Change font color
		FontFormatting fontFormat = ruleGreen.createFontFormatting();
		fontFormat.setFontColorIndex(IndexedColors.DARK_GREEN.getIndex());

		//Change background color
		PatternFormatting fill = ruleGreen.createPatternFormatting();
		fill.setFillBackgroundColor(IndexedColors.LIGHT_GREEN.getIndex());
		fill.setFillPattern(PatternFormatting.SOLID_FOREGROUND);

		//Define range for conditional formatting
		CellRangeAddress[] address1 = {CellRangeAddress.valueOf("B2:AAA5")};
		CellRangeAddress[] address2 = {CellRangeAddress.valueOf("B8:AAA11")};

		//Apply conditional formatting
		formatLayer.addConditionalFormatting(address1, ruleGreen);
		formatLayer.addConditionalFormatting(address2, ruleGreen);

		//Set style for numbers
		CellStyle style = workbook.createCellStyle();
		style.setDataFormat(workbook.createDataFormat().getFormat("0.00E+00"));

		//Save expected output data
		Row row = sheet.createRow(0);
		row.createCell(0).setCellValue("Expected Output");
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));
		for (int i = 0; i < gradesExpected[0].getM(); i++) {
			row = sheet.createRow(i+1);
			row.createCell(0).setCellValue(grades[i]);
			for (int j = 0; j < gradesExpected.length; j++) {
				row.createCell(j+1).setCellValue(gradesExpected[j].get(i, 0));
			}
		}

		//Save actual output data
		row = sheet.createRow(gradesExpected[0].getM() + 2);
		row.createCell(0).setCellValue("Actual Output");
		sheet.addMergedRegion(new CellRangeAddress(6, 6, 0, 1));
		Cell cell;
		for (int i = 0; i < gradesActual[0].getM(); i++) {
			row = sheet.createRow(i+gradesExpected[0].getM()+3);
			row.createCell(0).setCellValue(grades[i]);
			for (int j = 0; j < gradesActual.length; j++) {
				cell = row.createCell(j+1);
				cell.setCellValue(gradesActual[j].get(i, 0));
				cell.setCellStyle(style);
			}
		}
		
		sheet.createRow(12).createCell(0).setCellValue("File: " + fileNum);
		sheet.addMergedRegion(new CellRangeAddress(12, 12, 0, 1));
		
		//Close file
		FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream(fileName);
			workbook.write(fileOut);
			fileOut.close();
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Saves the results based on the test data to see how accurate the neural net is overall.
	 * @param essays
	 * 		Essays that the neural net was trained on.
	 * @param gradesExpected
	 * 		List of column Matrices of the expected grade for each essay.
	 * @param gradesActual
	 * 		List of column Matrices of the grade calculated by the neural net for each essay.
	 */
	private void saveTestResults(String[] essays, Matrix[] gradesExpected, Matrix[] gradesActual, String fileNum) {
		String fileName = "resources/test_results.xlsx";
		File file = new File(fileName);
		Workbook workbook = null;
		
		//Open workbook
		try {
			FileInputStream fis = new FileInputStream(file);
			workbook = new XSSFWorkbook(fis);
			fis.close();
		} catch (FileNotFoundException e) {
			workbook = new XSSFWorkbook();
		} catch (IOException e) {
			e.printStackTrace();
		}

		//Setup sheet
		int testNum = workbook.getNumberOfSheets();
		Sheet sheet = workbook.createSheet("Test Results " + testNum);
		Row row = sheet.createRow(0);
		row.createCell(0).setCellValue("Essay");
		row.createCell(1).setCellValue("Expected Grade");
		row.createCell(2).setCellValue("Actual Grade");
		row.createCell(3).setCellValue("Expected Grade");
		row.createCell(4).setCellValue("Actual Grade");
		row.createCell(5).setCellValue("Difference");
		row.createCell(6).setCellValue("Abs(Diff)");
		

		HashMap< String, Integer > map = new HashMap<>();
		map.put("A", 4);
		map.put("B", 3);
		map.put("C", 2);
		map.put("D", 1);
		
		String expectedGrade, actualGrade;
		
		//Save results into sheet
		for (int i = 0; i < essays.length; i++) {
			expectedGrade = matrixToGrade(gradesExpected[i]);
			actualGrade = matrixToGrade(gradesActual[i]);
			
			row = sheet.createRow(i+1);
			row.createCell(0).setCellValue(essays[i]);
			row.createCell(1).setCellValue(expectedGrade);
			row.createCell(2).setCellValue(actualGrade);
			row.createCell(3).setCellValue(map.get(expectedGrade).get());
			row.createCell(4).setCellValue(map.get(actualGrade).get());
			row.createCell(5).setCellFormula("D" + (i+2) + "-E" + (i+2));
			row.createCell(6).setCellFormula("ABS(F" + (i+2) + ")");
		}
		
		Cell cell;
		CellStyle stylePercent = workbook.createCellStyle();
		stylePercent.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
		CellStyle styleNumber = workbook.createCellStyle();
		styleNumber.setDataFormat(workbook.createDataFormat().getFormat("0.000"));
		
		row = sheet.getRow(0);
		row.createCell(8).setCellValue("# Essays:");
		row.createCell(9).setCellFormula("COUNT(G2:G9999)");
		
		row = sheet.getRow(1);
		row.createCell(8).setCellValue("# Incorrect:");
		row.createCell(9).setCellFormula("COUNTIF(G2:G9999, \">0\")");
		
		row = sheet.getRow(2);
		row.createCell(8).setCellValue("% Incorrect:");
		cell = row.createCell(9);
		cell.setCellFormula("J2/J1");
		cell.setCellStyle(stylePercent);
		
		row = sheet.getRow(3);
		row.createCell(8).setCellValue("Avg Amount Incorrect:");
		cell = row.createCell(9);
		cell.setCellFormula("SUM(G2:G9999)/J2");
		cell.setCellStyle(styleNumber);
		
		row = sheet.getRow(4);
		row.createCell(8).setCellValue("# Correct:");
		row.createCell(9).setCellFormula("COUNTIF(G2:G9999, 0)");
		
		row = sheet.getRow(5);
		row.createCell(8).setCellValue("% Correct:");
		cell = row.createCell(9);
		cell.setCellFormula("J5/J1");
		cell.setCellStyle(stylePercent);
		
		sheet.getRow(7).createCell(8).setCellValue("File: " + fileNum);
		
		for (int i = 1; i <= 8; i++) {
			sheet.autoSizeColumn(i);
		}
		
		//Close file
		FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream(fileName);
			workbook.write(fileOut);
			fileOut.close();
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Returns the comments from the GrammarChecker.
	 * @return
	 * 		List of comments.
	 */
	public String[] getComments() {
		return gram.getComments();
	}
	
	/**
	 * Returns the positions for the comments from the GrammarChecker.
	 * @return
	 * 		List of positions in the form (start, end), zero initialized.
	 */
	public List< int[] > getCommentPositions() {
		return gram.getCommentPositions();
	}

	/**
	 * Prints a String to the console and writes it to a log file if log is set to true.
	 * @param msg
	 * 		Message to be printed.
	 * @param
	 * 		Whether or not  to write the message to the console and the log file, generally 
	 * set to whatever {@link #debugging} is.
	 */

	void log(String msg, boolean log) {
		if (log) {
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
