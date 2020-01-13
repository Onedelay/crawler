## 크롤러서버

Spring boot를 이용한 간단한 크롤러 서버로 네이버/다음(실시간 검색어, 뉴스), Android weekly 사이트를 크롤링한다.

<br>

### API list

#### 포털사이트

아래 url로 접속 시 json 리스트 형태로 리턴한다.

아래 {category} 는 안드로이드 앱에서 가지고있다. (이 파라미터 안보낼 경우 전체 데이터를 뿌려주고싶은데.. 똑같은 코드를 또 복붙해야하는건가?ㅠㅠ)

- 네이버 : "정치", "경제", "사회", "생활/문화", "세계", "IT/과학"
- 다음 : "뉴스", "연예", "스포츠"

1. 네이버 뉴스 : https://onedelay-crawler-server.herokuapp.com/naver?category={text}
2. 다음 뉴스 : https://onedelay-crawler-server.herokuapp.com/daumcategory={text}
3. 네이버 실시간 검색어 : https://onedelay-crawler-server.herokuapp.com/naver_issue
4. 다음 실시간 검색어 : https://onedelay-crawler-server.herokuapp.com/daum_issue

<br>

#### Android weekly

https://onedelay-crawler-server.herokuapp.com/android_weekly?count=[int]

count 값은 optional이다.

<br>

좀 더 정리하고싶지만 일단은 필요한 것만 만들어둔 미완성 서버다.