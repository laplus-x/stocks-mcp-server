package dev.laplus.stocks_mcp_server.dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StockNameListResponse {
  @Data
  @NoArgsConstructor
  public static class StockNameData {
    @JsonProperty("c")
    private String code; // 股票代碼 c

    @JsonProperty("key")
    private String id; // 識別鍵 (含市場、代碼、日期)

    @JsonProperty("n")
    private String name; // 股票名稱
  }

  @JsonProperty("datas")
  private List<StockNameData> datas;
}
