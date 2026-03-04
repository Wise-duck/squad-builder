package com.wiseduck.squadbuilder.core.common.events

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object EventHandler {
    private val _eventFlow = MutableSharedFlow<SquadBuilderEvent>(extraBufferCapacity = 1)
    val eventFlow = _eventFlow.asSharedFlow()

    fun emit(event: SquadBuilderEvent) {
        _eventFlow.tryEmit(event)
    }
}

sealed interface SquadBuilderEvent {
    data class ShowDialog(val dialogSpec: SquadBuilderDialogSpec) : SquadBuilderEvent
}
