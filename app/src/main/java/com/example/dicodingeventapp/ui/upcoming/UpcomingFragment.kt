package com.example.dicodingeventapp.ui.upcoming

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
import com.example.dicodingeventapp.databinding.FragmentUpcomingBinding
import com.example.dicodingeventapp.ui.adapters.ListEventAdapter
import com.example.dicodingeventapp.ui.detail.DetailActivity
import com.example.dicodingeventapp.ui.factory.EventViewModelFactory
import com.google.android.material.snackbar.Snackbar

class UpcomingFragment : Fragment() {
    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val upcomingViewModel = ViewModelProvider(
            this,
            EventViewModelFactory.getInstance()
        )[UpcomingViewModel::class.java]

        upcomingViewModel.listEvent.observe(viewLifecycleOwner) {
            setListEventData(it)
        }

        upcomingViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        upcomingViewModel.errorMessage.observe(viewLifecycleOwner) { event ->
            if (event != null) {
                event.getContentIfNotHandled()?.let { errorMessage ->
                    Snackbar.make(requireView(), errorMessage, Snackbar.LENGTH_LONG)
                        .setAction("Retry") {
                            upcomingViewModel.loadUpcomingEvents()
                        }
                        .show()
                }
            }
        }

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvUpcomingEvents.layoutManager = layoutManager

        val searchBar = binding.searchBar
        searchBar.inflateMenu(R.menu.search_menu)

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, _, _ ->
                    val query = textView.text.toString()
                    if (query.isNotEmpty()) {
                        searchBar.setText(query)
                        upcomingViewModel.searchEvent(query)
                        searchView.hide()
                    }
                    false
                }

            upcomingViewModel.noResults.observe(viewLifecycleOwner) {
                showNoResults(it)
            }
        }

        searchBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.navigation_close -> {
                    searchBar.setText("")
                    upcomingViewModel.loadUpcomingEvents()
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
        binding.rvUpcomingEvents.adapter = adapter
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