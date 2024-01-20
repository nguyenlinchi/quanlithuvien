package library.ck_librarymanagament;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class HelloController {

    @FXML
    private Button cancelButton;
    @FXML
    private Label loginMessageLabel;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordTextField;
    public  void loginLabel(ActionEvent e){

        if( usernameTextField.getText().isBlank()==false && passwordTextField.getText().isBlank()==false){
//            loginMessageLabel.setText("You try to login!");
            validateLogin();

        }else {
            loginMessageLabel.setText("Please enter user and password.");

        }
    }

    public void cancelButtton(ActionEvent e){
        Stage stage=(Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
    public  void validateLogin(){
        DatabaseConection conectionNow=new DatabaseConection();
        Connection connectionDB=conectionNow.getConnection();

        String verifyLogin="SELECT count(1) FROM useraccounts WHERE username = '"+usernameTextField.getText()+"'AND password='"+passwordTextField.getText()+"'";
        try{
            Statement statement =connectionDB.createStatement();
            ResultSet queryResult=statement.executeQuery(verifyLogin);
            while (queryResult.next()){
                if( queryResult.getInt(1)==1){
                    loginMessageLabel.setText("Welcome");
                    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("library.fxml"));
                    Scene newScene = new Scene(fxmlLoader.load(), 1000, 500);

                    // Lấy stage hiện tại và thiết lập cảnh mới
                    Stage currentStage = (Stage) loginMessageLabel.getScene().getWindow();
                    currentStage.setScene(newScene);
                }else {
                    loginMessageLabel.setText("Invalind Login.PLease try again");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}