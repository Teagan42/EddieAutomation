package state

import data.platform.Platform
import state.machine.StateMachine
import state.machine.base.DeviceEvent
import state.machine.base.DeviceState
import state.machine.base.TransitionHandler

abstract class Thing<STATE : DeviceState<VALUE>, EVENT : DeviceEvent, SIDE_EFFECT : TransitionHandler, VALUE>(
        val id: String,
        val name: String,
        val platform: Platform,
        initialGraph: StateMachine.Graph<STATE, EVENT, SIDE_EFFECT, VALUE>,
        initialState: STATE,
        val children: MutableList<Thing<*, *, *, *>> = mutableListOf()
) : StateMachine<STATE, EVENT, SIDE_EFFECT, VALUE>(
        initialGraph,
        initialState
)