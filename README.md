# README.md

# 🏬 쇼핑몰 · 창고 통합 관리 시스템 (Java 21 / Swing 기반)

> 구매자, 운영자, 창고 관리자까지 전체 유통 흐름을 하나의 데스크탑 애플리케이션으로 통합한 프로젝트
> 

---

## 📌 프로젝트 개요

- **기획 의도**: 상품 주문부터 물류 처리까지 연동되는 구조를 직접 설계하고 Swing UI로 구현
- **기술 스택**: Java 21, Swing, MySQL, JDBC, Eclipse STS, Maven
- **프로젝트 구성**: 단일 Maven 프로젝트 내 역할별 패키지 분리
    - 쇼핑몰 사용자용 (SHOP)
    - 쇼핑몰 관리자용 (SHOP)
    - 창고 관리자용 (WMS)

---

## 👤 팀 구성 및 역할 분담

| 이름 | 담당 업무 |
| --- | --- |
| **구지훈** | 쇼핑몰 상품관리, 발주 기록, 사용자 상품 목록 구현, 쇼핑몰 DB 설계 |
| **박혜원** | 전체 UI 공통 구조 설계, 마이페이지, 회원관리, 주문관리, 쇼핑몰 메인 화면 구현 |
| **신주원** | 창고 조회 그래프 시각화, 실시간 모니터링, 입고 자동화 알고리즘 구현, 창고 DB 설계 |
| **정재환** | 창고 입출고 기능 전반, 재고관리 구현, WMS UI 공통 디자인 |
| **조완** | 창고 로그인 및 계정관리 구현 |

---

## 🧰 개발 환경 및 실행 방법

- Java 21
- Eclipse STS 또는 IntelliJ (UTF-8, Maven 지원 IDE)
- MySQL 8.x

### 실행 방법

1. `MySQL` 서버 실행 및 `shopping_wms` DB 생성
2. `\src\main\java\com\eoneifour\common\config\PrivateConfig.java`에 DB 정보 입력
3. `com.eoneifour.MainApp` 또는 `shop.view.ShopMainFrame` 실행

---

## 📂 디렉토리 구조

```
📦 src/main/java/com.eoneifour
├── common                      # 공통 설정/유틸/로그인/회원가입
│   ├── config/                 # DB 연결 정보
│   ├── exception/              # 예외 정의
│   ├── frame/                  # 기본 Swing 프레임
│   ├── repository/             # 로그인 DAO
│   ├── util/                   # 유틸 클래스 모음
│   └── view/                   # 로그인 / 회원가입 뷰
│
├── shop                        # 쇼핑몰 사용자 영역
│   ├── common.config/          # 사용자 전용 설정
│   ├── mypage.view/            # 마이페이지 화면
│   ├── product/                # 상품 목록 / 상세
│   ├── view/
│   │   ├── MypageMenuPanel.java
│   │   ├── ProductMenuPanel.java
│   │   └── ShopMainFrame.java   # 사용자 진입점
│
├── shopadmin                   # 쇼핑몰 운영자(관리자)
│   ├── order/                  # 주문 목록, 배송상태 처리
│   ├── product/                # 상품 등록, 수정, 상태변경
│   ├── purchaseOrder/          # 발주 목록
│   ├── user/                   # 회원 목록, 수정, 삭제
│   └── view/
│       └── ShopAdminMainFrame.java   # 관리자 진입점
│
└── wms                         # 창고 관리자 (WMS)
    ├── auth/                   # 로그인
    ├── common.config/          # 창고용 설정
    ├── home.view/              # WMS 메인 프레임
    ├── inbound/                # 입고 처리
    ├── inboundrate/            # 입고율 계산
    ├── iobound/                # 입출고 처리
    ├── iohistory/              # 입출고 기록 조회
    ├── monitoring/             # 도식화/그래프 기반 실시간 창고 상태
    ├── outbound.view/          # 출고 관련 UI
    └── wmsApp.java             # 창고 관리자 실행 진입점

```

---

## ⚙️ 주요 기능별 요약

### ✅ 사용자 (쇼핑몰)

- 이메일 기반 회원가입 / 로그인 / 정보수정 / 탈퇴
- 상품 목록 카테고리별 조회 (썸네일, 가격 포함)
- 상품 상세 조회 → 수량 선택 후 바로 주문
- 마이페이지: 주문 내역, 회원정보 확인 및 수정, 회원 탈퇴
- 주문 취소는 주문확인 중 까지만 가능

### 🛠 운영자 (관리자)

- 회원 목록 조회 / 검색 / 등록 / 수정 / 삭제
- 상품 목록 조회 / 검색 / 등록 / 수정 / 삭제(비활성화)
- 주문 목록 조회 / 검색 / 수정 / 주문 상태 변경
- 발주 요청 (수동 발주 방식, 수량 조건 없음)

### 🏭 창고 관리자 (WMS)

- 입고 지시: 위치 자동 지정 및 실시간 반영
- 출고 지시: FIFO 기준 자동 출고
- 입출고 기록 조회: 날짜/상품별 필터
- 실시간 모니터링 (도식화 + 애니메이션)
- 입고율 통계 (전체 및 스태커별 그래프 제공)
- DB ↔ UI 상태 동기화 버튼 지원

---

## 💻 ERD

![Image](https://github.com/user-attachments/assets/7dbce6d0-0855-4907-92ec-f21b4f61e366)


---

## 🖥️ 실행 화면 캡쳐

### 창고 WMS 메인화면

![Image](https://github.com/user-attachments/assets/bef7cf8f-751e-4fbf-9440-72fa53a35a6c)

### 창고 입고율 조회

![Image](https://github.com/user-attachments/assets/fcae537b-2cbd-4a24-acd6-ddfb096834c2)

### 창고 도식화 (모니터링)

![Image](https://github.com/user-attachments/assets/eb6f84fd-d2ae-4ef2-92e3-800d721b2c43)

### 관리자 회원관리

![Image](https://github.com/user-attachments/assets/89c69599-c3e1-424e-9cc0-1ff44f3a4e07)

### 사용자 상품 메인

![Image](https://github.com/user-attachments/assets/f11cdc09-1814-488a-ab0a-5e44bb7656fd)

### 마이페이지 - 회원 정보

![Image](https://github.com/user-attachments/assets/5966a329-609f-475c-96e2-a089a155c708)

> 위 캡처는 실행 프로그램 내 Swing 기반 UI입니다.
> 

---

## 🔗기타 참고 링크
