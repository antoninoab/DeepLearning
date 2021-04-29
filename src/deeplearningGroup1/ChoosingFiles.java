/**
 * 
 */
package deeplearningGroup1;

/**
 * @author AntoninoAbeshi
 *
 */

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
 
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;



/**
 * @ChoosingFiles2 method allows for the user to go and look at his or her old pdf files.
 *
 */
public class ChoosingFiles {
 
    private Desktop desktop = Desktop.getDesktop();
    public Stage primaryStage;
    
    public ChoosingFiles() {
    	
    	ChoosingFiles2();
    }
    
    public void ChoosingFiles2() {
    	
    primaryStage = new Stage();
	primaryStage.setTitle("Choose your file"); 
 
        final FileChooser fileChooser = new FileChooser();
 
        TextArea textArea = new TextArea();
        textArea.setMinHeight(70);
        /**
         * @button1 lets you select one file.
         * @button2 lets you select multiple files
         *
         */
        Button button1 = new Button("Select One File and Open");
 
        Button buttonM = new Button("Select Multi Files");
 
        button1.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
                textArea.clear();
                File file = fileChooser.showOpenDialog(primaryStage);
                if (file != null) {
                    openFile(file);
                    List<File> files = Arrays.asList(file);
                    printLog(textArea, files);
                    primaryStage.close();
                }
            }
        });
 
        buttonM.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
                textArea.clear();
                List<File> files = fileChooser.showOpenMultipleDialog(primaryStage);
 
                printLog(textArea, files);
            }
        });
 
        VBox root = new VBox();
        root.setPadding(new Insets(10));
        root.setSpacing(5);
 
        root.getChildren().addAll(button1);
 
        Scene scene = new Scene(root, 400, 200);
 
        primaryStage.setTitle("Make your choice");
        primaryStage.setScene(scene);
        primaryStage.show();
        
     // Add Extension Filters
        
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PDF", "*.pdf")); 
              

    }
 
    private void printLog(TextArea textArea, List<File> files) {
        if (files == null || files.isEmpty()) {
            return;
        }
        for (File file : files) {
            textArea.appendText(file.getAbsolutePath() + "\n");
        }
    }
 
    private void openFile(File file) {
        try {
            this.desktop.open(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    public static void main(String[] args) {
      
    }
    
    
 
}