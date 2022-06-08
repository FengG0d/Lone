package net.ccbluex.liquidbounce.features.module.modules.client

import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.client.audio.PositionedSoundRecord
import net.minecraft.util.ResourceLocation

@ModuleInfo(name = "Modules", category = ModuleCategory.CLIENT, canEnable = false)
object Modules : Module() {
    val toggleIgnoreScreenValue = BoolValue("ToggleIgnoreScreen", false)

    fun playSound(enable: Boolean) {
        mc.soundHandler.playSound(PositionedSoundRecord.create(ResourceLocation("random.click"), 1F))
    }
}