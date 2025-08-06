package util

import scalikejdbc._

object DBSetup:

  def init(): Unit =
    Class.forName("org.h2.Driver")
    ConnectionPool.singleton("jdbc:h2:file:./nutrition.db", "user", "pass")
    DB autoCommit { implicit session =>
      sql"""
        create table if not exists users (
          id bigint auto_increment primary key,
          username varchar(50) not null unique,
          password varchar(100) not null,
          role varchar(20) not null
        )
      """.execute.apply()
      sql"""
        create table if not exists food_items (
          id bigint auto_increment primary key,
          name varchar(255),
          calories int,
          protein double,
          fat double,
          carbs double,
          category varchar(100)
        )
      """.execute.apply()
      sql"""
        merge into users (id, username, password, role) key(username)
        values (null, 'admin', 'admin123', 'admin')
      """.update.apply()
      sql"""
        merge into users (id, username, password, role) key(username)
        values (null, 'user', 'user123', 'user')
      """.update.apply()
    }
