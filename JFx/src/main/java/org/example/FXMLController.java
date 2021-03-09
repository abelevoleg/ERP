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
    private ObservableList<Order> orderList = FXCollections.observableArrayList();
    private ObservableList<MaterialDataForOrder> materialListForOrder = FXCollections.observableArrayList();
    private ObservableList<MaterialDataForOrder> materialListInOrder = FXCollections.observableArrayList();
    private ObservableList<Order.StatusOfOrder> statusList = FXCollections.observableArrayList(List.of(Order.StatusOfOrder.values()));
    private ObservableList<String> statusNames = FXCollections.observableArrayList();
    private XYChart.Series<String, Integer> series = new XYChart.Series<>();
    private ArrayList<String> ncOrderNumbers = new ArrayList<>();
    private ArrayList<String> drillOrderNumbers = new ArrayList<>();
    private ArrayList<String> paintingOrderNumbers = new ArrayList<>();
    private ArrayList<String> packingOrderNumbers = new ArrayList<>();

    // гистограмма заказов в работе
    @FXML
    private BarChart<String, Integer> orderInWork;

    @FXML
    private CategoryAxis statusAxe;

    @FXML
    private NumberAxis quantityAxe;

    // список заказов
    @FXML
    private TableView<Order> tableOrder;

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

    // колонка названия материала в списке для создания нового заказа
    @FXML
    private TableColumn<MaterialDataForOrder,String> materialColumn;

    // количество материала в списке для создания нового заказа
    @FXML
    private TableColumn<MaterialDataForOrder, Double> quantityColumn;

    @FXML
    private Button enterprise;

    @FXML
    private Button warehouse;

    @FXML
    private Button saveOrder;

    @FXML
    private Button putOrder;

    @FXML
    private Button findOrder;

    @FXML
    private Button deleteOrder;

    @FXML
    private Button saveMaterial;

    // поле ввода номера заказа для поиска
    @FXML
    private TextField numberToFind;

    // поля ввода данных нового материала
    @FXML
    private TextField newDate;

    @FXML
    private TextField newNumber;

    @FXML
    private TextField newDescription;

    @FXML
    private TextField newMaterial;

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
    private Label ncOrders;

    @FXML
    private Label drillOrders;

    @FXML
    private Label paintingOrders;

    @FXML
    private Label packingOrders;


    public FXMLController() {
    }

    // активация полей ввода при нажатии кнопки нового заказа
    @FXML
    private void newOrder(ActionEvent event) {
        newDate.setDisable(false);
        newNumber.setDisable(false);
        newDescription.setDisable(false);
        newMaterial.setDisable(false);
        newQuantity.setDisable(false);
        saveOrder.setDisable(false);
        saveMaterial.setDisable(false);
        tableMaterialNewOrder.setDisable(false);
    }

    // сохранение данных нового материала в таблице в новом заказе
    @FXML
    private void saveMaterial(ActionEvent event) {
        try {
            Double.parseDouble(newQuantity.getText());
        } catch (Exception e) {
            newQuantity.setStyle("-fx-text-inner-color:Red;");
            newQuantity.setText("*.*");
            return;
        }
        MaterialDataForOrder materialDataForOrder = new MaterialDataForOrder(newMaterial.getText(), Double.parseDouble(newQuantity.getText()));
        materialListForOrder.add(materialDataForOrder);
        newMaterial.clear();
        newQuantity.clear();
        newQuantity.setStyle("-fx-text-inner-color:Black;");
    }

    // изменение материала в таблице материалов нового заказа
    @FXML
    private void changeNewMaterial(TableColumn.CellEditEvent<MaterialDataForOrder, String> event) {
        event.getTableView().getItems().get(event.getTablePosition().getRow()).setMaterialName(event.getNewValue());
    }

    // изменение количества материала в таблице материалов нового заказа
    @FXML
    private void changeNewQuantity(TableColumn.CellEditEvent<MaterialDataForOrder, Double> event) {
        try {
            event.getNewValue();
        } catch (NumberFormatException e) {
            return;
        }
        event.getTableView().getItems().get(event.getTablePosition().getRow()).setQuantity(event.getNewValue());
    }

    // удаление выбранного материала в таблице материалов нового заказа
    @FXML
    public void deleteNewMaterial(KeyEvent keyEvent) {
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
            newDate.setText("Формат даты dd.mm.yyyy");
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
        newMaterial.clear();
        newQuantity.clear();

        materialListForOrder.clear();
        
        newDate.setDisable(true);
        newNumber.setDisable(true);
        newDescription.setDisable(true);
        newMaterial.setDisable(true);
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
    private void findOrder(ActionEvent event) {
        String findNumber = numberToFind.getText();
        for (Order order : orderList){
            if (findNumber.equals(order.getNumber())){
                Order selectedOrder = order;
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
                return;
            }
        }
        numberToFind.setText(findNumber + " не найден");
    }

    // изменение материала в таблице материалов выбранного заказа
    @FXML
    private void changeMaterial(TableColumn.CellEditEvent<MaterialDataForOrder, String> event) {
        event.getTableView().getItems().get(event.getTablePosition().getRow()).setMaterialName(event.getNewValue());
    }

    // изменение количества материала в таблице материалов выбранного заказа
    @FXML
    private void changeQuantity(TableColumn.CellEditEvent<MaterialDataForOrder, Double> event) {
        try {
            event.getNewValue();
        } catch (NumberFormatException e) {
            return;
        }
        event.getTableView().getItems().get(event.getTablePosition().getRow()).setQuantity(event.getNewValue());
    }

    // удаление выбранного материала в таблице материалов выбранного заказа
    @FXML
    public void deleteMaterial(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.DELETE) {
            int index = tableMaterialOrder.getSelectionModel().getFocusedIndex();
            materialListInOrder.remove(index);
        }
    }

    // сохранение измененного заказа
    @FXML
    private void putOrder(ActionEvent event) {
        try {
            Order.dateformatddMMyyyy.parse(date.getText());
        } catch (ParseException e) {
            date.setStyle("-fx-text-inner-color:Red;");
            return;
        }
        List<MaterialDataForOrder> materialListForPutOrder = new ArrayList<>(materialListInOrder);
        Order changedOrder = new Order(date.getText(), number.getText(), materialListForPutOrder, description.getText());
        changedOrder.setStatusOfOrder(status.getValue());
        int index = tableOrder.getSelectionModel().getFocusedIndex();

        if (changedOrder.getStatus() != orderList.get(index).getStatus()){
            histogramUpdate(changedOrder, orderList.get(index));
        }
        orderList.set(index, changedOrder);
        Collections.sort(orderList);
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

        int startCount = 0;
        int drillCount = 0;
        int paintingCount = 0;
        int packingCount = 0;
        for (Order order : orderList){
            switch (order.getStatus()){
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
        series.getData().add(new XYChart.Data<>("ЧПУ/раскрой", startCount));
        series.getData().add(new XYChart.Data<>("Присадка", drillCount));
        series.getData().add(new XYChart.Data<>("Шлифовка/покраска", paintingCount));
        series.getData().add(new XYChart.Data<>("На упаковке", packingCount));
        orderInWork.getData().add(series);

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
    public void setWarehouseMode(ActionEvent actionEvent) throws IOException {
        MainApp.changeRoot("materialMode");
    }

    @FXML
    void saveToFile(){
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream("C:\\Users\\Oleg\\Desktop\\save.ser");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            ArrayList<Order> q1 = new ArrayList<>(orderList);
            objectOutputStream.writeObject(q1);
            objectOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void openFromFile(){
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream("C:\\Users\\Oleg\\Desktop\\save.ser");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            ArrayList<Order> q2;
            q2 = (ArrayList<Order>) objectInputStream.readObject();
            orderList.addAll(q2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        histogramInit();
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
    }

    private void initData() {
        List<MaterialDataForOrder> materialOrderList = new ArrayList<>();
        materialOrderList.add(new MaterialDataForOrder("МДФ 19мм", 1.5));
        materialOrderList.add(new MaterialDataForOrder("МДФ 16мм", 2.5));
        Order order = new Order("11.03.2021", "1122", materialOrderList, "покраска");
        Order order1 = new Order("12.03.2021", "1155", materialOrderList, "покраска");
        Order order2 = new Order("10.03.2021", "99448", materialOrderList, "покраска");
        Order order3 = new Order("17.03.2021", "1307", materialOrderList, "покраска");
        Order order4 = new Order("15.03.2021", "1125", materialOrderList, "покраска");
        orderList.addAll(order, order1, order2, order3, order4);
        Collections.sort(orderList);
    }
}
