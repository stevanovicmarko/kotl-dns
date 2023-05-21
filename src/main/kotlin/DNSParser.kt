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

        return  DNSHeader(id, flags, numQuestions, numAnswers, numAuthorities, numAdditionals)
    }

    private fun parseQuestion(): DNSQuestion {
        val parts = mutableListOf<String>()

        do {
            val length = inputStream.read()
            parts.add(inputStream.readNBytes(length).toString(Charsets.UTF_8))
        } while (length.toByte() != 0.toByte())

        val name = parts.joinToString(".")

        val type = inputStream.readNBytes(2).toInteger()
        val clazz = inputStream.readNBytes(2).toInteger()

        return DNSQuestion(name.toByteArray(), type, Clazz(clazz))
    }

    private fun parseRecord() {
        // TODO: Implement record parsing
    }

    fun parse(): Pair<DNSHeader, DNSQuestion> {
        val header = parseDNHeader()
        val question = parseQuestion()
        val record = parseRecord()
        return Pair(header, question)
    }

}