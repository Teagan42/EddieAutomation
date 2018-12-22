package state

import state.data.Platform
import state.machine.StateMachine
import state.machine.base.DeviceEvent
import state.machine.base.DeviceState
import state.machine.base.SideEffect

abstract class Thing<STATE : DeviceState<VALUE>, EVENT : DeviceEvent, SIDE_EFFECT : SideEffect, VALUE>(
        val id: String,
        val name: String,
        val platform: Platform,
        initialGraph: StateMachine.Graph<STATE, EVENT, SIDE_EFFECT, VALUE>,
        initialState: STATE,
        val parent: Thing<*, *, *, *>? = null
) : StateMachine<STATE, EVENT, SIDE_EFFECT, VALUE>(
        initialGraph,
        initialState
)