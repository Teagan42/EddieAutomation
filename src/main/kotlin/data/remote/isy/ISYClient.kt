package data.remote.isy

import com.universaldevices.device.model.UDNode
import data.platform.models.AuthenticationResult
import data.platform.models.Credentials

interface ISYClient {
    suspend fun connect(credentials: Credentials.Passsword) : AuthenticationResult

    fun addAllNodes()

    fun onNodeAdded(node: UDNode)
}