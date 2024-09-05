package com.example.composenewcamera

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Log.d("LifeCycle","onCreate Invoked")
        Toast.makeText(this,"onCreate Invoked", Toast.LENGTH_SHORT).show()
        setContent{
            CameraApplication()
        }
    }
    override fun onStart() {
        super.onStart()
        Log.d("LifeCycle","onStart Invoked")
        Toast.makeText(this,"onStart Invoked", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        Log.d("LifeCycle","onResume Invoked")
        Toast.makeText(this,"OnResume Invoked", Toast.LENGTH_SHORT).show()
    }

    override fun onPause() {
        super.onPause()
        Log.d("LifeCycle","onPause Invoked")
        Toast.makeText(this,"OnPause Invoked", Toast.LENGTH_SHORT).show()
    }

    override fun onStop() {
        super.onStop()
        Log.d("LifeCycle","onStop Invoked")
        Toast.makeText(this,"OnStop Invoked", Toast.LENGTH_SHORT).show()
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("LifeCycle","onRestart Invoked")
        Toast.makeText(this,"OnRestart Invoked", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("LifeCycle","onDestroy Invoked")
        Toast.makeText(this,"OnDestroy Invoked", Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun CameraApplication(){
    val context = LocalContext.current
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        context.packageName + ".provider", file
    )
    var captureImageUri by rememberSaveable {
        mutableStateOf<Uri>(Uri.EMPTY)
    }
    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture())
        {
            captureImageUri = uri
        }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission())
    {
        if (it){
            Toast.makeText(context,"Permission Granted",Toast.LENGTH_SHORT).show()
            cameraLauncher.launch(uri)
        }
        else{
            Toast.makeText(context,"Permission Denied",Toast.LENGTH_SHORT).show()
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
        , horizontalAlignment = Alignment.CenterHorizontally
        , verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = {
            val permissionCheckResult =
                context.checkSelfPermission(android.Manifest.permission.CAMERA)

            if (permissionCheckResult == PackageManager.PERMISSION_GRANTED)
            {
                cameraLauncher.launch(uri)
            }
            else {
                permissionLauncher.launch(android.Manifest.permission.CAMERA)
            }
        }) {
            Text(text = "Capture Image", fontSize = 20.sp, color = Color.White)
        }
    }

    if (captureImageUri.path?. isNotEmpty() == true)
    {
        Log.d("CaptureImageUri", "CapturedImage")
        Image(
            modifier = Modifier
                .padding(16.dp, 8.dp),
            painter = rememberAsyncImagePainter(captureImageUri),
            contentDescription = "Captured Image from the Camera"
        )
    }
    else{
        Image(
            modifier = Modifier
                .padding(16.dp, 8.dp),
            painter = painterResource(id = R.drawable.image),
            contentDescription = "Default Image"
        )
    }
}
@SuppressLint("SimpleDateFormat")
fun Context.createImageFile(): File {
    val timestamp = SimpleDateFormat("yyyy_MM_dd_HH::mm::ss").format(Date())
    val imageFileName =  "JPEG_" + timestamp + "_"
    val image = File.createTempFile(
        imageFileName,
        ".jpg",
        externalCacheDir
    )
    return image
}
@Preview
@Composable
fun CameraApplicationPreview(){
    CameraApplication()
}


