package com.example.dicodingeventapp.ui.favorite

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingeventapp.data.remote.response.ListEventsItem
import com.example.dicodingeventapp.databinding.FragmentFavoriteBinding
import com.example.dicodingeventapp.ui.adapters.ListHomeVerticalAdapter
import com.example.dicodingeventapp.ui.detail.DetailActivity
import com.example.dicodingeventapp.ui.factory.ViewModelFactory

class FavoriteFragment : Fragment() {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val favoriteViewModel = ViewModelProvider(
            this,
            ViewModelFactory.getInstance(requireActivity().application)
        )[FavoriteViewModel::class.java]

        favoriteViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        favoriteViewModel.favoriteEvents.observe(viewLifecycleOwner) { favoriteEvents ->
            val items = arrayListOf<ListEventsItem>()

            favoriteEvents.map {
                val item = ListEventsItem(
                    id = it.id.toInt(),
                    name = it.name,
                    mediaCover = it.mediaCover,
                    summary = it.summary
                )

                items.add(item)
            }

            val adapter = ListHomeVerticalAdapter {
                val intent = Intent(requireContext(), DetailActivity::class.java)
                intent.putExtra("eventId", it.id)
                startActivity(intent)
            }

            adapter.submitList(items)
            binding.rvFavoriteEvents.adapter = adapter
        }

        favoriteViewModel.noFavorite.observe(viewLifecycleOwner) {
            showNoFavorite(it)
        }

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvFavoriteEvents.layoutManager = layoutManager
    }

    private fun showNoFavorite(isNoFavorite: Boolean){
        binding.tvNoFavorite.visibility = if (isNoFavorite) View.VISIBLE else View.GONE
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}