package com.example.demo

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import com.getcapacitor.BridgeActivity
import com.getcapacitor.community.safearea.SafeAreaPlugin

class MainActivity : BridgeActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        SafeAreaPlugin.setSystemBarsStyle(this, SafeAreaPlugin.SystemBarsStyle.LIGHT)
    }
}