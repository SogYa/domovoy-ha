package com.sogya.domain.usecases.databaseusecase.servers

import com.sogya.domain.models.ServerStateDomain
import com.sogya.domain.repository.LocalDataBaseRepository

class UpdateServerUseCase(
    private val repository: LocalDataBaseRepository
) {
    fun invoke(serverStateDomain: ServerStateDomain) = repository.updateServer(serverStateDomain)
}