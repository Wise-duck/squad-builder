package com.wiseduck.squadbuilder.feature.edit.formation.data

data class PositionBounds(
    val minX: Float,
    val maxX: Float,
    val minY: Float,
    val maxY: Float,
    val centerX: Float,
    val centerY: Float,
)

fun getPositionForCoordinates(x: Float, y: Float): String {
    for ((position, bounds) in FormationConstants.SLOT_ZONES_BOUNDS) {
        if (x >= bounds.minX && x < bounds.maxX && y >= bounds.minY && y < bounds.maxY) {
            return position
        }
    }
    return ""
}

object FormationConstants {
    val SLOT_ZONES_BOUNDS: Map<String, PositionBounds> = mapOf(
        // Goalkeeper
        "GK" to PositionBounds(0.40f, 0.60f, 0.87f, 1.00f, 0.50f, 0.92f),
        // Defense
        "LWB" to PositionBounds(0.07f, 0.24f, 0.60f, 0.73f, 0.16f, 0.66f),
        "LB" to PositionBounds(0.07f, 0.24f, 0.73f, 0.85f, 0.16f, 0.79f),
        "LCB" to PositionBounds(0.24f, 0.40f, 0.71f, 0.85f, 0.36f, 0.78f),
        "CB" to PositionBounds(0.40f, 0.60f, 0.71f, 0.85f, 0.50f, 0.78f),
        "RCB" to PositionBounds(0.60f, 0.76f, 0.71f, 0.85f, 0.64f, 0.78f),
        "RB" to PositionBounds(0.76f, 0.93f, 0.73f, 0.85f, 0.84f, 0.79f),
        "RWB" to PositionBounds(0.76f, 0.93f, 0.60f, 0.73f, 0.84f, 0.66f),
        // Midfield
        "LDM" to PositionBounds(0.24f, 0.40f, 0.57f, 0.71f, 0.32f, 0.64f),
        "CDM" to PositionBounds(0.40f, 0.60f, 0.57f, 0.71f, 0.50f, 0.64f),
        "RDM" to PositionBounds(0.60f, 0.76f, 0.57f, 0.71f, 0.68f, 0.64f),
        "LCM" to PositionBounds(0.24f, 0.40f, 0.43f, 0.57f, 0.32f, 0.50f),
        "CM" to PositionBounds(0.40f, 0.60f, 0.43f, 0.57f, 0.50f, 0.50f),
        "RCM" to PositionBounds(0.60f, 0.76f, 0.43f, 0.57f, 0.68f, 0.50f),
        "LAM" to PositionBounds(0.24f, 0.40f, 0.29f, 0.43f, 0.32f, 0.36f),
        "CAM" to PositionBounds(0.40f, 0.60f, 0.29f, 0.43f, 0.50f, 0.36f),
        "RAM" to PositionBounds(0.60f, 0.76f, 0.29f, 0.43f, 0.68f, 0.36f),
        "LM" to PositionBounds(0.07f, 0.24f, 0.30f, 0.60f, 0.16f, 0.45f),
        "RM" to PositionBounds(0.76f, 0.93f, 0.30f, 0.60f, 0.84f, 0.45f),
        // Forward
        "LW" to PositionBounds(0.07f, 0.24f, 0.00f, 0.30f, 0.16f, 0.15f),
        "LS" to PositionBounds(0.24f, 0.40f, 0.00f, 0.15f, 0.32f, 0.07f),
        "ST" to PositionBounds(0.40f, 0.60f, 0.00f, 0.15f, 0.50f, 0.09f),
        "RS" to PositionBounds(0.60f, 0.76f, 0.00f, 0.15f, 0.68f, 0.07f),
        "RW" to PositionBounds(0.76f, 0.93f, 0.00f, 0.30f, 0.84f, 0.15f),
        "LF" to PositionBounds(0.24f, 0.40f, 0.15f, 0.29f, 0.32f, 0.22f),
        "CF" to PositionBounds(0.40f, 0.60f, 0.15f, 0.29f, 0.50f, 0.22f),
        "RF" to PositionBounds(0.60f, 0.76f, 0.15f, 0.29f, 0.68f, 0.22f)
    )
}