package com.nammashale.shalepride.ui.home

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.sin

/**
 * Custom View that draws a realistic-looking map for the bus tracker.
 * No Google Maps API needed — pure Canvas drawing.
 *
 * Draws:
 * - Green park areas and buildings
 * - Grey road network
 * - Bus route with dashed line
 * - Animated bus icon moving along the route
 * - Stop markers
 * - School destination marker
 */
class BusMapView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // ─── Paint Objects ─────────────────────────────────────────────────────

    private val bgPaint = Paint().apply {
        color = Color.parseColor("#E8F0E8")  // Light greenish map background
        style = Paint.Style.FILL
    }
    private val roadPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#FFFFFF")
        style = Paint.Style.STROKE
        strokeWidth = 18f
        strokeCap = Paint.Cap.ROUND
    }
    private val roadBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#CCCCCC")
        style = Paint.Style.STROKE
        strokeWidth = 22f
        strokeCap = Paint.Cap.ROUND
    }
    private val mainRoadPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#FAFAFA")
        style = Paint.Style.STROKE
        strokeWidth = 28f
        strokeCap = Paint.Cap.ROUND
    }
    private val mainRoadBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#BBBBBB")
        style = Paint.Style.STROKE
        strokeWidth = 33f
        strokeCap = Paint.Cap.ROUND
    }
    private val buildingPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#D4C5A9")
        style = Paint.Style.FILL
    }
    private val buildingBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#B0A090")
        style = Paint.Style.STROKE
        strokeWidth = 1f
    }
    private val parkPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#B5D5A0")
        style = Paint.Style.FILL
    }
    private val waterPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#AED6F1")
        style = Paint.Style.FILL
    }
    private val routePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#1E6B3C")
        style = Paint.Style.STROKE
        strokeWidth = 7f
        strokeCap = Paint.Cap.ROUND
        pathEffect = DashPathEffect(floatArrayOf(20f, 10f), 0f)
    }
    private val routeGlowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#4CAF50")
        style = Paint.Style.STROKE
        strokeWidth = 12f
        strokeCap = Paint.Cap.ROUND
        alpha = 60
    }
    private val stopPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }
    private val stopBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#1E6B3C")
        style = Paint.Style.STROKE
        strokeWidth = 3f
    }
    private val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#333333")
        textSize = 24f
        typeface = Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD)
    }
    private val smallLabelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#555555")
        textSize = 18f
    }
    private val schoolPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#1E6B3C")
        style = Paint.Style.FILL
    }

    // ─── Route State ───────────────────────────────────────────────────────

    // Bus position along route: 0.0f (start) → 1.0f (school)
    var busProgress: Float = 0.08f
        set(value) {
            field = value.coerceIn(0f, 1f)
            invalidate()
        }

    private val routePath = Path()
    private val routePoints = mutableListOf<PointF>()
    private var pathMeasure: PathMeasure? = null
    private var pathLength = 0f

    // ─── Drawing ───────────────────────────────────────────────────────────

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        buildRoute(w.toFloat(), h.toFloat())
    }

    private fun buildRoute(w: Float, h: Float) {
        routePath.reset()
        routePoints.clear()

        // Bus route: starts top-left, winds through roads to school (center-right)
        val pts = listOf(
            PointF(w * 0.10f, h * 0.15f),   // Stop 1: Pickup start
            PointF(w * 0.25f, h * 0.15f),   // → Road right
            PointF(w * 0.25f, h * 0.32f),   // ↓ Turn down
            PointF(w * 0.42f, h * 0.32f),   // → Road right
            PointF(w * 0.42f, h * 0.50f),   // ↓ Turn down
            PointF(w * 0.60f, h * 0.50f),   // → Road right
            PointF(w * 0.60f, h * 0.38f),   // ↑ Turn up
            PointF(w * 0.78f, h * 0.38f)    // → School
        )
        routePoints.addAll(pts)

        routePath.moveTo(pts[0].x, pts[0].y)
        for (i in 1 until pts.size) {
            routePath.lineTo(pts[i].x, pts[i].y)
        }

        pathMeasure = PathMeasure(routePath, false)
        pathLength = pathMeasure?.length ?: 0f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val w = width.toFloat()
        val h = height.toFloat()
        if (w == 0f || h == 0f) return

        // 1. Background
        canvas.drawRect(0f, 0f, w, h, bgPaint)

        // 2. Parks / green areas
        canvas.drawRect(w * 0.05f, h * 0.55f, w * 0.30f, h * 0.80f, parkPaint)
        canvas.drawRect(w * 0.65f, h * 0.55f, w * 0.92f, h * 0.78f, parkPaint)
        canvas.drawRect(w * 0.38f, h * 0.05f, w * 0.58f, h * 0.22f, parkPaint)

        // 3. Water body
        canvas.drawOval(w * 0.08f, h * 0.58f, w * 0.26f, h * 0.72f, waterPaint)

        // 4. Buildings (grey rectangles)
        drawBuildings(canvas, w, h)

        // 5. Roads (border then fill)
        drawRoads(canvas, w, h)

        // 6. Route glow (behind dashed line)
        canvas.drawPath(routePath, routeGlowPaint)

        // 7. Dashed route line
        canvas.drawPath(routePath, routePaint)

        // 8. Stop markers
        drawStops(canvas)

        // 9. School marker at end
        val school = routePoints.lastOrNull() ?: return
        drawSchoolMarker(canvas, school.x, school.y)

        // 10. Bus icon at current position
        drawBus(canvas)
    }

    private fun drawBuildings(canvas: Canvas, w: Float, h: Float) {
        val buildings = listOf(
            RectF(w * 0.05f, h * 0.05f, w * 0.18f, h * 0.13f),
            RectF(w * 0.31f, h * 0.05f, w * 0.37f, h * 0.14f),
            RectF(w * 0.60f, h * 0.06f, w * 0.72f, h * 0.16f),
            RectF(w * 0.75f, h * 0.05f, w * 0.90f, h * 0.15f),
            RectF(w * 0.05f, h * 0.38f, w * 0.20f, h * 0.52f),
            RectF(w * 0.30f, h * 0.40f, w * 0.38f, h * 0.50f),
            RectF(w * 0.45f, h * 0.58f, w * 0.58f, h * 0.70f),
            RectF(w * 0.62f, h * 0.82f, w * 0.78f, h * 0.92f),
            RectF(w * 0.32f, h * 0.82f, w * 0.45f, h * 0.92f),
        )
        for (r in buildings) {
            canvas.drawRoundRect(r, 4f, 4f, buildingPaint)
            canvas.drawRoundRect(r, 4f, 4f, buildingBorderPaint)
        }
    }

    private fun drawRoads(canvas: Canvas, w: Float, h: Float) {
        // Horizontal roads
        for (yFrac in listOf(0.15f, 0.32f, 0.50f, 0.65f, 0.82f)) {
            val y = h * yFrac
            canvas.drawLine(0f, y, w, y, roadBorderPaint)
            canvas.drawLine(0f, y, w, y, roadPaint)
        }
        // Vertical roads
        for (xFrac in listOf(0.25f, 0.42f, 0.60f, 0.78f)) {
            val x = w * xFrac
            canvas.drawLine(x, 0f, x, h, roadBorderPaint)
            canvas.drawLine(x, 0f, x, h, roadPaint)
        }
        // Main road (horizontal center)
        val mainY = h * 0.38f
        canvas.drawLine(0f, mainY, w, mainY, mainRoadBorderPaint)
        canvas.drawLine(0f, mainY, w, mainY, mainRoadPaint)
    }

    private fun drawStops(canvas: Canvas) {
        // Draw stop markers at intermediate points (not first or last)
        val stopIndices = listOf(1, 3, 5)
        val stopLabels = listOf("Stop 1", "Stop 2", "Stop 3")
        for (i in stopIndices.indices) {
            val pt = routePoints.getOrNull(stopIndices[i]) ?: continue
            canvas.drawCircle(pt.x, pt.y, 14f, stopPaint)
            canvas.drawCircle(pt.x, pt.y, 14f, stopBorderPaint)
            // Small dot in center
            canvas.drawCircle(pt.x, pt.y, 4f, stopBorderPaint)
            // Label below
            canvas.drawText(stopLabels[i], pt.x - 25f, pt.y + 32f, smallLabelPaint)
        }
    }

    private fun drawSchoolMarker(canvas: Canvas, x: Float, y: Float) {
        // Shadow
        val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#33000000")
        }
        canvas.drawCircle(x + 3f, y + 3f, 26f, shadowPaint)

        // Pin circle
        canvas.drawCircle(x, y, 26f, schoolPaint)

        // School emoji text
        val emojiPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = 28f
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("🏫", x, y + 10f, emojiPaint)

        // "School" label
        val bgRect = RectF(x - 36f, y + 32f, x + 36f, y + 54f)
        val bgPaint2 = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#1E6B3C")
        }
        canvas.drawRoundRect(bgRect, 8f, 8f, bgPaint2)
        val txtPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            textSize = 16f
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD)
        }
        canvas.drawText("SCHOOL", x, y + 49f, txtPaint)
    }

    private fun drawBus(canvas: Canvas) {
        val pm = pathMeasure ?: return
        val pos = FloatArray(2)
        val tan = FloatArray(2)
        pm.getPosTan(pathLength * busProgress, pos, tan)
        val bx = pos[0]
        val by = pos[1]

        // Shadow
        val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#44000000")
            maskFilter = BlurMaskFilter(12f, BlurMaskFilter.Blur.NORMAL)
        }
        canvas.drawCircle(bx + 4f, by + 4f, 28f, shadowPaint)

        // Bus circle background
        val busBgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#FFD600")  // Bright yellow for visibility
        }
        canvas.drawCircle(bx, by, 28f, busBgPaint)

        // Green border
        val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#1E6B3C")
            style = Paint.Style.STROKE
            strokeWidth = 3f
        }
        canvas.drawCircle(bx, by, 28f, borderPaint)

        // Bus emoji
        val emojiPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = 30f
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("🚌", bx, by + 10f, emojiPaint)
    }
}
