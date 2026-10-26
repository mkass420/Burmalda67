package com.example.burmalda67.ui.combinations

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.burmalda67.R
import com.example.burmalda67.data.HandCombination

// Адаптер для списка комбинаций. По клику раскрывается описание
class CombinationsAdapter(
    private val items: List<HandCombination>,
) : RecyclerView.Adapter<CombinationsAdapter.CombinationViewHolder>() {

    // ранги раскрытых элементов
    private val expanded = mutableSetOf<Int>()

    class CombinationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val rank: TextView = view.findViewById(R.id.rank)
        val name: TextView = view.findViewById(R.id.name)
        val probability: TextView = view.findViewById(R.id.probability)
        val chevron: ImageView = view.findViewById(R.id.chevron)
        val description: TextView = view.findViewById(R.id.description)
        val cards: List<TextView> = listOf(
            view.findViewById(R.id.card1),
            view.findViewById(R.id.card2),
            view.findViewById(R.id.card3),
            view.findViewById(R.id.card4),
            view.findViewById(R.id.card5),
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CombinationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_combination, parent, false)
        return CombinationViewHolder(view)
    }

    override fun onBindViewHolder(holder: CombinationViewHolder, position: Int) {
        val item = items[position]
        val context = holder.itemView.context
        val isExpanded = item.rank in expanded

        holder.rank.text = item.rank.toString()
        holder.name.text = item.name
        holder.probability.text = context.getString(R.string.combination_probability, item.probability)
        holder.description.text = item.description
        holder.description.visibility = if (isExpanded) View.VISIBLE else View.GONE
        holder.chevron.rotation = if (isExpanded) 180f else 0f

        holder.cards.forEachIndexed { index, cardView ->
            val card = item.cards[index]
            cardView.text = card
            val isRed = card.endsWith("♥") || card.endsWith("♦")
            cardView.setTextColor(
                ContextCompat.getColor(context, if (isRed) R.color.suit_red else R.color.suit_black)
            )
        }

        holder.itemView.setOnClickListener {
            val pos = holder.bindingAdapterPosition
            if (pos == RecyclerView.NO_POSITION) return@setOnClickListener
            val rank = items[pos].rank
            if (!expanded.remove(rank)) expanded.add(rank)
            notifyItemChanged(pos)
        }
    }

    override fun getItemCount(): Int = items.size
}
