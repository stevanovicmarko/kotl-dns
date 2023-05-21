import kotlin.random.Random

class DNSQuery {

    companion object {
        fun buildQuery(domainName: String, recordType: RecordType): ByteArray {
            val name = encodeDnsName(domainName)
            val id = Random.nextInt(0, 65535)
            val recursionDesired = 1.shl(8)
            val header = DNSHeader(id = id, flags = recursionDesired, numQuestions = 1)
            val question = DNSQuestion(name, recordType.value, Clazz.CLASS_IN)

            return header.toBytes() + question.toBytes()
        }

        private fun encodeDnsName(name: String): ByteArray {
            val parts = name.split(".")
            var bytes = byteArrayOf()

            for (part in parts) {
                bytes += part.length.toByte()
                bytes += part.toByteArray()
            }
            bytes += 0.toByte()
            return bytes
        }
    }


}