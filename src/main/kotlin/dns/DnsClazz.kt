package dns

@JvmInline
value class DnsClazz(val value: Int) {
    companion object {
        val CLASS_IN = DnsClazz(1)
    }
}