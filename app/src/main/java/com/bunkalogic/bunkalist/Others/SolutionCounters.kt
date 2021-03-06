package com.bunkalogic.bunkalist.Others

import com.bunkalogic.bunkalist.db.Counter
import com.bunkalogic.bunkalist.db.Shard
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.*
import kotlin.math.floor


class SolutionCounters(val db: FirebaseFirestore) {



    // [START create_counter]
    fun createCounter(ref: DocumentReference, numShards: Int): Task<Void> {
        // Initialize the counter document, then initialize each shard.
        return ref.set(Counter(numShards))
            .continueWithTask { task ->
                if (!task.isSuccessful) {
                    throw task.exception!!
                }

                val tasks = arrayListOf<Task<Void>>()

                // Initialize each shard with count=0
                for (i in 0 until numShards) {
                    val makeShard = ref.collection("shards")
                        .document(i.toString())
                        .set(Shard(0))

                    tasks.add(makeShard)
                }

                Tasks.whenAll(tasks)
            }
    }
    // [END create_counter]

    // [START increment_counter]
    fun incrementCounter(ref: DocumentReference, numShards: Int): Task<Void> {
        val shardId = floor(Math.random() * numShards).toInt()
        val shardRef = ref.collection("shards").document(shardId.toString())

        return shardRef.update("count", FieldValue.increment(1))
    }
    // [END increment_counter]

    // [START get_count]
    fun getCount(ref: DocumentReference): Task<Int> {
        // Sum the count of each shard in the subcollection
        return ref.collection("shards").get()
            .continueWith { task ->
                var count = 0
                for (snap in task.result!!) {
                    val shard = snap.toObject(Shard::class.java)
                    count += shard.count
                }
                count
            }
    }
    // [END get_count]
}