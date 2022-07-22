package hotelbooking.model

data class Hotel(val hotelId: HotelId, val numberOfRoom: Int? = null) {
    fun has(roomType: RoomType): Boolean {
        TODO("Not yet implemented")
    }

    fun rooms(roomType: RoomType): Int {
        return numberOfRoom ?: 0
    }
}
