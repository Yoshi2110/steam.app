package com.example.appsteam

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.appsteam.databinding.ItemGameLibraryBinding

class LibraryGamesAdapter(
    private var libraryList: List<Game>,
    private val onGameClick: (Game) -> Unit
) : RecyclerView.Adapter<LibraryGamesAdapter.LibraryViewHolder>() {

    class LibraryViewHolder(val binding: ItemGameLibraryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibraryViewHolder {
        val binding = ItemGameLibraryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LibraryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LibraryViewHolder, position: Int) {
        val game = libraryList[position]
        val context = holder.itemView.context

        with(holder.binding) {
            tvLibraryTitle.text = game.title
            tvLibraryTags.text = game.tags.take(3).joinToString(" • ")
            tvLibraryHours.text = game.hoursPlayed ?: "Adquirido recentemente"
            imgLibraryCover.setImageResource(game.imageResId)

            if (game.isInstalled) {
                btnLibraryAction.text = context.getString(R.string.play_button)
                btnLibraryAction.backgroundTintList = ContextCompat.getColorStateList(context, R.color.steam_green_btn)
                btnLibraryAction.setIconResource(R.drawable.ic_play)
            } else {
                btnLibraryAction.text = context.getString(R.string.install_button)
                btnLibraryAction.backgroundTintList = ContextCompat.getColorStateList(context, R.color.steam_search_bg)
                btnLibraryAction.setIconResource(R.drawable.ic_gamepad)
            }

            btnLibraryAction.setOnClickListener {
                if (game.isInstalled) {
                    Toast.makeText(context, "Iniciando ${game.title}...", Toast.LENGTH_SHORT).show()
                } else {
                    game.isInstalled = true
                    Toast.makeText(context, "Instalando ${game.title}...", Toast.LENGTH_SHORT).show()
                    notifyItemChanged(holder.bindingAdapterPosition)
                }
            }

            root.setOnClickListener {
                onGameClick(game)
            }
        }
    }

    override fun getItemCount(): Int = libraryList.size

    fun updateList(newList: List<Game>) {
        libraryList = newList
        notifyDataSetChanged()
    }
}
