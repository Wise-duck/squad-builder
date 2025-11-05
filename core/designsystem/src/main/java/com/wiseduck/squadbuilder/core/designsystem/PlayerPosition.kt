package com.wiseduck.squadbuilder.core.designsystem

import androidx.compose.ui.graphics.Color
import com.wiseduck.squadbuilder.core.designsystem.theme.Blue500
import com.wiseduck.squadbuilder.core.designsystem.theme.Green500
import com.wiseduck.squadbuilder.core.designsystem.theme.Red500
import com.wiseduck.squadbuilder.core.designsystem.theme.Yellow300

enum class PlayerPosition(val abbr: String, val color: Color) {
    FW("FW", Red500),
    MF("MF", Green500),
    DF("DF", Blue500),
    GK("GK", Yellow300),

    ST("ST", Red500),
    CF("CF", Red500),
    LW("LW", Red500),
    RW("RW", Red500),
    LF("LF", Red500),
    RF("RF", Red500),

    CAM("CAM", Green500),
    LAM("LAM", Green500),
    RAM("RAM", Green500),
    CM("CM", Green500),
    LCM("LCM", Green500),
    RCM("RCM", Green500),
    LM("LM", Green500),
    RM("RM", Green500),
    CDM("CDM", Green500),
    LDM("LCM", Green500),
    RDM("RCM", Green500),

    CB("CB", Blue500),
    LCB("LCB", Blue500),
    RCB("RCB", Blue500),
    LB("LB", Blue500),
    RB("RB", Blue500),
    LWB("LWB", Blue500),
    RWB("RWB", Blue500),
    ;

    companion object {
        fun getColor(position: String): Color {
            return entries.find { it.abbr.equals(position, ignoreCase = true) }?.color ?: Color.White
        }
    }
}
