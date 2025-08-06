package controller

import model.{Admin, RegularUser, User}
import javafx.fxml.FXML
import javafx.scene.control.{TextField, PasswordField, Label}
import javafx.scene.paint.Color
import view.{AdminDashboard, UserDashboard}

class LoginController:

  @FXML private var usernameField: TextField = _
  @FXML private var passwordField: PasswordField = _
  @FXML private var statusLabel: Label = _

  // Hardcoded users (later we can load from DB or file)
  private val users: List[User] = List(
    Admin("admin", "admin123"),
    RegularUser("user", "user123")
  )

  def authenticate(username: String, password: String): Option[User] =
    users.find {
      case Admin(u, p)       => u == username && p == password
      case RegularUser(u, p) => u == username && p == password
    }

  @FXML
  def handleLogin(): Unit =
    authenticate(usernameField.getText, passwordField.getText) match
      case Some(_: Admin) =>
        statusLabel.setTextFill(Color.GREEN)
        statusLabel.setText("Welcome, Admin")
        usernameField.getScene.getWindow.hide()
        AdminDashboard.show()
      case Some(_: RegularUser) =>
        statusLabel.setTextFill(Color.BLUE)
        statusLabel.setText("Welcome, User")
        usernameField.getScene.getWindow.hide()
        new UserDashboard().show()
      case None =>
        statusLabel.setTextFill(Color.RED)
        statusLabel.setText("Invalid username or password")

