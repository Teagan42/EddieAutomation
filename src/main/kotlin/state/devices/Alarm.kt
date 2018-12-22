package state.devices

import state.Device
import state.data.Platform
import state.machine.StateMachine
import state.machine.base.DeviceEvent
import state.machine.base.DeviceState
import state.machine.base.SideEffect

enum class Alarm {
    DISARMED,
    ARMED_HOME,
    ARMED_AWAY,
    TRIGGERED
}

sealed class AlarmState(override val value: Alarm) : DeviceState<Alarm> {
    object Disarmed : AlarmState(Alarm.DISARMED)
    object ArmedHome : AlarmState(Alarm.ARMED_HOME)
    object ArmedAway : AlarmState(Alarm.ARMED_AWAY)
    object Triggered : AlarmState(Alarm.TRIGGERED)
}

sealed class AlarmEvent : DeviceEvent {
    object Disarm : AlarmEvent()
    data class Arm(val homeMode: Boolean) : AlarmEvent()
    object Trigger : AlarmEvent()
}

class AlarmDevice(
        id: String,
        name: String,
        platform: Platform,
        initialGraph: StateMachine.Graph<AlarmState, AlarmEvent, SideEffect, Alarm>? = null,
        initialState: AlarmState = AlarmState.Disarmed,
        sideEffect: SideEffect
) : Device<AlarmState, AlarmEvent, SideEffect, Alarm>(
        id,
        name,
        platform,
        GraphBuilder(initialGraph)
            .apply {
                state<AlarmState.Disarmed> {
                    on<AlarmEvent.Arm> {
                        transitionTo(
                                when (it.homeMode) {
                                    true -> AlarmState.ArmedHome
                                    false -> AlarmState.ArmedAway
                                },
                                sideEffect
                        )
                    }
                }
                state<AlarmState.Triggered> {
                    on<AlarmEvent.Disarm> {
                        transitionTo(AlarmState.Disarmed,
                                     sideEffect
                        )
                    }
                }
                state<AlarmState.ArmedHome> {
                    on<AlarmEvent.Trigger> {
                        transitionTo(AlarmState.Triggered,
                                     sideEffect
                        )
                    }
                    on<AlarmEvent.Disarm> {
                        transitionTo(AlarmState.Disarmed,
                                     sideEffect
                        )
                    }
                    on<AlarmEvent.Arm> {
                        transitionTo(when (it.homeMode) {
                                         true -> AlarmState.ArmedHome
                                         false -> AlarmState.ArmedAway
                                     },
                                     sideEffect
                        )
                    }
                }
                state<AlarmState.ArmedAway> {
                    on<AlarmEvent.Trigger> {
                        transitionTo(AlarmState.Triggered,
                                     sideEffect
                        )
                    }
                    on<AlarmEvent.Disarm> {
                        transitionTo(AlarmState.Disarmed,
                                     sideEffect
                        )
                    }
                    on<AlarmEvent.Arm> {
                        transitionTo(when (it.homeMode) {
                                         true -> AlarmState.ArmedHome
                                         false -> AlarmState.ArmedAway
                                     },
                                     sideEffect
                        )
                    }
                }
            }.build(),
        initialState
)