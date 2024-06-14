package team5.backoffice.domain.exception

data class ModelNotFoundException(val modelName: String, val value: String) : RuntimeException(
    "$modelName not found with $value"
)