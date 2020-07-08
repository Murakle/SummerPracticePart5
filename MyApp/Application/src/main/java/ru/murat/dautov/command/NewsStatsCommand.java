package ru.murat.dautov.command;

import org.apache.felix.service.command.CommandProcessor;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

import ru.murat.dautov.request.IRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;


@Component(
        property = {
                CommandProcessor.COMMAND_SCOPE + ":String=news",
                CommandProcessor.COMMAND_FUNCTION + ":String=stats"
        },
        service = NewsStatsCommand.class
)
public class NewsStatsCommand {
    private BundleContext context;
    private String[] commonWords = {
            "в", "на", "с", "и", "о", "за", "по", "от", "к", "после", "для", "об", "из-за"
    };
    private char[] extraSymbols = {
            ',', '.', '\"', '\'', '!', '?', '(', ')', '«', '»', '\n'
    };

    private ISourceService sources;
    private Set<String> commonWordsSet;

    @Activate
    public void activate(BundleContext context) {
        this.context = context;
        commonWordsSet = new HashSet<>();
        commonWordsSet.addAll(Arrays.asList(commonWords));
        sources = context.getService(context.getServiceReference(ISourceService.class));
    }


    public void stats() {
        if (sources.size() == 1) {
            System.out.println("zero sources registered");
            return;
        }
        System.out.println("Choose sources from given:");
        for (int i = 0; i < sources.size(); i++) {
            System.out.println(i + ". " + sources.get(i));
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int param = 0;
        try {
            param = Integer.parseInt(reader.readLine());
        } catch (IOException e) {
            System.out.println("Not correct index");
        }
        stats(param);

    }

    public void stats(int param) {
        if (param < 0 || param >= sources.size()) {
            System.out.println("Not correct argument");
            return;
        }
        if (param == 0) {
            statsAll();
            return;
        }
        String result = "";

        Collection<ServiceReference<IRequest>> references = null;
        try {
            references = context.getServiceReferences(IRequest.class, null);
        } catch (InvalidSyntaxException e) {
            e.printStackTrace();
            return;
        }
        System.out.println(references.size());
        for (ServiceReference<IRequest> req :
                references) {

            if (req.getProperty("source").equals(sources.get(param))) {
                IRequest request = context.getService(req);
                try {
                    result = request.getTitles();
                } catch (IOException e) {
                    System.out.println("Can't get http request");
                    return;
                }
                List<Map.Entry<String, Integer>> popularWords = getPopularWords(result, 10);
                for (Map.Entry<String, Integer> s :
                        popularWords) {
                    System.out.println(s.getKey() + " " + s.getValue());
                }
                return;
            }
        }
        System.out.println(sources.get(param) + " request bundle isn't registered");
    }

    public void statsAll() {
        Collection<ServiceReference<IRequest>> references = null;
        try {
            references = context.getServiceReferences(IRequest.class, null);
        } catch (InvalidSyntaxException e) {
            e.printStackTrace();
            return;
        }
        Set<String> used = new HashSet<>();
        StringBuilder result = new StringBuilder();
        for (ServiceReference<IRequest> req :
                references) {

            String name = req.getBundle().getSymbolicName();
            if (used.contains(name)) continue;
            System.out.println(name);
            IRequest request = context.getService(req);
            try {
                result.append(request.getTitles()).append(" ");
            } catch (IOException e) {
                System.out.println("Can't get http request");
                return;
            }
            used.add(name);
        }
        List<Map.Entry<String, Integer>> popularWords = getPopularWords(result.toString(), 10);
        for (Map.Entry<String, Integer> s :
                popularWords) {
            System.out.println(s.getKey() + " " + s.getValue());
        }
    }


    private List<Map.Entry<String, Integer>> getPopularWords(String titles, int amount) {

        for (char c :
                extraSymbols) {
            titles = titles.replace(c, ' '); //todo fast replacement
        }
        String[] words = titles.trim().split(" ");
        Map<String, Integer> amounts = new TreeMap<>();
        for (String word :
                words) {
            if (!word.isBlank()) {
                amounts.put(word, amounts.getOrDefault(word, 0) + 1);
            }
        }

        return amounts.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .filter(e -> !commonWordsSet.contains(e.getKey().toLowerCase()))
                .limit(amount)
                .collect(Collectors.toList());
    }
}
