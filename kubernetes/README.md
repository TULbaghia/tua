## Jak into kubernetes

Pamiętaj o prawidłowym namespace

1. mariadb
    1. Załaduj `configMaps.yml`
    1. Załaduj `mariaSecret.yml`
    1. Załaduj `statefulSet.yml`
    1. Załaduj `service.yml`
2. wildfly
   1. Stworz projekt `wildfly helm chart (1.4)`
   2. Wybierz `yml` i wklej konfigurację
   3. Ustaw prawidłowy sekret
   4. Stwórz projekt
3. dostosowanie wildfly
   1. Wejdz w `imagestreams`
   2. Skopiuj nazwę repozytorium
   3. W `deployment` -> `wildfly` -> `yml` -> `linia:~256 -- image: >-`
   4. Zamien containers.resources.image na `<nazwa_repozytorium>:latest`
4. horizontalPodAutoscaler
   1. Załaduj konfigurację `hpa.yml`
