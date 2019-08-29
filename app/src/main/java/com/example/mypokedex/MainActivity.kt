package com.example.mypokedex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException

class MainActivity : AppCompatActivity(), PokemonRequest.PokemonRequestReponse {


    var pokemons: ArrayList<Pokemon> = ArrayList()
    private lateinit var linearLayoutManager: LinearLayoutManager

    private lateinit var pokemonRequest: PokemonRequest
    var num = 1


    private lateinit var adapter: PokemonAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pokemonRequest = PokemonRequest(this)
        linearLayoutManager = LinearLayoutManager(this)
        pokemon_rv.layoutManager = linearLayoutManager

        adapter = PokemonAdapter(pokemons)
        pokemon_rv.adapter = adapter

        setRecyclerViewScrollListener()

    }

    override fun onStart() {
        super.onStart()
        while(num < 8 ){
            requestPokemon(num)
            num+=1
        }
//        if (pokemons.size == 0) {
//            requestPokemon(num)
//            num+=1
//        }

    }


    private fun requestPokemon(number : Int) {
        try {
            pokemonRequest.getPokemon(number)
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }


    override fun receivedNewPokemon(newPokemon: Pokemon) {
        runOnUiThread {
            pokemons.add(newPokemon)
            adapter.notifyItemInserted(pokemons.size)
        }
    }

    private val lastVisibleItemPosition: Int
        get() = linearLayoutManager.findLastVisibleItemPosition()

    private fun setRecyclerViewScrollListener() {
        pokemon_rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val totalItemCount = pokemon_rv.layoutManager!!.itemCount
                if (!pokemonRequest.isLoadingData && totalItemCount == lastVisibleItemPosition + 1) {
                    requestPokemon(num)
                    num+=1
                }
            }
        })
    }
}
