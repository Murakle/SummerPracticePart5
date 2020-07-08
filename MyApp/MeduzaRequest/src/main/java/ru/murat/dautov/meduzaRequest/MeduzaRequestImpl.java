package ru.murat.dautov.meduzaRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import ru.murat.dautov.request.IRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;

@Component(service = IRequest.class)
public class MeduzaRequestImpl implements IRequest {
    public static final String address = "https://meduza.io/api/v3/search?chrono=news&locale=ru&page=0&per_page=100";

    public MeduzaRequestImpl() {

    }

    @Override
    public String getTitles() throws IOException {
        // get json file
        URL url = new URL(address);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Accept-Encoding", "gzip");

        BufferedReader in = new BufferedReader(new InputStreamReader(new GZIPInputStream(connection.getInputStream())));
        String inputLine;
        StringBuilder result = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            result.append(inputLine);
        }
        in.close();
        //parse json file (get titles)
        StringBuilder titles = new StringBuilder();
        JSONObject obj = new JSONObject(result.toString());
        JSONObject news = obj.getJSONObject("documents");
        for (String o : news.keySet()) {
            JSONObject n = news.getJSONObject(o);
            titles.append(n.getString("title")).append(" ");

        }
        return titles.toString();
    }

}
