package net.ccbluex.liquidbounce.utils.render

import net.minecraft.client.gui.Gui

object GuiUtils : Gui() {

    @JvmStatic
    fun getzLevel(): Float {
        return zLevel
    }
}