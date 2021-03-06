package com.bunkalogic.bunkalist.Fragments


import android.app.Notification
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bunkalogic.bunkalist.Activities.DetailsActivities.ListFollowsActivity
import com.bunkalogic.bunkalist.Activities.UserProfileActivities.ProfileListActivity
import com.bunkalogic.bunkalist.Adapters.ProfileListAdapter
import com.bunkalogic.bunkalist.Notification.NotificationHandler
import com.bunkalogic.bunkalist.Others.Constans

import com.bunkalogic.bunkalist.R
import com.bunkalogic.bunkalist.RxBus.RxBus
import com.bunkalogic.bunkalist.SharedPreferences.preferences
import com.bunkalogic.bunkalist.db.ItemListRating
import com.bunkalogic.bunkalist.db.NewListRating
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_list_profile.view.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.fragment_topandreview.view.*
import org.jetbrains.anko.support.v4.intentFor
import org.jetbrains.anko.support.v4.toast



/**
 *  Created by @author Naim Dridi on 25/02/19
 */


class ProfileFragment : androidx.fragment.app.Fragment() {

    private lateinit var _view: View
    val userToken = 1

    private var listProfileitem: ArrayList<ItemListRating> = ArrayList()
    private lateinit var adapter: ProfileListAdapter

    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private lateinit var currentUser: FirebaseUser

    private val store: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var addItemListDBRef: CollectionReference

    private var itemRatingSubscription: ListenerRegistration? = null
    private lateinit var itemRatingBusListener: Disposable


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _view = inflater.inflate(R.layout.fragment_profile, container, false)
        fixScrollPosition()
        setUpAddListDB()
        setUpCurrentUser()


        setUpCurrentUserUI()
        onClick()
        setUpRecycler()

        subscribeToProfileAllList()
        addToNewItemRating()


        return _view
    }

    private fun fixScrollPosition(){
        // This function ensures that when the fragment is loaded, it is displayed from the first position
        _view.scrollView6.isFocusableInTouchMode = true
        _view.scrollView6.descendantFocusability = ViewGroup.FOCUS_BEFORE_DESCENDANTS
    }

    // Initializing the currentUser
    private fun setUpCurrentUser(){
        currentUser = mAuth.currentUser!!
    }

    // Creating the name instance in the database
    private fun setUpAddListDB(){
        addItemListDBRef = store.collection("Users/${preferences.userId}/RatingList")
    }

    // Creating the new instance in the database
    private fun saveItemRatingList(itemListRating: ItemListRating){
        addItemListDBRef.add(itemListRating)
            .addOnCompleteListener {
                toast("Add in your list")
                Log.d("ProfileFragment", "itemRating added on firestore")
            }
            .addOnFailureListener {
                toast("Fail add in your list")
                Log.d("ProfileFragment", "itemRating error not added on firestore")
            }
    }

    // This is responsible for giving me the full list
    private fun subscribeToProfileAllList(){
        itemRatingSubscription = addItemListDBRef
            .orderBy("addDate", Query.Direction.DESCENDING)
            .limit(10)
            .addSnapshotListener(object : java.util.EventListener, EventListener<QuerySnapshot> {
                override fun onEvent(snapshot: QuerySnapshot?, exception: FirebaseFirestoreException?) {
                    exception?.let {
                        Log.d("ProfileFragment", "exception")
                        return
                    }

                    snapshot?.let {
                        listProfileitem.clear()
                        val itemRating = it.toObjects(ItemListRating::class.java)
                        listProfileitem.addAll(itemRating)
                        adapter.notifyDataSetChanged()
                        _view.recyclerProfileAll.smoothScrollToPosition(0)


                    }
                }

            })
    }

    private fun setUpRecycler(){
        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)


        adapter = ProfileListAdapter(context!!, listProfileitem)

        _view.recyclerProfileAll.setHasFixedSize(true)
        _view.recyclerProfileAll.layoutManager = layoutManager
        _view.recyclerProfileAll.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
        _view.recyclerProfileAll.adapter = adapter
    }

    private fun setUpCurrentUserUI(){

        _view.userNameProfile.text = currentUser.displayName


        if (currentUser.photoUrl.toString().isEmpty()){
            Glide.with(this)
                .load(R.drawable.ic_person_black_24dp)
                .placeholder(R.drawable.ic_person_black_24dp)
                .override(160, 160)
                .into(_view.userImageProfile)
        }else{
            Glide.with(this)
                .load(currentUser.photoUrl)
                .override(160, 160)
                .into(_view.userImageProfile)
        }



        _view.numberMovie.text = "Movies views: " + preferences.sizeMovies
        _view.numberSeries.text = "Series views: " + preferences.sizeSeries
        _view.numberAnime.text = "Anime views: " + preferences.sizeAnime

        _view.textViewFollowsNumbers.text = preferences.follows.toString()
        _view.textViewFollowersNumbers.text = preferences.followers.toString()

    }

    //private fun setUpOtherUser(username: String, userPhoto: String){
    //    _view.userNameProfile.text = username
//
    //    Glide.with(this)
    //        .load(userPhoto)
    //        .override(160, 160)
    //        .into(_view.userImageProfile)
    //}

    fun onClick(){
        _view.buttonListAll.setOnClickListener { startActivity(intentFor<ProfileListActivity>("list" to 1)) }
        _view.buttonTopFavsAll.setOnClickListener { startActivity(intentFor<ProfileListActivity>("top" to 2)) }

        _view.textViewFollows.setOnClickListener { startActivity(intentFor<ListFollowsActivity>("follow" to 1)) }

        _view.textViewFollowers.setOnClickListener { startActivity(intentFor<ListFollowsActivity>("followers" to 2)) }

    }

    private fun addToNewItemRating(){
        itemRatingBusListener = RxBus.listen(NewListRating::class.java).subscribe {
            saveItemRatingList(it.itemListRating)
        }
    }

    //companion object {
    //    @JvmStatic
    //    fun newInstance(userId: String, username: String, userPhoto: String){
    //        ProfileFragment().apply {
    //            arguments = Bundle().apply {
    //                putString(Constans.USER_PROFILE, userId)
    //                putString(Constans.USER_PROFILE, username)
    //                putString(Constans.USER_PROFILE, userPhoto)
    //            }
    //
    //        }
    //    }
    //}



    override fun onDestroyView() {
        itemRatingBusListener.dispose()
        itemRatingSubscription?.remove()
        super.onDestroyView()
    }




}
