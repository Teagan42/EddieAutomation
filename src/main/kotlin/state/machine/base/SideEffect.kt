package state.machine.base

import state.Device
import state.machine.StateMachine

interface SideEffect {
    fun execute(transition: StateMachine.Transition<*, *, *, *>)
}