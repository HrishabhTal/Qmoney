
package com.crio.warmup.stock.quotes;

import com.crio.warmup.stock.dto.Candle;
import com.crio.warmup.stock.dto.TiingoCandle;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.web.client.RestTemplate;

public class TiingoService implements StockQuotesService {
  private static ObjectMapper getObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    return objectMapper;
  }
private RestTemplate restTemplate;
  protected TiingoService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }
  @Override
  public List<Candle> getStockQuote(String symbol, LocalDate from, LocalDate to)
      throws JsonProcessingException {
        List<Candle>stock1;
        if(from.compareTo(to)>=0)
        {
          throw new RuntimeException();
        }
     String url=buildUri(symbol, from, to);
     String stocks=restTemplate.getForObject(url,String.class);
     ObjectMapper objectMapper=getObjectMapper();
     TiingoCandle[] stock=objectMapper.readValue(stocks,TiingoCandle[].class);
     if(stock!=null)
     {
      stock1=Arrays.asList(stock);
    }
    else
    {
     stock1=Arrays.asList(new TiingoCandle[0]);
    }
    return stock1;
  }
  protected String buildUri(String symbol, LocalDate startDate, LocalDate endDate) {
    //String uriTemplate = "https:api.tiingo.com/tiingo/daily/$SYMBOL/prices?"
         //+ "startDate=$STARTDATE&endDate=$ENDDATE&token=$APIKEY";
         String s= "https://api.tiingo.com/tiingo/daily/"+symbol+"/prices?startDate="+startDate.toString()+"&endDate="+endDate.toString()+"&token="+"1de01a245eb30551bc3d3feb89bf372f21b69833";
         return s;
}


  // TODO: CRIO_TASK_MODULE_ADDITIONAL_REFACTOR
  //  Implement getStockQuote method below that was also declared in the interface.

  // Note:
  // 1. You can move the code from PortfolioManagerImpl#getStockQuote inside newly created method.
  // 2. Run the tests using command below and make sure it passes.
  //    ./gradlew test --tests TiingoServiceTest


  //CHECKSTYLE:OFF

  // TODO: CRIO_TASK_MODULE_ADDITIONAL_REFACTOR
  //  Write a method to create appropriate url to call the Tiingo API.

}
