package platform

import com.udi.isy.jsdk.insteon.ISYInsteonClient
import com.universaldevices.device.model.ISYConfig
import data.remote.isy.ISYClient
import state.data.AuthenticatedPlatform
import state.data.PlatformType
import state.data.models.AuthenticationResult
import state.data.models.Credentials
import state.things.BinaryEvent
import state.things.OnOffEvent

typealias DON = OnOffEvent.TurnOn<Int>
typealias DOFF = OnOffEvent.TurnOff<Int>

class ISYPlatform(
        private val client: ISYClient
) : AuthenticatedPlatform<Credentials.Passsword> {
    override val name: String = ISYPlatform::class.simpleName ?: "ISY"
    override val type: PlatformType = PlatformType.POLL

    override suspend fun authenticate(credentials: Credentials.Passsword): AuthenticationResult =
        client.connect(credentials)

    override suspend fun initialize() {
        client.addAllNodes()
    }
}