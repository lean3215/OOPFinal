package app

import scalafx.application.JFXApp3
import scalafx.scene.Scene
import javafx.fxml.FXMLLoader
import scalafx.Includes._

object Main extends JFXApp3:
  repository.FoodRepository.setup()
  override def start(): Unit =
    val loader = new FXMLLoader(getClass.getResource("/login.fxml"))
    val root = loader.load[javafx.scene.Parent]()
    stage = new JFXApp3.PrimaryStage:
      title = "Login"
      scene = new Scene(root, 400, 350)

