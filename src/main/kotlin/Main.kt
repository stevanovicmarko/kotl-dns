import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress


fun main(args: Array<String>) {

    val sendData = buildQuery("www.example.com", RecordType.A)

    val socket = DatagramSocket()
    socket.broadcast = true
    val sendPacket = DatagramPacket(sendData, sendData.size, InetAddress.getByName("8.8.8.8"), 53)
    socket.send(sendPacket)

    val buffer = ByteArray(2048)
    val packet = DatagramPacket(buffer, buffer.size)
    socket.receive(packet)
    val response = byteArrayOf(*packet.data)
    val header = parseDNHeader(response)
    println(header)
    val questionPart = response.sliceArray(12 until response.size)
    val question = parseQuestion(questionPart)
    println(question)
}