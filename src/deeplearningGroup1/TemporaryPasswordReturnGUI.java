
/**
 * @author Antonino Abeshi
 *
 */
package deeplearningGroup1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.microsoft.sqlserver.jdbc.SQLServerException;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class TemporaryPasswordReturnGUI extends Application {
	
	public Stage thirdOne,fourthOne,fifthOne;
	private Scene scene3,scene4,scene5;
	private GridPane pane1,pane2,pane3; 
	public Button ClickToEnterCode;
	private TextField TemporaryPassword;
	public Button SubmitGO,SubmitGO2, Back;
	private TextField MatchTheCode, EmailText, EnterNewPassword, RepeatNewPassword;

	
	StudentAccount studentaccount = new StudentAccount();
	//RestrictingUserInput EnterNewPassword = new RestrictingUserInput();
	//RestrictingUserInput RepeatNewPassword = new RestrictingUserInput();

	public TemporaryPasswordReturnGUI() {
		
	
	}
	
	public void OpenTemp() {
		
		pane1 = new GridPane();
		
		pane1.setPadding(new Insets(25,25,25,25));    
		pane1.setAlignment(Pos.TOP_LEFT);
		pane1.setHgap(50);
		pane1.setVgap(10);
		
		scene3 = new Scene(pane1, 600, 600);  //creating a new stage
		thirdOne = new Stage();
		thirdOne.setTitle(" Retrieve Password ");
		thirdOne.setScene(scene3);
		thirdOne.show();
		
		pane1.setGridLinesVisible(false);
		
		Label Email = new Label("Here is your temporary Password");
		pane1.add(Email, 0, 1);
		TemporaryPassword = new TextField();
		pane1.add(TemporaryPassword, 1, 1, 2, 1);
		
		TemporaryPassword.setText(studentaccount.reset());
		TemporaryPassword.setEditable(false);
		
		ClickToEnterCode = new Button("Copy the code and click to create a new password");
		pane1.add(ClickToEnterCode, 1, 5);			//creating a back button
	
		
		ClickToEnterCode.setOnAction(e-> {  // adding the back button options to go back to the main scene 
			
				//prg.PasswordReset();
				CodeMatching();
				thirdOne.hide();
		
			
		});
	}
	
	public void CodeMatching()
	
	{
		pane2 = new GridPane();
		
		pane2.setPadding(new Insets(25,25,25,25));    
		pane2.setAlignment(Pos.TOP_LEFT);
		pane2.setHgap(50);
		pane2.setVgap(10);
		
		scene4 = new Scene(pane2, 600, 600);  
		fourthOne = new Stage();
		fourthOne.setTitle(" Enter Code ");
		fourthOne.setScene(scene4);
		fourthOne.show();
		
		pane2.setGridLinesVisible(false);
		
		Label Submit = new Label("Submit to reset your password");
		pane2.add(Submit, 0, 1);
		MatchTheCode = new TextField();
		pane2.add(MatchTheCode, 1, 1, 2, 1);
		
		
		SubmitGO = new Button("Submit to reset");
		pane2.add(SubmitGO, 2, 5);		
	
	
			SubmitGO.setOnAction(e-> {  
				
				if(MatchTheCode.getText().contentEquals(TemporaryPassword.getText()))
				{
					ResetPassword();
					fourthOne.hide();
				}
			
				
				
		
			
		});
	}
	
public void ResetPassword()
	
	{
	
		pane3 = new GridPane();
		
		pane3.setPadding(new Insets(25,25,25,25));    
		pane3.setAlignment(Pos.TOP_LEFT);
		pane3.setHgap(50);
		pane3.setVgap(10);
		
		scene5 = new Scene(pane3, 600, 600); 
		fifthOne = new Stage();
		fifthOne.setTitle(" Enter Code ");
		fifthOne.setScene(scene5);
		fifthOne.show();
		
		pane3.setGridLinesVisible(false);
		
		Label EML = new Label("Email");
		Label NewPass = new Label("Enter your new password");
		Label RepeatPass = new Label("Repeat your new password");
		pane3.add(EML, 0, 1);
		pane3.add(NewPass, 0, 2);
		pane3.add(RepeatPass, 0, 3);
		EmailText = new TextField();
		EnterNewPassword= new TextField();
		RepeatNewPassword= new TextField();
		pane3.add(EmailText,1,1,2,1);
		pane3.add(EnterNewPassword, 1, 2, 2, 1);
		pane3.add(RepeatNewPassword, 1, 3, 2, 1);
		
		
		SubmitGO2 = new Button("Submit");
		Text writtenText = new Text();
		pane3.add(SubmitGO2, 1, 5);	
		
		SubmitGO2.setOnAction(e-> {  
				
				
					if(EnterNewPassword.getText().contentEquals(RepeatNewPassword.getText()))
					{
						
						
				    	StudentAccount student = new StudentAccount();
				  
						String password = EnterNewPassword.getText();
						String Email = EmailText.getText();
				
						SQLConnection connecting = new SQLConnection();
						String connectionurl = connecting.connect();

						boolean resultSet;
						try (Connection connection = DriverManager.getConnection(connectionurl);
								Statement statement = connection.createStatement();) {

							// Updates the account with the new password when they request it
							String string1 = "'"+password+"'";
							String string2 = "'"+Email+"'";
							String selectSql = "update dbo.User_Info set Passwords = " + string1+ " where Username = " + string2; //dbo.Essays
							resultSet = statement.execute(selectSql);


						} catch (SQLServerException e1) {
							e1.printStackTrace();
						} catch (SQLException e2) {
							e2.printStackTrace();
						}
						
						EnterNewPassword.setEditable(false);   
						RepeatNewPassword.setEditable(false);
						
						pane3.add(writtenText, 1, 6);
					    writtenText.setFill(Color.FIREBRICK);
						writtenText.setText("Password Changed Succesfully");
				
					} else
						
					{
						Image Wrong = new Image("x image.png");
	    				Circle check1 = new Circle(10);
	    				check1.setFill(new ImagePattern(Wrong));
	    				pane3.add(check1, 4, 1);
	    				
	    				Image Wrong2 = new Image("x image.png");
    					Circle check2 = new Circle(10);
    					check2.setFill(new ImagePattern(Wrong2));
    					pane3.add(check2, 4, 2);
					}
				
			
				
				
		
			
		});
		
		Back = new Button("Finish");
		pane3.add(Back, 2, 5);		
	
		Back.setOnAction(e-> {  
			
			fifthOne.hide();
			
		});
		
	}
	

	@Override
	public void start(Stage arg0) throws Exception {
		
	}
	

}