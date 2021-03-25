package com.abunayla.simpleretrofitexample

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.abunayla.simpleretrofitexample.api.ApiRequests
import com.abunayla.simpleretrofitexample.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://cat-fact.herokuapp.com"

class MainActivity : AppCompatActivity() {
    private var TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root


        getFactData()

        binding.layoutGenerateNewFact.setOnClickListener {
            getFactData()
        }

        setContentView(view)
    }

    private fun getFactData() {

        binding.apply {
            tvTextView.visibility = View.GONE
            tvTimeStamp.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        }


        val api = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiRequests::class.java)

        // For illustration purposes only, not for production!
        GlobalScope.launch(Dispatchers.IO) {
            try {


                val response = api.getCatFacts().awaitResponse()
                if (response.isSuccessful) {
                    val data = response.body()!!
                    Log.d(TAG, data.text)
                    withContext(Dispatchers.Main) {
                        binding.apply {
                            tvTextView.visibility = View.VISIBLE
                            tvTimeStamp.visibility = View.VISIBLE
                            progressBar.visibility = View.GONE

                            tvTextView.text = data.text
                            tvTimeStamp.text = data.createdAt
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG)
                }
            }
        }
    }
}