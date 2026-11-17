package ua.frist008.action.record.data.network.record.entity

enum class StreamType(val id: Int) {
    OFF(id = 0),
    ON(id = 2),
    ALIEZ(id = 10),
    LIVESTREAM(id = 11),
    TWITCH(id = 12),
    U_STREAM(id = 13),
    YOUTUBE(id = 14),
    HIT_BOX(id = 15),
    ;

    companion object {

        operator fun get(index: Int): StreamType =
            entries.firstOrNull { it.id == index } ?: OFF
    }
}
