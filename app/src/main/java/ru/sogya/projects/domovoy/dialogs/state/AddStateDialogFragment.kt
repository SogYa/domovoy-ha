package ru.sogya.projects.domovoy.dialogs.state

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.AdapterView.OnItemClickListener
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import dagger.hilt.android.AndroidEntryPoint
import ru.sogya.projects.domovoy.R
import ru.sogya.projects.domovoy.models.StatePresenation
import ru.sogya.projects.domovoy.screens.controll.state.StateAdapter


@AndroidEntryPoint
class AddStateDialogFragment(private val dialogFragmentListener: DialogFragmentListener) :
    DialogFragment() {
    private val vm: AddStateDialogVM by viewModels()
    private var groupdDropDownList: MaterialAutoCompleteTextView? = null

    interface DialogFragmentListener {
        fun getStateHasSet(): HashSet<StatePresenation>
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.dialog_add_state, container, false)
    }

    override fun onResume() {
        super.onResume()
        val window: Window? = dialog?.window
        if (window != null) {
            val lp = WindowManager.LayoutParams()
            lp.copyFrom(window.attributes)
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            lp.height = WindowManager.LayoutParams.MATCH_PARENT
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window.attributes = lp
        }
    }

    override fun onStart() {
        super.onStart()
        vm.getGroupsLiveData().observe(viewLifecycleOwner) {
            groupdDropDownList?.setSimpleItems(it)

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val positiveButton: Button = view.findViewById(R.id.positive)
        val negativeButton: Button = view.findViewById(R.id.negative)
        groupdDropDownList =
            view.findViewById(R.id.groupDropDownList)
        val addStateRecyclerView: RecyclerView = view.findViewById(R.id.stateAddingDialogRecycler)
        val stateHashSet = dialogFragmentListener.getStateHasSet()
        val adapter = StateAdapter()
        addStateRecyclerView.adapter = adapter
        addStateRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter.updateStatesList(stateHashSet.toList())
        groupdDropDownList?.onItemClickListener =
            OnItemClickListener { _, _, id, _ -> vm.setSelectedGroup(id) }
        positiveButton.setOnClickListener {
            vm.addStatesToDataBase(stateHashSet) {
                if (it) {
                    Toast.makeText(context, "Устройства добавлены", Toast.LENGTH_SHORT).show()
                    dismiss()
                } else {
                    Toast.makeText(context, "Группа не выбрана", Toast.LENGTH_SHORT).show()
                }
            }
        }
        negativeButton.setOnClickListener {
            dismiss()
        }
    }
}