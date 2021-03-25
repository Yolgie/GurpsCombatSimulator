@file:Suppress("MagicNumber", "Unused")

package at.cnoize.gcs.app.skills

enum class Skill : DefaultFrom {
    AxeMace,
    Broadsword,
    Knife,
    Spear,
    TwoHandedAxeMace,
}

enum class Attribute : DefaultFrom {
    ST,
    DX,
    IQ,
    HT,
    Per
}

interface DefaultFrom

val SkillDefaults: Map<Skill, Map<DefaultFrom, Int>> = mapOf(
    Skill.AxeMace to mapOf(Attribute.DX to -5, Skill.TwoHandedAxeMace to -3),
    Skill.TwoHandedAxeMace to mapOf(Attribute.DX to -5, Skill.AxeMace to -3),
    Skill.Broadsword to mapOf(Attribute.DX to -5),
    Skill.Spear to mapOf(Attribute.DX to -5),
    Skill.Knife to mapOf(Attribute.DX to -4),
)
