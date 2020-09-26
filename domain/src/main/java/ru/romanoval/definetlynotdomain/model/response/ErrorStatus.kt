package ru.romanoval.domain.model.response

enum class ErrorStatus {

    //Что-то не так с запросом
    BAD_REQUEST,

    //Не был передан ключ
    UNAUTHORIZED,

    //Что-то с api
    INTERNAL_SERVER_ERROR,

    //Нет соединения
    NO_CONNECTION,

    //Когда у ждуна не вышло
    TIMEOUT,

    //Не получилось получить значение
    BAD_RESPONSE,

    //Неожиданная ошибка
    NOT_DEFINED,

}