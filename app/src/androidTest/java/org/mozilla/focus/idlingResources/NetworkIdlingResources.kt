/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.focus.idlingResources

import androidx.test.espresso.IdlingResource
import org.mozilla.focus.helpers.TestHelper.mDevice

/**
 * An IdlingResource implementation that waits until the network connection is online or offline.
 * The networkConnected parameter sets the expected connection status.
 * Only after connecting/disconnecting has completed further actions will be performed.
 */

class NetworkIdlingResources(private val networkConnected: Boolean) : IdlingResource {
    private var resourceCallback: IdlingResource.ResourceCallback? = null

    override fun getName(): String {
        return this::javaClass.name
    }

    override fun isIdleNow(): Boolean {
        val idle =
            if (networkConnected) {
                isOnline()
            } else {
                !isOnline()
            }
        if (idle)
            resourceCallback?.onTransitionToIdle()
        return idle
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        if (callback != null)
            resourceCallback = callback
    }

    private fun isOnline(): Boolean {
        val dataStatus = mDevice.executeShellCommand("settings get global mobile_data")
        val wifiStatus = mDevice.executeShellCommand("settings get global wifi_on")
        return(dataStatus.equals("1") && wifiStatus.equals("1"))
    }
}