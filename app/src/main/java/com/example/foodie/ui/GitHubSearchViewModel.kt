package com.example.foodie.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodie.api.GitHubService
import com.example.foodie.data.GitHubRepo
import com.example.foodie.data.GitHubReposRepository
import com.example.foodie.data.LoadingStatus
import kotlinx.coroutines.launch

class GitHubSearchViewModel: ViewModel() {
    private val repository = GitHubReposRepository(GitHubService.create())

    /*
     * The most recent search results from the GitHub API are stored in this private property.
     * These search results are exposed to the outside world in immutable form via the public
     * `searchResults` property below.
     */
    private val _searchResults = MutableLiveData<List<GitHubRepo>?>(null)

    /**
     * This value provides the most recent search results returned from the GitHub API.  It is
     * null if there are no current search results (e.g. in the case of an error).
     */
    val searchResults: LiveData<List<GitHubRepo>?> = _searchResults

    /*
     * The current loading state is stored in this private property.  This loading state is exposed
     * to the outside world in immutable form via the public `loadingStatus` property below.
     */
    private val _loadingStatus = MutableLiveData<LoadingStatus>(LoadingStatus.SUCCESS)

    /**
     * This property indicates the current loading state of an API query.  It is `LOADING` if an
     * API query is currently being executed, `SUCCESS` of the most recent API query was
     * successful, and `ERROR` if the most recent API query resulted in an error.
     */
    val loadingStatus: LiveData<LoadingStatus> = _loadingStatus

    /*
     * The current error message for the most recent API query is stored in this private property.
     * This error message is exposed to the outside world in immutable form via the public
     * `errorMessage` property below.
     */
    private val _errorMessage = MutableLiveData<String?>(null)

    /**
     * This property provides the error message associated with the most recent API query, if
     * there is one.  If there was no error associated with the most recent API query, it will be
     * null.
     */
    val errorMessage: LiveData<String?> = _errorMessage

    /**
     * This method triggers a new search query to the GitHub API's "search repositories" method.
     * It updates the public properties of this ViewModel class to reflect the current status
     * of the API query.
     *
     * @param query The search query term to send to the GitHub API.
     * @param sort The sorting option to send to the GitHub API.
     * @param user A specific GitHub user/organization to which to limit search results.
     * @param languages A set of programming languages to which to limit search results.
     * @param firstIssues The minimum number of "good first issues" to which to limit search
     *   results.
     */
    fun loadSearchResults(
        query: String,
        sort: String?,
        user: String?,
        languages: Set<String>?,
        firstIssues: Int
    ) {
        viewModelScope.launch {
            _loadingStatus.value = LoadingStatus.LOADING
            _errorMessage.value = null
            val result = repository.loadRepositoriesSearch(query, sort, user, languages, firstIssues)
            _loadingStatus.value = when (result.isSuccess) {
                true -> LoadingStatus.SUCCESS
                false -> LoadingStatus.ERROR
            }
            _searchResults.value = result.getOrNull()
            _errorMessage.value = result.exceptionOrNull()?.message
        }
    }
}