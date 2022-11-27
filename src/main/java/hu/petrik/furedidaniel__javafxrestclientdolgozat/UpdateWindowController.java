package hu.petrik.furedidaniel__javafxrestclientdolgozat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.regex.Pattern;

public class UpdateWindowController extends Controller{

    @FXML
    private Button submitButton;
    @FXML
    private ComboBox<Boolean> isHiringField;
    @FXML
    private Spinner<Integer> hiringPriorityField;
    @FXML
    private TextField jobTitleField;
    @FXML
    private TextField companyNameField;
    @FXML
    private TextField companyEmailField;

    private Job selectedJob;

    public void setJob(Job job){
        this.selectedJob = job;
        jobTitleField.setText(this.selectedJob.getJobTitle());
        companyNameField.setText(this.selectedJob.getCompanyName());
        companyEmailField.setText(this.selectedJob.getCompanyEmail());
        hiringPriorityField.getValueFactory().setValue(this.selectedJob.getHiringPriority());
        isHiringField.getSelectionModel().select(this.selectedJob.getIsHiring());
    }

    @FXML
    private void initialize(){
        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 10);
        hiringPriorityField.setValueFactory(valueFactory);
        ObservableList<Boolean> options =
                FXCollections.observableArrayList(true, false);
        isHiringField.setItems(options);
        isHiringField.getSelectionModel().selectFirst();
    }

    @FXML
    public void submitClick(ActionEvent actionEvent) {
        String jobTitle = jobTitleField.getText().trim();
        String companyName = companyNameField.getText().trim();
        String companyEmail = companyEmailField.getText().trim();
        String emailRegex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        int hiringPriority = hiringPriorityField.getValue();
        boolean isHiring = isHiringField.getValue();

        if (jobTitle.isEmpty() || companyName.isEmpty() || companyEmail.isEmpty() || !regexTest(companyEmail, emailRegex)) {
            warning("All fields must be filled and a valid email address is needed");
        }

        this.selectedJob.setJobTitle(jobTitle);
        this.selectedJob.setCompanyName(companyName);
        this.selectedJob.setCompanyEmail(companyEmail);
        this.selectedJob.setHiringPriority(hiringPriority);
        this.selectedJob.setHiring(isHiring);
        Gson converter = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String json = converter.toJson(this.selectedJob);

        try {
            String url = App.BASE_URL + "/" + this.selectedJob.getId();
            Response response = RequestHandler.put(url, json);
            if (response.getResponseCode() == 200){
                Stage stage = (Stage)this.submitButton.getScene().getWindow();
                stage.close();
            } else {
                String content = response.getContent();
                error(content);
            }
        } catch (IOException e){
            error("Couldn't update data");
        }
    }

    private boolean regexTest(String email, String regex){
        return Pattern.compile(regex)
                .matcher(email)
                .matches();
    }
}
