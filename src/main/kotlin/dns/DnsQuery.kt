package dns

import DnsParser
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.nio.charset.Charset
import kotlin.random.Random

const val DEFAULT_NAMESERVER = "198.41.0.4"

class DnsQuery
    (domainName: String, dnsRecordType: DnsRecordType) {

    private val sendData: ByteArray
    private val socket = DatagramSocket()

    init {
        socket.broadcast = true
        val name = encodeDnsName(domainName)
        val id = Random.nextInt(0, 65535)
        val header = DnsHeader(id = id, flags = 0, numQuestions = 1)
        val question = DnsQuestion(name, dnsRecordType.value, DnsClazz.CLASS_IN)
        this.sendData = header.toBytes() + question.toBytes()
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

    fun getAnswer(response: DnsPacket): String? = response.answers.find { it.type == DnsRecordType.A.value }?.data

    fun getNameServerIp(response: DnsPacket): String? = response.additionals.find { it.type == DnsRecordType.A.value }?.data

    fun getNameServer(response: DnsPacket): String?  = response.authorities.find { it.type == DnsRecordType.NS.value }?.let {
            return String(it.data.toByteArray(Charset.defaultCharset()), Charsets.UTF_8)
        }


    fun sendQuery(ipAddress: String): DnsPacket {
        val sendPacket = DatagramPacket(sendData, sendData.size, InetAddress.getByName(ipAddress), 53)
        socket.send(sendPacket)

        val buffer = ByteArray(1024)
        val packet = DatagramPacket(buffer, buffer.size)
        socket.receive(packet)

        val response = byteArrayOf(*packet.data)
        val dnsParser = DnsParser(response)
        return dnsParser.parse()
    }

    companion object {
        fun resolve(domainName: String, recordType: DnsRecordType): String {
            var nameserver = DEFAULT_NAMESERVER
            while (true) {
                val query = DnsQuery(domainName, recordType)
                val response = query.sendQuery(nameserver)

                query.getAnswer(response)?.let {
                    return it
                }

                nameserver = query.getNameServerIp(response)
                    ?: query.getNameServer(response)?.let { resolve(it, DnsRecordType.A) }
                            ?: throw Exception("No answer found")
            }
        }
    }
}