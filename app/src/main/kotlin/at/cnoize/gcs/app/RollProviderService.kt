package at.cnoize.gcs.app

import at.cnoize.gcs.app.ProjectSettings.getRollProviderService
import kotlin.random.Random
import kotlin.random.nextInt

interface RollProviderService {
    fun singleDieRoll(): Int

    fun Dice.roll(): Int {
        return nTimes(this.count) { singleDieRoll() } + mod
    }

    private fun nTimes(n: Int, transform: () -> Int): Int {
        return (1..n).map { transform.invoke() }.sum()
    }
}

fun Dice.roll(): Int {
    return with(getRollProviderService()) { roll() }
}

class FixedRollProviderService(val fixedRoll: Int) : RollProviderService {

    init {
        if (fixedRoll !in Dice.faces) {
            throw IllegalArgumentException(
                "RollServiceProvider can not return $fixedRoll since it's not a valid dice face"
            )
        }
    }

    override fun singleDieRoll(): Int {
        return fixedRoll
    }
}

class RandomRollProviderService : RollProviderService {
    override fun singleDieRoll(): Int {
        return Random.nextInt(Dice.faces)
    }
}
