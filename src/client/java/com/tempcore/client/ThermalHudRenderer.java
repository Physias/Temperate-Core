package com.tempcore.client;

import com.tempcore.Temperate;
import com.tempcore.ThermalState;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;

public class ThermalHudRenderer {

    private static final Identifier TEXTURE = Temperate.id("textures/gui/hud.png");

    //Texture dimensions
    private static final int TEXTURE_WIDTH = 256;
    private static final int TEXTURE_HEIGHT = 144;

    //Thermal colors
    private static final int COLD_COLOR = 0x79CFFF;
    private static final int HOT_COLOR = 0xFF713C;

    //Thermal opacity Levels
    private static final float ALPHA_I = 0.20f;
    private static final float ALPHA_II = 0.40f;
    private static final float ALPHA_III = 0.65f;

    //Transition settings
    private static final float TRANSITION_SPEED = 0.65f;
    private static final float MAX_FRAME_TIME = 0.1f;
    private static final float MIN_VISIBLE_ALPHA = 0.001f;

    private static float coldAlpha = 0.0f;
    private static float hotAlpha = 0.0f;
    private static long lastFrame = 0L;

    private ThermalHudRenderer() {}

    //Main HUD rendering
    public static void render(GuiGraphicsExtractor graphics, DeltaTracker delta) {
        Minecraft mc = Minecraft.getInstance();

        //Disable HUD for non-survival players
        if (mc.player == null || mc.player.isCreative() || mc.player.isSpectator()) {
            reset();
            return;
        }

        ThermalState state = ClientThermalHudState.getState();

        float targetCold = 0.0f;
        float targetHot = 0.0f;

        //Target opacity from thermal state
        switch (state) {
            case COLD_I -> targetCold = ALPHA_I;
            case HOT_I -> targetHot = ALPHA_I;
            case COLD_II -> targetCold = ALPHA_II;
            case HOT_II -> targetHot = ALPHA_II;
            case COLD_III -> targetCold = ALPHA_III;
            case HOT_III -> targetHot = ALPHA_III;

            default -> {}
        }

        //Frame time
        long now = Util.getMillis();
        float elapsed = lastFrame == 0L ? 0.0f : Math.min((now - lastFrame) / 1000.0f, MAX_FRAME_TIME);
        lastFrame = now;

        //Smooth transitions
        float change = elapsed * TRANSITION_SPEED;
        coldAlpha = approach(coldAlpha, targetCold, change);
        hotAlpha = approach(hotAlpha, targetHot, change);

        //Respect hidden vanilla HUD
        if (mc.gui.hud.isHidden()) {
            return;
        }

        //Render cold overlay
        if (coldAlpha > MIN_VISIBLE_ALPHA) {
            drawOverlay(graphics, coldAlpha, COLD_COLOR);
        }

        //Render hot overlay
        if (hotAlpha > MIN_VISIBLE_ALPHA) {
            drawOverlay(graphics, hotAlpha, HOT_COLOR);
        }
    }

    //Smooth interpolation
    private static float approach(float value, float target, float amount) {
        if (value < target) {
            return Math.min(value + amount, target);
        }
        return Math.max(value - amount, target);
    }

    //Draw colored texture
    private static void drawOverlay(GuiGraphicsExtractor graphics, float alpha, int rgb) {
        int opacity = Math.round(alpha * 255.0f);
        int color = (opacity << 24) | rgb;

        graphics.blit(
                RenderPipelines.GUI_TEXTURED, TEXTURE, 0, 0, 0.0f, 0.0f, graphics.guiWidth(), graphics.guiHeight(), TEXTURE_WIDTH, TEXTURE_HEIGHT, TEXTURE_WIDTH, TEXTURE_HEIGHT, color
        );
    }

    //Reset HUD transitions
    public static void reset() {
        coldAlpha = 0.0f;
        hotAlpha = 0.0f;
        lastFrame = 0L;
    }
}
