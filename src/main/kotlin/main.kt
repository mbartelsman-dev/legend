/**
 * Any of the various possible tokens for word generation
 * @property id Unique [String] identifier
 * @property rules [Rules] object that will be used for word generation with this token
 */
sealed class Token() {
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

        val soundClasses: Map<List<Token.SoundClass>, Double> = mapOf(
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

        val sounds: List<Token.Sound> = let {
            soundIds.map {
                rules.sounds[it] ?: error("The Token.Sound $id has not been registered with $rules.")
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
    fun register() {
        rules.registerToken(this)
    }
}

class Pattern {

}

/**
 * Class that contains the rules for generating a word for a given language
 *
 * @property sounds [Map] of [String] ids to their registered [Token.Sound] objects
 * @property soundClasses [Map] of [String] ids to their registered [Token.SoundClass] objects
 * @property syllables [Map] of [String] ids to their registered [Token.Syllable] objects
 */
class Rules {
    private val _sounds: MutableMap<String, Token.Sound> = mutableMapOf()
    val sounds: Map<String, Token.Sound>
        get() = _sounds

    private val _soundClasses: MutableMap<String, Token.SoundClass> = mutableMapOf()
    val soundClasses: Map<String, Token.SoundClass>
        get() = _soundClasses

    private val _syllables: MutableMap<String, MutableMap<Token.Syllable, Double>> = mutableMapOf()
    val syllables: Map<String, Map<Token.Syllable, Double>>
        get() = _syllables

    /**
     * Registers a [Token] with the rules. If a token id is already in use, an appropriate strategy
     * will be employed depending on the specific [Token] variant. For [Token.Sound]s, nothing will
     * happen; for [Token.SoundClass], the contents will be merged; for [Token.Syllable], a new variant
     * of the given id will be registered.
     * @param token: [Token] object that will be registered.
     */
    fun registerToken(token: Token) {
        when (token) {
            is Token.Sound      -> _sounds[token.id] = token

            is Token.SoundClass -> {
                if (_soundClasses.containsKey(token.id)) {
                    _soundClasses[token.id] = _soundClasses[token.id]!! + token
                } else {
                    _soundClasses[token.id] = token
                }
            }

            is Token.Syllable   -> {
                if (_syllables.containsKey(token.id)) {
                    _syllables[token.id]!![token] = token.weight
                } else {
                    _syllables[token.id] = mutableMapOf(token to token.weight)
                }
            }
        }
    }

    fun registerPattern(pattern: Pattern) {
        TODO("Not yet implemented")
    }

    fun removePattern(pattern: Pattern) {
        TODO("Not yet implemented")
    }

    fun registerSoundRestriction(pattern: Pattern) {
        TODO("Not yet implemented")
    }
}

fun main(args: Array<String>) {
    val rules = Rules()
    Token.Sound("p", rules).register()
}