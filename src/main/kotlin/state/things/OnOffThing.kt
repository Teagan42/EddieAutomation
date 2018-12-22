package state.things

import state.Thing
import state.data.Platform
import state.machine.StateMachine
import state.machine.base.DeviceEvent
import state.machine.base.DeviceState
import state.machine.base.TransitionHandler

sealed class OnOffState<LEVEL>(override val value: LEVEL) : DeviceState<LEVEL> where LEVEL : Number, LEVEL : Comparable<LEVEL> {
    @Suppress("UNCHECKED_CAST")
    class Off<LEVEL> : OnOffState<LEVEL>(0 as LEVEL) where LEVEL : Number, LEVEL : Comparable<LEVEL>

    class On<LEVEL>(onLevel: LEVEL) : OnOffState<LEVEL>(onLevel) where LEVEL : Number, LEVEL : Comparable<LEVEL>
}

sealed class OnOffEvent<LEVEL>() : DeviceEvent where LEVEL : Number, LEVEL : Comparable<LEVEL> {
    @Suppress("UNCHECKED_CAST")
    class TurnOff<LEVEL> : OnOffEvent<LEVEL>() where LEVEL : Number, LEVEL : Comparable<LEVEL>

    class TurnOn<LEVEL>(val onLevel: LEVEL) : OnOffEvent<LEVEL>() where LEVEL : Number, LEVEL : Comparable<LEVEL>
}

class OnOffThing<LEVEL>(
        id: String,
        name: String,
        platform: Platform,
        initialGraph: StateMachine.Graph<OnOffState<LEVEL>, OnOffEvent<LEVEL>, TransitionHandler, LEVEL>? = null,
        initialState: OnOffState<LEVEL> = OnOffState.Off(),
        transitionHandler: TransitionHandler,
        children: MutableList<Thing<*, *, *, *>> = mutableListOf()
) : Thing<OnOffState<LEVEL>, OnOffEvent<LEVEL>, TransitionHandler, LEVEL>(
        id,
        name,
        platform,
        GraphBuilder(initialGraph)
            .apply {
                state<OnOffState.Off<LEVEL>> {
                    on<OnOffEvent.TurnOn<LEVEL>> {
                        transitionTo(OnOffState.On(it.onLevel))
                    }
                }
                state<OnOffState.On<LEVEL>> {
                    on<OnOffEvent.TurnOff<LEVEL>> {
                        transitionTo(OnOffState.Off())
                    }
                    on<OnOffEvent.TurnOn<LEVEL>> {
                        transitionTo(OnOffState.On(it.onLevel))
                    }
                }
                onTransition(transitionHandler)
            }
            .build(),
        initialState,
        children
) where LEVEL : Number, LEVEL : Comparable<LEVEL>