package net.minecraft.util;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class MouseHelper {
    /**
     * Mouse delta X this frame
     */
    public int deltaX;

    /**
     * Mouse delta Y this frame
     */
    public int deltaY;
    private static final String __OBFID = "CL_00000648";

    /**
     * Grabs the mouse cursor it doesn't move and isn't seen.
     */
    public void grabMouseCursor() {
        Mouse.setGrabbed(true);
        deltaX = 0;
        deltaY = 0;
    }

    /**
     * Ungrabs the mouse cursor so it can be moved and set it to the center of
     * the screen
     */
    public void ungrabMouseCursor() {
        Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
        Mouse.setGrabbed(false);
    }

    public void mouseXYChange() {
        deltaX = Mouse.getDX();
        deltaY = Mouse.getDY();
    }
}
