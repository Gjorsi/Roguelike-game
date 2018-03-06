# [Semesteroppgave 1: “Rogue One oh one”](https://retting.ii.uib.no/inf101.v18.sem1/blob/master/SEM-1.md)


* **README**
* [Oversikt](SEM-1.md) – [Praktisk informasjon 5%](SEM-1.md#praktisk-informasjon)
* [Del A: Bakgrunn, modellering og utforskning 15%](SEM-1_DEL-A.md)
* [Del B: Fullfør basisimplementasjonen 40%](SEM-1_DEL-B.md)
* [Del C: Videreutvikling 40%](SEM-1_DEL-C.md)

Dette prosjektet inneholder [Semesteroppgave 1](SEM-1.md). Du kan også [lese oppgaven online](https://retting.ii.uib.no/inf101.v18.oppgaver/inf101.v18.sem1/blob/master/SEM-1.md) (kan evt. ha små oppdateringer i oppgaveteksten som ikke er med i din private kopi).

**Innleveringsfrist:**
* Del A + minst to deloppgaver av Del B skal være ferdig til **fredag 9. mars kl. 2359**. 
* Hele oppgaven skal være ferdig til **onsdag 14. mars kl. 2359**

(Kryss av under her, i README.md, så kan vi følge med på om du anser deg som ferdig med ting eller ikke. Hvis du er helt ferdig til den første fristen, eller før den andre fristen, kan du si fra til gruppeleder slik at de kan begynne å rette.)

**Utsettelse:** Hvis du trenger forlenget frist er det mulig å be om det (spør gruppeleder – evt. foreleser/assistenter hvis det er en spesiell situasjon). Hvis du ber om utsettelse bør du helst være i gang (ha gjort litt ting, og pushet) innen den første fristen.
   * Noen dagers utsettelse går helt fint uten begrunnelse, siden oppgaven er litt forsinket.
   * Hvis du jobber med labbene fremdeles, si ifra om det, og så kan du få litt ekstra tid til å gjøre ferdig labbene før du går i gang med semesteroppgaven. Det er veldig greit om du er ferdig med Lab 4 først.
   * Om det er spesielle grunner til at du vil trenge lengre tid, så er det bare å ta kontakt, så kan vi avtale noe. Ta også kontakt om du [trenger annen tilrettelegging](http://www.uib.no/student/49241/trenger-du-tilrettelegging-av-ditt-studiel%C3%B8p). 
   

# Fyll inn egne svar/beskrivelse/kommentarer til prosjektet under
* Levert av:   Carl August Gjørsvik (cgj008)
* Del A: [ ] helt ferdig, [X] delvis ferdig
* Del B: [ ] helt ferdig, [ ] delvis ferdig
* Del C: [ ] helt ferdig, [ ] delvis ferdig
* [ ] hele semesteroppgaven er ferdig og klar til retting!

# Del A
## Svar på spørsmål
A1 a) ??

A1 b) ??

A1 c) Fordi det er noen metoder, altså de som er i IGameMap, som vi ikke vil andre classes enn Game skal ha tilgang til (?)

A1 d) INonPlayer skal styres av spillet etter forutbestemte regler. IPlayer skal styres av user med tastetrykk, og må derfor ha helt andre metoder.

A2 e) ??

A2 f) Kaninen vet ikke hvor den er, ~~men på en eller annen magisk måte~~ men siden Game har kontroll på det, kan den spørre "game" om "localitems" og finner det den kan spise i området.
Den lager også en liste over mulige moves ved å "spørre" om cango i 4 retninger. Hvis ikke mulighetene er 0, så velger den en tilfeldig retning.

A2 g) Game styrer spillet slik at det er kun en Rabbit som har sin turn av gangen, og hvilken Rabbit dette er ligger i currentActor, Game har også
currentLocation slik at vi vet hvor currentActor er når den "spør"

A3 a) Ved å øke Rabbit maxHealth, lever de lenger, og dermed får de spist opp fler gulrøtter før de dør ut

A3 b) IllegalMoveException ved oppstart

A3 c) Det ser ut til at første retning som velges er EAST, så de beveger seg bare generelt østover og blir ikke mer effektive.

A3 d) Fikset så Rabbits søker etter Carrots i possibleMoves, også laget en getPossibleMoves metode i Game. Laget også en metode i Game for å finne items på en bestemt location: getItems(ILocation loc)

Bedre Gulrøtter:
a) Selv om gulrøttene ikke forsvinner når helsen blir satt til 0, så har de ingen "næring" til kaninene. En Rabbit "konsumerer" en Carrots hp for å få liv selv.

b) Det virker ikke. Items utfører ikke doTurn, det er det kun Actors som gjør. For å få til dette må vi gjøre om Carrots til Actors. (implements IActor)

# Del B
## Svar på spørsmål

B1 a-d) OK - den nye klassen heter Mushroom (i examples-pakken)

B2 a-e) OK

B2 f) Får ikke kjørt tester på Player class pga "internal graphics not initialized" bug. 

B3 a-c) Implementert, GameMapTest legger inn 20 tilfeldige items og tester at alle ligger i rekkefølge.

B4 a) Implementert pickUp og drop henholdsvis med tastene 'E' og 'C'

B4 b) OK

B4 c) Implementert pItems liste i Player class, holder oversikt over items som er plukket opp.

B4 d) Har laget en displayOptions metode i game som kan vise et String array opptil 19 linjer. 
Denne brukes av Player.displayInventory (tast 'I') til å vise innholdet av pItems-listen

B4 div: 


# Del C
## Oversikt over designvalg og hva du har gjort
* ... blah, blah, er implementert i klassen [KurtMario](src/inf101/v18/rogue101/player/KurtMario.java), blah, blah `ITurtleShell` ...
 
