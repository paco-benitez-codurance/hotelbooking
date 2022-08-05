package hotelbooking.model

data class Hotel(val hotelId: HotelId, val roomTypes: Map<RoomType, Int> = emptyMap()) {

    constructor(hotelId: HotelId, numberOfRoom: Int, roomType: RoomType) : this(
        hotelId,
        mapOf(roomType to numberOfRoom)
    )

    fun has(roomType: RoomType): Boolean {
        return this.roomTypes.containsKey(roomType)
    }

    fun rooms(roomType: RoomType): Int {
        return this.roomTypes[roomType] ?: 0
    }
}
