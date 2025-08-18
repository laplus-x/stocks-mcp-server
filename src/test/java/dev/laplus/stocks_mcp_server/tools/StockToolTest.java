package dev.laplus.stocks_mcp_server.tools;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import dev.laplus.stocks_mcp_server.dao.StockListResponse.StockData;
import dev.laplus.stocks_mcp_server.dao.StockNameListResponse.StockNameData;
import dev.laplus.stocks_mcp_server.dto.StockResource;
import dev.laplus.stocks_mcp_server.exceptions.NotFoundException;
import dev.laplus.stocks_mcp_server.usecases.StockService;
import reactor.core.publisher.Mono;

class StockToolTest {

    @Mock
    private StockService stockService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private StockTool stockTool;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Given a valid stock name, when getStock is called, then it should return the mapped StockResource")
    void shouldReturnStockResource() {
        // Given
        StockNameData stockNameData = new StockNameData();
        stockNameData.setCode("2330");
        stockNameData.setId("tse_2330.tw_20250818");
        stockNameData.setName("台積電");

        StockData stockData = new StockData();
        stockData.setCode("2330");
        stockData.setName("台積電");

        StockResource stockResource = new StockResource();
        stockResource.setCode("2330");
        stockResource.setName("台積電");

        given(stockService.getStockName("2330")).willReturn(Mono.just(stockNameData));
        given(stockService.getStockInfo("tse_2330.tw_20250818")).willReturn(Mono.just(stockData));
        given(modelMapper.map(stockData, StockResource.class)).willReturn(stockResource);

        // When
        StockResource result = stockTool.getStock("2330");

        // Then
        then(result).isNotNull();
        then(result.getCode()).isEqualTo("2330");
        then(result.getName()).isEqualTo("台積電");
    }

    @Test
    @DisplayName("Given a non-existent stock name, when getStock is called, then it should throw NotFoundException")
    void shouldThrowStockNameNotFoundException() {
        // Given
        given(stockService.getStockName("9999")).willReturn(Mono.empty());

        // When & Then
        thenThrownBy(() -> stockTool.getStock("9999"))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("No stock found for name or symbol: 9999");
    }

    @Test
    @DisplayName("Given a valid stock name but no stock info, when getStock is called, then it should throw NotFoundException")
    void shouldThrowStockInfoNotFoundException() {
        // Given
        StockNameData stockNameData = new StockNameData();
        stockNameData.setCode("2330");
        stockNameData.setId("tse_2330.tw_20250818");
        stockNameData.setName("台積電");

        given(stockService.getStockName("2330")).willReturn(Mono.just(stockNameData));
        given(stockService.getStockInfo("tse_2330.tw_20250818")).willReturn(Mono.empty());

        // When & Then
        thenThrownBy(() -> stockTool.getStock("2330"))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("No stock info found for stock ID: tse_2330.tw_20250818");
    }
}
