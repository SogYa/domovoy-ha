package ru.sogya.projects.smartrevolutionapp.screens.home.bottomsheet.stateadding

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sogya.domain.models.StateDomain
import com.sogya.domain.usecases.databaseusecase.states.InsertStateUseCase
import com.sogya.domain.usecases.network.GetStatesUseCase
import com.sogya.domain.usecases.sharedpreferences.GetStringPrefsUseCase
import com.sogya.domain.utils.Constants
import com.sogya.domain.utils.MyCallBack
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import ru.sogya.projects.smartrevolutionapp.utils.VisibilityStates
import javax.inject.Inject

@HiltViewModel
class StateAddingVM @Inject constructor(
    getStatesUseCase: GetStatesUseCase,
    private val getStringPrefsUseCase: GetStringPrefsUseCase,
    private val insertStateUseCase: InsertStateUseCase,
) : ViewModel() {
    private val statesLiveData = MutableLiveData<List<StateDomain>>()
    private val loadingViewLiveData = MutableLiveData<Int>()


    init {
        val url = getStringPrefsUseCase.invoke(Constants.SERVER_URI)
        val token = getStringPrefsUseCase.invoke(Constants.AUTH_TOKEN)
        getStatesUseCase.invoke(url, token).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableSingleObserver<List<StateDomain>>() {
                override fun onSuccess(t: List<StateDomain>) {
                    statesLiveData.postValue(t)
                    loadingViewLiveData.postValue(VisibilityStates.GONE.visibility)
                }

                override fun onError(e: Throwable) {
                    Log.d("StatesError", e.message.toString())
                }

            })
    }

    fun addStatesToDataBase(
        states: HashSet<StateDomain>,
        groupId: Int,
        myCallBack: MyCallBack<Boolean>
    ) {
        val ownerId = getStringPrefsUseCase.invoke(Constants.SERVER_URI)
        val listOfStates: MutableList<StateDomain> = states.toMutableList()
        Log.d("SetVM", listOfStates.toString())
        if (listOfStates.isEmpty()) {
            myCallBack.error()
        } else {
            listOfStates.forEach {
                it.ownerId = ownerId
                it.groupId = groupId
            }
            GlobalScope.async(Dispatchers.IO) {
                Log.d("Start", "Start")
                insertStateUseCase.invoke(listOfStates)
            }
        }
    }

    fun getStatesLiveData(): LiveData<List<StateDomain>> = statesLiveData
    fun getLoadingLiveData(): LiveData<Int> = loadingViewLiveData
}
