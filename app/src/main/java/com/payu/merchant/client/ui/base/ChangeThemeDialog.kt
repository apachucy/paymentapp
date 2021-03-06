package com.payu.merchant.client.ui.base

import android.app.AlertDialog
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.payu.merchant.client.R

private const val DAY_MODE_POSITION = 0
private const val NIGHT_MODE_POSITION = 1

const val THEME_DIALOG_TAG = "ThemeDialog"

class ChangeThemeDialog : DialogFragment() {

    companion object {
        fun showThemeDialog(fragmentManager: FragmentManager) {
            ChangeThemeDialog().apply {
                show(fragmentManager, THEME_DIALOG_TAG)
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val themeModes = arrayOf(
            resources.getString(R.string.theme_light),
            resources.getString(R.string.theme_dark),
            resources.getString(R.string.theme_default)
        )
        return activity?.let {
            AlertDialog.Builder(it)
                .setTitle(R.string.choose_theme_title)
                .setItems(themeModes) { _, which ->
                    when (which) {
                        DAY_MODE_POSITION -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        NIGHT_MODE_POSITION -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    }
                    dismiss()
                }
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}