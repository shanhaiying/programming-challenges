import java.io.File

// 2463

fun main(args: Array<String>) {

    assert(score(".#....##....#####...#######....#.#..##.", 3) == 325)

    val regex = Regex(".* => .*")
    val margin = 3
    val nIterations = 50000000000


    val lines = File(args[0]).readLines()

    var leftPots = 0
    var state = StringBuilder(lines[0].split(":")[1].trim())

    val transitions = lines.filter { it.matches(regex) }.map { line ->
        val split = line.split("=>")
        Pair(split[0].trim(), split[1].trim()[0])
    }.toMap()

    println("0 -> $state")


    val scores = ArrayList<Int>(5000)
    scores.add(score(state.toString(), leftPots))
    for (gen in 1..nIterations) {

        val leftEmptyPots = CharArray(maxOf(margin - state.indexOf('#'), 0)) { '.' }
        state.insert(0, leftEmptyPots)
        leftPots += leftEmptyPots.size

        val rightEmptyPots = CharArray(maxOf(margin - (state.length - state.lastIndexOf('#')), 0) + 1) { '.' }
        state.append(rightEmptyPots)

        val nextState = StringBuilder(state)

        transitions.forEach { t, u ->
            var patternIndex = state.indexOf(t, 0)
            while (patternIndex != -1) {
                nextState[patternIndex + 2] = u
                patternIndex = state.indexOf(t, patternIndex + 1)
            }
        }

        state = nextState

        scores.add(score(state.toString(), leftPots))

        val convergenceDelta = 5
        val distinct = scores.takeLast(convergenceDelta).zipWithNext().map { it.second - it.first }
        if(scores.size >= 5 && distinct.all { it == distinct.last() }) {
            println(scores.last() + (nIterations - gen) * distinct.last())
            break
        }
    }
}

private fun score(state: String, leftPots: Int): Int =
        (0 until state.length)
        .filter { state[it] == '#' }
        .sumBy { it - leftPots }