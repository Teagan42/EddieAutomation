package data.remote.isy

import com.universaldevices.device.model.UDNode
import platform.models.AuthenticationResult
import platform.models.Credentials

interface ISYClient {
    suspend fun connect(credentials: Credentials.Passsword) : AuthenticationResult

    fun addAllNodes()

    fun onNodeAdded(node: UDNode)
}