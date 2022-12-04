package projects.vaid.newsapp.ui

import androidx.lifecycle.ViewModel
import projects.vaid.newsapp.repository.NewsRepository

class NewsViewModel(
    val newsRepository: NewsRepository
) : ViewModel(){
}