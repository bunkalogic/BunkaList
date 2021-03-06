package com.bunkalogic.bunkalist.Adapters

import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.bunkalogic.bunkalist.Activities.DetailsActivities.ItemDetailsActivity
import com.bunkalogic.bunkalist.Others.Constans
import com.bunkalogic.bunkalist.R
import com.bunkalogic.bunkalist.Retrofit.Callback.OnGetMovieCallback
import com.bunkalogic.bunkalist.Retrofit.Callback.OnGetSeriesCallback
import com.bunkalogic.bunkalist.Retrofit.Response.Movies.Movie
import com.bunkalogic.bunkalist.Retrofit.Response.SeriesAndAnime.Series
import com.bunkalogic.bunkalist.SharedPreferences.preferences
import com.bunkalogic.bunkalist.ViewModel.ViewModelAPItmdb
import com.bunkalogic.bunkalist.db.ItemListRating
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import org.jetbrains.anko.intentFor
import java.text.SimpleDateFormat



class ProfileOtherListAdapter(private val ctx: Context, private var mValues: MutableList<ItemListRating>): androidx.recyclerview.widget.RecyclerView.Adapter<ProfileOtherListAdapter.ViewHolder>(){

    var searchViewModelAPItmdb: ViewModelAPItmdb? = null


    init {
        searchViewModelAPItmdb = ViewModelProviders.of(ctx as androidx.fragment.app.FragmentActivity).get(ViewModelAPItmdb::class.java)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileOtherListAdapter.ViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.list_profile_other_fragement_item, parent, false)

        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listRating = mValues[position]

            val numPosition = position + 1

            holder.numPosition.text = "$numPosition."
            holder.dateAt.text = SimpleDateFormat("dd.MM.yyyy").format(listRating.addDate)
            holder.yourRating.text = listRating.finalRate.toString()

            holder.yourRatingFilter.visibility = View.GONE
            holder.imageFilter.visibility = View.GONE



            // is responsible for checking the list has been filtered and if so load the corresponding notes
            if (preferences.ratingId != 0){
                when(preferences.ratingId){
                    Constans.filter_rating_story -> {
                        holder.imageFinalRate.visibility = View.GONE
                        holder.yourRating.visibility = View.GONE
                        holder.yourRatingFilter.visibility = View.VISIBLE
                        holder.imageFilter.visibility = View.VISIBLE
                        // We add the corresponding data
                        holder.yourRatingFilter.text = listRating.historyRate.toString()
                        Glide.with(ctx)
                            .load(R.drawable.ic_filter_rating_story)
                            .override(60, 60)
                            .into(holder.imageFilter)

                    }
                    Constans.filter_rating_characters -> {
                        holder.imageFinalRate.visibility = View.GONE
                        holder.yourRating.visibility = View.GONE
                        holder.yourRatingFilter.visibility = View.VISIBLE
                        holder.imageFilter.visibility = View.VISIBLE
                        // We add the corresponding data
                        holder.yourRatingFilter.text = listRating.characterRate.toString()
                        Glide.with(ctx)
                            .load(R.drawable.ic_filter_rating_characters)
                            .override(60, 60)
                            .into(holder.imageFilter)


                    }
                    Constans.filter_rating_soundtrack -> {
                        holder.imageFinalRate.visibility = View.GONE
                        holder.yourRating.visibility = View.GONE
                        holder.yourRatingFilter.visibility = View.VISIBLE
                        holder.imageFilter.visibility = View.VISIBLE
                        // We add the corresponding data
                        holder.yourRatingFilter.text = listRating.soundtrackRate.toString()
                        Glide.with(ctx)
                            .load(R.drawable.ic_filter_rating_soundtrack)
                            .override(60, 60)
                            .into(holder.imageFilter)
                    }
                    Constans.filter_rating_photography -> {
                        holder.imageFinalRate.visibility = View.GONE
                        holder.yourRating.visibility = View.GONE
                        holder.yourRatingFilter.visibility = View.VISIBLE
                        holder.imageFilter.visibility = View.VISIBLE
                        // We add the corresponding data
                        holder.yourRatingFilter.text = listRating.effectsRate.toString()
                        Glide.with(ctx)
                            .load(R.drawable.ic_filter_rating_effects)
                            .override(60, 60)
                            .into(holder.imageFilter)

                    }
                    Constans.filter_rating_enjoyment -> {
                        holder.imageFinalRate.visibility = View.GONE
                        holder.yourRating.visibility = View.GONE
                        holder.yourRatingFilter.visibility = View.VISIBLE
                        holder.imageFilter.visibility = View.VISIBLE
                        // We add the corresponding data
                        holder.yourRatingFilter.text = listRating.enjoymentRate.toString()
                        Glide.with(ctx)
                            .load(R.drawable.ic_filter_rating_enjoyment)
                            .override(60, 60)
                            .into(holder.imageFilter)

                    }
                    Constans.filter_rating_final ->{
                        holder.imageFinalRate.visibility = View.VISIBLE
                        holder.yourRating.visibility = View.VISIBLE
                        holder.yourRatingFilter.visibility = View.GONE
                        holder.imageFilter.visibility = View.GONE
                        holder.yourRating.text = listRating.finalRate.toString()
                    }
                }
            }else{
                holder.imageFinalRate.visibility = View.VISIBLE
                holder.yourRating.visibility = View.VISIBLE
                holder.yourRatingFilter.visibility = View.GONE
                holder.imageFilter.visibility = View.GONE
                holder.yourRating.text = listRating.finalRate.toString()
            }






            val type = listRating.typeOeuvre
            val idItem = listRating.oeuvreId
            val mediaTypeTV = "tv"
            val mediaTypeMovie = "movie"

            // check which value has type to load movie or series or anime
            if (type == Constans.MOVIE_LIST){
                searchViewModelAPItmdb!!.getMovie(idItem!!,object :
                    OnGetMovieCallback {
                    override fun onSuccess(movie: Movie) {
                        holder.title.text = movie.title
                        holder.dateRelease.text = movie.releaseDate?.split("-")?.get(0) ?: movie.releaseDate
                        //holder.description.text = movie.overview
                        holder.globalRating.text = movie.voteAverage.toString()

                        Glide.with(ctx)
                            .load(Constans.API_MOVIE_SERIES_ANIME_BASE_URL_IMG_PATH_POSTER + movie.posterPath)
                            .into(holder.imagePoster)

                        val name = movie.title


                        // is responsible for collecting the data to load in the ItemDetailsActivity
                        holder.itemView.setOnClickListener {
                            ctx.startActivity(ctx.intentFor<ItemDetailsActivity>(
                                "id" to idItem,
                                "type" to mediaTypeMovie,
                                "name" to name
                            ))
                        }


                    }

                    override fun onError() {
                        Log.d("ProfileListAdapter", "Error Movie try Again")
                    }

                })
            }else if (type == Constans.SERIE_LIST){
                searchViewModelAPItmdb!!.getSeriesAndAnime(idItem!!, object :
                    OnGetSeriesCallback {
                    override fun onSuccess(series: Series) {
                        holder.title.text = series.name
                        holder.dateRelease.text = series.firstAirDate?.split("-")?.get(0) ?: series.firstAirDate
                        //holder.description.text = series.overview
                        holder.globalRating.text = series.voteAverage.toString()

                        Glide.with(ctx)
                            .load(Constans.API_MOVIE_SERIES_ANIME_BASE_URL_IMG_PATH_POSTER + series.posterPath)
                            .into(holder.imagePoster)

                        val name = series.name


                        // is responsible for collecting the data to load in the ItemDetailsActivity
                        holder.itemView.setOnClickListener {
                            ctx.startActivity(ctx.intentFor<ItemDetailsActivity>(
                                "id" to idItem,
                                "type" to mediaTypeTV,
                                "name" to name
                            ))
                        }
                    }

                    override fun onError() {
                        Log.d("ProfileListAdapter", "Error Movie try Again")
                    }

                })
            }else if (type == Constans.ANIME_LIST){
                searchViewModelAPItmdb!!.getSeriesAndAnime(idItem!!, object :
                    OnGetSeriesCallback {
                    override fun onSuccess(series: Series) {
                        holder.title.text = series.name
                        holder.dateRelease.text = series.firstAirDate?.split("-")?.get(0) ?: series.firstAirDate
                        //holder.description.text = series.overview
                        holder.globalRating.text = series.voteAverage.toString()

                        Glide.with(ctx)
                            .load(Constans.API_MOVIE_SERIES_ANIME_BASE_URL_IMG_PATH_POSTER + series.posterPath)
                            .into(holder.imagePoster)


                        val name = series.name

                        // is responsible for collecting the data to load in the ItemDetailsActivity
                        holder.itemView.setOnClickListener {
                            ctx.startActivity(ctx.intentFor<ItemDetailsActivity>(
                                "id" to idItem,
                                "type" to mediaTypeTV,
                                "name" to name
                            ))
                        }


                    }

                    override fun onError() {
                        Log.d("ProfileListAdapter", "Error Movie try Again")
                    }

                })
            }

            holder.itemView.setOnLongClickListener {
               val ref = FirebaseFirestore.getInstance().collection("Users/${preferences.userId}/RatingList")

               //val refId = ref.document()
               //Log.d("ProfileListAdapter", refId)

                  ref
                  .document("id")
                      .delete()
                  .addOnCompleteListener(object : OnCompleteListener<Void>{
                      override fun onComplete(p0: Task<Void>) {
                          if (p0.isSuccessful){
                              Log.d("ProfileListAdapter", "Deleted item")
                              mValues.removeAt(position)
                              notifyDataSetChanged()
                          }else{
                              Log.d("ProfileListAdapter", "Failed deleted item")
                          }
                      }

                  })
                true
            }



    }

    fun removeItemList(item: ItemListRating){
       mValues.remove(item)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mValues.size
    }


    inner class ViewHolder internal constructor(view: View): androidx.recyclerview.widget.RecyclerView.ViewHolder(view){
        internal var title: TextView
        internal var dateRelease: TextView
        internal var globalRating: TextView
        internal var numPosition: TextView
        internal var dateAt: TextView
        internal var yourRating: TextView
        internal var imagePoster: ImageView
        internal var yourRatingFilter: TextView
        internal var imageFilter: ImageView
        internal var imageFinalRate: ImageView


        init {
            title = view.findViewById(R.id.textViewTitleListOtherProfile)
            dateRelease = view.findViewById(R.id.textViewDateReleastListOtherProfile)
            globalRating = view.findViewById(R.id.textViewRatingListOtherProfile)
            numPosition = view.findViewById(R.id.textViewListOtherProfileNumberPosition)
            dateAt = view.findViewById(R.id.textViewListOtherProfileDateAt)
            yourRating = view.findViewById(R.id.textViewListOtherProfileYourRating)
            imagePoster = view.findViewById(R.id.imageViewPosterListOtherProfile)
            yourRatingFilter = view.findViewById(R.id.textViewRatingFilterSelectedOther)
            imageFilter = view.findViewById(R.id.imageViewFilterIconOther)
            imageFinalRate = view.findViewById(R.id.imageViewFinalRateOther)
        }
    }

}