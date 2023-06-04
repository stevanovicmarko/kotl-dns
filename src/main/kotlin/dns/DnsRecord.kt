package dns

import java.io.Serializable

data class DnsRecord(val name: String, val type: Int, val clazz: DnsClazz, val ttl: Int, val data: String)