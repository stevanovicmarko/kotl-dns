
class DNSParser(private val response: ByteArray) {


    fun parseDNHeader(): DNSHeader {
        val items = response.sliceArray(0..11)
        val id = items.sliceArray(0..1).toInteger()
        val flags = items.sliceArray(2..3).toInteger()
        val numQuestions = items.sliceArray(4..5).toInteger()
        val numAnswers = items.sliceArray(6..7).toInteger()
        val numAuthorities = items.sliceArray(8..9).toInteger()
        val numAdditionals = items.sliceArray(10..11).toInteger()
        return DNSHeader(id, flags, numQuestions, numAnswers, numAuthorities, numAdditionals)
    }

    fun parseQuestion(): DNSQuestion {
        val questionPart = response.sliceArray(12 until response.size)
        val parts = mutableListOf<String>()
        var index = 0
        while (questionPart[index] != 0.toByte()) {
            val length = questionPart[index].toInt()
            parts.add(String(questionPart.sliceArray(index + 1..index + length)))
            index += length + 1
        }
        val name = parts.joinToString(".")
        index += 1
        val type = questionPart.sliceArray(index..index + 1).toInteger()
        val clazz = questionPart.sliceArray(index + 2..index + 3).toInteger()
        return DNSQuestion(name.toByteArray(), type, Clazz(clazz))
    }

    fun parse(): Pair<DNSHeader, DNSQuestion> {
        val header = parseDNHeader()
        val question = parseQuestion()
        return Pair(header, question)
    }

}