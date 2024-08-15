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
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class UpdateCustomerFormController implements Initializable {

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

    @FXML
    private TextField txtSearchBar;

    private Customer selectedCustomer;

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (selectedCustomer == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "No customer selected to update.");
            return;
        }

        if (isValidCustomer()) {
            selectedCustomer.setTitle(cmbTitle.getValue());
            selectedCustomer.setFirstName(txtFirstName.getText());
            selectedCustomer.setLastName(txtLastName.getText());
            selectedCustomer.setAddress(txtAddress.getText());
            selectedCustomer.setDob(dateDOB.getValue());
            selectedCustomer.setPhoneNumber(txtPhoneNumber.getText());

            List<Customer> customerList = DBConnection.getInstance().getConnection();

            int index = -1;
            for (int i = 0; i < customerList.size(); i++) {
                if (customerList.get(i).getId().equals(selectedCustomer.getId())) {
                    index = i;
                    break;
                }
            }

            if (index != -1) {
                customerList.set(index, selectedCustomer);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Customer updated successfully.");
                clearFields();

                Stage stage = new Stage();
                try {
                    stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/view_customers_form.fxml"))));
                    stage.show();

                    Stage disposeStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    disposeStage.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Customer not found.");
            }
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

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String searchTerm = txtSearchBar.getText().trim();
        if (searchTerm.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Search term cannot be empty.");
            return;
        }

        List<Customer> customerList = DBConnection.getInstance().getConnection();
        Customer foundCustomer = null;

        String[] nameParts = searchTerm.split("\\s+");
        if (nameParts.length == 2) {
            String firstName = nameParts[0];
            String lastName = nameParts[1];

            for (Customer customer : customerList) {
                if (customer.getFirstName().equalsIgnoreCase(firstName) &&
                        customer.getLastName().equalsIgnoreCase(lastName)) {
                    foundCustomer = customer;
                    break;
                }
            }
        } else {
            for (Customer customer : customerList) {
                if (customer.getFirstName().equalsIgnoreCase(searchTerm) ||
                        customer.getLastName().equalsIgnoreCase(searchTerm) ||
                        customer.getPhoneNumber().equals(searchTerm)) {
                    foundCustomer = customer;
                    break;
                }
            }
        }

        if (foundCustomer != null) {
            setCustomerData(foundCustomer);
        } else {
            showAlert(Alert.AlertType.INFORMATION, "Not Found", "No customer found with the provided search term.");
        }
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

    public void setCustomerData(Customer customer) {
        this.selectedCustomer = customer;
        txtID.setText(customer.getId());
        cmbTitle.setValue(customer.getTitle());
        txtFirstName.setText(customer.getFirstName());
        txtLastName.setText(customer.getLastName());
        txtAddress.setText(customer.getAddress());
        dateDOB.setValue(customer.getDob());
        txtPhoneNumber.setText(customer.getPhoneNumber());
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

    private boolean isValidSriLankanPhoneNumber(String phoneNumber) {
        Pattern pattern = Pattern.compile("^(?:0|94)?(7\\d{8})$");
        return pattern.matcher(phoneNumber).matches();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadTitles();
    }
}
