package com.example.dice

import kotlinx.coroutines.delay

class DiceModel {

    // suspend - призупиняюча функція. Thread.sleep() - блокує систему
    suspend fun rollSingleDie(durationMs: Long, onTick: (Int) -> Unit) {
        val iterations = (durationMs / 100).toInt()

        for (i in 0 until iterations) {
            val randomFace = (1..6).random()
            onTick(randomFace)

            delay(100)
        }
    }
}