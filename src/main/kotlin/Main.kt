import dns.DnsQuery
import dns.DnsRecordType



fun main(args: Array<String>) {

    val query = DnsQuery("216.239.32.10", "google.com", DnsRecordType.A)
    val response = query.sendQuery()
    println(response.answers)
}