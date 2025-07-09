package com.wiseduck.squadbuilder

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlin.math.sqrt
import android.os.Looper
import android.os.Handler
import android.util.DisplayMetrics
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.wiseduck.squadbuilder.databinding.ActivityMainBinding
import com.wiseduck.squadbuilder.fragment.ListFragment
import com.wiseduck.squadbuilder.fragment.SoccerFragment

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val soccerFragment = SoccerFragment()
    private val listFragment = ListFragment()
    // 만약 다른 프래그먼트도 필요하다면 여기서 추가

    private var backPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 라이트 모드 유지
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        // 화면 크기 계산
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val widthInInches = displayMetrics.widthPixels / displayMetrics.xdpi
        val heightInInches = displayMetrics.heightPixels / displayMetrics.ydpi
        val screenSizeInInches = sqrt((widthInInches * widthInInches) + (heightInInches * heightInInches))

        // 10인치 이상만 가로 모드를 허용
        if (screenSizeInInches < 10) {
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

    override fun onBackPressed() {
        if (backPressedOnce) {
            super.onBackPressed()
            return
        }

        this.backPressedOnce = true
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()

        // 2초 후에 backPressedOnce 값을 초기화
        Handler(Looper.getMainLooper()).postDelayed({
            backPressedOnce = false
        }, 2000)
    }
}
