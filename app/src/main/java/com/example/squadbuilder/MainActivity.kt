package com.example.squadbuilder

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.res.Configuration
import com.example.squadbuilder.R
import com.example.squadbuilder.databinding.ActivityMainBinding
import com.example.squadbuilder.fragment.FootballFragment
import com.example.squadbuilder.fragment.ListFragment
import com.example.squadbuilder.fragment.SoccerFragment

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val soccerFragment = SoccerFragment()
    private val listFragment = ListFragment()
    // 만약 다른 프래그먼트도 필요하다면 여기서 추가

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 화면이 작은 기기(예: 휴대폰)에서만 세로 모드 고정
        if (resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK < Configuration.SCREENLAYOUT_SIZE_LARGE) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        setContentView(binding.root)

        // 초기 프래그먼트를 추가하고 나머지 프래그먼트는 숨김
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.bottom_layout, soccerFragment, "SOCCER")
                .commit()
        }

        setNavigation()
    }

    private fun setNavigation() {
        // 앱 실행 시 축구 포메이션 설정 화면을 기본 화면으로 설정
        binding.bottomNavigationView.selectedItemId = R.id.main_bottom_nav_soccer

        // 네비게이션 바 아이템 선택 시 화면 전환 설정
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.main_bottom_nav_soccer -> {
                    showFragment(soccerFragment, "SOCCER")
                    true
                }
//                R.id.main_bottom_nav_football -> {
//                    showFragment(footballFragment, "FOOTBALL")
//                    true
//                }
                R.id.main_bottom_nav_list -> {
                    showFragment(listFragment, "LIST")
                    true
                }
                else -> false
            }
        }
    }

    private fun showFragment(fragment: androidx.fragment.app.Fragment, tag: String) {
        val transaction = supportFragmentManager.beginTransaction()

        // 현재 보이는 프래그먼트를 숨김
        supportFragmentManager.fragments.forEach {
            if (it.isVisible) transaction.hide(it)
        }

        // 이미 추가된 프래그먼트라면 show(), 아니면 add()
        if (supportFragmentManager.findFragmentByTag(tag) != null) {
            transaction.show(fragment)
        } else {
            transaction.add(R.id.bottom_layout, fragment, tag)
        }

        transaction.commit()
    }
}
