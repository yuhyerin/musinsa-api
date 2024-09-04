# MUSINSA Java Backend Engineer 과제
유혜린

## 구현 범위
### 기술 스택
Java 17, Spring Boot, H2 Database, Spring Data JPA, QueryDSL, Caffeine Cache, JUnit5, Thymeleaf

### Layer & 패키지 구조
크게 3개의 Layer 로 구분하여 개발했습니다.
- Contoller Layer : 어드민, 사용자 API, 화면용 뷰 컨트롤러를 분리했습니다.
- Service : 브랜드, 카테고리, 상품 서비스를 분리했습니다.
- Repository : 엔티티 Repository 와 QueryDSL 을 위한 CustomRepository 를 분리했습니다.
```
src
├── README.md
├── main
│   ├── java
│   │   └── com
│   │       └── musinsa
│   │           └── style
│   │               ├── MusinsaApiApplication.java
│   │               ├── config : 설정 파일
│   │               │   ├── CacheConfig.java
│   │               │   ├── QueryDSLConfig.java
│   │               │   └── WebConfig.java
│   │               ├── controller : 컨트롤러 Layer
│   │               │   ├── ProductController.java : 사용자 용 상품 정보 조회 API
│   │               │   ├── admin
│   │               │   │   └── AdminController.java : 어드민 용 브랜드, 상품 관리 API
│   │               │   ├── common
│   │               │   │   └── MusinsaResponse.java : 공통 응답
│   │               │   ├── dto : 컨트롤러 Layer 에서 사용하는 Request, Response DTO
│   │               │   │   ├── brand
│   │               │   │   │   ├── BrandCategoryItemDto.java
│   │               │   │   │   ├── BrandCreateRequest.java
│   │               │   │   │   ├── BrandDtoResponse.java
│   │               │   │   │   ├── BrandMinPriceDto.java
│   │               │   │   │   └── BrandMinTotalPriceResponse.java
│   │               │   │   ├── category
│   │               │   │   │   ├── CategoriesMinPriceResponse.java
│   │               │   │   │   ├── CategoryItemDto.java
│   │               │   │   │   └── CategoryMinPriceDto.java
│   │               │   │   ├── minmax
│   │               │   │   │   ├── CategoryMinMaxPrice.java
│   │               │   │   │   └── CategoryMinMaxPriceResponse.java
│   │               │   │   └── product
│   │               │   │       ├── ProductCreateRequest.java
│   │               │   │       ├── ProductDeleteRequest.java
│   │               │   │       ├── ProductDeleteResponse.java
│   │               │   │       └── ProductDtoResponse.java
│   │               │   └── view : 화면 렌더링 용 View 컨트롤러
│   │               │       └── ViewController.java
│   │               ├── enums
│   │               │   └── ResponseCode.java : 공통 응답 코드 정의
│   │               ├── exception
│   │               │   ├── BaseException.java
│   │               │   ├── BrandDeleteConflictException.java
│   │               │   ├── BrandNotFoundException.java
│   │               │   ├── CategoryNotFoundException.java
│   │               │   ├── MusinsaException.java
│   │               │   ├── ProductNotFoundException.java
│   │               │   └── advice
│   │               │       └── MusinsaControllerAdvice.java : 공통 예외 처리
│   │               ├── repository : 레포지토리 Layer
│   │               │   ├── BrandCustomRepository.java
│   │               │   ├── BrandCustomRepositoryImpl.java
│   │               │   ├── BrandRepository.java
│   │               │   ├── CategoryRepository.java
│   │               │   ├── ProductCustomRepository.java
│   │               │   ├── ProductCustomRepositoryImpl.java
│   │               │   ├── ProductRepository.java
│   │               │   └── entity : 엔티티 클래스
│   │               │       ├── Brand.java
│   │               │       ├── Category.java
│   │               │       └── Product.java
│   │               └── service : 서비스 Layer
│   │                   ├── BrandService.java
│   │                   ├── CategoryService.java
│   │                   └── ProductService.java
│   └── resources
│       ├── application.yml
│       ├── data.sql : 초기 데이터
│       └── templates : html 화면
│           ├── admin
│           │   ├── admin.html
│           │   ├── brands.html
│           │   └── products.html
│           └── main.html
└── test
    └── java
        └── com
            └── musinsa
                └── style
                    ├── MusinsaApiApplicationTests.java
                    ├── controller
                    │   └── ProductControllerTest.java
                    └── service
                        ├── BrandServiceTest.java
                        └── ProductServiceTest.java

```



### DB
DB 웹 콘솔 접근 : http://localhost:8888/h2

### API
#### 사용자
1. 카테고리 별 최저가 브랜드, 상품 가격, 총액 조회 API
- API URL : http://localhost:8888/v1/products/categories/min-prices
- json Response
  ```
  {
       "data": {
           "minPrice": {
               "categories": [
                   {
                       "categoryName": "상의",
                       "brandName": "C",
                       "price": 10000
                   },
                   {
                       "categoryName": "아우터",
                       "brandName": "E",
                       "price": 5000
                   },
                   {
                       "categoryName": "바지",
                       "brandName": "D",
                       "price": 3000
                   },
                   {
                       "categoryName": "스니커즈",
                       "brandName": "G",
                       "price": 9000
                   },
                   {
                       "categoryName": "가방",
                       "brandName": "A",
                       "price": 2000
                   },
                   {
                       "categoryName": "모자",
                       "brandName": "D",
                       "price": 1500
                   },
                   {
                       "categoryName": "양말",
                       "brandName": "I",
                       "price": 1700
                   },
                   {
                       "categoryName": "액세서리",
                       "brandName": "F",
                       "price": 1900
                   }
               ],
               "totalPrice": 34100
           }
       },
       "result": "200",
       "message": "성공"
    }
    
2. 모든 카테고리 상품 구매 시 총합 최저가 브랜드, 카테고리 상품 가격, 총액 조회 API
- API URL : http://localhost:8888/v1/products/brands/min-total-price
- json Response
  ```
  {
    "data": {
        "minPrice": {
            "brandName": "D",
            "categories": [
                {
                    "categoryName": "상의",
                    "price": 10100
                },
                {
                    "categoryName": "아우터",
                    "price": 5100
                },
                {
                    "categoryName": "바지",
                    "price": 3000
                },
                {
                    "categoryName": "스니커즈",
                    "price": 9500
                },
                {
                    "categoryName": "가방",
                    "price": 2500
                },
                {
                    "categoryName": "모자",
                    "price": 1500
                },
                {
                    "categoryName": "양말",
                    "price": 2400
                },
                {
                    "categoryName": "액세서리",
                    "price": 2000
                }
            ],
            "totalPrice": 36100
        }
    },
    "result": "200",
    "message": "성공"
  }

3. 카테고리 이름으로 최저, 최고가 브랜드, 상품 가격 조회 API
- API URL : http://localhost:8888/v1/products/categories/상의/min-max-price
- json Response
  ```
  {
    "data": {
        "categoryName": "상의",
        "minPrice": {
            "brandName": "C",
            "price": 10000
        },
        "maxPrice": {
            "brandName": "I",
            "price": 11400
        }
    },
    "result": "200",
    "message": "성공"
  }

#### 어드민
1. 브랜드 생성, 업데이트, 삭제, 조회 API
- 브랜드 생성
    - API URL : http://localhost:8888/v1/admin/brands
    - Http Method : POST
    - Request
      ```
      {
         "brandName" : "혜린 브랜드"
      }
    - Response
      ```
      {
         "data": {
             "brandId": 11,
             "brandName": "혜린 브랜드"
         },
         "result": "200",
         "message": "성공"
      }
- 브랜드 수정
    - API URL : http://localhost:8888/v1/admin/brands/{brandId}
    - Http Method : PUT
    - Request
      ```
      {
         "brandName" : "혜린브랜드222"
      }
    - Response
      ```
      {
         "data": {
             "brandId": 10,
             "brandName": "혜린 브랜드222"
         },
         "result": "200",
         "message": "성공"
      }
- 브랜드 삭제
    - API URL : http://localhost:8888/v1/admin/brands/{brandId}
    - Http Method : DELETE
    - Response
      ```
      {
         "data": 11,
         "result": "200",
         "message": "성공"
      }
2. 상품 생성, 업데이트, 삭제, 조회 API
- 상품 생성
    - API URL : http://localhost:8888/v1/admin/products
    - Http Method : POST
    - Request
      ```
      {
         "productName" : "혜린바지",
         "price" : 10000,
         "categoryId" : 3,
         "brandId" : 11
      }
    - Response
      ```
      {
         "data": {
             "productId": 73,
             "productName": "혜린바지",
             "price": 10000,
             "categoryName": "바지",
             "brandName": "혜린 브랜드"
         },
         "result": "200",
         "message": "성공"
      }
- 상품 수정
    - API URL : http://localhost:8888/v1/admin/products/{productId}
    - Http Method : PUT
    - Request
      ```
      {
         "productName" : "혜린바지2",
         "price" : 15000,
         "categoryId" : 3,
         "brandId" : 11
      }
    - Response
      ```
      {
         "data": {
             "productId": 73,
             "productName": "혜린바지2",
             "price": 15000,
             "categoryName": "바지",
             "brandName": "혜린 브랜드"
         },
         "result": "200",
         "message": "성공"
      }
- 상품 삭제
    - API URL : http://localhost:8888/v1/admin/products/delete
    - Http Method : POST
    - Request
      ```
      {
         "productIds" : [1,2,3,4,5]
      }
    - Success Response
      ```
      {
        "data": {
           "successProductIds": [
              1,
              2,
              3,
              4,
              5
           ],
           "failProductIds": []
        },
        "result": "200",
        "message": "성공"
      }
    - Fail Response
      ```
      {
         "data": null,
         "result": "400",
         "message": "상품 ID는 최대 30개까지 허용됩니다."
      }

### 화면
1. 메인 화면 : http://localhost:8888/musinsa/main
   <img width="992" alt="main" src="https://github.com/user-attachments/assets/41611337-55ac-4e7e-9b8b-9ecefc879b4f">
2. 어드민 메인 화면 : http://localhost:8888/musinsa/admin
   <img width="568" alt="admin_main" src="https://github.com/user-attachments/assets/cd503853-591e-4016-88f4-88ead545472a">
    1) 상품 관리 화면 : http://localhost:8888/musinsa/admin/products
       <img width="1012" alt="product" src="https://github.com/user-attachments/assets/42912691-2041-4366-8396-95f100502796">
    2) 브랜드 관리 화면 : http://localhost:8888/musinsa/admin/brands
       <img width="841" alt="brand" src="https://github.com/user-attachments/assets/5bbe9a59-82d5-4ff6-85c1-20e46d03b03a">
## 코드 빌드 & 실행
1) `./gradlew clean`
2) `./gradlew build`
3) `./gradlew bootRun`

## 테스트
1. 통합 테스트
- ProductControllerTest
    1) 고객은_카테고리_별로_최저가인_브랜드와_가격을_조회하고_총액을_확인할_수_있다
    2) 고객은_단일_브랜드로_전체_카테고리_상품을_구매시_최저가인_브랜드와_총액을_확인할_수_있다
    3) 고객은_카테고리이름으로_최저가_최고가_브랜드와_상품가격을_확인할_수_있다
    4) 존재하지_않는_카테고리인_경우_실패값과_실패사유를_확인할_수_있다
2. 단위 테스트
- BrandServiceTest
    1) 브랜드 생성 시 정상적으로 BrandDtoResponse 반환
    2) 브랜드 업데이트 시 정상적으로 BrandDtoResponse 반환
    3) 브랜드 업데이트 시 해당 브랜드가 존재하지 않을 경우 예외 발생
    4) 브랜드 삭제 시 해당 브랜드가 존재하지 않을 경우 예외 발생
    5) 브랜드 삭제 시 등록된 상품이 있으면 예외 발생
    6) 브랜드 삭제 시 정상적으로 브랜드 삭제

- ProductServiceTest
    1) 카테고리 별 최저가 조회 시 중복된 최저가 상품이 있는 경우 카테고리,상품 id 오름차 순 정렬 마지막 상품 정보 조회
    2) 모든 카테고리 상품 구매시 가격 총합 최저가 브랜드 정상 조회
    3) 카테고리 이름으로 최저가, 최고가 상품 정상 조회
    4) 카테고리 이름으로 최저가, 최고가 조회 시 카테고리 없는 경우 예외 발생
    5) 상품 생성 시 카테고리 존재하지 않는 경우 예외 발생
    6) 상품 업데이트 시 해당 상품이 존재하지 않는 경우 예외 발생
    7) 상품 삭제 시 일부 상품 아이디가 존재하지 않는 경우 failProductIds 값에 응답을 준다