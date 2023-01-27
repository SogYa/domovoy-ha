package ru.sogya.projects.smartrevolutionapp.screens.ticket.ticket

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sogya.domain.models.TicketDomain
import com.sogya.domain.usecases.firebase.ticket.ReadTicketByIdUseCase
import com.sogya.domain.utils.MyCallBack
import ru.sogya.projects.smartrevolutionapp.app.App
import ru.sogya.projects.smartrevolutionapp.utils.VisibilityStates

class TicketVM : ViewModel() {
    private val repository = App.getFirebase()
    private val getTicketUseCase = ReadTicketByIdUseCase(repository)
    private val loadingLiveData = MutableLiveData<Int>()
    private val ticketLiveData = MutableLiveData<TicketDomain>()

    fun getTicket(ticketId: String, myCallBack: MyCallBack<Boolean>) {
        loadingLiveData.value = VisibilityStates.VISIBLE.visibility
        getTicketUseCase.invoke(ticketId, object : MyCallBack<TicketDomain> {
            override fun data(t: TicketDomain) {
                super.data(t)
                loadingLiveData.postValue(VisibilityStates.GONE.visibility)
                ticketLiveData.postValue(t)
            }

            override fun error(error: String) {
                super.error(error)
                myCallBack.error(error)
            }
        })
    }

    fun getTicketLiveData() = ticketLiveData
    fun getLoadingLiveData() = loadingLiveData
}