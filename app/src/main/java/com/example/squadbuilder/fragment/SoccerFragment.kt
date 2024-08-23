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

    private val PICK_IMAGE_REQUEST2 = 2
    private var selectedPlayerImageUri: Uri? = null
    private var dialogView: View? = null  // 전역 변수로 변경하여 다이얼로그 뷰 참조 유지

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
        setupListeners()

        return fragmentBinding.root
    }

    private fun setupListeners() {
        fragmentBinding.saveButton.setOnClickListener { showSaveDialog() }
        fragmentBinding.teamProfileImage.setOnClickListener { selectTeamPhoto() }
        fragmentBinding.settingsButton.setOnClickListener { showResetConfirmationDialog() }
        fragmentBinding.soccerFieldLayout.setOnDragListener { v, event -> handleDragEvent(v, event) }
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

                // 팀 프로필 이미지 URI가 변경될 때 이미지를 초기화
                playerViewModel.teamProfileImageUri.observe(viewLifecycleOwner, Observer { uri ->
                    if (uri == null) {
                        fragmentBinding.teamProfileImage.setImageResource(R.drawable.icon_logo) // 기본 이미지로 설정
                    } else {
                        fragmentBinding.teamProfileImage.setImageURI(Uri.parse(uri))
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
        if (resultCode == AppCompatActivity.RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE_REQUEST -> {
                    selectedImageUri = data?.data
                    fragmentBinding.teamProfileImage.setImageURI(selectedImageUri)
                }
                PICK_IMAGE_REQUEST2 -> {
                    selectedPlayerImageUri = data?.data
                    dialogView?.findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.player_profile_image)?.setImageURI(selectedPlayerImageUri)
                }
            }
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
        dialogView = layoutInflater.inflate(R.layout.dialog_player_edit, null)  // 뷰를 전역 변수에 할당

        val playerNameEditText = dialogView!!.findViewById<EditText>(R.id.playerNameEditText)
        val playerNumberEditText = dialogView!!.findViewById<EditText>(R.id.playerNumberEditText)
        val playerPositionEditText = dialogView!!.findViewById<EditText>(R.id.playerPositionEditText)
        val playerProfileImageView = dialogView!!.findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.player_profile_image)

        playerNameEditText.setText(player.name)
        playerNumberEditText.setText(player.number.toString())
        playerPositionEditText.setText(player.position)

        // 기존 이미지가 있으면 로드, 없으면 기본 이미지 사용
        if (player.photoUri.isNullOrEmpty()) {
            playerProfileImageView.setImageResource(R.drawable.icon_q) // 기본 이미지 설정
        } else {
            playerProfileImageView.setImageURI(Uri.parse(player.photoUri))
        }


        // 이미지 선택 리스너 추가
        playerProfileImageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST2)
        }

        AlertDialog.Builder(requireContext())
            .setTitle("플레이어 정보 수정")
            .setView(dialogView)
            .setPositiveButton("저장") { dialog, _ ->
                val newName = playerNameEditText.text.toString()
                val newNumber = playerNumberEditText.text.toString().toIntOrNull() ?: player.number
                val newPosition = playerPositionEditText.text.toString()
                val newPhotoUri = selectedPlayerImageUri?.toString() ?: player.photoUri

                // ViewModel을 통해 플레이어 정보 업데이트
                updatePlayerDetails(player, newName, newNumber, newPosition, newPhotoUri)
                dialog.dismiss()
                selectedPlayerImageUri = null  // 다이얼로그가 닫힐 때 URI 초기화
                dialogView = null  // 다이얼로그가 닫힐 때 뷰 참조 해제
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.cancel()
                selectedPlayerImageUri = null  // 다이얼로그가 닫힐 때 URI 초기화
                dialogView = null  // 다이얼로그가 취소될 때 뷰 참조 해제
            }
            .show()
    }

    private fun updatePlayerDetails(player: Player, newName: String, newNumber: Int, newPosition: String, newPhotoUri: String?) {
        playerViewModel.updatePlayerDetails(player, newName, newNumber, newPosition, newPhotoUri)
        StyleableToast.makeText(requireContext(), "플레이어 정보 수정 완료", R.style.saveToast).show()
    }

    // 초기화 버튼 클릭 시 다이얼로그를 표시하는 함수
    private fun showResetConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("포메이션 초기화")
            .setMessage("정말로 포메이션을 초기화하시겠습니까? 이 작업은 되돌릴 수 없습니다.")
            .setPositiveButton("확인") { dialog, _ ->
                playerViewModel.resetFormation()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
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
