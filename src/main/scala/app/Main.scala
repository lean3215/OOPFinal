package app

import scalafx.application.JFXApp3
import scalafx.scene.Scene
import javafx.fxml.FXMLLoader
import javafx.scene.{Parent}
import javafx.scene.image.Image
import util.DBSetup
import scalafx.Includes.*

object Main extends JFXApp3:
  override def start(): Unit =
    DBSetup.init()
    val loader = new FXMLLoader(getClass.getResource("/view/login.fxml"))
    val root: Parent = loader.load()
    stage = new JFXApp3.PrimaryStage:
      title = "Nutrition Software"
      icons += new Image(getClass.getResourceAsStream("/images/login-icon.jpg"))
      scene = new Scene(root)
      scene().getStylesheets.add(getClass.getResource("/DarkTheme.css").toExternalForm)
