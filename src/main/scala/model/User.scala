package model

import scalafx.beans.property.*

case class User(id: Long, username: String, password: String, role: String):
  val usernameProperty = StringProperty(username)
  val passwordProperty = StringProperty(password)
  val roleProperty = StringProperty(role)
