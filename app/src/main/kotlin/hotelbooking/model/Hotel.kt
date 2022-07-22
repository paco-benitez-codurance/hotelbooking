package hotelbooking.model

data class Hotel(val hotelId: HotelId, val numberOfRoom: Int? = null, val roomType: RoomType? = null) {
    fun has(roomType: RoomType): Boolean {
        return this.roomType == roomType
    }

    fun rooms(roomType: RoomType): Int {
        if(this.roomType != roomType) {
            return 0
        }
        return numberOfRoom ?: 0
    }
}
