package hu.petrik.furedidaniel__javafxrestclientdolgozat;

import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class MainWindowController extends Controller{

    @FXML
    private Button insertButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;


    @FXML
    private TableView<Job> jobsTable;
    @FXML
    private TableColumn<Job, String> jobTitleCol;
    @FXML
    private TableColumn<Job, String> companyNameCol;
    @FXML
    private TableColumn<Job, String> companyEmailCol;
    @FXML
    private TableColumn<Job, Integer> hiringPriorityCol;
    @FXML
    private TableColumn<Job, Boolean> isHiringCol;


    @FXML
    private void initialize(){
        jobTitleCol.setCellValueFactory(new PropertyValueFactory<>("jobTitle"));
        companyNameCol.setCellValueFactory(new PropertyValueFactory<>("companyName"));
        companyEmailCol.setCellValueFactory(new PropertyValueFactory<>("companyEmail"));
        hiringPriorityCol.setCellValueFactory(new PropertyValueFactory<>("hiringPriority"));
        isHiringCol.setCellValueFactory(new PropertyValueFactory<>("isHiring"));

        Platform.runLater(() -> {
            try {
                loadDataFromServer();
            } catch (IOException e) {
                error("Couldn't get data from server");
                Platform.exit();
            }
        });
    }

    private void loadDataFromServer() throws IOException {
        Response response = RequestHandler.get(App.BASE_URL);
        String content = response.getContent();
        Gson converter = new Gson();
        Job[] jobs = converter.fromJson(content, Job[].class);
        jobsTable.getItems().clear();
        for (Job job : jobs){
            jobsTable.getItems().add(job);
        }
    }

    @FXML
    public void insertClick(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("create-data-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 640, 400);
            Stage stage = new Stage();
            stage.setTitle("Insert Data");
            stage.setScene(scene);
            stage.show();
            insertButton.setDisable(true);
            updateButton.setDisable(true);
            deleteButton.setDisable(true);
            stage.setOnHidden(e -> {
                insertButton.setDisable(false);
                updateButton.setDisable(false);
                deleteButton.setDisable(false);
                try {
                    loadDataFromServer();
                } catch (IOException ex) {
                    error("Couldn't get data from server");
                }
            });
        } catch (IOException e) {
            error("Couldn't load form");
        }
    }

    @FXML
    public void updateClick(ActionEvent actionEvent) {
        int selectedDataIndex = jobsTable.getSelectionModel().getSelectedIndex();
        if (selectedDataIndex == -1) {
            warning("Select a job to update");
            return;
        }
        Job selected = jobsTable.getSelectionModel().getSelectedItem();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("update-data-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 640, 480);
            Stage stage = new Stage();
            stage.setTitle("Update data entries");
            stage.setScene(scene);
            UpdateWindowController controller = fxmlLoader.getController();
            controller.setJob(selected);
            stage.show();
            insertButton.setDisable(true);
            updateButton.setDisable(true);
            deleteButton.setDisable(true);
            stage.setOnHidden(e -> {
                insertButton.setDisable(false);
                updateButton.setDisable(false);
                deleteButton.setDisable(false);
                try {
                    loadDataFromServer();
                } catch (IOException ex) {
                    error("Couldn't get data from server");
                }
            });
        } catch(IOException e){
            error("Couldn't get data from server");
        }
    }

    @FXML
    public void deleteClick(ActionEvent actionEvent) {
        int selectedDataIndex = jobsTable.getSelectionModel().getSelectedIndex();
        if (selectedDataIndex == -1) {
            warning("Select a job to delete");
            return;
        }
        Job selected = jobsTable.getSelectionModel().getSelectedItem();

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setHeaderText("Are you sure you want to delete the selected data entry?");
        Optional<ButtonType> optionalButtonType = confirmation.showAndWait();

        if (optionalButtonType.isEmpty()){
            error("Unknown error occured");
            return;
        }
        ButtonType clickedButton = optionalButtonType.get();
        if (clickedButton.equals(ButtonType.OK)){
            String url = App.BASE_URL + "/" + selected.getId();
            try {
                RequestHandler.delete(url);
                loadDataFromServer();
            } catch (IOException e){
                error("An error occured while trying to delete data from server");
            }
        }
    }
}