package com.example.burmalda67.data

// Заглушки вместо данных с сервера

data class Player(val nickname: String, val chips: Int)

data class PokerTable(
    val code: String,
    val name: String,
    val blinds: String,
    val seatsTaken: Int,
    val seatsTotal: Int,
)

object MockData {
    val player = Player(nickname = "Burmalda", chips = 25400)

    val tables = listOf(
        PokerTable("ACE777", "Стол «Бурмалда»", "10/20", 4, 6),
        PokerTable("42BRO", "42 братухи", "50/100", 5, 6),
        PokerTable("ENDOVA", "Фанаты стихов Ендовицкого", "100/200", 8, 9),
        PokerTable("CHEREMSHA", "Тихо не спеша черемша", "5/10", 1, 6),
    )
}
