package view

import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.layout.{VBox, HBox}
import scalafx.stage.Stage
import model.FoodItem
import scalafx.Includes._

object AddFoodPopup:

  def show(onAdd: FoodItem => Unit): Unit =
    val stage = new Stage:
      title = "Add Food"

    val nameField = new TextField()
    val caloriesField = new TextField()
    val proteinField = new TextField()
    val fatField = new TextField()
    val carbsField = new TextField()
    val categoryBox = new ComboBox[String](List("Fruits", "Vegetables", "Meat", "Dairy", "Grains"))

    val errorLabel = new Label("") {
      style = "-fx-text-fill: red;"
    }

    val addButton = new Button("Add")
    val cancelButton = new Button("Cancel")

    addButton.onAction = _ =>
      val name = nameField.text.value.trim
      val caloriesText = caloriesField.text.value.trim
      val proteinText = proteinField.text.value.trim
      val fatText = fatField.text.value.trim
      val carbsText = carbsField.text.value.trim
      val category = categoryBox.value.value

      // Validate input
      if name.isEmpty || caloriesText.isEmpty || proteinText.isEmpty || fatText.isEmpty || carbsText.isEmpty || category == null then
        errorLabel.text = "All fields must be filled."
      else if !caloriesText.forall(_.isDigit) || !proteinText.forall(_.isDigit) || !fatText.forall(_.isDigit) || !carbsText.forall(_.isDigit) then
        errorLabel.text = "Calories, protein, fat, and carbs must be numbers."
      else
        val newItem = FoodItem(
          id = 0,
          name = name,
          calories = caloriesText.toInt,
          protein = proteinText.toDouble,
          fat = fatText.toDouble,
          carbs = carbsText.toDouble,
          category = category
        )
        onAdd(newItem)
        val alert = new Alert(Alert.AlertType.Information):
          title = "Success"
          headerText = "Food Added"
          contentText = s"$name has been added to the database."
        alert.showAndWait()
        stage.close()

    cancelButton.onAction = _ => stage.close()

    val form = new VBox(10):
      padding = Insets(15)
      alignment = Pos.Center
      children = List(
        new Label("Add New Food Item"),
        new Label("Name:"), nameField,
        new Label("Calories:"), caloriesField,
        new Label("Protein (g):"), proteinField,
        new Label("Fat (g):"), fatField,
        new Label("Carbohydrates (g):"), carbsField,
        new Label("Category:"), categoryBox,
        errorLabel,
        new HBox(10) {
          alignment = Pos.Center
          children = List(addButton, cancelButton)
        }
      )

    stage.scene = new Scene(form, 300, 500)
    stage.showAndWait()