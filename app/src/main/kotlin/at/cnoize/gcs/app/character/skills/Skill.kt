@file:Suppress("MagicNumber", "Unused") // rules

package at.cnoize.gcs.app.character.skills

import at.cnoize.gcs.app.character.BasicAttribute

enum class Skill : DefaultableFrom {
    AxeMace,
    Broadsword,
    Knife,
    Spear,
    TwoHandedAxeMace,
    Shield,
    Brawling,
    Boxing,
    Karate,
    Judo,
    Wrestling,
    Cloak,
    Bolas
}

val SKILL_DEFAULTS: Map<Skill, Map<DefaultableFrom, Int>> = mapOf(
    Skill.AxeMace to mapOf(BasicAttribute.DX to -5, Skill.TwoHandedAxeMace to -3),
    Skill.TwoHandedAxeMace to mapOf(BasicAttribute.DX to -5, Skill.AxeMace to -3),
    Skill.Broadsword to mapOf(BasicAttribute.DX to -5),
    Skill.Spear to mapOf(BasicAttribute.DX to -5),
    Skill.Knife to mapOf(BasicAttribute.DX to -4),
    Skill.Shield to mapOf(BasicAttribute.DX to -4),
    Skill.Cloak to mapOf(BasicAttribute.DX to -5, Skill.Shield to -4), // and Net-4
)
