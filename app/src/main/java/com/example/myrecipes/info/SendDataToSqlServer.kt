package com.example.myrecipes.info

import android.content.Context
import android.os.AsyncTask
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.SQLException
import java.text.SimpleDateFormat
import java.util.*

class SendDataToSqlServer(
    private val context: Context,
    private val recipeId: Int,
    private val title: String,
    private val category: String,
    private val ingredients: String
) {
    private val dbUrl = "jdbc:sqlserver://172.20.190.215:1433;databaseName=recipes"
    private val dbUser = "sa"
    private val dbPassword = "a1n2a32008"

    fun execute() {
        // Usando AsyncTask para executar a operação de rede em segundo plano
        InsertRecipeTask().execute()
    }

    private inner class InsertRecipeTask : AsyncTask<Void, Void, Boolean>() {
        override fun doInBackground(vararg params: Void?): Boolean {
            return try {
                val connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)
                val query = "INSERT INTO MyRecipes (recipeId, Title, Category, Ingredients) VALUES (?, ?, ?, ?)"
                val preparedStatement = connection.prepareStatement(query)
                preparedStatement.setInt(1, recipeId)
                preparedStatement.setString(2, title)
                preparedStatement.setString(3, category)
                preparedStatement.setString(4, ingredients)

                val rowsInserted = preparedStatement.executeUpdate()
                preparedStatement.close()
                connection.close()

                rowsInserted > 0
            } catch (e: SQLException) {
                e.printStackTrace()
                false
            }
        }

        override fun onPostExecute(result: Boolean) {
            super.onPostExecute(result)
            // Mostrar mensagem ao usuário sobre o sucesso ou falha
            if (result) {
                Toast.makeText(context, "Dados enviados para o SQL Server com sucesso!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Erro ao enviar dados para o SQL Server.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
