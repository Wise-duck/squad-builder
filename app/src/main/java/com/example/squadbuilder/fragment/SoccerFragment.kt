package com.example.squadbuilder.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.squadbuilder.R
import com.example.squadbuilder.data.Player
import com.example.squadbuilder.databinding.FragmentSoccerBinding
import com.example.squadbuilder.viewmodel.PlayerViewModel

class SoccerFragment : Fragment() {

    private lateinit var binding: FragmentSoccerBinding
    private val playerViewModel: PlayerViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSoccerBinding.inflate(inflater, container, false)

        // ViewModel을 사용하여 플레이어 리스트를 관찰
        playerViewModel.players.observe(viewLifecycleOwner, Observer { players ->
            if (!players.isNullOrEmpty()) {
                binding.soccerFieldLayout.removeAllViews()  // 기존 플레이어 제거
                val fieldWidth = binding.soccerFieldLayout.width
                val fieldHeight = binding.soccerFieldLayout.height

                Log.d("PLAYER", players.toString())
                players.forEach { player ->
                    addPlayerToField(player, fieldWidth, fieldHeight)
                }
            }
        })

//        val view = inflater.inflate(R.layout.fragment_soccer, container, false)
//        val soccerField = view.findViewById<FrameLayout>(R.id.soccerFieldLayout)
//
//        // 리스너를 명시적으로 생성
//        val layoutListener = object : ViewTreeObserver.OnGlobalLayoutListener {
//            override fun onGlobalLayout() {
//                // 뷰의 크기가 결정된 후 실행
//                soccerField.viewTreeObserver.removeOnGlobalLayoutListener(this) // 리스너 제거
//
//                // 4-3-3 포메이션 좌표 설정 (x, y 비율)
//                val positions = arrayOf(
//                    // 수비 라인 (4명)
//                    Pair(0.1f, 0.8f), Pair(0.3f, 0.8f), Pair(0.7f, 0.8f), Pair(0.9f, 0.8f),
//                    // 미드필더 라인 (3명)
//                    Pair(0.2f, 0.5f), Pair(0.5f, 0.5f), Pair(0.8f, 0.5f),
//                    // 공격 라인 (3명)
//                    Pair(0.2f, 0.2f), Pair(0.5f, 0.2f), Pair(0.8f, 0.2f),
//                    // 골키퍼 (1명)
//                    Pair(0.5f, 0.9f)
//                )
//
//                val fieldWidth = soccerField.width
//                val fieldHeight = soccerField.height
//
//                for (i in positions.indices) {
//                    val player = ImageView(context)
//                    player.layoutParams = FrameLayout.LayoutParams(100, 100).apply {
//                        leftMargin = (positions[i].first * fieldWidth).toInt() - 50 // 중심 조정을 위해 -50
//                        topMargin = (positions[i].second * fieldHeight).toInt() - 50 // 중심 조정을 위해 -50
//                    }
//                    player.setImageResource(R.drawable.player_shape) // 원 모양 배경 이미지
//                    soccerField.addView(player)
//                }
//            }
//        }

        // 리스너 등록
//        soccerField.viewTreeObserver.addOnGlobalLayoutListener(layoutListener)

//        return view
        return binding.root
    }


    private fun addPlayerToField(player: Player, fieldWidth: Int, fieldHeight: Int) {
        val playerView = ImageView(requireContext())
        val x = (player.x * fieldWidth).toInt()
        val y = (player.y * fieldHeight).toInt()

        playerView.layoutParams = FrameLayout.LayoutParams(100, 100).apply {
            leftMargin = x - 50 // 중심 조정을 위해 -50
            topMargin = y - 50 // 중심 조정을 위해 -50
        }
        playerView.setImageResource(R.drawable.player_shape) // 플레이어 이미지 설정
        binding.soccerFieldLayout.addView(playerView)
    }
}