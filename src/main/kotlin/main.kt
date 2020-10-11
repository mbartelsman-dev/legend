fun main(args: Array<String>) {
    val rules = Rules("Noun")
    listOf<Registrable>(
            Token.Sound("p"),
            Token.Sound("t"),
            Token.Sound("k"),
            Token.Sound("a"),
            Token.Sound("e"),
            Token.Sound("o"),
            Token.Sound("u"),
            Token.Sound("r"),
            Token.Sound("n"),
            Token.Sound("s"),
            Token.SoundClass("C", listOf("p", "t", "k")),
            Token.SoundClass("F", listOf("a", "e")),
            Token.SoundClass("B", listOf("o", "u")),
            Token.SoundClass("K", listOf("n", "s")),
            Token.Syllable("SF1", listOf("F")),
            Token.Syllable("SF2", listOf("C", "F")),
            Token.Syllable("SF3", listOf("C", "F", "K")),
            Token.Syllable("SB1", listOf("B")),
            Token.Syllable("SB2", listOf("C", "B")),
            Token.Syllable("SB3", listOf("C", "B", "K")),
            Pattern("<init>", "SF1"),
            Pattern("SF1", "SF2"),
            Pattern("SF2", "SF2"),
            Pattern("SF2", "SF3"),
            Pattern("SF3", "<fin>"),
            Pattern("<init>", "SB1"),
            Pattern("SB1", "SB2"),
            Pattern("SB2", "SB2"),
            Pattern("SB2", "SB3"),
            Pattern("SB3", "<fin>"),
            Cluster(listOf("s", "p"))
    ).forEach {
        rules.register(it)
    }
    rules.toString()
}