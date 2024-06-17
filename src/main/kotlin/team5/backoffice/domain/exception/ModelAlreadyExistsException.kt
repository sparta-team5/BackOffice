package team5.backoffice.domain.exception

data class ModelAlreadyExistsException(val modelName: String, val value: String) : RuntimeException(
    "$modelName already exists with $value"
)