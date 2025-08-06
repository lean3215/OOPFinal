package view

import scalafx.stage.{Modality, Stage}
import scalafx.scene.Scene
import scalafx.Includes._
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import model.FoodItem

object AddFoodPopup:

  def show(onAdd: FoodItem => Unit): Unit =
    val loader = new FXMLLoader(getClass.getResource("/view/add_food_dialog.fxml"))
    val root: Parent = loader.load()
    val controller = loader.getController[AddFoodDialogController]
    val stage = new Stage:
      title = "Add Food"
      scene = new Scene(root)
      initModality(Modality.ApplicationModal)
    controller.setDialogStage(stage)
    controller.setOnAdd(onAdd)
    stage.showAndWait()
