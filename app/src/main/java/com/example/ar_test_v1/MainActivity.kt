package com.example.ar_test_v1

import android.annotation.SuppressLint
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isGone
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import io.github.sceneview.ar.ArSceneView
import io.github.sceneview.ar.arcore.LightEstimationMode
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.ar.node.PlacementMode
import io.github.sceneview.math.Position
import java.io.IOException


class MainActivity : AppCompatActivity() {

    lateinit var sceneView: ArSceneView
    lateinit var placeButton: ExtendedFloatingActionButton
    lateinit var unAnchorButton: Button
    private lateinit var modelNode: ArModelNode
    lateinit var textModel: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sceneView = findViewById<ArSceneView?>(R.id.sceneView).apply {
            this.lightEstimationMode = LightEstimationMode.ENVIRONMENTAL_HDR_NO_REFLECTIONS
        }

        placeButton = findViewById(R.id.place)
        unAnchorButton = findViewById(R.id.resetAnchor)
        textModel = findViewById(R.id.text)

        placeButton.setOnClickListener {
            placeModel()
            textModel.text = generateRandomSentence()
        }

        unAnchorButton.setOnClickListener {
            unAnchorModel()
        }

        modelNode = ArModelNode(PlacementMode.INSTANT).apply {
            loadModelGlbAsync(
                glbFileLocation = "models/mario.glb",
                scaleToUnits = 1f,
                centerOrigin = Position(-0.5f)
            ) {
                sceneView.planeRenderer.isVisible = true
            }
            onAnchorChanged = {
                placeButton.isGone = it != null
                unAnchorButton.isGone = it == null
            }
        }
        sceneView.addChild(modelNode)
    }

    @SuppressLint("SetTextI18n")
    private fun placeModel() {
        modelNode.anchor()
        sceneView.planeRenderer.isVisible = false
        textModel.text = "hello"
    }

    private fun unAnchorModel() {
        sceneView.removeChild(modelNode)
        sceneView.planeRenderer.isVisible = true
        textModel.text = ""
    }

    private fun generateRandomSentence(): String {
        val sentences = listOf(
            "This is sentence 1",
            "Another sentence here",
            "Sentence number three",
            "Lorem ipsum dolor sit amet",
            "Random sentence generator"
        )
        return sentences.random()
    }

    fun screenshot(view: View) {
        val rootView = window.decorView.rootView
        val bitmap = getBitmapFromView(rootView)

        try {
            val contentResolver = applicationContext.contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, "screenshot.png")
                put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }

            val imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            val outputStream = imageUri?.let { contentResolver.openOutputStream(it) }

            outputStream?.use {
                bitmap?.compress(Bitmap.CompressFormat.PNG, 100, it)
                Toast.makeText(this, "Screenshot saved", Toast.LENGTH_SHORT).show()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to save screenshot", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getBitmapFromView(view: View): Bitmap? {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)
        view.draw(canvas)
        return bitmap
    }
}
