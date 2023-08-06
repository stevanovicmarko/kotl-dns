package dns

data class DnsPacket(val header: DnsHeader,
                     val question: List<DnsQuestion>,
                     val answers: List<DnsRecord>,
                     val authorities: List<DnsRecord>,
                     val additionals: List<DnsRecord>)