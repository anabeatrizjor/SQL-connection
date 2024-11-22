package com.example.myrecipes

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myrecipes.R
import com.example.myrecipes.info.RecipeAdapter
import com.example.myrecipes.info.RecipeRepository
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var recipeRepository: RecipeRepository
    private lateinit var recipeAdapter: RecipeAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recipeRepository = RecipeRepository(this)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val recipes = recipeRepository.getAllRecipes()
                if (recipes.isNotEmpty()) {
                    recipeAdapter = RecipeAdapter(recipes)
                    recyclerView.adapter = recipeAdapter
                } else {
                    // Log ou mensagem de erro se não houver receitas
                    Log.d("MainActivity", "Nenhuma receita encontrada.")
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Erro ao carregar receitas: ${e.message}")
            }
        }

    }
}
