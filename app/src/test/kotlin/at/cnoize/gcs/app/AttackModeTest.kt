package at.cnoize.gcs.app

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertFails
import kotlin.test.assertNotNull

class AttackModeTest {

    @Nested
    inner class `Damage Dice` {
        @Test
        fun `all attack mode damage dice defined for 1-20 ST`() {
            AttackMode.values().forEach { mode ->
                (1..20).forEach { ST ->
                    assertNotNull(mode.getDamageDice(ST))
                }
            }
        }

        @Test
        fun `no damage dice for negative ST`() {
            AttackMode.values().forEach { mode ->
                assertFails { mode.getDamageDice(0) }
                assertFails { mode.getDamageDice(-1) }
                assertFails { mode.getDamageDice(Int.MIN_VALUE) }
            }
        }

        @Test
        fun `no damage dice for too high ST`() {
            AttackMode.values().forEach { mode ->
                assertFails { mode.getDamageDice(21) }
                assertFails { mode.getDamageDice(30) }
                assertFails { mode.getDamageDice(50) }
                assertFails { mode.getDamageDice(100) }
                assertFails { mode.getDamageDice(Int.MAX_VALUE) }
            }
        }
    }
}
