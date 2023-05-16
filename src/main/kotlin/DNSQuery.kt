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
            val bytes = mutableListOf<Byte>()
            for (part in parts) {
                bytes.add(part.length.toByte())
                bytes.addAll(part.toByteArray().toList())
            }
            bytes.add(0)
            return bytes.toByteArray()
        }
    }


}