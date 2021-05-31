package info.sigmaclient.management.users.impl;

import info.sigmaclient.Client;
import info.sigmaclient.gui.click.ClickGui;
import info.sigmaclient.management.users.User;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.ModuleManager;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.module.impl.movement.Fly;

/**
 * Created by Arithmo on 8/11/2017 at 10:08 PM.
 */
public class Staff extends User {

    public Staff(String name, String hwid) {
        setName(name);
        setHWID(hwid);
        upgradeFeatures();
        applyModules();
    }

    public void applyModules() {
        super.applyModules();
        /*ModuleManager m = Client.getModuleManager();
        Module module = (Module) m.get(Fly.class);
        module.getSettings().put(Fly.BYPASS, new Setting<>(Fly.BYPASS, false, "Bypass method for hypixel. (Upgraded Only)"));
        Client.clickGui = new ClickGui();
        Module.loadSettings();*/
    }

    public void upgradeFeatures() {
        super.upgradeFeatures();
    }

}
