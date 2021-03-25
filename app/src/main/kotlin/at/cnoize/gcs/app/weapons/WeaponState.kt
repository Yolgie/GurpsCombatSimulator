package at.cnoize.gcs.app.weapons

enum class WeaponState {
    /* weapon is ready to be used in an attack
     */
    Ready,

    /* melee weapons with a † or ‡ next to their ST rating become can unready
     * if you attack with them and do not have enough ST.
     * Maybe change to counter to be able to handle Whips & other weapons that
     * take multiple ready maneuvers to use.
     */
    Unready,

    /* melee weapons with `U` next to their parry rating are unbalanced and can
     * not be used to parry if they where used to attack this turn (and vice versa).
     */
    Unbalanced,

    /* picks are stuck after a successful attack and have to be extracted with a
     * ST roll to be used again.
     */
    Stuck,

    /* broken weapons, but especially shields can not be used to attack or defend
     * anymore, but still encumber until you drop it
     */
    Broken
}
