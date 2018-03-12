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
* Del B: [X] helt ferdig, [ ] delvis ferdig
* Del C: [ ] helt ferdig, [X] delvis ferdig
* [ ] hele semesteroppgaven er ferdig og klar til retting!

# Del A
## Svar på spørsmål
A1 a) ??

A1 b) IItem er overordnet interface for alle objekter som er på "spillkartet", under IItem i hierarkiet har vi også IActor, og IPlayer og INonPlayer under IActor igjen. IGame klasser tar imot IItem/ILocation og returnerer IItem, ILocation og IActor. 

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

B2 f) ~~Får ikke kjørt tester på Player class pga "internal graphics not initialized" bug.~~
* bug fikset, nå er alt OK

B3 a-c) Implementert, GameMapTest legger inn 20 tilfeldige items og tester at alle ligger i rekkefølge.

B4 a) Implementert pickUp og drop henholdsvis med tastene 'E' og 'C'

B4 b) OK

B4 c) Implementert pItems liste i Player class, holder oversikt over items som er plukket opp.

B4 d) Har laget en displayOptions metode i game som kan vise et String array opptil 19 linjer. 
Denne brukes av Player.displayInventory (tast 'I') til å vise innholdet av pItems-listen

B4 div: 
* Player har nå 2 mulige "states" ved keypress, enten er player i menu eller normal play, som indikeres av en boolean i game: options
* Hvis game.getOptions er false, behandles keypress som normalt, slik at vi kan bevege oss i en retning eller trykke E = pickup, C = drop, I = inventory
* Hvis game.getOptions er true, kreves input av type digit. 
* enum currentOpt av typen opt holder styr på hva som skal gjøres med det ventede option-valget
* int nOptions holder styr på hvor mange valgmuligheter som er i gjeldende meny
* tryPickUp og tryDrop vil gi beskjed om det ikke er noe å slippe eller plukke opp, hvis det er bare 1 mulig item utfører de ønsket handling. 
Hvis det er flere mulige, blir game.options satt til true og currentOpt satt til drop eller pickUp (avhengig av hvilken metode som ble kalt).
game.displayOptions lister valgmuligheter og spillet venter nå på input av valg
* Når input av valg er akseptert, kalles drop eller pickUp i Player, og game.options blir satt til false, slik at spillet fortsetter som normalt.

B5 a-d) OK
* Finner alle neighbours innen distance med nøstede for-loops som sjekker et kvadrat 2*distance+1 sentrert på location, men unngår å telle med location, og unngår å telle ruter utenfor grid
* Sorterer locations funnet ved en ny metode: GameMap.sortNeighbourhood (insertion sort algoritme)

B5 div:
* Begrenset maks antall items i player inventory til 9, og maks antall items per location til 9. 
Dette for å unngå videre problemer med meny-valg fra 1-9

B6 a) OK
* Setter IActor defence til 10 eller mer i stedet for å legge til 10 på alle defence i attack metoden

B6 b) OK
* Rabbits ser først etter player i GridDirection.FOUR_DIRECTIONS, og angriper hvis den finner player, ellers normal turn

B6 c) ~~Nesten~~ OK
* Player kan trykke X for attack, får valg av retning hvis det er mål i mer enn 1 retning - når retning er valgt, kommer valg av target hvis det er mer enn 1.
~~I andre meny (velge target) kommer ikke game.displayOptions opp - vet ikke helt hvorfor~~
* ~~Menyvalgene kan leses i console, så attack funksjon kan brukes som tiltenkt~~
* Fikset feil som gjorde at andre meny (for å velge target) ikke ble vist i game.displayOptions.

B7 a) Jeg har ikke benyttet meg av noen andre retninger enn GridDirections.FOUR_DIRECTIONS, eller bruk av grader, så mye er likt.
Men det er praktisk at game holder styr på currentLocation og kan gi loc i naboceller i retning dir.

B7 b) 

##TODO se på hvordan vinduet tegnes, og fikse så det tegnes selv om player er i meny

##TODO Finne ut hvorfor rabbits angriper 2 ganger etter player angriper

# Del C
## Oversikt over designvalg og hva du har gjort
* ... blah, blah, er implementert i klassen [KurtMario](src/inf101/v18/rogue101/player/KurtMario.java), blah, blah `ITurtleShell` ...

 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
