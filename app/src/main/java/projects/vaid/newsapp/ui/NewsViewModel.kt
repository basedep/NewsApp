package projects.vaid.newsapp.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import projects.vaid.newsapp.application.NewsApplication
import projects.vaid.newsapp.model.Article
import projects.vaid.newsapp.model.NewsResponse
import projects.vaid.newsapp.repository.NewsRepository
import projects.vaid.newsapp.util.Resource
import retrofit2.Response
import java.io.EOFException
import java.io.IOException

class NewsViewModel(
    application: Application,
    private val newsRepository: NewsRepository
) : AndroidViewModel(application){

    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1

    init {
        getBreakingNews("ru")
    }

    private fun getBreakingNews(countryCode: String) = viewModelScope.launch {
           safeNewsCall(countryCode)
        }


     fun getSearchNews(searchQuery: String) = viewModelScope.launch {
           safeSearchBreakingNewsCall(searchQuery)
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


    //безопасный вызов
    private suspend fun safeSearchBreakingNewsCall(searchQuery: String){
        searchNews.postValue(Resource.Loading())   //передаем состояние Loading в livedata перед запросом
        try {
            if (hasInternetConnection()) {
                val response = newsRepository.getSearchNews(searchQuery, breakingNewsPage) //делаем сам запрос
                searchNews.postValue(handleSearchNewsResponse(response))        //передаем ответ запроса
            }else{
                searchNews.postValue(Resource.Error(message = "No internet connection"))
            }
        }catch (e: Exception){
            when(e){
                is IOException -> searchNews.postValue(Resource.Error(message = "Network Error"))
                else -> searchNews.postValue(Resource.Error(message = "Conversion error"))
            }
        }
    }


    private suspend fun safeNewsCall(countryCode: String){
        breakingNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = newsRepository.getBreakingNews(countryCode, breakingNewsPage)
                breakingNews.postValue(handleBreakingNewsResponse(response))
            }else{
                breakingNews.postValue(Resource.Error(message = "No internet connection"))
            }
        }catch (e: Exception){
            when(e){
                is IOException -> breakingNews.postValue(Resource.Error(message = "Network Error"))
                else -> breakingNews.postValue(Resource.Error(message = "Conversion error"))
            }
        }
    }


    //проверка подключения к интернету
    private fun hasInternetConnection(): Boolean{
        val connectivityManager = getApplication<NewsApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)?: return false
            return when{
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }else{
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    return when(type){
                        TYPE_WIFI -> true
                        TYPE_MOBILE -> true
                        TYPE_ETHERNET -> true
                        else -> false
                    }
                }
            }
        }
        return false
    }

}