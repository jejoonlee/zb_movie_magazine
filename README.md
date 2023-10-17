# 영화 리뷰 (MOVMAG)

<img src="README.assets/DlWnWaUX4AAd8rQ.jpg" alt="DlWnWaUX4AAd8rQ" style="zoom:33%;" />



## What?

> #### 🪄영화에 대한 리뷰만 읽는 것이 아닌, 영화에 대해 소통할 수 있는 어플리케이션 개발이 목표



#### 영화 매거진에서 컨셉을 정해보았다

- 영화에 대한 리뷰, 그리고 리뷰에 대한 댓글을 달 수 있다
- 영화에 대한 리뷰만 읽는 것이 아닌, 영화에 대해 소통할 수 있는 어플리케이션 개발이 목표



#### 심화. 영화로 시작하지만, 가능하다면 TV 쇼도 추가할 예정이다





## Who?

#### 이용자는 크게 두 분류로 나뉜다

- 리뷰를 작성하는 **EDITOR**
  - 리뷰를 쓰고, 읽고, 수정하고, 삭제할 수 있음
  - 댓글을 쓰고, 읽고, 수정하고, 삭제할 수 있음
- 리뷰를 읽는 일반 **USER**
  - 리뷰를 읽을 수 있음
  - 댓글을 쓰고, 읽고, 수정하고, 삭제할 수 있음





## How?

#### 영화에 관련된 정보는 TMDB에서 영화 API를 사용할 예정

- 미리 영화들을 DB에 저장을 할 예정 (DB는 MySQL)





## 구현할 기능



#### 인증 (모두)

- **POST 회원가입**
  - *추후에 간편 로그인 추가 (gmail? kakao? naver?)* 
- **POST 로그인**



#### 영화 리뷰

- **POST 작성** (EDITOR)
- **GET 키워드 검색** (ADMIN, EDITOR, USER)
- **GET 리뷰 상세 내용** (ADMIN, EDITOR, USER)
- **PUT 리뷰 수정** (EDITOR)
- **DELETE 리뷰 삭제** (ADMIN, EDITOR)



#### 리뷰 댓글

- **POST 작성** (ADMIN, EDITOR, USER)
- **GET 해당 리뷰에 대한 댓글 일기** (ADMIN, EDITOR, USER)
  - 시간 순으로 내림차순으로 (최신이 제일 위에)
- **PUT 댓글 수정** (ADMIN, EDITOR, USER)
- **DELETE 리뷰 삭제** (ADMIN, EDITOR, USER)



#### 영화 업데이트

- TMDB에 저장되어 있는 영화를 업데이트 하기 (ADMIN만 할 수 있음)
  - *이 기능을 수동으로, URL을 통해서 기능을 구현할지 or 스케줄러를 사용할지?*
