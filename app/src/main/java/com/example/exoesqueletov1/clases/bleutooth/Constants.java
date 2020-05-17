package com.example.exoesqueletov1.clases.bleutooth;

import com.example.exoesqueletov1.BuildConfig;

public class Constants {

    // values have to be globally unique
    static final String INTENT_ACTION_DISCONNECT = BuildConfig.APPLICATION_ID + ".Disconnect";
    static final String NOTIFICATION_CHANNEL = BuildConfig.APPLICATION_ID + ".Channel";
    static final String INTENT_CLASS_MAIN_ACTIVITY = BuildConfig.APPLICATION_ID + ".MainActivity";

    // values have to be unique within each app
    static final int NOTIFY_MANAGER_START_FOREGROUND_SERVICE = 1001;

    public static final String WALK_STEPS = "a";
    public static final String WALK_MINUTES = "b";
    public static final String UP_RIGHT = "c";
    public static final String UP_LEFT = "d";
    public static final String PAUSE = "p";
    public static final String PLAY = "o";
    public static final String STOP = "s";
    public static final String LISTEN = "l";
    public static final String FINALISE = "f";
    public static final String RECEIVED = "r";

    private Constants() {}
}
