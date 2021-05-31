package info.sigmaclient.management.users;

import info.sigmaclient.json.Json;
import info.sigmaclient.json.JsonObject;
import info.sigmaclient.json.JsonValue;
import info.sigmaclient.management.users.impl.*;
import info.sigmaclient.Client;
import info.sigmaclient.util.security.Crypto;
import info.sigmaclient.util.security.HardwareUtil;

import java.io.*;
import java.net.*;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by Arithmo on 8/11/2017 at 9:57 PM.
 */
public class UserManager {

    private User userStatus = new Upgraded("Valrod", "yes");

    public User getUser() {
        return userStatus;
    }

    private String initialHWID;
    private String username;
    private String versionString = "unknown";
    private boolean updateAvailable = false;
    private boolean updateNeeded = false;
    private String newVersionName = "";
    private ArrayList<String> newChangelog = new ArrayList<>();
    private int updateProgress = 0;

    private String firstHWID;
    private String secondHWID;
    private boolean loginNeeded = false;
    private boolean premium = true;
    private boolean trolled = false;
    private boolean finishedLoginSequence = true;
    private String userSerialNumber = HardwareUtil.getUserSerialNumber();
    private String session = UUID.randomUUID().toString().replaceAll("-", "");
    private String premsTimestamp = null;

    private byte[] hwid;

    public UserManager() {
    }

    public boolean isUpdateAvailable() {
        return updateAvailable;
    }

    public boolean isUpdateNeeded() {
        return updateNeeded;
    }

    public ArrayList<String> getNewChangelog() {
        return newChangelog;
    }

    public String getNewVersionName() {
        return newVersionName;
    }

    public int getUpdateProgress() {
        return updateProgress;
    }

    public boolean isLoginNeeded() {
        return false;
    }

    public boolean isFinishedLoginSequence() {
        return finishedLoginSequence;
    }

    public void setFinishedLoginSequence() {
        finishedLoginSequence = true;
    }

    public String getVersionString() {
        return versionString;
    }

    public String getSecondHWID() {
        return secondHWID;
    }

    public String getSession() {
        return session;
    }

    public boolean isTrolled() {
        return false;
    }

    public String getUserSerialNumber() {
        return userSerialNumber;
    }

    public String getUsername() {
        return username;
    }

    public String getPremsTimestamp() {
        return premsTimestamp;
    }

    public boolean isPremium() {
        return true;
    }
}
