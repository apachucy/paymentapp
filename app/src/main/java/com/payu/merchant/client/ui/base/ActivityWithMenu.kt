package com.payu.merchant.client.ui.base

import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.payu.merchant.client.R

abstract class ActivityWithMenu : AppCompatActivity() {

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_demo_samples, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_sample_about -> {
            true
        }
        R.id.action_change_theme -> {
            ChangeThemeDialog.showThemeDialog(supportFragmentManager)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}