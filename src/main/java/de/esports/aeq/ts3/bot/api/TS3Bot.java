package de.esports.aeq.ts3.bot.api;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3ApiAsync;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.reconnect.ConnectionHandler;
import de.esports.aeq.ts3.bot.api.cache.BotCache;
import de.esports.aeq.ts3.bot.service.api.BotService;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class TS3Bot {

    private static final Logger LOG = LoggerFactory.getLogger(TS3Bot.class);

    private BotConfig config;

    private Scheduler scheduler;
    private TS3Query query;

    private Map<JobDetail, Trigger> jobs = new HashMap<>();
    private boolean jobsInitialized = false;

    private BotService service; // TODO
    private BotCache cache;

    public TS3Bot(BotConfig config, BotService service) {
        this.config = config;
        this.service = service;
        query = new TS3Query(createTS3Config());
    }

    public void connect() {
        synchronized (this) {
            query.connect();
        }
    }

    public void disconnect() {
        synchronized (this) {
            unscheduleAllJobs();
            query.exit();
        }
    }

    public void reconnect() {
        if (query.isConnected()) {
            disconnect();
        }
        connect();
    }

    public boolean isConnected() {
        return query.isConnected();
    }

    public TS3Api getApi() {
        return query.getApi();
    }

    public TS3ApiAsync getApiAsync() {
        return query.getAsyncApi();
    }

    public BotConfig getConfig() {
        return config;
    }

    public BotCache getCache() {
        return cache;
    }

    public BotService getService() {
        return service;
    }

    private TS3Config createTS3Config() {
        TS3Config ts3Config = new TS3Config();
        ts3Config.setConnectionHandler(new BotConnectionHandler());
        return ts3Config;
    }

    private void unscheduleAllJobs() {
        jobs.values().forEach(trigger -> {
            try {
                scheduler.unscheduleJob(trigger.getKey());
            } catch (SchedulerException e) {
                LOG.error("Cannot unschedule job", e);
            }
        });
        jobsInitialized = false;
    }

    public void addJob(JobDetail detail, Trigger trigger) {
        jobs.put(detail, trigger);
        synchronized (this) {
            if (isConnected()) {
                try {
                    scheduler.scheduleJob(detail, trigger);
                } catch (SchedulerException e) {
                    LOG.error("Cannot schedule job", e);
                }
            }
        }
    }

    private class BotConnectionHandler implements ConnectionHandler {

        @Override
        public void onConnect(TS3Query ts3Query) {
            if (jobsInitialized) {
                resumeAllJobs();
            } else {
                scheduleAllJobs();
            }
        }

        @Override
        public void onDisconnect(TS3Query ts3Query) {
            pauseAllJobs();
        }

        private void scheduleAllJobs() {
            jobs.forEach((detail, trigger) -> {
                try {
                    scheduler.scheduleJob(detail, trigger);
                } catch (SchedulerException e) {
                    LOG.error("Cannot schedule job", e);
                }
            });
            jobsInitialized = true;
        }

        private void pauseAllJobs() {
            jobs.keySet().forEach(detail -> {
                try {
                    scheduler.pauseJob(detail.getKey());
                } catch (SchedulerException e) {
                    LOG.error("Cannot pause job", e);
                }
            });
        }

        private void resumeAllJobs() {
            jobs.keySet().forEach(detail -> {
                try {
                    scheduler.resumeJob(detail.getKey());
                } catch (SchedulerException e) {
                    LOG.error("Cannot resume job", e);
                }
            });
        }
    }
}
