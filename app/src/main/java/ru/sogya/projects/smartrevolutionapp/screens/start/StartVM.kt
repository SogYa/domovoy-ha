package ru.sogya.projects.smartrevolutionapp.screens.start

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sogya.domain.utils.Constants
import ru.sogya.projects.smartrevolutionapp.R
import ru.sogya.projects.smartrevolutionapp.needtoremove.SPControl

class StartVM : ViewModel() {
    private val navigationLiveData = MutableLiveData<Int>()

    init {
        if (isAuth()) {
            if (SPControl.getInstance().getBoolPrefs(Constants.PREFS_IS_LOCKED)) {
                navigationLiveData.value = R.id.action_startFragment_to_lockFragment
            } else {
                navigationLiveData.value = R.id.action_startFragment_to_homeFragment
            }
        } else {
            navigationLiveData.value = R.id.action_startFragment_to_serversFragment
        }
    }

    fun getNavLiveData() = navigationLiveData

    private fun isAuth(): Boolean {
        return SPControl.getInstance().getStringPrefs(Constants.AUTH_TOKEN).isNotEmpty()
    }
}