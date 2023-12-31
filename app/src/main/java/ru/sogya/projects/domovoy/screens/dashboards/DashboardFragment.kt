package ru.sogya.projects.domovoy.screens.dashboards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sogya.domain.utils.Constants
import com.sogya.domain.utils.Constants.STATE_ID
import dagger.hilt.android.AndroidEntryPoint
import ru.sogya.projects.domovoy.R
import ru.sogya.projects.domovoy.databinding.FragmentDashboardBinding
import ru.sogya.projects.domovoy.dialogs.DeleteItemDialogFragment
import ru.sogya.projects.domovoy.models.StatePresentation
import ru.sogya.projects.domovoy.screens.states.player.MediaPlayerFragment
import ru.sogya.projects.domovoy.screens.states.sensor.SensorFragment

@AndroidEntryPoint
class DashboardFragment : Fragment(R.layout.fragment_dashboard),
    DashboardAdapter.OnStateClickListener,
    DeleteItemDialogFragment.DialogFragmentListener {
    private lateinit var binding: FragmentDashboardBinding
    private val vm: DashboardVM by viewModels()
    private lateinit var adapter: DashboardAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.takeIf { it.containsKey(Constants.GROUP_ID) }?.apply {
            val groupId = getInt(Constants.GROUP_ID)
            vm.getGroupStates(groupId)
        }
        binding.statesRecyclerView.layoutManager = LinearLayoutManager(context)
        adapter = DashboardAdapter(this)
        binding.statesRecyclerView.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        vm.getItemsLiveDat().observe(viewLifecycleOwner) {
            adapter.updateStatesList(it)
            binding.loadingView.visibility = GONE
            if (it.isEmpty()) {
                binding.dashboardHint.visibility = VISIBLE
            } else {
                binding.dashboardHint.visibility = GONE
            }
        }
    }

    override fun onClick(statePresentation: StatePresentation) {
        val dialogFragment: BottomSheetDialogFragment
        val arguments = Bundle()
        arguments.putString(STATE_ID, statePresentation.entityId)
        if (statePresentation.entityId.startsWith("sensor")) {
            dialogFragment = SensorFragment()
            showDialog(dialogFragment, arguments)

        } else if (statePresentation.entityId.startsWith("media_player")) {
            dialogFragment = MediaPlayerFragment()
            showDialog(dialogFragment, arguments)
        }

    }

    private fun showDialog(dialogFragment: BottomSheetDialogFragment, argument: Bundle) {
        dialogFragment.arguments = argument
        dialogFragment.show(childFragmentManager, dialogFragment.tag)
    }

    override fun onLongClick(statePresentation: StatePresentation) {
        val dialog = DeleteItemDialogFragment(this)
        val argument = Bundle()
        argument.putString(STATE_ID, statePresentation.entityId)
        dialog.arguments = argument
        dialog.show(childFragmentManager, dialog.tag)
    }

    override fun positiveButtonClicked(stateId: String) {
        vm.deleteState(stateId)
    }

    override fun onSwitchStateChanged(stateId: String, switchState: String) {
        vm.callSwitchService(stateId, switchState)
    }

    override fun onClickWithCommand(stateId: String, command: String) {
        vm.callMediaPLayerService(stateId, command)
    }

    override fun onSliderChangeValue(stateId: String, value: Int) {
        vm.changeCoverValue(stateId, value)
    }
}