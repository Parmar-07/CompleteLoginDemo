package com.example.loginneostore.base

interface DataMapper<FROM, TO> {
    fun mapper(from: FROM): TO
}


interface ResponseMapper<T> {
    fun mapperSuccess(t:T): T
    fun mapperFailure(): Throwable
}


interface ResponseDataMapper<T> {
    fun mapper(t : T): ResponseMapper<T>
}



