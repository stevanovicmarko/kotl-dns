package dns

@JvmInline
value class DnsRecordType(val value: Int) {
    companion object {
        val A = DnsRecordType(1)
        val NS = DnsRecordType(2)
        val CNAME = DnsRecordType(5)
        val SOA = DnsRecordType(6)
        val PTR = DnsRecordType(12)
        val MX = DnsRecordType(15)
        val TXT = DnsRecordType(16)
        val AAAA = DnsRecordType(28)
        val SRV = DnsRecordType(33)
        val OPT = DnsRecordType(41)
        val A6 = DnsRecordType(38)
        val ANY = DnsRecordType(255)
    }
}