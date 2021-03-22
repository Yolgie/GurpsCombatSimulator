package at.cnoize.gcs.app

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

class FixedRollProviderService(val fixedRoll: Int) : RollProviderService {
    override fun singleDieRoll(): Int {
        return fixedRoll
    }
}

class RandomRollProviderService : RollProviderService {
    override fun singleDieRoll(): Int {
        return Random.nextInt(Dice.faces)
    }
}
