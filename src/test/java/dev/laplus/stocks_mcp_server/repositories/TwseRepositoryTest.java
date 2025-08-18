package dev.laplus.stocks_mcp_server.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.laplus.stocks_mcp_server.dao.StockListResponse;
import dev.laplus.stocks_mcp_server.dao.StockNameListResponse;
import dev.laplus.stocks_mcp_server.exceptions.ParsingException;
import java.io.IOException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

class TwseRepositoryTest {

  @Nested
  @DisplayName("Given a valid JSON response from TWSE API")
  class SuccessCases {

    @Test
    @DisplayName("When getStockNames is called, Then it should return StockNameListResponse")
    void shouldReturnStockNameListResponse() {
      // Given
      String responseJson =
          "{\"datas\":[{\"c\":\"2330\",\"key\":\"tse_2330_20230818\",\"n\":\"TSMC\"}]}";
      mockWebServer.enqueue(
          new MockResponse().setBody(responseJson).addHeader("Content-Type", "application/json"));

      // When & Then
      StepVerifier.create(twseRepository.getStockNames("2330"))
          .assertNext(
              resp -> {
                assertThat(resp).isInstanceOf(StockNameListResponse.class);
                assertThat(resp.getDatas()).isNotEmpty();
                assertThat(resp.getDatas().get(0).getCode()).isEqualTo("2330");
                assertThat(resp.getDatas().get(0).getName()).isEqualTo("TSMC");
              })
          .verifyComplete();
    }

    @Test
    @DisplayName("When getStockInfo is called, Then it should return StockListResponse")
    void shouldReturnStockListResponse() {
      // Given
      String responseJson =
          "{\"msgArray\":[{\"c\":\"2330\",\"n\":\"TSMC\",\"z\":\"800.00\",\"v\":\"10000\"}]}";
      mockWebServer.enqueue(
          new MockResponse().setBody(responseJson).addHeader("Content-Type", "application/json"));

      // When & Then
      StepVerifier.create(twseRepository.getStockInfo("2330.tw"))
          .assertNext(
              resp -> {
                assertThat(resp).isInstanceOf(StockListResponse.class);
                assertThat(resp.getMsgArray()).isNotEmpty();
                assertThat(resp.getMsgArray().get(0).getCode()).isEqualTo("2330");
                assertThat(resp.getMsgArray().get(0).getName()).isEqualTo("TSMC");
                assertThat(resp.getMsgArray().get(0).getPrice()).isEqualTo("800.00");
                assertThat(resp.getMsgArray().get(0).getVolume()).isEqualTo("10000");
              })
          .verifyComplete();
    }
  }

  @Nested
  @DisplayName("Given an invalid JSON response from TWSE API")
  class FailureCases {

    @Test
    @DisplayName("When getStockNames is called, Then it should throw ParsingException")
    void shouldThrowParsingExceptionForStockNames() {
      // Given
      mockWebServer.enqueue(
          new MockResponse().setBody("INVALID_JSON").addHeader("Content-Type", "application/json"));

      // When & Then
      StepVerifier.create(twseRepository.getStockNames("2330"))
          .expectError(ParsingException.class)
          .verify();
    }

    @Test
    @DisplayName("When getStockInfo is called, Then it should throw ParsingException")
    void shouldThrowParsingExceptionForStockInfo() {
      // Given
      mockWebServer.enqueue(
          new MockResponse().setBody("INVALID_JSON").addHeader("Content-Type", "application/json"));

      // When & Then
      StepVerifier.create(twseRepository.getStockInfo("2330.tw"))
          .expectError(ParsingException.class)
          .verify();
    }
  }

  private static MockWebServer mockWebServer;

  @BeforeAll
  static void startServer() throws IOException {
    mockWebServer = new MockWebServer();
    mockWebServer.start();
  }

  @AfterAll
  static void shutdownServer() throws IOException {
    mockWebServer.shutdown();
  }

  private TwseRepository twseRepository;

  @BeforeEach
  void setUp() {
    String baseUrl = mockWebServer.url("/").toString();
    WebClient webClient = WebClient.builder().baseUrl(baseUrl).build();
    ObjectMapper objectMapper = new ObjectMapper();

    twseRepository = new TwseRepository(webClient, objectMapper);
  }

  @Test
  @DisplayName("Then repository should send correct request path")
  void shouldSendCorrectRequestPath() throws InterruptedException {
    // Given
    mockWebServer.enqueue(
        new MockResponse().setBody("{\"datas\":[]}").addHeader("Content-Type", "application/json"));

    // When
    twseRepository.getStockNames("2330").block();

    // Then
    var recordedRequest = mockWebServer.takeRequest();
    assertThat(recordedRequest.getPath()).isEqualTo("/getStockNames.jsp?n=2330");
  }
}
