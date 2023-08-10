package ru.sogya.projects.domovoy.dialogs.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.sogya.domain.utils.MyCallBack
import dagger.hilt.android.AndroidEntryPoint
import ru.sogya.projects.domovoy.databinding.DialogAddGroupBinding

@AndroidEntryPoint
class GroupBottomSheetFragment : DialogFragment() {
    private lateinit var binding: DialogAddGroupBinding
    private val vm: GroupDialogVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DialogAddGroupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button.setOnClickListener {
            val groupTag = binding.groupTag.text.toString()
            val groupDesc = binding.groupDesc.text.toString()
            if (groupTag.isNotEmpty()) {
                vm.createNewGroup(groupTag, groupDesc, object : MyCallBack<Boolean> {
                    override fun data(t: Boolean) {
                        Toast.makeText(context, "Group added", Toast.LENGTH_SHORT).show()
                    }

                    override fun error() {}

                })
            }
        }
    }

}