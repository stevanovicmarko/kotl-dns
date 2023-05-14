@JvmInline
value class RecordType(val value: Int) {
    companion object {
        val A = RecordType(1)
        val NS = RecordType(2)
        val CNAME = RecordType(5)
        val SOA = RecordType(6)
        val PTR = RecordType(12)
        val MX = RecordType(15)
        val TXT = RecordType(16)
        val AAAA = RecordType(28)
        val SRV = RecordType(33)
        val OPT = RecordType(41)
        val A6 = RecordType(38)
        val ANY = RecordType(255)
    }
}