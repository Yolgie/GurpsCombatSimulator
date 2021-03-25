package at.cnoize.gcs.app.weapons

data class Weapon(val name: String, val modes: Collection<WeaponMode>)

data class ActiveWeapon(val weapon: Weapon, val state: WeaponState)

