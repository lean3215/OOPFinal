package repository

import model.User
import scalikejdbc._

object UserRepository:

  def find(username: String, password: String): Option[User] =
    DB readOnly { implicit session =>
      sql"select * from users where username = $username and password = $password"
        .map { rs =>
          User(
            id = rs.long("id"),
            username = rs.string("username"),
            password = rs.string("password"),
            role = rs.string("role")
          )
        }.single.apply()
    }
