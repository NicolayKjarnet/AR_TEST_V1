package com.example.ar_test_v1

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import io.github.sceneview.ar.ArSceneView
import io.github.sceneview.ar.arcore.LightEstimationMode
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.ar.node.PlacementMode
import io.github.sceneview.math.Position

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    lateinit var sceneView: ArSceneView
    lateinit var placeButton: ImageButton
    lateinit var unAnchorButton: ImageButton
    lateinit var mapButton: ImageButton
    lateinit var mapView: MapView
    lateinit var googleMap: GoogleMap
    private lateinit var modelNode: ArModelNode
    lateinit var textModel: TextView
    var isMapVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        sceneView = findViewById<ArSceneView?>(R.id.sceneView).apply {
            this.lightEstimationMode = LightEstimationMode.ENVIRONMENTAL_HDR_NO_REFLECTIONS
        }

        placeButton = findViewById(R.id.place)
        unAnchorButton = findViewById(R.id.resetAnchor)
        textModel = findViewById(R.id.text)
        mapButton = findViewById(R.id.mapButton)

        placeButton.setOnClickListener {
            placeModel()
            textModel.text = generateRandomSentence()

            placeButton.isGone = true
            unAnchorButton.isGone = false
        }

        unAnchorButton.setOnClickListener {
            unAnchorModel()
            placeButton.isGone = false
            unAnchorButton.isGone = true
        }

        mapButton.setOnClickListener {
            if (isMapVisible) {
                hideMap()
                placeModel()
            } else {
                showMap()
                unAnchorModel()

            }
        }

        modelNode = ArModelNode(PlacementMode.INSTANT).apply {
            loadModelGlbAsync(
                glbFileLocation = "models/person.glb",
                scaleToUnits = 1f,
                centerOrigin = Position(-0.5f)
            ) {
                sceneView.planeRenderer.isVisible = true
            }
        }
        sceneView.addChild(modelNode)
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
        sceneView.destroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    private fun placeModel() {
        modelNode.anchor()
        sceneView.planeRenderer.isVisible = false


        if (modelNode.parent == null) {
            sceneView.addChild(modelNode)
        }
    }

    private fun unAnchorModel() {
        sceneView.removeChild(modelNode)
        sceneView.planeRenderer.isVisible = true
        textModel.text = "hello"

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

    private fun showMap() {
        mapView.visibility = View.VISIBLE
        isMapVisible = true
    }

    private fun hideMap() {
        mapView.visibility = View.GONE
        isMapVisible = false
    }

    override fun onMapReady(map: GoogleMap) {
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.screamface)
        val screamIcon = BitmapDescriptorFactory.fromBitmap(bitmap)
        googleMap = map
        val oslo = LatLng(59.9061, 10.7556)
        googleMap.addMarker(
            MarkerOptions()
                .position(oslo)
                .title("Marker in Oslo")
                .icon(screamIcon)
        )
        val personOnTheWay = LatLng(59.907829702, 10.737830382)

        googleMap.addMarker(
            MarkerOptions()
                .position(personOnTheWay)
                .title("person on the way")
                .icon(screamIcon)
        )
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(personOnTheWay, 13f))
        hideMap()
    }
}
