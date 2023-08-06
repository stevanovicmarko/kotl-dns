package utils
fun Int.to2ByteArray(): ByteArray {
    return byteArrayOf(shr(8).toByte(), toByte())
}

