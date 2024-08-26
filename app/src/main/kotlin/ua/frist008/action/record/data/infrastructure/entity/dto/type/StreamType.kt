package ua.frist008.action.record.data.infrastructure.entity.dto.type

enum class StreamType(val id: Int) {
    OFF(0),
    ON(2),
    ALIEZ(10),
    LIVESTREAM(11),
    TWITCH(12),
    U_STREAM(13),
    YOUTUBE(14),
    HIT_BOX(15),
    ;

    companion object {

        operator fun get(index: Int): StreamType =
            entries.firstOrNull { it.id == index } ?: OFF
    }
}
