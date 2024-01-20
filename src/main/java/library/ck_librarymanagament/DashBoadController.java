package library.ck_librarymanagament;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class DashBoadController {
    @FXML
    private Button home;
    @FXML
    private Button exit;
    @FXML
    private Label curriculum;
    @FXML
    private Label available;
    @FXML
    private Label entertainment;
    private Connection connection;
    @FXML
    private BarChart<String, Number> barChart;


    public void btnhome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Library.fxml"));
            Parent root = loader.load();


            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            // Đóng cửa sổ hiện tại
            Stage currentStage = (Stage) home.getScene().getWindow();
            currentStage.close();

            // Mở cửa sổ mới (màn hình chính)
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void btnexit() {
        System.exit(0);

    }
    @FXML
    public void initialize() {
        initializeDatabase();
        updateStatistics();
    }

    private void initializeDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/quanlithuvien", "root", "@123Java");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateStatistics() {
        int curriculumBooks = countBooksByCategory("Giáo trình");
        int entertainmentBooks = countBooksByCategory("Giải trí");
        int availableBooks = getTotalBooks() ;
//        System.out.println("Total Books: " + getTotalBooks());
//        System.out.println("Curriculum Books: " + curriculumBooks);
//        System.out.println("Entertainment Books: " + entertainmentBooks);
//        System.out.println("Available Books: " + availableBooks);

        updateLabels(curriculumBooks, entertainmentBooks, availableBooks);
        updateChart(curriculumBooks, entertainmentBooks, availableBooks);
    }

    private int countBooksByCategory(String title) {
        String query = "SELECT COUNT(*) FROM quanlithuvien.library WHERE Title = ?";
        return executeCountQuery(query, title);
    }

    private int getTotalBooks() {
        String query = "SELECT COUNT(*) FROM quanlithuvien.library";
        return executeCountQuery(query, null);
    }

    private int executeCountQuery(String query, String title) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            if (title != null) {
                preparedStatement.setString(1, title);
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void updateLabels(int curriculumBooks, int entertainmentBooks, int availableBooks) {
        // Cập nhật nhãn
        // curriculum, entertainment, available là các FXML Labels
        // Đã giả sử các label này đã được kết nối trong FXML
        curriculum.setText(String.valueOf(curriculumBooks));
        entertainment.setText(String.valueOf(entertainmentBooks));
        available.setText(String.valueOf(availableBooks));
    }

    private void updateChart(int curriculumBooks, int entertainmentBooks, int availableBooks) {
        barChart.getData().clear();

        // Tạo biểu đồ mới
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);

        // Tạo dữ liệu biểu đồ
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("Curriculum", curriculumBooks));
        series.getData().add(new XYChart.Data<>("Entertainment", entertainmentBooks));
        series.getData().add(new XYChart.Data<>("Available", availableBooks));

        // Thêm dữ liệu vào biểu đồ
        chart.getData().add(series);
        barChart.setData(chart.getData());
    }

}









