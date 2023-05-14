
data class DNSQuestion(
    val name: ByteArray,
    val type: Int,
    val clazz: Clazz
) {
    fun toBytes(): ByteArray = byteArrayOf(
        *name,
        *type.to2ByteArray(),
        *clazz.value.to2ByteArray(),
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DNSQuestion

        if (!name.contentEquals(other.name)) return false
        if (type != other.type) return false
        return clazz == other.clazz
    }

    override fun hashCode(): Int {
        var result = name.contentHashCode()
        result = 31 * result + type
        result = 31 * result + clazz.hashCode()
        return result
    }
}
