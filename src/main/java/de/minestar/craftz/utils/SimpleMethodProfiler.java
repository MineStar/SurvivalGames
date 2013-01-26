package de.minestar.craftz.utils;

import java.util.HashMap;

public class SimpleMethodProfiler {

    private static HashMap<String, ProfiledData> profiledMethods;
    private static final boolean ENABLED = true;

    static {
        profiledMethods = new HashMap<String, ProfiledData>();
    }

    public static void startProfiling(String name) {
        if (!ENABLED) {
            return;
        }
        ProfiledData data = new ProfiledData();
        profiledMethods.put(name, data);
        data.startProfiling();
    }

    public static void endProfiling(String name) {
        if (!ENABLED) {
            return;
        }
        ProfiledData data = profiledMethods.get(name);
        if (data != null) {
            data.endProfiling();
            System.out.println("-- " + name + " --");
            System.out.println("NANO: " + data.getTimeInNano());
            System.out.println("MILLI: " + data.getTimeInMilli());
        }
    }

    private static class ProfiledData {
        private long start = Long.MIN_VALUE;
        private long end = Long.MIN_VALUE;
        private long duration = Long.MIN_VALUE;

        public void startProfiling() {
            this.start = System.nanoTime();
        }

        public void endProfiling() {
            this.end = System.nanoTime();
            this.duration = this.end - this.start;
        }

        public long getTimeInNano() {
            return this.duration;
        }

        public float getTimeInMilli() {
            float durationInMS = (float) this.duration;
            return (float) (durationInMS / 1000000f);
        }
    }

}
