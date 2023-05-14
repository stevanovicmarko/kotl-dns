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


fun buildQuery(domainName: String, recordType: RecordType): ByteArray {
    val name = encodeDnsName(domainName)
    val id = Random.nextInt(0, 65535)
    val recursionDesired = 1.shl(8)
    val header = DNSHeader(id, recursionDesired, 1)
    val question = DNSQuestion(name, recordType.value, Clazz.CLASS_IN)

    return header.toBytes() + question.toBytes()
}