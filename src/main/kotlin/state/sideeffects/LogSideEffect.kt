package state.sideeffects

import state.machine.StateMachine
import state.machine.base.SideEffect

class LogSideEffect : SideEffect {
    override fun execute(transition: StateMachine.Transition<*, *, *, *>) {
        TODO("Not implemented")
    }
}