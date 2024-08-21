package com.example.squadbuilder.fragment

import android.content.ClipData
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.EditText
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
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

    // 드래그 중인 View와 그 상태를 관리하는 변수
    private var currentDraggingView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentBinding = FragmentSoccerBinding.inflate(inflater, container, false)
        fragmentBinding.viewModel = playerViewModel // ViewModel을 Data Binding에 연결
        fragmentBinding.soccerFieldLayout.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                fragmentBinding.soccerFieldLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)

                val fieldWidth = fragmentBinding.soccerFieldLayout.width
                val fieldHeight = fragmentBinding.soccerFieldLayout.height

                // ViewModel을 사용하여 플레이어 리스트를 관찰
                playerViewModel.players.observe(viewLifecycleOwner, Observer { players ->
                    if (!players.isNullOrEmpty() && currentDraggingView == null) { // 드래그 중이 아닐 때만 업데이트
                        fragmentBinding.soccerFieldLayout.removeAllViews()

                        players.forEach { player ->
                            addPlayerToField(player, fieldWidth, fieldHeight)
                        }
                    }
                })


                playerViewModel.formationsWithPlayers.observe(viewLifecycleOwner, Observer { formationsWithPlayers ->
                    formationsWithPlayers?.let {
                        // 저장된 포메이션과 플레이어 정보 확인 로그
                        Log.d("SoccerFragment", "Formations with Players: $it")
                    }
                })

            }
        })

        // 포메이션 저장 버튼 클릭 시 다이얼로그에 팀 이름 입력받아 저장
        fragmentBinding.saveButton.setOnClickListener {
            showSaveDialog()
        }

        // 드래그 이벤트를 처리하기 위한 리스너 설정
        fragmentBinding.soccerFieldLayout.setOnDragListener { v, event ->
            val view = event.localState as? View ?: return@setOnDragListener false

            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    currentDraggingView = view
                    true
                }
                DragEvent.ACTION_DRAG_LOCATION -> {
                    if (currentDraggingView == view) {
                        view.x = (event.x - view.width / 2).coerceIn(0f, (v.width - view.width).toFloat())
                        view.y = (event.y - view.height / 2).coerceIn(0f, (v.height - view.height).toFloat())
                        view.invalidate()
                    }
                    true
                }
                DragEvent.ACTION_DROP -> {
                    if (currentDraggingView == view) {
                        val xOffset = view.width / 2
                        val yOffset = view.height / 2

                        val maxX = v.width - view.width
                        val maxY = v.height - view.height
                        val newX = (event.x - xOffset).coerceIn(0f, maxX.toFloat())
                        val newY = (event.y - yOffset).coerceIn(0f, maxY.toFloat())

                        view.x = newX
                        view.y = newY
                        view.invalidate()

                        val playerTag = view.tag as? Player
                        if (playerTag != null) {
                            val fieldWidth = fragmentBinding.soccerFieldLayout.width
                            val fieldHeight = fragmentBinding.soccerFieldLayout.height

                            val relativeX = (newX + xOffset) / fieldWidth
                            val relativeY = (newY + yOffset) / fieldHeight

                            playerViewModel.updatePlayerPosition(playerTag, relativeX, relativeY)
                        }
                    }
                    currentDraggingView = null
                    true
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    currentDraggingView = null
                    true
                }
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

        // 태그 설정
        playerBinding.root.tag = player

        // 뷰 크기 측정 후 위치 설정
        playerBinding.root.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                // 기존 Listener 제거
                playerBinding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)

                if (playerBinding.root.left == 0 && playerBinding.root.top == 0) {
                    // 초기 위치만 설정, 이미 위치가 설정된 경우 위치 갱신을 방지
                    val x = (player.x * fieldWidth).toInt() - (playerBinding.root.width / 2)
                    val y = (player.y * fieldHeight).toInt() - (playerBinding.root.height / 2)

                    val layoutParams = playerBinding.root.layoutParams as FrameLayout.LayoutParams
                    layoutParams.leftMargin = x
                    layoutParams.topMargin = y
                    playerBinding.root.layoutParams = layoutParams
                }
            }
        })


        // 드래그 앤 드롭 설정
        playerBinding.root.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (currentDraggingView == null) { // 다른 드래그가 진행 중이지 않을 때만 새 드래그 시작
                        val clipData = ClipData.newPlainText("", "")
                        val shadowBuilder = View.DragShadowBuilder(v)
                        v.startDragAndDrop(clipData, shadowBuilder, v, 0)
                        v.performClick() // performClick 호출하여 클릭 이벤트 처리
                    }
                    true
                }
                else -> false
            }
        }

        // playerView를 필드에 추가
        fragmentBinding.soccerFieldLayout.addView(playerBinding.root)
    }

    // Fragment에서 다이얼로그를 사용하여 팀 이름을 입력받고 저장(추후 커스텀 다이얼로그로 변경)
    private fun showSaveDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("포메이션 저장")

        val input = EditText(requireContext())
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton("저장") { dialog, _ ->
            val teamName = input.text.toString()
            playerViewModel.saveFormation(teamName)
            dialog.dismiss()
        }
        builder.setNegativeButton("취소") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }
}
