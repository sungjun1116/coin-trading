# 🌳 브랜치 명명 가이드

[AngularJS commit convention](https://gist.github.com/stephenparish/9941e89d80e2bc58a153)을 참고한 브랜치 명명 규칙:

## 브랜치 타입
- **`main`** - 메인 브랜치 (프로덕션 코드)
- **`develop`** - 개발 브랜치 (개발 통합)
- **`feature/`** - 새로운 기능 개발 (docs, test 도 포함)
- **`bugfix/`** - 버그 수정
- **`hotfix/`** - 긴급 수정 (프로덕션)
- **`release/`** - 배포 준비

## 브랜치 명명 규칙
```text
{타입}/#{이슈번호}-{간단한-설명}
```

**예외 정책:**
- **`hotfix/`** 브랜치의 경우 긴급 상황으로 인해 이슈 번호 없이 진행 가능
- 긴급 보안 패치, 서비스 중단 상황 등에서 이슈 생성 과정 생략 허용
- 단, hotfix 완료 후 반드시 사후 문서화 및 이슈 등록 필요

## 예시
- `feature/#123-add-binance-order-api`
- `bugfix/#789-fix-authentication-error`
- `hotfix/critical-security-patch` (이슈번호 생략 허용)
- `hotfix/#999-emergency-db-fix` (이슈가 있는 경우)
- `release/v1.2.0`

## 브랜치 작업 플로우

### 기능 개발 플로우
1. **브랜치 생성**: `develop` → `feature/#{이슈번호}-{설명}`
2. **개발 작업**: 기능 구현 및 테스트
3. **Pull Request**: `feature/#{이슈번호}-{설명}` → `develop`
4. **코드 리뷰**: 팀원 리뷰 후 승인
5. **병합 완료**: `develop`에 병합 후 feature 브랜치 삭제
6. **이슈 자동 종료**: PR 병합 시 연결된 이슈 자동 종료

### 버그 수정 플로우
1. **브랜치 생성**: `develop` → `bugfix/#{이슈번호}-{설명}`
2. **버그 수정**: 문제 해결 및 테스트
3. **Pull Request**: `bugfix/#{이슈번호}-{설명}` → `develop`
4. **병합 완료**: 리뷰 후 `develop`에 병합
5. **이슈 자동 종료**: PR 병합 시 연결된 이슈 자동 종료

### 배포 플로우
1. **배포 준비**: `develop` → `release/v{버전}`
2. **최종 테스트**: 배포 전 통합 테스트
3. **배포**: `release/v{버전}` → `main`
4. **백포트**: `main` → `develop` (배포 후 변경사항 반영)

### 긴급 수정 플로우
1. **긴급 브랜치**: `main` → `hotfix/{설명}`
2. **수정 작업**: 긴급 문제 해결
3. **즉시 배포**: `hotfix/{설명}` → `main`
4. **백포트**: `hotfix/{설명}` → `develop`

### PR-이슈 연결

GitHub에서 PR과 이슈를 연결하여 PR 병합 시 자동으로 이슈를 종료시킬 수 있습니다.

#### 자동 종료 키워드
PR 제목이나 본문에 다음 키워드를 사용하면 PR이 병합될 때 해당 이슈가 자동으로 종료됩니다:

- `Closes #이슈번호`
- `Fixes #이슈번호`
- `Resolves #이슈번호`

#### 여러 이슈 동시 종료
하나의 PR에서 여러 이슈를 동시에 종료할 수 있습니다:

```markdown
feat(coinone): add comprehensive order management system

주문 생성, 취소, 수정 기능을 포함한 통합 주문 관리 시스템을 구현합니다.
사용자 경험 개선과 시스템 안정성을 향상시킵니다.

Closes #123, #456, #789
Fixes #101
```

#### 사용 예시
```markdown
# PR 제목
fix(auth): resolve authentication timeout and token refresh issues

# PR 본문  
잘못된 타임스탬프 검증과 토큰 갱신 로직의 문제를 해결합니다.
- 로컬 시간 대신 서버 시간 사용
- 토큰 만료 시 자동 갱신 기능 추가
- 인증 실패 시 적절한 에러 메시지 표시

Closes #456
Fixes #789
Resolves #101
```