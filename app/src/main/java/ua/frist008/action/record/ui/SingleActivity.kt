package ua.frist008.action.record.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import ua.frist008.action.record.ui.component.RootComponent
import ua.frist008.action.record.ui.theme.RootTheme

class SingleActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RootTheme {
                RootComponent()
            }
        }
    }
}
