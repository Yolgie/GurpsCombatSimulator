package at.cnoize.util.at.cnoize.gcs.app

import at.cnoize.gcs.app.character.BasicAttribute.DX
import at.cnoize.gcs.app.character.BasicAttribute.values
import at.cnoize.gcs.app.character.Character
import at.cnoize.gcs.app.character.SecondaryCharacteristic.HP
import at.cnoize.gcs.app.character.skills.DefaultableFrom
import at.cnoize.gcs.app.character.skills.Skill
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class CharacterTest {

    @Test
    fun `test attribute defaults`() {
        val character = Character("Player")

        values().forEach { basicAttribute ->
            val result = character.get(basicAttribute)
            assertEquals(Character.defaultAttributeLevel, result)
        }
    }

    @Test
    fun `test attribute set`() {
        values().forEach { chosenAttribute ->
            val character = Character("Player", mapOf(chosenAttribute to 12))

            val chosenAttributeResult = character.get(chosenAttribute)
            assertEquals(12, chosenAttributeResult)

            values()
                .filterNot { it == chosenAttribute }
                .forEach { basicAttribute ->
                    val result = character.get(basicAttribute)
                    assertEquals(Character.defaultAttributeLevel, result)
                }
        }
    }

    @Test
    fun `test get with DefaultFrom`() {
        val basicCharacter = Character("Bart", skills = mapOf(Skill.Brawling to 10, Skill.Shield to 10))

        val defaultFromAttribute = DX as DefaultableFrom
        val defaultFromCharacteristic = HP as DefaultableFrom
        val defaultFromSkillDirectly = Skill.Brawling as DefaultableFrom
        val defaultFromSkillWithoutDefault = Skill.Karate as DefaultableFrom
        val defaultFromSkillFromAnotherSkill = Skill.Cloak as DefaultableFrom

        assertEquals(Character.defaultAttributeLevel, basicCharacter.get(defaultFromAttribute))
        assertEquals(Character.defaultAttributeLevel, basicCharacter.get(defaultFromCharacteristic))
        assertEquals(Character.defaultAttributeLevel, basicCharacter.get(defaultFromSkillDirectly))
        assertEquals(null, basicCharacter.get(defaultFromSkillWithoutDefault))
        assertEquals(Character.defaultAttributeLevel-4, basicCharacter.get(defaultFromSkillFromAnotherSkill))
    }

    @Test
    fun `test get default via attribute instead of skill`() {
        val basicCharacter = Character("Bart", skills = mapOf(Skill.TwoHandedAxeMace to 7))

        assertEquals(5, basicCharacter.get(Skill.AxeMace)) // via DX-5
    }

    @Test
    fun `test get default via other skill`() {
        val basicCharacter = Character("Bart", skills = mapOf(Skill.TwoHandedAxeMace to 9))

        assertEquals(6, basicCharacter.get(Skill.AxeMace)) // via TwoHandedAxeMace - 3
    }

    @Test
    fun `test get no default via other skill`() {
        val basicCharacter = Character("Bart", skills = mapOf(Skill.TwoHandedAxeMace to 9))

        assertNull(basicCharacter.get(Skill.AxeMace, followDefaults = false))
    }

    @Test
    fun `test get best default`() {
        val basicCharacter = Character("Bart")

        val default = with(basicCharacter) { Skill.AxeMace.getBestDefault() }

        assertEquals(5, default) // via DX-5
    }
}
