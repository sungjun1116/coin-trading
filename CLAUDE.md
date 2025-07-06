# 🚀 알고리즘 트레이딩 플랫폼 - Claude 지침서

## 📋 기술 스택

이 프로젝트는 다음과 같은 기술 스택을 사용합니다:

### 핵심 기술
- **Java 21 LTS** - 최신 LTS 버전의 Java 언어
- **Spring Boot 3.x** - 엔터프라이즈급 애플리케이션 프레임워크
- **Gradle 8.x** - 빌드 자동화 도구

### 주요 의존성
- **Spring Web** - RESTful 웹 서비스 개발 (HTTP Interface 포함)
- **Spring Validation** - 데이터 검증 및 바인딩
- **Spring Actuator** - 애플리케이션 모니터링 및 관리
- **Lombok** - 코드 간소화 및 보일러플레이트 제거

### 프론트엔드 기술
- **Thymeleaf 3.x** - 서버 사이드 템플릿 엔진
  - 자연스러운 템플릿: HTML 파일 자체가 브라우저에서 렌더링 가능
  - Spring Boot와 완벽한 통합
  - 국제화(i18n) 지원
- **Bootstrap 5** - 반응형 CSS 프레임워크
- **Vanilla JavaScript + ES6+** - 모던 JavaScript 문법 사용
- **WebSocket + STOMP** - 실시간 통신 프로토콜
- **Chart.js** - 실시간 차트 라이브러리
- **ApexCharts** - 고급 차트 및 데이터 시각화

### 개발 도구
- **Spring Boot DevTools** - 개발 생산성 향상
- **JUnit 5** - 단위 테스트 프레임워크
- **AssertJ** - 테스트 어설션 라이브러리

## 🏗️ 프로젝트 구조

```text
📁 Application Layer
├── 🎯 Service Layer (비즈니스 로직)
└── 🏗️ Infrastructure Layer
    ├── 🌐 Client Layer (외부 API 클라이언트)
    │   ├── binance/                    # Binance 거래소 통합
    │   │   ├── request/                # 요청 DTO
    │   │   └── response/               # 응답 DTO
    │   └── coinone/                    # Coinone 거래소 통합
    │       ├── request/                # 요청 DTO
    │       └── response/               # 응답 DTO
    ├── ⚙️ Config Layer (설정 및 구성)
    │   ├── binance/                    # Binance 설정
    │   └── coinone/                    # Coinone 설정
    ├── 🚨 Exception Layer (예외 처리)
    │   ├── binance/                    # Binance 커스텀 예외
    │   └── coinone/                    # Coinone 커스텀 예외
    └── 🔌 Interceptor Layer (미들웨어)
        ├── binance/                    # Binance 인증
        ├── coinone/                    # Coinone 인증
        └── common/                     # 공통 인터셉터

🔧 Utility Layer
└── util/                              # 암호화, 서명 등 유틸리티

🧪 Test Layer
├── service/                           # 서비스 계층 테스트
└── infrastructure/                    # 인프라 계층 테스트
    ├── client/                        # API 클라이언트 테스트
    └── config/                        # 설정 테스트
```

### 주요 디렉토리 역할

- **`service/`** - 비즈니스 로직과 도메인 규칙을 포함하는 서비스 계층
- **`infrastructure/client/`** - 외부 거래소 API와의 통신을 담당하는 클라이언트 계층
- **`infrastructure/config/`** - Spring 설정, 프로퍼티, REST 클라이언트 구성
- **`infrastructure/exception/`** - 거래소별 커스텀 예외 처리
- **`infrastructure/interceptor/`** - 인증, 로깅, 요청/응답 처리 미들웨어
- **`util/`** - 암호화, 서명, 유틸리티 함수들
- **`test/`** - 단위 테스트, 통합 테스트, 테스트 지원 클래스들

### 아키텍처 특징

이 프로젝트는 **계층형 아키텍처(Layered Architecture)**를 따라 관심사를 명확히 분리했습니다:

1. **애플리케이션 계층**: 메인 진입점과 애플리케이션 로직
2. **서비스 계층**: 비즈니스 로직과 도메인 규칙
3. **인프라스트럭처 계층**: 외부 시스템 통합, 설정, 기술적 관심사

## 📏 코드 스타일 및 규칙

이 프로젝트는 [Spring Framework Code Style](https://github.com/spring-projects/spring-framework/wiki/Code-Style)을 따릅니다:

### 네이밍 규칙
- **클래스명**: PascalCase (예: `CoinoneService`, `BinanceAccountApiClient`)
- **메서드명**: camelCase (예: `getAccountInfo`, `placeOrder`)
- **변수명**: camelCase (예: `accessToken`, `requestBody`)
- **상수명**: UPPER_SNAKE_CASE (예: `DEFAULT_TIMEOUT`, `MAX_RETRY_COUNT`)

### 코드 구조
- **패키지 구조**: 계층형 아키텍처를 반영한 패키지 구조
- **의존성 주입**: `@Autowired` 대신 생성자 주입 사용
- **예외 처리**: 커스텀 예외 클래스 사용
- **검증**: Spring Validation 어노테이션 사용

### 문서화
- **JavaDoc**: public 메서드와 클래스에 대한 문서화
- **주석**: 복잡한 비즈니스 로직에 대한 설명
- **README**: 프로젝트 설정 및 사용법 문서화

### 테스트 규칙
- **단위 테스트**: 모든 public 메서드에 대한 테스트
- **통합 테스트**: 외부 API 호출에 대한 통합 테스트
- **테스트 명명**: `should_ReturnExpectedResult_When_GivenCondition` 형식

## 📝 문서 작성 언어 가이드

이 프로젝트의 문서 작성 언어는 다음과 같습니다:

### 언어 규칙
- **한국어**: 대부분의 마크다운 문서(.md)는 한국어로 작성
- **영어**: README.md 파일만 영어로 작성 (국제적 접근성을 위해)

### 적용 대상
- **한국어 문서**: CLAUDE.md, docs/ 디렉토리의 모든 마크다운 파일
- **영어 문서**: README.md (프로젝트 소개, 설치 방법, 사용법 등)

### 작성 가이드
- 기술 용어는 영어 그대로 사용 (예: API, REST, JSON)
- 한국어 문서에서도 코드 예시와 명령어는 영어로 작성
- README.md는 국제 개발자들이 이해할 수 있도록 명확한 영어로 작성

### 문서화 시 필수 포함 사항
#### PR과 이슈 연결 가이드 포함
모든 문서화 작업 시 다음 내용을 반드시 포함해야 합니다:
- PR 제목/본문에 이슈 연결 키워드 사용법
- 브랜치명과 이슈 번호 연결 방식
- 자동 이슈 종료 메커니즘 설명
- 이슈 추적성 향상을 위한 베스트 프랙티스

#### Claude Code 생성 문구 제거
문서화 작업 시 Claude Code가 자동으로 추가하는 다음 문구들을 항상 제거해야 합니다:
- `🤖 Generated with [Claude Code](https://claude.ai/code)`
- `Co-Authored-By: Claude <noreply@anthropic.com>`
- 기타 Claude 생성 관련 서명이나 워터마크

## 📝 Memory

### 프로젝트 메모리 관리
이 섹션은 Claude Code의 메모리 시스템을 활용하여 프로젝트 컨텍스트를 유지하고 개발 효율성을 높이기 위한 공간입니다.

#### 메모리 사용법
- **`#` 단축키**: 빠른 메모리 추가
- **`/memory` 명령어**: 기존 메모리 편집
- **`@path/to/import`**: 외부 메모리 파일 임포트

#### 외부 문서 참조 시스템
Claude Code는 `@` 문법을 통해 외부 파일의 내용을 메모리로 가져올 수 있습니다. 아래 가이드들은 각각 독립적인 문서로 관리되며, 필요시 해당 문법으로 참조하여 사용할 수 있습니다.

## 📋 기타 개발 가이드 및 규칙

### 🌳 브랜치 관리 가이드
@docs/branch-guide.md

### 📝 커밋 메시지 가이드  
@docs/commit-message-guide.md

### 🤖 MCP (Model Context Protocol) 가이드
@docs/mcp-guide.md
