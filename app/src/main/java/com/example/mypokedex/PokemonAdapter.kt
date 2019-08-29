package com.example.mypokedex

import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.pokedex_row.view.*

class PokemonAdapter(private val pokemons: ArrayList<Pokemon>) : RecyclerView.Adapter<PokemonAdapter.PokemonHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonHolder {
        val inflatedView = parent.inflate(R.layout.pokedex_row, false)
        return PokemonHolder(inflatedView)
    }

    override fun getItemCount(): Int = pokemons.size

    override fun onBindViewHolder(holder: PokemonHolder, position: Int) {
        val itemPokemon = pokemons[position]
        holder.bindPokemon(itemPokemon)
    }


    class PokemonHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {


        private var view: View = v
        private var pokemon: Pokemon? = null

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val context = itemView.context
            val showPhotoIntent = Intent(context, PokemonActivity::class.java)
            showPhotoIntent.putExtra(POKEMON_URL, pokemon)
            context.startActivity(showPhotoIntent)
        }

        fun bindPokemon(pokemon: Pokemon) {
            this.pokemon = pokemon
            Picasso.get().load(pokemon.imgURL).into(view.pokemon_img)
            view.pokemon_name.text = pokemon.name
        }
        companion object {
            //5
            private val POKEMON_URL  = "POKEMON"
        }
    }
}
