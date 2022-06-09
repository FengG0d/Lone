package net.ccbluex.liquidbounce.features.module.modules.client

import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.ui.client.scriptMenu.GuiScriptLoadMenu

@ModuleInfo(name = "ScriptManager", category = ModuleCategory.CLIENT, canEnable = false)
class ScriptManager : Module() {
    override fun onEnable() {
        mc.displayGuiScreen(GuiScriptLoadMenu())
    }
}