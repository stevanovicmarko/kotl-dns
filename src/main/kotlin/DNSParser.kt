import java.io.DataInputStream


class DNSParser(private val inputStream: DataInputStream) {

    constructor(response: ByteArray) : this(DataInputStream(response.inputStream()))

    private fun parseDNHeader(): DNSHeader {

        val id = inputStream.readShort().toInt()
        val flags = inputStream.readShort().toInt()
        val numQuestions = inputStream.readShort().toInt()
        val numAnswers = inputStream.readShort().toInt()
        val numAuthorities = inputStream.readShort().toInt()
        val numAdditionals = inputStream.readShort().toInt()

        return DNSHeader(id, flags, numQuestions, numAnswers, numAuthorities, numAdditionals)
    }

    private fun parseQuestion(): DNSQuestion {
        val parts = mutableListOf<String>()

        do {
            val length = inputStream.read()
            parts.add(inputStream.readNBytes(length).toString(Charsets.UTF_8))
        } while (length.toByte() != 0.toByte())

        val name = parts.filter { it.isNotEmpty() }.joinToString(".")

        val type = inputStream.readShort().toInt()
        val clazz = inputStream.readShort().toInt()
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
        val type = inputStream.readShort().toInt()
        val clazz = inputStream.readShort().toInt()
        val ttl = inputStream.readInt()
        val dataLength = inputStream.readShort().toInt()
        println("Name: $name, Type: $type, Class: $clazz, TTL: $ttl, Data Length: $dataLength")
    }

    fun parse(): Pair<DNSHeader, DNSQuestion> {
        val header = parseDNHeader()
        val question = parseQuestion()
        parseRecord()
        return Pair(header, question)
    }

}