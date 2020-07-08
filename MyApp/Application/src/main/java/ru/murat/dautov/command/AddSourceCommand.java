package ru.murat.dautov.command;

import org.apache.felix.service.command.CommandProcessor;
import org.osgi.framework.*;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

@Component(
        property = {
                CommandProcessor.COMMAND_SCOPE + ":String=news",
                CommandProcessor.COMMAND_FUNCTION + ":String=add"
        },
        service = AddSourceCommand.class
)
public class AddSourceCommand {

    private BundleContext context;
    private ISourceService sources;

    @Activate
    public void activate(BundleContext context) {
        this.context = context;
        sources = context.getService(context.getServiceReference(ISourceService.class));
    }


    public void add(String pathToJar) {
        Bundle b;
        try {
            context = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
            b = context.installBundle(pathToJar);
        } catch (BundleException e) {
            e.printStackTrace();
            System.out.println(e.getType());
            System.out.println(e.getMessage());
            System.out.println("Not correct path to jar or not correct jar");
            return;
        }
        try {
            b.start();
        } catch (BundleException e) {
            System.out.println("Couldn't start the bundle");
            e.printStackTrace();
        }

        ServiceReference<?>[] services = b.getRegisteredServices();
        for (ServiceReference<?> serviceReference :
                services) {
            if (serviceReference.getProperty("type").equals("source")) {
                sources.addService((String) serviceReference.getProperty("source"));
                return;
            }
        }
        System.out.println("There is no service with type source in this bundle");
    }
}
