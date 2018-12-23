package data.remote.isy

import com.universaldevices.device.model.UDNode
import common.models.AuthenticationResult
import common.models.Credentials

enum class ISYCommand(val cmd: String) {
    TURN_ON("DON"),
    TURN_OFF("DOF"),
    TURN_ON_FAST("DFON"),
    TURN_OFF_FAST("DFOFF"),
    BRIGHTEN("BRT"),
    DIM("DIM"),
    SECURE_COMMAND("SECMD"),
    CLIMATE_FAN_SPEED("CLIFS"),
    CLIMATE_MODE("CLIMD"),
    CLIMATE_COOL_SET_POINT("CLISPC"),
    CLIMATE_HEAT_SET_POINT("CLISPH")
}

interface ISYClient {
    suspend fun connect(credentials: Credentials.Passsword): AuthenticationResult

    suspend fun disconnect()

    fun addAllNodes()

    fun onNodeAdded(node: UDNode)

    fun <VALUE> sendCommand(command: ISYCommand,
                            value: VALUE,
                            node: UDNode
    )
}