package com.ir.ali.downloadzy

import android.app.DownloadManager
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import com.ir.ali.downloadzy.databinding.ActivityMainBinding
import java.net.URL

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.downloadButton.setOnClickListener {
            if (isConnected()) download()
           //else TODO("Snack bar")
        }
    }
    private fun isConnected(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }
    private fun download() {
        // Take user Url from UI layout and pass it to variable called "downloadUrl"
        val downloadUrl = binding.linkInput.text.toString()
        // Pull out file name from user Url
        val fileName = URL(downloadUrl).path.substringAfterLast('/')
        val downloadManager: DownloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        // Open request to download
        val downloadRequest = DownloadManager.Request(Uri.parse(downloadUrl))
        downloadRequest.setTitle("Download Started")
        // Set downloaded file path in DOWNLOADS directory and also set downloaded file name using "fileName" variable
        downloadRequest.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        // Notify user with notifications (while progress and also after complete download)
        downloadRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
        downloadRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        // Final Implementation and take out download id to use it later
        val downloadId = downloadManager.enqueue(downloadRequest)
    }
}