package ca.fuwafuwa.kaku.Windows.Views

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.text.style.LineBackgroundSpan
import android.util.Log

class PitchAccentSpan(private val mPitch: Int, private val mStart: Int, private val mEnd: Int, private val mNumCommas: Int) : LineBackgroundSpan {

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

        val offsetX = left + paint.measureText(text?.subSequence(start, start + mStart).toString()) + (paint.measureText(",") * mNumCommas)

        if (mPitchStart == null || mPitchEnd == null) {
            val textSpan = text?.subSequence(start + mStart + mNumCommas, start + mEnd + mNumCommas)
            val numDots = countSpecialsUntilLimit(textSpan, mPitch)

            mPitchStart = if (mPitch == 1 || mPitch + numDots == textSpan?.length) {
                0F
            } else {
                paint.measureText(textSpan?.get(0)?.toString())
            }

            mPitchEnd = when (mPitch) {
                0 -> paint.measureText(textSpan?.subSequence(1, textSpan.length)?.toString())
                1 -> paint.measureText(textSpan?.subSequence(0, 1)?.toString())
                else -> {
                    Log.d("PITCHOVERLAY", "pitch: $mPitch end: ${mPitch + numDots - 1}")
                    paint.measureText(textSpan?.subSequence(1, mPitch + numDots)?.toString())
                }
            }
        }

        val path = Path()
        path.moveTo(offsetX + mPitchStart!!, top.toFloat() + 3f)
        path.lineTo(offsetX + mPitchStart!! + mPitchEnd!!, top.toFloat() + 3f)
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
        var mutableLimit = limit
        // Ignore dots marking kanji position and small letters
        val specials = listOf('.', ' ', 'ぁ', 'ぃ', 'ぅ', 'ぇ', 'ぉ', 'ァ', 'ィ', 'ゥ', 'ェ', 'ォ', 'ゃ', 'ゅ', 'ょ')
        while (i < mutableLimit) {
            // This theoretically shouldn't happen but it might
            if (mutableLimit >= text.length) {
                return numSpecial
            }

            if (specials.contains(text[i])) {
                mutableLimit++
                numSpecial++
            }
            i++
        }
        return numSpecial
    }
}