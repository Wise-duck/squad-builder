package com.example.squadbuilder.fragment

import android.content.ClipData
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.view.*
import android.widget.EditText
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updateLayoutParams
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.squadbuilder.R
import com.example.squadbuilder.data.Player
import com.example.squadbuilder.databinding.FragmentSoccerBinding
import com.example.squadbuilder.databinding.PlayerViewBinding
import com.example.squadbuilder.viewmodel.PlayerViewModel
import io.github.muddz.styleabletoast.StyleableToast

class SoccerFragment : Fragment() {

    private lateinit var fragmentBinding: FragmentSoccerBinding
    private val playerViewModel: PlayerViewModel by viewModels()

    private val PICK_IMAGE_REQUEST = 1
    private var selectedImageUri: Uri? = null

    // 드래그 중인 View와 그 상태를 관리하는 변수
    private var currentDraggingView: View? = null
    private var isDragging = false

    // 클릭과 드래그 판단 기준
    companion object {
        private const val DRAG_THRESHOLD = 10f
        private const val CLICK_THRESHOLD = 200L
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentBinding = FragmentSoccerBinding.inflate(inflater, container, false)
        fragmentBinding.viewModel = playerViewModel
        observeViewModel()

        fragmentBinding.saveButton.setOnClickListener { showSaveDialog() }
        fragmentBinding.teamProfileImage.setOnClickListener { selectTeamPhoto() }
        fragmentBinding.soccerFieldLayout.setOnDragListener { v, event -> handleDragEvent(v, event) }

        return fragmentBinding.root
    }

    private fun observeViewModel() {
        fragmentBinding.soccerFieldLayout.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                fragmentBinding.soccerFieldLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val fieldWidth = fragmentBinding.soccerFieldLayout.width
                val fieldHeight = fragmentBinding.soccerFieldLayout.height

                playerViewModel.players.observe(viewLifecycleOwner, Observer { players ->
                    if (!players.isNullOrEmpty() && currentDraggingView == null) {
                        fragmentBinding.soccerFieldLayout.removeAllViews()
                        players.forEach { player -> addPlayerToField(player, fieldWidth, fieldHeight) }
                    }
                })
            }
        })
    }
    // 팀 사진 선택 버튼 클릭 시 호출
    private fun selectTeamPhoto() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    // 사진 선택 결과 처리
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == AppCompatActivity.RESULT_OK) {
            selectedImageUri = data?.data
            fragmentBinding.teamProfileImage.setImageURI(selectedImageUri)
        }
    }
    private fun handleDragEvent(v: View, event: DragEvent): Boolean {
        val view = event.localState as? View ?: return false

        when (event.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                isDragging = true
                currentDraggingView = view
            }
            DragEvent.ACTION_DRAG_LOCATION -> {
                moveViewDuringDrag(view, event, v)
            }
            DragEvent.ACTION_DROP -> {
                updateViewPositionAfterDrop(view, event, v)
            }
            DragEvent.ACTION_DRAG_ENDED -> {
                resetDragState()
            }
        }
        return true
    }

    private fun moveViewDuringDrag(view: View, event: DragEvent, container: View) {
        if (currentDraggingView == view) {
            view.x = (event.x - view.width / 2).coerceIn(0f, (container.width - view.width).toFloat())
            view.y = (event.y - view.height / 2).coerceIn(0f, (container.height - view.height).toFloat())
            view.invalidate()
        }
    }

    private fun updateViewPositionAfterDrop(view: View, event: DragEvent, container: View) {
        if (currentDraggingView == view) {
            val xOffset = view.width / 2
            val yOffset = view.height / 2

            val maxX = container.width - view.width
            val maxY = container.height - view.height
            val newX = (event.x - xOffset).coerceIn(0f, maxX.toFloat())
            val newY = (event.y - yOffset).coerceIn(0f, maxY.toFloat())

            view.x = newX
            view.y = newY
            view.invalidate()

            updatePlayerPosition(view, newX, newY, container)
        }
        resetDragState()
    }

    private fun resetDragState() {
        isDragging = false
        currentDraggingView = null
    }

    private fun updatePlayerPosition(view: View, newX: Float, newY: Float, container: View) {
        val player = view.tag as? Player ?: return
        val relativeX = (newX + view.width / 2) / container.width
        val relativeY = (newY + view.height / 2) / container.height
        playerViewModel.updatePlayerPosition(player, relativeX, relativeY)
    }

    private fun addPlayerToField(player: Player, fieldWidth: Int, fieldHeight: Int) {
        val inflater = LayoutInflater.from(context)
        val playerBinding: PlayerViewBinding = DataBindingUtil.inflate(inflater, R.layout.player_view, fragmentBinding.soccerFieldLayout, false)

        playerBinding.player = player
        playerBinding.root.tag = player

        setPositionOnLayout(player, playerBinding, fieldWidth, fieldHeight)
        setupTouchListener(playerBinding.root, player)

        fragmentBinding.soccerFieldLayout.addView(playerBinding.root)
    }

    private fun setPositionOnLayout(player: Player, playerBinding: PlayerViewBinding, fieldWidth: Int, fieldHeight: Int) {
        playerBinding.root.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                playerBinding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
                if (playerBinding.root.left == 0 && playerBinding.root.top == 0) {
                    val x = (player.x * fieldWidth).toInt() - (playerBinding.root.width / 2)
                    val y = (player.y * fieldHeight).toInt() - (playerBinding.root.height / 2)
                    playerBinding.root.updateLayoutParams<FrameLayout.LayoutParams> {
                        leftMargin = x
                        topMargin = y
                    }
                }
            }
        })
    }

    private fun setupTouchListener(view: View, player: Player) {
        var initialX = 0f
        var initialY = 0f
        var startTime = 0L

        view.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    initialX = event.x
                    initialY = event.y
                    startTime = System.currentTimeMillis()
                    true
                }
                MotionEvent.ACTION_MOVE -> handleMoveEvent(v, event, initialX, initialY)
                MotionEvent.ACTION_UP -> handleUpEvent(v, event, initialX, initialY, startTime, player)
                else -> false
            }
        }
    }

    private fun handleMoveEvent(v: View, event: MotionEvent, initialX: Float, initialY: Float): Boolean {
        val deltaX = Math.abs(event.x - initialX)
        val deltaY = Math.abs(event.y - initialY)
        if (deltaX > DRAG_THRESHOLD || deltaY > DRAG_THRESHOLD) {
            val clipData = ClipData.newPlainText("", "")
            val shadowBuilder = View.DragShadowBuilder(v)
            v.startDragAndDrop(clipData, shadowBuilder, v, 0)
            return true
        }
        return false
    }

    private fun handleUpEvent(v: View, event: MotionEvent, initialX: Float, initialY: Float, startTime: Long, player: Player): Boolean {
        val duration = System.currentTimeMillis() - startTime
        val deltaX = Math.abs(event.x - initialX)
        val deltaY = Math.abs(event.y - initialY)
        if (deltaX < DRAG_THRESHOLD && deltaY < DRAG_THRESHOLD && duration < CLICK_THRESHOLD) {
            showPlayerEditDialog(player)
        }
        return true
    }
    private fun showPlayerEditDialog(player: Player) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_player_edit, null)
        val playerNameEditText = dialogView.findViewById<EditText>(R.id.playerNameEditText)
        val playerNumberEditText = dialogView.findViewById<EditText>(R.id.playerNumberEditText)
        val playerPositionEditText = dialogView.findViewById<EditText>(R.id.playerPositionEditText)

        playerNameEditText.setText(player.name)
        playerNumberEditText.setText(player.number.toString())
        playerPositionEditText.setText(player.position)

        AlertDialog.Builder(requireContext())
            .setTitle("플레이어 수정")
            .setView(dialogView)
            .setPositiveButton("저장") { dialog, _ ->
                updatePlayerDetails(player, playerNameEditText.text.toString(), playerNumberEditText.text.toString().toIntOrNull() ?: player.number, playerPositionEditText.text.toString())
                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ -> dialog.cancel() }
            .show()
    }

    private fun updatePlayerDetails(player: Player, newName: String, newNumber: Int, newPosition: String) {
        playerViewModel.updatePlayerDetails(player, newName, newNumber, newPosition)
        StyleableToast.makeText(requireContext(), "플레이어 정보가 업데이트되었습니다.", R.style.saveToast).show()
    }

    private fun showSaveDialog() {
        val input = EditText(requireContext()).apply { inputType = InputType.TYPE_CLASS_TEXT }
        AlertDialog.Builder(requireContext())
            .setTitle("포메이션 저장")
            .setView(input)
            .setPositiveButton("저장") { dialog, _ ->
                // 팀 이름과 팀 사진 URI를 매개 변수로 전달
                playerViewModel.saveFormation(input.text.toString(), selectedImageUri.toString())
                dialog.dismiss()
                StyleableToast.makeText(requireContext(), "포메이션 '${input.text}'이 저장되었습니다.", R.style.saveToast).show()
            }
            .setNegativeButton("취소") { dialog, _ -> dialog.cancel() }
            .show()
    }
}
