package at.cnoize.gcs.app

import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.TestFactory
import kotlin.random.Random
import kotlin.test.assertEquals

class DiceTest {
    @Nested
    inner class `Optional Rule for Modifying Dice + Adds (B269)` {
        // add tests
    }

    @Nested
    inner class `Dice Arithmetic` {
        private fun generateDynamicTestForDiceAddition(
            firstCount: Int,
            secondCount: Int,
            firstMod: Int = 0,
            secondMod: Int = 0
        ): DynamicTest {
            val first = Dice(firstCount, firstMod)
            val second = Dice(secondCount, secondMod)
            val expectedResult = Dice(firstCount + secondCount, firstMod + secondMod)
            return dynamicTest("adding $first + $second to expecting $expectedResult") {
                assertEquals(expectedResult, first + second)
            }
        }

        @TestFactory
        fun `adding without mods`() = listOf(
            0 to 0,
            0 to 1,
            1 to 0,
            1 to 1,
            1 to 2,
            3 to 5,
            0 to 3,
            4 to 0,
            1 to 2
        ).map { (first, second) ->
            generateDynamicTestForDiceAddition(first, second)
        }

        @TestFactory
        fun `adding with mods`() = listOf(
            Pair(0, 0) to Pair(0, 0),
            Pair(0, 1) to Pair(0, 2),
            Pair(0, 1) to Pair(0, 0),
            Pair(0, 0) to Pair(0, 1),
            Pair(0, -1) to Pair(0, 0),
            Pair(0, 0) to Pair(0, -1),
            Pair(3, -3) to Pair(-3, 3),
            Pair(2, 5) to Pair(-1, -5),
            Pair(-4, 2) to Pair(4, 3),
        ).map { (firstDice, secondDice) ->
            generateDynamicTestForDiceAddition(
                firstCount = firstDice.first,
                firstMod = firstDice.second,
                secondCount = secondDice.first,
                secondMod = secondDice.second
            )
        }

        @Nested
        inner class `Random Tests with dice arithmetic` {
            @Nested
            inner class `adding completely random without mods` {
                @TestFactory
                fun `adding randoms without mods`() = generateSequence {
                    generateDynamicTestForDiceAddition(
                        Random.nextInt(),
                        Random.nextInt()
                    )
                }.asIterable().take(TestSettings.repeatRandomTests)
            }

            @Nested
            inner class `adding in range 1-20 without mods` {
                @TestFactory
                fun `adding randoms without mods`() = generateSequence {
                    generateDynamicTestForDiceAddition(
                        Random.nextInt(1, 21),
                        Random.nextInt(1, 21)
                    )
                }.asIterable().take(TestSettings.repeatRandomTests)
            }

            @Nested
            inner class `adding completely random with mods` {
                @TestFactory
                fun `adding randoms with mods`() = generateSequence {
                    generateDynamicTestForDiceAddition(
                        Random.nextInt(),
                        Random.nextInt(),
                        Random.nextInt(),
                        Random.nextInt()
                    )
                }.asIterable().take(TestSettings.repeatRandomTests)
            }

            @Nested
            inner class `adding in range 1-20 with mods` {
                @TestFactory
                fun `adding randoms without mods`() = generateSequence {
                    generateDynamicTestForDiceAddition(
                        firstCount = Random.nextInt(1, 21),
                        firstMod = Random.nextInt(-2, +4),
                        secondCount = Random.nextInt(1, 21),
                        secondMod = Random.nextInt(-2, +4)
                    )
                }.asIterable().take(TestSettings.repeatRandomTests)
            }
        }
    }

    @Nested
    inner class ToString {
        @TestFactory
        fun `toString results in d notation`() = listOf(
            Dice(1, -1) to "1d-1",
            Dice(1, -2) to "1d-2",
            Dice(1, -3) to "1d-3",
            Dice(1, -4) to "1d-4",
            Dice(1, -5) to "1d-5",
            Dice(1, -6) to "1d-6",
            Dice(1, +1) to "1d+1",
            Dice(1, 0) to "1d",
            Dice(2, -1) to "2d-1",
            Dice(2, -2) to "2d-2",
            Dice(2, +2) to "2d+2",
            Dice(3, +0) to "3d",
            Dice(4, +3) to "4d+3",
            Dice(5, +1) to "5d+1",
            Dice(2, 0) to "2d",
            Dice(0, 0) to "0d",
            Dice(0, +1) to "0d+1",
            Dice(0, -1) to "0d-1",
            Dice(0, +4) to "0d+4",
        ).map { (dice, expectedString) ->
            dynamicTest("${dice.count} dice with modifier ${dice.mod} results in $expectedString") {
                assertEquals(expectedString, dice.toString())
            }
        }
    }
}
