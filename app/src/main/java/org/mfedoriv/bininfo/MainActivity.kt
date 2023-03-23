package org.mfedoriv.bininfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import kotlinx.coroutines.launch
import okhttp3.*
import org.mfedoriv.bininfo.databinding.ActivityMainBinding
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val cardInfoAdapter = CardInfoAdapter(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Init Recycler View
        binding.cardInfoRv.layoutManager = LinearLayoutManager(this)
        binding.cardInfoRv.adapter = cardInfoAdapter

        binding.searchButton.setOnClickListener {
            if (TextUtils.isEmpty(binding.searchEt.text)) {
                binding.searchEt.error = "BIN can't be empty!"
            } else {
                requestData(binding.searchEt.text.toString().toInt())
            }

        }

    }

    private fun requestData(bin: Int) {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("https://lookup.binlist.net/$bin")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        Toast.makeText(applicationContext, "BIN is invalid. Try again!", Toast.LENGTH_SHORT).show()
                    }
                    val cardInfo = parseCardInfoFromJSON(bin, response.body!!.string())
                    lifecycleScope.launch {
                        cardInfoAdapter.add(cardInfo)
                    }
                }
            }
        })
    }

    private fun parseCardInfoFromJSON(bin: Int, responseBody: String): CardInfo {
        val moshi: Moshi = Moshi.Builder().build()
        val adapter: JsonAdapter<CardInfo> = moshi.adapter(CardInfo::class.java)
        var cardInfo = adapter.fromJson(responseBody)
        cardInfo!!.bin = bin
        return cardInfo
    }
}