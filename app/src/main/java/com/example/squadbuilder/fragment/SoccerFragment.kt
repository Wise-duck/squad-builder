package com.example.squadbuilder.fragment

import android.content.ClipData
import android.os.Bundle
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.squadbuilder.R
import com.example.squadbuilder.data.Player
import com.example.squadbuilder.databinding.FragmentSoccerBinding
import com.example.squadbuilder.databinding.PlayerViewBinding
import com.example.squadbuilder.viewmodel.PlayerViewModel

class SoccerFragment : Fragment() {

    private lateinit var fragmentBinding: FragmentSoccerBinding
    private val playerViewModel: PlayerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentBinding = FragmentSoccerBinding.inflate(inflater, container, false)

        fragmentBinding.soccerFieldLayout.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                fragmentBinding.soccerFieldLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)

                val fieldWidth = fragmentBinding.soccerFieldLayout.width
                val fieldHeight = fragmentBinding.soccerFieldLayout.height

                // ViewModel을 사용하여 플레이어 리스트를 관찰
                playerViewModel.players.observe(viewLifecycleOwner, Observer { players ->
                    if (!players.isNullOrEmpty()) {
                        fragmentBinding.soccerFieldLayout.removeAllViews()  // 기존 플레이어 제거

                        players.forEach { player ->
                            addPlayerToField(player, fieldWidth, fieldHeight)
                        }
                    }
                })
            }
        })

        // 드래그 이벤트를 처리하기 위한 리스너 설정
        fragmentBinding.soccerFieldLayout.setOnDragListener { v, event ->
            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> true
                DragEvent.ACTION_DRAG_LOCATION -> {
                    val view = event.localState as View
                    view.x = event.x - view.width / 2
                    view.y = event.y - view.height / 2
                    view.invalidate()
                    true
                }
                DragEvent.ACTION_DROP -> {
                    val view = event.localState as View
                    view.x = event.x - view.width / 2
                    view.y = event.y - view.height / 2
                    view.invalidate()
                    true
                }
                DragEvent.ACTION_DRAG_ENDED -> true
                else -> false
            }
        }

        return fragmentBinding.root
    }

    private fun addPlayerToField(player: Player, fieldWidth: Int, fieldHeight: Int) {
        val inflater = LayoutInflater.from(context)
        val playerBinding: PlayerViewBinding = DataBindingUtil.inflate(inflater, R.layout.player_view, fragmentBinding.soccerFieldLayout, false)

        // 바인딩 객체에 플레이어 데이터 설정
        playerBinding.player = player

        // 실제 좌표 값 계산
        val x = (player.x * fieldWidth).toInt()
        val y = (player.y * fieldHeight).toInt()

        playerBinding.root.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT).apply {
            leftMargin = x - (playerBinding.root.measuredWidth / 2)
            topMargin = y - (playerBinding.root.measuredHeight / 2)
        }

        // 드래그 앤 드롭 설정
        playerBinding.root.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    val clipData = ClipData.newPlainText("", "")
                    val shadowBuilder = View.DragShadowBuilder(v)
                    v.startDragAndDrop(clipData, shadowBuilder, v, 0)
                    v.performClick() // performClick 호출하여 클릭 이벤트 처리
                    true
                }
                else -> false
            }
        }

        // playerView를 필드에 추가
        fragmentBinding.soccerFieldLayout.addView(playerBinding.root)
    }
}
