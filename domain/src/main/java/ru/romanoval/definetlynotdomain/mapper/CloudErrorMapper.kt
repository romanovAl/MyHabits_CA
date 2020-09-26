package ru.romanoval.domain.mapper

import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.HttpException
import ru.romanoval.domain.model.response.ErrorModel
import ru.romanoval.domain.model.response.ErrorStatus
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class CloudErrorMapper @Inject constructor(private val gson: Gson) {

    fun mapToDomainErrorException(throwable: Throwable?) : ErrorModel {
        val errorModel : ErrorModel? = when(throwable){

            //Если throwable экземпляр HttpException, тогда пытаемся спарсить данные ошибки из тела ответа
            is HttpException -> {
                if(throwable.code() == 401){
                    ErrorModel(ErrorStatus.UNAUTHORIZED)
                }else{
                    getHttpError(throwable.response()?.errorBody())
                }
            }

            //Обрабатываем ошибку таймаута вызова апи
            is SocketTimeoutException -> {
                ErrorModel("TIME OUT!", 0, ErrorStatus.TIMEOUT)
            }

            is IOException -> {
                ErrorModel("CHECK CONNECTION!", 0, ErrorStatus.NO_CONNECTION)
            }

            is UnknownHostException -> {
                ErrorModel("CHECK CONNECTION!", 0, ErrorStatus.NO_CONNECTION)
            }

            else -> null

        }

        return errorModel!!
    }

    private fun getHttpError(body: ResponseBody?): ErrorModel{
        return try{
            val result = body?.string()
            val json = Gson().fromJson(result, JsonObject::class.java)
            ErrorModel(json.toString(), 400, ErrorStatus.BAD_RESPONSE)
        }catch (e: Throwable){
            e.printStackTrace()
            ErrorModel(ErrorStatus.NOT_DEFINED)
        }
    }

}