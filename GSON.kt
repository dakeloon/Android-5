package com.example.gson

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import okhttp3.*
import timber.log.Timber
import java.io.IOException
import java.net.URL


class MainActivity : AppCompatActivity(), Adapter.CellClickListener {
    private val myUrl = URL("https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=ff49fcd4d4a08aa6aafb6ea3de826464&tags=cat&format=json&nojsoncallback=1")
    private var okHttpClient: OkHttpClient = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Timber.plant(Timber.DebugTree())

        load()

        val recyclerView: RecyclerView = findViewById(R.id.rView)

        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = Adapter(this, fetchList(), this)

        val mDividerItemDecoration: DividerItemDecoration = DividerItemDecoration(
            recyclerView.context,
            (recyclerView.layoutManager as GridLayoutManager).orientation
        )
        recyclerView.addItemDecoration(mDividerItemDecoration);
    }

    private fun load() {
        val request: Request = Request.Builder().url(myUrl).build()
        okHttpClient.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                TODO("Not yet implemented")
            }

            override fun onResponse(call: Call, response: Response) {
                val json = response.body()?.string()
                val wrapped: Wrapper = Gson().fromJson(json, Wrapper::class.java)
                val firstPage: PhotoPage = Gson().fromJson(wrapped.photos, PhotoPage::class.java)

                val photos = Gson().fromJson(firstPage.photo, Array<Photo>::class.java).toList()

                for (i in photos.indices) {
                    if (i % 5 == 0) {
                        Timber.d(firstPage.photo[i].toString())
                    }
                }
            }
        })
    }

    private fun fetchList(): ArrayList<Photo> {
        val list = arrayListOf<Photo>()

        val model1 = Photo(52549327280, "60858939@N00","e9c0544af2", 65535, 66, "Cinnamon Cake")
        val model2 = Photo(52548824136, "31269254@N04","880d68c737", 65535, 66, "Cosmic Cat")
        val model3 = Photo(52548792454, "61300408@N05","c01489098e", 65535, 66, "\\u3054\\u307e")
        val model4 = Photo(52530249735, "97555832@N08","5f29879aaf", 65535, 66, "Alma&Felicia_02992")
        val model5 = Photo(52525803402, "43056966@N07","47e6af2b13", 65535, 66, "Valldemossa Cat [Valldemossa - 24 August 2022]")
        val model6 = Photo(52547834187, "84904494@N00","1661a66208", 65535, 66, "Better Homes & Gardens 2022-11 122")


        list.add(model1)
        list.add(model2)
        list.add(model3)
        list.add(model4)
        list.add(model5)
        list.add(model6)

        return list
    }
}
