import dns.DnsQuery
import dns.DnsRecordType

const val DEFAULT_NAMESERVER = "198.41.0.4"
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


fun main(args: Array<String>) {

    val ipAddress = resolve("twitter.com", DnsRecordType.A)
    println(ipAddress)
}