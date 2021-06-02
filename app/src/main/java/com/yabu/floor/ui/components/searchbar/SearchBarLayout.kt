package com.yabu.floor.ui.components.searchbar

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.HapticFeedbackConstants
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import com.yabu.floor.databinding.ComponentSearchBarBinding

/**
 * SearchBar component with the EditText hidden by default in order to avoid
 * unwanted automatic focus.
 * Listen to a click, the EditText focus and the IME Search action by setting
 * appropriate listeners.
 */
class SearchBarLayout : FrameLayout,
        TextView.OnEditorActionListener,
        OnClickListener {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    interface OnSearchListener {
        fun onSearch(query: String)
    }

    private var binding = ComponentSearchBarBinding.inflate(
        LayoutInflater.from(context),
        this,
        false
    )

    private var onSearchListener: OnSearchListener? = null

    private var onSearchBarFocus: OnFocusChangeListener? = null

    private var onSearchBarClick: OnClickListener? = null

    var query: String = ""

    private val onClearSearchBar = OnClickListener { v ->
        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)

        binding.searchBarEditText.setText("")
        onSearchListener?.onSearch("")
    }

    private val onFocusSearchBar = OnFocusChangeListener { v, hasFocus ->
        onSearchBarFocus?.onFocusChange(v, hasFocus)
    }

    // Text Input listener to toggle the X Close button.
    private val onEditTextInputChanged = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            binding.searchBarClose.visibility = if (s.isNullOrBlank()) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }
    }

    init {
        binding.apply {
            searchBarEditText.setOnEditorActionListener(this@SearchBarLayout)
            searchBarEditText.onFocusChangeListener = onFocusSearchBar
            searchBarClose.setOnClickListener(onClearSearchBar)
            setOnClickListener(this@SearchBarLayout)
            searchBarEditText.addTextChangedListener(onEditTextInputChanged)
        }

        this.addView(binding.root)
    }

    fun getSearchBar(): TextInputEditText {
        return binding.searchBarEditText
    }

    /**
     * Displays a return arrow instead of the icon.
     */
    fun setReturnIcon() {
        binding.searchBarBack.visibility = View.VISIBLE
        binding.searchBarIcon.visibility = View.GONE
    }

    /**
     * Displays a Search Icon in the left side of the Search Bar.
     */
    fun setSearchIcon() {
        binding.searchBarBack.visibility = View.GONE
        binding.searchBarIcon.visibility = View.VISIBLE
    }

    /**
     * Set the EditText to visible and request focus.
     */
    fun setEditTextFocusable() {
        binding.searchBarEditText.visibility = View.VISIBLE
        binding.searchBarHintText.visibility = View.GONE
        binding.searchBarEditText.requestFocus()
    }

    fun setSearchBarReturnClick(listener: OnClickListener) {
        binding.searchBarBack.setOnClickListener(listener)
    }

    fun setSearchBarClick(listener: OnClickListener) {
        onSearchBarClick = listener
    }

    fun setSearchBarFocus(listener: OnFocusChangeListener) {
        onSearchBarFocus = listener
    }

    fun setSearchListener(listener: OnSearchListener?) {
        onSearchListener = listener
    }

    fun setSearchBarQuery(query: String?) {
        this.query = query ?: ""
        binding.searchBarEditText.setText(query)
        // Avoid capturing the focus when set programmatically
        binding.searchBarEditText.clearFocus()
    }

    override fun onClick(v: View?) {
        onSearchBarClick?.onClick(v)
    }

    override fun onEditorAction(view: TextView?, id: Int, event: KeyEvent?): Boolean {
        if (id == EditorInfo.IME_ACTION_SEARCH) {
            val query = binding.searchBarEditText.text.toString().trim()

            onSearchListener?.onSearch(query)
            return true
        }
        return false
    }
}