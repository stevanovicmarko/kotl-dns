import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress


fun main(args: Array<String>) {

    val sendData = DNSQuery.buildQuery("example.com", RecordType.A)

    val socket = DatagramSocket()
    socket.broadcast = true
    val sendPacket = DatagramPacket(sendData, sendData.size, InetAddress.getByName("8.8.8.8"), 53)
    socket.send(sendPacket)

    val buffer = ByteArray(2048)
    val packet = DatagramPacket(buffer, buffer.size)
    socket.receive(packet)

    val response = byteArrayOf(*packet.data)
    val dnsParser = DNSParser(response)
    val (header, question) = dnsParser.parse()
    println(header)
    println(question)
}