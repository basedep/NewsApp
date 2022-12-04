package projects.vaid.newsapp.repository

import projects.vaid.newsapp.database.ArticleDatabase
import projects.vaid.newsapp.retrofit.api.RetrofitInstance


class NewsRepository(val database: ArticleDatabase) {

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)

    suspend fun getSearchNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.searchForNews(countryCode, pageNumber)

}