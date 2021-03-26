@file:Suppress("MagicNumber", "Unused") // rules

package at.cnoize.gcs.app.character.skills

import at.cnoize.gcs.app.character.BasicAttribute

enum class Skill : DefaultFrom {
    AxeMace,
    Broadsword,
    Knife,
    Spear,
    TwoHandedAxeMace,
    Shield
}

val SkillDefaults: Map<Skill, Map<DefaultFrom, Int>> = mapOf(
    Skill.AxeMace to mapOf(BasicAttribute.DX to -5, Skill.TwoHandedAxeMace to -3),
    Skill.TwoHandedAxeMace to mapOf(BasicAttribute.DX to -5, Skill.AxeMace to -3),
    Skill.Broadsword to mapOf(BasicAttribute.DX to -5),
    Skill.Spear to mapOf(BasicAttribute.DX to -5),
    Skill.Knife to mapOf(BasicAttribute.DX to -4),
    Skill.Shield to mapOf(BasicAttribute.DX to -4),
)
