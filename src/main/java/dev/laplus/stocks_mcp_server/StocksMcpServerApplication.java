package dev.laplus.stocks_mcp_server;

import dev.laplus.stocks_mcp_server.tools.StockTool;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class StocksMcpServerApplication {
  public static void main(String[] args) {
    SpringApplication.run(StocksMcpServerApplication.class, args);
  }

  @Bean
  public ToolCallbackProvider tools(StockTool stockTool) {
    return MethodToolCallbackProvider.builder().toolObjects(stockTool).build();
  }
}
