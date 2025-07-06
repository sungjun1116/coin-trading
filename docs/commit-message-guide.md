# 📝 커밋 메시지 규칙

[Git 커밋 메시지 컨벤션](https://gist.github.com/stephenparish/9941e89d80e2bc58a153)을 따르는 형식:

```text
<type>(<scope>): <subject>
<BLANK LINE>
<body>
<BLANK LINE>
<footer>
```

## 타입 (필수)
- `feat`: 새로운 기능 (feature)
- `fix`: 버그 수정 (bug fix)
- `docs`: 문서 변경 (documentation)
- `style`: 코드 포맷팅 (formatting, 세미콜론 누락 등)
- `refactor`: 코드 리팩토링
- `test`: 누락된 테스트 추가
- `chore`: 유지보수 작업

## 규칙
- 제목(subject)은 최대 50자
- 본문(body)과 푸터(footer)는 각 줄당 최대 72자
- 명령조, 현재시제 사용 ("change" not "changed")
- 첫 글자는 소문자
- 제목 끝에 마침표(.) 없음
- 범위(scope)는 선택사항
- 본문과 푸터는 선택사항

## 언어 규칙
- **제목(subject)**: 영어로 작성
- **본문(body)**: 한국어로 작성
- **푸터(footer)**: 영어로 작성 (GitHub 키워드 등)

## 범위(Scope)
- 변경된 위치나 특정 기능을 명시
- 예: `auth`, `coinone`, `binance`, `config`

## 본문(Body)
- 변경 동기 설명
- 이전 동작과의 차이점 설명

## 푸터(Footer)
- 중대한 변경사항 참조
- 관련 이슈 종료
- 예: `Closes #123, #245`

## 예시

```text
feat(coinone): add order cancellation API

활성 주문에 대한 새로운 취소 기능을 구현합니다.
즉시 주문 취소 기능과 적절한 오류 처리를 제공합니다.

Closes #123
```

```text
fix(auth): resolve authentication timeout issue

잘못된 타임스탬프 검증으로 인해 인증이 실패하는 문제를 해결합니다.
로컬 시간 대신 서버 시간을 사용하도록 타임스탬프 생성을 조정했습니다.

Fixes #456
```

```text
docs(readme): update installation instructions

새로운 Java 21 요구사항을 추가하고 설정 단계를 업데이트합니다.

BREAKING CHANGE: Java 17 is no longer supported, minimum version is Java 21
```