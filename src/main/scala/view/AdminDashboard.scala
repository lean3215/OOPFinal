package view

import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.layout.{BorderPane, HBox, VBox}
import scalafx.stage.Stage
import scalafx.collections.ObservableBuffer
import scalafx.Includes._
import model.FoodItem
import repository.FoodRepository

object AdminDashboard:

  def show(): Unit =
    val allFoods = ObservableBuffer(FoodRepository.getAll(): _*)
    val filteredFoods = ObservableBuffer.from(allFoods)

    val foodTable = new TableView[FoodItem](filteredFoods):
      columnResizePolicy = TableView.ConstrainedResizePolicy
      sortPolicy = _ => true // Enable sorting
      columns ++= List(
        new TableColumn[FoodItem, String] {
          text = "Name"
          cellValueFactory = _.value.nameProperty
          prefWidth = 150
          sortable = true
        },
        new TableColumn[FoodItem, Number] {
          text = "Calories"
          cellValueFactory = _.value.caloriesProperty.delegate
          prefWidth = 80
          sortable = true
        },
        new TableColumn[FoodItem, Number] {
          text = "Protein (g)"
          cellValueFactory = _.value.proteinProperty.delegate
          prefWidth = 100
          sortable = true
        },
        new TableColumn[FoodItem, Number] {
          text = "Fat (g)"
          cellValueFactory = _.value.fatProperty.delegate
          prefWidth = 100
          sortable = true
        },
        new TableColumn[FoodItem, Number] {
          text = "Carbs (g)"
          cellValueFactory = _.value.carbsProperty.delegate
          prefWidth = 100
          sortable = true
        },
        new TableColumn[FoodItem, String] {
          text = "Category"
          cellValueFactory = _.value.categoryProperty.delegate
          prefWidth = 100
          sortable = true
        }
      )

    val searchField = new TextField() {
      promptText = "e.g. Apple, Dairy..."
    }

    val categoryFilter = new ComboBox[String](Seq("All", "Fruits", "Vegetables", "Meat", "Dairy", "Grains")) {
      value = "All"
    }

    val minCalField = new TextField { promptText = "Min Cal" }
    val maxCalField = new TextField { promptText = "Max Cal" }

    val minProteinField = new TextField { promptText = "Min Protein" }
    val maxProteinField = new TextField { promptText = "Max Protein" }

    val minFatField = new TextField { promptText = "Min Fat" }
    val maxFatField = new TextField { promptText = "Max Fat" }

    val applyFilterButton = new Button("🔄 Apply Filters")
    val clearFilterButton = new Button("❌ Clear Filters")

    def applyFilters(): Unit =
      val query = searchField.text.value.trim.toLowerCase
      val cat = categoryFilter.value.value
      val minCal = minCalField.text.value.toIntOption
      val maxCal = maxCalField.text.value.toIntOption
      val minProtein = minProteinField.text.value.toDoubleOption
      val maxProtein = maxProteinField.text.value.toDoubleOption
      val minFat = minFatField.text.value.toDoubleOption
      val maxFat = maxFatField.text.value.toDoubleOption

      filteredFoods.clear()
      filteredFoods ++= allFoods.filter { f =>
        val matchesQuery = f.name.toLowerCase.contains(query) || f.category.toLowerCase.contains(query)
        val matchesCategory = cat == "All" || f.category == cat
        val matchesCal = minCal.forall(f.calories >= _) && maxCal.forall(f.calories <= _)
        val matchesProtein = minProtein.forall(f.protein >= _) && maxProtein.forall(f.protein <= _)
        val matchesFat = minFat.forall(f.fat >= _) && maxFat.forall(f.fat <= _)
        matchesQuery && matchesCategory && matchesCal && matchesProtein && matchesFat
      }

    def clearFilters(): Unit =
      searchField.clear()
      categoryFilter.value = "All"
      minCalField.clear()
      maxCalField.clear()
      minProteinField.clear()
      maxProteinField.clear()
      minFatField.clear()
      maxFatField.clear()
      filteredFoods.clear()
      filteredFoods ++= allFoods

    applyFilterButton.onAction = _ => applyFilters()
    clearFilterButton.onAction = _ => clearFilters()

    val addButton = new Button("➕ Add Food")
    addButton.onAction = _ =>
      AddFoodPopup.show { newFood =>
        FoodRepository.insert(newFood)
        allFoods += newFood
        filteredFoods += newFood
      }

    val editButton = new Button("✏️ Edit")
    editButton.onAction = _ =>
      val selected = foodTable.selectionModel().getSelectedItem
      if selected != null then
        EditFoodPopup.show(selected) { updated =>
          FoodRepository.update(updated)
          val indexAll = allFoods.indexWhere(_.id == updated.id)
          if indexAll >= 0 then allFoods.update(indexAll, updated)
          val indexFiltered = filteredFoods.indexWhere(_.id == updated.id)
          if indexFiltered >= 0 then filteredFoods.update(indexFiltered, updated)
        }
      else
        new Alert(Alert.AlertType.Warning) {
          title = "No Selection"
          headerText = "No Food Selected"
          contentText = "Please select a food item to edit."
        }.showAndWait()

    val deleteButton = new Button("❌ Delete")
    deleteButton.onAction = _ =>
      val selected = foodTable.selectionModel().getSelectedItem
      if selected != null then
        val confirm = new Alert(Alert.AlertType.Confirmation):
          title = "Confirm Delete"
          headerText = s"Delete '${selected.name}'?"
          contentText = "This action cannot be undone."
        val result = confirm.showAndWait()
        if result.isDefined && result.get == ButtonType.OK then
          FoodRepository.delete(selected.id)
          allFoods -= selected
          filteredFoods -= selected
      else
        new Alert(Alert.AlertType.Warning) {
          title = "No Selection"
          headerText = "No Food Selected"
          contentText = "Please select a food item to delete."
        }.showAndWait()

    val filterRow1 = new HBox(10):
      children = List(
        new Label("🔍 Search (Name or Category):"), searchField,
        new Label("Category:"), categoryFilter,
        applyFilterButton,
        clearFilterButton
      )
      alignment = Pos.Center

    val filterRow2 = new HBox(10):
      children = List(
        new Label("Calories:"), minCalField, new Label("-"), maxCalField,
        new Label("Protein:"), minProteinField, new Label("-"), maxProteinField,
        new Label("Fat:"), minFatField, new Label("-"), maxFatField
      )
      alignment = Pos.Center

    val buttonBar = new HBox(10):
      alignment = Pos.Center
      padding = Insets(10)
      children = List(addButton, editButton, deleteButton)

    val layout = new BorderPane:
      top = new VBox(10):
        padding = Insets(10)
        alignment = Pos.Center
        children = List(
          new Label("🍽️ Admin Dashboard - Nutritional Database") {
            style = "-fx-font-size: 20px; -fx-text-fill: #2e7d32;"
          },
          filterRow1,
          filterRow2
        )
      center = foodTable
      bottom = buttonBar

    val stage = new Stage:
      title = "Admin Dashboard"
      scene = new Scene(layout, 900, 600)

    stage.show()