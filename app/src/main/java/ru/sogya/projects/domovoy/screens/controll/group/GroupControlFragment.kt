package ru.sogya.projects.domovoy.screens.controll.group

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import ru.sogya.projects.domovoy.databinding.DialogAddGroupBinding
import ru.sogya.projects.domovoy.databinding.FragmentGroupControllBinding
import ru.sogya.projects.domovoy.dialogs.group.GroupBottomSheetFragment

@AndroidEntryPoint
class GroupControlFragment : Fragment() {
    private var _binding: FragmentGroupControllBinding? = null
    private val binding get() = _binding!!
    private val vm: GroupControlVM by viewModels()
    private var adapter: GroupControlAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGroupControllBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            adapter = GroupControlAdapter()
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            addButton.setOnClickListener {
                val dialog = GroupBottomSheetFragment()
                dialog.show(parentFragmentManager, dialog.tag)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        vm.getGroupsLiveData().observe(viewLifecycleOwner) {
            binding.apply {
                adapter?.updateGroupList(it)
                Log.d("LiveDataGroup", it.toString())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}