package controller;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ViewCustomersFormController implements Initializable {

    @FXML
    private TableColumn<?, ?> colAddress;

    @FXML
    private TableColumn<?, ?> colDOB;

    @FXML
    private TableColumn<?, ?> colFirstName;

    @FXML
    private TableColumn<?, ?> colID;

    @FXML
    private TableColumn<?, ?> colLastName;

    @FXML
    private TableColumn<?, ?> colTitle;

    @FXML
    private TableColumn<?, ?> colPhoneNumber;

    @FXML
    private TableView<Customer> tblCustomers;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configureTable();
        loadTable();
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
    void btnDeleteOnAction(ActionEvent event) {
        Customer selectedCustomer = tblCustomers.getSelectionModel().getSelectedItem();
        if (selectedCustomer != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/delete_customer_form.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(loader.load()));
                DeleteCustomerFormController controller = loader.getController();
                controller.setCustomerData(selectedCustomer);
                stage.show();

                Stage currentStage = (Stage) tblCustomers.getScene().getWindow();
                currentStage.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        Customer selectedCustomer = tblCustomers.getSelectionModel().getSelectedItem();
        if (selectedCustomer != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/update_customer_form.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(loader.load()));
                UpdateCustomerFormController controller = loader.getController();
                controller.setCustomerData(selectedCustomer);
                stage.show();

                Stage currentStage = (Stage) tblCustomers.getScene().getWindow();
                currentStage.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void configureTable() {
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colDOB.setCellValueFactory(new PropertyValueFactory<>("dob"));
        colPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
    }

    private void loadTable() {
        List<Customer> customerList = DBConnection.getInstance().getConnection();
        ObservableList<Customer> customerObservableList = FXCollections.observableArrayList();

        customerList.forEach(obj -> {
            customerObservableList.add(obj);
        });

        tblCustomers.setItems(customerObservableList);
    }
}
