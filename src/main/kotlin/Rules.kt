/**
 * Class that contains the rules for generating a word for a given language
 *
 * @property sounds [Map] of [String] ids to their registered [Token.Sound] objects
 * @property soundClasses [Map] of [String] ids to their registered [Token.SoundClass] objects
 * @property syllables [Map] of [String] ids to their registered [Token.Syllable] objects
 * @property patterns [Set] of allowed [Token.Syllable] patterns in a word
 * @property illegalClusters [List] of dissallowed sequences of [Token.Sound]s in a word
 */
class Rules private constructor(
        val id: String,
        val sounds: Map<String, Token.Sound>,
        val soundClasses: Map<String, Token.SoundClass>,
        val syllables: Map<String, Map<Token.Syllable, Double>>,
        val patterns: Set<Pair<String, String>>,
        val illegalClusters: List<List<String>>,
) {
    class Builder(private val id: String) {
        private val sounds: MutableMap<String, Token.Sound> = mutableMapOf()
        private val soundClasses: MutableMap<String, Token.SoundClass> = mutableMapOf()
        private val syllables: MutableMap<String, MutableMap<Token.Syllable, Double>> = mutableMapOf()
        private val patterns: MutableSet<Pair<String, String>> = mutableSetOf()
        private val illegalClusters: MutableList<List<String>> = mutableListOf()

        init {
            syllables[Token.Syllable.INIT.id] = mutableMapOf(Token.Syllable.INIT to Token.Syllable.INIT.weight)
            syllables[Token.Syllable.LAST.id] = mutableMapOf(Token.Syllable.LAST to Token.Syllable.LAST.weight)
        }

        /**
         * Adds a [Registrable] object to the rules
         * @param obj the object to be added
         */
        fun register(obj: Registrable) {
            when (obj) {
                is Token.Sound -> addSound(obj)
                is Token.SoundClass -> addSoundClass(obj)
                is Token.Syllable -> addSyllable(obj)
                is Pattern -> addPattern(obj)
                is Cluster -> forbidCluster(obj)
            }
        }

        /**
         * Adds a [Token.Sound] object to the rules
         */
        private fun addSound(sound: Token.Sound) {
            sounds[sound.id] = sound
        }

        /**
         * Adds a [Token.SoundClass] object to the rules
         */
        private fun addSoundClass(soundClass: Token.SoundClass) {
            soundClass.sounds.forEach{ check(sounds.containsKey(it)) { unregisteredErrorMsg(it, soundClass.id) } }

            if (soundClasses.containsKey(soundClass.id)) {
                soundClasses[soundClass.id] = soundClasses[soundClass.id]!!.join(soundClass)
            } else {
                soundClasses[soundClass.id] = soundClass
            }
        }

        /**
         * Adds a [Token.Syllable] object to the rules
         */
        private fun addSyllable(syllable: Token.Syllable) {
            syllable.structure.forEach{ check(soundClasses.containsKey(it)) { unregisteredErrorMsg(it, syllable.id) } }

            if (syllables.containsKey(syllable.id)) {
                syllables[syllable.id]!![syllable] = syllable.weight
            } else {
                syllables[syllable.id] = mutableMapOf(syllable to syllable.weight)
            }
        }

        /**
         * Registers a new [Pattern] into [patterns]
         */
        private fun addPattern(pattern: Pattern) {
            check( syllables.containsKey(pattern.first) ) { unregisteredErrorMsg(pattern.first) }
            check( syllables.containsKey(pattern.second) ) { unregisteredErrorMsg(pattern.second) }

            patterns.add(Pair(pattern.first, pattern.second))
        }

        /**
         * Registers a new [Cluster] into [illegalClusters]
         */
        private fun forbidCluster(cluster: Cluster) {
            cluster.sounds.forEach{ check(sounds.containsKey(it)) { unregisteredErrorMsg(it) } }
            illegalClusters.add(cluster.sounds)
        }

        /**
         * Returns a standard error [String] message for when a token is needed but hasn't been registered yet.
         * @param unreg [String] id of the object that's missing
         * @param parent [String] id of the missing objects parent, if there's any.
         * @return Error string
         */
        private fun unregisteredErrorMsg(unreg: String, parent: String? = null): String {
            val parentString = if (parent != null) " from $parent" else ""
            return "The identifier $unreg from$parentString has not been registered with the rules for $id,"
        }

        fun build(): Rules = Rules(id, sounds, soundClasses, syllables, patterns, illegalClusters)
        // TODO: If there are no word initiation or termination patterns, make them for all syllables.
    }
}