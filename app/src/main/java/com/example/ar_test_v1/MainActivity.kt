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


<<<<<<< HEAD
        // Create a bitmap representing the camera feed
        val cameraBitmap = getCameraBitmap()

        // Overlay the camera bitmap on top of the screenshot bitmap
        val mergedBitmap = mergeBitmaps(bitmap, cameraBitmap)

        try {
            val contentResolver = applicationContext.contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, "screenshot.png")
                put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures")
            }

            val imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            val outputStream = imageUri?.let { contentResolver.openOutputStream(it) }

            outputStream?.use {
                mergedBitmap?.compress(Bitmap.CompressFormat.PNG, 100, it)
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

    private fun getCameraBitmap(): Bitmap? {
        // Assuming you have a cameraView in your layout
        val cameraView: View? = findViewById(R.id.cameraView)

        // Get the dimensions of the camera view
        val width = cameraView?.width ?: 0
        val height = cameraView?.height ?: 0

        if (width > 0 && height > 0) {
            // Create a bitmap with the same dimensions as the camera view
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

            // Draw the camera view on the bitmap
            val canvas = Canvas(bitmap)
            cameraView?.draw(canvas)

            return bitmap
        }
        return null
    }

    private fun mergeBitmaps(backgroundBitmap: Bitmap?, overlayBitmap: Bitmap?): Bitmap? {
        if (backgroundBitmap == null || overlayBitmap == null) {
            return null
        }

        val mergedBitmap = Bitmap.createBitmap(backgroundBitmap.width, backgroundBitmap.height, backgroundBitmap.config)
        val canvas = Canvas(mergedBitmap)
        canvas.drawBitmap(backgroundBitmap, 0f, 0f, null)
        canvas.drawBitmap(overlayBitmap, 0f, 0f, null)

        return mergedBitmap
    }
=======
>>>>>>> origin/main
}
