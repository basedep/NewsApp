package projects.vaid.newsapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_news.*
import projects.vaid.newsapp.R
import projects.vaid.newsapp.database.ArticleDatabase
import projects.vaid.newsapp.repository.NewsRepository

class NewsActivity: AppCompatActivity() {

    lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(application, repository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)[NewsViewModel::class.java]

        setContentView(R.layout.activity_news)
        //соединяем с navigation
        bottomNavigationView.setupWithNavController(newsNavHostFragment.findNavController())

    }
}