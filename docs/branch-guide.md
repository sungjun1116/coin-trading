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