package ru.sogya.projects.domovoy.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.sogya.domain.utils.Constants.STATE_ID
import ru.sogya.projects.domovoy.R

class DeleteItemDialogFragment(
    private val dialogFragmentListener: DialogFragmentListener
) : DialogFragment() {
    interface DialogFragmentListener {
        fun positiveButtonClicked(stateId: String)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.dialog_delete_item, container, false)
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
        positiveButton.setOnClickListener {
            val entityId = arguments?.getString(STATE_ID)
            dialogFragmentListener.positiveButtonClicked(entityId.toString())
            dismiss()
        }
        negativeButton.setOnClickListener {
            dismiss()
        }
    }
}