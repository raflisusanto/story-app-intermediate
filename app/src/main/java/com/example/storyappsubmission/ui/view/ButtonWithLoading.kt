package com.example.storyappsubmission.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.example.storyappsubmission.databinding.ButtonWithLoadingBinding

class ButtonWithLoading @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: ButtonWithLoadingBinding =
        ButtonWithLoadingBinding.inflate(LayoutInflater.from(context), this, true)
    private var initialLabel: String = ""

    fun setButtonLabel(label: String) {
        initialLabel = label
        binding.raisedBtn.text = label
    }

    fun setOnClick(onClick: OnClickListener) {
        binding.raisedBtn.setOnClickListener(onClick)
    }

    fun showLoading() {
        with(binding) {
            pbLoading.visibility = VISIBLE
            raisedBtn.text = ""
            raisedBtn.isEnabled = false
        }
    }

    fun hideLoading() {
        with(binding) {
            pbLoading.visibility = GONE
            raisedBtn.text = initialLabel
            raisedBtn.isEnabled = true
        }
    }
}
