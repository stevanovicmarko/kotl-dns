package dns

import utils.to2ByteArray

data class DnsHeader(
    val id: Int,
    val flags: Int,
    val numQuestions: Int = 0,
    val numAnswers: Int = 0,
    val numAuthorities: Int = 0,
    val numAdditionals: Int = 0
) {
    fun toBytes(): ByteArray = byteArrayOf(
            *id.to2ByteArray(),
            *flags.to2ByteArray(),
            *numQuestions.to2ByteArray(),
            *numAnswers.to2ByteArray(),
            *numAuthorities.to2ByteArray(),
            *numAdditionals.to2ByteArray()
        )
}