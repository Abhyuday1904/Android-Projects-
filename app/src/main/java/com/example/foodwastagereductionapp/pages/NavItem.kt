package com.example.foodwastagereductionapp.pages

import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector


sealed class NavItemIcon {
    data class VectorIcon(val imageVector: ImageVector) : NavItemIcon()
    data class ResourceIcon(val painter: Painter) : NavItemIcon()
}
data class NavItem(
    val label :String ,
    val icon : NavItemIcon,

)