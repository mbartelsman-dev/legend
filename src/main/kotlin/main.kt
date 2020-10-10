fun main(args: Array<String>) {
    val rules = Rules("Noun")
    listOf<Registrable>(
            Token.Sound("p", rules),
            Token.Sound("t", rules),
            Token.Sound("k", rules),
            Token.Sound("a", rules),
            Token.Sound("e", rules),
            Token.Sound("o", rules),
            Token.Sound("u", rules),
            Token.Sound("r", rules),
            Token.Sound("n", rules),
            Token.Sound("s", rules),
            Token.SoundClass("C", listOf("p", "t", "k"), rules),
            Token.SoundClass("F", listOf("a", "e"), rules),
            Token.SoundClass("B", listOf("o", "u"), rules),
            Token.SoundClass("K", listOf("n", "s"), rules),
            Token.Syllable("SF1", listOf("F"), rules),
            Token.Syllable("SF2", listOf("C", "F"), rules),
            Token.Syllable("SF3", listOf("C", "F", "K"), rules),
            Token.Syllable("SB1", listOf("B"), rules),
            Token.Syllable("SB2", listOf("C", "B"), rules),
            Token.Syllable("SB3", listOf("C", "B", "K"), rules),
            Pattern("<init>", "SF1", rules),
            Pattern("SF1", "SF2", rules),
            Pattern("SF2", "SF2", rules),
            Pattern("SF2", "SF3", rules),
            Pattern("SF3", "<fin>", rules),
            Pattern("<init>", "SB1", rules),
            Pattern("SB1", "SB2", rules),
            Pattern("SB2", "SB2", rules),
            Pattern("SB2", "SB3", rules),
            Pattern("SB3", "<fin>", rules),
            IllegalCluster(listOf("s", "p"), rules)
    ).forEach {
        it.register()
    }
    rules.toString()
}