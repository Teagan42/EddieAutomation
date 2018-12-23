package data.remote.isy

import com.nanoxml.XMLElement
import com.udi.isy.jsdk.insteon.ISYInsteonClient
import com.universaldevices.client.NoDeviceException
import com.universaldevices.common.properties.UDProperty
import com.universaldevices.device.model.*
import com.universaldevices.upnp.UDProxyDevice
import data.models.ISYNodeEvent
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import common.models.AuthenticationResult
import common.models.Credentials
import common.util.ifTrueMaybe
import java.net.URI
import java.util.*
import kotlin.coroutines.CoroutineContext

@ExperimentalCoroutinesApi
class ISYRemoteClient(
        private val deviceUUID: UUID,
        private val deviceUri: URI,
        private val nodeEventChannel: Channel<ISYNodeEvent>,
        override val coroutineContext: CoroutineContext = Dispatchers.IO + Job()
) : ISYClient,
    CoroutineScope,
    ISYInsteonClient() {

    sealed class DeviceStatus {
        object Online : DeviceStatus()
        object Offline : DeviceStatus()
    }

    private lateinit var deviceStatusChannel: Channel<DeviceStatus>

    override suspend fun connect(credentials: Credentials.Passsword): AuthenticationResult =
        Channel<DeviceStatus>()
            .let { deviceStatusChannel = it }
            .run {
                start(deviceUUID.toString(),
                      deviceUri.toString()
                )
            }
            .run {
                (deviceStatusChannel.receive() == DeviceStatus.Offline).ifTrueMaybe {
                    AuthenticationResult.Failed(IllegalStateException("Device is offline"))
                }
            }
            ?: run {
                try {
                    authenticate(credentials.userName,
                                 credentials.password
                    ).let {
                        when (it) {
                            true -> AuthenticationResult.Authenticated
                            false -> AuthenticationResult.Failed(IllegalAccessException("Unable to authenticate with the device"))
                        }
                    }

                } catch (noDevice: NoDeviceException) {
                    AuthenticationResult.Failed(noDevice)
                }
            }

    override suspend fun disconnect() {
        this.stop()
    }

    override fun addAllNodes() {
        device.nodes.values.forEach { onNodeAdded(it) }
    }

    override fun onNodeAdded(node: UDNode) {
        launch {
            if (!nodeEventChannel.isClosedForSend) {
                nodeEventChannel.send(ISYNodeEvent.Added(node))
            }
        }
    }

    override fun <VALUE> sendCommand(command: ISYCommand,
                                     value: VALUE,
                                     node: UDNode
    ) {
        launch {
            when (node) {
                is UDGroup ->
                    changeGroupState(command.cmd,
                                     value.toString(),
                                     node.address
                    )
                else ->
                    changeNodeState(command.cmd,
                                    value.toString(),
                                    node.address
                    )
            }
        }
    }

    //region ISYInsteonClient

    override fun onNetworkRenamed(p0: String?) {
        // TODO
    }

    override fun onNewFolder(p0: UDFolder?) {
        // TODO
    }

    override fun onNewGroup(group: UDGroup?) {
        group?.let { onNodeAdded(group) }
    }

    override fun onFolderRemoved(p0: String?) {
        // TODO
    }

    override fun onNodeDiscoveryStopped() {
        // TODO
    }

    override fun onGroupRenamed(p0: UDGroup?) {
        // TODO
    }

    override fun onNodeMovedAsSlave(p0: UDNode?,
                                    p1: UDGroup?
    ) {
        // TODO
    }

    override fun onProgress(p0: String?,
                            p1: XMLElement?
    ) {
        // TODO
    }

    override fun onNewNode(p0: UDNode?) {
        // TODO
    }

    override fun onModelChanged(p0: UDControl?,
                                p1: Any?,
                                p2: UDNode?
    ) {
        // TODO
    }

    override fun onNodeDevicePropertyChanged(device: UDProxyDevice?,
                                             node: UDNode?,
                                             property: UDProperty<*>?
    ) {
        launch {
            if (!nodeEventChannel.isClosedForSend) {
                node?.let { nodeEventChannel.send(ISYNodeEvent.StateChanged(node)) }
            }
        }
    }

    override fun onDeviceSpecific(p0: String?,
                                  p1: String?,
                                  p2: XMLElement?
    ) {
        // TODO
    }

    override fun onNodeDevicePropertiesRefreshed(device: UDProxyDevice?,
                                                 node: UDNode?
    ) {
        launch {
            if (!nodeEventChannel.isClosedForSend) {
                node?.let { nodeEventChannel.send(ISYNodeEvent.StateChanged(node)) }
            }
        }
    }

    override fun onTriggerStatus(p0: String?,
                                 p1: XMLElement?
    ) {
        // TODO
    }

    override fun onDeviceOffLine() {
        if (!deviceStatusChannel.isClosedForReceive) {
            launch {
                deviceStatusChannel.send(DeviceStatus.Offline)
            }
        }
    }

    override fun onDeviceOnLine() {
        if (!deviceStatusChannel.isClosedForReceive) {
            launch {
                deviceStatusChannel.send(DeviceStatus.Online)
            }
        }
    }

    override fun onDiscoveringNodes() {
        // TODO
    }

    override fun onNodeIsWritingToDevice(p0: UDNode?,
                                         p1: Boolean
    ) {
        // TODO
    }

    override fun onNodeParentChanged(p0: UDNode?,
                                     p1: UDNode?
    ) {
        // TODO
    }

    override fun onSystemStatus(p0: Boolean) {
        // TODO
    }

    override fun onNodeRevised(p0: UDProxyDevice?,
                               p1: UDNode?
    ) {
        // TODO
    }

    override fun onGroupRemoved(p0: String?) {
        // TODO
    }

    override fun onInternetAccessEnabled(p0: String?) {
        // TODO
    }

    override fun onNodeError(p0: UDNode?) {
        // TODO
    }

    override fun onNodeMovedAsMaster(p0: UDNode?,
                                     p1: UDGroup?
    ) {
        // TODO
    }

    override fun onNodeHasPendingDeviceWrites(p0: UDNode?,
                                              p1: Boolean
    ) {
        // TODO
    }

    override fun onNewDeviceAnnounced(p0: UDProxyDevice?) {
        // TODO
    }

    override fun onNodeDeviceIdChanged(p0: UDProxyDevice?,
                                       p1: UDNode?
    ) {
        // TODO
    }

    override fun onNodeDevicePropertiesRefreshedComplete(p0: UDProxyDevice?) {
        // TODO
    }

    override fun onNodePowerInfoChanged(p0: UDNode?) {
        // TODO
    }

    override fun onNodeToGroupRoleChanged(p0: UDNode?,
                                          p1: UDGroup?,
                                          p2: Char
    ) {
        // TODO
    }

    override fun onNodeErrorCleared(p0: UDProxyDevice?,
                                    p1: UDNode?
    ) {
        // TODO
    }

    override fun onNodeEnabled(p0: UDNode?,
                               p1: Boolean
    ) {
        // TODO
    }

    override fun onInternetAccessDisabled() {
        // TODO
    }

    override fun onNodeRemovedFromGroup(p0: UDNode?,
                                        p1: UDGroup?
    ) {
        // TODO
    }

    override fun onNodeSupportedTypeInfoChanged(p0: UDProxyDevice?,
                                                p1: String?
    ) {
        // TODO
    }

    override fun onFolderRenamed(p0: UDFolder?) {
        // TODO
    }

    override fun onLinkerEvent(p0: UDProxyDevice?,
                               p1: String?,
                               p2: XMLElement?
    ) {
        // TODO
    }

    override fun onNodeRenamed(p0: UDNode?) {
        // TODO
    }

    override fun onSystemConfigChanged(p0: String?,
                                       p1: XMLElement?
    ) {
        // TODO
    }

    override fun onNodeRemoved(p0: String?) {
        // TODO
    }

    //endregion
}