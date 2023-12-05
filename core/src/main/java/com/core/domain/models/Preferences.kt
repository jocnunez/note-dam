package com.core.domain.models

enum class Preferences(val clave: String) {
    // Comprueba si es la primera vez que se ejecuta la aplicación
    FIRST_RUN("first_run"),

    // Clave de la configuración para elegir el tipo de data source
    APP_CONF_MODE("configuration_data"),

    // Data Sources posibles
    CSV("CSV"),
    JSON("JSON"),
    XML("XML"),

    // Files
    JSON_FILE("categorias.json")
}
