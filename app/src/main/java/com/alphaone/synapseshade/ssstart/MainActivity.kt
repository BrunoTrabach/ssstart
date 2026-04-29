package com.alphaone.synapseshade.ssstart

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.leanback.app.GuidedStepSupportFragment
import androidx.leanback.widget.GuidanceStylist
import androidx.leanback.widget.GuidedAction

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GuidedStepSupportFragment.addAsRoot(this, MainFragment(), android.R.id.content)
    }

    class MainFragment : GuidedStepSupportFragment() {
        override fun onCreateGuidance(savedInstanceState: Bundle?): GuidanceStylist.Guidance {
            return GuidanceStylist.Guidance(
                getString(R.string.app_name),
                getString(R.string.desc),
                "",
                null
            )
        }
        override fun onCreateActions(actions: MutableList<GuidedAction>, savedInstanceState: Bundle?) {
            val prefs = requireContext().getSharedPreferences("ssstart_cfg", 0)
            val enabled = prefs.getBoolean("enabled", true)
            
            actions.add(GuidedAction.Builder(requireContext())
                .id(1).title(getString(R.string.toggle)).checkSetId(1)
                .checked(enabled).build())
            actions.add(GuidedAction.Builder(requireContext())
                .id(2).title(getString(R.string.choose_app)).description(prefs.getString("target_label", "")).build())
            actions.add(GuidedAction.Builder(requireContext())
                .id(3).title(getString(R.string.test_boot)).build())
        }
        override fun onGuidedActionClicked(action: GuidedAction) {
            val prefs = requireContext().getSharedPreferences("ssstart_cfg", 0)
            when(action.id) {
                1L -> prefs.edit().putBoolean("enabled", action.isChecked).apply()
                2L -> startActivity(Intent(requireContext(), AppPickerActivity::class.java))
                3L -> requireContext().sendBroadcast(Intent(Intent.ACTION_BOOT_COMPLETED))
            }
        }
    }
}
