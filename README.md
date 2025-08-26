**Requisiti**

Voglio leggere da un database le informazioni relative agli ordini di acquisto di copie o abbonamenti a una rivista, filtrare e formattare i risultati, e quindi esportarli in un file in cui mantengo solo alcuni campi a cui sono interessato.

Voglio visualizzare un elenco sintetico degli ordini presenti e i dettagli di un particolare ordine selezionato. Voglio fare operazioni di lettura, aggiornamento e cancellazione sul database degli ordini.

La funzione che mi interessa particolarmente è specificare un intervallo di date ed estrarre gli ordini conclusi in quel periodo.

---

**Scelte di design**

Per l'accesso al database intendo seguire il pattern Repository (descritto in [Eva03] nel riferimento bibliografico del testo). Per la creazione degli oggetti mi rifaccio al pattern Builder ([GHJV95]).

(Il termine "Order" = "ordine" è da intendersi nel senso di ordine di acquisto di un prodotto.)

Schema del Model–view–presenter:

    model
        Order

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

Inizialmente faccio uno sketch del Model e scrivo le interfacce del Repository e delle View (File > New > Interface). Quindi comincio ad applicare il TDD per costruire il Controller.

Faccio un mock del Repository e della View; quindi nel primo test verifico che il metodo `requestOrders()`, invocato senza parametri, riporti sulla View la lista di tutti gli ordini presenti. Per passare dalla fase *red* alla fase *green*, con il content assist di Eclipse comincio a definire i primi tratti del Controller: i campi `listView` e `orderRepository` e il metodo `requestOrders()`.

---

**Preparazione dell'ambiente**

Con il gestore di pacchetti del S.O. (Arch Linux) installo:

- OpenJDK 8.462.u08-1
- Eclipse 2025-06
- Maven 3.9.10
- Git 2.50.1
- Docker Engine 28.3.3

Sul sistema sono già presenti le versioni 17 e 24 di OpenJDK; imposto la 8 come default:

    # archlinux-java set java-8-openjdk

Tramite il Marketplace di Eclipse installo alcuni plugin:

- Pitclipse 2.2.0
- Docker Tooling 5.18.1
- SonarQube for IDE 11.13
- WindowBuilder 1.20.0

---

**Docker**

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

Includo i comandi relativi a Docker in uno script (setup.sh).

---

Imposto il progetto Maven:

    mvn archetype:generate \
    -DarchetypeGroupId=org.apache.maven.archetypes \
    -DarchetypeArtifactId=maven-archetype-quickstart \
    -DarchetypeVersion=1.1 \
    -DgroupId=org.blefuscu \
    -DartifactId=apt-subscriptions \
    -DinteractiveMode=false

Dopodiché lo importo in Eclipse secondo la procedura descritta nella sezione 7.3.2 del libro.

Inizializzo il repository Git nella cartella apt-subscriptions appena creata:

    git init
    git config user.name "Lorenzo Maffucci"
    git config user.email "lorenzo.maffucci@edu.unifi.it"
    
Quindi importo il repository in Eclipse (Window > Show View > Other... > Git > Git Repositories).

---  

**Code Coverage**

Escludo le classi del Model dal computo della code coverage.

Faccio un test:

    mvn clean jacoco:prepare-agent test jacoco:report
    
---

**Mutation Testing**

Faccio un test:

    mvn clean test org.pitest:pitest-maven:mutationCoverage

---

**Continuous Integration**

// TODO