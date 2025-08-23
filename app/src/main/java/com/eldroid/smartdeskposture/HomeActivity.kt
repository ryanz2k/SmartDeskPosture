package com.eldroid.smartdeskposture

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eldroid.smartdeskposture.model.PostureData
import com.eldroid.smartdeskposture.model.User
import com.eldroid.smartdeskposture.presenter.HomePresenter
import com.eldroid.smartdeskposture.presenter.HomePresenterImpl
import com.eldroid.smartdeskposture.view.HomeView
import com.eldroid.smartdeskposture.data.UserDataManager
import java.text.SimpleDateFormat
import java.util.*

class HomeActivity : AppCompatActivity(), HomeView {
    
    private lateinit var presenter: HomePresenter
    
    private lateinit var userNameTextView: TextView
    private lateinit var userEmailTextView: TextView
    private lateinit var postureRecyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var postureAdapter: PostureAdapter
    private lateinit var logoutButton: Button
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        
        presenter = HomePresenterImpl(this)
        initializeViews()
        setupRecyclerView()
        setupLogoutButton()
        loadData()
    }
    
    private fun initializeViews() {
        userNameTextView = findViewById(R.id.userNameTextView)
        userEmailTextView = findViewById(R.id.userEmailTextView)
        postureRecyclerView = findViewById(R.id.postureRecyclerView)
        progressBar = findViewById(R.id.progressBar)
        logoutButton = findViewById(R.id.logoutButton)
    }
    
    private fun setupRecyclerView() {
        postureAdapter = PostureAdapter()
        postureRecyclerView.layoutManager = LinearLayoutManager(this)
        postureRecyclerView.adapter = postureAdapter
    }
    
    private fun setupLogoutButton() {
        logoutButton.setOnClickListener {
            // Clear user data and go back to login
            UserDataManager.clearUser()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
    
    private fun loadData() {
        presenter.loadUserData()
        presenter.loadPostureData()
    }
    
    override fun displayUserInfo(user: User) {
        userNameTextView.text = "Welcome, ${user.name}!"
        userEmailTextView.text = user.email
    }
    
    override fun displayPostureData(postureDataList: List<PostureData>) {
        postureAdapter.updateData(postureDataList)
    }
    
    override fun showLoading() {
        progressBar.visibility = View.VISIBLE
    }
    
    override fun hideLoading() {
        progressBar.visibility = View.GONE
    }
    
    override fun showError(message: String) {
        // Handle error display
    }
}

// Custom adapter for displaying posture data with better formatting
class PostureAdapter : RecyclerView.Adapter<PostureAdapter.PostureViewHolder>() {
    
    private var postureDataList: List<PostureData> = emptyList()
    private val dateFormat = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
    
    fun updateData(newData: List<PostureData>) {
        postureDataList = newData
        notifyDataSetChanged()
    }
    
    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): PostureViewHolder {
        val view = android.view.LayoutInflater.from(parent.context)
            .inflate(R.layout.item_posture_data, parent, false)
        return PostureViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: PostureViewHolder, position: Int) {
        val postureData = postureDataList[position]
        holder.bind(postureData, dateFormat)
    }
    
    override fun getItemCount(): Int = postureDataList.size
    
    class PostureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val postureTypeTextView: TextView = itemView.findViewById(R.id.postureTypeTextView)
        private val durationTextView: TextView = itemView.findViewById(R.id.durationTextView)
        private val timestampTextView: TextView = itemView.findViewById(R.id.timestampTextView)
        private val reminderCountTextView: TextView = itemView.findViewById(R.id.reminderCountTextView)
        
        fun bind(postureData: PostureData, dateFormat: SimpleDateFormat) {
            postureTypeTextView.text = "Posture: ${postureData.postureType}"
            durationTextView.text = "${postureData.duration} min"
            
            val timestamp = Date(postureData.timestamp)
            timestampTextView.text = dateFormat.format(timestamp)
            
            reminderCountTextView.text = "Reminders: ${postureData.reminderCount}"
            
            // Set color based on posture type
            val color = when (postureData.postureType) {
                "Good" -> android.graphics.Color.parseColor("#4CAF50") // Green
                "Fair" -> android.graphics.Color.parseColor("#FF9800") // Orange
                "Poor" -> android.graphics.Color.parseColor("#F44336") // Red
                else -> android.graphics.Color.BLACK
            }
            postureTypeTextView.setTextColor(color)
        }
    }
}
