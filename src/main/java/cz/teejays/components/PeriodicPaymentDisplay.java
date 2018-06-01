package cz.teejays.components;

import cz.teejays.AppContext;
import cz.teejays.AppOptions;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Properties;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class PeriodicPaymentDisplay {

    private AppContext appContext;
    private Scheduler scheduler;

    public PeriodicPaymentDisplay(AppContext appContext) {
        this.appContext = appContext;

        // quartz scheduler
        try {
            Properties schedulerProperties = new Properties();
            schedulerProperties.setProperty(StdSchedulerFactory.PROP_SCHED_SKIP_UPDATE_CHECK, "true");
            scheduler = StdSchedulerFactory.getDefaultScheduler();
        } catch (SchedulerException e) {
            appContext.getOutputHandler().exitApp("Unable to initialize periodic displaying", -1);
        }
    }

    /**
     * Start periodically running the display job. Schedule the job a starts the scheduler.
     */
    public void startPeriodicDisplaying() {

        // quartz job detail
        JobDataMap jobData = new JobDataMap();
        jobData.put(PeriodicPaymentDisplayJob.APP_CONTEXT_QUARTZ_KEY, appContext); // pass app context to job
        JobDetail job = newJob(PeriodicPaymentDisplayJob.class)
                .usingJobData(jobData)
                .withIdentity("job1", "group1")
                .build();

        // quartz trigger
        Trigger trigger = newTrigger()
                .withIdentity("trigger1", "group1")
                .withSchedule(simpleSchedule()
                        .withIntervalInSeconds(AppOptions.DISPLAY_INTERVAL)
                        .repeatForever())
                .build();

        // quartz schedule
        try {
            scheduler.scheduleJob(job, trigger);
            scheduler.getContext().put(PeriodicPaymentDisplayJob.APP_CONTEXT_QUARTZ_KEY, appContext); // add app context to quartz context
        } catch (SchedulerException e) {
            appContext.getOutputHandler().exitApp("Unable to schedule periodic displaying", -1);
        }

        // start
        try {
            scheduler.start();
        } catch (SchedulerException e) {
            appContext.getOutputHandler().exitApp("Unable to start periodic displaying", -1);
        }
    }

    /**
     * Kill the scheduler.
     */
    public void stopPeriodicDisplaying() {
        try {
            scheduler.shutdown();
        } catch (SchedulerException e) {
            appContext.getOutputHandler().exitApp("Unable to start periodic displaying", -1);
        }
    }
}
