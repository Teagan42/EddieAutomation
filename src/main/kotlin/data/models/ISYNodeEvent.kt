package data.models

import com.universaldevices.device.model.UDNode

sealed class ISYNodeEvent(val node: UDNode) {
    class Added(node: UDNode) : ISYNodeEvent(node)
    class Removed(node: UDNode): ISYNodeEvent(node)
    class StateChanged(node: UDNode) : ISYNodeEvent(node)
}