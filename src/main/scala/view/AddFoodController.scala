package view

import javafx.fxml.FXML
import model.FoodItem
import scalafx.scene.control.{Alert, Button, ComboBox, DatePicker, Label, TextField}
import scalafx.scene.control.Alert.AlertType
import scalafx.beans.binding.Bindings
import scalafx.stage.Stage
import scalafx.Includes._
import java.util.ResourceBundle

class AddFoodController:

  @FXML private var nameField: TextField = _
  @FXML private var caloriesField: TextField = _
  @FXML private var proteinField: TextField = _
  @FXML private var fatField: TextField = _
  @FXML private var carbsField: TextField = _
  @FXML private var categoryBox: ComboBox[String] = _
  @FXML private var datePicker: DatePicker = _
  @FXML private var addButton: Button = _
  @FXML private var errorLabel: Label = _
  @FXML private var resources: ResourceBundle = _

  private var onAdd: FoodItem => Unit = _
  private var stage: Stage = _

  @FXML def initialize(): Unit =
    addButton.disable <== Bindings.createBooleanBinding(
      () => nameField.text.value.trim.isEmpty ||
            caloriesField.text.value.trim.isEmpty ||
            proteinField.text.value.trim.isEmpty ||
            fatField.text.value.trim.isEmpty ||
            carbsField.text.value.trim.isEmpty ||
            categoryBox.value.value == null ||
            datePicker.value.value == null,
      nameField.text, caloriesField.text, proteinField.text, fatField.text, carbsField.text, categoryBox.value, datePicker.value
    )

  def setOnAdd(callback: FoodItem => Unit): Unit =
    onAdd = callback

  def setStage(s: Stage): Unit =
    stage = s

  @FXML def handleAdd(): Unit =
    val name = nameField.text.value.trim
    val caloriesText = caloriesField.text.value.trim
    val proteinText = proteinField.text.value.trim
    val fatText = fatField.text.value.trim
    val carbsText = carbsField.text.value.trim
    val category = categoryBox.value.value
    val date = datePicker.value.value

    if name.isEmpty || caloriesText.isEmpty || proteinText.isEmpty || fatText.isEmpty || carbsText.isEmpty || category == null || date == null then
      errorLabel.text = resources.getString("add.requiredError")
      new Alert(AlertType.Error) {
        headerText = errorLabel.text()
      }.showAndWait()
    else if !caloriesText.forall(_.isDigit) || !proteinText.forall(_.isDigit) || !fatText.forall(_.isDigit) || !carbsText.forall(_.isDigit) then
      errorLabel.text = resources.getString("add.numberError")
      new Alert(AlertType.Error) {
        headerText = errorLabel.text()
      }.showAndWait()
    else
      val item = FoodItem(0, name, caloriesText.toInt, proteinText.toDouble, fatText.toDouble, carbsText.toDouble, category)
      onAdd(item)
      new Alert(AlertType.Information) {
        title = resources.getString("add.successTitle")
        headerText = resources.getString("add.successHeader")
        contentText = resources.getString("add.successContent").formatted(name)
      }.showAndWait()
      stage.close()

  @FXML def handleCancel(): Unit =
    stage.close()
