package platform

import data.remote.isy.ISYClient
import platform.models.AuthenticationResult
import platform.models.Credentials
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