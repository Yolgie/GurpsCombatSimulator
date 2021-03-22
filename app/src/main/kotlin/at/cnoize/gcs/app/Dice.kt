package at.cnoize.gcs.app

import kotlin.math.abs

data class Dice(val count: Int = 3, val mod: Int = 0) {
    private val modSign: Char = if (mod >= 0) '+' else '-'

    operator fun plus(other: Dice): Dice {
        return Dice(this.count + other.count, this.mod + other.mod)
    }

    override fun toString(): String {
        return when (mod) {
            0 -> "${count}d"
            else -> "${count}d$modSign${abs(mod)}"
        }
    }

    companion object {
        const val minValue = 1
        const val maxValue = 6
        val faces = (minValue..maxValue)
    }
}
