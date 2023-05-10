# 아르바이트 급여 및 근무지관리 어플 WINWIN

현재 일하고 있는 근무지 목록과, 한달 동안 번 급여를 확인할 수 있는 어플

## 1. 로그인

* retrofit라이브러리를 활용한 HTTP통신

* 각 input에 대한 유효성 검증

* 로그인 시 서버에서 Token 생성 & 해당 토큰을 sharedpreferences에 저장. 이후 사용자 인증 시 활용

![bandicam 2023-05-05 00-09-29-743](https://user-images.githubusercontent.com/50603211/236253685-3cd6f889-2b0b-4c5c-8c84-9a3294e1080f.gif)

## 2. 회원가입

* 이메일 중복 검사

* 비밀번호 일치 및 유효성 검사

![녹화_2023_05_05_16_48_55_764](https://user-images.githubusercontent.com/50603211/236404066-70d5bf27-5e69-49e7-af2c-5ffc98e545e4.gif)

## 3. 근무지 조회 및 급여 조회

* 근무한 근무지 목록과 해당 근무지에서 한달동안 번 급여 확인

* RecyclerView를 활용한 가로 및 세로 스크롤

* CardView를 활용해 근무지 목록 및 달력 구분

* 근무 일정이 있는 경우 주간 달력에 dot으로 표시 (최대 3개까지 표현 가능)

* '>' 버튼 터치 시 상세 내역으로 이동

![녹화_2023_05_05_17_00_20_71](https://user-images.githubusercontent.com/50603211/236405870-34b6bf1c-5664-4525-a569-fd5b5f6e5c37.gif)


## 4. 근무지 별 상세 조회

* 특정 근무지에 대한 한달 동안의 상세 내역 출력

* 달력을 통해 근무 이력이 있는 날의 상세 내역 조회 가능

* Expandable RecyclerView를 활용해 상세 내역 조회 가능

* 서버에서 데이터 가져오기 (구현중)

![녹화_2023_05_10_14_51_22_590](https://github.com/cyz065/AndroidProject/assets/50603211/7faf4e2c-aee3-4449-a42b-92e61ec454ce)

