package at.cnoize.gcs.app

object ProjectSettings {

    private val featureFlags = mapOf(
            FeatureFlag.ModifyDicePlusAdds to false,
            FeatureFlag.DamageToShields to false
    )

    fun getFlag(featureFlag: FeatureFlag): Boolean {
        return featureFlags.getOrDefault(featureFlag, false)
    }

    fun getRollProviderService(): RollProviderService {
        return RandomRollProviderService()
    }
}

enum class FeatureFlag(val description: String, val reference: String) {
    ModifyDicePlusAdds("Optional Rule for Modifying Dice + Adds", "B269"),
    DamageToShields(
            "Optional Rule for calculating combat damage to shields if the DB makes the difference in a defense",
            "B484"
    )
}
