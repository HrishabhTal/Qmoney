
package com.crio.warmup.stock.portfolio;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.SECONDS;

import com.crio.warmup.stock.dto.AnnualizedReturn;
import com.crio.warmup.stock.dto.Candle;
import com.crio.warmup.stock.dto.PortfolioTrade;
import com.crio.warmup.stock.dto.TiingoCandle;
import com.crio.warmup.stock.quotes.StockQuotesService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.springframework.web.client.RestTemplate;

public class PortfolioManagerImpl implements PortfolioManager
{

  private RestTemplate restTemplate;


  // Caution: Do not delete or modify the constructor, or else your build will break!
  // This is absolutely necessary for backward compatibility
  protected PortfolioManagerImpl(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }


  //TODO: CRIO_TASK_MODULE_REFACTOR
  // 1. Now we want to convert our code into a module, so we will not call it from main anymore.
  //    Copy your code from Module#3 PortfolioManagerApplication#calculateAnnualizedReturn
  //    into #calculateAnnualizedReturn function here and ensure it follows the method signature.
  // 2. Logic to read Json file and convert them into Objects will not be required further as our
  //    clients will take care of it, going forward.

  // Note:
  // Make sure to exercise the tests inside PortfolioManagerTest using command below:
  // ./gradlew test --tests PortfolioManagerTest

  //CHECKSTYLE:OFF

private StockQuotesService stockQuotesService;
PortfolioManagerImpl(StockQuotesService stockQuotesService){
  this.stockQuotesService=stockQuotesService;
}




  private Comparator<AnnualizedReturn> getComparator() {
    return Comparator.comparing(AnnualizedReturn::getAnnualizedReturn).reversed();
  }

  //CHECKSTYLE:OFF

  // TODO: CRIO_TASK_MODULE_REFACTOR
  //  Extract the logic to call Tiingo third-party APIs to a separate function.
  //  Remember to fill out the buildUri function and use that.


  public List<Candle> getStockQuote(String symbol, LocalDate from, LocalDate to)
      throws JsonProcessingException {
        return stockQuotesService.getStockQuote(symbol, from, to);
  }

  protected String buildUri(String symbol, LocalDate startDate, LocalDate endDate) {
       //String uriTemplate = "https:api.tiingo.com/tiingo/daily/$SYMBOL/prices?"
            //+ "startDate=$STARTDATE&endDate=$ENDDATE&token=$APIKEY";
            String s= "https://api.tiingo.com/tiingo/daily/"+symbol+"/prices?startDate="+startDate.toString()+"&endDate="+endDate.toString()+"&token="+"1de01a245eb30551bc3d3feb89bf372f21b69833";
            return s;
  }

  public  AnnualizedReturn getAnnualizedReturn(PortfolioTrade trade,
  LocalDate endLocalDate) 
  {
    AnnualizedReturn annualizedReturn;
    String symbol=trade.getSymbol();
    LocalDate start=trade.getPurchaseDate();
    try
    {
      List<Candle> stocks;
      stocks=getStockQuote(symbol, start, endLocalDate);
      Candle start1=stocks.get(0);
      Candle latest=stocks.get(stocks.size()-1);
      Double buyPrice=start1.getOpen();
      Double sellPrice=latest.getClose();
      Double totalReturn=(sellPrice-buyPrice)/buyPrice;
      Double years=(double)ChronoUnit.DAYS.between(start,endLocalDate)/365;
      Double annualizedReturns=Math.pow((1+totalReturn),(1/years))-1;
      annualizedReturn=new AnnualizedReturn(symbol, annualizedReturns, totalReturn);
    }catch(JsonProcessingException e){
      annualizedReturn=new AnnualizedReturn(symbol,Double.NaN,Double.NaN);
    }
      return annualizedReturn;


    }

  @Override
  public List<AnnualizedReturn> calculateAnnualizedReturn(List<PortfolioTrade> portfolioTrades,
      LocalDate endDate) 
      {
      AnnualizedReturn annualizedReturn;
      List<AnnualizedReturn>ret=new ArrayList<AnnualizedReturn>();
      for(int i=0;i<portfolioTrades.size();i++)
      {
        annualizedReturn=getAnnualizedReturn(portfolioTrades.get(i),endDate);
        ret.add(annualizedReturn);
      }
      Comparator<AnnualizedReturn>SortbyAnnReturn=Comparator.comparing(AnnualizedReturn::getAnnualizedReturn).reversed();
      Collections.sort(ret,SortbyAnnReturn);
      return ret;

      
  }




  // ¶TODO: CRIO_TASK_MODULE_ADDITIONAL_REFACTOR
  //  Modify the function #getStockQuote and start delegating to calls to
  //  stockQuoteService provided via newly added constructor of the class.
  //  You also have a liberty to completely get rid of that function itself, however, make sure
  //  that you do not delete the #getStockQuote function.

}
