package com.example.myapp.activity

import androidx.activity.viewModels
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapp.R
import com.example.myapp.adapter.PokemonListAdapter
import com.example.myapp.databinding.ActivityPokemonBinding
import com.example.myapp.model.Result
import com.example.myapp.state.PokedexState
import com.example.myapp.util.ItemOffsetDecoration
import com.example.myapp.util.PaginationScrollListener
import com.example.myapp.viewmodel.PokedexViewModel
import kotlinx.coroutines.launch

class PokemonActivity : BaseActivity() {
    private lateinit var binding: ActivityPokemonBinding

    private var adapter = PokemonListAdapter()
    private val viewModel: PokedexViewModel by viewModels()

    private var currentItem = 0
    private var isLoading = false

    companion object {
        private const val CHANNEL_ID = "123"
        private const val ITEM_OFFSET = 20
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokemonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNotification.setOnClickListener {
            showNotification()
        }

        binding.rvPokemon.setHasFixedSize(true)
        binding.rvPokemon.layoutManager = GridLayoutManager(this, 2)
        val itemDecoration = ItemOffsetDecoration(this, R.dimen.spacing)
        binding.rvPokemon.addItemDecoration(itemDecoration)
        binding.rvPokemon.addOnScrollListener(object: PaginationScrollListener(binding.rvPokemon.layoutManager as GridLayoutManager) {
            override fun loadMoreItems() {
                currentItem += ITEM_OFFSET
                viewModel.fetchData(currentItem, ITEM_OFFSET)
                setLoading(true)
            }

            override fun isLoading(): Boolean {
                return isLoading
            }
        })
        binding.rvPokemon.adapter = adapter

        viewModel.fetchData(currentItem, ITEM_OFFSET)
        observeViewModelState()
    }

    private fun observeViewModelState() {
        lifecycleScope.launch {
            viewModel.state.collect {
                when (it) {
                    is PokedexState.Init -> {
                        setLoading(true)
                    }
                    is PokedexState.Success -> {
                        updatePokedex(it.data)
                        setLoading(false)
                    }
                    is PokedexState.Failure -> {
                        Toast.makeText(this@PokemonActivity, "Failed to load", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun updatePokedex(data: List<Result>) {
        adapter.processData(data, currentItem)
    }

    private fun showNotification() {
        createNotificationChannel()

        // create unique id
        val notificationId = kotlin.math.abs((0..999999999999).random()).toInt()

        // handle notification click
        val intent = Intent(this, PokemonActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Pokemon")
            .setContentText("Gotta catch 'em all")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            with (NotificationManagerCompat.from(this)) {
                notify(notificationId, notification.build())
            }
        } else {
            Toast.makeText(this, "Notification turned off", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            // register channel with system
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun setLoading(loading: Boolean) {
        isLoading = loading
        binding.tvLoading.visibility = if (loading) View.VISIBLE else View.GONE
    }
}