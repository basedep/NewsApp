package projects.vaid.newsapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import projects.vaid.newsapp.model.Article
import projects.vaid.newsapp.model.NewsResponse
import projects.vaid.newsapp.repository.NewsRepository
import projects.vaid.newsapp.util.Resource
import retrofit2.Response

class NewsViewModel(val newsRepository: NewsRepository) : ViewModel(){

    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1

    init {
        getBreakingNews("ru")
    }

    private fun getBreakingNews(countryCode: String) =
        viewModelScope.launch {
            breakingNews.postValue(Resource.Loading())  //передаем состояние Loading в livedata перед запросом
            val response = newsRepository.getBreakingNews(countryCode, breakingNewsPage) //делаем сам запрос
            breakingNews.postValue(handleBreakingNewsResponse(response)) //передаем ответ запроса
        }


     fun getSearchNews(searchQuery: String) =
        viewModelScope.launch {
            searchNews.postValue(Resource.Loading())
            val response = newsRepository.getSearchNews(searchQuery, searchNewsPage)
            searchNews.postValue(handleSearchNewsResponse(response))
        }


    //Возвращаем ответ в виде объекта Resource зависимости от результат запроса
    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { responseResult ->
                return Resource.Success(responseResult)
            }
        }
        return Resource.Error(message = response.message())
    }


    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { responseResult ->
                return Resource.Success(responseResult)
            }
        }
        return Resource.Error(message = response.message())
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.insertArticle(article)
    }

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }

    fun getAllArticles() = newsRepository.getAllArticles()

}