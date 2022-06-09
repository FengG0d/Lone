package net.ccbluex.liquidbounce.features.module.modules.misc

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.event.WorldEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.network.play.server.S02PacketChat
import net.minecraft.network.play.server.S14PacketEntity
import java.util.regex.Pattern

@ModuleInfo(name = "GetName", description = "faq", category = ModuleCategory.MISC)
class GetName : Module() {
    private val playerName: MutableList<String> = ArrayList()
    private val ground = mutableListOf<Int>()
    private val modeValue = ListValue("Mode", arrayOf("4V4/1V1", "32V32/64V64"), "4V4/1V1")

    override fun onDisable() {
        clearAll()
    }

    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet

        if (packet is S02PacketChat) {
            val matcher = Pattern.compile("杀死了 (.*?)\\(").matcher(packet.chatComponent.unformattedText)
            val matcher2 = Pattern.compile("> (.*?)\\(").matcher(packet.chatComponent.unformattedText)
            val friendsConfig = LiquidBounce.fileManager.friendsConfig

            if (matcher.find()) {
                val name = matcher.group(1)
                if (name != "") {
                    if (!playerName.contains(name)) {
                        playerName.add(name)
                        friendsConfig.addFriend(name)
                        ClientUtils.displayChatMessage("§l§b[§e§k1Lone§l§b]§c§dAdd a dead Player: $name")
                        Thread {
                            try {
                                Thread.sleep(6000)
                                playerName.remove(name)
                                friendsConfig.removeFriend(name)
                                ClientUtils.displayChatMessage("§l§b[§e§k1Lone§l§b]§c§dDelete a dead Player: $name")
                            } catch (ex: InterruptedException) {
                                ex.printStackTrace()
                            }
                        }.start()
                    }
                }
            }
            if (matcher2.find()) {
                val name = matcher2.group(1)
                if (name != "" && !name.contains("[")) {
                    if (!playerName.contains(name)) {
                        playerName.add(name)
                        friendsConfig.addFriend(name)
                        ClientUtils.displayChatMessage("§l§b[§e§k1Lone§l§b]§c§dAdd a dead Player: $name")
                        Thread {
                            try {
                                Thread.sleep(6000)
                                playerName.remove(name)
                                friendsConfig.removeFriend(name)
                                ClientUtils.displayChatMessage("§l§b[§e§k1Lone§l§b]§c§dDelete a dead Player: $name")
                            } catch (ex: InterruptedException) {
                                ex.printStackTrace()
                            }
                        }.start()
                    }
                }
            }
        }
        if (modeValue.get() == "4V4/1V1") {
            if (packet is S14PacketEntity) {
                val entity = packet.getEntity(mc.theWorld!!)

                if (entity is EntityPlayer) {
                    if (packet.onGround && !ground.contains(entity.entityId))
                        ground.add(entity.entityId)
                }
            }
        }
        if (modeValue.get() == "32V32/64V64") {
            if (packet is S14PacketEntity) {
                val entity = packet.getEntity(mc.theWorld!!)

                if (entity is EntityPlayer) {
                    if (packet.onGround && !ground.contains(entity.entityId))
                        ground.add(entity.entityId)
                }
            }
        }
    }

    @EventTarget
    fun onWorld(event: WorldEvent) {
        clearAll()
    }

    private fun clearAll() {
        playerName.clear()
    }
}