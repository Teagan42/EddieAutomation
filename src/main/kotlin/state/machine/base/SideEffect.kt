package state.machine.base

import state.machine.StateMachine

interface SideEffect : (StateMachine<*, *, *, *>, StateMachine.Transition<*, *, *, *>) -> Unit