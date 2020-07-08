package ru.murat.dautov.command;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

    public void start(BundleContext context) {
        context.registerService(
                ISourceService.class.getName(),
                new SourceServiceImpl(),
                null);

        context.registerService(
                NewsStatsCommand.class.getName(),
                new NewsStatsCommand(), null);
        context.registerService(
                AddSourceCommand.class.getName(),
                new AddSourceCommand(), null);
        context.registerService(
                RemoveSourceCommand.class.getName(),
                new RemoveSourceCommand(), null);

    }

    public void stop(BundleContext context) {

    }
}