package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class FXMLArchiveController implements Initializable {
    ObservableList<Order> orderArchiveList = FXCollections.observableArrayList();
    private ObservableList<MaterialDataForOrder> materialListInArchiveOrder = FXCollections.observableArrayList();
    private ObservableList<Material> materialListInStatistic = FXCollections.observableArrayList();

    @FXML
    private MenuItem save;

    @FXML
    private MenuItem saveToFile;

    @FXML
    private MenuItem open;

    // список заказов
    @FXML
    TableView<Order> tableArchiveOrder;

    // список материалов в выбранном заказе
    @FXML
    private TableView<MaterialDataForOrder> tableArchiveMaterialOrder;

    // таблица израсходованных материалов в статистике
    @FXML
    private TableView<Material> tableMaterialStatistic;

    // колонка даты в списке заказов
    @FXML
    private TableColumn<Order, String> dateArchiveOrderColumn;

    // колонка номера заказа в списке заказов
    @FXML
    private TableColumn<Order, String> numberArchiveOrderColumn;

    // колонка статуса заказа в списке заказов
    @FXML
    private TableColumn<Order, String> descriptionArchiveOrderColumn;

    // колонка названия материала в списке
    @FXML
    private TableColumn<MaterialDataForOrder,String> materialArchiveColumn;

    // количество материала в списке
    @FXML
    private TableColumn<MaterialDataForOrder, Double> quantityArchiveColumn;

    // столбец названия материала в статистике
    @FXML
    private TableColumn<Material, String> materialNameStatisticColumn;

    // столбец количества потраченного материала в статистике
    @FXML
    private TableColumn<Material, Integer> quantityStatisticColumn;

    @FXML
    private Button findOrder;

    @FXML
    private Button deleteOrder;

    @FXML
    private Button toSeeStatistic;

    // кнопки переключения режимов приложения
    @FXML
    private Button enterprise;

    @FXML
    private Button warehouse;

    // поле ввода номера заказа для поиска
    @FXML
    private TextField numberToFind;

    // поля данных просмотра заказа
    @FXML
    private TextField date;

    @FXML
    private TextField number;

    @FXML
    private TextField description;

    // поле ввода даты начала периода статистики
    @FXML
    private TextField dateAfter;

    // поле ввода конца периода статистики
    @FXML
    private TextField dateBefore;

    @FXML
    private Label summMaterial;

    @FXML
    private Label summOrder;


    // выбор заказа в таблице заказов
    @FXML
    private void choiceOrder(MouseEvent event) {
        Order selectedOrder = tableArchiveOrder.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            number.setText(selectedOrder.getNumber());
            date.setText(selectedOrder.getDate());
            description.setText(selectedOrder.getDescription());
            materialListInArchiveOrder.clear();
            materialListInArchiveOrder.addAll(selectedOrder.getMaterialList());
            deleteOrder.setDisable(false);
        }
    }

    // поиск заказа в таблице заказов
    @FXML
    private void findOrder(ActionEvent event) throws IOException {
        String findNumber = numberToFind.getText();
        if (!findOrder(findNumber)) {
            FXMLController cont = Context.getInstance().getFXMLController();
            if (cont.findOrder(findNumber)) {
                MainApp.changeRoot("primary");
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Заказ не найден!");
                alert.showAndWait();
            }
        }
    }

    boolean findOrder(String findNumber) {
        for (Order order : orderArchiveList){
            if (findNumber.equals(order.getNumber())){
                Order selectedOrder;
                selectedOrder = order;
                tableArchiveOrder.requestFocus();
                tableArchiveOrder.getFocusModel().focus(orderArchiveList.indexOf(selectedOrder));
                tableArchiveOrder.scrollTo(selectedOrder);
                number.setText(selectedOrder.getNumber());
                date.setText(selectedOrder.getDate());
                description.setText(selectedOrder.getDescription());
                materialListInArchiveOrder.clear();
                materialListInArchiveOrder.addAll(selectedOrder.getMaterialList());
                deleteOrder.setDisable(false);
                return true;
            }
        }
        return false;
    }

    // удаление заказа
    @FXML
    private void deleteOrder(ActionEvent event) {
        int index = tableArchiveOrder.getSelectionModel().getFocusedIndex();
        orderArchiveList.remove(index);
        materialListInArchiveOrder.clear();
        date.clear();
        number.clear();
        description.clear();
        deleteOrder.setDisable(true);
    }

    // просмотр статистики за заданный период
    @FXML
    private void toSeeStatistic(ActionEvent actionEvent) throws ParseException {
        materialListInStatistic.clear();
        summMaterial.setText("Итого: ");
        summOrder.setText("Отгружено заказов: ");

        Date begin = Order.dateformatddMMyyyy.parse(dateAfter.getText());
        Date end = Order.dateformatddMMyyyy.parse(dateBefore.getText());
        List<Order> orderArchiveInPeriodList = new ArrayList<>();

        // сортировка заказа с выбором относящихся к заданному периоду
        for (Order order : orderArchiveList){
            Date dateOrder = Order.dateformatddMMyyyy.parse(order.getDate().substring(0, 10));
            if (dateOrder.before(end) && dateOrder.after(begin)){
                orderArchiveInPeriodList.add(order);
            }
        }

        if (orderArchiveInPeriodList.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Нет отгруженных заказов в этом периоде!");
            alert.showAndWait();
            return;
        }

        // составление таблицы материалов отгруженных заказов
        for (Order order : orderArchiveInPeriodList){
            for (MaterialDataForOrder materialData : order.getMaterialList()){
                boolean haveMaterialInBase = false;
                Material archiveMaterial = new Material(materialData.getMaterialName(), null, (int) (materialData.getQuantity() / FXMLController.LISTAREA + 0.75));
                for (Material material : materialListInStatistic) {
                    if (materialData.getMaterialName().equals(material.getName())){
                        material.setQuantity(material.getQuantity() + (int) (materialData.getQuantity() / FXMLController.LISTAREA + 0.75));
                        haveMaterialInBase = true;
                        break;
                    }
                }
                if (!haveMaterialInBase) materialListInStatistic.add(archiveMaterial);
            }
        }
        summMaterial.setText("Итого: " + materialListInStatistic.stream().mapToInt(Material::getQuantity).sum() + " листов");
        summOrder.setText("Отгружено заказов: " + orderArchiveList.size());
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

    @FXML
    private void setEnterpriseMode(ActionEvent actionEvent) throws IOException {
        MainApp.changeRoot("primary");
    }

    @FXML
    private void setWarehouseMode(ActionEvent actionEvent) throws IOException {
        MainApp.changeRoot("materialMode");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // таблица архива заказов
        dateArchiveOrderColumn.setCellValueFactory(new PropertyValueFactory<Order, String>("date"));
        numberArchiveOrderColumn.setCellValueFactory(new PropertyValueFactory<Order, String>("number"));
        descriptionArchiveOrderColumn.setCellValueFactory(new PropertyValueFactory<Order, String>("description"));
        tableArchiveOrder.setItems(orderArchiveList);

        // таблица материалов в архивном заказе
        materialArchiveColumn.setCellValueFactory(new PropertyValueFactory<>("materialName"));
        quantityArchiveColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tableArchiveMaterialOrder.setItems(materialListInArchiveOrder);

        // таблица израсходованных материалов в статистике
        materialNameStatisticColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantityStatisticColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tableMaterialStatistic.setItems(materialListInStatistic);

        // установка значений по умолчанию в поля периода для статистики
        dateBefore.setText(Order.dateformatddMMyyyy.format(new Date()));
        LocalDate date = LocalDate.now();
        dateAfter.setText("01.01." + date.getYear());

        Context.getInstance().setFXMLArchiveController(this);
    }

}
