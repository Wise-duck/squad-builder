package com.example.squadbuilder

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.squadbuilder.data.Player
import com.example.squadbuilder.databinding.ActivityFormationDetailBinding
import com.example.squadbuilder.databinding.PlayerViewBinding
import com.example.squadbuilder.viewmodel.PlayerViewModel

class FormationDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormationDetailBinding
    private val playerViewModel: PlayerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormationDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Intent에서 포메이션 ID를 받아옴
        val formationId = intent.getIntExtra("FORMATION_ID", -1)

        if (formationId != -1) {
            // ViewModel을 통해 포메이션과 플레이어 데이터를 가져옴
            playerViewModel.getFormationWithPlayers(formationId).observe(this, Observer { formationWithPlayers ->
                formationWithPlayers?.let {
                    // 팀 이름을 텍스트뷰에 설정
                    binding.teamNameTextView.text = it.formation.teamName

                    // soccerFieldLayout의 렌더링이 완료된 후 플레이어를 배치
                    binding.soccerFieldLayout.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                        override fun onGlobalLayout() {
                            binding.soccerFieldLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)
                            setupFormation(it.players)
                        }
                    })
                }
            })
        }
    }

    private fun setupFormation(players: List<Player>) {
        val fieldWidth = binding.soccerFieldLayout.width
        val fieldHeight = binding.soccerFieldLayout.height

        binding.soccerFieldLayout.removeAllViews()

        players.forEach { player ->
            addPlayerToField(player, fieldWidth, fieldHeight)
        }
    }

    private fun addPlayerToField(player: Player, fieldWidth: Int, fieldHeight: Int) {
        val inflater = layoutInflater
        val playerBinding = PlayerViewBinding.inflate(inflater, binding.soccerFieldLayout, false)

        // 바인딩 객체에 플레이어 데이터 설정
        playerBinding.player = player

        // 실제 좌표 값 계산
        val x = (player.x * fieldWidth).toInt()
        val y = (player.y * fieldHeight).toInt()

        // 레이아웃 파라미터 설정
        playerBinding.root.layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            leftMargin = x - (playerBinding.root.measuredWidth / 2)
            topMargin = y - (playerBinding.root.measuredHeight / 2)
        }

        // 필드에 플레이어 추가
        binding.soccerFieldLayout.addView(playerBinding.root)
    }
}
