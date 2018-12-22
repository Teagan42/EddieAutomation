package data.remote.isy

import com.universaldevices.dev.UDDevice
import com.universaldevices.device.model.UDNode
import state.data.models.AuthenticationResult
import state.data.models.Credentials

interface ISYClient {
    suspend fun connect(credentials: Credentials.Passsword) : AuthenticationResult

    fun addAllNodes()

    fun onNodeAdded(node: UDNode)
}