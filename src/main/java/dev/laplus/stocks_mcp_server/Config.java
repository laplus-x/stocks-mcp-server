package dev.laplus.stocks_mcp_server;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class Config {
  @Bean
  public WebClient twseClientBean() {
    return WebClient.create("https://mis.twse.com.tw/stock/api");
  }

  @Bean
  public ModelMapper modelMapperBean() {
    return new ModelMapper();
  }
}
