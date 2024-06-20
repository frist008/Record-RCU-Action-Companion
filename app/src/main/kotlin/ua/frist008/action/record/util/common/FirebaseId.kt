package ua.frist008.action.record.util.common

import com.google.android.gms.common.util.MurmurHash3
import com.google.firebase.installations.FirebaseInstallations
import kotlinx.coroutines.tasks.await
import timber.log.Timber

object FirebaseId {

    private val firebase by lazy { FirebaseInstallations.getInstance() }

    private var idHashArr: ByteArray? = null

    suspend fun getIdMurmurHash3(): ByteArray? =
        idHashArr ?: runCatching {
            val id = firebase.id.await()
            val idArr = id.toByteArray()
            val hash = MurmurHash3.murmurhash3_x86_32(idArr, 0, idArr.size, 0)
            val idHashArr = byteArrayOf(
                (hash shr (Byte.SIZE_BITS * 3)).toByte(),
                (hash shr (Byte.SIZE_BITS * 2)).toByte(),
                (hash shr Byte.SIZE_BITS).toByte(),
                hash.toByte(),
            ).also(this::idHashArr::set)

            idHashArr
        }.onFailure { Timber.e(it) }.getOrNull()
}
