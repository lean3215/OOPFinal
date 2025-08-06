package view

import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.layout.{VBox, HBox}
import scalafx.stage.Stage
import model.FoodItem
import scalafx.Includes._

object EditFoodPopup:

  def show(existing: FoodItem)(onUpdate: FoodItem => Unit): Unit =
    val nameField = new TextField() {text = existing.name}
    val caloriesField = new TextField() {text = existing.calories.toString}
    val proteinField = new TextField() {text = existing.protein.toString}
    val fatField = new TextField() {text = existing.fat.toString}
    val carbsField = new TextField() {text = existing.carbs.toString}
    val categoryBox = new ComboBox[String](List("Fruits", "Vegetables", "Meat", "Dairy", "Grains"))
    categoryBox.value = existing.category

    val errorLabel = new Label("") {
      style = "-fx-text-fill: red;"
    }

    val updateButton = new Button("Update")
    val cancelButton = new Button("Cancel")

    val stage = new Stage:
      title = "Edit Food"
      scene = new Scene(new VBox(10) {
        padding = Insets(15)
        alignment = Pos.Center
        children = List(
          new Label("Edit Food Item"),
          new Label("Name:"), nameField,
          new Label("Calories:"), caloriesField,
          new Label("Protein (g):"), proteinField,
          new Label("Fat (g):"), fatField,
          new Label("Carbohydrates (g):"), carbsField,
          new Label("Category:"), categoryBox,
          errorLabel,
          new HBox(10) {
            alignment = Pos.Center
            children = List(updateButton, cancelButton)
          }
        )
      }, 300, 500)

    updateButton.onAction = _ =>
      val name = nameField.text.value.trim
      val caloriesText = caloriesField.text.value.trim
      val proteinText = proteinField.text.value.trim
      val fatText = fatField.text.value.trim
      val carbsText = carbsField.text.value.trim
      val category = categoryBox.value.value

      if name.isEmpty || caloriesText.isEmpty || proteinText.isEmpty || fatText.isEmpty || carbsText.isEmpty || category == null then
        errorLabel.text = "All fields must be filled."
      else if !caloriesText.forall(_.isDigit) || !proteinText.forall(_.isDigit) || !fatText.forall(_.isDigit) || !carbsText.forall(_.isDigit) then
        errorLabel.text = "Calories, protein, fat, and carbs must be numbers."
      else
        val updated = existing.copy(
          name = name,
          calories = caloriesText.toInt,
          protein = proteinText.toDouble,
          fat = fatText.toDouble,
          carbs = carbsText.toDouble,
          category = category
        )
        onUpdate(updated)
        stage.close()

    cancelButton.onAction = _ => stage.close()

    stage.showAndWait()