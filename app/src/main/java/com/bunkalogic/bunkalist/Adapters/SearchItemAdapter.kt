package com.bunkalogic.bunkalist.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bunkalogic.bunkalist.Activities.DetailsActivities.ItemDetailsActivity
import com.bunkalogic.bunkalist.Dialog.AddListDialog
import com.bunkalogic.bunkalist.Others.Constans
import com.bunkalogic.bunkalist.R
import com.bunkalogic.bunkalist.Retrofit.Response.ResultSearchAll
import org.jetbrains.anko.intentFor
import android.support.v7.app.AppCompatActivity
import com.bunkalogic.bunkalist.SharedPreferences.preferences


class SearchItemAdapter(private val ctx: Context, private var mValues: List<ResultSearchAll>?): RecyclerView.Adapter<SearchItemAdapter.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_search_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        if (mValues != null)
            return mValues!!.size
            else return 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(mValues!![position])
    }

   fun setData(title: List<ResultSearchAll>){
       this.mValues = title
       notifyDataSetChanged()
   }


    inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        private val imageViewPoster: ImageView = mView.findViewById(R.id.imageViewPoster)
        private val textViewTitle: TextView = mView.findViewById(R.id.textViewTitle)
        private val textViewDateReleast :TextView = mView.findViewById(R.id.textViewDateReleast)
        private val textViewDescription: TextView = mView.findViewById(R.id.textViewDescription)
        private val textViewSeeDetails: TextView = mView.findViewById(R.id.textViewGetFullDetails)
        private val imageViewRating: ImageView = mView.findViewById(R.id.imageViewAddToMyList)
        private val textViewRating: TextView = mView.findViewById(R.id.textViewRating)

        fun bind(mItem : ResultSearchAll){

            textViewTitle.text = mItem.title
            if (textViewTitle.text.isEmpty()){
                textViewTitle.text = mItem.name
            }//else{
                //textViewTitle.text = ctx.getString(R.string.search_item_not_have_tittle)
            //}

            // It is responsible for just showing the date age of the movie or series
            textViewDateReleast.text = mItem.releaseDate
            if (textViewDateReleast.text.isEmpty()){
                textViewDateReleast.text = mItem.firstAirDate?.split("-")?.get(0) ?: mItem.firstAirDate
            }else{
                textViewDateReleast.text = mItem.releaseDate?.split("-")?.get(0) ?: mItem.releaseDate
            }


            // It is responsible for loading the poster of the movies and series
            textViewRating.text = mItem.voteAverage.toString()
            textViewDescription.text = mItem.overview

            val photo = mItem.posterPath

                Glide.with(ctx)
                    .load(Constans.API_MOVIE_SERIES_ANIME_BASE_URL_IMG_PATH_POSTER + photo)
                    .into(imageViewPoster)

            // is responsible for collecting the id and type to load after depending on whether it is a movie or series
            val id = mItem.id
            val type = mItem.mediaType
            val title = mItem.title
            val name = mItem.name

            textViewSeeDetails.setOnClickListener {
                ctx.startActivity(ctx.intentFor<ItemDetailsActivity>(
                    "id" to id,
                    "type" to type,
                    "title" to title,
                    "name" to name
                ))
            }


            imageViewRating.setOnClickListener {
                preferences.itemID = id!!
                mItem.title = preferences.itemName
                val manager = (ctx as AppCompatActivity).supportFragmentManager
                AddListDialog().show(manager, "")
            }
        }
    }
}