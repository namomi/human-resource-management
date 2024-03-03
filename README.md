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
