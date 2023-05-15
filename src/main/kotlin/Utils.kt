import kotlin.random.Random

fun Int.to2ByteArray(): ByteArray {
    return byteArrayOf(shr(8).toByte(), toByte())
}


fun encodeDnsName(name: String): ByteArray {
    val parts = name.split(".")
    val bytes = mutableListOf<Byte>()
    for (part in parts) {
        bytes.add(part.length.toByte())
        bytes.addAll(part.toByteArray().toList())
    }
    bytes.add(0)
    return bytes.toByteArray()
}

fun ByteArray.toInteger(): Int {
    var result = 0
    var shift = 0
    for (byte in this.reversed()) {
        result = result or (byte.toInt() shl shift)
        shift += 8
    }
    return result
}

fun parseDNHeader(header: ByteArray): DNSHeader {
    val items = header.sliceArray(0..11)
    val id = items.sliceArray(0..1).toInteger()
    val flags = items.sliceArray(2..3).toInteger()
    val numQuestions = items.sliceArray(4..5).toInteger()
    val numAnswers = items.sliceArray(6..7).toInteger()
    val numAuthorities = items.sliceArray(8..9).toInteger()
    val numAdditionals = items.sliceArray(10..11).toInteger()
    return DNSHeader(id, flags, numQuestions, numAnswers, numAuthorities, numAdditionals)}

fun parseQuestion(question: ByteArray): DNSQuestion {
    val parts = mutableListOf<String>()
    var index = 0
    while (question[index] != 0.toByte()) {
        val length = question[index].toInt()
        parts.add(String(question.sliceArray(index + 1 .. index + length)))
        index += length + 1
    }
    val name = parts.joinToString(".")
    index += 1
    val type = question.sliceArray(index  .. index + 1).toInteger()
    val clazz = question.sliceArray(index + 2 .. index + 3).toInteger()
    return DNSQuestion(name.toByteArray(), type, Clazz(clazz))
}


fun buildQuery(domainName: String, recordType: RecordType): ByteArray {
    val name = encodeDnsName(domainName)
    val id = Random.nextInt(0, 65535)
    val recursionDesired = 1.shl(8)
    val header = DNSHeader(id = id, flags = recursionDesired, numQuestions = 1)
    val question = DNSQuestion(name, recordType.value, Clazz.CLASS_IN)

    return header.toBytes() + question.toBytes()
}