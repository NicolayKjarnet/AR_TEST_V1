package com.example.ar_test_v1

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
<<<<<<< HEAD
import android.widget.FrameLayout
import android.widget.RelativeLayout
=======
>>>>>>> origin/main
import android.widget.TextView
import androidx.core.view.isGone
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import dev.romainguy.kotlin.math.pointAt
import dev.romainguy.kotlin.math.pow
import io.github.sceneview.ar.ArSceneView
import io.github.sceneview.ar.arcore.LightEstimationMode
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.ar.node.PlacementMode
import io.github.sceneview.math.Position

class MainActivity : AppCompatActivity() {

    lateinit var sceneView: ArSceneView
    lateinit var placeButton: ExtendedFloatingActionButton
    private lateinit var modelNode: ArModelNode
    lateinit var textModel: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sceneView = findViewById<ArSceneView?>(R.id.sceneView).apply {
            this.lightEstimationMode = LightEstimationMode.ENVIRONMENTAL_HDR_NO_REFLECTIONS
        }

        placeButton = findViewById(R.id.place)

        textModel = findViewById(R.id.text)

        placeButton.setOnClickListener {
            placeModel()
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
            }
        }
        sceneView.addChild(modelNode)

    }

    @SuppressLint("SetTextI18n")
    private fun placeModel() {
        modelNode.anchor()
        sceneView.planeRenderer.isVisible = false

        val anchorX = modelNode.position.x
        val anchorY = modelNode.position.y

        val layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )

        layoutParams.leftMargin = anchorX.toInt()
        layoutParams.topMargin = anchorY.toInt()

        textModel.text = "hello"
    }
}
