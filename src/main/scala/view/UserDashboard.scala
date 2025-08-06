package view

import model.FoodItem
import repository.FoodRepository
import scalafx.collections.ObservableBuffer
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout._
import scalafx.stage.Stage
import scalafx.scene.text.Font
import scalafx.Includes._

class UserDashboard extends Stage {

  private val foodRepo = FoodRepository
  private val allFoods = ObservableBuffer.from(foodRepo.getAll())
  private var selectedFoods = ObservableBuffer[FoodItem]()

  title = "User Dashboard"
  scene = new Scene(1100, 750) {
    stylesheets += getClass.getResource("/user-dashboard.css").toExternalForm

    val searchNameField = new TextField {
      promptText = "Search food by name..."
      prefWidth = 400
      styleClass += "search-bar"
    }

    val categoryBox = new ComboBox[String](ObservableBuffer("All", "Fruit", "Vegetable", "Meat", "Dairy", "Grain")) {
      value = "All"
    }

    val mealTypeBox = new ComboBox[String](ObservableBuffer("Breakfast", "Lunch", "Dinner")) {
      value = "Breakfast"
    }

    val searchCal = new TextField { promptText = "Max Calories"; prefWidth = 120 }
    val searchProtein = new TextField { promptText = "Max Protein"; prefWidth = 120 }
    val searchFat = new TextField { promptText = "Max Fat"; prefWidth = 120 }
    val searchCarbs = new TextField { promptText = "Max Carbs"; prefWidth = 120 }

    val filterButton = new Button("Apply Filters")
    val resetButton = new Button("Reset")

    val filterBar = new VBox(10,
      new HBox(10, searchNameField),
      new HBox(10, new Label("Meal:"), mealTypeBox, new Label("Category:"), categoryBox),
      new HBox(10, searchCal, searchProtein, searchFat, searchCarbs),
      new HBox(10, filterButton, resetButton)
    ) {
      padding = Insets(10)
      alignment = Pos.TopLeft
    }

    val foodCardPane = new FlowPane {
      hgap = 10
      vgap = 10
      padding = Insets(10)
    }

    def refreshFoodCards(): Unit = renderCards(foodCardPane.children.map(_.userData.asInstanceOf[FoodItem]).toSeq)

    def renderCards(foods: Seq[FoodItem]): Unit = {
      foodCardPane.children.clear()
      foods.foreach { food =>
        val card = new VBox(5) {
          padding = Insets(10)
          styleClass += "food-card"
          userData = food
          children = Seq(
            new Label(food.name) { styleClass += "food-name" },
            new Label(s"Category: ${food.category}"),
            new Label(s"Calories: ${food.calories} kcal"),
            new Label(s"Protein: ${food.protein} g"),
            new Label(s"Fat: ${food.fat} g"),
            new Label(s"Carbs: ${food.carbs} g")
          )
        }

        if (selectedFoods.contains(food)) card.styleClass += "selected-card"

        card.onMouseClicked = (_: MouseEvent) => {
          if (selectedFoods.contains(food)) {
            selectedFoods -= food
            card.styleClass -= "selected-card"
          } else {
            selectedFoods += food
            card.styleClass += "selected-card"
          }
        }

        foodCardPane.children += card
      }
    }

    filterButton.onAction = _ => {
      val nameFilter = searchNameField.text.value.toLowerCase
      val maxCalories = searchCal.text.value.toDoubleOption.getOrElse(Double.MaxValue)
      val maxProtein = searchProtein.text.value.toDoubleOption.getOrElse(Double.MaxValue)
      val maxFat = searchFat.text.value.toDoubleOption.getOrElse(Double.MaxValue)
      val maxCarbs = searchCarbs.text.value.toDoubleOption.getOrElse(Double.MaxValue)

      val filtered = allFoods.filter { f =>
        f.name.toLowerCase.contains(nameFilter) &&
          (categoryBox.value.value == "All" || f.category == categoryBox.value.value) &&
          f.calories <= maxCalories &&
          f.protein <= maxProtein &&
          f.fat <= maxFat &&
          f.carbs <= maxCarbs
      }

      renderCards(filtered.toSeq)
    }

    resetButton.onAction = _ => {
      searchNameField.text = ""
      searchCal.text = ""
      searchProtein.text = ""
      searchFat.text = ""
      searchCarbs.text = ""
      categoryBox.value = "All"
      selectedFoods.clear()
      renderCards(allFoods.toSeq)
    }

    val showNutritionButton = new Button("Show Total Nutrition") {
      styleClass += "summary-button"
    }

    showNutritionButton.onAction = _ => {
      val totalCalories = selectedFoods.map(_.calories).sum
      val totalProtein = selectedFoods.map(_.protein).sum
      val totalFat = selectedFoods.map(_.fat).sum
      val totalCarbs = selectedFoods.map(_.carbs).sum

      val message =
        s"""
           |Total Nutrition:
           |Calories: $totalCalories kcal
           |Protein: $totalProtein g
           |Fat: $totalFat g
           |Carbs: $totalCarbs g
           |""".stripMargin

      new Alert(Alert.AlertType.Information) {
        title = "Total Nutrition"
        headerText = "Selected Food Summary"
        contentText = message
        initOwner(UserDashboard.this)
      }.showAndWait()
    }

    root = new BorderPane {
      top = filterBar
      center = new ScrollPane {
        content = foodCardPane
        fitToWidth = true
      }
      bottom = new VBox(10, showNutritionButton) {
        padding = Insets(10)
        alignment = Pos.Center
      }
    }

    renderCards(allFoods.toSeq)
  }
}