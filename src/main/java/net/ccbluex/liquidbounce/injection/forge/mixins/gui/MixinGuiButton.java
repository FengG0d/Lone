package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.client.HUD;
import net.ccbluex.liquidbounce.features.module.modules.client.button.AbstractButtonRenderer;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.RiseColorUtil;
import net.ccbluex.liquidbounce.utils.render.RiseRenderUtil;
import net.ccbluex.liquidbounce.utils.render.RoundedUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.awt.*;

@Mixin(GuiButton.class)
public abstract class MixinGuiButton extends Gui {

    @Final
    @Shadow
    protected static ResourceLocation buttonTextures;
    protected final AbstractButtonRenderer buttonRenderer = LiquidBounce.moduleManager.getModule(HUD.class).getButtonRenderer((GuiButton) (Object) this);
    @Shadow
    public int xPosition;
    @Shadow
    public int yPosition;
    @Shadow
    public int width;
    @Shadow
    public int height;
    @Shadow
    public boolean hovered;
    @Shadow
    public boolean enabled;
    @Shadow
    public boolean visible;
    @Shadow
    public int packedFGColour;
    @Shadow
    public String displayString;

    @Shadow
    public abstract void mouseDragged(Minecraft mc, int mouseX, int mouseY);

    @Shadow
    protected abstract int getHoverState(boolean p_getHoverState_1_);

    /**
     * @author FengGod
     */
//   @Inject(method = "drawButton", at = @At("HEAD"), cancellable = true)
//   public void drawButton(Minecraft mc, int mouseX, int mouseY, CallbackInfo ci) {
//      if(this.buttonRenderer != null) {
//         if(!visible) {
//            return;
//         }
//         this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
//         this.mouseDragged(mc, mouseX, mouseY);
//         buttonRenderer.render(mouseX, mouseY, mc);
//         buttonRenderer.drawButtonText(mc);
//         ci.cancel();
//      }
//   }
    @Overwrite
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            if (mc.theWorld == null) {
                mc.getTextureManager().bindTexture(buttonTextures);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                GlStateManager.blendFunc(770, 771);

                RoundedUtil.drawRound(this.xPosition, this.yPosition, this.width, this.height, 8, new Color(0, 0, 0, 65));

                if (hovered)
                    RoundedUtil.drawRound(this.xPosition, this.yPosition, this.width, this.height, 8, new Color(0, 0, 0, 65));

                this.mouseDragged(mc, mouseX, mouseY);

                Fonts.font35.drawCenteredString(this.displayString, this.xPosition + this.width / 2.0F, this.yPosition + (this.height - 5) / 2.0F, Color.WHITE.hashCode());

            } else {
                mc.getTextureManager().bindTexture(buttonTextures);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                GlStateManager.blendFunc(770, 771);

                RoundedUtil.drawRound(this.xPosition, this.yPosition, this.width, this.height, 4, new Color(0, 0, 0, 65));

                if (hovered)
                    RoundedUtil.drawRound(this.xPosition, this.yPosition, this.width, this.height, 4, new Color(0, 0, 0, 65));

                final Color color1 = new Color(78, 161, 253, 255);
                final Color color2 = new Color(78, 253, 154, 255);

                final Color cock = RiseColorUtil.mixColors(color1, color2, (Math.sin(mc.ingameGUI.getUpdateCounter()) + 1) * 0.5f);
                final Color cock2 = RiseColorUtil.mixColors(color1, color2, (Math.sin(mc.ingameGUI.getUpdateCounter() + this.width * 0.4f) + 1) * 0.5f);

                RiseRenderUtil.gradientSideways(this.xPosition, this.yPosition + this.height, this.width, 0.5f, cock, cock2);

                for (int i = 0; i <= 7; ++i) {
                    RiseRenderUtil.gradientSideways(this.xPosition - i, this.yPosition + this.height - i, this.width + i * 2, i * 2 - 0.5f, new Color(cock.getRed(), cock.getGreen(), cock.getBlue(), 4), new Color(cock2.getRed(), cock2.getGreen(), cock2.getBlue(), 3));
                }

                this.mouseDragged(mc, mouseX, mouseY);

                Fonts.font35.drawCenteredString(this.displayString, this.xPosition + this.width / 2.0F, this.yPosition + (this.height - 5) / 2.0F, Color.WHITE.hashCode());
            }
        }
    }
}
