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
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.sogya.domain.utils.Constants
import ru.sogya.projects.domovoy.R

class DeleteServerDialogFragment(
    private val selectDialogFragmentListener: DeleteDialogFragmentListener
) : DialogFragment() {
    interface DeleteDialogFragmentListener {
        fun onClickDelete(serverID: String?)
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
        val titleTV: TextView = view.findViewById(R.id.dialogTittle)
        val textTV: TextView = view.findViewById(R.id.dialogText)
        titleTV.text = getString(R.string.dialog_tittle_remove_server)
        textTV.text = getString(R.string.dialog_server_remove_text)
        positiveButton.text = getString(R.string.dialog_positive_remove)
        positiveButton.setOnClickListener {
            val entityId = arguments?.getString(Constants.SERVER_URI)
            selectDialogFragmentListener.onClickDelete(entityId)
            dismiss()
        }
        negativeButton.setOnClickListener {
            dismiss()
        }
    }
}