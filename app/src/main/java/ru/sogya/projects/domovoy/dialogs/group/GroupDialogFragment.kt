package ru.sogya.projects.domovoy.dialogs.group

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.sogya.domain.utils.MyCallBack
import dagger.hilt.android.AndroidEntryPoint
import ru.sogya.projects.domovoy.R

@AndroidEntryPoint
class GroupDialogFragment : DialogFragment() {
    private val vm: GroupDialogVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.dialog_add_group, container, false)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val positiveButton: Button = view.findViewById(R.id.positive)
        val negativeButton: Button = view.findViewById(R.id.negative)
        val groupTag: TextView = view.findViewById(R.id.groupTag)
        positiveButton.setOnClickListener {
            val groupTagText = groupTag.text.toString()
            if (groupTagText.isNotEmpty()) {
                vm.createNewGroup(groupTagText, object : MyCallBack<Boolean> {
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