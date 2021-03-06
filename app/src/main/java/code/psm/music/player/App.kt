/*
 * Copyright (c) 2020 Hemanth Savarla.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 */
package code.psm.music.player

import android.app.Application
import android.widget.Toast
import code.psm.music.appthemehelper.ThemeStore
import code.psm.music.appthemehelper.util.VersionUtils
import code.psm.music.player.Constants.PRO_VERSION_PRODUCT_ID
import code.psm.music.player.appshortcuts.DynamicShortcutManager
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

//    lateinit var billingProcessor: BillingProcessor

    override fun onCreate() {
        super.onCreate()
        instance = this

        startKoin {
            androidContext(this@App)
            modules(appModules)
        }
        // default theme
        if (!ThemeStore.isConfigured(this, 3)) {
            ThemeStore.editTheme(this)
                .accentColorRes(R.color.color_splash)
                .coloredNavigationBar(true)
                .commit()
        }

        if (VersionUtils.hasNougatMR())
            DynamicShortcutManager(this).initDynamicShortcuts()

        // automatically restores purchases
//        billingProcessor = BillingProcessor(this, BuildConfig.GOOGLE_PLAY_LICENSING_KEY,
//            object : BillingProcessor.IBillingHandler {
//                override fun onProductPurchased(productId: String, details: TransactionDetails?) {}
//
//                override fun onPurchaseHistoryRestored() {
////                    Toast.makeText(
////                        this@App,
////                        R.string.restored_previous_purchase_please_restart,
////                        Toast.LENGTH_LONG
////                    ).show()
//                }
//
//                override fun onBillingError(errorCode: Int, error: Throwable?) {}
//
//                override fun onBillingInitialized() {}
//            })
    }

    override fun onTerminate() {
        super.onTerminate()
        //billingProcessor.release()
    }

    companion object {
        private var instance: App? = null

        fun getContext(): App {
            return instance!!
        }

        fun isProVersion(): Boolean {
            return true

        }
    }
}
