package com.example.squadbuilder.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.ImageView
import com.example.squadbuilder.R

/**
 * A simple [Fragment] subclass.
 * Use the [SoccerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SoccerFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_soccer, container, false)
        val soccerField = view.findViewById<FrameLayout>(R.id.soccerFieldLayout)

        // 리스너를 명시적으로 생성
        val layoutListener = object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                // 뷰의 크기가 결정된 후 실행
                soccerField.viewTreeObserver.removeOnGlobalLayoutListener(this) // 리스너 제거

                // 4-3-3 포메이션 좌표 설정 (x, y 비율)
                val positions = arrayOf(
                    // 수비 라인 (4명)
                    Pair(0.1f, 0.8f), Pair(0.3f, 0.8f), Pair(0.7f, 0.8f), Pair(0.9f, 0.8f),
                    // 미드필더 라인 (3명)
                    Pair(0.2f, 0.5f), Pair(0.5f, 0.5f), Pair(0.8f, 0.5f),
                    // 공격 라인 (3명)
                    Pair(0.2f, 0.2f), Pair(0.5f, 0.2f), Pair(0.8f, 0.2f),
                    // 골키퍼 (1명)
                    Pair(0.5f, 0.9f)
                )

                val fieldWidth = soccerField.width
                val fieldHeight = soccerField.height

                for (i in positions.indices) {
                    val player = ImageView(context)
                    player.layoutParams = FrameLayout.LayoutParams(100, 100).apply {
                        leftMargin = (positions[i].first * fieldWidth).toInt() - 50 // 중심 조정을 위해 -50
                        topMargin = (positions[i].second * fieldHeight).toInt() - 50 // 중심 조정을 위해 -50
                    }
                    player.setImageResource(R.drawable.player_shape) // 원 모양 배경 이미지
                    soccerField.addView(player)
                }
            }
        }

        // 리스너 등록
        soccerField.viewTreeObserver.addOnGlobalLayoutListener(layoutListener)

        return view
    }
}