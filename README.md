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
            - ordini opportunamente formattati con i soli dati rilevanti

    repository
        OrderRepository
            - operazioni di ricerca, aggiornamento ed eliminazione

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
            MessageView
                - un campo che mostra messaggi informativi o di errore

    controller
        SubscriptionsController
            operazioni sugli ordini (-> database)
        ExportController
            operazioni sugli export (-> filesystem)

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
- ✅ formattazione di una lista di ordini secondo i seguenti criteri:
    1. `orderId`
    2. `orderDate`: "2025-08-05 11:11:01" -> "05/08/2025"
    3. `paidDate`: come sopra, se presente, altrimenti campo vuoto
    4. `orderTotal`
    5. `orderNetTotal`
    6. `paymentMethodTitle`
    7. `shippingFirstName` se presente, altrimenti `shippingFirstName` <- `billingFirstName`
    8. `shippingLastName` se presente, altrimenti `shippingLastName` <- `billingLastName`
    9. `shippingAddress1`: come sopra (medesimo criterio)
    10. `shippingPostcode`: come sopra
    11. `shippingCity`: come sopra
    12. `shippingState`: come sopra
    13. `customerEmail`
    14. `billingPhone`
    15. `shippingItems`: togliere la stringa "items:" in testa e togliere a partire dal primo carattere "|" fino a fine della stringa
    16. `firstIssue`
    17. `lastIssue`
    18. `customerNote`
- ✅ esportazione della lista formattata nel file .csv.
    - Si pone il problema di organizzare degli unit test su un metodo che scrive sul filesystem. Per farlo, dichiaro un'interfaccia *wrapper*, `ExportManager`, con i metodi necessari (controllo presenza del file, scrittura del file, cancellazione del file) che invocherò come mock in questa fase e successivamente implementerò richiamando metodi delle API `java.nio`.
- ✅ comunicazione con le view

(Osservo che al comparire di nuovi scheletri di codice SonarCloud lamenta diverse *issues* dovute alla disseminazione di vari TODO e la build nella CI non va a buon fine perché la coverage complessiva scende al di sotto del 100%.)

**TODO**: Nella conversione dei documenti in Order, le stringhe `orderDate` e `paidDate` vanno tradotte in oggetti di tipo `LocalDate` (es: "2025-08-05 11:11:01" -> `LocalDate.of(2025, 8, 5)`; stringhe che eventualmente contengono doppi apici ("), segnatamente `shippingItems`, vanno trattate in modo consono.

**TODO**: Migliorare la formattazione di orderDate e paidDate.

Proseguo con l'implementazione del Repository. Ho già aggiunto al POM le dipendenze per il Mongo Java Driver e per l'accesso ai log. Aggiungo la dipendenza per MongoDB Java Server (database in-memory). Al lancio del primo unit test ottengo un errore di inizializzazione:

    java.lang.UnsupportedClassVersionError: de/bwaldvogel/mongo/MongoBackend has been compiled by a more recent version of the Java Runtime (class file version 61.0), this version of the Java Runtime only recognizes class file versions up to 52.0
    
Abbasso la versione del mongo-java-server in modo da farlo funzionare anche con Java 8 (52.0). Aggiungo `toString`, `equals` e `hashCode` al codice della classe Order per far funzionare i confronti con la mappatura dei documenti estratti dal database (v. `findAll()` nel Repository). 

Concludo l'implementazione del Repository e verifico in locale che tutto funzioni:

    mvn clean test
    mvn clean verify -Pjacoco,mutation-testing

Controllo che il mutation test da Eclipse vada a buon fine e vedo che sopravvivono solo 4 mutanti nel Model.

Osservo i report di SonarQube e faccio un po' di pulizia. Nel Repository estraggo costanti laddove ho indicato con una stringa i riferimenti ai campi dei documenti (`ORDER_ID`, `ORDER_DATE`). Nel test del Repository SonarLint suggerisce, ad esempio, di limitare a uno i possibili metodi che possono sollevare un'eccezione nella lambda di `testFindByDateRangeWhenDateRangeIsIncorrect()`.

Ripulisco e faccio un push, approfittandone anche per eseguire un workflow di Windows. Osservo intanto che il technical debt stimato da SonarQube si è ridotto da 1h a 35m. Il workflow per Windows, come mi aspettavo, non funziona per un problema sul percorso del volume:

    Error:  Failed to execute goal io.fabric8:docker-maven-plugin:0.45.1:start (docker-start) on project apt-subscriptions: I/O Error: Unable to create container for [mongo:5] : {"message":"invalid volume specification: 'D:\\a\\apt-subscriptions\\apt-subscriptions\\assets:/assets'"} (Internal Server Error: 500) -> [Help 1]
    
**TODO**: capire le [linee guida per lo storage](https://learn.microsoft.com/en-us/virtualization/windowscontainers/manage-containers/persistent-storage) su Microsoft Learn. Nel frattempo modifico il workflow in modo che il trigger avvenga solo [manualmente](https://docs.github.com/en/actions/using-workflows/events-that-trigger-workflows#workflow_dispatch) (`on: workflow_dispatch`).

Comincio a implementare la View preparando i primi unit test. Aggiorno nel POM la dipendenza di AssertJ Core sostituendola con AssertJ Swing.

Tengo sotto controllo la code coverage. A buon punto della scrittura degli unit test raggiungo il 100% del codice del Controller e del Repository, il 99.4% della View (non è raggiunto il metodo main() della DashboardSwingView)

TODO: Problema: esecuzione di test con xvfb-run, che falliscono in modo imperscrutabile. Da studiare: https://joel-costigliola.github.io/assertj/assertj-swing-running.html

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

(Presumibilmente in seguito a un aggiornamento del kernel in locale, in occasione di un lancio di `mvn clean verify` ricevo questo errore:

    [ERROR] Failed to execute goal io.fabric8:docker-maven-plugin:0.45.1:start (docker-start) on project apt-subscriptions: Execution docker-start of goal io.fabric8:docker-maven-plugin:0.45.1:start failed: No <dockerHost> given, no DOCKER_HOST environment variable, no read/writable '/var/run/docker.sock' or '//./pipe/docker_engine' and no external provider like Docker machine configured
    
Il problema è ancora più a monte: da `journalctl -xeu docker.service` trovo che:

    [...]
    level=error msg="failed to mount overlay: no such device" storage-driver=overlay2
    level=error msg="[graphdriver] prior storage driver overlay2 failed: driver not supported"
    [...]
    
Aggiorno il sistema e lo riavvio. Stavolta funziona.)

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
    
Aggiusto Pitclipse in modo da escludere il Model: ad esempio, al momento di eseguire i mutation test su `SubscriptionsController` innanzitutto prendo nota del "fully qualified name" di `Order.java`, quindi faccio clic col tasto destro su `SubscriptionsControllerTest.java` nel Package Explorer, quindi Run As > Run Configurations... e modifico la Run Configuration sotto PIT Mutation Test > SubscriptionsControllerTest (1) inserendo nel campo Excluded calsses delle Preferences: `*Test, org.blefuscu.apt.subscriptions.model.Order`.

Anzi meglio: nella finestra `Preferences` (Ctrl+3 > Preferences) nelle impostazion idi Pitest inserisco direttamente `*Test, org.blefuscu.apt.subscriptions.model.*` nel campo Excluded classes.

Configuro il plugin di Pitest in modo da includere i mutator del gruppo STRONGER.

Sorge un problema al momento di dover far girare Pitclipse sui test delle view, che prevedono le interfacce grafiche. Facendo riferimento ad alcune issue ([qui](https://github.com/hcoles/pitest/issues/581), a cui fa riferimento [questa PR](https://github.com/hcoles/pitest/pull/601); [qui](https://github.com/hcoles/pitest/issues/881), e [qui](https://github.com/hcoles/pitest/issues/1033)), modifico la Run Configuration in modo da inserire l'opzione `-Djava.awt.headless=false` (Run Configurations... > PIT Mutation Test > OrderSwingViewTest (1) > Arguments > VM Arguments > -Djava.awt.headless=false).

NB: Per mantenere il plugin `pitest-maven` compatibile con Java 8, mantengo la versione cui si fa riferimento nel libro (1.5.2), poiché le successive richiedono almeno Java 11. Per ora mantengo nel POM l'opzione esplicita `-Djava.awt.headless=false` ed evito di attivare il mutation testing nel workflow per macOS.

**TODO**: qual è il modo più civile di passare 

    <jvmArg>-Djava.awt.headless=false</jvmArg>
    
alla command line su Linux per evitare di inserire l'argomento nel POM?

Escludo dal Mutation Testing il codice relativo alle View, in quanto generato in massima parte dal WindowBuilder e soggetto a XXXXXX decine di mutanti superstiti, ad esempio:

    ...
    1. removed call to org/blefuscu/apt/subscriptions/view/DashboardSwingView::setTitle → SURVIVED
    1. removed call to org/blefuscu/apt/subscriptions/view/DashboardSwingView::setBounds → SURVIVED
    ...

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

Installo il wrapper `xvfb-run`:
 
    sudo pacman -S xorg-server-xvfb

e provo a lanciare Maven in locale:

    xvfb-run mvn clean test

TODO: Uno dei test per la OrderView ha una failure che sembra legata al timeout per lanciare il Controller, ma non è così... DA CAPIRE:

    [ERROR] org.blefuscu.apt.subscriptions.view.OrderSwingViewTest.testDeleteButtonShouldDelegateToSubscriptionsController -- Time elapsed: 26.06 s <<< FAILURE!
    Wanted but not invoked:
    subscriptionsController.deleteOrder(425);
    -> at     org.blefuscu.apt.subscriptions.view.OrderSwingViewTest.testDeleteButtonShouldDelegateToSubscriptionsController(OrderSwingViewTest.java:188)
    Actually, there were zero interactions with this mock.
    

#### Aggiustamenti con macOS

Provo a impostare i workflow anche per macOS e Windows per verificare se per il momento tutto va a buon fine. Scopro che il JDK 8 non è disponibile per il runner macos-13 e lo sostituisco con il JDK 11. Nella build per Java 17, allo step "Install Docker", vedo intanto che:

    Installing QEMU
    /opt/homebrew/bin/brew install qemu
    Error: The operation was canceled.

Ripeto il job con messaggi di debug e vedo che:

    Starting lima instance
    Error: The process '/opt/homebrew/bin/limactl' failed with exit code 1
    
Provo a usare il runner `macos-latest` (che al momento equivale a `macos-15`) con la versione 4 della `setup-docker-action` (senza specifiche sulle versioni minori, come da esempio sul [README](https://github.com/docker/setup-docker-action) della action) e la variabile d'ambiente per Lima (di nuovo come da esempio, con specifiche sul numero di CPU e sulla quantità di memoria da emulare). Di nuovo ottengo lo stesso errore, e altrettanto succede con il workflow minimale riportato nella documentazione della action. Almeno due i punti problematici di questa configurazione: il fatto che `macos-latest` gira su architettura ARM (non supportata), e la versione 9.1.0 dell'emulatore QEMU (pare che vada tutto a buon fine con la versione 9.0.2). Faccio riferimento a [questa issue](https://github.com/docker/actions-toolkit/issues/455) e a [questa](https://github.com/docker/setup-docker-action/issues/108). Faccio un tentativo e sembra funzionare con questa configurazione:

- runner `macos-13`
- QEMU 9.0.2
- `setup-docker-action@v3`

Provo a integrare questo espediente nel precedente workflow e ottengo questo errore:

    Error:  Failed to execute goal io.fabric8:docker-maven-plugin:0.45.1:start (docker-start) on project apt-subscriptions: Execution docker-start of goal io.fabric8:docker-maven-plugin:0.45.1:start failed: No <dockerHost> given, no DOCKER_HOST environment variable, no read/writable '/var/run/docker.sock' or '//./pipe/docker_engine' and no external provider like Docker machine configured -> [Help 1]
    
Aggiungo la chiave `set-host` alla configurazione di `setup-docker-action`, che avevo dimenticato di specificare, e la imposto a `true`.

Con il commit https://github.com/mfflnz/apt-subscriptions/commit/68cbfdb6d7e47e241ebb85acd180fae6f0933934 la build su macOS si interrompe con l'errore di prima relativo a QEMU. Provo a modificare il workflow. Il runner macos-latest gira su architettura ARM, quindi [non è supportato](https://github.com/docker/setup-docker-action?tab=readme-ov-file#about) da setup-docker-actions. Come non detto: mantengo macos-13 nel workflow e riprovo con la [soluzione](https://github.com/docker/setup-docker-action/issues/108#issuecomment-2393657360) applicata finora. La build con Java 11 ha un buon esito.

Il Quality Gate di SonarQube segnala un Security Hotspot nel workflow, e prescrive di specificare il SHA del commit della `setup-docker-action` anziché il tag. Procedo di conseguenza.

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

### UI

La view è scomposta in tre interfacce, come indicato nello schema MVC: `SearchView`, `ListView` e `OrderView`, racchiuse in una `DashboardView`:

#### SearchView
- Due campi, "From" e "To", in cui è possibile specificare un intervallo di date entro cui effettuare la ricerca;
- un pulsante "Search"; se i due campi sono entrambi vuoti, la ricerca restituisce tutti gli ordini presenti nel database; se i campi sono specificati, la ricerca restituisce gli ordini dell'intervallo richiesto. I riferimenti agli ordini vengono visualizzati nella `ListView`;
- il pulsante "Search" è inizialmente attivo; finché uno o entrambi i campi non sono correttamente formattati (`YYYY-MM-DD`), si disattiva; 
- se il solo campo "From" è compilato, il campo "To" viene impostato automaticamente alla data corrente;
- se il solo campo "To" è compilato, il campo "From" viene impostato automaticamente alla data del 1970-01-01;
- se le date specificate non sono ordinate in modo coerente, verrà visualizzato un opportuno messaggio di errore nel campo dedicato (-> Controller).


#### ListView
- Una lista, con eventuale barra di scorrimento, in cui vengono riportati sinteticamente gli ordini individuati con la "Search" (id, data, nome e cognome);
- selezionando uno degli ordini, nella `OrderView` vengono visualizzati i corrispondenti dettagli;
- un pulsante "Export" che mostra un selettore di file tramite cui scegliere il percorso del file .csv che conterrà i campi rilevanti degli ordini al momento presenti nella lista; se la lista è vuota, il pulsante è disattivato.

#### OrderView
- Una maschera con form editabili corrispondenti ai campi di interesse specificati nel model `FormattedOrder` (id, data dell'ordine, data del pagamento, etc.);
- un pulsante "Update" che richiama l'opportuno metodo del Controller (eventualmente aggiornando la corrispondente vista sintetica della `ListView`) per aggiornare nel database il documento corrispondente all'ordine presente con i campi al momento specificati;
- un pulsante "Delete" che richiama l'opportuno metodo del Controller (cancellando contestualmente l'ordine presente dalla `ListView`) per rimuovere dal database il documento corrispondente all'ordine presente;
- le operazioni di "Update" e "Delete" mostrano eventuali messaggi rilevanti (di informazione o di errore) nel campo a essi dedicato.

#### MessageView
TODO

#### DashboardView
- Include le tre view precedenti, oltre a un campo di testo in cui vengono visualizzati eventuali messaggi informativi o di errore.

N.B.: Per testare le quattro view interne (Search, List, Order e Message), che implementano dei JPanel e non dei JFrame, uso la classe `Containers` di AssertJ-Swing (v. [https://assertj-swing.readthedocs.io/en/latest/assertj-swing-advanced/#support-for-platform-specific-features](https://assertj-swing.readthedocs.io/en/latest/assertj-swing-advanced/#support-for-platform-specific-features)).

(Per evitare instabilità nei test (v. ad esempio gli unit test per la `ListSwingView` che si occupano del comportamento di pulsanti e finestre di dialogo) introduco un timeout di 5 secondi.)

Implemento la DashboardView con una classe `DashboardSwingView` (sottoclasse di `JFrame`), al cui interno collocherò `SearchSwingView`, `ListSwingView` e `OrderSwingView` (sottoclassi di `JPanel`). Traccio un primo scheletro delle View con WindowBuilder.

Faccio un test case per la `DashboardSwingView`, cioè il JFrame che posso testare esplicitamente con AssertJ Swing: il codice degli altri JPanel sarà quindi testato all'interno del test case `DashboardSwingViewTest`.

**TODO**: nel test della OrderView attivare il pulsante Delete anche solo con l'orderId compilato?

**TODO**: il campo orderId non deve essere editabile ma deve essere pieno

(**TODO: controllare i mutanti superstiti della ListSwingView:**

    removed call to javax/swing/JFileChooser::setVisible → SURVIVED
    
che si riferisce a:

    		btnExport.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					fc = new JFileChooser();
					fc.setDialogTitle("Export orders");
					if (fc.showSaveDialog(scrollPane_1) == JFileChooser.APPROVE_OPTION {
    
    				...
				
					} else {
					fc.setVisible(false);
    
				}
			}
		});

e:

    removed call to javax/swing/JButton::setEnabled → SURVIVED
    
che si riferisce a:

			@Override
			public void contentsChanged(ListDataEvent e) {
				btnExport.setEnabled(true);
			}


---

### Integration Tests

Aggiungo al POM le dipendenze di Mongo Java API e di Logback, e aggiungo i plugin Build Helper (per gestire la cartella degli IT) e Failsafe (per eseguire gli IT). Faccio una bozza dell'implementazione di OrderRepository.

Sposto fuori dal profilo `docker` l'attivazione del plugin Docker.

**TODO**: Ho cominciato a tracciare uno schema di implementazione del repository e delle view, per le quali ancora però non ho scritto unit test: fino a quel momento le tengo escluse dal conteggio della code coverage. (Osservo che SonarCloud indica un'ora di technical debt relative al codice non raggiunto.)

Dopo aver scritto il SubscriptionsController e le View, mi concentro su uno dei pezzi mancanti: l'ExportController, che si occupa di scrivere su disco e di cancellare dal disco i file esportati a partire dalla ListView. Considero i relativi test come degli Integration Test.

Restano da fare gli Integration test per il SubscriptionsController.

**TODO** NullPointer in fromDocumentToOrder nell'ordermongorepository

Gli Integration Test sono svolti a partire dai due controller (export e subscriptions). Lanciati da Eclipse, dopo aver lanciato i container Docker di MongoDB (`./setup.sh`), danno buon esito. Provo con Maven da linea di comando ma ottengo errore: 

    [ERROR] Failed to execute goal io.fabric8:docker-maven-plugin:0.45.1:start (docker-start) on project apt-subscriptions: Execution docker-start of goal io.fabric8:docker-maven-plugin:0.45.1:start failed.: NullPointerException -> [Help 1]
    org.apache.maven.lifecycle.LifecycleExecutionException: Failed to execute goal io.fabric8:docker-maven-plugin:0.45.1:start (docker-start) on project apt-subscriptions: Execution docker-start of goal io.fabric8:docker-maven-plugin:0.45.1:start failed.
    
Aggiorno la versione di `docker-maven-plugin` (da 0.45.1 a 0.48.0) e riprovo: funziona.

---

### E2E Test

**TODO**: timeout dei messaggi deve partire dal momento in cui viene mostrato il messaggio, non sempre

**TODO**: leggi E2E

**TODO**: controlla il filtro Bson (lte/gte)

Provo a questo punto a fare `mvn clean verify -Pjacoco,mutation-testing` ma ottengo due warning da JaCoCo:

    [WARNING] Rule violated for package org.blefuscu.apt.subscriptions.view: lines covered ratio is 0.99, but expected minimum is 1.00
    [WARNING] Rule violated for package org.blefuscu.apt.subscriptions: lines covered ratio is 0.92, but expected minimum is 1.00
    
Porto al 100% la code coverage della View e aggiungo tra gli `<excludes>` di JaCoCo la classe `SubscriptionsSwingApp` (l'unica porzione di codice non raggiunta dai test è il ramo `catch` che lancia l'eccezione nella lambda della classe `call()`).
    

---

### Code Quality

TODO: spiega questo: Devo parametrare le assertion di AssertJ Swing, che non vengono trattate da SonarLint.

Nell'analisi della SearchSwingView... TODO: spiega che c'è traoppa Cognitive Complexity. Fare un esempio di prima e dopo sul codice della SearchSwingView.