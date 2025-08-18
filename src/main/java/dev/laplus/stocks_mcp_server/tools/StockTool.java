package dev.laplus.stocks_mcp_server.tools;

import dev.laplus.stocks_mcp_server.dto.StockResource;
import dev.laplus.stocks_mcp_server.exceptions.NotFoundException;
import dev.laplus.stocks_mcp_server.usecases.StockService;
import org.modelmapper.ModelMapper;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class StockTool {
  private final ModelMapper modelMapper;

  private final StockService stockService;

  public StockTool(ModelMapper modelMapper, StockService stockService) {
    this.modelMapper = modelMapper;
    this.stockService = stockService;
  }

  @Tool(name = "getStock", description = "Retrieve stock information by stock name or symbol")
  public StockResource getStock(String name) {
    return stockService
        .getStockName(name)
        .switchIfEmpty(
            Mono.error(new NotFoundException("No stock found for name or symbol: " + name)))
        .flatMap(
            stockName ->
                stockService
                    .getStockInfo(stockName.getId())
                    .switchIfEmpty(
                        Mono.error(
                            new NotFoundException(
                                "No stock info found for stock ID: " + stockName.getId()))))
        .map(stock -> modelMapper.map(stock, StockResource.class))
        .block();
  }
}
