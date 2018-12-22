package state

import platform.Platform
import state.machine.StateMachine
import state.machine.base.ThingEvent
import state.machine.base.ThingState
import state.machine.base.TransitionHandler

abstract class Thing<STATE : ThingState<VALUE>, EVENT : ThingEvent, SIDE_EFFECT : TransitionHandler, VALUE>(
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