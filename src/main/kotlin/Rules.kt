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
    val sounds: Map<String, Token.Sound>
        get() = _sounds
    private val _sounds: MutableMap<String, Token.Sound> = mutableMapOf()

    val soundClasses: Map<String, Token.SoundClass>
        get() = _soundClasses
    private val _soundClasses: MutableMap<String, Token.SoundClass> = mutableMapOf()

    val syllables: Map<String, Map<Token.Syllable, Double>>
        get() = _syllables
    private val _syllables: MutableMap<String, MutableMap<Token.Syllable, Double>> = mutableMapOf()

    val patterns: Set<Pair<String, String>>
        get() = _patterns
    private val _patterns: MutableSet<Pair<String, String>> = mutableSetOf()

    val illegalClusters: List<List<String>>
        get() = _illegalClusters
    private val _illegalClusters: MutableList<List<String>> = mutableListOf()

    init {
        _syllables["<init>"] = mutableMapOf()
        _syllables["<fin>"] = mutableMapOf()
    }

    fun register(obj: Registrable) {
        when (obj) {
            is Token.Sound -> addSound(obj)
            is Token.SoundClass -> addSoundClass(obj)
            is Token.Syllable -> addSyllable(obj)
            is Pattern -> addPattern(obj)
            is Cluster -> forbidCluster(obj)
        }
    }

    private fun addSound(sound: Token.Sound) {
        _sounds[sound.id] = sound
    }

    private fun addSoundClass(soundClass: Token.SoundClass) {
        soundClass.sounds.forEach{ check(sounds.containsKey(it)) { unregisteredErrorMsg(it, soundClass.id) } }

        if (_soundClasses.containsKey(soundClass.id)) {
            _soundClasses[soundClass.id] = _soundClasses[soundClass.id]!!.join(soundClass)
        } else {
            _soundClasses[soundClass.id] = soundClass
        }
    }

    private fun addSyllable(syllable: Token.Syllable) {
        syllable.structure.forEach{ check(soundClasses.containsKey(it)) { unregisteredErrorMsg(it, syllable.id) } }

        if (_syllables.containsKey(syllable.id)) {
            _syllables[syllable.id]!![syllable] = syllable.weight
        } else {
            _syllables[syllable.id] = mutableMapOf(syllable to syllable.weight)
        }
    }

    /**
     * Registers a new [Pattern] into [patterns]
     */
    private fun addPattern(pattern: Pattern) {
        check( syllables.containsKey(pattern.first) ) { unregisteredErrorMsg(pattern.first) }
        check( syllables.containsKey(pattern.second) ) { unregisteredErrorMsg(pattern.second) }

        _patterns.add(Pair(pattern.first, pattern.second))
    }

    /**
     * Registers a new [Cluster] into [illegalClusters]
     */
    private fun forbidCluster(cluster: Cluster) {
        cluster.sounds.forEach{ check(sounds.containsKey(it)) { unregisteredErrorMsg(it) } }
        _illegalClusters.add(cluster.sounds)
    }

    private fun unregisteredErrorMsg(unreg: String, parent: String? = null): String {
        val parentString = if (parent != null) " from $parent" else ""
        return "The identifier $unreg from$parentString has not been registered with the rules for $id,"
    }
}