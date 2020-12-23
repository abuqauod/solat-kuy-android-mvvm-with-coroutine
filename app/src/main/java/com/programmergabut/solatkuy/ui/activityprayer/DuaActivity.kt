package com.programmergabut.solatkuy.ui.activityprayer

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import com.programmergabut.solatkuy.R
import com.programmergabut.solatkuy.base.BaseActivity
import com.programmergabut.solatkuy.databinding.ActivityDuaBinding
import com.programmergabut.solatkuy.util.LogConfig.Companion.ERROR
import java.lang.Exception

class DuaActivity : BaseActivity<ActivityDuaBinding, ViewModel>(R.layout.activity_dua, null) {

    companion object{
        const val DUA_TITLE = "duaTitle"
        const val DUA_AR = "duaAr"
        const val DUA_LT = "duaLt"
        const val DUA_EN = "duaEn"
        const val DUA_IN = "duaIn"
        const val DUA_REF = "duaRef"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setListener()
    }

    override fun setListener(){
        try {
            binding.tvPrayerTitle.text = intent.getStringExtra(DUA_TITLE) ?: throw Exception("DuaActivity getExtras $DUA_TITLE")
            binding.tvPrayerAr.text = intent.getStringExtra(DUA_AR) ?: throw Exception("DuaActivity getExtras $DUA_AR")
            binding.tvPrayerLt.text = intent.getStringExtra(DUA_LT) ?: throw Exception("DuaActivity getExtras $DUA_LT")
            binding.tvPrayerEn.text = intent.getStringExtra(DUA_EN) ?: throw Exception("DuaActivity getExtras $DUA_EN")
            binding.tvPrayerIn.text = intent.getStringExtra(DUA_IN) ?: throw Exception("DuaActivity getExtras $DUA_IN")
            binding.tvPrayerRef.text = intent.getStringExtra(DUA_REF) ?: throw Exception("DuaActivity getExtras $DUA_REF")
        }
        catch (ex: Exception){
            Log.d(ERROR, ex.message.toString())
            showBottomSheet(isCancelable = false, isFinish = true)
        }
    }



}