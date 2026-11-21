package ua.frist008.action.record.data.network

import com.google.android.gms.common.util.MurmurHash3
import com.google.firebase.FirebaseException
import com.google.firebase.installations.FirebaseInstallations
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeoutOrNull
import timber.log.Timber
import ua.frist008.action.record.core.util.common.ByteConst
import kotlin.time.Duration.Companion.seconds

object FirebaseId {

    private val firebase by lazy { FirebaseInstallations.getInstance() }

    private var idHashArr: ByteArray? = null

    /**
     * Gives the id of the phone in the radar request: four bytes of MurmurHash3 of the Firebase
     * Installation ID.
     *
     * @return the four bytes, or `null` when the ID failed or did not come within the timeout, and
     * the request goes with zeros in their place.
     */
    suspend fun getIdMurmurHash3(): ByteArray? =
        idHashArr ?: loadId()?.let(::hashId)?.also { idHashArr = it }

    private suspend fun loadId(): String? =
        try {
            val idTimeout = 3.seconds
            val id = withTimeoutOrNull(idTimeout) { firebase.id.await() }

            if (id == null) {
                Timber.w("timeout")
            }

            id
        } catch (e: FirebaseException) {
            Timber.e(e)
            null
        }

    private fun hashId(id: String): ByteArray {
        val idArr = id.toByteArray()
        val hash = MurmurHash3.murmurhash3_x86_32(idArr, 0, idArr.size, 0)

        return byteArrayOf(
            (hash shr ByteConst.BITS_3).toByte(),
            (hash shr ByteConst.BITS_2).toByte(),
            (hash shr ByteConst.BITS_1).toByte(),
            hash.toByte(),
        )
    }
}
