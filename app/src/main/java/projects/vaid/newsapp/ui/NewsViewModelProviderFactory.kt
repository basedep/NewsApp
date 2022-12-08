package projects.vaid.newsapp.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import projects.vaid.newsapp.repository.NewsRepository

class NewsViewModelProviderFactory(
    val application: Application,
    private val newsRepository: NewsRepository
) : ViewModelProvider.Factory{

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
       return NewsViewModel(application, newsRepository) as T
    }
}