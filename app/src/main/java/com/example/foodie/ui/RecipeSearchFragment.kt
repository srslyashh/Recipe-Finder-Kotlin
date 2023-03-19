package com.example.foodie.ui

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodie.R
import com.example.foodie.data.LoadingStatus
import com.example.foodie.data.Recipe
import com.google.android.material.progressindicator.CircularProgressIndicator

class RecipeSearchFragment: Fragment(R.layout.recipe_search_fragment) {
    private val TAG = "MainActivity"

    private val simpleRecipeListAdapter = RecipeListAdapter(::onSimpleRecipeClick)
    private val viewModel: RecipeSearchViewModel by viewModels()

    private lateinit var searchResultsListRV: RecyclerView
    private lateinit var searchErrorTV: TextView
    private lateinit var loadingIndicator: CircularProgressIndicator

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        val searchBoxET: EditText = view.findViewById(R.id.et_search_box)
        val searchBtn: Button = view.findViewById(R.id.btn_search)

        searchErrorTV = view.findViewById(R.id.tv_search_error)
        loadingIndicator = view.findViewById(R.id.loading_indicator)

        /*
         * Set up RecyclerView.
         */
        searchResultsListRV = view.findViewById(R.id.rv_search_results)
        searchResultsListRV.layoutManager = LinearLayoutManager(requireContext())
        searchResultsListRV.setHasFixedSize(true)
        searchResultsListRV.adapter = simpleRecipeListAdapter

        /*
         * Set up an observer on the current search results.  Every time the search results change,
         * send the new search results into the RecyclerView adapter to be displayed.
         */
        viewModel.searchResults.observe(viewLifecycleOwner) { searchResults ->
            simpleRecipeListAdapter.updateSimpleRecipeList(searchResults)
        }

        /*
         * Set up an observer on the loading status of the API query.  Display the correct UI
         * elements based on the current loading status.
         */
        viewModel.loadingStatus.observe(viewLifecycleOwner) { loadingStatus ->
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
        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage->
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
        val directions = RecipeSearchFragmentDirections.navigateToRecipeDetail(recipe)
        findNavController().navigate(directions)
    }
}