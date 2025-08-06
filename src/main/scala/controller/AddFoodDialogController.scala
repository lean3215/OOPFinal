package controller

import model.FoodItem
import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType
import scalafx.stage.Stage
import javafx.fxml.FXML
import javafx.scene.control.{TextField, ComboBox, Label}

class AddFoodDialogController:

  @FXML private var nameField: TextField = _
  @FXML private var caloriesField: TextField = _
  @FXML private var proteinField: TextField = _
  @FXML private var fatField: TextField = _
  @FXML private var carbsField: TextField = _
  @FXML private var categoryBox: ComboBox[String] = _
  @FXML private var errorLabel: Label = _

  private var dialogStage: Stage = _
  private var onAdd: FoodItem => Unit = _

  def setDialogStage(stage: Stage): Unit =
    dialogStage = stage

  def setOnAdd(callback: FoodItem => Unit): Unit =
    onAdd = callback

  @FXML
  private def handleAdd(): Unit =
    val name = nameField.getText.trim
    val caloriesText = caloriesField.getText.trim
    val proteinText = proteinField.getText.trim
    val fatText = fatField.getText.trim
    val carbsText = carbsField.getText.trim
    val category = Option(categoryBox.getValue)

    if name.isEmpty || caloriesText.isEmpty || proteinText.isEmpty || fatText.isEmpty || carbsText.isEmpty || category.isEmpty then
      errorLabel.setText("All fields must be filled.")
    else if !caloriesText.forall(_.isDigit) || !proteinText.forall(c => c.isDigit || c == '.') || !fatText.forall(c => c.isDigit || c == '.') || !carbsText.forall(c => c.isDigit || c == '.') then
      errorLabel.setText("Calories, protein, fat, and carbs must be numbers.")
    else
      val newItem = FoodItem(
        id = 0,
        name = name,
        calories = caloriesText.toInt,
        protein = proteinText.toDouble,
        fat = fatText.toDouble,
        carbs = carbsText.toDouble,
        category = category.get
      )
      if onAdd != null then onAdd(newItem)
      new Alert(AlertType.Information) {
        title = "Success"
        headerText = "Food Added"
        contentText = s"$name has been added to the database."
        initOwner(dialogStage)
      }.showAndWait()
      dialogStage.close()

  @FXML
  private def handleCancel(): Unit =
    if dialogStage != null then dialogStage.close()
