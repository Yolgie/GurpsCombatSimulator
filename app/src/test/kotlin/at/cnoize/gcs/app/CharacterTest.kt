package at.cnoize.util.at.cnoize.gcs.app

import at.cnoize.gcs.app.character.BasicAttribute.DX
import at.cnoize.gcs.app.character.BasicAttribute.values
import at.cnoize.gcs.app.character.Character
import at.cnoize.gcs.app.character.SecondaryCharacteristic.HP
import at.cnoize.gcs.app.character.skills.DefaultFrom
import at.cnoize.gcs.app.character.skills.Skill
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

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
        val basicCharacter = Character("Bart", skills = mapOf(Skill.Brawling to 10))

        val defaultFromAttribute = DX as DefaultFrom
        val defaultFromCharacteristic = HP as DefaultFrom
        val defaultFromSkill = Skill.Brawling as DefaultFrom
        val defaultFromSkillWithoutDefault = Skill.Karate as DefaultFrom

        assertEquals(Character.defaultAttributeLevel, basicCharacter.get(defaultFromAttribute))
        assertEquals(Character.defaultAttributeLevel, basicCharacter.get(defaultFromCharacteristic))
        assertEquals(Character.defaultAttributeLevel, basicCharacter.get(defaultFromSkill))
        assertEquals(null, basicCharacter.get(defaultFromSkillWithoutDefault))
    }
}
