package net.ccbluex.liquidbounce.utils.render;

import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public final class RiseRenderUtil extends MinecraftInstance {

    private static final Frustum frustrum = new Frustum();

    public long last2DFrame = System.currentTimeMillis();
    public long last3DFrame = System.currentTimeMillis();

    public float delta2DFrameTime;
    public float delta3DFrameTime;

    public static void enable(final int glTarget) {
        GL11.glEnable(glTarget);
    }

    public static void disable(final int glTarget) {
        GL11.glDisable(glTarget);
    }

    public static void start() {
        enable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        disable(GL11.GL_TEXTURE_2D);
        disable(GL11.GL_CULL_FACE);
        GlStateManager.disableAlpha();
        GlStateManager.disableDepth();
    }

    public static void stop() {
        GlStateManager.enableAlpha();
        GlStateManager.enableDepth();
        enable(GL11.GL_CULL_FACE);
        enable(GL11.GL_TEXTURE_2D);
        disable(GL11.GL_BLEND);
        color(Color.white);
    }

    public static void begin(final int glMode) {
        GL11.glBegin(glMode);
    }

    public static void end() {
        GL11.glEnd();
    }

    public static void vertex(final double x, final double y) {
        GL11.glVertex2d(x, y);
    }

    public static void color(final double red, final double green, final double blue, final double alpha) {
        GL11.glColor4d(red, green, blue, alpha);
    }

    public static void color(Color color) {
        if (color == null)
            color = Color.white;
        color(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, color.getAlpha() / 255F);
    }

    public static void rect(final double x, final double y, final double width, final double height, final boolean filled, final Color color) {
        start();
        if (color != null)
            color(color);
        begin(filled ? GL11.GL_TRIANGLE_FAN : GL11.GL_LINES);

        {
            vertex(x, y);
            vertex(x + width, y);
            vertex(x + width, y + height);
            vertex(x, y + height);
            if (!filled) {
                vertex(x, y);
                vertex(x, y + height);
                vertex(x + width, y);
                vertex(x + width, y + height);
            }
        }
        end();
        stop();
    }

    public static void scissor(double x, double y, double width, double height) {
        final ScaledResolution sr = new ScaledResolution(mc);
        final double scale = sr.getScaleFactor();

        y = sr.getScaledHeight() - y;

        x *= scale;
        y *= scale;
        width *= scale;
        height *= scale;

        GL11.glScissor((int) x, (int) (y - height), (int) width, (int) height);
    }

    public static void putVertex3DInWorld(final double x, final double y, final double z) {
        putVertex3d(getRenderPos(x, y, z));
    }

    public static void drawBoundingBox(final AxisAlignedBB aa) {

        begin(GL_TRIANGLE_STRIP);
        putVertex3DInWorld(aa.minX, aa.minY, aa.minZ);
        putVertex3DInWorld(aa.minX, aa.maxY, aa.minZ);
        putVertex3DInWorld(aa.maxX, aa.minY, aa.minZ);
        putVertex3DInWorld(aa.maxX, aa.maxY, aa.minZ);
        putVertex3DInWorld(aa.maxX, aa.minY, aa.maxZ);
        putVertex3DInWorld(aa.maxX, aa.maxY, aa.maxZ);
        putVertex3DInWorld(aa.minX, aa.minY, aa.maxZ);
        putVertex3DInWorld(aa.minX, aa.maxY, aa.maxZ);
        end();

        begin(GL_TRIANGLE_STRIP);
        putVertex3DInWorld(aa.maxX, aa.maxY, aa.minZ);
        putVertex3DInWorld(aa.maxX, aa.minY, aa.minZ);
        putVertex3DInWorld(aa.minX, aa.maxY, aa.minZ);
        putVertex3DInWorld(aa.minX, aa.minY, aa.minZ);
        putVertex3DInWorld(aa.minX, aa.maxY, aa.maxZ);
        putVertex3DInWorld(aa.minX, aa.minY, aa.maxZ);
        putVertex3DInWorld(aa.maxX, aa.maxY, aa.maxZ);
        putVertex3DInWorld(aa.maxX, aa.minY, aa.maxZ);
        end();

        begin(GL_TRIANGLE_STRIP);
        putVertex3DInWorld(aa.minX, aa.maxY, aa.minZ);
        putVertex3DInWorld(aa.maxX, aa.maxY, aa.minZ);
        putVertex3DInWorld(aa.maxX, aa.maxY, aa.maxZ);
        putVertex3DInWorld(aa.minX, aa.maxY, aa.maxZ);
        putVertex3DInWorld(aa.minX, aa.maxY, aa.minZ);
        putVertex3DInWorld(aa.minX, aa.maxY, aa.maxZ);
        putVertex3DInWorld(aa.maxX, aa.maxY, aa.maxZ);
        putVertex3DInWorld(aa.maxX, aa.maxY, aa.minZ);
        end();

        begin(GL_TRIANGLE_STRIP);
        putVertex3DInWorld(aa.minX, aa.minY, aa.minZ);
        putVertex3DInWorld(aa.maxX, aa.minY, aa.minZ);
        putVertex3DInWorld(aa.maxX, aa.minY, aa.maxZ);
        putVertex3DInWorld(aa.minX, aa.minY, aa.maxZ);
        putVertex3DInWorld(aa.minX, aa.minY, aa.minZ);
        putVertex3DInWorld(aa.minX, aa.minY, aa.maxZ);
        putVertex3DInWorld(aa.maxX, aa.minY, aa.maxZ);
        putVertex3DInWorld(aa.maxX, aa.minY, aa.minZ);
        end();

        begin(GL_TRIANGLE_STRIP);
        putVertex3DInWorld(aa.minX, aa.minY, aa.minZ);
        putVertex3DInWorld(aa.minX, aa.maxY, aa.minZ);
        putVertex3DInWorld(aa.minX, aa.minY, aa.maxZ);
        putVertex3DInWorld(aa.minX, aa.maxY, aa.maxZ);
        putVertex3DInWorld(aa.maxX, aa.minY, aa.maxZ);
        putVertex3DInWorld(aa.maxX, aa.maxY, aa.maxZ);
        putVertex3DInWorld(aa.maxX, aa.minY, aa.minZ);
        putVertex3DInWorld(aa.maxX, aa.maxY, aa.minZ);
        end();

        begin(GL_TRIANGLE_STRIP);
        putVertex3DInWorld(aa.minX, aa.maxY, aa.maxZ);
        putVertex3DInWorld(aa.minX, aa.minY, aa.maxZ);
        putVertex3DInWorld(aa.minX, aa.maxY, aa.minZ);
        putVertex3DInWorld(aa.minX, aa.minY, aa.minZ);
        putVertex3DInWorld(aa.maxX, aa.maxY, aa.minZ);
        putVertex3DInWorld(aa.maxX, aa.minY, aa.minZ);
        putVertex3DInWorld(aa.maxX, aa.maxY, aa.maxZ);
        putVertex3DInWorld(aa.maxX, aa.minY, aa.maxZ);
        end();
    }

    public static void drawSolidBlockESP(final double x, final double y, final double z, final float red, final float green, final float blue,
                                         final float alpha) {
        GL11.glPushMatrix();
        GL11.glColor4f(red, green, blue, alpha);
        drawBoundingBox(new AxisAlignedBB(x, y, z, x + 1D, y + 1D, z + 1D));
        GL11.glPopMatrix();
    }

    public static void drawSolidEntityESP(final double x, final double y, final double z, final double width, final double height, final float red,
                                          final float green, final float blue, final float alpha) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glColor4f(red, green, blue, alpha);
        drawBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    public static void drawSolidEntityESPFixed(final double x, final double y, final double z, final double width, final double height, final float red,
                                               final float green, final float blue, final float alpha, final Entity e) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glColor4f(red, green, blue, alpha);
        drawBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    public static void draw3DLine(double x, double y, double z, double x1, double y1, double z1, final float red, final float green,
                                  final float blue, final float alpha, final float lineWdith) {

        x = x - mc.getRenderManager().renderPosX;
        x1 = x1 - mc.getRenderManager().renderPosX;
        y = y - mc.getRenderManager().renderPosY;
        y1 = y1 - mc.getRenderManager().renderPosY;
        z = z - mc.getRenderManager().renderPosZ;
        z1 = z1 - mc.getRenderManager().renderPosZ;

        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glLineWidth(lineWdith);
        GL11.glColor4f(red, green, blue, alpha);
        GL11.glBegin(2);
        GL11.glVertex3d(x, y, z);
        GL11.glVertex3d(x1, y1, z1);
        GL11.glEnd();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    public static void color4f(final float red, final float green, final float blue, final float alpha) {
        GL11.glColor4f(red, green, blue, alpha);
    }

    public static void lineWidth(final float width) {
        GL11.glLineWidth(width);
    }

    public static void glBegin(final int mode) {
        GL11.glBegin(mode);
    }

    public static void glEnd() {
        GL11.glEnd();
    }

    public static void putVertex3d(final double x, final double y, final double z) {
        GL11.glVertex3d(x, y, z);
    }

    public static void putVertex3d(final Vec3 vec) {
        GL11.glVertex3d(vec.xCoord, vec.yCoord, vec.zCoord);
    }

    public static Vec3 getRenderPos(double x, double y, double z) {

        x = x - mc.getRenderManager().renderPosX;
        y = y - mc.getRenderManager().renderPosY;
        z = z - mc.getRenderManager().renderPosZ;

        return new Vec3(x, y, z);
    }

    public static void drawCircle(final int x, final int y, final double r, final float f1, final float f2, final float f3, final float f) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glBegin(GL11.GL_LINE_LOOP);

        for (int i = 0; i <= 360; i++) {
            final double x2 = Math.sin(((i * Math.PI) / 180)) * r;
            final double y2 = Math.cos(((i * Math.PI) / 180)) * r;
            GL11.glVertex2d(x + x2, y + y2);
        }

        GL11.glEnd();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public static void drawFilledCircle(final int x, final int y, final double r, final int c) {
        final float f = ((c >> 24) & 0xff) / 255F;
        final float f1 = ((c >> 16) & 0xff) / 255F;
        final float f2 = ((c >> 8) & 0xff) / 255F;
        final float f3 = (c & 0xff) / 255F;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);

        for (int i = 0; i <= 360; i++) {
            final double x2 = Math.sin(((i * Math.PI) / 180)) * r;
            final double y2 = Math.cos(((i * Math.PI) / 180)) * r;
            GL11.glVertex2d(x + x2, y + y2);
        }

        GL11.glEnd();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public static void drawFilledCircle(final int x, final int y, final double r, final int c, final int quality) {
        final float f = ((c >> 24) & 0xff) / 255F;
        final float f1 = ((c >> 16) & 0xff) / 255F;
        final float f2 = ((c >> 8) & 0xff) / 255F;
        final float f3 = (c & 0xff) / 255F;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);

        for (int i = 0; i <= 360 / quality; i++) {
            final double x2 = Math.sin(((i * quality * Math.PI) / 180)) * r;
            final double y2 = Math.cos(((i * quality * Math.PI) / 180)) * r;
            GL11.glVertex2d(x + x2, y + y2);
        }

        GL11.glEnd();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public static void drawFilledCircle(final double x, final double y, final double r, final int c, final int quality) {
        final float f = ((c >> 24) & 0xff) / 255F;
        final float f1 = ((c >> 16) & 0xff) / 255F;
        final float f2 = ((c >> 8) & 0xff) / 255F;
        final float f3 = (c & 0xff) / 255F;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);

        for (int i = 0; i <= 360 / quality; i++) {
            final double x2 = Math.sin(((i * quality * Math.PI) / 180)) * r;
            final double y2 = Math.cos(((i * quality * Math.PI) / 180)) * r;
            GL11.glVertex2d(x + x2, y + y2);
        }

        GL11.glEnd();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public static void drawFilledCircleNoGL(final int x, final int y, final double r, final int c) {
        final float f = ((c >> 24) & 0xff) / 255F;
        final float f1 = ((c >> 16) & 0xff) / 255F;
        final float f2 = ((c >> 8) & 0xff) / 255F;
        final float f3 = (c & 0xff) / 255F;

        GL11.glColor4f(f1, f2, f3, f);
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);

        for (int i = 0; i <= 360 / 20; i++) {
            final double x2 = Math.sin(((i * 20 * Math.PI) / 180)) * r;
            final double y2 = Math.cos(((i * 20 * Math.PI) / 180)) * r;
            GL11.glVertex2d(x + x2, y + y2);
        }

        GL11.glEnd();

    }

    public static void drawFilledCircleNoGL(final int x, final int y, final double r, final int c, final int quality) {
        final float f = ((c >> 24) & 0xff) / 255F;
        final float f1 = ((c >> 16) & 0xff) / 255F;
        final float f2 = ((c >> 8) & 0xff) / 255F;
        final float f3 = (c & 0xff) / 255F;

        GL11.glColor4f(f1, f2, f3, f);
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);

        for (int i = 0; i <= 360 / quality; i++) {
            final double x2 = Math.sin(((i * quality * Math.PI) / 180)) * r;
            final double y2 = Math.cos(((i * quality * Math.PI) / 180)) * r;
            GL11.glVertex2d(x + x2, y + y2);
        }

        GL11.glEnd();
    }

    public static void drawSphere(final double red, final double green, final double blue, final double alpha, final double x, final double y, final double z,
                                  final float size, final int slices, final int stacks, final float lWidth) {
        final Sphere sphere = new Sphere();

        enableDefaults();
        GL11.glColor4d(red, green, blue, alpha);
        GL11.glTranslated(x, y, z);
        GL11.glLineWidth(lWidth);
        sphere.setDrawStyle(GLU.GLU_SILHOUETTE);
        sphere.draw(size, slices, stacks);
        disableDefaults();
    }

    public static void enableDefaults() {
        mc.entityRenderer.disableLightmap();
        GL11.glEnable(3042 /* GL_BLEND */);
        GL11.glDisable(3553 /* GL_TEXTURE_2D */);
        GL11.glDisable(2896 /* GL_LIGHTING */);
        // GL11.glDisable(2929 /*GL_DEPTH_TEST*/);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848 /* GL_LINE_SMOOTH */);
        GL11.glPushMatrix();
    }

    public static void disableDefaults() {
        GL11.glPopMatrix();
        GL11.glDisable(2848 /* GL_LINE_SMOOTH */);
        GL11.glDepthMask(true);
        // GL11.glEnable(2929 /*GL_DEPTH_TEST*/);
        GL11.glEnable(3553 /* GL_TEXTURE_2D */);
        GL11.glEnable(2896 /* GL_LIGHTING */);
        GL11.glDisable(3042 /* GL_BLEND */);
        mc.entityRenderer.enableLightmap();
    }

    public static boolean isInViewFrustrum(final Entity entity) {
        return (isInViewFrustrum(entity.getEntityBoundingBox()) || entity.ignoreFrustumCheck);
    }

    private static boolean isInViewFrustrum(final AxisAlignedBB bb) {
        final Entity current = mc.getRenderViewEntity();
        frustrum.setPosition(current.posX, current.posY, current.posZ);
        return frustrum.isBoundingBoxInFrustum(bb);
    }

    public static void quickDrawRect(final float x, final float y, final float x2, final float y2, final int color) {
        glColor(color);
        glBegin(GL_QUADS);

        glVertex2d(x2, y);
        glVertex2d(x, y);
        glVertex2d(x, y2);
        glVertex2d(x2, y2);

        glEnd();
    }

    public static void quickDrawBorderedRect(final float x, final float y, final float x2, final float y2, final float width, final int color1, final int color2) {
        quickDrawRect(x, y, x2, y2, color2);

        glColor(color1);
        glLineWidth(width);

        glBegin(GL_LINE_LOOP);

        glVertex2d(x2, y);
        glVertex2d(x, y);
        glVertex2d(x, y2);
        glVertex2d(x2, y2);

        glEnd();
    }

    private static void glColor(final int hex) {
        final float alpha = (hex >> 24 & 0xFF) / 255F;
        final float red = (hex >> 16 & 0xFF) / 255F;
        final float green = (hex >> 8 & 0xFF) / 255F;
        final float blue = (hex & 0xFF) / 255F;

        GlStateManager.color(red, green, blue, alpha);
    }

    public static void drawGradientRect(final int left, final int top, final int right, final int bottom, final int startColor, final int endColor) {
        final float f = (float) (startColor >> 24 & 255) / 255.0F;
        final float f1 = (float) (startColor >> 16 & 255) / 255.0F;
        final float f2 = (float) (startColor >> 8 & 255) / 255.0F;
        final float f3 = (float) (startColor & 255) / 255.0F;
        final float f4 = (float) (endColor >> 24 & 255) / 255.0F;
        final float f5 = (float) (endColor >> 16 & 255) / 255.0F;
        final float f6 = (float) (endColor >> 8 & 255) / 255.0F;
        final float f7 = (float) (endColor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(right, top, GuiUtils.getzLevel()).color(f1, f2, f3, f).endVertex();
        worldrenderer.pos(left, top, GuiUtils.getzLevel()).color(f1, f2, f3, f).endVertex();
        worldrenderer.pos(left, bottom, GuiUtils.getzLevel()).color(f5, f6, f7, f4).endVertex();
        worldrenderer.pos(right, bottom, GuiUtils.getzLevel()).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void startSmooth() {
        enable(GL11.GL_POLYGON_SMOOTH);
        enable(GL11.GL_LINE_SMOOTH);
        enable(GL11.GL_POINT_SMOOTH);
    }

    public static void endSmooth() {
        disable(GL11.GL_POINT_SMOOTH);
        disable(GL11.GL_LINE_SMOOTH);
        disable(GL11.GL_POLYGON_SMOOTH);
    }

    public static void rect(final double x, final double y, final double width, final double height, final Color color) {
        rect(x, y, width, height, true, color);
    }

    public static void gradientSideways(final double x, final double y, final double width, final double height, final boolean filled, final Color color1, final Color color2) {
        start();
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GlStateManager.disableAlpha();
        if (color1 != null)
            color(color1);
        begin(filled ? GL11.GL_TRIANGLE_FAN : GL11.GL_LINES);
        {
            vertex(x, y);
            vertex(x, y + height);
            if (color2 != null)
                color(color2);
            vertex(x + width, y + height);
            vertex(x + width, y);
        }
        end();
        GlStateManager.enableAlpha();
        GL11.glShadeModel(GL11.GL_FLAT);
        stop();
    }

    public static void gradientSideways(final double x, final double y, final double width, final double height, final Color color1, final Color color2) {
        gradientSideways(x, y, width, height, true, color1, color2);
    }

    /**
     * ClickGui shit
     */

    public static void roundedRect(final double x, final double y, double width, double height, final double edgeRadius, final Color color) {
        final double halfRadius = edgeRadius / 2;
        width -= halfRadius;
        height -= halfRadius;

        float sideLength = (float) edgeRadius;
        sideLength /= 2;
        start();
        if (color != null)
            color(color);
        begin(GL11.GL_TRIANGLE_FAN);

        {
            for (double i = 180; i <= 270; i++) {
                final double angle = i * (Math.PI * 2) / 360;
                vertex(x + (sideLength * Math.cos(angle)) + sideLength, y + (sideLength * Math.sin(angle)) + sideLength);
            }
            vertex(x + sideLength, y + sideLength);
        }

        end();
        stop();

        sideLength = (float) edgeRadius;
        sideLength /= 2;
        start();
        if (color != null)
            color(color);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        begin(GL11.GL_TRIANGLE_FAN);

        {
            for (double i = 0; i <= 90; i++) {
                final double angle = i * (Math.PI * 2) / 360;
                vertex(x + width + (sideLength * Math.cos(angle)), y + height + (sideLength * Math.sin(angle)));
            }
            vertex(x + width, y + height);
        }

        end();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        stop();

        sideLength = (float) edgeRadius;
        sideLength /= 2;
        start();
        if (color != null)
            color(color);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        begin(GL11.GL_TRIANGLE_FAN);

        {
            for (double i = 270; i <= 360; i++) {
                final double angle = i * (Math.PI * 2) / 360;
                vertex(x + width + (sideLength * Math.cos(angle)), y + (sideLength * Math.sin(angle)) + sideLength);
            }
            vertex(x + width, y + sideLength);
        }

        end();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        stop();

        sideLength = (float) edgeRadius;
        sideLength /= 2;
        start();
        if (color != null)
            color(color);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        begin(GL11.GL_TRIANGLE_FAN);

        {
            for (double i = 90; i <= 180; i++) {
                final double angle = i * (Math.PI * 2) / 360;
                vertex(x + (sideLength * Math.cos(angle)) + sideLength, y + height + (sideLength * Math.sin(angle)));
            }
            vertex(x + sideLength, y + height);
        }

        end();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        stop();

        // Main block
        rect(x + halfRadius, y + halfRadius, width - halfRadius, height - halfRadius, color);

        // Horizontal bars
        rect(x, y + halfRadius, edgeRadius / 2, height - halfRadius, color);
        rect(x + width, y + halfRadius, edgeRadius / 2, height - halfRadius, color);

        // Vertical bars
        rect(x + halfRadius, y, width - halfRadius, halfRadius, color);
        rect(x + halfRadius, y + height, width - halfRadius, halfRadius, color);
    }

    public static void roundedRectCustom(final double x, final double y, double width, double height, final double edgeRadius, final Color color, final boolean topLeft, final boolean topRight, final boolean bottomLeft, final boolean bottomRight) {
        final double halfRadius = edgeRadius / 2;
        width -= halfRadius;
        height -= halfRadius;

        float sideLength = (float) edgeRadius;
        sideLength /= 2;
        start();
        if (color != null)
            color(color);

        if (topLeft) {

            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            begin(GL11.GL_TRIANGLE_FAN);

            for (double i = 180; i <= 270; i++) {
                final double angle = i * (Math.PI * 2) / 360;
                vertex(x + (sideLength * Math.cos(angle)) + sideLength, y + (sideLength * Math.sin(angle)) + sideLength);
            }
            vertex(x + sideLength, y + sideLength);


            end();
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
            stop();

        } else {

            rect(x, y, sideLength, sideLength, color);

        }

        sideLength = (float) edgeRadius;
        sideLength /= 2;
        start();
        if (color != null)
            color(color);


        if (bottomRight) {
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            begin(GL11.GL_TRIANGLE_FAN);
            for (double i = 0; i <= 90; i++) {
                final double angle = i * (Math.PI * 2) / 360;
                vertex(x + width + (sideLength * Math.cos(angle)), y + height + (sideLength * Math.sin(angle)));
            }
            vertex(x + width, y + height);
            end();
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
            stop();
        } else {
            rect(x + width, y + height, sideLength, sideLength, color);
        }


        sideLength = (float) edgeRadius;
        sideLength /= 2;
        start();
        if (color != null)
            color(color);


        if (topRight) {
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            begin(GL11.GL_TRIANGLE_FAN);
            for (double i = 270; i <= 360; i++) {
                final double angle = i * (Math.PI * 2) / 360;
                vertex(x + width + (sideLength * Math.cos(angle)), y + (sideLength * Math.sin(angle)) + sideLength);
            }
            vertex(x + width, y + sideLength);
            end();
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
            stop();
        } else {
            rect(x + width, y, sideLength, sideLength, color);
        }


        sideLength = (float) edgeRadius;
        sideLength /= 2;
        start();
        if (color != null)
            color(color);


        if (bottomLeft) {
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            begin(GL11.GL_TRIANGLE_FAN);
            for (double i = 90; i <= 180; i++) {
                final double angle = i * (Math.PI * 2) / 360;
                vertex(x + (sideLength * Math.cos(angle)) + sideLength, y + height + (sideLength * Math.sin(angle)));
            }
            vertex(x + sideLength, y + height);
            end();
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
            stop();
        } else {
            rect(x, y + height, sideLength, sideLength, color);
        }


        // Main block
        rect(x + halfRadius, y + halfRadius, width - halfRadius, height - halfRadius, color);

        // Horizontal bars
        rect(x, y + halfRadius, edgeRadius / 2, height - halfRadius, color);
        rect(x + width, y + halfRadius, edgeRadius / 2, height - halfRadius, color);

        // Vertical bars
        rect(x + halfRadius, y, width - halfRadius, halfRadius, color);
        rect(x + halfRadius, y + height, width - halfRadius, halfRadius, color);
    }

    public void push() {
        GL11.glPushMatrix();
    }

    public void pop() {
        GL11.glPopMatrix();
    }

    public void translate(final double x, final double y) {
        GL11.glTranslated(x, y, 0);
    }

    public void scale(final double x, final double y) {
        GL11.glScaled(x, y, 1);
    }

    public void rotate(final double x, final double y, final double z, final double angle) {
        GL11.glRotated(angle, x, y, z);
    }

    public void color(final double red, final double green, final double blue) {
        color(red, green, blue, 1);
    }

    public void color(Color color, final int alpha) {
        if (color == null)
            color = Color.white;
        color(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, 0.5);
    }

    public void lineWidth(final double width) {
        GL11.glLineWidth((float) width);
    }

    public void rect(final double x, final double y, final double width, final double height, final boolean filled) {
        rect(x, y, width, height, filled, null);
    }

    public void rect(final double x, final double y, final double width, final double height) {
        rect(x, y, width, height, true, null);
    }

    public void rectCentered(double x, double y, final double width, final double height, final boolean filled, final Color color) {
        x -= width / 2;
        y -= height / 2;
        rect(x, y, width, height, filled, color);
    }

    public void rectCentered(double x, double y, final double width, final double height, final boolean filled) {
        x -= width / 2;
        y -= height / 2;
        rect(x, y, width, height, filled, null);
    }

    public void rectCentered(double x, double y, final double width, final double height, final Color color) {
        x -= width / 2;
        y -= height / 2;
        rect(x, y, width, height, true, color);
    }

    public void rectCentered(double x, double y, final double width, final double height) {
        x -= width / 2;
        y -= height / 2;
        rect(x, y, width, height, true, null);
    }

    public void gradient(final double x, final double y, final double width, final double height, final boolean filled, final Color color1, final Color color2) {
        start();
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GlStateManager.enableAlpha();
        GL11.glAlphaFunc(GL_GREATER, 0);
        if (color1 != null)
            color(color1);
        begin(filled ? GL11.GL_QUADS : GL11.GL_LINES);
        {
            vertex(x, y);
            vertex(x + width, y);
            if (color2 != null)
                color(color2);
            vertex(x + width, y + height);
            vertex(x, y + height);
            if (!filled) {
                vertex(x, y);
                vertex(x, y + height);
                vertex(x + width, y);
                vertex(x + width, y + height);
            }
        }
        end();
        GL11.glAlphaFunc(GL_GREATER, 0.1f);
        GlStateManager.disableAlpha();
        GL11.glShadeModel(GL11.GL_FLAT);
        stop();
    }

    public void gradient(final double x, final double y, final double width, final double height, final Color color1, final Color color2) {
        gradient(x, y, width, height, true, color1, color2);
    }

    public void gradientCentered(double x, double y, final double width, final double height, final Color color1, final Color color2) {
        x -= width / 2;
        y -= height / 2;
        gradient(x, y, width, height, true, color1, color2);
    }

    public void gradientSidewaysCentered(double x, double y, final double width, final double height, final Color color1, final Color color2) {
        x -= width / 2;
        y -= height / 2;
        gradientSideways(x, y, width, height, true, color1, color2);
    }

    public void polygon(final double x, final double y, double sideLength, final double amountOfSides, final boolean filled, final Color color) {
        sideLength /= 2;
        start();
        if (color != null)
            color(color);
        if (!filled) GL11.glLineWidth(2);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        begin(filled ? GL11.GL_TRIANGLE_FAN : GL11.GL_LINE_STRIP);
        {
            for (double i = 0; i <= amountOfSides / 4; i++) {
                final double angle = i * 4 * (Math.PI * 2) / 360;
                vertex(x + (sideLength * Math.cos(angle)) + sideLength, y + (sideLength * Math.sin(angle)) + sideLength);
            }
        }
        end();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        stop();
    }

    public void polygon(final double x, final double y, final double sideLength, final int amountOfSides, final boolean filled) {
        polygon(x, y, sideLength, amountOfSides, filled, null);
    }

    public void polygon(final double x, final double y, final double sideLength, final int amountOfSides, final Color color) {
        polygon(x, y, sideLength, amountOfSides, true, color);
    }

    public void polygon(final double x, final double y, final double sideLength, final int amountOfSides) {
        polygon(x, y, sideLength, amountOfSides, true, null);
    }

    public void polygonCentered(double x, double y, final double sideLength, final int amountOfSides, final boolean filled, final Color color) {
        x -= sideLength / 2;
        y -= sideLength / 2;
        polygon(x, y, sideLength, amountOfSides, filled, color);
    }

    public void polygonCentered(double x, double y, final double sideLength, final int amountOfSides, final boolean filled) {
        x -= sideLength / 2;
        y -= sideLength / 2;
        polygon(x, y, sideLength, amountOfSides, filled, null);
    }

    public void polygonCentered(double x, double y, final double sideLength, final int amountOfSides, final Color color) {
        x -= sideLength / 2;
        y -= sideLength / 2;
        polygon(x, y, sideLength, amountOfSides, true, color);
    }

    public void polygonCentered(double x, double y, final double sideLength, final int amountOfSides) {
        x -= sideLength / 2;
        y -= sideLength / 2;
        polygon(x, y, sideLength, amountOfSides, true, null);
    }

    public void circle(final double x, final double y, final double radius, final boolean filled, final Color color) {
        polygon(x, y, radius, 360, filled, color);
    }

    public void circle(final double x, final double y, final double radius, final boolean filled) {
        polygon(x, y, radius, 360, filled);
    }

    public void circle(final double x, final double y, final double radius, final Color color) {
        polygon(x, y, radius, 360, color);
    }

    public void circle(final double x, final double y, final double radius) {
        polygon(x, y, radius, 360);
    }

    public void circleCentered(double x, double y, final double radius, final boolean filled, final Color color) {
        x -= radius / 2;
        y -= radius / 2;
        polygon(x, y, radius, 360, filled, color);
    }

    public void circleCentered(double x, double y, final double radius, final boolean filled) {
        x -= radius / 2;
        y -= radius / 2;
        polygon(x, y, radius, 360, filled);
    }

    public void circleCentered(double x, double y, final double radius, final boolean filled, final int sides) {
        x -= radius / 2;
        y -= radius / 2;
        polygon(x, y, radius, sides, filled);
    }

    public void circleCentered(double x, double y, final double radius, final Color color) {
        x -= radius / 2;
        y -= radius / 2;
        polygon(x, y, radius, 360, color);
    }

    public void circleCentered(double x, double y, final double radius) {
        x -= radius / 2;
        y -= radius / 2;
        polygon(x, y, radius, 360);
    }

    public void line(final double firstX, final double firstY, final double secondX, final double secondY, final double lineWidth, final Color color) {
        start();
        if (color != null)
            color(color);
        lineWidth(lineWidth);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        begin(GL11.GL_LINES);
        {
            vertex(firstX, firstY);
            vertex(secondX, secondY);
        }
        end();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        stop();
    }

    public void line(final double firstX, final double firstY, final double secondX, final double secondY, final double lineWidth) {
        line(firstX, firstY, secondX, secondY, lineWidth, null);
    }

    public void line(final double firstX, final double firstY, final double secondX, final double secondY, final Color color) {
        line(firstX, firstY, secondX, secondY, 0, color);
    }

    public void line(final double firstX, final double firstY, final double secondX, final double secondY) {
        line(firstX, firstY, secondX, secondY, 0, null);
    }

    public void image(final ResourceLocation imageLocation, final float x, final float y, final float width, final float height) {
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        enable(GL11.GL_BLEND);
        GlStateManager.disableAlpha();
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        mc.getTextureManager().bindTexture(imageLocation);
        Gui.drawModalRectWithCustomSizedTexture((int) x, (int) y, (float) 0, (float) 0, (int) width, (int) height, width, height);
        GlStateManager.enableAlpha();
        disable(GL11.GL_BLEND);
    }

    public void imageCentered(final ResourceLocation imageLocation, float x, float y, final float width, final float height) {
        x -= width / 2f;
        y -= height / 2f;
        image(imageLocation, x, y, width, height);
    }

    public void outlineInlinedGradientRect(final double x, final double y, final double width, final double height, final double inlineOffset, final Color color1, final Color color2) {
        gradient(x, y, width, inlineOffset, color1, color2);
        gradient(x, y + height - inlineOffset, width, inlineOffset, color2, color1);
        gradientSideways(x, y, inlineOffset, height, color1, color2);
        gradientSideways(x + width - inlineOffset, y, inlineOffset, height, color2, color1);
    }

    public void roundedOutLine(final double x, final double y, double width, double height, final double thickness, final double edgeRadius, final Color color) {
        final double halfRadius = edgeRadius / 2;
        width -= halfRadius;
        height -= halfRadius;

        float sideLength = (float) edgeRadius;
        sideLength /= 2;
        start();
        if (color != null)
            color(color);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        begin(GL11.GL_LINES);

        {
            for (double i = 180; i <= 270; i++) {
                final double angle = i * (Math.PI * 2) / 360;
                vertex(x + (sideLength * Math.cos(angle)) + sideLength, y + (sideLength * Math.sin(angle)) + sideLength);
            }
            vertex(x + sideLength, y + sideLength);
        }

        end();
        stop();

        sideLength = (float) edgeRadius;
        sideLength /= 2;
        start();
        if (color != null)
            color(color);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        begin(GL11.GL_LINES);

        {
            for (double i = 0; i <= 90; i++) {
                final double angle = i * (Math.PI * 2) / 360;
                vertex(x + width + (sideLength * Math.cos(angle)), y + height + (sideLength * Math.sin(angle)));
            }
            vertex(x + width, y + height);
        }

        end();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        stop();

        sideLength = (float) edgeRadius;
        sideLength /= 2;
        start();
        if (color != null)
            color(color);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        begin(GL_LINES);

        {
            for (double i = 270; i <= 360; i++) {
                final double angle = i * (Math.PI * 2) / 360;
                vertex(x + width + (sideLength * Math.cos(angle)), y + (sideLength * Math.sin(angle)) + sideLength);
            }
            vertex(x + width, y + sideLength);
        }

        end();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        stop();

        sideLength = (float) edgeRadius;
        sideLength /= 2;
        start();
        if (color != null)
            color(color);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        begin(GL_LINES);

        {
            for (double i = 90; i <= 180; i++) {
                final double angle = i * (Math.PI * 2) / 360;
                vertex(x + (sideLength * Math.cos(angle)) + sideLength, y + height + (sideLength * Math.sin(angle)));
            }
            vertex(x + sideLength, y + height);
        }

        end();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        stop();

        // Main block
        //rect(x + halfRadius, y + halfRadius, width - halfRadius, height - halfRadius, color);

        // Horizontal bars
        /*outline(x, y + halfRadius, edgeRadius / 2, height - halfRadius, thickness, color);
        outline(x + width, y + halfRadius, edgeRadius / 2, height - halfRadius, thickness, color);

        // Vertical bars
        outline(x + halfRadius, y, width - halfRadius, halfRadius, thickness, color);
        outline(x + halfRadius, y + height, width - halfRadius, halfRadius, thickness, color);*/
    }

    public void roundedRectTop(final double x, final double y, double width, double height, final double edgeRadius, final Color color) {
        final double halfRadius = edgeRadius / 2;
        width -= halfRadius;
        height -= halfRadius;

        // Top left and right circles
        circle(x, y, edgeRadius, color);
        circle(x + width - edgeRadius / 2, y, edgeRadius, color);
        // Main block
        rect(x, y + halfRadius, width + halfRadius, height, color);

        // Vertical bar
        rect(x + halfRadius, y, width - halfRadius, halfRadius, color);
    }

    public void roundedRectBottom(final double x, final double y, double width, double height, final double edgeRadius, final Color color) {
        final double halfRadius = edgeRadius / 2;
        width -= halfRadius;
        height -= halfRadius;

        // Bottom left and right circles
        circle(x + width - edgeRadius / 2, y + height - edgeRadius / 2, edgeRadius, color);
        circle(x, y + height - edgeRadius / 2, edgeRadius, color);

        // Main block
        rect(x, y, width + halfRadius, height, color);

        // Vertical bar
        rect(x + halfRadius, y + height, width - halfRadius, halfRadius, color);
    }

    public void roundedRectRight(final double x, final double y, double width, double height, final double edgeRadius, final Color color1, final Color color2) {
        final double halfRadius = edgeRadius / 2;
        width -= halfRadius;
        height -= halfRadius;

        // Top left and right circles
        circle(x + width - edgeRadius / 2, y, edgeRadius, color2);
        circle(x + width - edgeRadius / 2, y + height - edgeRadius / 2, edgeRadius, color2);

        // Main block
        gradientSideways(x, y, width, height + halfRadius, color1, color2);

        // Vertical bar
        rect(x + width, y + halfRadius, 5, height - halfRadius, color2);
    }

    public void roundedRectRightTop(final double x, final double y, double width, double height, final double edgeRadius, final Color color1, final Color color2) {
        final double halfRadius = edgeRadius / 2;
        width -= halfRadius;
        height -= halfRadius;

        // Top left and right circles
        circle(x + width - edgeRadius / 2, y, edgeRadius, color2);

        // Main block
        gradientSideways(x, y, width, height + halfRadius, color1, color2);

        // Vertical bar
        rect(x + width, y + halfRadius, 5, height, color2);
    }

    public void roundedRectRightBottom(final double x, final double y, double width, double height, final double edgeRadius, final Color color1, final Color color2) {
        final double halfRadius = edgeRadius / 2;
        width -= halfRadius;
        height -= halfRadius;

        // Bottom left and right circles
        circle(x + width - edgeRadius / 2, y + height - edgeRadius / 2, edgeRadius, color2);

        // Main block
        gradientSideways(x, y, width, height + halfRadius, color1, color2);
        // Vertical bar
        rect(x + width, y, 5, height, color2);
    }

    public void drawBorder(final float x, final float y, final float x2, final float y2, final float width, final int color1) {
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_LINE_SMOOTH);

        color(new Color(color1));
        glLineWidth(width);

        glBegin(GL_LINE_LOOP);

        glVertex2d(x2, y);
        glVertex2d(x, y);
        glVertex2d(x, y2);
        glVertex2d(x2, y2);

        glEnd();

        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
        glDisable(GL_LINE_SMOOTH);
    }
}
