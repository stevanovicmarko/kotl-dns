package dns

import kotlin.random.Random

class DnsQuery {

    companion object {
        fun buildQuery(domainName: String, dnsRecordType: DnsRecordType): ByteArray {
            val name = encodeDnsName(domainName)
            val id = Random.nextInt(0, 65535)
            val recursionDesired = 1.shl(8)
            val header = DnsHeader(id = id, flags = recursionDesired, numQuestions = 1)
            val question = DnsQuestion(name, dnsRecordType.value, DnsClazz.CLASS_IN)
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