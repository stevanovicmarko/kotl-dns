
fun Int.to2ByteArray(): ByteArray {
    return byteArrayOf(shr(8).toByte(), toByte())
}


fun ByteArray.toInteger(): Int {
    // TODO: This is maybe not correct
    var result = 0
    var shift = 0
    for (byte in this.reversed()) {
        result = result or (byte.toInt() shl shift)
        shift += 8
    }
    return result
}
