// src/main/scala/controller/LoginController.scala
package controller

import model.{Admin, RegularUser, User}

class LoginController:

  // Hardcoded users (later we can load from DB or file)
  private val users: List[User] = List(
    Admin("admin", "admin123"),
    RegularUser("user", "user123")
  )

  def authenticate(username: String, password: String): Option[User] =
    users.find {
      case Admin(u, p) => u == username && p == password
      case RegularUser(u, p) => u == username && p == password
    }