package dns

data class DnsPacket(val header: DnsHeader,
                     val question: List<DnsQuestion>,
                     val answers: List<DnsRecord>,
                     val authorities: List<DnsRecord>,
                     val additionals: List<DnsRecord>) {

    fun ipAddresses(): List<List<UByte>> {
        return answers.filter { it.type == DnsRecordType.A.value }
            .map { it -> it.data.map { it.toUByte() } }
    }
}