package library.ck_librarymanagament;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConection {
 
    public static Connection getConnection(){
      Connection databaseLink = null;
        String databaseName="quanlithuvien";
        String databaseUser="root";
        String databasePassword="@123Java";
        String url="jdbc:mysql://localhost/"+databaseName;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink= DriverManager.getConnection(url,databaseUser,databasePassword);
            System.out.println("Thành công");
        }catch (Exception e){
            e.printStackTrace();
        }
        return databaseLink;
    }
}
