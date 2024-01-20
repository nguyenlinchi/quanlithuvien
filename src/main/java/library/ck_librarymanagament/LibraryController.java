package library.ck_librarymanagament;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Arrays;
import java.util.List;

public class LibraryController {


    @FXML
    private TableView<library> tableView;
    @FXML
    private TableColumn<library, Integer> id;
    @FXML
    private TableColumn<library, String> namebook;
    @FXML
    private TableColumn<library, String> author;
    @FXML
    private TableColumn<library, String> title;
    @FXML
    private TableColumn<library, String> describe;
    private Connection connection;
    @FXML
    private TextField ID;
    @FXML
    private TextField Namebook;
    @FXML
    private TextField Author;
    @FXML
    private ChoiceBox<String> Title;
    @FXML
    private TextField Describe;
    @FXML
    private TextField btnsearch;
    @FXML
    private  Button btnDashboard;
    @FXML
    private  Button btnclear;


    public void initialize() {

        id.setCellValueFactory(new PropertyValueFactory<>("ID"));


        namebook.setCellValueFactory(new PropertyValueFactory<>("Namebook"));
        author.setCellValueFactory(new PropertyValueFactory<>("Author"));
        title.setCellValueFactory(new PropertyValueFactory<>("Title"));
        describe.setCellValueFactory(new PropertyValueFactory<>("Describe"));
        initializeTitleChoices();
        loadDataFromDatabase();
    }

    private void loadDataFromDatabase() {
        try {
            // Kiểm tra xem đối tượng connection đã được khởi tạo hay chưa
            if (connection == null) {
                // Khởi tạo và kết nối tới cơ sở dữ liệu
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/quanlithuvien", "root", "@123Java");
            }

            String query = "SELECT * FROM quanlithuvien.library";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    int id = resultSet.getInt("ID");
                    String namebook = resultSet.getString("Namebook");
                    String author = resultSet.getString("Author");
                    String title = resultSet.getString("Title");
                    String describe = resultSet.getString("Describe");

                    library libraryDB = new library(id, namebook, author, title, describe);

                    tableView.getItems().add(libraryDB);
                }
            }
        } catch (Exception e) {
            // Xử lý ngoại lệ SQL
            e.printStackTrace();
        }
    }
    private void initializeTitleChoices() {
        // Giả sử bạn có một danh sách các tiêu đề sách
        List<String> bookTitles = Arrays.asList("Giáo trình", "Giải trí");

        Title.getItems().addAll(bookTitles);
    }
    public void addIBook(ActionEvent actionEvent) {
        // Tạo và hiển thị cửa sổ/hộp thoại để nhập thông tin mới

        // Lấy thông tin từ các trường nhập
        int Id = Integer.parseInt(ID.getText());
        String namebook = Namebook.getText();
        String author = Author.getText();
        String title = Title.getValue();
        String describe = Describe.getText();

        try {
            // Thêm sách mới vào cơ sở dữ liệu
            String insertQuery = "INSERT INTO quanlithuvien.library (ID,Namebook, Author, Title, `Describe`) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setInt(1, Id);
                preparedStatement.setString(2, namebook);
                preparedStatement.setString(3, author);
                preparedStatement.setString(4, title);
                preparedStatement.setString(5, describe);


                preparedStatement.executeUpdate();

                // Cập nhật TableView
                tableView.getItems().clear(); // Xóa dữ liệu cũ
                loadDataFromDatabase(); // Nạp lại dữ liệu từ cơ sở dữ liệu
            }
        } catch (Exception e) {
            // Xử lý ngoại lệ SQL
            e.printStackTrace();
        }
    }

    public void editBook(ActionEvent actionEvent) {
        try {
            int id= Integer.parseInt(ID.getText());
            String namebook = Namebook.getText();
            String author = Author.getText();
            String title = Title.getValue();
            String describe = Describe.getText();

            library selectedBook = tableView.getSelectionModel().getSelectedItem();
            if (selectedBook == null) {
                // Hiển thị thông báo lỗi hoặc cảnh báo nếu không có hàng nào được chọn
                // Ví dụ: Alert.show("Không có sách nào được chọn để chỉnh sửa");
                return;
            }

            // Lấy ID của sách được chọn
            int editedId = selectedBook.getID();

            // Cập nhật sách trong cơ sở dữ liệu
            String updateQuery = "UPDATE quanlithuvien.library SET Namebook=?, Author=?, Title=?, `Describe`=? WHERE ID=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                preparedStatement.setString(1, namebook);
                preparedStatement.setString(2, author);
                preparedStatement.setString(3, title);
                preparedStatement.setString(4, describe);
                preparedStatement.setInt(5, editedId);

                // Thực hiện câu truy vấn UPDATE
                int rowsUpdated = preparedStatement.executeUpdate();

                if (rowsUpdated > 0) {
                    System.out.println("Cập nhật thành công.");
                    // Cập nhật TableView
                    tableView.getItems().clear(); // Xóa dữ liệu cũ
                    loadDataFromDatabase(); // Nạp lại dữ liệu từ cơ sở dữ liệu
                } else {
                    System.out.println("Không có dòng nào được cập nhật.");
                }
            }
        } catch (Exception e) {
            // Xử lý ngoại lệ SQL
            e.printStackTrace();
        }
    }



    public  void clearBook(ActionEvent actionEvent) {
        ID.clear();
        Namebook.clear();
        Author.clear();
        Describe.clear();

    }
    public void searchBook(ActionEvent actionEvent) {
        String searchText = btnsearch.getText();
        if (searchText != null && !searchText.isEmpty()) {
            try {
                String query = "SELECT * FROM quanlithuvien.library WHERE Namebook LIKE ? OR Author LIKE ?  OR Title LIKE ?   OR ID LIKE ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                    preparedStatement.setString(1, "%" + searchText + "%");
                    preparedStatement.setString(2, "%" + searchText + "%");
                    preparedStatement.setString(3, "%" + searchText + "%");
                    preparedStatement.setString(4, "%" + searchText + "%");


                    ResultSet resultSet = preparedStatement.executeQuery();

                    tableView.getItems().clear();

                    while (resultSet.next()) {
                        int id = resultSet.getInt("ID");
                        String namebook = resultSet.getString("Namebook");
                        String author = resultSet.getString("Author");
                        String title = resultSet.getString("Title");
                        String describe = resultSet.getString("Describe");

                        library libraryDB = new library(id, namebook, author, title, describe);

                        tableView.getItems().add(libraryDB);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Không tìm thấy thông tin.");
        }
    }


    public void deleteBook(ActionEvent actionEvent) {
        // Lấy mục đã chọn từ TableView
        library selectedLibrary = tableView.getSelectionModel().getSelectedItem();

        if (selectedLibrary == null) {
            System.out.println("Vui lòng chọn một cuốn sách để xóa.");
            return;
        }

        try {
            // Xóa sách khỏi cơ sở dữ liệu
            String deleteQuery = "DELETE FROM quanlithuvien.library WHERE ID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
                preparedStatement.setInt(1, selectedLibrary.getID());

                // Thực hiện câu truy vấn DELETE
                preparedStatement.executeUpdate();

                tableView.getItems().clear(); // Xóa dữ liệu cũ
                loadDataFromDatabase(); // Nạp lại dữ liệu từ cơ sở dữ liệu
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    public void btnDashboard(ActionEvent actionEvent) throws IOException {
            Stage stage=(Stage) btnDashboard.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Dashboad.fxml"));
            Scene scene =new Scene(fxmlLoader.load(),800,520);
            stage.setScene(scene);
            stage.show();

    }

    public  void exitBook(ActionEvent actionEvent){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Parent root = loader.load();

            HelloController Controller = loader.getController();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            // Đóng giao diện quản lý thư viện
            ((Stage) tableView.getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}


