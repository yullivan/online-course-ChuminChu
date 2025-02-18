## 관계
### 강의 : 강사 한명 / 여러 수강생
### 강사 : 여러 강의
### 수강생 : 여러 강의
### 강사 N:1 강의 | 수강생 : N:M

# 👥 강의

## ➡️ **강의 목록 조회**
- **Method**: `GET`
- **Path**: `/lectures`
- **Example Endpoint**: `https://localhost:8080/lectures`
- **Response**:
    - `id` (Long): 강의 ID
    - `title` (String): 강의 제목
    - `teacherName` (String): 강사 이름
    - `price` (Number): 강의 가격
    - `count` (Number): 수강생 수
    - `category` (Category): 강의 카테고리
    - `createTime` (LocalDateTime): 강의 등록 일시

## ➡️ **강의 상세 조회**
- **Method**: `GET`
- **Path**: `/lectures/{lectureId}`
- **Example Endpoint**: `https://localhost:8080/lectures/1`
- **Request Parameters**:
    - **Path Parameters**:
        - `lectureId` (Long): 강의 ID
- **Response**:
    - `id` (Long): 강의 ID
    - `title` (String): 강의 제목
    - `introduce` (String): 강의 소개
    - `price` (Number): 강의 가격
    - `studentCount` (Number): 수강생 수
    - **StudentResponse** (List):
        - `nickName` (String): 수강생 닉네임
        - `enrollmentTime` (LocalDateTime): 수강 신청 일시
    - `category` (Category): 강의 카테고리
    - `createTime` (LocalDateTime): 강의 등록 일시
    - `updateTime` (LocalDateTime): 마지막 업데이트 일시

## ➡️ **강의 등록**
- **Method**: `POST`
- **Path**: `/lectures`
- **Example Endpoint**: `https://localhost:8080/lectures`
- **Request Parameters**:
    - **Body Parameters**:
        - `title` (String): 강의 제목
        - `introduce` (String): 강의 소개
        - `price` (Number): 강의 가격
        - `category` (Category): 강의 카테고리
        - `createTime` (LocalDateTime): 강의 등록 일시
- **Response**:
    - `id` (Long): 강의 ID
    - `title` (String): 강의 제목
    - `introduce` (String): 강의 소개
    - `price` (Number): 강의 가격
    - `category` (Category): 강의 카테고리
    - `createTime` (LocalDateTime): 강의 등록 일시

## ➡️ **강의 삭제**
- **Method**: `DELETE`
- **Path**: `/lectures/{lectureId}`
- **Example Endpoint**: `https://localhost:8080/lectures/1`
- **Request Parameters**:
    - **Path Parameters**:
        - `lectureId` (Long): 강의 ID
- **Response**:
    - `id` (Long): 삭제된 강의 ID

## ➡️ **강의 수정**
- **Method**: `PUT`
- **Path**: `/lectures/{lectureId}`
- **Example Endpoint**: `https://localhost:8080/lectures/1`
- **Request Parameters**:
    - **Path Parameters**:
        - `lectureId` (Long): 강의 ID
    - **Body Parameters**: (수정할 필드만 포함)
        - `title` (String): 강의 제목
        - `introduce` (String): 강의 소개
        - `price` (Number): 강의 가격
        - `category` (Category): 강의 카테고리
- **Response**:
    - `id` (Long): 수정된 강의 ID
    - `title` (String): 수정된 강의 제목
    - `introduce` (String): 수정된 강의 소개
    - `price` (Number): 수정된 강의 가격
    - `category` (Category): 수정된 강의 카테고리
    - `updateTime` (LocalDateTime): 수정 일시

---
