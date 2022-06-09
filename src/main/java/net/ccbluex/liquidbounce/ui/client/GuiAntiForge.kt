package net.ccbluex.liquidbounce.ui.client

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.features.special.AntiForge
import net.ccbluex.liquidbounce.utils.extensions.drawCenteredString
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.GuiScreen
import org.lwjgl.input.Keyboard

class GuiAntiForge(private val prevGui: GuiScreen) : GuiScreen() {
    private lateinit var enabledButton: GuiButton
    private lateinit var fmlButton: GuiButton
    private lateinit var proxyButton: GuiButton
    private lateinit var payloadButton: GuiButton

    override fun initGui() {
        buttonList.add(GuiButton(1, width / 2 - 100, height / 4 + 35, "Button").also { enabledButton = it })
        buttonList.add(GuiButton(2, width / 2 - 100, height / 4 + 50 + 25, "Button").also { fmlButton = it })
        buttonList.add(GuiButton(3, width / 2 - 100, height / 4 + 50 + 25 * 2, "Button").also { proxyButton = it })
        buttonList.add(GuiButton(4, width / 2 - 100, height / 4 + 50 + 25 * 3, "Button").also { payloadButton = it })
        buttonList.add(GuiButton(0, width / 2 - 100, height / 4 + 55 + 25 * 4 + 5, "Back"))
        updateButtonStat()
    }

    private fun updateButtonStat() {
        enabledButton.displayString = "Status: " + if (AntiForge.enabled) "§aOn" else "§cOff"
        fmlButton.displayString = "FML Brand: " + if (AntiForge.blockFML) "§aOn" else "§cOff"
        proxyButton.displayString = "FML Proxy Packets: " + if (AntiForge.blockProxyPacket) "§aOn" else "§cOff"
        payloadButton.displayString = "Payload Packets: " + if (AntiForge.blockPayloadPackets) "§aOn" else "§cOff"
    }

    override fun actionPerformed(button: GuiButton) {
        when (button.id) {
            1 -> AntiForge.enabled = !AntiForge.enabled
            2 -> AntiForge.blockFML = !AntiForge.blockFML
            3 -> AntiForge.blockProxyPacket = !AntiForge.blockProxyPacket
            4 -> AntiForge.blockPayloadPackets = !AntiForge.blockPayloadPackets
            0 -> mc.displayGuiScreen(prevGui)
        }
        updateButtonStat()
        if (button.id != 0) {
            LiquidBounce.fileManager.saveConfig(LiquidBounce.fileManager.specialConfig)
        }
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        drawBackground(0)
        mc.fontRendererObj.drawCenteredString("AntiForge", width / 2f, height / 8f + 5f, 4673984, true)
        super.drawScreen(mouseX, mouseY, partialTicks)
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {
        if (Keyboard.KEY_ESCAPE == keyCode) {
            mc.displayGuiScreen(prevGui)
            return
        }
        super.keyTyped(typedChar, keyCode)
    }
}