package dev.laplus.stocks_mcp_server.usecases;

import dev.laplus.stocks_mcp_server.dao.StockListResponse.StockData;
import dev.laplus.stocks_mcp_server.dao.StockNameListResponse.StockNameData;
import dev.laplus.stocks_mcp_server.repositories.TwseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class StockService {
  @Autowired private TwseRepository twseRepository;

  public Mono<StockNameData> getStockName(String keyword) {
    return twseRepository
        .getStockNames(keyword)
        .flatMapMany(resp -> Flux.fromIterable(resp.getDatas()))
        .next();
  }

  public Mono<StockData> getStockInfo(String id) {
    return twseRepository
        .getStockInfo(id)
        .flatMapMany(resp -> Flux.fromIterable(resp.getMsgArray()))
        .next();
  }
}
