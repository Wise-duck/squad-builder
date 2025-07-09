package com.wiseduck.squadbuilder.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.wiseduck.squadbuilder.FormationDetailActivity
import com.wiseduck.squadbuilder.R
import com.wiseduck.squadbuilder.adapter.FormationAdapter
import com.wiseduck.squadbuilder.data.Formation
import com.wiseduck.squadbuilder.databinding.FragmentListBinding
import com.wiseduck.squadbuilder.viewmodel.PlayerViewModel


class ListFragment : Fragment() {

    private lateinit var binding: FragmentListBinding
    private val playerViewModel: PlayerViewModel by viewModels()
    private lateinit var formationAdapter: FormationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // 데이터 바인딩 초기화
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false)
        binding.lifecycleOwner = viewLifecycleOwner // LiveData를 사용할 때 필요

        // ViewModel 설정
        binding.viewModel = playerViewModel

        // RecyclerView 설정
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        // 어댑터 초기화 및 설정
        playerViewModel.formationsWithPlayers.observe(viewLifecycleOwner, Observer { formations ->
            formations?.let {
                formationAdapter = FormationAdapter(it.map { it.formation },
                    clickListener = { formation ->
                        // 포메이션 클릭 시 인텐트로 FormationDetailActivity에 포메이션 ID 전달
                        val intent = Intent(context, FormationDetailActivity::class.java).apply {
                            putExtra("FORMATION_ID", formation.id)
                        }
                        startActivity(intent)
                    },
                    deleteListener = { formation ->
                        // 삭제 버튼 클릭 시 ViewModel에서 포메이션 삭제
                        showDeleteConfirmationDialog(formation)
                    }
                )
                binding.recyclerView.adapter = formationAdapter
            }
        })

        return binding.root
    }

    private fun showDeleteConfirmationDialog(formation: Formation) {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Confirm deletion")
            .setMessage("Are you sure you want to delete it? This action is irreversible.")
            .setPositiveButton("Delete") { dialog, _ ->
                // 사용자가 "삭제"를 선택했을 때 삭제를 실행합니다.
                playerViewModel.deleteFormation(formation)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                // 사용자가 "취소"를 선택했을 때 다이얼로그를 닫습니다.
                dialog.dismiss()
            }
            .create()

        // 다이얼로그의 배경을 둥근 모서리로 설정
        dialog.window?.setBackgroundDrawableResource(R.drawable.rounded_dialog_background)

        dialog.show()
    }

}
