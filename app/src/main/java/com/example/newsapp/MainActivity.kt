package com.example.newsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
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

        binding.recylerView.layoutManager = LinearLayoutManager(this)
        fetchData()
        mAdapter= NewsListAdapter( this)
        binding.recylerView.adapter = mAdapter
    }

    private fun fetchData() {
        val url = "https://newsapi.org/v2/top-headlines?country=in&category=business&apiKey=499653c0a19a4a2d86617ce635b1c417"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
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
            },
            Response.ErrorListener { error ->
                // TODO: Handle error
            }

        )

// Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)

    }

    override fun onItemClicked(item: News) {
    }
}