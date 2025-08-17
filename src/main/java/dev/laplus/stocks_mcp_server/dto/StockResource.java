package dev.laplus.stocks_mcp_server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockResource {
  private String code; // 股票代碼
  private String name; // 股票名稱
  private String price; // 成交價
  private String volume; // 成交量
}
