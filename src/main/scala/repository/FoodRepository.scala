package repository

import model.FoodItem
import scalikejdbc._

object FoodRepository:

  def getAll(): List[FoodItem] =
    DB readOnly { implicit session =>
      sql"select * from food_items"
        .map { rs =>
          FoodItem(
            id = rs.long("id"),
            name = rs.string("name"),
            calories = rs.int("calories"),
            protein = rs.double("protein"),
            fat = rs.double("fat"),
            carbs = rs.double("carbs"),
            category = rs.string("category")
          )
        }.list.apply()
    }

  def insert(food: FoodItem): Unit =
    DB autoCommit { implicit session =>
      sql"""
        insert into food_items (name, calories, protein, fat, carbs, category)
        values (${food.name}, ${food.calories}, ${food.protein}, ${food.fat}, ${food.carbs}, ${food.category})
      """.update.apply()
    }

  def update(food: FoodItem): Unit =
    DB autoCommit { implicit session =>
      sql"""
        update food_items
        set name = ${food.name},
            calories = ${food.calories},
            protein = ${food.protein},
            fat = ${food.fat},
            carbs = ${food.carbs},
            category = ${food.category}
        where id = ${food.id}
      """.update.apply()
    }

  def delete(id: Long): Unit =
    DB autoCommit { implicit session =>
      sql"delete from food_items where id = $id".update.apply()
    }

