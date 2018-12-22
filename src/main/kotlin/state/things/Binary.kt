package state.things

import state.Thing
import state.data.Platform
import state.machine.StateMachine
import state.machine.base.DeviceEvent
import state.machine.base.DeviceState
import state.machine.base.SideEffect

sealed class BinaryState(override val value: Boolean) : DeviceState<Boolean> {
    object On : BinaryState(true)
    object Off : BinaryState(false)
}

sealed class BinaryEvent : DeviceEvent {
    object TurnOn : BinaryEvent()
    object TurnOff : BinaryEvent()
    object Toggle : BinaryEvent()
}

class BinaryThing(
        id: String,
        name: String,
        platform: Platform,
        initialGraph: StateMachine.Graph<BinaryState, BinaryEvent, SideEffect, Boolean>? = null,
        initialState: BinaryState = BinaryState.Off,
        sideEffect: SideEffect,
        children: MutableList<Thing<*, *, *, *>> = mutableListOf()
) : Thing<BinaryState, BinaryEvent, SideEffect, Boolean>(
        id,
        name,
        platform,
        GraphBuilder(initialGraph)
            .apply {
                state<BinaryState.Off> {
                    on<BinaryEvent.TurnOn> {
                        transitionTo(BinaryState.On,
                                     sideEffect
                        )
                    }
                    on<BinaryEvent.Toggle> {
                        transitionTo(BinaryState.On,
                                     sideEffect
                        )
                    }
                }
                state<BinaryState.On> {
                    on<BinaryEvent.TurnOff> {
                        transitionTo(BinaryState.Off,
                                     sideEffect
                        )
                    }
                    on<BinaryEvent.Toggle> {
                        transitionTo(BinaryState.Off,
                                     sideEffect
                        )
                    }
                }
                onTransition {
                    sideEffect.execute(it)
                }
            }
            .build(),
        initialState,
        children
)