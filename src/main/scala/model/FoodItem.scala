// src/main/scala/model/FoodItem.scala
package model

import scalafx.beans.property._

case class FoodItem(
                     id: Long,
                     name: String,
                     calories: Int,
                     protein: Double,
                     fat: Double,
                     carbs: Double,
                     category: String
                   ):
  val nameProperty = StringProperty(name)
  val caloriesProperty = IntegerProperty(calories)
  val proteinProperty = DoubleProperty(protein)
  val fatProperty = DoubleProperty(fat)
  val carbsProperty = DoubleProperty(carbs)
  val categoryProperty = StringProperty(category)