// src/main/scala/model/User.scala
package model

trait User:
  def username: String
  def role: String

case class Admin(username: String, password: String) extends User:
  val role = "admin"

case class RegularUser(username: String, password: String) extends User:
  val role = "user"