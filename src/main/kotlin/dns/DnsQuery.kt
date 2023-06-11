package dns

import DnsParser
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import kotlin.random.Random

class DnsQuery
    (private val ipAddress: String, domainName: String, dnsRecordType: DnsRecordType) {

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


    fun sendQuery(): DnsPacket {
        val sendPacket = DatagramPacket(sendData, sendData.size, InetAddress.getByName(ipAddress), 53)
        socket.send(sendPacket)

        val buffer = ByteArray(1024)
        val packet = DatagramPacket(buffer, buffer.size)
        socket.receive(packet)

        val response = byteArrayOf(*packet.data)
        val dnsParser = DnsParser(response)
        return dnsParser.parse()
    }
}