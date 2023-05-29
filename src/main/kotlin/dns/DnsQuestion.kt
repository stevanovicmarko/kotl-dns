package dns

import utils.to2ByteArray

data class DnsQuestion(
    val name: ByteArray,
    val type: Int,
    val dnsClazz: DnsClazz
) {
    fun toBytes(): ByteArray = byteArrayOf(
        *name,
        *type.to2ByteArray(),
        *dnsClazz.value.to2ByteArray(),
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DnsQuestion

        if (!name.contentEquals(other.name)) return false
        if (type != other.type) return false
        return dnsClazz == other.dnsClazz
    }

    override fun hashCode(): Int {
        var result = name.contentHashCode()
        result = 31 * result + type
        result = 31 * result + dnsClazz.hashCode()
        return result
    }
}
