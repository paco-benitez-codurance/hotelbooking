package hotelbooking.model

data class Hotel(val hotelId: HotelId) {
    fun has(roomType: RoomType): Boolean {
        TODO("Not yet implemented")
    }

    fun rooms(roomType: RoomType): Int {
        return 0
    }
}
