package com.example.demo

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import com.getcapacitor.BridgeActivity
import kotlin.jvm.java

class MainActivity : BridgeActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        registerPlugin(EdgeToEdgePlugin::class.java)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
    }
}