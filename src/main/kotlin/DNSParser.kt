import java.io.ByteArrayInputStream


class DNSParser(private val inputStream: ByteArrayInputStream) {

    constructor(response: ByteArray) : this(ByteArrayInputStream(response))

    private fun parseDNHeader(): DNSHeader {

        val id = inputStream.readNBytes(2).toInteger()
        val flags = inputStream.readNBytes(2).toInteger()
        val numQuestions = inputStream.readNBytes(2).toInteger()
        val numAnswers = inputStream.readNBytes(2).toInteger()
        val numAuthorities = inputStream.readNBytes(2).toInteger()
        val numAdditionals = inputStream.readNBytes(2).toInteger()

        return DNSHeader(id, flags, numQuestions, numAnswers, numAuthorities, numAdditionals)
    }

    private fun parseQuestion(): DNSQuestion {
        val parts = mutableListOf<String>()

        do {
            val length = inputStream.read()
            parts.add(inputStream.readNBytes(length).toString(Charsets.UTF_8))
        } while (length.toByte() != 0.toByte())

        val name = parts.filter { it.isNotEmpty() }.joinToString(".")

        val type = inputStream.readNBytes(2).toInteger()
        val clazz = inputStream.readNBytes(2).toInteger()
        println(name)
        return DNSQuestion(name.toByteArray(), type, Clazz(clazz))
    }

    private fun decodeCompressedName(length: Int): String {
        val pointer = (length and 0b00111111) + inputStream.read()
        inputStream.reset()
        inputStream.skip(pointer.toLong())
        val name = decodeName()
        return name
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

    private fun parseRecord() {
        val name = decodeName()
        // TODO: Input steam positioning is broken after decodeName()
        inputStream.reset()
        inputStream.skip(31)
        val type = inputStream.readNBytes(2).toInteger()
        val clazz = inputStream.readNBytes(2).toInteger()
        val ttl = inputStream.readNBytes(4).toInteger()
        val dataLength = inputStream.readNBytes(2).toInteger()
        println("Name: $name, Type: $type, Class: $clazz, TTL: $ttl, Data Length: $dataLength")
    }

    fun parse(): Pair<DNSHeader, DNSQuestion> {
        val header = parseDNHeader()
        val question = parseQuestion()
        parseRecord()
        return Pair(header, question)
    }

}