package ca.fuwafuwa.kaku.Windows.Views

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.text.style.LineBackgroundSpan

class PitchAccentSpan(private val mPitch: Int, private val mStart: Int, private val mEnd: Int) : LineBackgroundSpan {

    private val p = Paint()
    private var mPitchStart: Float? = null
    private var mPitchEnd: Float? = null

    init {
        p.color = Color.DKGRAY
        p.style = Paint.Style.STROKE
        p.strokeWidth = 3F
    }

    override fun drawBackground(canvas: Canvas?, paint: Paint?, left: Int, right: Int, top: Int, baseline: Int, bottom: Int, text: CharSequence?, start: Int, end: Int, lnum: Int) {
        if (paint == null) {
            return
        }

        val offsetX = left + paint.measureText(text?.subSequence(start, start + mStart).toString())

        if (mPitchStart == null || mPitchEnd == null) {
            val textSpan = text?.subSequence(start + mStart, start + mEnd)
            val numDots = countSpecialsUntilLimit(textSpan, mPitch)

            mPitchStart = if (mPitch == 1 || mPitch + numDots == textSpan?.length) {
                0F
            } else {
                paint.measureText(textSpan?.get(0)?.toString())
            }

            mPitchEnd = if (mPitch == 0) {
                paint.measureText(textSpan?.subSequence(1, textSpan.length)?.toString())
            }
            else {
                paint.measureText(textSpan?.subSequence(0, mPitch + numDots)?.toString())
            }
        }

        val path = Path()
        path.moveTo(offsetX + mPitchStart!!, top.toFloat())
        path.lineTo(offsetX + mPitchStart!! + mPitchEnd!!, top.toFloat())
        if (mPitch != 0) {
            path.lineTo(offsetX + mPitchStart!! + mPitchEnd!!, top + ((bottom - top) * 0.25f))
        }
        canvas?.drawPath(path, p)
    }

    fun countSpecialsUntilLimit(text: CharSequence?, limit: Int) : Int {
        if (text == null) {
            return 0
        }

        var i = 0
        var numSpecial = 0
        // Ignore dots marking kanji position and small letters
        val specials = listOf('.', 'ぁ', 'ぃ', 'ぅ', 'ぇ', 'お', 'ァ', 'ィ', 'ゥ', 'ェ', 'ォ')
        while (i < limit) {
            if (specials.contains(text[i])) {
                numSpecial++
                i--
            }
            i++
        }
        return numSpecial
    }
}