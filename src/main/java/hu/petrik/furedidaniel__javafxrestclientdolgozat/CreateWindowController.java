package hu.petrik.furedidaniel__javafxrestclientdolgozat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.regex.Pattern;

public class CreateWindowController extends Controller{

    @FXML
    private Button submitButton;
    @FXML
    private TextField jobTitleField;
    @FXML
    private TextField companyNameField;
    @FXML
    private TextField companyEmailField;
    @FXML
    private Spinner<Integer> hiringPriorityField;
    @FXML
    private ComboBox<Boolean> isHiringField;

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
    public void submitClick(ActionEvent actionEvent){
        String jobTitle = jobTitleField.getText().trim();
        String companyName = companyNameField.getText().trim();
        String companyEmail = companyEmailField.getText().trim();
        String emailRegex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        int hiringPriority = hiringPriorityField.getValue();
        boolean isHiring = isHiringField.getValue();

        if (jobTitle.isEmpty() || companyName.isEmpty() || companyEmail.isEmpty() || !regexTest(companyEmail, emailRegex)) {
            warning("All fields must be filled and a valid email address is needed");
        }

        Job insertedJob = new Job(0, jobTitle, companyName, companyEmail, hiringPriority, isHiring);
        Gson converter = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String json = converter.toJson(insertedJob);

        try {
            Response response = RequestHandler.post(App.BASE_URL, json);
            if (response.getResponseCode() == 201){
                warning("New data added to list");
                jobTitleField.setText("");
                companyNameField.setText("");
                companyEmailField.setText("");
                hiringPriorityField.getValueFactory().setValue(10);
            }
            else {
                error("Couldn't insert data to server");
            }
        } catch (IOException e) {
            error("Couldn't insert data to server");
        }
    }

    private boolean regexTest(String email, String regex){
        return Pattern.compile(regex)
                .matcher(email)
                .matches();
    }
}
