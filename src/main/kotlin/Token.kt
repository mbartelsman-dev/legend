/**
 * Any of the various possible tokens for word generation
 *
 * @property id Unique [String] identifier
 * @property rules [Rules] object that will be used for word generation with this token
 */
sealed class Token : Registrable {
    abstract val id: String
    protected abstract val rules: Rules

    /**
     * Token representing a variant of a syllable structure
     * @property soundClassIds [List] of [String] identifiers representing the [Token.SoundClass]es
     * that compose this syllable structure
     * @property soundClasses [List] of [Token.SoundClass] that compose this syllable structure
     * @property weight [Double] that will be used when selecting this variant of the given [id]
     */
    data class Syllable(
            override val id: String,
            private val soundClassIds: List<String>,
            override val rules: Rules,
            val weight: Double = 1.0,
    ) : Token() {

        val soundClasses: Map<List<SoundClass>, Double> = mapOf(
                soundClassIds.map {
                    rules.soundClasses[it] ?: error("The Token.SoundClass $id has not been registered with $rules.")
                } to weight
        )
    }

    /**
     * Token representing a variant of a syllable structure
     * @property soundIds [List] of [String] identifiers representing [Token.Sound]s that can
     * stand for this sound class
     * @property sounds [List] of [Token.Sound]s that can stand for this sound class
     */
    data class SoundClass(
            override val id: String,
            private  val soundIds: List<String>,
            override val rules: Rules,
    ) : Token() {

        val sounds: List<Sound> = let {
            soundIds.map {
                rules.sounds[it] ?: error("The Token.Sound $it has not been registered with $rules.")
            }
        }

        /**
         * Combines two [Token.SoundClass]es by joining their possible sounds, with [other] being put at the back
         * @param other the [Token.SoundClass] that will be combined with this object.
         * @return a new instance of [Token.SoundClass] containing the joined [Token.Sound] lists
         */
        operator fun plus(other: SoundClass): SoundClass = SoundClass(id, soundIds + other.soundIds, rules)
    }


    /**
     * Token representing a possible phoneme in the word generation rules
     */
    data class Sound(
            override val id: String,
            override val rules: Rules,
    ) : Token()

    /**
     * Registers this token to [rules]
     */
    override fun register() {
        rules.registerToken(this)
    }
}