package com.example.androidtask.domain.use_cases

import android.content.Context
import com.example.androidtask.core.Resource
import com.example.androidtask.data.dto.toImportantTimings
import com.example.androidtask.data.repository.RepositoryImp
import com.example.androidtask.domain.models.ImportantTimings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject

class GetSalatTimesUseCase @Inject constructor(
    private val repository: RepositoryImp,
) {

    operator fun invoke(
        date: String,
        address: String
    ): Flow<Resource<ImportantTimings, String>> = flow {
        emit(Resource.Loading())
        try {
            val timingsDto = repository.getSalatTimes(date , address )
            emit(Resource.Success(timingsDto.data.timings.toImportantTimings()))

        } catch (ex: HttpException) {
            emit(Resource.Error(ex.message))
        } catch (e: IOException) {
            emit(Resource.Error("Can't reach the server, please check your internet Connection and try again"))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage.toString()))
        }


    }

}