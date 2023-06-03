package utils

import java.io.ByteArrayInputStream
import java.io.DataInputStream
import java.nio.ByteBuffer

class CountingDataInputStream(inputStream: ByteArrayInputStream) {
    var count = 0
        private set

    private val dataInputStream = DataInputStream(inputStream)

    fun read(): Int {
        count++
        return dataInputStream.read()
    }

    fun readNBytes(len: Int): ByteArray {
        count += len
        return dataInputStream.readNBytes(len)
    }

    fun readInt(): Int {
        count += 4
        return dataInputStream.readInt()
    }

    fun readShort(): Short {
        count += 2
        return dataInputStream.readShort()
    }

    fun reset() {
        count = 0
        dataInputStream.reset()
    }

    fun skip(n: Long) {
        count += n.toInt()
        dataInputStream.skip(n)
    }

    fun readShortToInt(): Int = ByteBuffer.wrap(byteArrayOf(0, 0, *this.readNBytes(2))).getInt()
}