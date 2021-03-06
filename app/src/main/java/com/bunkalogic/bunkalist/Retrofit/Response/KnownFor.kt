package com.bunkalogic.bunkalist.Retrofit.Response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class KnownFor {

    @SerializedName("poster_path")
    @Expose
    var posterPath: String? = null
    @SerializedName("adult")
    @Expose
    var adult: Boolean? = null
    @SerializedName("overview")
    @Expose
    var overview: String? = null
    @SerializedName("release_date")
    @Expose
    var releaseDate: String? = null
    @SerializedName("original_title")
    @Expose
    var originalTitle: String? = null
    @SerializedName("genre_ids")
    @Expose
    var genreIds: List<Int>? = null
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("media_type")
    @Expose
    var mediaType: String? = null
    @SerializedName("original_language")
    @Expose
    var originalLanguage: String? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("backdrop_path")
    @Expose
    var backdropPath: String? = null
    @SerializedName("popularity")
    @Expose
    var popularity: Double? = null
    @SerializedName("vote_count")
    @Expose
    var voteCount: Int? = null
    @SerializedName("video")
    @Expose
    var video: Boolean? = null
    @SerializedName("vote_average")
    @Expose
    var voteAverage: Double? = null
    @SerializedName("first_air_date")
    @Expose
    var firstAirDate: String? = null
    @SerializedName("origin_country")
    @Expose
    var originCountry: List<String>? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("original_name")
    @Expose
    var originalName: String? = null

    /**
     * No args constructor for use in serialization
     *
     */
    constructor() {}

    /**
     *
     * @param genreIds
     * @param originalName
     * @param originalLanguage
     * @param adult
     * @param backdropPath
     * @param voteCount
     * @param mediaType
     * @param id
     * @param title
     * @param originCountry
     * @param releaseDate
     * @param overview
     * @param name
     * @param posterPath
     * @param firstAirDate
     * @param originalTitle
     * @param voteAverage
     * @param video
     * @param popularity
     */
    constructor(
        posterPath: String,
        adult: Boolean?,
        overview: String,
        releaseDate: String,
        originalTitle: String,
        genreIds: List<Int>,
        id: Int?,
        mediaType: String,
        originalLanguage: String,
        title: String,
        backdropPath: String,
        popularity: Double?,
        voteCount: Int?,
        video: Boolean?,
        voteAverage: Double?,
        firstAirDate: String,
        originCountry: List<String>,
        name: String,
        originalName: String
    ) : super() {
        this.posterPath = posterPath
        this.adult = adult
        this.overview = overview
        this.releaseDate = releaseDate
        this.originalTitle = originalTitle
        this.genreIds = genreIds
        this.id = id
        this.mediaType = mediaType
        this.originalLanguage = originalLanguage
        this.title = title
        this.backdropPath = backdropPath
        this.popularity = popularity
        this.voteCount = voteCount
        this.video = video
        this.voteAverage = voteAverage
        this.firstAirDate = firstAirDate
        this.originCountry = originCountry
        this.name = name
        this.originalName = originalName
    }

}