package net.ccbluex.liquidbounce.ui.client.clickgui.liquidbounce.style;

import net.ccbluex.liquidbounce.ui.client.clickgui.liquidbounce.Panel;
import net.ccbluex.liquidbounce.ui.client.clickgui.liquidbounce.elements.ButtonElement;
import net.ccbluex.liquidbounce.ui.client.clickgui.liquidbounce.elements.ModuleElement;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;

public abstract class Style extends MinecraftInstance {

    public abstract void drawPanel(final int mouseX, final int mouseY, final Panel panel);

    public abstract void drawDescription(final int mouseX, final int mouseY, final String text);

    public abstract void drawButtonElement(final int mouseX, final int mouseY, final ButtonElement buttonElement);

    public abstract void drawModuleElement(final int mouseX, final int mouseY, final ModuleElement moduleElement);

}
