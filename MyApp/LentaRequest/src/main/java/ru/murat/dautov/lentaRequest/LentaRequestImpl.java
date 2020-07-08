package ru.murat.dautov.lentaRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import ru.murat.dautov.request.IRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Component(service = IRequest.class)
public class LentaRequestImpl implements IRequest {
    public static final String address = "https://api.lenta.ru/lists/latest";

    public LentaRequestImpl() {

    }

    @Override
    public String getTitles() throws IOException {
        // get json file

        URL url = new URL(address);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder result = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            result.append(inputLine);
        }
        in.close();

        //parse json file (get titles)
        StringBuilder titles = new StringBuilder();
        JSONObject obj = new JSONObject(result.toString());
        JSONArray news = obj.getJSONArray("headlines");
        for (Object o : news) {
            if (o instanceof JSONObject) {
                JSONObject n = (JSONObject) o;
                titles.append(n.getJSONObject("info").getString("title")).append(" ");
            }
        }
        return titles.toString();
    }

}
