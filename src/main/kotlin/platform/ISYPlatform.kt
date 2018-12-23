package platform

import common.models.AuthenticationResult
import common.models.Credentials
import data.remote.isy.ISYClient

class ISYPlatform(
        override val credentials: Credentials.Passsword,
        private val client: ISYClient
) : AuthenticatedPlatform<Credentials.Passsword> {
    override val name: String = ISYPlatform::class.simpleName ?: "ISY"
    override val type: PlatformType = PlatformType.POLL

    override suspend fun authenticate(): AuthenticationResult =
        client.connect(credentials)

    override suspend fun initialize() {
        authenticate()
        client.addAllNodes()
    }

    override suspend fun destroy() {
        client.disconnect()
    }
}