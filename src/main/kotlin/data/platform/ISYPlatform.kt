package data.platform

import data.remote.isy.ISYClient
import data.platform.models.AuthenticationResult
import data.platform.models.Credentials
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