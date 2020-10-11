/**
 * Common superclass for classes that can be registered with [Rules]
 */
sealed class Registrable

data class Pattern(
        val first: String,
        val second: String,
) : Registrable()

data class Cluster(
        val sounds: List<String>,
) : Registrable()

/**
 * Any of the various possible tokens for word generation
 * @property id Unique [String] identifier
 */
sealed class Token : Registrable() {
    abstract val id: String

    /**
     * Token representing a variant of a syllable structure
     * @property structure [List] of [String] identifiers representing the [Token.SoundClass]es
     * that compose this syllable structure
     * @property weight [Double] that will be used when selecting this variant of the given [id]
     */
    data class Syllable(
            override val id: String,
            val structure: List<String>,
            val weight: Double = 1.0
    ) : Token()


    /**
     * Token representing a variant of a syllable structure
     * @property sounds [List] of [String] identifiers representing [Token.Sound]s that can
     * stand for this sound class
     * @property sounds [List] of [Token.Sound]s that can stand for this sound class
     */
    data class SoundClass(
            override val id: String,
            val sounds: List<String>,
    ) : Token()

    /**
     * Token representing a possible phoneme in the word generation rules
     */
    data class Sound(
            override val id: String,
    ) : Token()
}

/**
 * Combines two [Token.SoundClass]es by joining their possible sounds, with [other] being put at the back
 * @param other the [Token.SoundClass] that will be combined with this object.
 * @return a new instance of [Token.SoundClass] containing the joined [Token.Sound] lists
 */
fun Token.SoundClass.join(other: Token.SoundClass): Token.SoundClass =
        Token.SoundClass(id, this.sounds + other.sounds)