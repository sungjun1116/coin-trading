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

```
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

## 🌳 브랜치 명명 가이드

[AngularJS commit convention](https://gist.github.com/stephenparish/9941e89d80e2bc58a153)을 참고한 브랜치 명명 규칙:

### 브랜치 타입
- **`main`** - 메인 브랜치 (프로덕션 코드)
- **`develop`** - 개발 브랜치 (개발 통합)
- **`feature/`** - 새로운 기능 개발 (docs, test 도 포함)
- **`bugfix/`** - 버그 수정
- **`hotfix/`** - 긴급 수정 (프로덕션)
- **`release/`** - 배포 준비

### 브랜치 명명 규칙
```
{타입}/#{이슈번호}-{간단한-설명}
```

### 예시
- `feature/#123-add-binance-order-api`
- `bugfix/#789-fix-authentication-error`
- `hotfix/critical-security-patch`
- `release/v1.2.0`

### 브랜치 작업 플로우

#### 기능 개발 플로우
1. **브랜치 생성**: `develop` → `feature/#{이슈번호}-{설명}`
2. **개발 작업**: 기능 구현 및 테스트
3. **Pull Request**: `feature/#{이슈번호}-{설명}` → `develop`
4. **코드 리뷰**: 팀원 리뷰 후 승인
5. **병합 완료**: `develop`에 병합 후 feature 브랜치 삭제

#### 버그 수정 플로우
1. **브랜치 생성**: `develop` → `bugfix/#{이슈번호}-{설명}`
2. **버그 수정**: 문제 해결 및 테스트
3. **Pull Request**: `bugfix/#{이슈번호}-{설명}` → `develop`
4. **병합 완료**: 리뷰 후 `develop`에 병합

#### 배포 플로우
1. **배포 준비**: `develop` → `release/v{버전}`
2. **최종 테스트**: 배포 전 통합 테스트
3. **배포**: `release/v{버전}` → `main`
4. **백포트**: `main` → `develop` (배포 후 변경사항 반영)

#### 긴급 수정 플로우
1. **긴급 브랜치**: `main` → `hotfix/{설명}`
2. **수정 작업**: 긴급 문제 해결
3. **즉시 배포**: `hotfix/{설명}` → `main`
4. **백포트**: `hotfix/{설명}` → `develop`

### 커밋 메시지 규칙

[Git 커밋 메시지 컨벤션](https://gist.github.com/stephenparish/9941e89d80e2bc58a153)을 따르는 형식:

```
<type>(<scope>): <subject>
<BLANK LINE>
<body>
<BLANK LINE>
<footer>
```

**타입 (필수):**
- `feat`: 새로운 기능 (feature)
- `fix`: 버그 수정 (bug fix)
- `docs`: 문서 변경 (documentation)
- `style`: 코드 포맷팅 (formatting, 세미콜론 누락 등)
- `refactor`: 코드 리팩토링
- `test`: 누락된 테스트 추가
- `chore`: 유지보수 작업

**규칙:**
- 각 줄은 최대 100자
- 명령조, 현재시제 사용 ("change" not "changed")
- 첫 글자는 소문자
- 제목 끝에 마침표(.) 없음
- 범위(scope)는 선택사항
- 본문과 푸터는 선택사항

**범위(Scope):**
- 변경된 위치나 특정 기능을 명시
- 예: `auth`, `coinone`, `binance`, `config`

**본문(Body):**
- 변경 동기 설명
- 이전 동작과의 차이점 설명

**푸터(Footer):**
- 중대한 변경사항 참조
- 관련 이슈 종료
- 예: `Closes #123, #245`

**예시:**
```
feat(coinone): add order cancellation API

Implement new cancellation functionality for active orders.
Provides immediate order cancellation with proper error handling.

Closes #123
```

```
fix(auth): resolve authentication timeout issue

The authentication was failing due to incorrect timestamp validation.
Adjusted timestamp generation to use server time instead of local time.

Fixes #456
```

```
docs(readme): update installation instructions

Add new Java 21 requirement and update setup steps.

BREAKING CHANGE: Java 17 is no longer supported, minimum version is Java 21
```

## 🤖 MCP (Model Context Protocol) 가이드

이 프로젝트에서 Claude Code와 효율적으로 작업하기 위한 MCP 프로토콜 가이드입니다.

### GitHub MCP 활용

#### 이슈 관리
- **이슈 생성**: 새로운 기능 요청, 버그 리포트, 개선 사항 등을 체계적으로 관리
- **이슈 검색**: 기존 이슈를 빠르게 찾아 중복 방지
- **이슈 업데이트**: 진행 상황 업데이트, 라벨 추가, 담당자 지정 등

### 라벨 시스템
- **`enhancement`**: 새로운 기능 또는 개선사항
- **`bug`**: 버그 수정
- **`refactoring`**: 코드 리팩토링
- **`documentation`**: 문서 관련 작업

#### 이슈 제목 작성 가이드

GitHub 이슈의 제목은 간결하면서도 명확하게 작성하여 이슈의 목적과 내용을 쉽게 파악할 수 있도록 해야 합니다.

**제목 작성 규칙:**
- **타입 접두사**: `[타입]` 형태로 이슈 종류를 명시
- **간결성**: 50자 이내로 제한
- **명확성**: 구체적인 작업 내용 포함
- **현재시제**: 동사 원형 사용

**타입별 제목 예시:**

**기능 개발 (Feature):**
- `[FEAT] Binance 선물 거래 API 연동`

**버그 수정 (Bug):**
- `[BUG] Coinone 인증 토큰 만료 처리 오류`

**리팩토링 (Refactoring):**
- `[REFACTOR] 계층형 아키텍처 구조 개선`

**문서화 (Documentation):**
- `[DOCS] API 클라이언트 사용법 문서 추가`

**테스트 (Test):**
- `[TEST] 거래소 API 통합 테스트 추가`

**기타 (Chore):**
- `[CHORE] 의존성 업그레이드 (Spring Boot 3.2)`

#### 효과적인 이슈 생성 가이드

**1. 기능 개발(enhancement) 이슈 작성법**
```markdown
## 📋 개요
새로운 기능에 대한 간단한 설명

## 🎯 목적
이 기능이 왜 필요한지, 어떤 문제를 해결하는지 설명

## 📝 요구사항
### 기능적 요구사항
- 요구사항 1
- 요구사항 2

### 비기능적 요구사항
- 성능 요구사항
- 보안 요구사항

## 🔧 구현 방안
기술적 접근 방법 및 구현 계획

## 🧪 테스트 계획
테스트 시나리오 및 검증 방법

## 📚 참고사항
관련 문서, 레퍼런스 등
```

**2. 버그 수정(bug) 이슈 작성법**
```markdown
## 📋 버그 요약
발생한 버그에 대한 간단한 설명

## 🐛 현상
### 예상 동작
정상적으로 동작해야 하는 방식 설명

### 실제 동작
현재 발생하고 있는 문제 상황 설명

### 재현 단계
1. 단계 1
2. 단계 2
3. 단계 3

## 🔍 원인 분석
버그 발생 원인에 대한 분석 (가능한 범위에서)

## 🔧 해결 방안
버그 수정을 위한 구체적인 방법

## 🧪 테스트 계획
- [ ] 버그 수정 검증
- [ ] 회귀 테스트
- [ ] 관련 기능 영향도 확인

## 📊 영향도
- **심각도**: High/Medium/Low
- **영향 범위**: 관련 기능 및 사용자 범위
```

**3. 리팩토링(refactoring) 이슈 작성법**
```markdown
## 📋 요약
프로젝트의 현재 상태와 목표하는 리팩토링 내용을 명확히 기술

## 🎯 목표
- [ ] 구체적인 개선 목표 1
- [ ] 구체적인 개선 목표 2
- [ ] 구체적인 개선 목표 3

## 🔧 변경사항
### Before/After 구조 비교
기존 구조와 새로운 구조를 시각적으로 비교

## 🚀 구현 작업
- [ ] 구체적인 작업 항목 1
- [ ] 구체적인 작업 항목 2
- [ ] 구체적인 작업 항목 3

## 🧪 테스트 계획
- [ ] 테스트 항목 1
- [ ] 테스트 항목 2

## 📈 개선 효과
리팩토링으로 얻을 수 있는 구체적인 이점들 설명
```

**4. 문서화(documentation) 이슈 작성법**
```markdown
## 📋 요약
문서화 작업의 범위와 목표를 명확히 기술

## 🎯 목표
- [ ] 구체적인 문서화 목표 1
- [ ] 구체적인 문서화 목표 2
- [ ] 구체적인 문서화 목표 3

## 🔧 변경사항
### 새로 추가된 파일
- **`파일명`** - 파일 설명

### 수정된 파일
- **`파일명`** - 수정 내용 설명

## 🚀 구현 작업
- [ ] 구체적인 작업 항목 1
- [ ] 구체적인 작업 항목 2
- [ ] 구체적인 작업 항목 3

## 📈 개선 효과
문서화로 얻을 수 있는 구체적인 이점들 설명
```

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

