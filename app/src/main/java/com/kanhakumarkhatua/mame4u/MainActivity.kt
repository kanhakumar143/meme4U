package com.kanhakumarkhatua.mame4u

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest

import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.kanhakumarkhatua.mame4u.MySingleton.MySingleton.Companion.getInstance


class MainActivity : AppCompatActivity() {

    var currentImgUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        loadMeme()
    }

    private fun loadMeme(){
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE

        val url = "https://meme-api.herokuapp.com/gimme"

// Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url,null,
            { response ->
                currentImgUrl = response.getString("url")
                val imageView = findViewById<ImageView>(R.id.memeImgView)
                Glide.with(this).load(currentImgUrl).listener(object: RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }
                }).into(imageView)
            },
            {
                Toast.makeText(this, "Something Want Wrong", Toast.LENGTH_SHORT).show()
            })

// Add the request to the RequestQueue.
        MySingleton.MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)

    }

    fun shareMeme(view: View) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type="text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "Hii, Checkout the image $currentImgUrl")
        val chooser = Intent.createChooser(intent, "Share meme using ...")
        startActivity(chooser)
    }
    fun nextMeme(view: View) {
        loadMeme()
    }

//    Mame link https://meme-api.herokuapp.com/gimme
}