package com.example.dicodingeventapp.ui.finished

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingeventapp.R
import com.example.dicodingeventapp.data.remote.response.ListEventsItem
import com.example.dicodingeventapp.databinding.FragmentFinishedBinding
import com.example.dicodingeventapp.ui.adapters.ListEventAdapter
import com.example.dicodingeventapp.ui.detail.DetailActivity
import com.example.dicodingeventapp.ui.factory.EventViewModelFactory
import com.google.android.material.snackbar.Snackbar

class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val finishedViewModel = ViewModelProvider(
            this,
            EventViewModelFactory.getInstance()
        )[FinishedViewModel::class.java]

        finishedViewModel.listEvent.observe(viewLifecycleOwner) { listEvent ->
            setListEventData(listEvent)
        }

        finishedViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        finishedViewModel.noResults.observe(viewLifecycleOwner) {
            showNoResults(it)
        }

        finishedViewModel.errorMessage.observe(viewLifecycleOwner) { event ->
            if (event != null) {
                event.getContentIfNotHandled()?.let { errorMessage ->
                    Snackbar.make(requireView(), errorMessage, Snackbar.LENGTH_LONG)
                        .setAction("Retry") {
                            finishedViewModel.loadFinishedEvents()
                        }
                        .show()
                }
            }
        }

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvFinishedEvents.layoutManager = layoutManager

        val searchBar = binding.searchBarFinished
        searchBar.inflateMenu(R.menu.search_menu)

        with(binding) {
            searchViewFinished.setupWithSearchBar(searchBar)
            searchViewFinished
                .editText
                .setOnEditorActionListener { textView, _, _ ->
                    val query = textView.text.toString()
                    if (query.isNotEmpty()) {
                        searchBar.setText(query)
                        finishedViewModel.searchEvent(query)
                        searchViewFinished.hide()
                    }
                    false
                }
        }

        searchBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.navigation_close -> {
                    searchBar.setText("")
                    finishedViewModel.loadFinishedEvents()
                    true
                }
                else -> false
            }
        }
    }

    private fun setListEventData(listEventsItem: List<ListEventsItem>) {
        val adapter = ListEventAdapter {
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("eventId", it.id)
            startActivity(intent)
        }

        adapter.submitList(listEventsItem)
        binding.rvFinishedEvents.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showNoResults(isNoResults: Boolean) {
        binding.tvNoResults.visibility = if (isNoResults) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}