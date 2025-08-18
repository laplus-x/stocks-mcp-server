package dev.laplus.stocks_mcp_server.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dev.laplus.stocks_mcp_server.dao.StockListResponse;
import dev.laplus.stocks_mcp_server.dao.StockListResponse.StockData;
import dev.laplus.stocks_mcp_server.dao.StockNameListResponse;
import dev.laplus.stocks_mcp_server.dao.StockNameListResponse.StockNameData;
import dev.laplus.stocks_mcp_server.repositories.TwseRepository;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class StockServiceTest {

    @Nested
    @DisplayName("Given valid data from TwseRepository")
    class SuccessCases {

        @Test
        @DisplayName("When getStockName is called, Then it should return first StockNameData")
        void shouldReturnFirstStockNameData() {
            // Given
            StockNameData stockNameData1 = new StockNameData();
            stockNameData1.setCode("2330");
            stockNameData1.setName("台積電");

            StockNameData stockNameData2 = new StockNameData();
            stockNameData2.setCode("2317");
            stockNameData2.setName("鴻海");

            StockNameListResponse response = new StockNameListResponse();
            response.setDatas(List.of(stockNameData1, stockNameData2));

            when(twseRepository.getStockNames(anyString())).thenReturn(Mono.just(response));

            // When & Then
            StepVerifier.create(stockService.getStockName("2330"))
                    .consumeNextWith(result -> {
                        assertThat(result.getCode()).isEqualTo("2330");
                        assertThat(result.getName()).isEqualTo("台積電");
                    })
                    .verifyComplete();
        }

        @Test
        @DisplayName("When getStockInfo is called, Then it should return first StockData")
        void shouldReturnFirstStockData() {
            // Given
            StockData stockData1 = new StockData();
            stockData1.setCode("2330");
            stockData1.setName("台積電");

            StockData stockData2 = new StockData();
            stockData2.setCode("2317");
            stockData2.setName("鴻海");

            StockListResponse response = new StockListResponse();
            response.setMsgArray(List.of(stockData1, stockData2));

            when(twseRepository.getStockInfo(anyString())).thenReturn(Mono.just(response));

            // When & Then
            StepVerifier.create(stockService.getStockInfo("2330"))
                    .consumeNextWith(result -> {
                        assertThat(result.getCode()).isEqualTo("2330");
                        assertThat(result.getName()).isEqualTo("台積電");
                    })
                    .verifyComplete();
        }
    }

    @Nested
    @DisplayName("Given empty list from TwseRepository")
    class EmptyCases {

        @Test
        @DisplayName("When getStockName is called, Then it should complete without value")
        void shouldReturnEmptyForStockName() {
            // Given
            StockNameListResponse response = new StockNameListResponse();
            response.setDatas(List.of()); // empty list

            when(twseRepository.getStockNames(anyString())).thenReturn(Mono.just(response));

            // When & Then
            StepVerifier.create(stockService.getStockName("2330"))
                    .verifyComplete(); // no value emitted
        }

        @Test
        @DisplayName("When getStockInfo is called, Then it should complete without value")
        void shouldReturnEmptyForStockInfo() {
            // Given
            StockListResponse response = new StockListResponse();
            response.setMsgArray(List.of()); // empty list

            when(twseRepository.getStockInfo(anyString())).thenReturn(Mono.just(response));

            // When & Then
            StepVerifier.create(stockService.getStockInfo("2330"))
                    .verifyComplete(); // no value emitted
        }
    }

    @Mock
    private TwseRepository twseRepository;

    private StockService stockService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        stockService = new StockService(twseRepository);
    }
}
