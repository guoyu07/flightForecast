package org.kangbiao.flightForecast.controller;

import org.jsoup.Jsoup;
import org.kangbiao.flightForecast.dao.CrawlerTaskDao;
import org.kangbiao.flightForecast.dao.FlightPriceDao;
import org.kangbiao.flightForecast.domain.CrawlerTask;
import org.kangbiao.flightForecast.domain.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by kangbiao on 2016/12/18.
 *
 */
@RestController
@RequestMapping(value = "/flight")
public class FlightController {

    @Value("${flight.interface.price-trend-url}")
    private String priceTrendUrl;


    @Autowired
    private CrawlerTaskDao crawlerTaskDao;

    @Autowired
    private FlightPriceDao flightPriceDao;

    @RequestMapping(value = "/getPriceTrend")
    public String  getPriceTrend(@Param(value = "orgCityCode") String orgCityCode,
                                 @Param(value = "dstCityCode") String dstCityCode) throws IOException {

        String url= priceTrendUrl.replaceFirst("\\{dstCityCode\\}",dstCityCode)
                .replaceFirst("\\{orgCityCode\\}",orgCityCode);
        String response=Jsoup.connect(url).ignoreContentType(true).execute().body();
        return response.split("\\(")[1].replace(")","");
    }

    @RequestMapping(value = "/queryByBuyDate")
    public Response queryByBuyDate(HttpServletRequest request){
        Response response=new Response();
        String distCityCode=request.getParameter("distCityCode");
        String orgCityCode=request.getParameter("orgCityCode");
        String date=request.getParameter("date");
        CrawlerTask crawlerTask=crawlerTaskDao.findByOrgDistCityCode(orgCityCode,distCityCode);
        if (crawlerTask==null){
            response.setMsg("该航线任务未配置");
            return response;
        }
        response.setData(flightPriceDao.findByTaskIdBuyDate(crawlerTask.getId(),date));
        return response;
    }
}
