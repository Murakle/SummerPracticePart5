package ru.murat.dautov.command;

import org.apache.felix.service.command.CommandProcessor;
import org.osgi.framework.*;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import ru.murat.dautov.request.IRequest;

import java.util.Collection;


@Component(
        property = {
                CommandProcessor.COMMAND_SCOPE + ":String=news",
                CommandProcessor.COMMAND_FUNCTION + ":String=remove"
        },
        service = RemoveSourceCommand.class
)
public class RemoveSourceCommand {

    private BundleContext context;
    private ISourceService sources;

    @Activate
    public void activate(BundleContext context) {
        this.context = context;
        sources = context.getService(context.getServiceReference(ISourceService.class));
    }

    public void remove(int index) {
        if (index < 0 || index >= sources.size()) {
            System.out.println("Not correct index");
            return;
        }
        String name = sources.get(index);
        Collection<ServiceReference<IRequest>> serviceReferences;
        try {
            serviceReferences = context.getServiceReferences(IRequest.class, "(source=" + name + ")");
        } catch (InvalidSyntaxException e) {
            e.printStackTrace();
            return;
        }
        for (ServiceReference<IRequest> ref :
                serviceReferences) {
            Bundle b = ref.getBundle();
            if (b != null) {
                try {
                    b.uninstall();
                } catch (BundleException e) {
                    System.out.println("Can't uninstall bundle");
                    return;
                }
            }
        }
        sources.remove(index);
    }
}
