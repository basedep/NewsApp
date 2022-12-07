package projects.vaid.newsapp.repository

import androidx.lifecycle.LiveData
import projects.vaid.newsapp.database.ArticleDatabase
import projects.vaid.newsapp.model.Article
import projects.vaid.newsapp.retrofit.api.RetrofitInstance


class NewsRepository(private val database: ArticleDatabase) {

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)

    suspend fun getSearchNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.searchForNews(countryCode, pageNumber)

    suspend fun insertArticle(article: Article) =
        database.getDao().insertArticle(article)

    suspend fun deleteArticle(article: Article) =
        database.getDao().deleteArticle(article)

    fun getAllArticles():LiveData<List<Article>> =
        database.getDao().getAllArticles()

}