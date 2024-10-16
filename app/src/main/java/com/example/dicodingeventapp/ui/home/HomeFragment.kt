package com.example.dicodingeventapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingeventapp.data.remote.response.ListEventsItem
import com.example.dicodingeventapp.databinding.FragmentHomeBinding
import com.example.dicodingeventapp.ui.adapters.ListHomeHorizontalAdapter
import com.example.dicodingeventapp.ui.adapters.ListHomeVerticalAdapter
import com.example.dicodingeventapp.ui.detail.DetailActivity
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val homeViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[HomeViewModel::class.java]

        homeViewModel.upcomingEvents.observe(viewLifecycleOwner) { listEventsItem ->
            setUpcomingEventsData(listEventsItem)
        }

        homeViewModel.finishedEvents.observe(viewLifecycleOwner) { listEventsItem ->
            setFinishedEventsData(listEventsItem)
        }

        homeViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        homeViewModel.errorMessage.observe(viewLifecycleOwner) { event ->
            if (event != null) {
                event.getContentIfNotHandled()?.let { errorMessage ->
                    Snackbar.make(requireView(), errorMessage, Snackbar.LENGTH_LONG).setDuration(3000)
                        .setAction("Retry") {
                            homeViewModel.load()
                        }
                        .show()
                }
            }
        }

        val layoutManagerVertical = LinearLayoutManager(requireContext())
        val layoutManagerHorizontal =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvUpcomingHr.layoutManager = layoutManagerHorizontal
        binding.rvFinishedVr.layoutManager = layoutManagerVertical
    }

    private fun setUpcomingEventsData(listEventsItem: List<ListEventsItem>) {
        val adapter = ListHomeHorizontalAdapter {
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("eventId", it.id)
            startActivity(intent)
        }
        adapter.submitList(listEventsItem)
        binding.rvUpcomingHr.adapter = adapter
    }

    private fun setFinishedEventsData(listEventsItem: List<ListEventsItem>) {
        val adapter = ListHomeVerticalAdapter {
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("eventId", it.id)
            startActivity(intent)
        }
        adapter.submitList(listEventsItem)
        binding.rvFinishedVr.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}