//import java.net.DatagramPacket
//import java.net.DatagramSocket
//import java.net.InetAddress



fun main(args: Array<String>) {

    val sendData = buildQuery("example.com", RecordType.A)
    //    println(DNSHeader(0x1314, 0, 1).toBytes().joinToString("") { it.toUByte().toString(16) })

    println(sendData.joinToString("") { it.toUByte().toString(16) })
//    val socket = DatagramSocket()
//    socket.broadcast = true
//    val sendPacket = DatagramPacket(sendData, sendData.size,
//        InetAddress.getByName("8.8.8.8"), 53)
//    socket.send(sendPacket)
//
//    val buffer = ByteArray(2048)
//    val packet = DatagramPacket(buffer, buffer.size)
//    socket.receive(packet)
//    val response = packet.data.decodeToString()
//    println(response)
}