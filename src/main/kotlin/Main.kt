import dns.DnsQuery
import dns.DnsRecordType

const val DEFAULT_NAMSESERVER = "198.41.0.4"
fun resolve(domainName: String, recordType: DnsRecordType): String {
    var nameserver = DEFAULT_NAMSESERVER
    while (true) {
        val query = DnsQuery(domainName, recordType)
        val response = query.sendQuery(nameserver)

        val ip = query.getAnswer(response)
        nameserver = if (ip != null) {
            return ip
        } else if (query.getNameServerIp(response) != null) {
            query.getNameServerIp(response)!!
        } else if (query.getNameServer(response) != null) {
            resolve(query.getNameServer(response)!!, DnsRecordType.A)
        } else {
            throw Exception("No answer found")
        }
    }
}


fun main(args: Array<String>) {

    val ipAddress = resolve("google.com", DnsRecordType.A)
    println(ipAddress)
}