package info.sigmaclient.module.impl.other;

import info.sigmaclient.Client;
import info.sigmaclient.event.Event;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.management.MoveUtils;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.ModuleManager;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.impl.movement.Bhop;
import info.sigmaclient.module.impl.movement.Fly;
import info.sigmaclient.module.impl.movement.LongJump;
import info.sigmaclient.module.impl.player.Scaffold;
import info.sigmaclient.util.misc.BlockUtils;
import org.apache.commons.codec.binary.Base64;

import java.security.*;
import java.security.spec.X509EncodedKeySpec;

public class ButPainSub {
    private static Fly flyModule;
    private static Scaffold scaffoldModule;
    private static LongJump longjumpModule;
    private static Bhop bhopModule;
    private static boolean qualify = true;
    
    private static Module fakeFlyModule = new Module(new ModuleData(ModuleData.Type.Other, "", "")) {
        @Override
        public void onEvent(Event event) {
            if (event instanceof EventUpdate) {

                EventUpdate em = (EventUpdate) event;

                mc.thePlayer.motionY = 0;
                mc.thePlayer.jumpMovementFactor = 0.31f + MoveUtils.getSpeedEffect() * 0.05f;
                if (mc.gameSettings.keyBindJump.getIsKeyPressed()) {
                    mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.2, mc.thePlayer.posZ);
                }
            }
        }
    };
    private static Module fakeScaffoldModule = new Module(new ModuleData(ModuleData.Type.Other, "", "")) {
        @Override
        public void onEvent(Event event) {
            mc.thePlayer.motionX *= 0.9;
            mc.thePlayer.motionZ *= 0.9;
        }
    };
    private static Module fakeLongjumpModule = new Module(new ModuleData(ModuleData.Type.Other, "", "")) {
        @Override
        public void onEvent(Event event) {
            float x2 = 1f + MoveUtils.getSpeedEffect() * 0.45f;
            if ((mc.thePlayer.moveForward != 0 || mc.thePlayer.moveStrafing != 0) && mc.thePlayer.onGround) {
                mc.thePlayer.motionX *= 1;
                mc.thePlayer.motionZ *= 1;
                mc.thePlayer.jump();
            }
            if (mc.thePlayer.onGround && BlockUtils.isOnGround(0.01)) {

            } else {
                mc.thePlayer.motionX *= 0;
                mc.thePlayer.motionZ *= 0;
                mc.thePlayer.jumpMovementFactor = 0.28f;
            }
        }
    };
    private static Module fakeBhopModule = new Module(new ModuleData(ModuleData.Type.Other, "", "")) {
        @Override
        public void onEvent(Event event) {
            if (event instanceof EventUpdate) {
                if (mc.thePlayer.onGround) {
                    if ((mc.thePlayer.moveForward != 0.0F) || (mc.thePlayer.moveStrafing != 0.0F)) {
                        mc.thePlayer.jump();
                        mc.thePlayer.motionX *= 0.9;
                        mc.thePlayer.motionZ *= 0.9;
                    }
                } else {
                    mc.thePlayer.motionX *= 1.1;
                    mc.thePlayer.motionZ *= 1.1;
                }
            }
        }
    };

    public ButPainSub() {
        ModuleManager m = Client.getModuleManager();
        flyModule = (Fly) m.get(Fly.class);
        scaffoldModule = (Scaffold) m.get(Scaffold.class);
        longjumpModule = (LongJump) m.get(LongJump.class);
        bhopModule = (Bhop) m.get(Bhop.class);
        qualify = true;
    }

    public void onUpdate() {
        if (!qualify) {
            if (flyModule.getPremiumAddon() != null) {
                flyModule.setPremiumAddon(fakeFlyModule);
            }
            if (scaffoldModule.getPremiumAddon() != null) {
                scaffoldModule.setPremiumAddon(fakeScaffoldModule);
            }
            if (longjumpModule.getPremiumAddon() != null) {
                longjumpModule.setPremiumAddon(fakeLongjumpModule);
            }
            if (bhopModule.getPremiumAddon() != null) {
                bhopModule.setPremiumAddon(fakeBhopModule);
            }
        }
    }
    
    public static void oe() {
    	ModuleManager m = Client.getModuleManager();
        flyModule = (Fly) m.get(Fly.class);
        scaffoldModule = (Scaffold) m.get(Scaffold.class);
        longjumpModule = (LongJump) m.get(LongJump.class);
        bhopModule = (Bhop) m.get(Bhop.class);
        
        flyModule.setPremiumAddon(fakeFlyModule);
        scaffoldModule.setPremiumAddon(fakeScaffoldModule);
        longjumpModule.setPremiumAddon(fakeLongjumpModule);
        bhopModule.setPremiumAddon(fakeBhopModule);
    }
}
