package com.example.myrecipes.info

import android.content.Context
import java.sql.DriverManager
import java.sql.SQLException

class RecipeRepository(private val context: Context) {

    private val dbUrl = "jdbc:sqlserver://172.20.190.215:1433;databaseName=recipes"
    private val dbUser = "sa"
    private val dbPassword = "a1n2a32008"

    fun getAllRecipes(): List<Recipe> {
        val recipes = mutableListOf<Recipe>()

        try {
            DriverManager.getConnection(dbUrl, dbUser, dbPassword).use { connection ->
                val query = "SELECT recipeId, Title, Category, Ingredients FROM MyRecipes"
                connection.createStatement().use { statement ->
                    statement.executeQuery(query).use { resultSet ->
                        while (resultSet.next()) {
                            val recipe = Recipe(
                                resultSet.getInt("recipeId"),
                                resultSet.getString("Title"),
                                resultSet.getString("Category"),
                                resultSet.getString("Ingredients")
                            )
                            recipes.add(recipe)
                        }
                    }
                }
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }

        return recipes
    }
}
