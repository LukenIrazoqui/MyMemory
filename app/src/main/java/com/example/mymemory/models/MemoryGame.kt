package com.example.mymemory.models

import com.example.mymemory.utils.DEFAULT_ICONS

class MemoryGame(private val boardSize: BoardSize, private val customGameImages: List<String>?) {

    val cards: List<MemoryCard>
    var numPairsFound = 0

    private var numFlips = 0
    private var indexOfSelectedCard: Int? = null

    init {
        if (customGameImages == null) {
            val chosenImages = DEFAULT_ICONS.shuffled().take(boardSize.getNumPairs())
            val randomizedImages = (chosenImages + chosenImages).shuffled()

            cards = randomizedImages.map { MemoryCard(it) }
        } else {
            val randomizedImages = (customGameImages + customGameImages).shuffled()

            cards = randomizedImages.map { MemoryCard(it.hashCode(), it) }
        }
    }

    fun flipCard(position: Int): Boolean {
        val card = cards[position]

        var foundMatch = false
        if (indexOfSelectedCard == null) {
            restoreCards()
            indexOfSelectedCard = position
        } else {
            foundMatch = checkForMatch(indexOfSelectedCard!!, position)
            indexOfSelectedCard = null
            numFlips++
        }
        card.isFaceUp = !card.isFaceUp
        return foundMatch
    }

    private fun checkForMatch(position1: Int, position2: Int): Boolean {
        if (cards[position1].identifier != cards[position2].identifier) {
            return false
        }
        cards[position2].isMatched = true
        cards[position1].isMatched = true
        numPairsFound++
        return true
    }

    private fun restoreCards() {
        for (card in cards) {
            if (!card.isMatched) {
                card.isFaceUp = false
            }
        }
    }

    fun haveWonGame(): Boolean {
        return numPairsFound == boardSize.getNumPairs()
    }

    fun isCardFaceUp(position: Int): Boolean {
        return cards[position].isFaceUp
    }

    fun getMoves(): Int {
        return numFlips
    }

}