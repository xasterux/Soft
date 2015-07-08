package general;

/**
 * Created by ��������� on 07.07.2015.
 */
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;

import jdk.internal.org.objectweb.asm.tree.analysis.Value;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.ContentHandler;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.ParseException;
import org.json.simple.ItemList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONAware;
import org.json.simple.JSONStreamAware;
import org.json.simple.JSONValue;
/**
 * This example demonstrates the use of the {@link ResponseHandler} to simplify
 * the process of processing the HTTP response and releasing associated resources.
 */
public class ClientWithResponseHandler {

    static long offset=585990749; //Начальный оффсет.
    static long currentOffset=0; //Текущий оффсет
    public final static void main(String[] args) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();


        try {
            String str=new String();



            HttpGet getUpdate2 = new HttpGet("https://api.telegram.org/bot113568597:AAE6qeqjK7TyZQcVY222uqrrsYP2_uPCGmc/getUpdates?offset="+offset+"&limit=10"); //УРЛ с getUpdate

            // Create a custom response handler
            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

                @Override
                public String handleResponse(
                        final HttpResponse response) throws ClientProtocolException, IOException {//обработчик ошибок
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {

                            HttpEntity entity = response.getEntity();

                        return entity != null ? EntityUtils.toString(entity) : null;
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }

            };


            JSONParser parser = new JSONParser();
            long chat_id=108311653;
            String messageStr=""; //Строка сообщения
            String temp="";
            String id="id";
            do { //бесконечный цикл, на котором прога и держится
                HttpGet getUpdate = new HttpGet("https://api.telegram.org/bot113568597:AAE6qeqjK7TyZQcVY222uqrrsYP2_uPCGmc/getUpdates?offset="+offset+"&limit=10");

                String responseBody = httpclient.execute(getUpdate, responseHandler); //Строка выполняет URL
                Object obj = parser.parse(responseBody); //парсит выходные строки в json-объекты
                JSONObject jsonObj = (JSONObject) obj;
                JSONArray i=(JSONArray)((java.util.HashMap.Entry)jsonObj.entrySet().toArray()[0]).getValue(); //Определение размера массива, в котором содержатся сообщения
                if(i.size()<10)
                {           //Внизу 2 огромные строки, первая определяет текст последнего сообщения, вторая - его оффсет
                    Object obj1 =  ((HashMap.Entry)((JSONObject)((HashMap.Entry)((JSONObject)((JSONArray)((HashMap.Entry)((JSONObject)obj).entrySet().toArray()[0]).getValue()).get(i.size()-1)).entrySet().toArray()[1]).getValue()).entrySet().toArray()[4]).getValue();
                    currentOffset=((Long)((java.util.HashMap.Entry)((org.json.simple.JSONObject)((org.json.simple.JSONArray)((java.util.HashMap.Entry)jsonObj.entrySet().toArray()[0]).getValue()).get(i.size()-1)).entrySet().toArray()[0]).getValue()).longValue();
                    if(currentOffset!=offset) {

                        messageStr = "Все говорят " + URLEncoder.encode(obj1.toString(),"UTF-8") + ", а ты купи слона";
                        if(obj1.toString().equals("/shialabeouf")) {
                            messageStr ="Do It!";

                        }
                        if(obj1.toString().equals("/rickroll"))
                            messageStr ="Never gonna give you up, never gonna let you down!";
                        messageStr = messageStr.replaceAll(" ", "%20");
                        temp=((java.util.HashMap.Entry)((org.json.simple.JSONObject)((java.util.HashMap.Entry)((org.json.simple.JSONObject)((java.util.HashMap.Entry)((org.json.simple.JSONObject)((org.json.simple.JSONArray)((java.util.HashMap.Entry)jsonObj.entrySet().toArray()[0]).getValue()).get(i.size()-1)).entrySet().toArray()[1]).getValue()).entrySet().toArray()[1]).getValue()).entrySet().toArray()[0]).getKey().toString();
                        // 2 вида описания аккаунта - с фамилией и без
                        if(temp.equals(id)) {
                            chat_id = ((Long) ((java.util.HashMap.Entry) ((org.json.simple.JSONObject) ((java.util.HashMap.Entry) ((org.json.simple.JSONObject) ((java.util.HashMap.Entry) ((org.json.simple.JSONObject) ((org.json.simple.JSONArray) ((java.util.HashMap.Entry) jsonObj.entrySet().toArray()[0]).getValue()).get(i.size() - 1)).entrySet().toArray()[1]).getValue()).entrySet().toArray()[1]).getValue()).entrySet().toArray()[0]).getValue()).longValue();
                        }
                        else {
                            chat_id = ((Long) ((java.util.HashMap.Entry) ((org.json.simple.JSONObject) ((java.util.HashMap.Entry) ((org.json.simple.JSONObject) ((java.util.HashMap.Entry) ((org.json.simple.JSONObject) ((org.json.simple.JSONArray) ((java.util.HashMap.Entry) jsonObj.entrySet().toArray()[0]).getValue()).get(i.size() - 1)).entrySet().toArray()[1]).getValue()).entrySet().toArray()[1]).getValue()).entrySet().toArray()[1]).getValue()).longValue();
                        }
                        HttpGet sendMessage = new HttpGet("https://api.telegram.org/bot113568597:AAE6qeqjK7TyZQcVY222uqrrsYP2_uPCGmc/sendMessage?chat_id=" + chat_id + "&text=" + messageStr);
                        String responseBody2 = httpclient.execute(sendMessage, responseHandler);
                    }
                    offset=currentOffset;
                }
                else{

                    offset=((Long)((java.util.HashMap.Entry)((org.json.simple.JSONObject)((org.json.simple.JSONArray)((java.util.HashMap.Entry)jsonObj.entrySet().toArray()[0]).getValue()).get(i.size()-1)).entrySet().toArray()[0]).getValue()).longValue();

                }


            }while(offset!=0);








        } finally {
            httpclient.close();
        }
    }


}

