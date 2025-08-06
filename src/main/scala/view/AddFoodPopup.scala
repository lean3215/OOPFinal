package view

import scalafx.scene.Scene
import scalafx.stage.Stage
import javafx.fxml.FXMLLoader
import scalafx.Includes._
import model.FoodItem
import java.util.ResourceBundle
import scalafx.scene.Parent
import scalafx.scene.image.Image

object AddFoodPopup:

  def show(onAdd: FoodItem => Unit): Unit =
    val bundle = ResourceBundle.getBundle("i18n.messages")
    val loader = new FXMLLoader(getClass.getResource("/fxml/AddFoodPopup.fxml"), bundle)
    val root: Parent = loader.load()
    val controller = loader.getController[AddFoodController]
    controller.setOnAdd(onAdd)
    val stage = new Stage:
      title = bundle.getString("add.title")
      scene = new Scene(root)
    controller.setStage(stage)
    val iconStream = getClass.getResourceAsStream("/images/login-icon.jpg")
    if iconStream != null then stage.icons += new Image(iconStream)
    stage.showAndWait()
