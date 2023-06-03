import dns.DnsQuery
import dns.DnsRecordType
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress


fun main(args: Array<String>) {

    val sendData = DnsQuery.buildQuery("www.example.com", DnsRecordType.A)

    val socket = DatagramSocket()
    socket.broadcast = true
    val sendPacket = DatagramPacket(sendData, sendData.size, InetAddress.getByName("8.8.8.8"), 53)
    socket.send(sendPacket)

    val buffer = ByteArray(1024)
    val packet = DatagramPacket(buffer, buffer.size)
    socket.receive(packet)

    val response = byteArrayOf(*packet.data)
    val dnsParser = DnsParser(response)
    val dnsPacket = dnsParser.parse()
    println(dnsPacket)
    println(dnsPacket.ipAddresses())
}