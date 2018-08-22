package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;

public class Playground {
    private static String getProcessId(final String fallback) {
        // Note: may fail in some JVM implementations
        // therefore fallback has to be provided

        // something like '<pid>@<hostname>', at least in SUN / Oracle JVMs
        final String jvmName = ManagementFactory.getRuntimeMXBean().getName();
        final int index = jvmName.indexOf('@');

        if (index < 1) {
            // part before '@' empty (index = 0) / '@' not found (index = -1)
            return fallback;
        }

        try {
            return Long.toString(Long.parseLong(jvmName.substring(0, index)));
        } catch (NumberFormatException e) {
            // ignore
        }
        return fallback;
    }

    private static final Logger LOG = LoggerFactory.getLogger(Playground.class);

    public static void main(final String argv[]) {
        LOG.info("My PID is {}", getProcessId("unknown"));
        final Thread thread = new Thread(Playground::looper);
        thread.start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> stopper(thread)));
    }

    private static void stopper(final Thread thread) {
        LOG.info("Shutdown");
        thread.interrupt();
    }

    private static void looper() {
        while (!Thread.interrupted()) {
            LOG.info("The current time is: {}", System.currentTimeMillis());
            try {
                Thread.sleep(500);
            } catch (final InterruptedException e) {
                LOG.info("Sleep interrupted");
            }
        }
        LOG.info("Exited tick thread");
    }
}
