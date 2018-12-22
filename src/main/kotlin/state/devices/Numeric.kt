package state.devices

import state.Device
import state.data.Platform
import state.machine.StateMachine
import state.machine.base.DeviceEvent
import state.machine.base.DeviceState
import state.machine.base.SideEffect

data class NumericState<NumberType>(override val value: NumberType) :
    DeviceState<NumberType> where NumberType : Number, NumberType : Comparable<NumberType>

data class SetNumericValue<NumberType>(val value: NumberType) :
    DeviceEvent where NumberType : Number, NumberType : Comparable<NumberType>

class NumericDevice<NumberType>(
        id: String,
        name: String,
        platform: Platform,
        initialGraph: StateMachine.Graph<NumericState<NumberType>, SetNumericValue<NumberType>, SideEffect, NumberType>? = null,
        initialState: NumericState<NumberType>,
        sideEffect: SideEffect
) : Device<NumericState<NumberType>, SetNumericValue<NumberType>, SideEffect, NumberType>(
        id,
        name,
        platform,
        GraphBuilder(initialGraph)
            .apply {
                state<NumericState<NumberType>> {
                    on<SetNumericValue<NumberType>> {
                        transitionTo(NumericState(it.value),
                                     sideEffect
                        )
                    }
                }
                onTransition {
                    TODO("Handle Transition")
                }
            }
            .build(),
        initialState
) where NumberType : Number, NumberType : Comparable<NumberType>