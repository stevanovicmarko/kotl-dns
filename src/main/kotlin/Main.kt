import dns.DnsQuery
import dns.DnsRecordType
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val url = args.first()
    if (url.isEmpty()) {
        println("Please provide a URL to resolve")
        exitProcess(0)
    }
    runCatching {
        val ipAddress = DnsQuery.resolve(url, DnsRecordType.A)
        println(ipAddress)
    }.onFailure {
        println("Failed to resolve '$url'")
    }

}