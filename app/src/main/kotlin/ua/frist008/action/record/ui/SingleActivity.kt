package ua.frist008.action.record.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.graphics.toArgb
import dagger.hilt.android.AndroidEntryPoint
import ua.frist008.action.record.ui.surface.RootSurface
import ua.frist008.action.record.ui.theme.color.Palette

@AndroidEntryPoint class SingleActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Palette.PURPLE_DARK.toArgb()),
            navigationBarStyle = SystemBarStyle.dark(Palette.TRANSPARENT.toArgb()),
        )
        super.onCreate(savedInstanceState)
        setContent { RootSurface() }
    }
}
