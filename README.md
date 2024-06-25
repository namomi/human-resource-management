# 💻 인사 관리 프로젝트
직원 등록 및 출 퇴근, 팀 추가 연차나 초과 근무시간을 조회할 수 있는 API입니다.

# ⚙ 개발 환경
- Project : `Gradle - Groovy`
- Language : `Java`
- Spring Boot : `3.2.2`
- Packaging : `Jar`
- Java : `17`
- Dependencies : `Lombok`, `Spring Web`, `JPA`, `org.josn`, `Spring Validation`
- IDEA : `IntelliJ`
- DB : `MySQL`
- Cache: `Redis`

# 주요 요구 API 명세서
- [x] 1단계
<img width="704" alt="스크린샷 2024-06-25 오전 9 07 16" src="https://github.com/namomi/human-resource-management/assets/65004844/1ab03113-f3c9-45cd-8101-9a560a535510">

- [x] 2단계
<img width="706" alt="스크린샷 2024-06-25 오전 9 09 02" src="https://github.com/namomi/human-resource-management/assets/65004844/d6672441-6674-497f-92a4-3966a8d97d00">

- [x] 3단계
<img width="694" alt="스크린샷 2024-06-25 오전 9 11 56" src="https://github.com/namomi/human-resource-management/assets/65004844/84d2ba4b-753d-41e0-a102-7024ed654ab3">

- [x] 4단계
<img width="709" alt="스크린샷 2024-06-25 오전 9 12 51" src="https://github.com/namomi/human-resource-management/assets/65004844/14eb1311-4f42-4ab9-ad10-b07e5e8bcdf1">

# ⚙ 기술 스택
- Spring Boot
- Java
- JPA
- MySql
- Redis
- Docker
- logback
- swagger


# 🔎 주요 기능
- `POST / employee / add` : 외부 API에서 직원 정보를 DB에 저장
- `PUT / employee / /{employeeId} /team"` : 직원에 팀 추가
- `GET / employee` : 모든 직원 정보 조회
- `POST / team / add` : 외부 API에서 팀 정보를 DB에 저장
- `GET / team` : 모든 팀 정보 조회
- `POST / employee / attendance / {employeeId}` : 해당 직원을 출근
- `POST / employee / gotOffWork / {employeeId}` : 해당 직원을 퇴근
- `GET / employee / {employeeId} / {date}` : 해당 근무시간 조회
- `POST / employee / annual-leve/ {employeeId}` : 해당 직원 연차 신청
- `GET / employee / total-leaves/ {employeeId}` : 해당 직원 남은 연차 조회
- `GET / employee / overtime / {year} / {month}` : 해당 년 월의 모든 직원 초과근무 조회

# 🔎 추가 기능
- DB와 관련된 함수 트랙잭션 처리
- 매달 오전 1시에 CSV로 저장
- Logback을 이용한 로그 설정
- ExceptionHandler를 이용한 예외처리
- Swagger를 이용한 API documentation
