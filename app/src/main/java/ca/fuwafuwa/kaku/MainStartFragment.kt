package ca.fuwafuwa.kaku

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.util.*



class MainStartFragment : Fragment()
{
    private lateinit var mainActivity : MainActivity
    private lateinit var rootView : View

    private lateinit var kakuLogo : TextView
    private lateinit var kakuTitle : TextView
    private lateinit var tutorialText : TextView

    private lateinit var supportText : TextView
    private lateinit var progressBar : ProgressBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        mainActivity = activity as MainActivity

        rootView = inflater.inflate(R.layout.fragment_start, container, false)

        kakuLogo = rootView.findViewById(R.id.kaku_logo)
        kakuTitle = rootView.findViewById(R.id.kaku_title)
        tutorialText = rootView.findViewById(R.id.kaku_tutorial)

        supportText = rootView.findViewById(R.id.support_text)
        progressBar = rootView.findViewById(R.id.progress_bar)

        tutorialText.setOnClickListener {
            startActivity(Intent(mainActivity, TutorialActivity::class.java))
        }

        configureBottomPromo()

        return rootView
    }

    override fun onStart()
    {
        super.onStart()

        supportText.viewTreeObserver.addOnGlobalLayoutListener {
            var pos = IntArray(2)
            supportText.getLocationInWindow(pos)
            val drawableHeight = rootView.height - pos[1] - dpToPx(mainActivity, 30)

            val logoSize = drawableHeight.toFloat() / 2
            val titleSize = logoSize / 5
            val textSize = titleSize / 2

            kakuLogo.setTextSize(TypedValue.COMPLEX_UNIT_PX, logoSize)
            kakuTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize)
            tutorialText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
            supportText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
        }
    }

    override fun onResume()
    {
        super.onResume()

        if (!MainService.IsRunning())
        {
            onKakuLoadStart()
        }

        Timer().schedule(object : TimerTask()
        {
            override fun run()
            {
                mainActivity.runOnUiThread {
                    mainActivity.startKaku(this@MainStartFragment)
                }
            }
        }, 3000)
    }

    fun onKakuLoadStart()
    {
        progressBar.isIndeterminate = true
        progressBar.progress = 0
        supportText.text = getString(R.string.kaku_loading)
    }

    fun onKakuLoaded()
    {
        progressBar.isIndeterminate = false
        progressBar.progress = 100
        writeSupportText()
    }

    private fun configureBottomPromo()
    {
        if (MainService.IsRunning())
        {
            onKakuLoaded()
        }
    }

    private fun writeSupportText()
    {
        supportText.text = getString(R.string.support_text)
    }
}
