data class IllegalCluster(
        val sounds: List<String>,
        val rules: Rules,
) : Registrable {
    init {
        sounds.forEach {
            check(rules.sounds.containsKey(it)) { "The Token.Sound $it has not been registered with $rules." }
        }
    }

    override fun register() {
        rules.registerIllegalCluster(this)
    }
}