package dev.ivanronco.primeraappaccesodatos.dataStorageService

interface IDataSetorage <T> {
    fun importData(): T
    fun exportData(data: T)
}