import dns.*
import utils.CountingDataInputStream

class DnsParser(private val inputStream: CountingDataInputStream) {

    constructor(response: ByteArray) : this(CountingDataInputStream(response.inputStream()))

    private fun parseDnsHeader(): DnsHeader {

        val id = inputStream.readShortToInt()
        val flags = inputStream.readShortToInt()
        val numQuestions = inputStream.readShortToInt()
        val numAnswers = inputStream.readShortToInt()
        val numAuthorities = inputStream.readShortToInt()
        val numAdditionals = inputStream.readShortToInt()

        return DnsHeader(id, flags, numQuestions, numAnswers, numAuthorities, numAdditionals)
    }

    private fun parseQuestion(): DnsQuestion {
        val parts = mutableListOf<String>()

        do {
            val length = inputStream.read()
            parts.add(inputStream.readNBytes(length).toString(Charsets.UTF_8))
        } while (length.toByte() != 0.toByte())

        val name = parts.filter { it.isNotEmpty() }.joinToString(".")

        val type = inputStream.readShort().toInt()
        val clazz = inputStream.readShort().toInt()
        return DnsQuestion(name.toByteArray(), type, DnsClazz(clazz))
    }

    private fun decodeCompressedName(length: Int): String {
        val pointer = (length and 0b00111111) + inputStream.read()
        inputStream.reset()
        inputStream.skip(pointer.toLong())
        return decodeName()
    }

    private fun decodeName(): String {
        val parts = mutableListOf<String>()

        do {
            val length = inputStream.read()
            if ((length and 0b11000000) != 0) {
                val name = decodeCompressedName(length)
                parts.add(name)
                break
            } else {
                parts.add(inputStream.readNBytes(length).toString(Charsets.UTF_8))
            }
        } while (length.toByte() != 0.toByte())

        return parts.filter { it.isNotEmpty() }.joinToString(".")
    }

    private fun parseRecord(): DnsRecord {
        val count = inputStream.count
        val name = decodeName()
        inputStream.reset()
        inputStream.skip((count + 2).toLong())
        val type = inputStream.readShort().toInt()
        val clazz = inputStream.readShort().toInt()
        val ttl = inputStream.readInt()
        val dataLength = inputStream.readShort().toInt()
        val data = inputStream.readNBytes(dataLength)
        return DnsRecord(name, type, DnsClazz(clazz), ttl, data)
    }

    fun parse(): DnsPacket {
        val header = parseDnsHeader()
        val questions = (0 until header.numQuestions).map { parseQuestion() }
        val answers = (0 until header.numAnswers).map { parseRecord() }
        val authorities = (0 until header.numAuthorities).map { parseRecord() }
        val additionals = (0 until header.numAdditionals).map { parseRecord() }
        return DnsPacket(header, questions, answers, authorities, additionals)
    }

}