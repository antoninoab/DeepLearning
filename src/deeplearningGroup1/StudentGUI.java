/**
 * @author Antonino Abeshi
 *
 */

package deeplearningGroup1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.text.PDFTextStripper;


import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class StudentGUI{

	public Stage secondOne;
	private Scene scene2;
	private GridPane pane1;
	private Button clickToSubmit;
	private TextField enterEssayTopic;
	private TextArea enterTextField, NameTextField;
	private Text essayTopic, enterText, NameText;
	private Jarvis jarvis;
	public Stage fourthOne;
	private Scene scene4;
	private Pane pane2;
	
	
	public StudentGUI(Jarvis jarvis) {
		this.jarvis = jarvis;
	}
   
	/**
	 * @StudentBox creating a student box for them to writte the essay in
	 */
	public void StudentBox() 
	{
		pane1 = new GridPane();
		Image image2 = new Image ("sure.jpg");
		pane1.setBackground(new Background(new BackgroundImage(image2,BackgroundRepeat.REPEAT,BackgroundRepeat.REPEAT,BackgroundPosition.DEFAULT,BackgroundSize.DEFAULT)));
		
		pane1.setPadding(new Insets(25,25,25,25));
		pane1.setAlignment(Pos.TOP_LEFT);
		pane1.setHgap(50);
		pane1.setVgap(10);
		
		scene2 = new Scene(pane1, 600, 600);
		secondOne = new Stage();
		secondOne.setTitle(" Welcome Student ");
		secondOne.setScene(scene2);
		secondOne.show();
		
		pane1.setGridLinesVisible(false);
		
		essayTopic = new Text(" This is your topic");
		essayTopic.setFont(Font.font("Times New Roman",FontWeight.BOLD, 15));
		pane1.add(essayTopic, 0, 0,2,1);
		
		enterEssayTopic = new TextField();					//topic entry 
		pane1.add(enterEssayTopic, 0, 1,10,1);
		
		enterText = new Text(" Enter Your Essay");
		enterText.setFont(Font.font("Times New Roman",FontWeight.BOLD, 15));
		pane1.add(enterText, 0,3,1,1);
		
		
		enterTextField = new TextArea();				//text are to enter the essay
		enterTextField.setWrapText(true);
		pane1.add(enterTextField, 0, 4, 10, 20);
		
		
		/**
		 * @Enter the name of the actual essay to save as a pdf
		 */
		//The way the gridpane is set up is row 0, column 0, and spanning 2 columns  but only 1 row
		
		
		NameText = new Text("Enter name to save FilE");
		NameText.setFont(Font.font("Times New Roman",FontWeight.BOLD, 15));
		pane1.add(NameText, 0,25,5,1);
		
		
		NameTextField = new TextArea();				//text are to enter the essay
		NameTextField.setWrapText(true);
		pane1.add(NameTextField, 0, 27, 1, 1);
		
		
		
		clickToSubmit = new Button("Click To Submit");   //button to submit
		pane1.add(clickToSubmit, 4, 27, 5,1);
		
		/**
		 * @ButtonToSubmit this button makes sure that we retrieve the data from the typing and its graded by Jarvis
		 */
		clickToSubmit.setOnAction(event1 -> 
	    {
	    	
	    	
	    	
	    	
	    	NameTextField.setEditable(false);
	    	
	    	/**
			 * @Author Steven Rose Adding the connection from Jarvis to the sumbit button
			 */
	    	String topic = essayTopic.getText();
	    	Essay e = new Essay(0, 300, 4, topic);
	    	e.writeEssay(enterTextField.getText());
	    	//TODO: Done
	    	String grade = jarvis.gradeEssay(e);
	    	//*******************************************************************************************
	    	/**
	    	 *@Author Antonino Abeshi
	    	 *@GradePage  creating the switch statement in order to open the grade 
	    	 */
	    	
	    	switch(grade = jarvis.gradeEssay(e))
	    	{
	    					//A Panel
	    		case "A":	pane2 = new Pane();
							scene4 = new Scene(pane2, 600, 600);
							Label A = new Label();
							A.setText("A");
							A.setFont(Font.font("Calibri",FontWeight.BOLD, 100));
							A.setLayoutX(250);
							A.setLayoutY(200);
							pane2.getChildren().add(A);
							fourthOne = new Stage();
							fourthOne.setTitle(" Letter Grade");
							fourthOne.setScene(scene4);
							fourthOne.show();
							secondOne.hide();
							break;
							// B Panel
		    	case "B":	pane2 = new Pane();
							scene4 = new Scene(pane2, 600, 600);
							Label B = new Label();
							B.setText("B");
							B.setFont(Font.font("Calibri",FontWeight.BOLD, 100));
							B.setLayoutX(250);
							B.setLayoutY(200);
							pane2.getChildren().add(B);
							fourthOne = new Stage();
							fourthOne.setTitle(" Letter Grade");
							fourthOne.setScene(scene4);
							fourthOne.show();
							secondOne.hide();
							break;
							// C Panel
		    	case "C":	pane2 = new Pane();
							scene4 = new Scene(pane2, 600, 600);
							Label C = new Label();
							C.setText("C");
							C.setFont(Font.font("Calibri",FontWeight.BOLD, 100));
							C.setLayoutX(250);
							C.setLayoutY(200);
							pane2.getChildren().add(C);
							fourthOne = new Stage();
							fourthOne.setTitle(" Letter Grade");
							fourthOne.setScene(scene4);
							fourthOne.show();
							secondOne.hide();
							break;
							//D Panel
		    	case "D":  	pane2 = new Pane();
							scene4 = new Scene(pane2, 600, 600);
							Label D = new Label();
							D.setText("D");
							D.setFont(Font.font("Calibri",FontWeight.BOLD, 100));
							D.setLayoutX(250);
							D.setLayoutY(200);
							pane2.getChildren().add(D);
							fourthOne = new Stage();
							fourthOne.setTitle(" Letter Grade");
							fourthOne.setScene(scene4);
							fourthOne.show();
							secondOne.hide();
							break;
		    	case "F":  	pane2 = new Pane();
							scene4 = new Scene(pane2, 600, 600);
							Label F = new Label();
							F.setText("F");
							F.setFont(Font.font("Calibri",FontWeight.BOLD, 100));
							F.setLayoutX(250);
							F.setLayoutY(200);
							pane2.getChildren().add(F);
							fourthOne = new Stage();
							fourthOne.setTitle(" Letter Grade");
							fourthOne.setScene(scene4);
							fourthOne.show();
							secondOne.hide();
							break;
				default:    System.out.println("No Grade");			
	    	}
	    			
	    	System.out.println("Topic: " + topic);
	    	System.out.println("Essay: \n{\n" + enterTextField.getText() + "\n}");
	    	System.out.println("Grade: " + grade);
	    	
	    	
//************************************************************************************************************************************************
	    	
	    	/**
	    	 *@Author Antonino Abeshi
	    	 *@PDFBox libraries used. 
	    	 *@PDDocument   Creates a document using the PDFBox Libraries
	    	 */
	    	PDDocument document = new PDDocument();
			PDPage page = new PDPage();
			document.addPage(page);
			
			//PDFont font = PDType1Font.HELVETICA_BOLD;
			
			// Start a new content stream which will "hold" the to be created content
			
			/**
	    	 *@Author Antonino Abeshi
	    	 *@PDPageContentStream this will crate a stream to print to the pdf
	    	 */
			PDPageContentStream contentStream;
			try {
				contentStream = new PDPageContentStream(document, page);
				// Define a text content stream using the selected font, moving the cursor and drawing the text "Hello World"
				
				PDFont pdfFont = PDType1Font.TIMES_BOLD;
				float fontSize = 12;
				float leading = 1.5f * fontSize;
				//contentStream.setFont(pdfFont, 12);
			
				
				/**
		    	 *@Author Antonino Abeshi
		    	 *@PDRectangle Makes possible the creation of the box inside the PDF
		    	 */
				PDRectangle mediabox = page.getMediaBox();
			    float margin = 72;
			    float width = mediabox.getWidth() - 2*margin;
			    float startX = mediabox.getLowerLeftX() + margin;
			    float startY = mediabox.getUpperRightY() - margin;

			    
			    /**
		    	 *@Author Antonino Abeshi
		    	 *@List creates an array of strings
		    	 *@A while look to make it possible to detect space
		    	 *@ContentStream BeginText actually writes starts copying strings from here to the PDF file.
		    	 */
			    
			    
			    
			    String text = enterTextField.getText();
			    List<String> lines = new ArrayList<String>();
			    int lastSpace = -1;
			    while (text.length() > 0)
			    {
			        int spaceIndex = text.indexOf(' ', lastSpace + 1);
			        if (spaceIndex < 0)
			            spaceIndex = text.length();
			        String subString = text.substring(0, spaceIndex);
			        float size = fontSize * pdfFont.getStringWidth(subString) / 1000;
			        System.out.printf("'%s' - %f of %f\n", subString, size, width);
			        if (size > width)
			        {
			            if (lastSpace < 0)
			                lastSpace = spaceIndex;
			            subString = text.substring(0, lastSpace);
			            lines.add(subString);
			            text = text.substring(lastSpace).trim();
			            System.out.printf("'%s' is line\n", subString);
			            lastSpace = -1;
			        }
			        else if (spaceIndex == text.length())
			        {
			            lines.add(text);
			            System.out.printf("'%s' is line\n", text);
			            text = "";
			        }
			        else
			        {
			            lastSpace = spaceIndex;
			        }
			    }

			    contentStream.beginText();
			    contentStream.setFont(pdfFont, fontSize);
			    contentStream.newLineAtOffset(startX, startY);
			    for (String line: lines)
			    {
			        contentStream.showText(line);
			        contentStream.newLineAtOffset(0, - leading);
			    }
			    contentStream.endText(); 
			    contentStream.close();
			    
			    // pdf annotator 
			    /*
			     * @Author Antonino Abeshi
			     * @PDFTextStripper looks at the text in the pdf and highlights, or underlines the selected words.
			     */

			    PDFTextStripper annotate = new MyAnnotator();
		        annotate.setSortByPosition(true);
		        annotate.setStartPage(0);
		        annotate.setEndPage(document.getNumberOfPages());
		        Writer dummy = new OutputStreamWriter(new ByteArrayOutputStream());
		        annotate.writeText(document, dummy);
		        
		        
		        /// pdf annotator

		        // Save the results and ensure that the document is properly closed:
				
			   // String gradeDisplay = grade = jarvis.gradeEssay(e);
			   // contentStream.showText(gradeDisplay);

				// Make sure that the content stream is closed:
				contentStream.close();
				

				String nameText = NameTextField.getText();
				
				
				document.save(nameText + ".pdf");
			
				document.close();
				
				
			} catch (IOException e1) {
				
				e1.printStackTrace();
			}
			
		//File file = new File("/Users/imac/Desktop/Eclipse workspace/Testing/ni.pdf");
		
		//fourthOne.close();
			
			
			//String[] string = jarvis.getComments();
			System.out.println(jarvis.getComments());
			System.out.println(jarvis.getCommentPositions());
			
			
	    	
	    });
		
		
		//Timer 
		
		
		
	}
	
	


public void searchForString() {
	
	List<int[]> x = jarvis.getCommentPositions();
	
	
	
	//String dooshbag = new String();
	String[] words = jarvis.getComments();
	String str = enterTextField.getText();
	
	if (words.equals(str))
	{
		System.out.println(words);
	}
	Boolean found;
	
	//found = str.
	
	
	//for(int i = -1; (i = str.lastIndexOf(str, x));

	//System.out.println()); 
	//for (int i = -1; (i = str.codePointAt(x)) != -1; i++) {
	//    System.out.println(i);
	//}
	
	//System.out.println(str.indexOf(x)); // prints "4"
	//System.out.println(str.lastIndexOf(dooshbag)); // prints "22"
	
	System.out.println();
	
	
	
	// Returns index of first occurrence of character. 
    int firstIndex = str.indexOf('s'); 
    System.out.println("" + firstIndex); 
  
    // Returns index of last occurrence specified character. 
    int lastIndex = str.lastIndexOf('s'); 
    System.out.println("" + lastIndex); 
  
    // Index of the first occurrence of specified char 
    // after the specified index if found. 
    int first_in = str.indexOf('s', 10); 
    System.out.println("" + first_in); 
  
    int last_in = str.lastIndexOf('s', 20); 
    System.out.println("" + last_in); 
  
    // gives ASCII value of character at location 20 
    int char_at = str.charAt(20); 
    System.out.println("" +  char_at); 
  
    // throws StringIndexOutOfBoundsException 
    // char_at = str.charAt(50); 
  
	
}

}
