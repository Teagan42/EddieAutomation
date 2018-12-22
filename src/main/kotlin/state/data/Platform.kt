package state.data

interface Platform {
    val name: String
    val type: PlatformType
}

enum class PlatformType {
    POLL,
    PUSH
}