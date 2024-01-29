package com.hehspace_host.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.widget.ImageView
import java.io.IOException
import java.net.URL
 
class LoadImageTask() : AsyncTask<String, Void, Bitmap>()
{
    override fun doInBackground(vararg urls: String): Bitmap?
    {
        try {
            val stream = URL(urls[0]).openStream()
 
            return BitmapFactory.decodeStream(stream)
        } catch (e: IOException) {
            e.printStackTrace()
        }
 
        return null
    }
 
    override fun onPostExecute(bitmap: Bitmap?)
    {
       // imageView.setImageBitmap(bitmap)
    }
}