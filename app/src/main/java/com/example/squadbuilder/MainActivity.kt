package com.example.squadbuilder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.squadbuilder.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().replace(R.id.bottom_layout, SoccerFragment()).commit()
        setNavigation()
    }

    private fun setNavigation() {
        // 앱 실행 시 축구 포메이션 설정 화면을 기본 화면으로 설정
        binding.bottomNavigationView.selectedItemId = R.id.main_bottom_nav_soccer

        // 네비게이션 바 아이템 선택 시 화면 전환 설정
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.main_bottom_nav_soccer -> {
                    replaceFragment(SoccerFragment())
                    true
                }
                R.id.main_bottom_nav_football -> {
                    replaceFragment(FootballFragment())
                    true
                }
                R.id.main_bottom_nav_list -> {
                    replaceFragment(ListFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: androidx.fragment.app.Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.bottom_layout, fragment)
            .commit()
    }
}