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

* 근무한 근무지 목록과 해당 근무지에서 이번 달 동안 번 급여 확인

* RecyclerView를 활용한 가로 및 세로 스크롤

* CardView를 활용해 근무지 목록 및 달력 구분

* 근무 일정이 있는 경우 월간 달력에 dot으로 표시 (1 day 당 최대 3개까지 표현 가능)

* '>' 버튼 터치 시 상세 내역으로 이동

* 서버에서 데이터를 받아 CardView 생성 및 월간 달력에 dot 표시

* 현재를 기준으로 지난 1년간의 모든 기록 조회 가능

![녹화_2023_05_23_20_43_57_232](https://github.com/cyz065/AndroidProject/assets/50603211/d7c33a4b-cc92-4a72-8380-fc78d2a3bcf4)

## 4. 근무지 별 상세 조회

* 특정 근무지에 대한 이번 달 동안의 상세 내역 출력

* 달력을 통해 근무 이력이 있는 날의 상세 내역 조회 가능

* Expandable RecyclerView를 활용해 상세 내역 조회 가능

* 서버에서 데이터 가져오기

* 일정 개수에 따라 달력에 dot으로 표시 (1 day 당 최대 3개까지 표현 가능)

* 홈 화면과 다르게 설정 된 기간(이번 달) 동안의 내역만 조회

* (5)의 근무 기록 추가로 인해 일정이 새롭게 추가된 경우, 달력에 새롭게 추가된 일정 추가

![녹화_2023_05_23_20_41_33_239](https://github.com/cyz065/AndroidProject/assets/50603211/1e84aa60-1e2a-4c5a-8f22-4c6a5def3388)

## 5. 근무 기록 추가

* 추가하기 버튼 클릭을 통해 해당 근무지의 근무 기록을 추가

* number picker와 date picker를 통해 추가 수당 배율 및 날짜 & 시간 입력 가능

* onResume() 메소드에 HTTP통신 메소드를 구현하고, adapter에 notifyDataSetChanged() 메소드를 통해 새로운 기록이 추가되는 경우 이를 반영한 결과를 출력

![녹화_2023_05_23_22_06_00_864](https://github.com/cyz065/AndroidProject/assets/50603211/b637c6d9-8732-4237-8ba2-229cfaa352d0)



