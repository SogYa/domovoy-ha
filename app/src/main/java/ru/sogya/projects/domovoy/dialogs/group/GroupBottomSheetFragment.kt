package ru.sogya.projects.domovoy.dialogs.group

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.sogya.domain.utils.MyCallBack
import dagger.hilt.android.AndroidEntryPoint
import ru.sogya.projects.domovoy.R

@AndroidEntryPoint
class GroupBottomSheetFragment : DialogFragment() {
    private val vm: GroupDialogVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        if (dialog != null && dialog?.window != null)
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        return inflater.inflate(R.layout.dialog_add_group, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val positiveButton: Button = view.findViewById(R.id.positive)
        val negativeButton: Button = view.findViewById(R.id.negative)
        val groupTag: TextView = view.findViewById(R.id.groupTag)
        val groupDesc: TextView = view.findViewById(R.id.groupDesc)
        positiveButton.setOnClickListener {
            val groupTagText = groupTag.text.toString()
            val groupDescText = groupDesc.text.toString()
            if (groupTagText.isNotEmpty()) {
                vm.createNewGroup(groupTagText, groupDescText, object : MyCallBack<Boolean> {
                    override fun data(t: Boolean) {
                        Toast.makeText(context, "Group added", Toast.LENGTH_SHORT).show()
                        dismiss()
                    }

                    override fun error() {}

                })
            }
            negativeButton.setOnClickListener {
               dialog?.dismiss()
            }
        }
    }

}