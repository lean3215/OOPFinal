package controller

import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.control.{TableColumn, TableView, Alert, ButtonType, Label}
import javafx.scene.control.Alert.AlertType
import javafx.scene.Scene
import javafx.stage.{Modality, Stage}
import javafx.scene.{Parent}
import repository.FoodRepository
import model.FoodItem
import scalafx.collections.ObservableBuffer
import util.Extensions.*
import java.time.LocalDate

class UserDashboardController:
  @FXML private var foodTable: TableView[FoodItem] = _
  @FXML private var nameColumn: TableColumn[FoodItem, String] = _
  @FXML private var caloriesColumn: TableColumn[FoodItem, Number] = _
  @FXML private var proteinColumn: TableColumn[FoodItem, Number] = _
  @FXML private var fatColumn: TableColumn[FoodItem, Number] = _
  @FXML private var carbsColumn: TableColumn[FoodItem, Number] = _
  @FXML private var categoryColumn: TableColumn[FoodItem, String] = _
  @FXML private var dateLabel: Label = _

  private val foods = ObservableBuffer.from(FoodRepository.getAll())

  @FXML def initialize(): Unit =
    foodTable.setItems(foods)
    nameColumn.setCellValueFactory(_.value.nameProperty)
    caloriesColumn.setCellValueFactory(_.value.caloriesProperty)
    proteinColumn.setCellValueFactory(_.value.proteinProperty)
    fatColumn.setCellValueFactory(_.value.fatProperty)
    carbsColumn.setCellValueFactory(_.value.carbsProperty)
    categoryColumn.setCellValueFactory(_.value.categoryProperty)
    dateLabel.setText(LocalDate.now.format)

  @FXML private def handleAdd(): Unit =
    val loader = new FXMLLoader(getClass.getResource("/view/add_food_dialog.fxml"))
    val root: Parent = loader.load()
    val controller = loader.getController[AddFoodDialogController]
    val stage = new Stage()
    stage.initModality(Modality.ApplicationModal)
    stage.setTitle("Add Food")
    stage.getIcons.add(new javafx.scene.image.Image(getClass.getResourceAsStream("/images/login-icon.jpg")))
    val scene = new Scene(root)
    scene.getStylesheets.add(getClass.getResource("/DarkTheme.css").toExternalForm)
    stage.setScene(scene)
    controller.setDialogStage(stage)
    controller.setOnAdd { food =>
      FoodRepository.insert(food)
      foods.setAll(FoodRepository.getAll())
    }
    stage.showAndWait()

  @FXML private def handleDelete(): Unit =
    val selected = foodTable.getSelectionModel.getSelectedItem
    if selected != null then
      val confirm = new Alert(AlertType.Confirmation)
      confirm.setTitle("Delete")
      confirm.setHeaderText(s"Delete ${selected.name}?")
      val result = confirm.showAndWait()
      if result.isPresent && result.get == ButtonType.OK then
        FoodRepository.delete(selected.id)
        foods.setAll(FoodRepository.getAll())
    else
      new Alert(AlertType.Warning) {
        title = "No Selection"
        headerText = "No Food Selected"
        contentText = "Please select an item to delete"
      }.showAndWait()
