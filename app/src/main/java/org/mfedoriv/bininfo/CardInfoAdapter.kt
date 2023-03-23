package org.mfedoriv.bininfo

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.mfedoriv.bininfo.databinding.ItemCardInfoLayoutBinding

class CardInfoAdapter(private val context: Context) :
    RecyclerView.Adapter<CardInfoAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemCardInfoLayoutBinding.bind(view)
    }

    private val cardInfoList = mutableListOf<CardInfo>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardInfoAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_card_info_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CardInfoAdapter.ViewHolder, position: Int) {
        val cardInfo = cardInfoList[position]
        with(holder.binding) {
            bin.text = cardInfo.bin.toString()
            scheme.text = cardInfo.scheme
            brand.text = cardInfo.brand
            length.text = cardInfo.number?.length.toString()
            luhn.text = cardInfo.number?.luhn.convert()
            bank.text = cardInfo.bank?.name ?: ""
            site.text = cardInfo.bank?.url ?: ""
            phone.text = cardInfo.bank?.phone ?: ""
            type.text = cardInfo.type
            prepaid.text = cardInfo.prepaid.convert()
            country.text = cardInfo.country?.name ?: ""
            if (cardInfo.country?.latitude == null && cardInfo.country?.longitude == null) {
                latitudeLongitude.text = ""
            } else {
                "latitude: ${cardInfo.country.latitude},\nlongitude: ${cardInfo.country.longitude}".also {
                    latitudeLongitude.text = it
                    latitudeLongitude.setOnClickListener {
                        val uri = "geo:${cardInfo.country.latitude},${cardInfo.country.longitude}"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                        context.startActivity(intent)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int = cardInfoList.size

    fun add(cardInfo: CardInfo) {
        cardInfoList.add(0, cardInfo)
        notifyDataSetChanged()
    }
}