package dev.laplus.stocks_mcp_server.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.laplus.stocks_mcp_server.dao.StockListResponse;
import dev.laplus.stocks_mcp_server.dao.StockNameListResponse;
import dev.laplus.stocks_mcp_server.exceptions.ParsingException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Repository
public class TwseRepository {
  private final ObjectMapper objectMapper;

  private final WebClient webClient;

  public TwseRepository(
      @Qualifier("twseClientBean") WebClient webClient, ObjectMapper objectMapper) {
    this.webClient = webClient;
    this.objectMapper = objectMapper;
  }

  public Mono<StockNameListResponse> getStockNames(String keyword) {
    return webClient
        .get()
        .uri("/getStockNames.jsp?n={keyword}", keyword)
        .retrieve()
        .bodyToMono(String.class)
        .map(
            resp -> {
              try {
                return objectMapper.readValue(resp, StockNameListResponse.class);
              } catch (JsonProcessingException e) {
                throw new ParsingException("Failed to parse stock name JSON", e);
              }
            });
  }

  public Mono<StockListResponse> getStockInfo(String id) {
    return webClient
        .get()
        .uri("/getStockInfo.jsp?ex_ch={id}&json=1&lang=zh_tw", id)
        .retrieve()
        .bodyToMono(String.class)
        .map(
            resp -> {
              try {
                return objectMapper.readValue(resp, StockListResponse.class);
              } catch (JsonProcessingException e) {
                throw new ParsingException("Failed to parse stock JSON", e);
              }
            });
  }
}
