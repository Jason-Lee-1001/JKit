package com.jstudio.jkit

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment

/**
 * Created by Jason
 */
inline fun <reified T : Fragment> newFragment(vararg pairs: Pair<String, Any?>): T {
    val fragment: T = T::class.java.newInstance()
    if (pairs.isNotEmpty()) fragment.arguments = bundleOf(*pairs)
    return fragment
}