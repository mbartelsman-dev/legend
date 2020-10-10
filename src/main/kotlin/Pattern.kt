data class Pattern(
        val first: String,
        val second: String,
        val rules: Rules,
) : Registrable {
    init {
        check(rules.syllables.containsKey(first)) { "The Token.Syllable $first has not been registered with $rules." }
        check(rules.syllables.containsKey(second)) { "The Token.Syllable $second has not been registered with $rules." }
    }

    override fun register() {
        rules.registerPattern(this)
    }
}