package projects.vaid.newsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_search_news.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import projects.vaid.newsapp.R
import projects.vaid.newsapp.adapter.NewsAdapter
import projects.vaid.newsapp.ui.NewsActivity
import projects.vaid.newsapp.ui.NewsViewModel
import projects.vaid.newsapp.util.Constants.Companion.SEARCH_DELAY
import projects.vaid.newsapp.util.Resource

class SearchNewsFragment : Fragment(R.layout.fragment_search_news) {

    lateinit var viewModel: NewsViewModel
    private val TAG = "SearchNewsFragment"
    lateinit var newsAdapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        setupRecyclerView()

        newsAdapter.setOnItemClickListener{
            val bundle = Bundle().apply{
                putParcelable("article", it)  //кладем объект Article
            }

            findNavController().navigate(
                R.id.action_searchNewsFragment_to_articleFragment, //action
                bundle    //передаем объект Article во фрагмент
            )
        }

        var job: Job? = null
        etSearch.addTextChangedListener{ editable ->  //при изменении текста
            job?.cancel()               //отменяем текущий Job и создаем новую
            job = MainScope().launch {
                delay(SEARCH_DELAY)
                editable?.let {
                    if (editable.toString().isNotEmpty())
                        viewModel.getSearchNews(editable.toString())  //делаем запрос с задержкой в 500
                }
            }
        }

        viewModel.searchNews.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let{ msg ->
                        Log.d(TAG, "Error: $msg")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }

    }

    private fun hideProgressBar(){
        searchPaginationProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar(){
        searchPaginationProgressBar.visibility = View.VISIBLE
    }

    private fun setupRecyclerView(){
        newsAdapter = NewsAdapter()
        recyclerSearchNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

}