package diplom.ponikarov.controller;

import diplom.ponikarov.ControllerViewLoader;
import diplom.ponikarov.entity.ClimateData;
import diplom.ponikarov.service.ClimateDataService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

@Component
public class MainViewController extends AbstractController implements Initializable {

    @FXML
    private TableView<ClimateData> tableId;
    @FXML
    private TableColumn<ClimateData, Integer> tableColumnControllerNumber;
    @FXML
    private TableColumn<ClimateData, Date> tableColumnDate;
    @FXML
    private TableColumn<ClimateData, Float> tableColumnTemperature;
    @FXML
    private TableColumn<ClimateData, Float> tableColumnHumidity;
    @FXML
    private TableColumn<ClimateData, String> tableColumnStatus;
    @FXML
    private ComboBox<Integer> selectControllerNumber;
    @Autowired
    private Integer historyDataCount;
    @Autowired
    private ClimateDataService climateDataService;

    @Value("#{'${controllersNumber}'.split(',')}")
    private List<Integer> controllersNumber;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //fill combo box by controllers number
        selectControllerNumber.setItems(FXCollections.observableArrayList(controllersNumber));

        //fill table by climate data from db
        tableInitialize();

        //load history data from repository
        refreshHistoryData();
    }

    @FXML
    public void openControllerDetails() {

        ControllerDetailsController controller = (ControllerDetailsController) ControllerViewLoader.load("/fxml/controllerDetails.fxml");

        Integer controllerNumber = selectControllerNumber.getValue();
        controller.initControllerDetailsController(controllerNumber);

        ControllerViewLoader.view(controller, "SerialController details");
    }

    @FXML
    public void refreshHistoryData() {

        List<ClimateData> data = climateDataService.getAllWithLimit(historyDataCount);

        tableId.setItems(FXCollections.observableArrayList(data));
    }

    private void tableInitialize() {
        tableColumnControllerNumber.setCellValueFactory(new PropertyValueFactory<>("controllerNumber"));
        tableColumnDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        tableColumnTemperature.setCellValueFactory(new PropertyValueFactory<>("temperature"));
        tableColumnHumidity.setCellValueFactory(new PropertyValueFactory<>("humidity"));
        tableColumnStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }
}