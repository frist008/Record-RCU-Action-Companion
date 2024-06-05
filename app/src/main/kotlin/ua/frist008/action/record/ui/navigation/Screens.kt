package ua.frist008.action.record.ui.navigation

sealed class NavCommand(
    route: String? = null,
    val replaceScreen: Boolean = false,
) {

    val route by lazy(LazyThreadSafetyMode.NONE) { route ?: getStubRoute() }

    open fun getStubRoute() = this::class.simpleName!!

    override fun toString() = route

    class BackCommand(screen: NavCommand? = null) : NavCommand(screen?.route) {

        override fun getStubRoute() = ""
    }

    object DevicesScreen : NavCommand()
    object RecordScreen : NavCommand()
    object SettingsScreen : NavCommand()

    companion object {

        const val ROOT_LAYER = "ROOT_LAYER"
    }
}
