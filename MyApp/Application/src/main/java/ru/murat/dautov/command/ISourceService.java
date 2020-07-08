package ru.murat.dautov.command;

import java.util.List;

public interface ISourceService {

    public List<String> getServices();

    public void addService(String name);

    public int size();

    public String get(int index);

    void remove(int index);
}
