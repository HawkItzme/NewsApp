package com.example.newsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.AuthFailureError
import com.android.volley.Header
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.newsapp.databinding.ActivityMainBinding
import org.json.JSONObject

class MainActivity : AppCompatActivity(), NewsItemsClicked {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mAdapter: NewsListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        mAdapter= NewsListAdapter( this)

        binding.recylerView.layoutManager = LinearLayoutManager(this)
        binding.recylerView.adapter = mAdapter

        fetchData()
    }

    private fun fetchData() {
        val url = "https://newsapi.org/v2/top-headlines?country=in&category=business"
        val jsonObjectRequest :JsonObjectRequest = object : JsonObjectRequest(
            Method.GET, url, null,
            Response.Listener {
                val  newsJsonArray = it.getJSONArray("articles")
                val newsArray = ArrayList<News>()
                for(i in 0 until newsJsonArray.length()){
                    val newsJsonObject = newsJsonArray.getJSONObject(i)
                    val news = News(
                        newsJsonObject.getString("title"),
                        newsJsonObject.getString("author"),
                        newsJsonObject.getString("url"),
                        newsJsonObject.getString("urltoImage"),
                    )
                    newsArray.add(news)
                }
                mAdapter.updateNews(newsArray)

                Log.e("FetchData: ", it.toString())
            },
            Response.ErrorListener{
                it.message?.let { error -> Log.e("FetchDataError: ", error) }
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["x-api-key"] = "499653c0a19a4a2d86617ce635b1c417"
                return headers
            }
        }
        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this)
            .addToRequestQueue(jsonObjectRequest)


    }

    override fun onItemClicked(item: News) {
    }
}