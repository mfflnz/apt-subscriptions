**Pacchetti e plugin**

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

    docker run -it --network apt-network -v "$PWD"/assets:/assets --rm mongo:latest mongoimport --host my-mongo --collection='orders' --headerline --file=assets/orders.csv --type=csv

Col comando sopra ottengo:

    2025-08-11T14:19:07.198+0000    connected to: mongodb://my-mongo/
    2025-08-11T14:19:08.005+0000    3868 document(s) imported successfully. 0 document(s) failed to import.

Nella shell di MongoDB (mongosh) faccio una verifica sui contenuti appena importati:

    docker run -it --network apt-network --rm mongo:latest mongosh --host my-mongo

Nella shell di MongoDB:

    test> db.orders.countDocuments()
    3868

Includo i comandi relativi a Docker in uno script (setup.sh).

---

**Requisiti**

Voglio leggere da un database le informazioni relative agli ordini di abbonamenti alla rivista, filtrare e formattare i risultati, e quindi salvarli in un file .csv in cui mantengo solo alcuni campi a cui sono interessato.

Voglio vedere un elenco sintetico degli ordini presenti e i dettagli dell'ordine selezionato.  Voglio fare operazioni CRUD sugli ordini.

La funzione che più mi interessa è specificare un range di date ed estrarre gli ordini conclusi in quel periodo.

Per l'accesso al database intendo seguire il pattern Repository.

Schema del Model–view–presenter:

    model
        Order

    repository
        OrderRepository

    view
        SearchView
        ListView
        OrderView

    controller
        SubscriptionsController

Imposto il progetto Maven:

    mvn archetype:generate \
    -DarchetypeGroupId=org.apache.maven.archetypes \
    -DarchetypeArtifactId=maven-archetype-quickstart \
    -DarchetypeVersion=1.1 \
    -DgroupId=org.blefuscu \
    -DartifactId=apt-subscriptions \
    -DinteractiveMode=false

Dopodiché lo importo in Eclipse secondo la procedura descritta nella sezione 7.3.2 del libro.

Inizializzo il repository di Git nella cartella apt-subscriptions appena creata:

    git init
    git config user.name "Lorenzo Maffucci"
    git config user.email "lorenzo.maffucci@edu.unifi.it"
    
Quindi importo il repository in Eclipse (Window > Show View > Other... > Git > Git Repositories).
    
