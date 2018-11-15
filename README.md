## 크롤러서버

<br>

Spring boot 프레임워크를 사용한 간단한 크롤러 서버

kotlin으로 작성되었음

<br>

### API 목록(이 말이 맞는지 잘 모르겠다)

아래 url 접속 시 json 리스트 형태로 리턴한다.

이제 이걸 안드로이드 앱에서 받도록 구현하면 된다!

아래 {category} 는 안드로이드 앱에서 가지고있다. (이 파라미터 안보낼 경우 전체 데이터를 뿌려주고싶은데.. 똑같은 코드를 또 복붙해야하는건가?ㅠㅠ)

- 네이버 : "정치", "경제", "사회", "생활/문화", "세계", "IT/과학"
- 다음 : "뉴스", "연예", "스포츠"

1. 네이버 뉴스 : https://onedelay-crawler-server.herokuapp.com/naver/{category}
2. 다음 뉴스 : https://onedelay-crawler-server.herokuapp.com/daum{category}

<br>

