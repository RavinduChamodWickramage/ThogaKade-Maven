package controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class AddCustomerFormController implements Initializable {

    @FXML
    private JFXComboBox<String> cmbTitle;

    @FXML
    private DatePicker dateDOB;

    @FXML
    private JFXTextField txtAddress;

    @FXML
    private JFXTextField txtFirstName;

    @FXML
    private JFXTextField txtID;

    @FXML
    private JFXTextField txtLastName;

    @FXML
    private JFXTextField txtPhoneNumber;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadTitles();
        txtID.setText(generateCustomerId());
    }

    @FXML
    void btnAddOnAction(ActionEvent event) {
        if (isValidCustomer()) {
            String id = txtID.getText();
            String title = cmbTitle.getValue();
            String firstName = txtFirstName.getText();
            String lastName = txtLastName.getText();
            String address = txtAddress.getText();
            LocalDate dob = dateDOB.getValue();
            String phoneNumber = txtPhoneNumber.getText();

            Customer customer = new Customer(id, title, firstName, lastName, address, dob, phoneNumber);

            List<Customer> customerList = DBConnection.getInstance().getConnection();
            customerList.add(customer);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Customer added successfully.");
            clearFields();

            txtID.setText(generateCustomerId());
        }
    }
    @FXML
    void btnBackDashBoardOnAction(ActionEvent event) {
        Stage stage = new Stage();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/dash_board_form.fxml"))));
            stage.show();

            Stage disposeStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            disposeStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        txtID.setText("");
        cmbTitle.setValue(null);
        txtFirstName.setText("");
        txtLastName.setText("");
        txtAddress.setText("");
        dateDOB.setValue(null);
        txtPhoneNumber.setText("");
    }


    private void loadTitles() {
        ObservableList<String> titles = FXCollections.observableArrayList();
        titles.add("MR");
        titles.add("Miss");
        titles.add("MRS");
        cmbTitle.setItems(titles);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean isValidCustomer() {
        String id = txtID.getText();
        String title = cmbTitle.getValue();
        String firstName = txtFirstName.getText();
        String lastName = txtLastName.getText();
        String address = txtAddress.getText();
        LocalDate dob = dateDOB.getValue();
        String phoneNumber = txtPhoneNumber.getText();

        List<Customer> customerList = DBConnection.getInstance().getConnection();
        for (Customer customer : customerList) {
            if (customer.getId().equals(id)) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "ID already exists.");
                return false;
            }
        }

        if (title == null || title.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Title cannot be empty.");
            return false;
        }

        if (firstName == null || firstName.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "First Name cannot be empty.");
            return false;
        }

        if (lastName == null || lastName.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Last Name cannot be empty.");
            return false;
        }

        if (address == null || address.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Address cannot be empty.");
            return false;
        }

        if (dob == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Date of Birth cannot be empty.");
            return false;
        }

        LocalDate today = LocalDate.now();
        if (dob.isAfter(today)) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Date of Birth cannot be in the future.");
            return false;
        }

        if (Period.between(dob, today).getYears() > 120) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Date of Birth is too far in the past.");
            return false;
        }

        if (title == null || title.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Title cannot be empty.");
            return false;
        }

        if (firstName == null || firstName.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "First Name cannot be empty.");
            return false;
        }

        if (lastName == null || lastName.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Last Name cannot be empty.");
            return false;
        }

        if (address == null || address.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Address cannot be empty.");
            return false;
        }

        if (dob == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Date of Birth cannot be empty.");
            return false;
        }

        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Phone Number cannot be empty.");
            return false;
        }


        if (!isValidSriLankanPhoneNumber(phoneNumber)) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Invalid Sri Lankan phone number.");
            return false;
        }


        return true;
    }

    private String generateCustomerId() {
        List<Customer> customerList = DBConnection.getInstance().getConnection();

        if (customerList.isEmpty()) {
            return "C001";
        }

        String lastCustomerId = customerList.stream()
                .max(Comparator.comparing(Customer::getId))
                .get()
                .getId();

        int lastCustomerNumber = Integer.parseInt(lastCustomerId.substring(1));
        int newCustomerNumber = lastCustomerNumber + 1;

        return String.format("C%03d", newCustomerNumber);
    }

    private boolean isValidSriLankanPhoneNumber(String phoneNumber) {
        Pattern pattern = Pattern.compile("^(?:0|94)?(7\\d{8})$");
        return pattern.matcher(phoneNumber).matches();
    }

}
