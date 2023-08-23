package ru.sogya.projects.domovoy.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.sogya.domain.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import ru.sogya.projects.domovoy.R

@AndroidEntryPoint
class AddServerDialogFragment(private val dialogFragmentListener: DialogFragmentListener) : DialogFragment() {
    interface DialogFragmentListener {
        fun startAuth(bundle: Bundle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.dialog_add_server, container, false)
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
        val serverNameEditText: TextInputEditText = view.findViewById(R.id.editTextмServerName)
        val serverUriEditText: TextInputEditText = view.findViewById(R.id.editTextServerUri)
        positiveButton.setOnClickListener {
            val serverName = serverNameEditText.text.toString()
            val serverUri = serverUriEditText.text.toString()
            if (serverUri != "" && serverName != "") {
                if (serverUri.startsWith("https://")) {
                    Log.d("ServerUriString", serverUri)
                    val bundle = Bundle()
                    bundle.putString(Constants.SERVER_NAME,serverName)
                    bundle.putString(Constants.SERVER_URI,serverUri)
                    dialogFragmentListener.startAuth(bundle)
                    dismiss()
                } else {
                    serverUriEditText.error = "Некорректный адрес сервера!"
                }
            } else {
                Toast.makeText(requireContext(), "Поля не заполнены", Toast.LENGTH_SHORT).show()
            }
        }
        negativeButton.setOnClickListener {
            dismiss()
        }
    }
}