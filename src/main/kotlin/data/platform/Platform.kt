package data.platform

interface Platform {
    val name: String
    val type: PlatformType

    suspend fun initialize()
}

enum class PlatformType {
    POLL,
    PUSH
}