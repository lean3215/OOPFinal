package app

import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label, PasswordField, TextField}
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout.*
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.paint.Color
import scalafx.scene.text.{Font, FontWeight}
import scalafx.scene.paint.LinearGradient
import scalafx.scene.paint.CycleMethod
import scalafx.scene.paint.Stop
import scalafx.collections.ObservableBuffer
import repository.FoodRepository
import view.UserDashboard

object Main extends JFXApp3:
  repository.FoodRepository.setup()
  override def start(): Unit =

    val usernameField = new TextField:
      promptText = "Enter username"
      style = "-fx-background-radius: 8; -fx-border-radius: 8;"

    val passwordField = new PasswordField:
      promptText = "Enter password"
      style = "-fx-background-radius: 8; -fx-border-radius: 8;"

    val statusLabel = new Label:
      textFill = Color.Red
      font = Font.font("Arial", FontWeight.Normal, 20)

    val loginButton = new Button("🔐 Login"):
      style =
        "-fx-background-color: #00796b; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10;"
      onAction = _ =>
        val username = usernameField.text.value
        val password = passwordField.text.value
        if username == "admin" && password == "admin123" then
          statusLabel.textFill = Color.Green
          statusLabel.text = s"Welcome, Admin"
          stage.close()
          view.AdminDashboard.show()
        else if username == "user" && password == "user123" then
          statusLabel.textFill = Color.Blue
          statusLabel.text = s"Welcome, User"
          stage.close()
          new UserDashboard().show()
        else
          statusLabel.textFill = Color.Red
          statusLabel.text = "Invalid username or password"

    val card = new VBox(15) {
      alignment = Pos.Center
      padding = Insets(30)
      style =
        "-fx-background-color: rgba(255, 255, 255, 0.7); " +
          "-fx-background-radius: 15; " +
          "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0.5, 0.0, 0.0);"
      children = List(
        new Label("🍏 Nutritional Info Login") {
          font = Font.font("Segoe UI", FontWeight.Bold, 28)
          textFill = Color.web("#2e7d32")
        },
        usernameField,
        passwordField,
        loginButton,
        statusLabel
      )
    }

    val backgroundImage = new Image(getClass.getResource("/images/login-icon.jpg").toString)
    val backgroundView = new ImageView(backgroundImage)
    backgroundView.setPreserveRatio(false)

    val root = new StackPane {
      children = Seq(backgroundView, card)
      alignment = Pos.Center
    }

    // ✅ First create scene and stage
    stage = new JFXApp3.PrimaryStage:
      title = "Login"
      scene = new Scene(root, 400, 350)

    // ✅ Then bind image size *after* stage is initialized
    backgroundView.fitWidth <== stage.width
    backgroundView.fitHeight <== stage.height