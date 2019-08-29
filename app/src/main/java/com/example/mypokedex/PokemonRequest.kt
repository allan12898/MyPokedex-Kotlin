package com.example.mypokedex

import android.app.Activity
import android.content.Context
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class PokemonRequest(listeningActivity: Activity) {

    interface PokemonRequestReponse {
        fun receivedNewPokemon(newPokemon: Pokemon)
    }

    private val responseListener: PokemonRequestReponse
    private val context: Context
    private val client: OkHttpClient
    var isLoadingData: Boolean = false
        private set

    init {
        responseListener = listeningActivity as PokemonRequestReponse
        context = listeningActivity.applicationContext
        client = OkHttpClient()
    }

    fun getPokemon(offset: Int = 1) {
        var url = api_url + offset
        var request = Request.Builder().url(url).build()
        isLoadingData = true

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                isLoadingData = false
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                try {


                    val pokemonResponse = response.body()!!.string()

                    val pokemonJSON = JSONObject(pokemonResponse)

                    var newPokemonDetail   = pokemonJSON.getJSONObject("pokemon")
                    var newPokemonImg   = pokemonJSON.getJSONObject("sprites").getString("front_default")


                    var pokemon = Pokemon()
                    pokemon.name = pokemonJSON.getString("name")
                    pokemon.url = newPokemonDetail.getString("url").toString()
                    pokemon.imgURL = newPokemonImg.toString()

                    responseListener.receivedNewPokemon(pokemon)
                    isLoadingData = false

                } catch (e: JSONException) {
                    e.printStackTrace()
                    isLoadingData = false
                }
            }

        })
    }

    companion object {
        private val api_url = "https://pokeapi.co/api/v2/pokemon-form/"

        private val MEDIA_TYPE_KEY = "media_type"
        private val MEDIA_TYPE_VIDEO_VALUE = "video"
        private val URL_SCHEME = "https"
        private val URL_AUTHORITY = "api.nasa.gov"
        private val URL_PATH_1 = "planetary"
        private val URL_PATH_2 = "apod"
        private val URL_QUERY_PARAM_DATE_KEY = "date"
        private val URL_QUERY_PARAM_API_KEY = "api_key"
    }

}