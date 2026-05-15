package com.nammashale.shalepride.ui.home

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import com.nammashale.shalepride.databinding.FragmentBusTrackingBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

/**
 * Feature 5: Live School Bus Tracker — OSMDroid (OpenStreetMap)
 *
 * 100% FREE, no API key, uses real OpenStreetMap street tiles.
 *
 * Route: Jayanagar → Banashankari → Kanakapura Road → Udayapura → DSATM
 * (Near Art of Living International Centre, Kanakapura Road, Bengaluru)
 */
class BusTrackingFragment : Fragment() {

    private var _binding: FragmentBusTrackingBinding? = null
    private val binding get() = _binding!!

    private lateinit var mapView: MapView
    private var busMarker: Marker? = null
    private var busAnimator: ValueAnimator? = null

    private val handler = Handler(Looper.getMainLooper())
    private val totalMinutes = 12
    private var elapsedSeconds = 0
    private val totalSeconds = totalMinutes * 60

    private val speeds = listOf(28, 31, 34, 38, 32, 36, 30, 35, 33, 37)
    private var speedIndex = 0

    // ─── Route: Jayanagar → Kanakapura Road → Udayapura → DSATM ──────────────
    // DSATM (Dayananda Sagar Academy of Technology and Management)
    // Near Art of Living International Centre, Udayapura, Kanakapura Road, Bengaluru 560082
    private val routeWaypoints = listOf(
        GeoPoint(12.9415, 77.5830),  // 🏠 Jayanagar 4th Block (Pickup Start)
        GeoPoint(12.9350, 77.5720),
        GeoPoint(12.9280, 77.5620),
        GeoPoint(12.9210, 77.5530),  // 🚏 Stop 2 — Banashankari
        GeoPoint(12.9130, 77.5460),
        GeoPoint(12.9050, 77.5400),
        GeoPoint(12.8970, 77.5360),  // 🚏 Stop 3 — Kanakapura Road Junction
        GeoPoint(12.8880, 77.5330),
        GeoPoint(12.8780, 77.5310),
        GeoPoint(12.8680, 77.5295),
        GeoPoint(12.8580, 77.5285),  // 🚏 Stop 4 — Udayapura (near Art of Living)
        GeoPoint(12.8500, 77.5275),
        GeoPoint(12.8430, 77.5268),
        GeoPoint(12.8352, 77.5261)   // 🏫 DSATM — Kanakapura Road, Bengaluru 560082
    )

    // Smooth animation path: 30 interpolated points between each waypoint
    private val animPoints = mutableListOf<GeoPoint>()

    private val tickRunnable = object : Runnable {
        override fun run() {
            if (!isAdded || _binding == null) return
            elapsedSeconds++
            updateUI()
            if (elapsedSeconds < totalSeconds) handler.postDelayed(this, 1000)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // REQUIRED: set OSMDroid user agent before inflating
        Configuration.getInstance().userAgentValue = requireContext().packageName

        _binding = FragmentBusTrackingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarBus.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // Setup map
        mapView = binding.mapView
        setupMap()

        // Build smooth animation path
        buildAnimPath()

        // Add overlays
        drawRoute()
        addStopMarkers()
        addBusMarker()
        zoomToRoute()

        // Start moving bus
        startBusAnimation()

        // Start ETA countdown
        updateUI()
        handler.postDelayed(tickRunnable, 1000)
    }

    private fun setupMap() {
        mapView.setTileSource(TileSourceFactory.MAPNIK)   // OpenStreetMap standard tiles
        mapView.setMultiTouchControls(true)
        mapView.controller.setZoom(15.0)
        mapView.isClickable = true

        // Disable rotation so it stays like a normal map
        mapView.setMapOrientation(0f)
    }

    private fun buildAnimPath() {
        animPoints.clear()
        for (i in 0 until routeWaypoints.size - 1) {
            val from = routeWaypoints[i]
            val to = routeWaypoints[i + 1]
            for (step in 0..30) {
                val t = step / 30.0
                animPoints.add(
                    GeoPoint(
                        from.latitude + (to.latitude - from.latitude) * t,
                        from.longitude + (to.longitude - from.longitude) * t
                    )
                )
            }
        }
    }

    private fun drawRoute() {
        val polyline = Polyline(mapView).apply {
            setPoints(routeWaypoints)
            outlinePaint.color = Color.parseColor("#1E6B3C")
            outlinePaint.strokeWidth = 12f
            outlinePaint.isAntiAlias = true
            outlinePaint.style = Paint.Style.STROKE
            outlinePaint.pathEffect = DashPathEffect(floatArrayOf(30f, 15f), 0f)
            isGeodesic = true
        }
        mapView.overlays.add(polyline)
    }

    private fun addStopMarkers() {
        val stops = mapOf(
            0  to Triple("🏠 Jayanagar 4th Block",        "Pickup Start",                    Color.parseColor("#1565C0")),
            3  to Triple("🚏 Banashankari",               "Stop 2",                          Color.parseColor("#E65100")),
            6  to Triple("🚏 Kanakapura Road Junction",   "Stop 3",                          Color.parseColor("#E65100")),
            10 to Triple("🚏 Udayapura",                  "Stop 4 — Near Art of Living",     Color.parseColor("#E65100")),
            13 to Triple("🏫 DSATM",                     "Kanakapura Road, Bengaluru 560082", Color.parseColor("#1B5E20"))
        )

        for ((index, info) in stops) {
            val point = routeWaypoints.getOrNull(index) ?: continue
            val (title, snippet, color) = info
            val marker = Marker(mapView).apply {
                position = point
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                this.title = title
                this.snippet = snippet
                icon = createMarkerIcon(requireContext(), color)
            }
            mapView.overlays.add(marker)
        }
    }

    private fun addBusMarker() {
        val start = routeWaypoints.first()
        busMarker = Marker(mapView).apply {
            position = start
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
            title = "🚌 DSATM School Bus"
            snippet = "En route to DSATM via Kanakapura Road"
            icon = createBusIcon(requireContext())
        }
        mapView.overlays.add(busMarker)
    }

    private fun zoomToRoute() {
        val lats = routeWaypoints.map { it.latitude }
        val lngs = routeWaypoints.map { it.longitude }
        val box = BoundingBox(
            lats.max(), lngs.max(),
            lats.min(), lngs.min()
        )
        mapView.post {
            mapView.zoomToBoundingBox(box, true, 80)
        }
    }

    private fun startBusAnimation() {
        val totalPoints = animPoints.size
        busAnimator = ValueAnimator.ofInt(0, totalPoints - 1).apply {
            duration = (totalSeconds * 1000).toLong()
            interpolator = LinearInterpolator()
            addUpdateListener { anim ->
                if (_binding == null) return@addUpdateListener
                val idx = anim.animatedValue as Int
                val pos = animPoints.getOrNull(idx) ?: return@addUpdateListener
                busMarker?.position = pos
                // Smooth camera follow every 5 frames
                if (idx % 5 == 0) {
                    mapView.controller.animateTo(pos)
                }
                mapView.invalidate()
            }
            start()
        }
    }

    // ─── Bitmap helpers ──────────────────────────────────────────────────────

    private fun createBusIcon(ctx: Context): android.graphics.drawable.BitmapDrawable {
        val size = 130
        val bmp = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)

        // Outer shadow
        val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#44000000")
            maskFilter = BlurMaskFilter(10f, BlurMaskFilter.Blur.NORMAL)
        }
        canvas.drawCircle(size / 2f + 3f, size / 2f + 3f, 52f, shadowPaint)

        // Yellow circle
        val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#FFD600")
        }
        canvas.drawCircle(size / 2f, size / 2f, 52f, bgPaint)

        // Green border
        val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#1E6B3C")
            style = Paint.Style.STROKE
            strokeWidth = 6f
        }
        canvas.drawCircle(size / 2f, size / 2f, 52f, borderPaint)

        // Bus emoji
        val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = 58f
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("🚌", size / 2f, size / 2f + 20f, textPaint)

        return android.graphics.drawable.BitmapDrawable(ctx.resources, bmp)
    }

    private fun createMarkerIcon(ctx: Context, color: Int): android.graphics.drawable.BitmapDrawable {
        val w = 60; val h = 80
        val bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply { this.color = color }

        // Pin circle
        canvas.drawCircle(w / 2f, 28f, 24f, paint)

        // White inner circle
        val innerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            this.color = Color.WHITE
        }
        canvas.drawCircle(w / 2f, 28f, 12f, innerPaint)

        // Pin tail
        val path = Path().apply {
            moveTo(w / 2f - 10f, 46f)
            lineTo(w / 2f + 10f, 46f)
            lineTo(w / 2f, 70f)
            close()
        }
        canvas.drawPath(path, paint)

        return android.graphics.drawable.BitmapDrawable(ctx.resources, bmp)
    }

    // ─── ETA / UI updates ────────────────────────────────────────────────────

    private fun updateUI() {
        val remainingSecs = (totalSeconds - elapsedSeconds).coerceAtLeast(0)
        val remainingMins = (remainingSecs / 60.0).roundToInt().coerceAtLeast(0)
        val progressPct = ((elapsedSeconds.toFloat() / totalSeconds) * 90 + 10).toInt().coerceIn(10, 100)

        binding.tvEtaMinutes.text = remainingMins.toString()
        binding.progressBusRoute.progress = progressPct
        binding.tvProgressPercent.text = "$progressPct%"

        if (elapsedSeconds % 8 == 0) speedIndex = (speedIndex + 1) % speeds.size
        binding.tvSpeed.text = "🚀 ${speeds[speedIndex]} km/h"

        when {
            remainingMins == 0 -> {
                binding.chipBusStatus.text = "✅  Arrived at DSATM"
                binding.chipBusStatus.chipBackgroundColor =
                    android.content.res.ColorStateList.valueOf(Color.parseColor("#E8F5E9"))
            }
            remainingMins <= 3 -> {
                binding.chipBusStatus.text = "🟡  Arriving Soon"
                binding.chipBusStatus.chipBackgroundColor =
                    android.content.res.ColorStateList.valueOf(Color.parseColor("#FFF9C4"))
            }
            else -> {
                binding.chipBusStatus.text = "🟢  On Route"
                binding.chipBusStatus.chipBackgroundColor =
                    android.content.res.ColorStateList.valueOf(Color.parseColor("#E8F5E9"))
            }
        }

        val time = SimpleDateFormat("hh:mm:ss a", Locale.getDefault()).format(Date())
        binding.tvLastUpdate.text = "Live • $time"
    }

    // ─── Lifecycle ────────────────────────────────────────────────────────────

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroyView() {
        handler.removeCallbacks(tickRunnable)
        busAnimator?.cancel()
        mapView.onDetach()
        super.onDestroyView()
        _binding = null
    }
}
