package platform

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class PlatformManager(
        override val coroutineContext: CoroutineContext = Dispatchers.IO + Job()
) : CoroutineScope {
    private val platforms: MutableList<Platform> = mutableListOf()

    fun initialize(vararg platform: Platform) {
        platform.forEach {
            platforms.add(it)
            launch { it.initialize() }
        }
    }

    fun destroy(vararg platform: Platform) {
        platform.forEach {
            launch {
                it.destroy()
                platforms.remove(it)
            }
        }
    }

    fun destroy() {
        destroy(*platforms.toTypedArray())
    }
}