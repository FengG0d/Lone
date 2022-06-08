package net.ccbluex.liquidbounce.ui.client;

import net.ccbluex.liquidbounce.ui.font.cfont.CFontRenderer;
import net.ccbluex.liquidbounce.ui.font.cfont.FontLoaders;
import net.ccbluex.liquidbounce.utils.render.FengRenderUtils;
import net.ccbluex.liquidbounce.utils.render.SmoothRenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class MainMenuButton extends GuiButton {

    public MainMenuButton(final int buttonId, final int x, final int y, final int width, final int height, final String buttonText) {
        super(buttonId, x, y, width, height, buttonText);
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (visible) {
            Color color = new Color(180, 180, 180);
            CFontRenderer fontrenderer = FontLoaders.C18;
            hovered = (mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height);

            SmoothRenderUtils.drawRoundedRect(xPosition, yPosition, xPosition + width, yPosition + height, 3, new Color(1, 1, 1, 80).getRGB());

            if (hovered) {
                GL11.glPushMatrix();
                FengRenderUtils.color(color.darker().getRGB());
                GL11.glPopMatrix();
            }

            mouseDragged(mc, mouseX, mouseY);
            int stringColor = new Color(255, 255, 255, 200).getRGB();

            if (hovered)
                stringColor = color.darker().getRGB();

            fontrenderer.drawCenteredString(displayString.toUpperCase(), xPosition + width / 2f, yPosition + (height - 6) / 2f, stringColor);
        }
    }
}
