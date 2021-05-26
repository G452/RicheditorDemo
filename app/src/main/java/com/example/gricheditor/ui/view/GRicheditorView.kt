package com.example.gricheditor.ui.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.example.gricheditor.R
import com.example.gricheditor.extentions.setVisible
import kotlinx.android.synthetic.main.rich_editor_layout.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick

/**
 * author：G
 * time：2021/4/28 13:54
 * about：
 **/
class GRicheditorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = -1
) : FrameLayout(context, attrs, defStyleAttr) {
    private val imgURL: String = "https://avatar.csdnimg.cn/1/9/7/1_qq_43143981_1552988521.jpg"

    init {
        LayoutInflater.from(context).inflate(R.layout.rich_editor_layout, this)
        viewTreeObserver.addOnGlobalFocusChangeListener { _, _ -> richBtns.setVisible(hasFocus()) }
        initClick()
    }

    private fun initClick() {
        var isChangedTextColor = false
        var isChangedBgColor = false
        action_undo.onClick { mEditor.undo() }
        action_redo.onClick { mEditor.redo() }
        action_bold.onClick { mEditor.setBold() }
        action_italic.onClick { mEditor.setItalic() }
        action_subscript.onClick { mEditor.setSubscript() }
        action_superscript.onClick { mEditor.setSuperscript() }
        action_strikethrough.onClick { mEditor.setStrikeThrough() }
        action_underline.onClick { mEditor.setUnderline() }
        action_heading1.onClick { mEditor.setHeading(1) }
        action_heading2.onClick { mEditor.setHeading(2) }
        action_heading3.onClick { mEditor.setHeading(3) }
        action_heading4.onClick { mEditor.setHeading(4) }
        action_heading5.onClick { mEditor.setHeading(5) }
        action_heading6.onClick { mEditor.setHeading(6) }
        action_indent.onClick { mEditor.setIndent() }
        action_outdent.onClick { mEditor.setOutdent() }
        action_align_left.onClick { mEditor.setAlignLeft() }
        action_align_center.onClick { mEditor.setAlignCenter() }
        action_align_right.onClick { mEditor.setAlignRight() }
        action_insert_bullets.onClick { mEditor.setBullets() }
        action_insert_numbers.onClick { mEditor.setNumbers() }
        action_blockquote.onClick { mEditor.setBlockquote() }
        action_insert_audio.onClick { mEditor.insertAudio("https://file-examples-com.github.io/uploads/2017/11/file_example_MP3_5MG.mp3") }
        action_insert_video.onClick {
            mEditor.insertVideo(
                "https://test-videos.co.uk/vids/bigbuckbunny/mp4/h264/1080/Big_Buck_Bunny_1080_10s_10MB.mp4",
                360
            )
        }
        action_insert_link.onClick { mEditor.insertLink("https://github.com/G452", "G") }
        action_insert_checkbox.onClick { mEditor.insertTodo() }
        action_insert_image.onClick { mEditor.insertImage(imgURL, "", 320) }
        action_txt_color.onClick {
            mEditor.setTextColor(if (isChangedTextColor) Color.BLACK else Color.RED)
            isChangedTextColor = !isChangedTextColor
        }
        action_bg_color.onClick {
            mEditor.setTextBackgroundColor(if (isChangedBgColor) Color.TRANSPARENT else Color.YELLOW)
            isChangedBgColor = !isChangedBgColor
        }
    }

    fun setEditPadding(left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0) {
        mEditor.setPadding(left, top, right, bottom)
    }

    fun setPlaceholder(text: Any) {
        mEditor.setPlaceholder(text.toString())
    }

    fun getHtml(): String? {
        return mEditor.html
    }


}