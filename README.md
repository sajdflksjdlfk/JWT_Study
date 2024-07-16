### 뉴스 수집 및 요약 시스템 기능 정의서

---

#### 프로젝트 개요
**프로젝트 이름:** 뉴스 수집 및 요약 시스템  
**목표:** 사용자는 관심 주제를 설정하고, 시스템은 주기적으로 뉴스를 수집하여 OpenAI API를 사용해 간단하게 요약된 내용을 제공합니다. JWT를 사용하여 사용자 인증 및 인가를 처리합니다.

---

#### 주요 기능
1. **사용자 인증 및 인가**
   - **회원가입**
   - **로그인**
   - **로그아웃**
   - **JWT 발급 및 검증**

2. **뉴스 관리**
   - **뉴스 수집**
     - 외부 뉴스 API를 사용하여 주기적으로 뉴스 수집
   - **뉴스 CRUD**
     - 뉴스 목록 조회
     - 뉴스 추가
     - 뉴스 수정
     - 뉴스 삭제

3. **뉴스 요약**
   - **뉴스 요약 생성**
     - 수집된 뉴스를 OpenAI API를 사용하여 요약
   - **요약된 뉴스 조회**
     - 사용자에게 요약된 뉴스 제공

---

#### 상세 기능 정의

##### 1. 사용자 인증 및 인가

**회원가입**
- **설명:** 사용자가 계정을 생성하는 기능.
- **입력:** 사용자명, 이메일, 비밀번호
- **처리:** 사용자 정보를 데이터베이스에 저장하고 JWT 발급
- **출력:** 성공 메시지, JWT 토큰

**로그인**
- **설명:** 사용자가 시스템에 로그인하는 기능.
- **입력:** 이메일, 비밀번호
- **처리:** 사용자 인증 후 JWT 발급
- **출력:** 성공 메시지, JWT 토큰

**로그아웃**
- **설명:** 사용자가 시스템에서 로그아웃하는 기능.
- **입력:** JWT 토큰
- **처리:** 서버 측에서 토큰 무효화
- **출력:** 성공 메시지

**JWT 발급 및 검증**
- **설명:** JWT를 발급하고 유효성을 검증하는 기능.
- **입력:** 사용자 인증 정보
- **처리:** JWT 생성 및 검증 로직
- **출력:** JWT 토큰, 인증 결과

##### 2. 뉴스 관리

**뉴스 수집**
- **설명:** 외부 뉴스 API를 사용하여 주기적으로 뉴스를 수집하는 기능.
- **입력:** 사용자 관심 주제
- **처리:** 외부 API를 호출하여 뉴스 데이터 수집 후 데이터베이스에 저장
- **출력:** 수집된 뉴스 데이터

**뉴스 목록 조회**
- **설명:** 사용자가 수집된 뉴스 목록을 조회하는 기능.
- **입력:** 페이지 번호, 검색어 (옵션)
- **처리:** 데이터베이스에서 뉴스 데이터를 조회
- **출력:** 뉴스 목록

**뉴스 추가**
- **설명:** 사용자가 뉴스를 추가하는 기능.
- **입력:** 뉴스 제목, 내용, 링크 등
- **처리:** 입력된 뉴스 데이터를 데이터베이스에 저장
- **출력:** 성공 메시지

**뉴스 수정**
- **설명:** 사용자가 뉴스를 수정하는 기능.
- **입력:** 뉴스 ID, 수정할 제목, 내용 등
- **처리:** 해당 뉴스 데이터를 데이터베이스에서 수정
- **출력:** 성공 메시지

**뉴스 삭제**
- **설명:** 사용자가 뉴스를 삭제하는 기능.
- **입력:** 뉴스 ID
- **처리:** 해당 뉴스 데이터를 데이터베이스에서 삭제
- **출력:** 성공 메시지

##### 3. 뉴스 요약

**뉴스 요약 생성**
- **설명:** 수집된 뉴스를 OpenAI API를 사용하여 요약하는 기능.
- **입력:** 뉴스 내용
- **처리:** OpenAI API를 호출하여 요약된 내용 생성 후 데이터베이스에 저장
- **출력:** 요약된 뉴스 내용

**요약된 뉴스 조회**
- **설명:** 사용자가 요약된 뉴스 목록을 조회하는 기능.
- **입력:** 페이지 번호, 검색어 (옵션)
- **처리:** 데이터베이스에서 요약된 뉴스 데이터를 조회
- **출력:** 요약된 뉴스 목록

---