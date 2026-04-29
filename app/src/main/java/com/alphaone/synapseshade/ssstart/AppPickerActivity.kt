package com.alphaone.synapseshade.ssstart

import android.content.pm.ApplicationInfo
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.leanback.app.GuidedStepSupportFragment
import androidx.leanback.widget.GuidanceStylist
import androidx.leanback.widget.GuidedAction

class AppPickerActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GuidedStepSupportFragment.addAsRoot(this, PickerFragment(), android.R.id.content)
    }
    class PickerFragment : GuidedStepSupportFragment() {
        override fun onCreateGuidance(savedInstanceState: Bundle?) =
            GuidanceStylist.Guidance(getString(R.string.choose_app), "", "", null)

        override fun onCreateActions(actions: MutableList<GuidedAction>, savedInstanceState: Bundle?) {
            val pm = requireContext().packageManager
            val apps = pm.getInstalledApplications(0)
                .filter { it.flags and ApplicationInfo.FLAG_SYSTEM == 0 || pm.getLaunchIntentForPackage(it.packageName) != null }
                .sortedBy { pm.getApplicationLabel(it).toString() }

            apps.forEach { app ->
                val label = pm.getApplicationLabel(app).toString()
                actions.add(GuidedAction.Builder(requireContext())
                    .id(app.packageName.hashCode().toLong())
                    .title(label)
                    .description(app.packageName)
                    .build())
            }
        }
        override fun onGuidedActionClicked(action: GuidedAction) {
            requireContext().getSharedPreferences("ssstart_cfg", 0).edit()
                .putString("target_pkg", action.description.toString())
                .putString("target_label", action.title.toString())
                .apply()
            activity?.finish()
        }
    }
}
