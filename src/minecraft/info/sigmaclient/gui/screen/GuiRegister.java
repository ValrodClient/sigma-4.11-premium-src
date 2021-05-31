package info.sigmaclient.gui.screen;

import info.sigmaclient.Client;
import info.sigmaclient.gui.screen.impl.mainmenu.GuiModdedMainMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.PasswordField;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;

public class GuiRegister extends GuiScreen {
    private GuiTextField username;
    private PasswordField password;
    private GuiTextField mail;
    private GuiTextField captchaField;
    private String error;

    private GuiButton registerButton;
    private GuiButton loginButton;

    @Override
    public void initGui() {
        buttonList.clear();

        buttonList.add(registerButton = new GuiButton(0, this.width / 2 - 100, this.height / 2 + 30, "Register"));
        buttonList.add(loginButton = new GuiButton(1, this.width / 2 - 100, this.height / 2 + 55, "Back to login"));

        username = new GuiTextField(this.eventButton, this.mc.fontRendererObj, this.width / 2 - 100, this.height / 2 - 140, 200, 20);
        username.setRegex("^[A-Za-z0-9\\_]{1,16}$");
        password = new PasswordField(this.mc.fontRendererObj, this.width / 2 - 100, this.height / 2 - 100, 200, 20);
        mail = new GuiTextField(this.eventButton, this.mc.fontRendererObj, this.width / 2 - 100, this.height / 2 - 60, 200, 20);
        captchaField = new GuiTextField(this.eventButton, this.mc.fontRendererObj, this.width / 2 - 100, this.height / 2, 200, 20);
        captchaField.setRegex("^[A-Za-z]{1,6}$");
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        drawString(this.mc.fontRendererObj, "Enter your username:", this.width / 2 - 100, this.height / 2 - 152, -1);
        drawString(this.mc.fontRendererObj, "Enter your password:", this.width / 2 - 100, this.height / 2 - 112, -1);
        drawString(this.mc.fontRendererObj, "Enter your mail:", this.width / 2 - 100, this.height / 2 - 72, -1);
        drawString(this.mc.fontRendererObj, "(optional, required to reset your password)", this.width / 2 - 15, this.height / 2 - 72, Color.gray.getRGB());
        username.drawTextBox();
        password.drawTextBox();
        mail.drawTextBox();
        ResourceLocation captchaRl = null;
        if (error != null) {
            drawCenteredString(this.mc.fontRendererObj, "§cError: " + error, this.width / 2, this.height / 2 - 180, -1);
        }
        if (Client.um.isFinishedLoginSequence()) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiModdedMainMenu());
        }
    }

    @Override
    protected void keyTyped(final char par1, final int par2) {
        if (par2 == Keyboard.KEY_TAB) {
            if (username.isFocused()) {
                username.setFocused(false);
                password.setFocused(true);
            } else if (password.isFocused()) {
                password.setFocused(false);
                mail.setFocused(true);
            } else if (mail.isFocused()) {
                mail.setFocused(false);
                captchaField.setFocused(true);
            }
        } else {
            username.textboxKeyTyped(par1, par2);
            password.textboxKeyTyped(par1, par2);
            mail.textboxKeyTyped(par1, par2);
        }
    }

    @Override
    protected void mouseClicked(final int par1, final int par2, final int par3) throws IOException {
        super.mouseClicked(par1, par2, par3);
        username.mouseClicked(par1, par2, par3);
        password.mouseClicked(par1, par2, par3);
        mail.mouseClicked(par1, par2, par3);
    }

    @Override
    protected void actionPerformed(final GuiButton button) {
        switch (button.id) {
            case 1:
                Minecraft.getMinecraft().displayGuiScreen(new GuiLogin());
                break;
        }
    }
}
