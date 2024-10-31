package com.example.dicodingeventapp.ui.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.dicodingeventapp.R
import com.example.dicodingeventapp.data.local.FavoriteEvent
import com.example.dicodingeventapp.data.remote.response.Event
import com.example.dicodingeventapp.databinding.ActivityDetailBinding
import com.example.dicodingeventapp.ui.factory.EventViewModelFactory
import com.example.dicodingeventapp.ui.factory.FavoriteViewModelFactory
import com.example.dicodingeventapp.ui.factory.SettingViewModelFactory
import com.example.dicodingeventapp.ui.setting.SettingPreferences
import com.example.dicodingeventapp.ui.setting.SettingViewModel
import com.example.dicodingeventapp.ui.setting.datastore
import com.example.dicodingeventapp.util.Handler
import com.google.android.material.snackbar.Snackbar

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    private var isFavorite = false
    private var thisEvent = Event()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SettingPreferences.getInstance(application.datastore)
        val settingViewModel = ViewModelProvider(
            this,
            SettingViewModelFactory(pref)
        )[SettingViewModel::class.java]

        settingViewModel.getThemeSetting()
            .observe(this) { isDarkModeActive: Boolean ->
                if (isDarkModeActive) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val detailViewModel = ViewModelProvider(
            this,
            EventViewModelFactory.getInstance()
        )[DetailViewModel::class.java]

        val favoriteAddDeleteViewModel = ViewModelProvider(
            this,
            FavoriteViewModelFactory.getInstance(application)
        )[FavoriteAddDeleteViewModel::class.java]

        val eventId = intent.getIntExtra("eventId", -1).toString()
        detailViewModel.findEventById(eventId)

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        detailViewModel.event.observe(this) { event ->
            setEvent(event)
        }

        detailViewModel.errorMessage.observe(this) { event ->
            if (event != null) {
                event.getContentIfNotHandled()?.let { errorMessage ->
                    Snackbar.make(window.decorView.rootView, errorMessage, Snackbar.LENGTH_LONG)
                        .setAction("Retry") {
                            detailViewModel.findEventById(eventId)
                        }
                        .show()
                }
            }
        }

        favoriteAddDeleteViewModel.getFavoriteEventById(eventId).observe(this) { favEvent ->
            isFavorite = favEvent != null
            btnFavorite(isFavorite)
        }

        binding.fabFavorite.setOnClickListener {
            val favoriteEvent = FavoriteEvent(
                id = thisEvent.id.toString(),
                name = thisEvent.name ?: "",
                mediaCover = thisEvent.mediaCover,
            )

            if (isFavorite) {
                favoriteAddDeleteViewModel.delete(favoriteEvent)
            } else {
                favoriteAddDeleteViewModel.insert(favoriteEvent)
            }

            isFavorite = !isFavorite
            btnFavorite(isFavorite)
        }

    }

    @SuppressLint("SetTextI18n")
    private fun setEvent(event: Event) {
        thisEvent = event

        val remainingQuota = (event.quota ?: 0) - (event.registrants ?: 0)
        val beginTime = Handler.dateFormat(event.beginTime)
        val endTime = Handler.dateFormat(event.endTime)

        binding.tvEventName.text = event.name
        binding.tvEventOrganizer.text = event.ownerName
        binding.tvEventLocation.text = event.cityName
        binding.tvEvenTime.text = "$beginTime - $endTime"
        binding.tvEvenQuota.text = resources.getString(R.string.quota_remaining, remainingQuota)
        binding.tvEventDescription.text =
            event.description?.let { HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY) }

        Glide.with(binding.ivEventCover.context)
            .load(event.mediaCover)
            .into(binding.ivEventCover)

        binding.btnOpenLink.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event.link))
            startActivity(intent)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun btnFavorite(isFavorite: Boolean) {
        binding.fabFavorite.setImageResource(
            if (isFavorite) {
                R.drawable.baseline_favorite_24
            } else {
                R.drawable.baseline_favorite_border_24
            }
        )
    }
}