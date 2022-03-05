package com.ratings.app.ui.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.appcompat.widget.AppCompatRadioButton
import android.widget.CompoundButton
import android.view.accessibility.AccessibilityNodeInfo
import android.view.accessibility.AccessibilityEvent
import com.ratings.app.R
import java.util.concurrent.atomic.AtomicInteger

class RadioGridGroup : GridLayout {
    var checkedCheckableImageButtonId = NOTHING_CHECKED
        private set
    private var childOnCheckedChangeListener: CompoundButton.OnCheckedChangeListener? = null
    private var protectFromCheckedChange = false
    private var onCheckedChangeListener: OnCheckedChangeListener? = null
    private var passThroughListener: PassThroughHierarchyChangeListener? = null

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    private fun init() {
        childOnCheckedChangeListener = CheckedStateTracker()
        passThroughListener = PassThroughHierarchyChangeListener()
        super.setOnHierarchyChangeListener(passThroughListener)
    }

    override fun setOnHierarchyChangeListener(listener: OnHierarchyChangeListener) {
        passThroughListener?.mOnHierarchyChangeListener = listener
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (checkedCheckableImageButtonId != NOTHING_CHECKED) {
            protectFromCheckedChange = true
            setCheckedStateForView(checkedCheckableImageButtonId, true)
            protectFromCheckedChange = false
            setCheckedId(checkedCheckableImageButtonId)
        }
    }

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        if (child is AppCompatRadioButton) {
            if (child.isChecked) {
                protectFromCheckedChange = true
                if (checkedCheckableImageButtonId != NOTHING_CHECKED) {
                    setCheckedStateForView(checkedCheckableImageButtonId, false)
                }
                protectFromCheckedChange = false
                setCheckedId(child.id)
            }
        }
        super.addView(child, index, params)
    }

    private fun setCheckedId(id: Int) {
        checkedCheckableImageButtonId = id
        if (onCheckedChangeListener != null) {
            onCheckedChangeListener?.onCheckedChanged(this, checkedCheckableImageButtonId)
        }
    }

    private fun setCheckedStateForView(viewId: Int, checked: Boolean) {
        val checkedView = findViewById<AppCompatRadioButton>(viewId)

        if (checkedView != null && checkedView is AppCompatRadioButton) {
            checkedView.isChecked = checked
            if(!checked) checkedView.setTextColor(resources.getColor(R.color.black))
        }
    }

    override fun onInitializeAccessibilityEvent(event: AccessibilityEvent) {
        super.onInitializeAccessibilityEvent(event)
        event.className = RadioGridGroup::class.java.name
    }

    override fun onInitializeAccessibilityNodeInfo(info: AccessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(info)
        info.className = RadioGridGroup::class.java.name
    }

    interface OnCheckedChangeListener {
        fun onCheckedChanged(group: RadioGridGroup?, checkedId: Int)
    }

    private inner class CheckedStateTracker : CompoundButton.OnCheckedChangeListener {
        override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
            if (protectFromCheckedChange) {
                return
            }
            protectFromCheckedChange = true
            if (checkedCheckableImageButtonId != NOTHING_CHECKED) {
                setCheckedStateForView(checkedCheckableImageButtonId, false)
            }
            buttonView.setTextColor(resources.getColor(R.color.white))
            protectFromCheckedChange = false
            val id = buttonView.id
            setCheckedId(id)
        }
    }

    private inner class PassThroughHierarchyChangeListener : OnHierarchyChangeListener {
        var mOnHierarchyChangeListener: OnHierarchyChangeListener? = null
        override fun onChildViewAdded(parent: View, child: View) {
            if (parent === this@RadioGridGroup && child is AppCompatRadioButton) {
                var id = child.getId()
                // generates an id if it's missing
                if (id == NO_ID) {
                    id = generateViewId()
                    child.setId(id)
                }
                child.setOnCheckedChangeListener(
                    childOnCheckedChangeListener)
            }
            mOnHierarchyChangeListener?.onChildViewAdded(parent, child)
        }

        override fun onChildViewRemoved(parent: View, child: View) {
            if (parent === this@RadioGridGroup && child is AppCompatRadioButton) {
                child.setOnCheckedChangeListener(null)
            }
            mOnHierarchyChangeListener?.onChildViewRemoved(parent, child)
        }
    }

    companion object {
        const val NOTHING_CHECKED = -1
        private const val TRANSPARENT_COLOR = 0x00FFFFFF

        private val nextGeneratedId = AtomicInteger(1)
        fun generateViewId(): Int {
            while (true) {
                val result = nextGeneratedId.get()

                // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
                var newValue = result + 1
                if (newValue > TRANSPARENT_COLOR) newValue = 1 // Roll over to 1, not 0.
                if (nextGeneratedId.compareAndSet(result, newValue)) {
                    return result
                }
            }
        }
    }
}