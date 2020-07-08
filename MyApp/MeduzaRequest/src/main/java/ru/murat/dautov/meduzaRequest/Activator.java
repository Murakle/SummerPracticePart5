package ru.murat.dautov.meduzaRequest;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import ru.murat.dautov.request.IRequest;

import java.util.Dictionary;
import java.util.Hashtable;


public class Activator implements BundleActivator {

    private ServiceRegistration serviceRegistration;

    public void start(BundleContext context) throws Exception {
        System.out.println("Start MeduzaRequest");
        Dictionary<String, String> metadata = new Hashtable<>();
        metadata.put("type", "source");
        metadata.put("source", "Meduza");
        serviceRegistration =
                context.registerService(IRequest.class.getName(),
                        new MeduzaRequestImpl(),
                        metadata);
    }

    public void stop(BundleContext context) throws Exception {
        serviceRegistration.unregister();
        System.out.println("Stop MeduzaRequest");
    }
}
