package com.example.squadbuilder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.ImageView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SoccerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SoccerFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SoccerFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SoccerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}