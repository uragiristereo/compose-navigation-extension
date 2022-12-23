package com.uragiristereo.navigation.compose.extension

import androidx.compose.runtime.Composable
import androidx.core.net.toUri
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.Navigator
import androidx.navigation.compose.composable
import androidx.navigation.navOptions

fun NavHostController.navigate(
    route: NavigationRoute,
    data: Map<String, Any> = mapOf(),
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
) {
    val uri = route.parseData(data)

    try {
        navigate(
            request = NavDeepLinkRequest.Builder
                .fromUri(NavDestination.createRoute(uri).toUri())
                .build(),
            navOptions = navOptions,
            navigatorExtras = navigatorExtras,
        )
    } catch (e: IllegalArgumentException) {
        // When the data is too large it usually throws IllegalArgumentException "Navigation destination that matches request cannot be found"
        // So we're printing the error instead

        route.printNavigationError(e)
    }
}

fun NavHostController.navigate(
    route: NavigationRoute,
    data: Map<String, Any> = mapOf(),
    builder: NavOptionsBuilder.() -> Unit,
) {
    navigate(
        route = route,
        data = data,
        navOptions = navOptions(builder),
    )
}

fun NavGraphBuilder.composable(
    route: NavigationRoute,
    deepLinks: List<NavDeepLink> = listOf(),
    content: @Composable NavigationRoute.(NavBackStackEntry) -> Unit,
) {
    composable(
        route = route.route,
        arguments = route.getNamedNavArgs(),
        deepLinks = deepLinks,
        content = { content(route, it) },
    )
}
