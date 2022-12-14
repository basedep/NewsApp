package projects.vaid.newsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_breaking_news.*
import projects.vaid.newsapp.R
import projects.vaid.newsapp.adapter.NewsAdapter
import projects.vaid.newsapp.ui.NewsActivity
import projects.vaid.newsapp.ui.NewsViewModel
import projects.vaid.newsapp.util.Resource

class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {

    private val TAG = "BreakingNewsFragment"
    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel //viewModel из активити
        setupRecyclerView()

        //при нажатии
        newsAdapter.setOnItemClickListener{
            val bundle = Bundle().apply{
                putParcelable("article", it)  //кладем объект Article
            }

            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleFragment, //action
                 bundle    //передаем объект Article во фрагмент
            )
        }

        viewModel.breakingNews.observe(viewLifecycleOwner) { response ->
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
                        Toast.makeText(requireContext(), "No internet connection", Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }

    }

    private fun hideProgressBar(){
        paginationProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar(){
        paginationProgressBar.visibility = View.VISIBLE
    }

    private fun setupRecyclerView(){
        newsAdapter = NewsAdapter()
        recyclerBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

}