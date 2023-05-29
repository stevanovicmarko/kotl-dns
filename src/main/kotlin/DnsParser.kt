import dns.*
import utils.CountingDataInputStream

class DnsParser(private val inputStream: CountingDataInputStream) {

    constructor(response: ByteArray) : this(CountingDataInputStream(response.inputStream()))

    private fun parseDNHeader(): DnsHeader {

        val id = inputStream.readShort().toInt()
        val flags = inputStream.readShort().toInt()
        val numQuestions = inputStream.readShort().toInt()
        val numAnswers = inputStream.readShort().toInt()
        val numAuthorities = inputStream.readShort().toInt()
        val numAdditionals = inputStream.readShort().toInt()

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
//        println("Name: $name, Type: $type, Class: $clazz, TTL: $ttl, Data Length: $dataLength")
        val data = inputStream.readNBytes(dataLength)
        return DnsRecord(name, type, DnsClazz(clazz), ttl, data)
    }

    fun parse(): Triple<DnsHeader, DnsQuestion, DnsRecord> {
        val header = parseDNHeader()
        val question = parseQuestion()
        val record = parseRecord()
        return Triple(header, question, record)
    }

}