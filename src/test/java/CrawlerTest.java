import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.jsoup.Jsoup;
import org.junit.Test;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * Created by kangbiao on 2016/12/17.
 *
 */
public class CrawlerTest {

    @Test
    public void test(){
        Date date=new Date();
        DateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        String time=format.format(date);
        System.out.println(time);
    }

    @Test
    public void test1() throws IOException {
        String response = Jsoup.connect("https://flight-api.tuniu.com/query/queryCalendarPrices?callback=jQuery17207817263226723168_1479379501967&%7B%22orgCityCode%22:%22906%22,%22dstCityCode%22:%222802%22,%22type%22:1,%22backDate%22:%22%22%7D").ignoreContentType(true).execute().body();
        String responseJson = response.split("\\(")[1].replace(")", "");
        Gson gson=new Gson();
        Map jsonObject=gson.fromJson(responseJson,new TypeToken<Map<String,Object>>(){}.getType());
        if (!(Boolean)jsonObject.get("success")){
            //TODO

            return;
        }
        ArrayList flightPrices=(ArrayList)jsonObject.get("data");
        if (flightPrices==null||flightPrices.size()<1){
            //TODO 航班价格信息不存在
            return;
        }
        for (Object flightPrice:flightPrices){
            System.out.println(((Map)flightPrice).get("date"));
        }
    }
}
