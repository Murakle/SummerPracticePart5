package ru.murat.dautov.command;

import org.osgi.service.component.annotations.Component;

import java.util.ArrayList;
import java.util.List;

@Component(service = ISourceService.class)
public class SourceServiceImpl implements ISourceService {
    private final ArrayList<String> names;

    public SourceServiceImpl() {
        this.names = new ArrayList<>();
        names.add("All");
    }

    @Override
    public List<String> getServices() {
        return names;
    }

    @Override
    public void addService(String name) {
        names.add(name);
    }

    @Override
    public int size() {
        return names.size();
    }

    @Override
    public String get(int index) {
        if (index < 0 || index >= names.size()) {
            return null;
        }
        return names.get(index);
    }

    @Override
    public void remove(int index) {
        names.remove(index);
    }
}
