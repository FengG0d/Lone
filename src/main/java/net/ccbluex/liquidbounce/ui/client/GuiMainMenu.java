package net.ccbluex.liquidbounce.ui.client;


import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.FengRenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Project;

import java.awt.*;
import java.io.IOException;
import java.util.Random;

public class GuiMainMenu extends GuiScreen {
    private static final Random RANDOM = new Random();
    private static final ResourceLocation[] titlePanoramaPaths = new ResourceLocation[]{new ResourceLocation("lone/panorama/panorama_0.png"), new ResourceLocation("lone/panorama/panorama_1.png"), new ResourceLocation("lone/panorama/panorama_2.png"), new ResourceLocation("lone/panorama/panorama_3.png"), new ResourceLocation("lone/panorama/panorama_4.png"), new ResourceLocation("lone/panorama/panorama_5.png")};
    final float updateCounter;
    private int astolfo;
    private int panoramaTimer;
    private ResourceLocation backgroundTexture;

    public GuiMainMenu() {
        updateCounter = RANDOM.nextFloat();
    }

    public void updateScreen() {
        ++panoramaTimer;
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    protected void keyTyped(char p_keyTyped_1_, int p_keyTyped_2_) throws IOException {
    }

    public void initGui() {
        DynamicTexture viewportTexture = new DynamicTexture(256, 256);
        backgroundTexture = mc.getTextureManager().getDynamicTextureLocation("background", viewportTexture);

        astolfo = RandomUtils.nextInt(0, 4);

        addButtons();
    }

    private void addButtons() {
        int defaultHeight = height / 2 - 8;

        buttonList.add(new MainMenuButton(1, width / 2 - 72, defaultHeight, 140, 20, I18n.format("menu.singleplayer")));
        buttonList.add(new MainMenuButton(2, width / 2 - 72, defaultHeight + 24, 140, 20, I18n.format("menu.multiplayer")));
        buttonList.add(new MainMenuButton(3, width / 2 - 72, defaultHeight + 48, 140, 20, I18n.format("Account Manager")));
        buttonList.add(new MainMenuButton(4, width / 2 - 72, defaultHeight + 72, 140, 20, I18n.format("menu.options")));
        buttonList.add(new MainMenuButton(5, width / 2 - 72, defaultHeight + 96, 140, 20, I18n.format("Exit")));
    }

    protected void actionPerformed(GuiButton p_actionPerformed_1_) throws IOException {
        if (p_actionPerformed_1_.id == 1) mc.displayGuiScreen(new GuiSelectWorld(this));
        if (p_actionPerformed_1_.id == 2) mc.displayGuiScreen(new GuiMultiplayer(this));
//        if (p_actionPerformed_1_.id == 3) mc.displayGuiScreen(new GuiAltManager(this));
        if (p_actionPerformed_1_.id == 4) mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
        if (p_actionPerformed_1_.id == 5) mc.shutdown();
    }


    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.disableAlpha();
        renderSkybox(partialTicks);
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) (width / 2 + 90), 70.0F, 0.0F);
        GlStateManager.rotate(-20.0F, 0.0F, 0.0F, 1.0F);
        float f = 1.8F - MathHelper.abs(MathHelper.sin((float) (Minecraft.getSystemTime() % 1000L) / 1000.0F * (float) Math.PI * 2.0F) * 0.1F);
        f = f * 100.0F / 32f;
        GlStateManager.scale(f, f, f);
        GlStateManager.popMatrix();

        Fonts.font40.drawCenteredString(LiquidBounce.CLIENT_NAME, width / 2f, height / 2f - 60f, Color.WHITE.getRGB());
        final int[] imageWidth = {230, 250, 400};
        final int[] imageHeight = {312, 353, 422, 305, 242};
        int imageX, imageY;
        switch (astolfo) {
            case 0:
                imageX = imageWidth[0];
                imageY = imageHeight[0];
                break;
            case 1:
                imageX = imageWidth[1];
                imageY = imageHeight[1];
                break;
            case 2:
                imageX = imageWidth[0];
                imageY = imageHeight[2];
                break;
            case 3:
                imageX = imageWidth[2];
                imageY = imageHeight[3];
                break;
            case 4:
                imageX = imageWidth[0];
                imageY = imageHeight[4];
                break;
            default: {
                imageX = 0;
                imageY = 0;
                break;
            }
        }
        FengRenderUtils.drawImage(new ResourceLocation("lone/astolfos/" + astolfo + ".png"), width - imageX / 3, height - imageY / 3, imageX / 3, imageY / 3);

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawPanorama(float p_drawPanorama_3_) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.matrixMode(5889);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        Project.gluPerspective(120.0F, 1.0F, 0.05F, 10.0F);
        GlStateManager.matrixMode(5888);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.disableCull();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        int i = 8;

        for (int j = 0; j < i * i; ++j) {
            GlStateManager.pushMatrix();
            float f = ((float) (j % i) / (float) i - 0.5F) / 64.0F;
            float f1 = ((float) (j / i) / (float) i - 0.5F) / 64.0F;
            float f2 = 0.0F;
            GlStateManager.translate(f, f1, f2);
            GlStateManager.rotate(MathHelper.sin(((float) panoramaTimer + p_drawPanorama_3_) / 400.0F) * 25.0F + 20.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(-((float) panoramaTimer + p_drawPanorama_3_) * 0.1F, 0.0F, 1.0F, 0.0F);

            for (int k = 0; k < 6; ++k) {
                GlStateManager.pushMatrix();
                if (k == 1) GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
                if (k == 2) GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                if (k == 3) GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
                if (k == 4) GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
                if (k == 5) GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);

                mc.getTextureManager().bindTexture(titlePanoramaPaths[k]);
                worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                int l = 255 / (j + 1);
                worldrenderer.pos(-1.0D, -1.0D, 1.0D).tex(0.0D, 0.0D).color(255, 255, 255, l).endVertex();
                worldrenderer.pos(1.0D, -1.0D, 1.0D).tex(1.0D, 0.0D).color(255, 255, 255, l).endVertex();
                worldrenderer.pos(1.0D, 1.0D, 1.0D).tex(1.0D, 1.0D).color(255, 255, 255, l).endVertex();
                worldrenderer.pos(-1.0D, 1.0D, 1.0D).tex(0.0D, 1.0D).color(255, 255, 255, l).endVertex();
                tessellator.draw();
                GlStateManager.popMatrix();
            }

            GlStateManager.popMatrix();
            GlStateManager.colorMask(true, true, true, false);
        }

        worldrenderer.setTranslation(0.0D, 0.0D, 0.0D);
        GlStateManager.colorMask(true, true, true, true);
        GlStateManager.matrixMode(5889);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();
        GlStateManager.depthMask(true);
        GlStateManager.enableCull();
        GlStateManager.enableDepth();
    }

    private void rotateAndBlurSkybox() {
        mc.getTextureManager().bindTexture(backgroundTexture);
        GL11.glTexParameteri(3553, 10241, 9729);
        GL11.glTexParameteri(3553, 10240, 9729);
        GL11.glCopyTexSubImage2D(3553, 0, 0, 0, 0, 0, 256, 256);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.colorMask(true, true, true, false);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        GlStateManager.disableAlpha();
        int i = 3;

        for (int j = 0; j < i; ++j) {
            float f = 1.0F / (float) (j + 1);
            int k = width;
            int l = height;
            float f1 = (float) (j - i / 2) / 256.0F;
            worldrenderer.pos(k, l, zLevel).tex((0.0F + f1), 1.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
            worldrenderer.pos(k, 0.0D, zLevel).tex((1.0F + f1), 1.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
            worldrenderer.pos(0.0D, 0.0D, zLevel).tex((1.0F + f1), 0.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
            worldrenderer.pos(0.0D, l, zLevel).tex((0.0F + f1), 0.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
        }

        tessellator.draw();
        GlStateManager.enableAlpha();
        GlStateManager.colorMask(true, true, true, true);
    }

    private void renderSkybox(float p_renderSkybox_3_) {
        mc.getFramebuffer().unbindFramebuffer();
        GlStateManager.viewport(0, 0, 256, 256);
        drawPanorama(p_renderSkybox_3_);
        rotateAndBlurSkybox();
        rotateAndBlurSkybox();
        rotateAndBlurSkybox();
        rotateAndBlurSkybox();
        rotateAndBlurSkybox();
        rotateAndBlurSkybox();
        //rotateAndBlurSkybox(p_renderSkybox_3_);
        mc.getFramebuffer().bindFramebuffer(true);
        GlStateManager.viewport(0, 0, mc.displayWidth, mc.displayHeight);
        float f = width > height ? 120.0F / (float) width : 120.0F / (float) height;
        float f1 = (float) height * f / 256.0F;
        float f2 = (float) width * f / 256.0F;
        int i = width;
        int j = height;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos(0.0D, j, zLevel).tex((0.5F - f1), (0.5F + f2)).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        worldrenderer.pos(i, j, zLevel).tex((0.5F - f1), (0.5F - f2)).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        worldrenderer.pos(i, 0.0D, zLevel).tex((0.5F + f1), (0.5F - f2)).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        worldrenderer.pos(0.0D, 0.0D, zLevel).tex((0.5F + f1), (0.5F + f2)).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        tessellator.draw();
    }
}
