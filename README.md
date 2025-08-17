stocks-mcp-server 是一個基於 Spring Boot 與 [Model Context Protocol (MCP)](https://modelcontextprotocol.org/) 的台股查詢後端服務。提供股票名稱/代碼查詢、即時股價、成交量等資訊，並以 AI 工具形式暴露 API，方便集成至 AI Agent 或自動化流程。

# 📈 stocks-mcp-server

> Spring Boot + MCP Server based AI tool service for Taiwan Stock Query

## 📝 Project Overview

stocks-mcp-server is a backend service built with Spring Boot and [Model Context Protocol (MCP)](https://modelcontextprotocol.org/) for querying Taiwan stock information. It provides APIs for searching by stock name/symbol, real-time price, and volume, and exposes these as AI tools for easy integration with AI agents or automation workflows.

## 🏗️ Architecture

- **Spring Boot WebFlux**: High-performance, non-blocking REST API
- **Spring AI MCP Server**: AI tool registration and STDIO/SSE communication
- **TWSE API**: Integration with Taiwan Stock Exchange public data
- **ModelMapper**: Data object mapping
- **Lombok**: Simplified Java POJO syntax

Main modules:
- `Config.java`: Bean registration (RestClient, ModelMapper)
- `StocksMcpServerApplication.java`: Main entry, AI tool registration
- `repositories/TwseRepository.java`: TWSE API integration and data parsing
- `tools/StockTool.java`: AI tool method (getStock)
- `usecases/StockService.java`: Business logic
- `dao/`, `dto/`: Data structures
- `exceptions/`: Error handling

## 🚀 Installation & Startup

### 1. Requirements

- Java 17+
- Maven 3.9+

### 2. Local Startup

```bash
./mvnw spring-boot:run
```

or

```bash
mvn spring-boot:run
```

## 🛠️ API Tool (MCP)

### getStock

- **Description**: Query real-time stock info by name or symbol
- **Parameter**: `name` (stock name or symbol)
- **Response**:
	```json
	{
		"code": "2330",
		"name": "TSMC",
		"price": "800.00",
		"volume": "10000"
	}
	```

### SSE Endpoints

- `/mcp/sse`: MCP Server SSE communication
- `/mcp/sse/messages`: Message push

## 📦 Tech Stack

- Spring Boot 3.5
- Spring AI MCP Server WebFlux
- ModelMapper
- Lombok
- JUnit 5

## 🧩 Directory Structure

```
src/
	main/
		java/dev/laplus/stocks_mcp_server/
			Config.java
			StocksMcpServerApplication.java
			repositories/
			tools/
			usecases/
			dao/
			dto/
			exceptions/
		resources/application.properties
	test/
		java/dev/laplus/stocks_mcp_server/
			StocksMcpServerApplicationTests.java
Taskfile.yml
pom.xml
```

## 🧪 Testing

```bash
./mvnw test
```

## 🤝 Contributing

Issues, PRs, and discussions are welcome!

## 📄 License

Apache License 2.0
