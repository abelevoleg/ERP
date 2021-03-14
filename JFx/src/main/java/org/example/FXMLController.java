package org.example;

import java.io.*;
import java.net.URL;
import java.text.ParseException;
import java.util.*;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.converter.DoubleStringConverter;


public class FXMLController implements Initializable {
    ObservableList<Order> orderList = FXCollections.observableArrayList();
    private ObservableList<MaterialDataForOrder> materialListForOrder = FXCollections.observableArrayList();
    private ObservableList<MaterialDataForOrder> materialListInOrder = FXCollections.observableArrayList();
    private ObservableList<Order.StatusOfOrder> statusList = FXCollections.observableArrayList(List.of(Order.StatusOfOrder.values()));
    private ObservableList<String> statusNames = FXCollections.observableArrayList();
    private XYChart.Series<String, Integer> series = new XYChart.Series<>();
    private ArrayList<String> ncOrderNumbers = new ArrayList<>();
    private ArrayList<String> drillOrderNumbers = new ArrayList<>();
    private ArrayList<String> paintingOrderNumbers = new ArrayList<>();
    private ArrayList<String> packingOrderNumbers = new ArrayList<>();

    // площадь листа материала в метрах квадратных (стандартный размер 2,8х2,07м)
    static final double LISTAREA = 5.796;

    @FXML
    private MenuItem save;

    @FXML
    private MenuItem saveToFile;

    @FXML
    public MenuItem open;

    // гистограмма заказов в работе
    @FXML
    private BarChart<String, Integer> orderInWork;

    @FXML
    private CategoryAxis statusAxe;

    @FXML
    private NumberAxis quantityAxe;

    // список заказов
    @FXML
    TableView<Order> tableOrder;

    // список материалов для создания нового заказа
    @FXML
    private TableView<MaterialDataForOrder> tableMaterialNewOrder;

    // список материалов в выбранном заказе
    @FXML
    private TableView<MaterialDataForOrder> tableMaterialOrder;

    // колонка даты в списке заказов
    @FXML
    private TableColumn<Order, String> dateOrderColumn;

    // колонка номера заказа в списке заказов
    @FXML
    private TableColumn<Order, String> numberOrderColumn;

    // колонка статуса заказа в списке заказов
    @FXML
    private TableColumn<Order, String> statusOrderColumn;

    // колонка названия материала в списке для создания нового заказа
    @FXML
    private TableColumn<MaterialDataForOrder,String> materialNewColumn;

    // количество материала в списке для создания нового заказа
    @FXML
    private TableColumn<MaterialDataForOrder, Double> quantityNewColumn;

    // колонка названия материала в списке
    @FXML
    private TableColumn<MaterialDataForOrder,String> materialColumn;

    // количество материала в списке
    @FXML
    private TableColumn<MaterialDataForOrder, Double> quantityColumn;

    @FXML
    private Button enterprise;

    @FXML
    private Button warehouse;

    @FXML
    private Button archive;

    @FXML
    private Button newOrder;

    @FXML
    private Button saveOrder;

    @FXML
    private Button putOrder;

    @FXML
    private Button findOrder;

    @FXML
    private Button deleteOrder;

    @FXML
    private Button choiceMaterialInOrder;

    @FXML
    private Button saveMaterial;

    // поле ввода номера заказа для поиска
    @FXML
    private TextField numberToFind;

    // поля ввода данных нового заказа
    @FXML
    private TextField newDate;

    @FXML
    private TextField newNumber;

    @FXML
    private TextField newDescription;

    // поля ввода данных нового материала в новом заказе
    @FXML
    private TextField newQuantity;

    // поля данных просмотра и редактирования заказа
    @FXML
    private TextField date;

    @FXML
    private TextField number;

    @FXML
    private TextField description;

    @FXML
    private ChoiceBox<Order.StatusOfOrder> status;

    @FXML
    public ChoiceBox<String> material;

    // области вывода номеров заказов, соответствующих столбцам гистограммы
    @FXML
    private Label ncOrders;

    @FXML
    private Label drillOrders;

    @FXML
    private Label paintingOrders;

    @FXML
    private Label packingOrders;

    // активация полей ввода при нажатии кнопки нового заказа
    @FXML
    private void newOrder(ActionEvent event) {
        newDate.setDisable(false);
        newNumber.setDisable(false);
        newDescription.setDisable(false);
        choiceMaterialInOrder.setDisable(false);
        newQuantity.setDisable(false);
        saveOrder.setDisable(false);
        saveMaterial.setDisable(false);
        tableMaterialNewOrder.setDisable(false);
    }

    // выбор материала из базы
    @FXML
    private void choiceMaterialInOrder(ActionEvent actionEvent) throws IOException {
        FXMLMaterialController cont = Context.getInstance().getFXMLMaterialController();
        List<Material> materialListForOrder = FXCollections.observableArrayList();
        ObservableList<String> materialNameForOrder = FXCollections.observableArrayList();
        materialListForOrder.addAll(cont.materialList);
        for (Material m : materialListForOrder){
            materialNameForOrder.add(m.getName());
        }
        material.setItems(materialNameForOrder);
        material.setDisable(false);
    }

    // сохранение данных нового материала в таблице в новом заказе
    @FXML
    private void saveMaterial(ActionEvent event) {
        try {
            Double.parseDouble(newQuantity.getText());
        } catch (NumberFormatException e) {
            newQuantity.setStyle("-fx-text-inner-color:Red;");
            newQuantity.setText("*.*");
            Alert alert = new Alert(Alert.AlertType.ERROR, "Количество материала должно быть числом!");
            alert.showAndWait();
            return;
        }
        MaterialDataForOrder materialDataForOrder = new MaterialDataForOrder(material.getValue(), Double.parseDouble(newQuantity.getText()));
        materialListForOrder.add(materialDataForOrder);
        material.setValue(null);
        material.setDisable(true);
        newQuantity.clear();
        newQuantity.setStyle("-fx-text-inner-color:Black;");
    }

    // изменение количества материала в таблице материалов нового заказа
    @FXML
    private void changeNewQuantity(TableColumn.CellEditEvent<MaterialDataForOrder, Double> event) {
        try {
            event.getNewValue();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Количество материала должно быть числом!");
            alert.showAndWait();
            return;
        }
        event.getTableView().getItems().get(event.getTablePosition().getRow()).setQuantity(event.getNewValue());
    }

    // удаление выбранного материала в таблице материалов нового заказа
    @FXML
    private void deleteNewMaterial(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.DELETE) {
            int index = tableMaterialNewOrder.getSelectionModel().getFocusedIndex();
            materialListForOrder.remove(index);
        }
    }

    // сохранение нового заказа в таблице заказов
    @FXML
    private void saveOrder(ActionEvent event) {
        try {
            Order.dateformatddMMyyyy.parse(newDate.getText());
        } catch (ParseException e) {
            newDate.setStyle("-fx-text-inner-color:Red;");
            newDate.setText("dd.mm.yyyy");
            Alert alert = new Alert(Alert.AlertType.ERROR, "Неверный формат даты!");
            alert.showAndWait();
            return;
        }
        List<MaterialDataForOrder> materialListForSaveOrder = new ArrayList<>(materialListForOrder);
        Order order = new Order(newDate.getText(), newNumber.getText(), materialListForSaveOrder, newDescription.getText());
        orderList.add(order);
        Collections.sort(orderList);
        newDate.setStyle("-fx-text-inner-color:Black;");
        newDate.clear();
        newNumber.clear();
        newDescription.clear();
        material.setValue(null);
        newQuantity.clear();

        materialListForOrder.clear();
        
        newDate.setDisable(true);
        newNumber.setDisable(true);
        newDescription.setDisable(true);
        choiceMaterialInOrder.setDisable(true);
        material.setDisable(true);
        newQuantity.setDisable(true);
        saveOrder.setDisable(true);
        saveMaterial.setDisable(true);
        tableMaterialNewOrder.setDisable(true);
    }

    // выбор заказа в таблице заказов
    @FXML
    private void choiceOrder(MouseEvent event) {
        Order selectedOrder = tableOrder.getSelectionModel().getSelectedItem();
        number.setText(selectedOrder.getNumber());
        date.setText(selectedOrder.getDate());
        description.setText(selectedOrder.getDescription());
        status.setValue(selectedOrder.getStatus());
        materialListInOrder.clear();
        materialListInOrder.addAll(selectedOrder.getMaterialList());
        putOrder.setDisable(false);
        deleteOrder.setDisable(false);
    }

    // поиск заказа в таблице заказов
    @FXML
    private void findOrder(ActionEvent event) throws IOException {
        String findNumber = numberToFind.getText();
        if (!findOrder(findNumber)) {
            FXMLArchiveController archiveCont = Context.getInstance().getFXMLArchiveController();
            if (archiveCont.findOrder(findNumber)) {
                MainApp.changeRoot("archiveMode");
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Заказ не найден!");
                alert.showAndWait();
            }
        }
    }

    boolean findOrder(String findNumber) {
        for (Order order : orderList){
            if (findNumber.equals(order.getNumber())){
                Order selectedOrder;
                selectedOrder = order;
                tableOrder.requestFocus();
                tableOrder.getFocusModel().focus(orderList.indexOf(selectedOrder));
                tableOrder.scrollTo(selectedOrder);
                number.setText(selectedOrder.getNumber());
                date.setText(selectedOrder.getDate());
                description.setText(selectedOrder.getDescription());
                status.setValue(selectedOrder.getStatus());
                materialListInOrder.clear();
                materialListInOrder.addAll(selectedOrder.getMaterialList());
                putOrder.setDisable(false);
                deleteOrder.setDisable(false);
                return true;
            }
        }
        return false;
    }

//    // изменение материала в таблице материалов выбранного заказа
//    @FXML
//    private void changeMaterial(TableColumn.CellEditEvent<MaterialDataForOrder, String> event) {
//        event.getTableView().getItems().get(event.getTablePosition().getRow()).setMaterialName(event.getNewValue());
//    }

    // изменение количества материала в таблице материалов выбранного заказа
    @FXML
    private void changeQuantity(TableColumn.CellEditEvent<MaterialDataForOrder, Double> event) {
        try {
            event.getNewValue();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Количество материала должно быть числом!");
            alert.showAndWait();
            return;
        }
        event.getTableView().getItems().get(event.getTablePosition().getRow()).setQuantity(event.getNewValue());
    }

    // удаление выбранного материала в таблице материалов выбранного заказа
    @FXML
    private void deleteMaterial(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.DELETE) {
            int index = tableMaterialOrder.getSelectionModel().getFocusedIndex();
            materialListInOrder.remove(index);
        }
    }

    // сохранение измененного заказа
    @FXML
    private void putOrder(ActionEvent event) {
        // проверка формата даты
        try {
            Order.dateformatddMMyyyy.parse(date.getText());
        } catch (ParseException e) {
            date.setStyle("-fx-text-inner-color:Red;");
            Alert alert = new Alert(Alert.AlertType.ERROR, "Неверный формат даты!");
            alert.showAndWait();
            return;
        }

        // создание измененного заказа
        List<MaterialDataForOrder> materialListForPutOrder = new ArrayList<>(materialListInOrder);
        Order changedOrder = new Order(date.getText(), number.getText(), materialListForPutOrder, description.getText());
        changedOrder.setStatusOfOrder(status.getValue());
        int index = tableOrder.getSelectionModel().getFocusedIndex();

        // действия при изменении статуса заказа
        if (changedOrder.getStatus() != orderList.get(index).getStatus()){
            // проверка наличия материала на заказ, выдаваемый в работу
            if (orderList.get(index).getStatus() == Order.StatusOfOrder.NEW){
                FXMLMaterialController cont = Context.getInstance().getFXMLMaterialController();
                for (MaterialDataForOrder materialData : materialListForPutOrder) {
                    boolean materialExist = false;
                    for (Material m : cont.materialList) {
                        if (materialData.getMaterialName().equals(m.getName())) {
                            materialExist = true;
                            int materialQuantity = m.getQuantity() - (int) (materialData.getQuantity() / LISTAREA + 0.75);
                            if (materialQuantity < 0) {
                                Alert alert = new Alert(Alert.AlertType.ERROR, "Нет материала! Невозможно выдать заказ в работу!");
                                alert.showAndWait();
                                return;
                            }
                            break;
                        }
                    }
                    if (!materialExist) {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Нет материала! Невозможно выдать заказ в работу!");
                        alert.showAndWait();
                        return;
                    }
                }

                // списание материала, если материал в наличии и новый заказ выдается в работу
                for (MaterialDataForOrder materialData : materialListForPutOrder) {
                    for (Material m : cont.materialList) {
                        if (materialData.getMaterialName().equals(m.getName())) {
                            int materialQuantity = m.getQuantity() - (int) (materialData.getQuantity() / LISTAREA + 0.75);
                            m.setQuantity(materialQuantity);
                            break;
                        }
                    }
                }
                cont.tableMaterial.refresh();
                Context.getInstance().setFXMLMaterialController(cont);

                // возврат материала на склад, если заказ выдан ошибочно и переводится обратно в статус "NEW"
            } else if (changedOrder.getStatus() == Order.StatusOfOrder.NEW){
                FXMLMaterialController cont = Context.getInstance().getFXMLMaterialController();
                for (MaterialDataForOrder materialData : materialListForPutOrder){
                    for (Material m : cont.materialList){
                        if (materialData.getMaterialName().equals(m.getName())){
                            int materialQuantity = m.getQuantity() + (int) (materialData.getQuantity() / LISTAREA + 0.75);
                            m.setQuantity(materialQuantity);
                            cont.tableMaterial.refresh();
                            Context.getInstance().setFXMLMaterialController(cont);
                        }
                    }
                }
            }

            histogramUpdate(changedOrder, orderList.get(index));
        }

        // сохранение измененного заказа в таблицу
        orderList.set(index, changedOrder);

        // перенос заказа в архив, если новый статус "Отгружен"
        if (changedOrder.getStatus() == Order.StatusOfOrder.FINISH) {
            FXMLArchiveController archiveCont = Context.getInstance().getFXMLArchiveController();
            changedOrder.setDate(Order.dateformatddMMyyyy.format(new Date()) + "/" + changedOrder.getDate());
            archiveCont.orderArchiveList.add(changedOrder);
            archiveCont.tableArchiveOrder.refresh();
            Context.getInstance().setFXMLArchiveController(archiveCont);

            orderList.remove(index);
        } else Collections.sort(orderList);

        // очистка полей формы данных материала
        materialListInOrder.clear();
        date.setStyle("-fx-text-inner-color:Black;");
        date.clear();
        number.clear();
        description.clear();
        status.setValue(null);
        putOrder.setDisable(true);
        deleteOrder.setDisable(true);
    }

    // удаление заказа
    @FXML
    private void deleteOrder(ActionEvent event) {
        int index = tableOrder.getSelectionModel().getFocusedIndex();
        histogramUpdate(null, orderList.get(index));

        orderList.remove(index);
        materialListInOrder.clear();
        date.clear();
        number.clear();
        description.clear();
        status.setValue(null);
        putOrder.setDisable(true);
        deleteOrder.setDisable(true);
    }

    // инициализация гистограммы
    private void histogramInit(){
        String[] values = Arrays.stream(Order.StatusOfOrder.values()).map( value -> value.getStatus()).toArray( String[]::new );
        String[] valuesStatus = Arrays.copyOfRange(values, 1, values.length - 1);
        statusNames.addAll(Arrays.asList(valuesStatus));
        statusAxe.setCategories(statusNames);

        series.getData().add(new XYChart.Data<>("ЧПУ/раскрой", 0));
        series.getData().add(new XYChart.Data<>("Присадка", 0));
        series.getData().add(new XYChart.Data<>("Шлифовка/покраска", 0));
        series.getData().add(new XYChart.Data<>("На упаковке", 0));
        orderInWork.getData().add(series);
    }

    // загрузка данных списка заказов в гистограмму
    void histogramLoad(List<Order> orderList) {
        ncOrderNumbers.clear();
        drillOrderNumbers.clear();
        paintingOrderNumbers.clear();
        packingOrderNumbers.clear();
        int startCount = 0;
        int drillCount = 0;
        int paintingCount = 0;
        int packingCount = 0;
        for (Order order : orderList) {
            switch (order.getStatus()) {
                case NEW:
                    break;
                case START:
                    startCount++;
                    ncOrderNumbers.add(order.getNumber());
                    break;
                case DRILL:
                    drillCount++;
                    drillOrderNumbers.add(order.getNumber());
                    break;
                case PAINTING:
                    paintingCount++;
                    paintingOrderNumbers.add(order.getNumber());
                    break;
                case PACKING:
                    packingCount++;
                    packingOrderNumbers.add(order.getNumber());
                    break;
            }
        }
        series.getData().get(0).setYValue(startCount);
        series.getData().get(1).setYValue(drillCount);
        series.getData().get(2).setYValue(paintingCount);
        series.getData().get(3).setYValue(packingCount);

        setNumberToLabels();
    }

    // обновление гистограммы
    private void histogramUpdate(Order changedOrder, Order selectedOrder){
        switch (selectedOrder.getStatus()){
            case NEW:
                break;
            case START:
                series.getData().get(0).setYValue(series.getData().get(0).getYValue() - 1);
                ncOrderNumbers.remove(selectedOrder.getNumber());
                break;
            case DRILL:
                series.getData().get(1).setYValue(series.getData().get(1).getYValue() - 1);
                drillOrderNumbers.remove(selectedOrder.getNumber());
                break;
            case PAINTING:
                series.getData().get(2).setYValue(series.getData().get(2).getYValue() - 1);
                paintingOrderNumbers.remove(selectedOrder.getNumber());
                break;
            case PACKING:
                series.getData().get(3).setYValue(series.getData().get(3).getYValue() - 1);
                packingOrderNumbers.remove(selectedOrder.getNumber());
                break;
        }

        if (changedOrder != null) {
            switch (changedOrder.getStatus()) {
                case START:
                    series.getData().get(0).setYValue(series.getData().get(0).getYValue() + 1);
                    ncOrderNumbers.add(changedOrder.getNumber());
                    break;
                case DRILL:
                    series.getData().get(1).setYValue(series.getData().get(1).getYValue() + 1);
                    drillOrderNumbers.add(changedOrder.getNumber());
                    break;
                case PAINTING:
                    series.getData().get(2).setYValue(series.getData().get(2).getYValue() + 1);
                    paintingOrderNumbers.add(changedOrder.getNumber());
                    break;
                case PACKING:
                    series.getData().get(3).setYValue(series.getData().get(3).getYValue() + 1);
                    packingOrderNumbers.add(changedOrder.getNumber());
                    break;
            }
        }
        setNumberToLabels();
    }

    // установка в поля под гистограммой списка заказов
    private void setNumberToLabels(){
        String ncNumber = setTextOrderNumbers(ncOrderNumbers);
        ncOrders.setText(ncNumber);
        String drillNumber = setTextOrderNumbers(drillOrderNumbers);
        drillOrders.setText(drillNumber);
        String paintingNumber = setTextOrderNumbers(paintingOrderNumbers);
        paintingOrders.setText(paintingNumber);
        String packingNumber = setTextOrderNumbers(packingOrderNumbers);
        packingOrders.setText(packingNumber);
    }

    // получение строки для установки в поля под гистограммой
    private String setTextOrderNumbers(ArrayList<String> numberOrders){
        String typeNumber = "";
        for (String orderNumber : numberOrders) typeNumber = typeNumber + orderNumber + "\n";
        return typeNumber;
    }

    // переключение в режим склада (таблица материалов)
    @FXML
    private void setWarehouseMode(ActionEvent actionEvent) throws IOException {
        MainApp.changeRoot("materialMode");
    }

    // переключение в режим архива заказов
    @FXML
    public void setArchiveMode(ActionEvent actionEvent) throws IOException {
        MainApp.changeRoot("archiveMode");
    }

    // сохранение состояния таблиц заказов и материалов в текущий файл
    @FXML
    void save(){
        MenuController.save();
    }

    // сохранение состояния таблиц заказов и материалов в новый файл
    @FXML
    void saveToFile(){
        MenuController.saveToFile();
    }

    // загрузка состояния таблиц заказов и материалов из файла
    @FXML
    void openFromFile(){
        MenuController.openFromFile();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initData();
        dateOrderColumn.setCellValueFactory(new PropertyValueFactory<Order, String>("date"));
        numberOrderColumn.setCellValueFactory(new PropertyValueFactory<Order, String>("number"));
        statusOrderColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatusText()));
        tableOrder.setItems(orderList);

        materialNewColumn.setCellValueFactory(new PropertyValueFactory<>("materialName"));
        materialNewColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        quantityNewColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityNewColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        tableMaterialNewOrder.setItems(materialListForOrder);

        materialColumn.setCellValueFactory(new PropertyValueFactory<>("materialName"));
        materialColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        tableMaterialOrder.setItems(materialListInOrder);
        status.setItems(statusList);

        histogramInit();

        Context.getInstance().setFXMLController(this);
    }

    private void initData() {
        List<MaterialDataForOrder> materialOrderList = new ArrayList<>();
        materialOrderList.add(new MaterialDataForOrder("МДФ 19мм односторонний", 1.5));
        materialOrderList.add(new MaterialDataForOrder("МДФ 16мм односторонний", 2.5));
        Order order = new Order("11.03.2021", "1122", materialOrderList, "покраска");
        Order order1 = new Order("12.03.2021", "1155", materialOrderList, "покраска");
        Order order2 = new Order("10.03.2021", "99448", materialOrderList, "покраска");
        Order order3 = new Order("17.03.2021", "1307", materialOrderList, "покраска");
        Order order4 = new Order("15.03.2021", "1125", materialOrderList, "покраска");
        orderList.addAll(order, order1, order2, order3, order4);
        Collections.sort(orderList);
    }

}
