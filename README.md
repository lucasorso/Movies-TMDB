# Movies Project

Simples projeto desenvolvido em Kotlin, que utiliza a API [The Movie Databse](https://www.themoviedb.org/documentation/api), 
na qual são listados os próximos filmes em cartaz.

São utiliandos 3 _endpoints_: upcoming, search, details.<br />
Os dados são persistidos para funcionamento _Offline_.<br />
O Projeto encontra-se com as últimas atualizações do plugin do Gradle(no momento) além da inclusão do Jetack AndroidX.<br />
NavigationComponet para navegação entre as telas.<br />
Testes unitários dos serviços que consomem a API.<br />
Proguard com Retrofit + OkHttp + Glide.<br />

###### Arquitetura
- Utlizou-se do padrão MVP + _Clean Code_

###### Bibliotecas
- Extensões reativas: RxJAva, RxKotlin, RxAndroid
- Imagens: Glide
- Comunicacão : Retrofit + OkHttp
- Testes: Mockito
- Banco de dados: Realm
