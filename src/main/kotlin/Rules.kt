/**
 * Class that contains the rules for generating a word for a given language
 *
 * @property sounds [Map] of [String] ids to their registered [Token.Sound] objects
 * @property soundClasses [Map] of [String] ids to their registered [Token.SoundClass] objects
 * @property syllables [Map] of [String] ids to their registered [Token.Syllable] objects
 * @property patterns [Set] of allowed [Token.Syllable] patterns in a word
 * @property illegalClusters [List] of dissallowed sequences of [Token.Sound]s in a word
 */
class Rules(
        val id: String
) {
    private val _sounds: MutableMap<String, Token.Sound> = mutableMapOf()
    val sounds: Map<String, Token.Sound>
        get() = _sounds

    private val _soundClasses: MutableMap<String, Token.SoundClass> = mutableMapOf()
    val soundClasses: Map<String, Token.SoundClass>
        get() = _soundClasses

    private val _syllables: MutableMap<String, MutableMap<Token.Syllable, Double>> = mutableMapOf()
    val syllables: Map<String, Map<Token.Syllable, Double>>
        get() = _syllables

    private val _patterns: MutableSet<Pair<String, String>> = mutableSetOf()
    val patterns: Set<Pair<String, String>>
        get() = _patterns

    private val _illegalClusters: MutableList<List<String>> = mutableListOf()
    val illegalClusters: List<List<String>>
        get() = _illegalClusters

    /**
     * Registers a [Token] with the rules. If a token id is already in use, an appropriate strategy
     * will be employed depending on the specific [Token] variant. For [Token.Sound]s, nothing will
     * happen; for [Token.SoundClass], the contents will be merged; for [Token.Syllable], a new variant
     * of the given id will be registered.
     * @param token: [Token] object that will be registered.
     */
    fun registerToken(token: Token) {
        when (token) {
            is Token.Sound -> _sounds[token.id] = token

            is Token.SoundClass -> {
                if (_soundClasses.containsKey(token.id)) {
                    _soundClasses[token.id] = _soundClasses[token.id]!! + token
                } else {
                    _soundClasses[token.id] = token
                }
            }

            is Token.Syllable -> {
                if (_syllables.containsKey(token.id)) {
                    _syllables[token.id]!![token] = token.weight
                } else {
                    _syllables[token.id] = mutableMapOf(token to token.weight)
                }
            }
        }
    }

    /**
     * Registers a new [Pattern] into [patterns]
     */
    fun registerPattern(pattern: Pattern) {
        _patterns.add(Pair(pattern.first, pattern.second))
    }

    /**
     * Registers a new [IllegalCluster] into [illegalClusters]
     */
    fun registerIllegalCluster(cluster: IllegalCluster) {
        _illegalClusters.add(cluster.sounds)
    }
}