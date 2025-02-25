# OrderLink 🛒

**온라인·오프라인 주문을 지원하는 AI 기반 주문 관리 플랫폼**

## 📜 프로젝트 개요
OrderLink는 온라인·오프라인 주문을 관리하고 AI를 활용한 음식 설명 추천 기능을 제공하는 플랫폼입니다. 사용자는 주문을 생성하고 결제할 수 있으며, 점주는 AI를 활용하여 음식 설명을 등록할 수 있습니다.

---

## 📌 주요 기능

### ✅ 사용자 & 인증
- Spring Security + JWT를 활용한 인증·인가 시스템 적용
- 사용자 역할 기반 접근 제어 (CUSTOMER, OWNER, MASTER)
- 로그인 및 인증된 사용자만 특정 기능 사용 가능

### ✅ 주문 및 결제
- 주문 생성 및 주문 상품 등록, 결제 내역 생성
- 주문 취소는 **5분 내 가능**
- QueryDSL을 활용한 주문 검색 기능
  - 주문 상태, 음식점 이름, 음식 이름, 기간 조건을 중복 선택하여 검색 가능
- N+1 문제 해결을 위해 Fetch Join 사용
- 결제 시 **카드 번호 마스킹 처리** (마지막 4자리 제외)

### ✅ 리뷰
- 주문에 대한 리뷰가 이미 존재하면 예외 발생
- 리뷰는 해당 주문을 한 음식점에 대해서 리뷰를 작성
- 주문의 userId와 로그인한 사용자가 동일한 경우에만 작성 가능. 그렇지 않으면 해당 리뷰에 대한 권한이 없다는 예외 발생
- 리뷰 등록/수정/삭제시마다 음식점 테이블의 중간 집계 필드들(avgRating, ratingSum, ratingCount) 업데이트
- 리뷰 목록 조회시에는 요약본이 보여지게 되며, 리뷰 내용이 50자가 넘는 경우에는 "..." 으로 뒷 부분이 잘리게 표현
- 리뷰 목록 조회에서는 간략한 정보를 제공하고, 상세 조회에서는 주문 관련 정보까지 자세한 정보를 추가로 제공

### ✅ 운영 지역 관리
- 트리 구조의 **계층형 지역 데이터 관리**
- `parentId`를 활용하여 상위 지역과 하위 지역을 연결
- 재귀 쿼리를 이용한 지역 조회 기능 지원

### ✅ 음식 및 음식점 관리
- 음식 등록 시 **AWS S3**를 활용한 음식 사진 업로드
- 신규 음식점 등록 시 **MASTER가 직접 승인 및 등록**
- OWNER 및 MASTER만 특정 음식점 정보 수정 가능

### ✅ AI 음식 설명 추천 (Gemini 연동)
- **Gemini API**를 이용한 음식 설명 자동 생성 기능
- 설명은 50자 이하로 제한, 입력 텍스트는 100자 제한
- 요청 및 응답 데이터를 DB에 저장하여 추후 분석 가능

### ✅ 카테고리 관리
- N:M 확장성을 고려한 **중간 테이블**을 활용하여 관리

### ✅ 배송지 관리
- 고객(CUSTOMER)이 **다수의 배송지 등록 가능**
- **최대 1개의 기본 배송지 설정 가능**
- 본인 외에는 배송지 접근 제한

---

## 📌 시스템 아키텍처

![image](https://github.com/user-attachments/assets/5e91f188-7927-4c4b-a095-6f82c7c86668)



---

## 📌 데이터 모델 (ERD)

![image](https://github.com/user-attachments/assets/1668b06b-2d6f-434a-baf3-f9146774b3ff)

---

## 📌 기술 스택

| 기술  | 설명 |
|-------|------|
| **Backend** | Spring Boot 3.4.2, Java 17 |
| **Database** | PostgreSQL, H2 (Test) |
| **Security** | Spring Security, JWT |
| **API 개발** | QueryDSL, WebClient |
| **AI** | Gemini API (음식 설명 추천) |
| **빌드 도구** | Gradle |
| **배포 환경** | AWS EC2, RDS, S3 |

---

## 📌 팀원 역할

| 이름 | 역할 | 주요 담당 |
|------|------|----------|
| **김규현** | 팀장 | 스크럼 진행, 음식점 도메인 API 개발 및 리팩토링, 코드 리뷰 |
| **백지환** | 테크리더 | 프로젝트 디렉토리 기초세팅, 감사로그를 위한 Entity 세팅, SpringSecurity, JWT를 활용한 인증/인가, 사용자·리뷰·배송지·GEMINI 연동을 통한 AI 음식 설명 추천 API 개발, Git-flow 브랜치 전략 관리, 코드리뷰 |
| **박경린** | 백엔드 개발 | API 응답 및 예외처리 설정, 주문·주문상품·결제·카테고리·운영지역·음식 이미지 API 개발, AWS 인프라 설정(EC2, RDS, S3) 및 배포, 코드 리뷰 |
| **나현희** | 백엔드 개발 | 음식 API 개발 및 리팩토링, 코드 리뷰 |


---


## 📌 프로젝트 구조 - Project structure

```
📦main
 ┣ 📂java
 ┃ ┗ 📂com
 ┃ ┃ ┗ 📂order
 ┃ ┃ ┃ ┗ 📂orderlink
 ┃ ┃ ┃ ┃ ┣ 📂address
 ┃ ┃ ┃ ┃ ┃ ┣ 📂application
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dtos
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AddressRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜AddressResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜AddressService.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜AddressRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜Address.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📂presentation
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜AddressController.java
 ┃ ┃ ┃ ┃ ┣ 📂ai
 ┃ ┃ ┃ ┃ ┃ ┣ 📂application
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dtos
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AiApiRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜AiApiResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜AiApiService.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜AiApiLogRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜AiApiLog.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📂presentation
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜AiApiController.java
 ┃ ┃ ┃ ┃ ┣ 📂category
 ┃ ┃ ┃ ┃ ┃ ┣ 📂application
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dtos
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CategoryDTO.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CategoryRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜CategoryResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜CategoryService.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CategoryRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜RestaurantCategoryRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜Category.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜RestaurantCategory.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📂presentation
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜CategoryController.java
 ┃ ┃ ┃ ┃ ┣ 📂common
 ┃ ┃ ┃ ┃ ┃ ┣ 📂auth
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂controller
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ServiceAccountTokenController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂filter
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜JwtAuthenticationFilter.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜JwtAuthorizationFilter.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂service
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ServiceAccountTokenService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂util
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜JwtUtil.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜LocalTimeToStringConverter.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ServiceAccountUserDetails.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜UserDetailsImpl.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UserDetailsServiceImpl.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂client
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜FoodClient.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜OrderClient.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜RestaurantClient.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UserClient.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂config
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AiWebClientConfig.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AuditConfig.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AuditorAwareImpl.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜QueryDslConfig.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜S3Config.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜SecurityConfig.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜WebClientConfig.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂dtos
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AuthRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AuthResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ErrorNonDataResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ErrorResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜FoodRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜SuccessNonDataResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜SuccessResponse.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂entity
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜BaseTimeEntity.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂enums
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ErrorCode.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜SuccessCode.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂exception
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AddressException.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AiApiException.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AuthException.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜BaseException.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CategoryException.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜FoodException.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜GlobalExceptionHandler.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜OrderException.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜PaymentException.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜RegionException.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜RestaurantException.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ReviewException.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UserException.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📂external
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂s3
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜S3Service.java
 ┃ ┃ ┃ ┃ ┣ 📂food
 ┃ ┃ ┃ ┃ ┃ ┣ 📂application
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dtos
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜FoodDTO.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜FoodRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜FoodResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜FoodService.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜FoodRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜FoodSearchRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜Food.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂infrastructure
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜FoodSearchRepositoryImpl.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📂presentation
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜FoodController.java
 ┃ ┃ ┃ ┃ ┣ 📂order
 ┃ ┃ ┃ ┃ ┃ ┣ 📂application
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dtos
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜OrderDTO.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜OrderFoodDTO.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜OrderRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜OrderResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜RestaurantOrderDTO.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜OrderService.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜OrderRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜OrderRepositoryCustom.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜OrderRepositoryImpl.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜Order.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜OrderStatus.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜OrderType.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📂presentation
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜OrderController.java
 ┃ ┃ ┃ ┃ ┣ 📂orderitem
 ┃ ┃ ┃ ┃ ┃ ┣ 📂application
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜OrderItemService.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜OrderItemRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜OrderItem.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📂presentation
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜OrderController.java
 ┃ ┃ ┃ ┃ ┣ 📂payment
 ┃ ┃ ┃ ┃ ┃ ┣ 📂application
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dtos
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜PaymentRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜PaymentResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜PaymentService.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜PaymentRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜Payment.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜PaymentStatus.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📂presentation
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜PaymentController.java
 ┃ ┃ ┃ ┃ ┣ 📂region
 ┃ ┃ ┃ ┃ ┃ ┣ 📂application
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dtos
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜RegionDTO.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜RegionRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜RegionResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜RegionService.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜RegionRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜Region.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📂presentation
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜RegionController.java
 ┃ ┃ ┃ ┃ ┣ 📂restaurant
 ┃ ┃ ┃ ┃ ┃ ┣ 📂application
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dtos
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜RestaurantRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜RestaurantResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜RestaurantService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜TokenGenerator.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜RestaurantRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜Restaurant.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📂presentation
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜RestaurantController.java
 ┃ ┃ ┃ ┃ ┣ 📂review
 ┃ ┃ ┃ ┃ ┃ ┣ 📂application
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dtos
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ReviewRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ReviewResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ReviewService.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ReviewRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜Review.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📂presentation
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ReviewController.java
 ┃ ┃ ┃ ┃ ┣ 📂user
 ┃ ┃ ┃ ┃ ┃ ┣ 📂application
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dtos
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜UserRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UserResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UserService.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UserRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜User.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UserRoleEnum.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📂presentation
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UserController.java
 ┃ ┃ ┃ ┃ ┗ 📜OrderLinkApplication.java
 ┗ 📂resources
 ┃ ┣ 📂static
 ┃ ┣ 📂templates
 ┃ ┗ 📜application.properties
```

---


## 📌 서비스 구성 - User Flow Diagram

![image](https://github.com/user-attachments/assets/5b382449-0339-4201-9dda-8734f7d4320f)


---

## 📌 서버 실행 방법

```bash
cd /build/libs
nohup java -jar -Dspring.profiles.active=dev OrderLink-0.0.1-SNAPSHOT.jar > output.log 2>&1 &
```

---

## 📌 API 명세서
[📄 API 문서 확인하기](https://cliff-legal-971.notion.site/198f620e914280559c4cf437adf8a4c2?v=f00a6b494ccc45bc8ea0ba4148f2d60b)

---

## 📌 Git 브랜치 전략

- `main`: 배포용 브랜치
- `develop`: 개발 브랜치
- `feat/xxx`: 새로운 기능 개발 브랜치
- `fix/xxx`: 버그 수정 브랜치
- `hotfix/xxx`: 긴급 수정 브랜치
- `test/xxx`: 테스트용 브랜치
- `refactor/xxx`: 리팩터링용 브랜치

---

## 📌 컨벤션

### ✅ 코드 스타일
- Java 코드 스타일: **Naver Java 코딩 컨벤션**

### ✅ 커밋 메시지 규칙

| 태그 | 설명 |
|------|------|
| `feat` | 새로운 기능 추가 |
| `fix` | 버그 수정 |
| `refactor` | 코드 리팩토링 |
| `docs` | 문서 수정 |
| `test` | 테스트 코드 추가/수정 |
| `chore` | 기타 변경 사항, 기초 세팅 |

#### 📌 예시
```
feat(Order): 주문 생성 API 추가
```

---

## 📌 기타
문의 사항이나 버그 리포트는 **GitHub Issues** 또는 팀원에게 문의해 주세요.

