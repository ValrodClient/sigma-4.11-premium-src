package info.sigmaclient.management.users.impl;

import info.sigmaclient.management.users.User;

/**
 * Created by Arithmo on 8/11/2017 at 9:57 PM.
 */
public class Default extends User {

    public Default() {
        this.setHWID("omikronAllah");
        this.setName("Valrod");
        upgradeFeatures();
        applyModules();
    }

    public void applyModules() {
        super.applyModules();
    }

    public void upgradeFeatures() {
        super.upgradeFeatures();
    }

}
