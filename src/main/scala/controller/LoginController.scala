package controller

import javafx.fxml.FXML
import javafx.scene.control.{PasswordField, TextField, Alert}
import javafx.scene.control.Alert.AlertType
import javafx.stage.Stage
import repository.UserRepository
import javafx.fxml.FXMLLoader
import javafx.scene.{Parent, Scene}
import javafx.scene.image.Image
import util.DBSetup

class LoginController:
  @FXML private var usernameField: TextField = _
  @FXML private var passwordField: PasswordField = _

  @FXML private def handleLogin(): Unit =
    val username = usernameField.getText.trim
    val password = passwordField.getText.trim
    if username.isEmpty || password.isEmpty then
      val alert = new Alert(AlertType.WARNING)
      alert.setTitle("Invalid Input")
      alert.setHeaderText("Missing Fields")
      alert.setContentText("Please enter username and password")
      alert.showAndWait()
    else
      UserRepository.find(username, password) match
        case Some(user) =>
          val loader = new FXMLLoader(getClass.getResource("/view/user_dashboard.fxml"))
          val root: Parent = loader.load()
          val stage = new Stage()
          stage.setTitle("Dashboard")
          stage.getIcons.add(new Image(getClass.getResourceAsStream("/images/login-icon.jpg")))
          val scene = new Scene(root)
          scene.getStylesheets.add(getClass.getResource("/DarkTheme.css").toExternalForm)
          stage.setScene(scene)
          stage.show()
          // close login window
          val currentStage = usernameField.getScene.getWindow.asInstanceOf[Stage]
          currentStage.close()
        case None =>
          val alert = new Alert(AlertType.ERROR)
          alert.setTitle("Login Failed")
          alert.setHeaderText("Invalid Credentials")
          alert.setContentText("Username or password incorrect")
          alert.showAndWait()
