package ru.murat.dautov.lentaRequest;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import ru.murat.dautov.request.IRequest;

import java.util.*;


public class Activator implements BundleActivator {

    private ServiceRegistration serviceRegistration;

    public void start(BundleContext context) throws Exception {
        System.out.println("Start LentaRequest activator");
        //noinspection unchecked
        Dictionary<String, String> metadata = new Hashtable<>();
        metadata.put("type", "source");
        metadata.put("source", "Lenta");
        serviceRegistration =
                context.registerService(IRequest.class.getName(),
                        new LentaRequestImpl(),
                        metadata);
    }

    public void stop(BundleContext context) throws Exception {
        serviceRegistration.unregister();
        System.out.println("Stop LentaRequest activator");
    }
}
