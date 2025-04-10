package com.example.ecommerceapp

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    // Views
    private lateinit var bannerSlider: ViewPager2
    private lateinit var productRecyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var userIcon: ImageButton

    // Firebase components
    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.main_activity_layout)

        initializeViews()
        setupNavigationDrawer()
        setupBannerSlider()
        setupProductRecyclerView()
        setupSearchView()
        setupEdgeToEdge()
    }

    private fun initializeViews() {
        auth = Firebase.auth
        bannerSlider = findViewById(R.id.bannerSlider)
        productRecyclerView = findViewById(R.id.productRecyclerView)
        searchView = findViewById(R.id.searchView)
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        userIcon = findViewById(R.id.userIcon)

        // Set up user icon click
        userIcon.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    private fun setupNavigationDrawer() {
        val currentUser = auth.currentUser
        val headerView = navView.getHeaderView(0)
        val userName = headerView.findViewById<TextView>(R.id.userName)
        val userEmail = headerView.findViewById<TextView>(R.id.userEmail)

        currentUser?.let { user ->
            userName.text = user.displayName ?: "User"
            userEmail.text = user.email ?: ""
        }

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_sign_out -> {
                    signOutUser()
                    true
                }
                else -> false
            }
        }
    }

    private fun signOutUser() {
        auth.signOut()
        Toast.makeText(this, "Signed out successfully", Toast.LENGTH_SHORT).show()
        drawerLayout.closeDrawer(GravityCompat.START)
        // Add your navigation to login screen here
        // Example: startActivity(Intent(this, LoginActivity::class.java))
        // finish()
    }

    private fun setupBannerSlider() {
        db.collection("banners")
            .whereEqualTo("isActive", true)
            .get()
            .addOnSuccessListener { documents ->
                val banners = documents.map { doc ->
                    NavigationSlide.BannerItem(
                        imageUrl = doc.getString("imageUrl") ?: "",
                        offerText = doc.getString("offerText") ?: "",
                        isActive = doc.getBoolean("isActive") ?: true
                    )
                }
                bannerSlider.adapter = NavigationSlide(banners) { clickedBanner ->
                    Toast.makeText(this, "Selected: ${clickedBanner.offerText}", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Failed to load banners", Toast.LENGTH_SHORT).show()
            }
    }

    private fun setupProductRecyclerView() {
        productRecyclerView.layoutManager = LinearLayoutManager(this)
        // TODO: Initialize your product adapter
    }

    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { searchProducts(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { searchProducts(it) }
                return true
            }
        })
    }

    private fun searchProducts(query: String) {
        // TODO: Implement search functionality
    }

    private fun setupEdgeToEdge() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}

