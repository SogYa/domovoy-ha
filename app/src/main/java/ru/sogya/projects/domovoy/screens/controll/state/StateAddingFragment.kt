package ru.sogya.projects.domovoy.screens.controll.state

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ru.sogya.projects.domovoy.R
import ru.sogya.projects.domovoy.databinding.FragmentAddStateBinding
import ru.sogya.projects.domovoy.dialogs.state.AddStateDialogFragment
import ru.sogya.projects.domovoy.models.StatePresentation

@AndroidEntryPoint
class StateAddingFragment : Fragment(R.layout.fragment_add_state) {
    private val vm: StateAddingVM by viewModels()
    private lateinit var adapter: StateAdapter
    private lateinit var binding: FragmentAddStateBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddStateBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(requireContext())
        binding.statesRecyclerView.layoutManager = layoutManager
        adapter = StateAdapter()
        binding.statesRecyclerView.adapter = adapter
        binding.editTextSearch.doOnTextChanged { inputText, _, _, _ ->
            adapter.filter.filter(inputText)
        }
        binding.addFub2.setOnClickListener {
            if (adapter.sendCheckedSet().isNotEmpty()) {
                val dialog = AddStateDialogFragment(sendHashSetToDialog())
                dialog.show(parentFragmentManager, dialog.tag)
                adapter.clearCheckedSet()
            }
        }
    }

    private fun sendHashSetToDialog(): AddStateDialogFragment.DialogFragmentListener {
        return object : AddStateDialogFragment.DialogFragmentListener {
            override fun getStateHasSet(): HashSet<StatePresentation> {
                return adapter.sendCheckedSet()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }

    override fun onStart() {
        super.onStart()
        vm.getStatesLiveData().observe(viewLifecycleOwner) {
            adapter.updateStatesList(it)
            Log.d("List", it.toString())
        }
        vm.getLoadingLiveData().observe(viewLifecycleOwner) {
            binding.loadingView.visibility = it
        }
    }

    override fun onStop() {
        super.onStop()
        adapter.clearCheckedSet()
    }
}