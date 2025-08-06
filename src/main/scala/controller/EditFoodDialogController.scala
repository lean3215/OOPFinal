package controller

import model.FoodItem
import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType
import scalafx.stage.Stage
import javafx.fxml.FXML
import javafx.scene.control.{TextField, ComboBox}

class EditFoodDialogController:
  @FXML private var nameField: TextField = _
  @FXML private var caloriesField: TextField = _
  @FXML private var proteinField: TextField = _
  @FXML private var fatField: TextField = _
  @FXML private var carbsField: TextField = _
  @FXML private var categoryBox: ComboBox[String] = _

  private var dialogStage: Stage = _
  private var foodItem: FoodItem = _
  private var onUpdate: FoodItem => Unit = _

  def setDialogStage(stage: Stage): Unit =
    dialogStage = stage

  def setFoodItem(item: FoodItem): Unit =
    foodItem = item
    nameField.setText(item.name)
    caloriesField.setText(item.calories.toString)
    proteinField.setText(item.protein.toString)
    fatField.setText(item.fat.toString)
    carbsField.setText(item.carbs.toString)
    categoryBox.setValue(item.category)

  def setOnUpdate(callback: FoodItem => Unit): Unit =
    onUpdate = callback

  @FXML private def handleUpdate(): Unit =
    val name = nameField.getText.trim
    val caloriesOpt = caloriesField.getText.trim.toIntOption
    val proteinOpt = proteinField.getText.trim.toDoubleOption
    val fatOpt = fatField.getText.trim.toDoubleOption
    val carbsOpt = carbsField.getText.trim.toDoubleOption
    val category = Option(categoryBox.getValue)

    val errors = collection.mutable.ArrayBuffer[String]()
    if name.isEmpty || category.isEmpty ||
      caloriesOpt.isEmpty || proteinOpt.isEmpty || fatOpt.isEmpty || carbsOpt.isEmpty then
      errors += "All fields must be filled with valid numbers."

    if errors.nonEmpty then
      new Alert(AlertType.Error) {
        title = "Invalid Input"
        headerText = "Please correct the following"
        contentText = errors.mkString("\n")
        initOwner(dialogStage)
      }.showAndWait()
    else
      val updated = foodItem.copy(
        name = name,
        calories = caloriesOpt.get,
        protein = proteinOpt.get,
        fat = fatOpt.get,
        carbs = carbsOpt.get,
        category = category.get
      )
      if onUpdate != null then onUpdate(updated)
      new Alert(AlertType.Information) {
        title = "Success"
        headerText = "Food Updated"
        contentText = s"$name has been updated."
        initOwner(dialogStage)
      }.showAndWait()
      dialogStage.close()

  @FXML private def handleCancel(): Unit =
    if dialogStage != null then dialogStage.close()
