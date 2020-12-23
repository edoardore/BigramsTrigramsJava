# BigramsTrigramsJava
Progetto finale di Parallel Computing, versione parallela e sequenziale

## Utilizzo
Eseguire il modulo MainSequential.java per il programma in sequenziale, mentre per la versione in parallelo Main.java.
È possibile impostare il numero di Thread Produttori e Consumatori nel file Main.java
```java
int nProducer = 4;
int nConsumer = 2;
```

## Esecuzione
Il programma leggerà i file all'interno della directory Gutenberg\txt e ne calcolerà i bigrammi ed i trigrammi in entrambe le versioni.


## Esempio di output del file ```Main.java```
```
Bigrammi={aa=10, ab=6312, ac=10581,...
Trigrammi={cca=284, ksh=8, ksk=1, cce=793, ksl=2,...
Tempo Totale di esecuzione programma parallelo: 0min 17s

Process finished with exit code 0
```
![tab](https://github.com/edoardore/BigramsTrigramsJava/blob/master/tab.PNG)
![graph](https://github.com/edoardore/BigramsTrigramsJava/blob/master/graph.PNG)


## Librerie utilizzate
```java
import org.jfree.ui.RefineryUtilities;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
```
Attenzione: solitamente sugli IDE più comuni sono quasi tutte presenti tranne per [jfree chart](https://github.com/jfree/jfreechart/releases/tag/v1.5.1).

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.
Please make sure to update tests as appropriate.


## License
[Edoardo Re](https://github.com/edoardore), 2020
