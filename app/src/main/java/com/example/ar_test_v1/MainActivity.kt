package com.example.ar_test_v1

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isGone
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import io.github.sceneview.ar.ArSceneView
import io.github.sceneview.ar.arcore.LightEstimationMode
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.ar.node.PlacementMode
import io.github.sceneview.math.Position

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
}
