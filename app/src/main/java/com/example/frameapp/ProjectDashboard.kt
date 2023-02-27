package com.example.frameapp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.frameapp.adapter.ImageListAdapter
import com.example.frameapp.adapter.ViewPagerAdapter
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator


class ProjectDashboard : AppCompatActivity() {

    //store uris of picked images
    private var images: ArrayList<Uri?>? = null
     lateinit var recyclerView : RecyclerView
     lateinit var recyclerView1 : RecyclerView
     lateinit var adapter: ImageListAdapter
     lateinit var viewpager : ViewPager
     lateinit var viewPagerAdapter : ViewPagerAdapter
     lateinit var nextBT : Button
     lateinit var dotIndicator : DotsIndicator
     lateinit var addImages : TextView
     lateinit var cardView : CardView
     val REQUEST_CODE = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project_dashboard)

        findIds()
        listeners()
    }

    private fun setAdapter(imageUri: Uri, count:Int, images: ArrayList<Uri?>?) {
        adapter = ImageListAdapter(this,images)
        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapter

        viewPagerAdapter = ViewPagerAdapter(this,imageUri,count,images)
        viewpager.setAdapter(viewPagerAdapter)
        dotIndicator.attachTo(viewpager)
    }

    private fun listeners() {
        nextBT.setOnClickListener {
            var intent = Intent(this,EditProjectScreen::class.java)
            startActivity(intent)
        }

        addImages.setOnClickListener {
            pickImagesIntent()
        }
    }


    private fun findIds() {
        recyclerView = findViewById(R.id.recyclerView)
        viewpager = findViewById(R.id.viewpager)
        dotIndicator = findViewById(R.id.dots_indicator)
        nextBT = findViewById(R.id.nextBT)
        addImages = findViewById(R.id.addImages)
        images = ArrayList()
        cardView = findViewById(R.id.cardView)
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun pickImagesIntent(){
        if (Build.VERSION.SDK_INT < 19) {
            var intent = Intent()
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Choose Pictures")
                , REQUEST_CODE
            )
        }
        else { // For latest versions API LEVEL 19+
            var intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){

            // if multiple images are selected
            if (data?.getClipData() != null) {
                var count = data.clipData!!.itemCount

                for (i in 0..count - 1) {
                    var imageUri: Uri = data.clipData!!.getItemAt(i).uri
                    images!!.add(imageUri)
                    Log.d("imageUri ==> ",""+images)

                    setAdapter(imageUri,count,images)
                    //     iv_image.setImageURI(imageUri) Here you can assign your Image URI to the ImageViews
                }

            } else if (data?.getData() != null) {
                // if single image is selected

                var imageUri: Uri = data.data!!
                Log.d("imageUri ---> ",""+imageUri)
                images!!.add(imageUri)
                setAdapter(imageUri,1,images)
                //   iv_image.setImageURI(imageUri) Here you can assign the picked image uri to your imageview

            }
        }
    }
}