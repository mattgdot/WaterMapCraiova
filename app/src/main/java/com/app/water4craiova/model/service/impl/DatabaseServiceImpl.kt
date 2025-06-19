package com.app.water4craiova.model.service.impl

import android.net.Uri
import android.util.Log
import com.app.water4craiova.model.WaterSource
import com.app.water4craiova.model.service.DatabaseService
import com.app.water4craiova.utils.Constants.ADDED_BY
import com.app.water4craiova.utils.Constants.APPROVED
import com.app.water4craiova.utils.Constants.DATA_COLLECTION
import com.app.water4craiova.utils.Constants.GEOHASH
import com.app.water4craiova.utils.Constants.LATITUDE
import com.app.water4craiova.utils.Constants.LONGITUDE

import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.app
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.UUID
import javax.inject.Inject

class DatabaseServiceImpl @Inject constructor() : DatabaseService {

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getSources(
        latitude: Double,
        longitude: Double,
    ): List<WaterSource> = suspendCancellableCoroutine { cont ->

        val center = GeoLocation(latitude, longitude)
        val radiusInM = 50.0 * 1000.0

        val bounds = GeoFireUtils.getGeoHashQueryBounds(center, radiusInM)
        val tasks: MutableList<Task<QuerySnapshot>> = ArrayList()
        for (b in bounds) {
            val q =
                Firebase.firestore.collection(DATA_COLLECTION).whereEqualTo(APPROVED, true)
                    .orderBy(GEOHASH).startAt(b.startHash).endAt(b.endHash)
            tasks.add(q.get())
        }

        Tasks.whenAllComplete(tasks).addOnCompleteListener {
            val matchingDocs: MutableList<DocumentSnapshot> = ArrayList()
            for (task in tasks) {
                val snap = task.result
                for (doc in snap!!.documents) {
                    val lat = doc.getDouble(LATITUDE)!!
                    val lng = doc.getDouble(LONGITUDE)!!

                    val docLocation = GeoLocation(lat, lng)
                    val distanceInM = GeoFireUtils.getDistanceBetween(docLocation, center)
                    if (distanceInM <= radiusInM) {
                        matchingDocs.add(doc)
                    }
                }
            }
            cont.resume(matchingDocs.map { it.toObject(WaterSource::class.java)!! }) {}
        }.addOnFailureListener {
            cont.resume(emptyList()) {}
        }.addOnCanceledListener {
            cont.cancel()
        }
    }

    override suspend fun addSource(source: WaterSource, onResult: (Throwable?) -> Unit) {
        Firebase.firestore.collection(DATA_COLLECTION).add(
            source
        ).addOnCompleteListener {
            onResult(it.exception)
        }
    }

    override fun getMySources(userId: String): Flow<List<WaterSource>> = callbackFlow {
        val docRef =
            Firebase.firestore.collection(DATA_COLLECTION).whereEqualTo(ADDED_BY, userId)

        val listener = docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val sources = snapshot.toObjects(WaterSource::class.java)
                trySend(sources)
            }
        }

        awaitClose {
            listener.remove()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun uploadImage(uri: Uri): String = suspendCancellableCoroutine { cont ->
        val ref = FirebaseStorage.getInstance().reference.child("images/${UUID.randomUUID()}")
        val uploadTask = ref.putFile(uri)

        val urlTask = uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            ref.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                cont.resume(task.result.toString()) {}
            } else {
                cont.resume("") {}
            }
        }.addOnCanceledListener {
            cont.cancel()
        }
    }
}