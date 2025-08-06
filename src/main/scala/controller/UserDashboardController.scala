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
import scalafx.Includes.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    dateLabel.setText(LocalDate.now.format(formatter))

  @FXML private def handleAdd(): Unit =
    val loader = new FXMLLoader(getClass.getResource("/view/add_food_dialog.fxml"))
    val root: Parent = loader.load()
    val controller = loader.getController[AddFoodDialogController]
    val stage = new Stage()
    stage.initModality(Modality.APPLICATION_MODAL)
    stage.setTitle("Add Food")
    stage.getIcons.add(new javafx.scene.image.Image(getClass.getResourceAsStream("/images/login-icon.jpg")))
    val scene = new Scene(root)
    scene.getStylesheets.add(getClass.getResource("/DarkTheme.css").toExternalForm)
    stage.setScene(scene)
    controller.setDialogStage(new scalafx.stage.Stage(stage))
    controller.setOnAdd { food =>
      FoodRepository.insert(food)
      foods.setAll(FoodRepository.getAll()*)
    }
    stage.showAndWait()

  @FXML private def handleEdit(): Unit =
    val selected = foodTable.getSelectionModel.getSelectedItem
    if selected != null then
      val loader = new FXMLLoader(getClass.getResource("/view/edit_food_dialog.fxml"))
      val root: Parent = loader.load()
      val controller = loader.getController[EditFoodDialogController]
      val stage = new Stage()
      stage.initModality(Modality.APPLICATION_MODAL)
      stage.setTitle("Edit Food")
      stage.getIcons.add(new javafx.scene.image.Image(getClass.getResourceAsStream("/images/login-icon.jpg")))
      val scene = new Scene(root)
      scene.getStylesheets.add(getClass.getResource("/DarkTheme.css").toExternalForm)
      stage.setScene(scene)
      controller.setDialogStage(new scalafx.stage.Stage(stage))
      controller.setFoodItem(selected)
      controller.setOnUpdate { food =>
        FoodRepository.update(food)
        foods.setAll(FoodRepository.getAll()*)
      }
      stage.showAndWait()
    else
      val warning = new Alert(AlertType.WARNING)
      warning.setTitle("No Selection")
      warning.setHeaderText("No Food Selected")
      warning.setContentText("Please select an item to edit")
      warning.showAndWait()

  @FXML private def handleDelete(): Unit =
    val selected = foodTable.getSelectionModel.getSelectedItem
    if selected != null then
      val confirm = new Alert(AlertType.CONFIRMATION)
      confirm.setTitle("Delete")
      confirm.setHeaderText(s"Delete ${selected.name}?")
      val result = confirm.showAndWait()
      if result.isPresent && result.get == ButtonType.OK then
        FoodRepository.delete(selected.id)
        foods.setAll(FoodRepository.getAll()*)
    else
      val warning = new Alert(AlertType.WARNING)
      warning.setTitle("No Selection")
      warning.setHeaderText("No Food Selected")
      warning.setContentText("Please select an item to delete")
      warning.showAndWait()
