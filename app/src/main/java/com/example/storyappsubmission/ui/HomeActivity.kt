package com.example.storyappsubmission.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyappsubmission.R
// import com.example.storyappsubmission.data.remote.response.ListStoryItem
import com.example.storyappsubmission.databinding.ActivityHomeBinding
import com.example.storyappsubmission.helper.showToast
import com.example.storyappsubmission.ui.adapter.StoryAdapter
import com.example.storyappsubmission.viewmodel.AuthViewModel
import com.example.storyappsubmission.viewmodel.StoryViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val storyViewModel: StoryViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeToolbar()

        // Setup FAB
        binding.fabAddStory.setOnClickListener {
            val intent = Intent(this@HomeActivity, AddStoryActivity::class.java)
            startActivity(intent)
        }

        // Setup RecyclerView
        val layoutManager = LinearLayoutManager(this@HomeActivity)
        binding.rvStoryCard.layoutManager = layoutManager

        // ViewModels
        //        storyViewModel.listStories.observe(this) { stories ->
        //            setStoriesData(stories)
        //        }

        storyViewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) binding.progressBar.visibility = View.VISIBLE
            else binding.progressBar.visibility = View.GONE
        }

        storyViewModel.feedbackToast.observe(this) { errorMessage ->
            errorMessage?.showToast(this)
        }

        getStories()
    }

    //    override fun onResume() {
    //        super.onResume()
    //        storyViewModel.getAllStories()
    //    }

    //    private fun setStoriesData(storiesList: List<ListStoryItem>) {
    //        val adapter = StoryAdapter()
    //        adapter.submitList(storiesList)
    //        binding.rvStoryCard.adapter = adapter
    //
    //        if (storiesList.isEmpty()) binding.tvEmpty.visibility = View.VISIBLE
    //        else binding.tvEmpty.visibility = View.GONE
    //    }

    private fun getStories() {
        val adapter = StoryAdapter()
        binding.rvStoryCard.adapter = adapter
        storyViewModel.story.observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }

    private fun initializeToolbar() {
        with(binding) {
            toolbar.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.logout -> {
                        removeSession()
                        true
                    }
                    R.id.maps -> {
                        val intent = Intent(this@HomeActivity, MapsActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun removeSession() {
        authViewModel.removeToken {
            startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
            finish()
        }
    }
}