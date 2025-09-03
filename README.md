[![Java CI with Maven in Linux](https://github.com/mfflnz/apt-subscriptions/actions/workflows/linux.yml/badge.svg)](https://github.com/mfflnz/apt-subscriptions/actions/workflows/linux.yml) [![Java CI with Maven in macOS](https://github.com/mfflnz/apt-subscriptions/actions/workflows/macos.yml/badge.svg)](https://github.com/mfflnz/apt-subscriptions/actions/workflows/macos.yml) [![Java CI with Maven in Windows](https://github.com/mfflnz/apt-subscriptions/actions/workflows/windows.yml/badge.svg)](https://github.com/mfflnz/apt-subscriptions/actions/workflows/windows.yml)
[![Coverage Status](https://coveralls.io/repos/github/mfflnz/apt-subscriptions/badge.svg?branch=main)](https://coveralls.io/github/mfflnz/apt-subscriptions?branch=main)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=mfflnz_apt-subscriptions&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=mfflnz_apt-subscriptions) [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=mfflnz_apt-subscriptions&metric=coverage)](https://sonarcloud.io/summary/new_code?id=mfflnz_apt-subscriptions) [![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=mfflnz_apt-subscriptions&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=mfflnz_apt-subscriptions) [![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=mfflnz_apt-subscriptions&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=mfflnz_apt-subscriptions)

---

### Requisiti

Voglio leggere da un database le informazioni relative agli ordini di acquisto di copie o abbonamenti a una rivista, filtrare e formattare i risultati, e quindi esportarli in un file in cui mantengo solo alcuni campi a cui sono interessato.

Voglio visualizzare un elenco sintetico degli ordini presenti e i dettagli di un particolare ordine selezionato. Voglio fare operazioni di lettura, aggiornamento e cancellazione sul database degli ordini.

La funzione che mi interessa particolarmente è specificare un intervallo di date ed estrarre gli ordini conclusi in quel periodo.

---

### Ambiente

Con il gestore di pacchetti del S.O. (Arch Linux) installo:

- OpenJDK 8.462.u08-1
- Eclipse 2025-06
- Maven 3.9.11
- Git 2.51.0
- Docker Engine 28.3.3
- Docker Compose 2.39.2

Sul sistema sono già presenti le versioni 17 e 24 di OpenJDK; imposto la 8 come default:

    # archlinux-java set java-8-openjdk

Tramite il Marketplace di Eclipse installo alcuni plugin:

- Pitclipse 2.2.0
- Docker Tooling 5.18.1
- SonarQube for IDE 11.13
- WindowBuilder 1.20.0

---

### Design

Per l'accesso al database intendo seguire il pattern Repository (descritto in [Eva03] nel riferimento bibliografico del testo). Per la creazione degli oggetti mi rifaccio al pattern Builder ([GHJV95]).

(Il termine "Order" = "ordine" è da intendersi nel senso di ordine di acquisto di un prodotto.)

Schema del Model–view–presenter:

    model
        Order
        FormattedOrder

    repository
        OrderRepository

    view
        DashboardView
            SearchView
                - ricerca degli ordini per intervallo di date
            ListView
                - elenco sintetico degli ordini
                - esportazione degli ordini
            OrderView
                - vista dettagliata dell'ordine selezionato
                - aggiornamento dell'ordine selezionato
                - eliminazione dell'ordine selezionato

    controller
        SubscriptionsController

---

### TDD

Inizialmente faccio uno sketch del Model e scrivo le interfacce del Repository e delle View (File > New > Interface). Quindi comincio ad applicare il TDD per costruire il Controller.

Faccio un mock del Repository e della View; quindi nel primo test verifico che il metodo `requestOrders()`, invocato senza parametri, riporti sulla View la lista di tutti gli ordini presenti. Per passare dalla fase *red* alla fase *green*, con il content assist di Eclipse comincio a definire i primi tratti del Controller: i campi `listView` e `orderRepository` e il metodo `requestOrders()`.

Col test successivo scrivo una versione del metodo `requestOrders` che accetta due parametri di tipo `LocalDate` e verifico che ci siano interazioni coi mock del Repository e della View.

Proseguo con alcuni test su `requestOrders(LocalDate fromDate, LocalDate toDate)` che permetteranno di gestire i casi di errore sulle date verificando con AssertJ che si sollevino eccezioni accompagnate da messaggi pertinenti:

- `fromDate` non è specificato;
- `toDate` non è specificato;
- `fromDate` è successivo a `toDate`.

Verifico che il codice del Controller sia raggiungibile e che i mutanti siano eliminati.

Proseguo con l'implementazione di altre funzioni del Controller:

- ✅ recupero dei dettagli di un ordine;
- ✅ eliminazione di un ordine;
- ✅ modifica di un ordine;
- ❌ formattazione di una lista di ordini secondo i seguenti criteri:
    1. `orderId`
    2. `orderDate`: "2025-08-05 11:11:01" -> "05/08/2025"
    3. `paidDate`: come sopra, se presente, altrimenti campo vuoto
    4. `orderTotal`
    5. `orderNetTotal`
    6. `paymentMethodTitle` **TODO**
    7. `shippingFirstName` se presente, altrimenti `shippingFirstName` <- `billingFirstName`
    8. `shippingLastName` se presente, altrimenti `shippingLastName` <- `billingLastName`
    9. `shippingAddress1`: come sopra (medesimo criterio)
    10. `shippingPostcode`: come sopra
    11. `shippingCity`: come sopra
    12. `shippingState`: come sopra
    13. `customerEmail`
    14. `billingPhone`
    15. `shippingItems`: togliere la stringa "items:" in testa e togliere a partire dal primo carattere "|" fino a fine della stringa
    16. `firstIssue` **TODO**
    17. `lastIssue` **TODO**
    18. `customerNote` **TODO**
- ❌ esportazione della lista formattata nel file .csv.

(Osservo che SonarCloud lamenta diverse *issues* dovute alla disseminazione di vari TODO.)

Nella classe `SubscriptionsController` estraggo il metodo privato `checkOrderAvailability`.

**TODO**: Nella conversione dei documenti in Order, le stringhe `orderDate` e `paidDate` vanno tradotte in oggetti di tipo `LocalDate` (es: "2025-08-05 11:11:01" -> `LocalDate.of(2025, 8, 5)`; stringhe che eventualmente contengono doppi apici ("), segnatamente `shippingItems`, vanno trattate in modo consono.

**TODO**: Migliorare la formattazione di orderDate e paidDate.

---

### Docker

Creo una rete per mettere in comunicazione i container che useremo:

    docker network create apt-network

Lancio il container di MongoDB:

    docker run -d --name my-mongo --network apt-network -p 27017:27017 --rm mongo

Nella cartella assets ho copiato un file .csv con i dati da caricare su MongoDB (intestazione e informazioni sugli ordini) e lo importo con il tool mongoimport:

    docker run -it --network apt-network -v "$PWD"/assets:/assets --rm mongo:latest mongoimport --host my-mongo --collection='orders' --headerline --file=assets/sample-orders.csv --type=csv

Col comando sopra ottengo:

    2025-08-21T11:05:38.680+0000    connected to: mongodb://my-mongo/
    2025-08-21T11:05:38.730+0000    4 document(s) imported successfully. 0 document(s) failed to import.

Nella shell di MongoDB (mongosh) faccio una verifica sui contenuti appena importati:

    docker run -it --network apt-network --rm mongo:latest mongosh --host my-mongo

Nella shell di MongoDB:

    test> db.orders.countDocuments()
    4

Includo i comandi relativi a Docker in uno script (`setup.sh`).

---

### Maven

Imposto il progetto:

    mvn archetype:generate \
    -DarchetypeGroupId=org.apache.maven.archetypes \
    -DarchetypeArtifactId=maven-archetype-quickstart \
    -DarchetypeVersion=1.1 \
    -DgroupId=org.blefuscu \
    -DartifactId=apt-subscriptions \
    -DinteractiveMode=false

Dopodiché lo importo in Eclipse secondo la procedura descritta nella sezione 7.3.2 del libro.

---

### Git

Inizializzo il repository nella cartella `apt-subscriptions` appena creata:

    git init
    git config user.name "Lorenzo Maffucci"
    git config user.email "lorenzo.maffucci@edu.unifi.it"
    
Quindi lo importo in Eclipse (Window > Show View > Other... > Git > Git Repositories) e lo connetto al repository remoto su GitHub:

    git remote add origin git@github.com:mfflnz/apt-subscriptions.git

---  

### Code Coverage

Escludo le classi del Model dal computo della code coverage.

Faccio un test:

    mvn clean jacoco:prepare-agent test jacoco:report
    
---

### Mutation Testing

Faccio un test:

    mvn clean test org.pitest:pitest-maven:mutationCoverage
    
**TODO**: aggiustare Pitclipse per escludere il Model.

Configuro il plugin di Pitest in modo da includere i mutator del gruppo STRONGER.

---

### Continuous Integration

Faccio un primo workflow su GitHub Actions coi plugin di JaCoCo e Pitest. Il repository è connesso a Coveralls; faccio un test in locale:

    mvn coveralls:report -DrepoToken=<TOKEN>
    
Inserisco il token di Coveralls tra i *secrets* del repository e aggiorno di conseguenza il workflow finora sperimentato (`linux.yaml`).

A una prima build, il Quality Gate di SonarCloud non è va a buon fine per eccesso di codice duplicato nel Model: lo escludo dall'analisi (nell'interfaccia web di SonarCloud: Administration > General Settings > Analysis Scope > Coverage Exclusions e Duplication Exclusions).

Modifico il POM spostando in due profili ad hoc l'attivazione dei plugin di JaCoCo e Pitest. I *goal* che generano i report sono legati alla fase *verify*, per cui il comando diventa:

    mvn clean verify -Pjacoco,mutation-testing

Per il momento IWOMM. Verifico in locale la configurazione di SonarCloud con GitHub Actions (finora era impostata la Automatic Analysis).

La build va a buon fine ma, probabilmente per via della formattazione degli `additional-maven-args`, non esegue i profili e i goal richiesti per la versione 17 di Java:

    Warning:  The requested profile "mutation-testing coveralls:report org.sonarsource.scanner.maven:sonar-maven-plugin:sonar -Dsonar.projectKey=mfflnz_apt-subscriptions" could not be activated because it does not exist.

(Infatti anche l'ultimo computo di Coveralls non ha ricevuto i dati dell'ultimo commit.)

Imposto Docker su GitHub Actions. Nel POM configuro il `docker-maven-plugin` in modo da attivarlo con il profilo `docker`. (Faccio un test in locale con `mvn verify -Pdocker`.) Attivo due container di MongoDB, uno dei quali con funzione di server, l'altro per importare da shell (`mongoimport`) alcuni documenti-campione su cui fare i test.

#### Aggiustamenti con macOS

Provo a impostare i workflow anche per macOS e Windows per verificare se per il momento tutto va a buon fine. Scopro che il JDK 8 non è disponibile per il runner macos-13 e lo sostituisco con il JDK 11. Nella build per Java 17, allo step "Install Docker", vedo intanto che:

    Installing QEMU
    /opt/homebrew/bin/brew install qemu
    Error: The operation was canceled.

Ripeto il job con messaggi di debug e vedo che:

    Starting lima instance
    Error: The process '/opt/homebrew/bin/limactl' failed with exit code 1
    
Provo a usare il runner `macos-latest` (che al momento equivale a `macos-15`) con la versione 4 della `setup-docker-action` (senza specifiche sulle versioni minori, come da esempio sul [README](https://github.com/docker/setup-docker-action) della action) e la variabile d'ambiente per Lima (di nuovo come da esempio, con specifiche sul numero di CPU e sulla quantità di memoria da emulare). Di nuovo ottengo lo stesso errore, e altrettanto succede con il workflow minimale riportato nella documentazione della action. Sembra un problema legato alla versione 9.1.0 di QEMU, che pare non presentarsi usando la versione 9.0.2. Faccio riferimento a [questa issue](https://github.com/docker/actions-toolkit/issues/455) e a [questa](https://github.com/docker/setup-docker-action/issues/108). Faccio un tentativo e sembra funzionare con questa configurazione:

- runner `macos-13`
- QEMU 9.0.2
- `setup-docker-action@v3`

Provo a integrare questo espediente nel precedente workflow e ottengo questo errore:

    Error:  Failed to execute goal io.fabric8:docker-maven-plugin:0.45.1:start (docker-start) on project apt-subscriptions: Execution docker-start of goal io.fabric8:docker-maven-plugin:0.45.1:start failed: No <dockerHost> given, no DOCKER_HOST environment variable, no read/writable '/var/run/docker.sock' or '//./pipe/docker_engine' and no external provider like Docker machine configured -> [Help 1]
    
Aggiungo la chiave `set-host` alla configurazione di `setup-docker-action`, che avevo dimenticato di specificare, e la imposto a `true`.

Con il commit https://github.com/mfflnz/apt-subscriptions/commit/68cbfdb6d7e47e241ebb85acd180fae6f0933934 la build su macOS si interrompe con l'errore di prima relativo a QEMU. Provo a modificare il workflow.

#### Workflow per Windows

Provo a impostare un workflow per Windows 2025 con Java 11, 17 e 21.

Docker mi dà errore:

    Error:  DOCKER> I/O Error [Unable to create container for [mongo:5] : {"message":"invalid volume specification: 'D:\\a\\apt-subscriptions\\apt-subscriptions\\assets:/assets'"} (Internal Server Error: 500)]
    
Provo a risolvere inserendo il percorso della cartella `assets` nelle proprietà del POM e a sovrascriverlo con l'opzione `--define` di Maven da linea di comando nel workflow di Windows:

    -Dassetspath="%cd%\assets"
    
La build di Java 21 si interrompe:

    Warning: Failed to download action 'https://api.github.com/repos/actions/setup-java/zipball/c5195efecf7bdfc987ee8bae7a71cb8b11521c00'. Error: A connection attempt failed because the connected party did not properly respond after a period of time, or established connection failed because connected host has failed to respond. (api.github.com:443) 
    Warning: Back off 18.306 seconds before retry.
    Error: A connection attempt failed because the connected party did not properly respond after a period of time, or established connection failed because connected host has failed to respond. (api.github.com:443)

Il problema del path non si è ancora risolto. Per il momento sospendo la configurazione del workflow di Windows e provo a impostare un IT per vedere se riesco a comunicare correttamente con il database.

**TODO**: Configurare il workflow di Windows

---

### Integration Tests

Aggiungo al POM le dipendenze di Mongo Java API e di Logback, e aggiungo i plugin Build Helper (per gestire la cartella degli IT) e Failsafe (per eseguire gli IT). Faccio una bozza dell'implementazione di OrderRepository.

Sposto fuori dal profilo `docker` l'attivazione del plugin Docker.

**TODO**: Ho cominciato a tracciare uno schema di implementazione del repository e delle view, per le quali ancora però non ho scritto unit test: fino a quel momento le tengo escluse dal conteggio della code coverage. (Osservo che SonarCloud indica un'ora di technical debt relative al codice non raggiunto.)