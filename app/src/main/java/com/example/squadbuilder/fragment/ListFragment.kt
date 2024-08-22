package com.example.squadbuilder

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
import com.example.squadbuilder.adapter.FormationAdapter
import com.example.squadbuilder.databinding.FragmentListBinding
import com.example.squadbuilder.viewmodel.PlayerViewModel


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
                        playerViewModel.deleteFormation(formation)
                    }
                )
                binding.recyclerView.adapter = formationAdapter
            }
        })

        return binding.root
    }
}
