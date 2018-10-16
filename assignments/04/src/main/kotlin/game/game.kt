package game

import java.io.File
import java.util.Random

class WordHandler {
    companion object {
        val words = File("dictionary.txt").readText().split("\n")
    }

    fun get_random_word(): String {
        return words[Random().nextInt(words.size)]
    }
}

class Game {
    companion object {
        const val N_ATTEMPTS = 10
    }

    var active = false

    fun print_greeting() {
        println("Hi there!")
        println("Let's play the game!")
        println("I'll make a word and you'll try to guess it in 10 attempts\n")
    }

    fun play() {
        clear_screen()
        print_greeting()

        var n_attempts = N_ATTEMPTS

        val word_handler = WordHandler()

        val guessed_word = word_handler.get_random_word()
        val guessed_len = guessed_word.length

        println("My word consist of " + guessed_len.toString() + " characters!\n")

        var w: String?

        while (n_attempts > 0) {
            var correct_input = false

            do {
                w = readLine()
                if (w == null || guessed_len != w.length) {
                    println("Your word must contain " + guessed_len.toString() + " characters. Please, try again!\n")
                } else {
                    correct_input = true
                }
            } while (!correct_input)

            if (w == guessed_word) {
                println("\nExcellent! You win!")
                println("I do made the word '" + guessed_word + "'\n")
                return
            } else {
                println("Didn't guess!\n")
            }

            n_attempts--
        }

        println("You loose!")
        println("I made the word '" + guessed_word + "'\n")
    }
}

fun clear_screen() {
    print("\u001b[H\u001b[2J")
}

fun main(args: Array<String>) {
    val game = Game()
    game.active = true

    while (game.active) {
        game.play()
        var ans: String?

        do {
            println("Play again? [Y/n]")
            ans = readLine()?.toLowerCase()
        } while (ans != "y" && ans != "n")

        if (ans == "n") {
            game.active = false
        }
    }
}
