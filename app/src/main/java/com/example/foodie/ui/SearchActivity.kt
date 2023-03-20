package com.example.foodie.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodie.R
import com.example.foodie.data.Recipe
import androidx.navigation.fragment.findNavController
import com.example.foodie.data.LoadingStatus
import com.google.android.material.progressindicator.CircularProgressIndicator

class SearchActivity : AppCompatActivity() {
    private val TAG = "SearchActivity"

    private val simpleRecipeListAdapter = RecipeListAdapter(::onSimpleRecipeClick)
    private val viewModel: RecipeSearchViewModel by viewModels()

    private lateinit var searchResultsListRV: RecyclerView
    private lateinit var searchErrorTV: TextView
    private lateinit var loadingIndicator: CircularProgressIndicator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_recipe)

        val searchBoxET: EditText = findViewById(R.id.et_search_box)
        val searchBtn: Button = findViewById(R.id.btn_search)

        searchErrorTV = findViewById(R.id.tv_search_error)
        loadingIndicator = findViewById(R.id.loading_indicator)

        /*
         * Set up RecyclerView.
         */
        searchResultsListRV = findViewById(R.id.rv_search_results)
        searchResultsListRV.layoutManager = LinearLayoutManager(this)
        searchResultsListRV.setHasFixedSize(true)
        searchResultsListRV.adapter = simpleRecipeListAdapter

        /*
         * Set up an observer on the current search results.  Every time the search results change,
         * send the new search results into the RecyclerView adapter to be displayed.
         */
        viewModel.searchResults.observe(this) { searchResults ->
            simpleRecipeListAdapter.updateSimpleRecipeList(searchResults)
        }

        /*
         * Set up an observer on the loading status of the API query.  Display the correct UI
         * elements based on the current loading status.
         */
        viewModel.loadingStatus.observe(this) { loadingStatus ->
            when (loadingStatus) {
                LoadingStatus.LOADING -> {
                    loadingIndicator.visibility = View.VISIBLE
                    searchResultsListRV.visibility = View.INVISIBLE
                    searchErrorTV.visibility = View.INVISIBLE
                }
                LoadingStatus.ERROR -> {
                    loadingIndicator.visibility = View.INVISIBLE
                    searchResultsListRV.visibility = View.INVISIBLE
                    searchErrorTV.visibility = View.VISIBLE
                }
                else -> {
                    loadingIndicator.visibility = View.INVISIBLE
                    searchResultsListRV.visibility = View.VISIBLE
                    searchErrorTV.visibility = View.INVISIBLE
                }
            }
        }

        /*
         * Set up an observer on the error message associated with the current API query.  If the
         * error message is not null, display it to the user.
         */
        viewModel.errorMessage.observe(this) { errorMessage ->
            if (errorMessage != null) {
                Log.d(TAG, "Error executing search query: $errorMessage")
                searchErrorTV.text = getString(R.string.search_error, errorMessage)
            }
        }

        searchBtn.setOnClickListener {
            val query = searchBoxET.text.toString()
            if (!TextUtils.isEmpty(query)) {
                viewModel.searchRecipes(query)
                searchResultsListRV.scrollToPosition(0)
            }
        }
    }

    private fun onSimpleRecipeClick(recipe: Recipe) {
        val intent = Intent(this, RecipeDetailActivity::class.java)
        intent.putExtra(EXTRA_FAVORITE_RECIPE, recipe)
        startActivity(intent)
    }
}