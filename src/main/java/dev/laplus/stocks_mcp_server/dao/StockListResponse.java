package dev.laplus.stocks_mcp_server.dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StockListResponse {
  @Data
  @NoArgsConstructor
  public static class StockData {
    @JsonProperty("c")
    private String code; // 股票代碼 c

    @JsonProperty("n")
    private String name; // 股票名稱 n

    @JsonProperty("z")
    private String price; // 成交價 z

    @JsonProperty("v")
    private String volume; // 成交量 v
  }

  @JsonProperty("msgArray")
  private List<StockData> msgArray;
}
