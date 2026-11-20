package com.quila.bkksmkn6.ui

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.quila.bkksmkn6.R
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()   // tampil sampai ke belakang status bar (wajib di Android 15+)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Ambil view dari layout dengan findViewById
        val rootMain = findViewById<View>(R.id.rootMain)
        val appBar = findViewById<AppBarLayout>(R.id.appBar)
        val pager = findViewById<ViewPager2>(R.id.pager)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        // Beri jarak di atas header sebesar tinggi status bar (jam, sinyal, baterai),
        // supaya logo tidak menabrak. Latar header yang putih ikut sampai ke atas.
        ViewCompat.setOnApplyWindowInsetsListener(rootMain) { _, insets ->
            val atas = insets.getInsets(
                WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.displayCutout()
            ).top
            appBar.updatePadding(top = atas)
            insets   // dikembalikan apa adanya, jadi navigasi bawah mengatur jaraknya sendiri
        }

        // Urutan menu navigasi bawah = urutan halaman
        val menuHalaman = listOf(R.id.nav_jobs, R.id.nav_news, R.id.nav_companies)

        pager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = menuHalaman.size
            override fun createFragment(position: Int): Fragment = when (position) {
                0 -> JobsFragment()
                1 -> NewsFragment()
                else -> CompaniesFragment()
            }
        }
        pager.isUserInputEnabled = false          // pindah halaman lewat navigasi bawah saja
        pager.offscreenPageLimit = menuHalaman.size // semua halaman tetap hidup

        bottomNav.setOnItemSelectedListener { item ->
            val index = menuHalaman.indexOf(item.itemId)
            if (index < 0) return@setOnItemSelectedListener false
            pager.setCurrentItem(index, false)
            appBar.setExpanded(true, true)          // header logo muncul lagi
            true
        }
    }
}