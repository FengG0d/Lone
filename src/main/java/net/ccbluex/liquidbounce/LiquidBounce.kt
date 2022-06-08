package net.ccbluex.liquidbounce

import net.ccbluex.liquidbounce.event.ClientShutdownEvent
import net.ccbluex.liquidbounce.event.EventManager
import net.ccbluex.liquidbounce.features.command.CommandManager
import net.ccbluex.liquidbounce.features.macro.MacroManager
import net.ccbluex.liquidbounce.features.module.ModuleManager
import net.ccbluex.liquidbounce.features.special.AntiForge
import net.ccbluex.liquidbounce.features.special.CombatManager
import net.ccbluex.liquidbounce.features.special.ServerSpoof
import net.ccbluex.liquidbounce.file.FileManager
import net.ccbluex.liquidbounce.file.config.ConfigManager
import net.ccbluex.liquidbounce.ui.client.clickgui.ClickGUIModule
import net.ccbluex.liquidbounce.ui.client.clickgui.ClickGuiConfig
import net.ccbluex.liquidbounce.ui.client.clickgui.clickgui.ClickGui
import net.ccbluex.liquidbounce.ui.client.scriptMenu.scriptOnline.ScriptSubscribe
import net.ccbluex.liquidbounce.ui.client.scriptMenu.scriptOnline.Subscriptions
import net.ccbluex.liquidbounce.script.ScriptManager
import net.ccbluex.liquidbounce.ui.client.cape.GuiCapeManager
import net.ccbluex.liquidbounce.ui.client.GuiMainMenu
import net.ccbluex.liquidbounce.ui.client.hud.HUD
import net.ccbluex.liquidbounce.ui.client.keybind.KeyBindManager
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.ui.font.FontsGC
import net.ccbluex.liquidbounce.utils.misc.LanguageManager
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.ccbluex.liquidbounce.utils.InventoryUtils
import net.ccbluex.liquidbounce.utils.RotationUtils
import net.minecraft.util.ResourceLocation
import java.io.File

object LiquidBounce {


    // Client information
    const val CLIENT_NAME = "Lone"

    var Darkmode = true
    const val COLORED_NAME = "§b[§b!§7] §b§lLone §b» "
    const val CLIENT_CREATOR = "CCBlueX & UnlegitMC & FengGod"
    const val CLIENT_WEBSITE = "Lone.today"
    const val CLIENT_VERSION = "b1.1.4514"
    const val MINECRAFT_VERSION = "1.8.9"

    var isStarting = true
    var isLoadingConfig = true

    // Managers
    lateinit var moduleManager: ModuleManager

    lateinit var commandManager: CommandManager
    lateinit var eventManager: EventManager
    lateinit var subscriptions: Subscriptions
    lateinit var fileManager: FileManager
    lateinit var scriptManager: ScriptManager
    lateinit var combatManager: CombatManager
    lateinit var macroManager: MacroManager
    lateinit var configManager: ConfigManager

    // Some UI things
    lateinit var hud: HUD
    lateinit var mainMenu: GuiMainMenu
    lateinit var keyBindManager: KeyBindManager

    // Click gui
    lateinit var clickGui: ClickGui
    lateinit var clickGuiConfig: ClickGuiConfig

    // Menu Background
    var background: ResourceLocation? = ResourceLocation("lone/background.png")

    /**
     * Execute if client will be started
     */
    fun initClient() {
        ClientUtils.logInfo("Loading $CLIENT_NAME $CLIENT_VERSION, by $CLIENT_CREATOR")
        ClientUtils.setTitle("Initializing...")
        val startTime = System.currentTimeMillis()
        // Create file manager
        fileManager = FileManager()
        configManager = ConfigManager()
        subscriptions = Subscriptions()

        // Create event manager
        eventManager = EventManager()

        // Load language
        LanguageManager.loadLanguage()

        // Register listeners
        eventManager.registerListener(RotationUtils())
        eventManager.registerListener(AntiForge)
        eventManager.registerListener(InventoryUtils)
        eventManager.registerListener(ServerSpoof)

        // Create command manager
        commandManager = CommandManager()

        fileManager.loadConfigs(
            fileManager.accountsConfig,
            fileManager.friendsConfig,
            fileManager.specialConfig,
            fileManager.subscriptsConfig
        )
        // Load client fonts
        Fonts.loadFonts()
        eventManager.registerListener(FontsGC)

        macroManager = MacroManager()
        eventManager.registerListener(macroManager)

        // Setup module manager and register modules
        moduleManager = ModuleManager()
        moduleManager.registerModules()

        try {
            // ScriptManager, Remapper will be lazy loaded when scripts are enabled
            scriptManager = ScriptManager()
            scriptManager.loadScripts()
            scriptManager.enableScripts()
        } catch (throwable: Throwable) {
            ClientUtils.logError("Failed to load scripts.", throwable)
        }

        // Register commands
        commandManager.registerCommands()

        // KeyBindManager
        keyBindManager = KeyBindManager()

        // bstats.org user count display
        ClientUtils.buildMetrics()

        combatManager = CombatManager()
        eventManager.registerListener(combatManager)

        GuiCapeManager.load()

        mainMenu = GuiMainMenu()

        // Set HUD
        hud = HUD.createDefault()

        fileManager.loadConfigs(fileManager.hudConfig, fileManager.xrayConfig)

        ClientUtils.setTitle("Loading script subscripts...")
        for (subscript in fileManager.subscriptsConfig.subscripts) {
            //println(subscript.url+":"+subscript.name)
            Subscriptions.addSubscribes(ScriptSubscribe(subscript.url, subscript.name))
            scriptManager.disableScripts()
            scriptManager.unloadScripts()
            for (scriptSubscribe in Subscriptions.subscribes) {
                scriptSubscribe.load()
            }
            scriptManager.loadScripts()
            scriptManager.enableScripts()
        }

        ClientUtils.logInfo("Loading some ui things...")

        moduleManager.registerModule(ClickGUIModule())

        clickGui = ClickGui()
        clickGuiConfig =
            ClickGuiConfig(
                File(
                    fileManager.dir,
                    "clickgui.json"
                )
            )
        fileManager.loadConfig(clickGuiConfig)

        // Load configs
        configManager.loadLegacySupport()
        configManager.loadConfigSet()

        // Set is starting status
        isStarting = false
        isLoadingConfig = false

        ClientUtils.setTitle()
        ClientUtils.logInfo("$CLIENT_NAME $CLIENT_VERSION loaded in ${(System.currentTimeMillis() - startTime)}ms!")
    }

    /**
     * Execute if client will be stopped
     */
    fun stopClient() {
        if (!isStarting && !isLoadingConfig) {
            ClientUtils.logInfo("Shutting down $CLIENT_NAME $CLIENT_VERSION!")

            // Call client shutdown
            eventManager.callEvent(ClientShutdownEvent())

            // Save all available configs
            GuiCapeManager.save()
            configManager.save(true, true)
            fileManager.saveAllConfigs()
        }
    }
}
