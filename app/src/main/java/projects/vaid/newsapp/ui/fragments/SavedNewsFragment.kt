package projects.vaid.newsapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_saved_news.*
import projects.vaid.newsapp.R
import projects.vaid.newsapp.adapter.NewsAdapter
import projects.vaid.newsapp.ui.NewsActivity
import projects.vaid.newsapp.ui.NewsViewModel

class SavedNewsFragment : Fragment(R.layout.fragment_saved_news) {

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        setupRecyclerView()

        newsAdapter.setOnItemClickListener{

            val bundle = Bundle().apply{
                putSerializable("article", it)  //кладем объект Article
            }

            findNavController().navigate(
                R.id.action_savedNewsFragment_to_articleFragment, //action
                bundle    //передаем объект Article во фрагмент
            )
        }
    }

    private fun setupRecyclerView(){
        newsAdapter = NewsAdapter()
        recyclerSavedNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

}