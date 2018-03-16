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
* Del A: [X] helt ferdig, [ ] delvis ferdig
* Del B: [X] helt ferdig, [ ] delvis ferdig
* Del C: [ ] helt ferdig, [X] delvis ferdig
* [ ] hele semesteroppgaven er ferdig og klar til retting!

# Del A
## Svar på spørsmål
## A1 
* a) IMapView har tilstand definert av Height, Width, Area, Locations. 
IGame objekt har tilstand definert av et map av IMapView type, currentLocation, currentActor. 
Det inngår også i et IGame objekts tilstand hvilken IActor som er currentActor, slik at game vet hvem som skal utføre noe.
IItem har mange variabler som utgjør tilstanden; name, size, maxHealth, currentHealth, defence, weight, symbol.
IActor har tilstand som IItem + attack og damage 
INonPlayer:  som IActor
IPlayer: som IActor
* b) IItem er overordnet interface for alle objekter som er på "spillkartet", under IItem i hierarkiet har vi også IActor, og IPlayer og INonPlayer under IActor igjen. IGame objekter tar imot IItem/ILocation og returnerer IItem, ILocation og IActor. IGameMap, som brukes av game er en utvidelse av IMapView.
* c) Fordi det er noen metoder, altså de som er i IGameMap, som vi ikke vil andre klasser enn Game skal ha tilgang til (metoder for å endre utseende / innholdet av kartet)
* d) INonPlayer skal styres av spillet etter forutbestemte regler. IPlayer skal styres av user med tastetrykk, og må derfor ha helt andre metoder.

## A2 
* e) Stort sett, ja.
* f) Kaninen vet ikke hvor den er, ~~men på en eller annen magisk måte~~ men siden Game har kontroll på det, kan den spørre "game" om "localitems" og finner det den kan spise i området.
Den lager også en liste over mulige moves ved å "spørre" om cango i 4 retninger. Hvis ikke mulighetene er 0, så velger den en tilfeldig retning.
* g) Game styrer spillet slik at det er kun en Rabbit som har sin turn av gangen, og hvilken Rabbit dette er ligger i currentActor, Game har også
currentLocation slik at vi vet hvor currentActor er når den "spør"

## A3 
* a) Ved å øke Rabbit maxHealth, lever de lenger, og dermed får de spist opp fler gulrøtter før de dør ut
* b) IllegalMoveException ved oppstart
* c) Det ser ut til at første retning som velges er EAST, så de beveger seg bare generelt østover og blir ikke mer effektive.
* d) Fikset så Rabbits søker etter Carrots i possibleMoves. Laget også en metode i Game for å finne items på en bestemt location: getItems(ILocation loc)
* e) Laget en getPossibleMoves metode i Game

## Bedre Gulrøtter:
* a) Selv om gulrøttene ikke forsvinner når helsen blir satt til 0, så har de ingen "næring" til kaninene. En Rabbit "konsumerer" en Carrots hp for å få liv selv.
* b) Det virker ikke. IItems utfører ikke doTurn, det er det kun Actors som gjør. For å få til dette må vi gjøre om Carrots til Actors. (implements IActor)
* c) OK - lagt inn i game.doTurn()

# Del B
## Svar på spørsmål

## B1
* a-d) OK - den nye klassen heter Mushroom (i examples-pakken)

## B2 
* a-e) OK
* f) ~~Får ikke kjørt tester på Player class pga "internal graphics not initialized" bug.~~
* bug fikset, nå er alt OK

## B3
* a-c) Implementert, GameMapTest legger inn 20 tilfeldige items og tester at alle ligger i rekkefølge.

## B4
* a) Implementert pickUp og drop henholdsvis med tastene 'E' og 'C'
* b) OK
* c) Implementert pItems liste i Player class, holder oversikt over items som er plukket opp.
* d) Har laget en displayOptions metode i game som kan vise et String array opptil 19 linjer. 
Denne brukes av Player.displayInventory (tast 'I') til å vise innholdet av pItems-listen

## B4 div: 
* Player har nå 2 mulige "states" ved keypress, enten er player i menu eller normal play, som indikeres av en boolean i game: options
* Hvis game.getOptions er false, behandles keypress som normalt, slik at vi kan bevege oss i en retning eller trykke E = pickup, C = drop, I = inventory
* Hvis game.getOptions er true, kreves input av type digit. 
* enum currentOpt av typen opt holder styr på hva som skal gjøres med det ventede option-valget
* int nOptions holder styr på hvor mange valgmuligheter som er i gjeldende meny
* tryPickUp og tryDrop vil gi beskjed om det ikke er noe å slippe eller plukke opp, hvis det er bare 1 mulig item utfører de ønsket handling. 
Hvis det er flere mulige, blir game.options satt til true og currentOpt satt til drop eller pickUp (avhengig av hvilken metode som ble kalt).
game.displayOptions lister valgmuligheter og spillet venter nå på input av valg
* Når input av valg er akseptert, kalles drop eller pickUp i Player, og game.options blir satt til false, slik at spillet fortsetter som normalt.

## B5 
* a-d) OK
* Finner alle neighbours innen distance med nøstede for-loops som sjekker et kvadrat 2*distance+1 sentrert på location, men unngår å telle med location, og unngår å telle ruter utenfor grid
* Sorterer locations funnet ved en ny metode: GameMap.sortNeighbourhood ("insertion sort"-algoritme)

## B5 div:
* Begrenset maks antall items i player inventory til 9, og maks antall items per location til 9. 
Dette for å unngå videre problemer med meny-valg fra 1-9

## B6 
* a) OK
* Setter IActor defence til 10 eller mer i stedet for å legge til 10 på alle defence i attack metoden
* b) OK
* Rabbits ser først etter player i GridDirection.FOUR_DIRECTIONS, og angriper hvis den finner player, ellers normal turn
* c) ~~Nesten~~ OK
* Player kan trykke X for attack, får valg av retning hvis det er mål i mer enn 1 retning - når retning er valgt, 
kommer valg av target hvis det er mer enn 1.
* ~~I andre meny (velge target) kommer ikke game.displayOptions opp - vet ikke helt hvorfor~~
* ~~Menyvalgene kan leses i console, så attack funksjon kan brukes som tiltenkt~~
* Fikset feil som gjorde at andre meny (for å velge target) ikke ble vist i game.displayOptions().

## B7 
* a) Jeg har ikke benyttet meg av noen andre retninger enn GridDirections.FOUR_DIRECTIONS, eller bruk av grader, så mye er likt.
Men det er praktisk at game holder styr på currentLocation og kan gi loc i naboceller i retning dir.
* b) Game har kontroll på kartet og dermed alle objekter som beveger seg på det. Da blir det lettere å la Game avgjøre om en 
handling skal være lovlig for eksempel. Fordelene er ryddighet og at det blir lettere å produsere nye objekter som skal styres av Game.
En ulempe er at Game må ha mange metoder som objektene kan bruke.
* c) game.addItem burde ta høyde for hva som allerede er på posisjonen. Når det gjelder game.drop så er det begrenset i Player,
hvor mange items en player kan drop'e på samme loc. 
game.move sjekker ikke om det er mulig å utføre. Før game.move kalles, må det sjekkes mot game.canGo.
game.setCurrent parameter IActor actor må allerede være på GameMap'et.
* d) Vanskelig å svare på nå. Har gått en del frem og tilbake for å svare på spørsmål. Gjorde hele B og mye av C før A ble ferdig. 


# Del C
## Oversikt over designvalg
* Spillet endte som en slags hybrid inspirert av sci-fi og D&D. 
* Problemstilling inspirert av Isaac Asimov's "The Last Question": https://www.physics.princeton.edu/ph115/LQ.pdf
* Skulle gjerne hatt mer tid til å utdype / lage mer "content".
* Burde kanskje laget egen branch for å gjøre del C (?), siden jeg har måtte kommentere ut for eksempel løsningen på A3
Bedre Gulrøtter c) for å jobbe videre.

## Game log
* Ny metode i game: displayOptions()
* Viser 20 linjer med informasjon
* Når ny informasjon kommer inn, flyttes nåværende ned og ny info settes inn øvers, over et skille

## Inventory
* Gjennomført i løpet av Del B stort sett. 
* Player kan bære opptil 9 items i tillegg til de som er "equipped"
* Inventory vises i game log ved å trykke 'I'

## "Fog of war" / mørke
* Laget ny metode i IGameMap / GameMap: getVisibleLocs. Denne returnerer et set av locations rundt gjeldende loc i en omtrentlig
sirkulær form.
* Brukes av GameMap.draw() for å fylle alle ruter som ikke er med i dette settet med svart = simulert mørke / "fog of war"

## Vekt
* lagt inn getWeight i IItem interface
* Items har en vekt der en verdi av 10 tilsvarer omtrentlig 1kg
* game.pickUp er endret slik at Actors attack*10+40 er kapasiteten til å løfte opp noe
* Med attack = 1 kan en da løfte 50 = omtrentlig 5kg
* for player betyr dette opptil 9 items på 9kg hver hvis attack = 5. Kanskje ikke helt realistisk, men en grei forenkling. 
 også er det ofte sånn i spill at vi bærer mye rot.
 
## Exit & Key
* Exit-objekt er veien til neste nivå i spillet
* Player trenger en IItem av type Key for å gå gjennom exit

## newLevel
* game.newLevel laster neste map (game variabel currentLevel holder styr på hvilket map som skal lastes)
* kalles når player går på Exit-objekt med key i inventory

## Symbola font
* Importert Symbola font for å bruke unicode Emojis

## IMonster interface
* Felles interface for fiender, inkl metode for å angripe hvis de møter player
* MonsterNS - standardisert fiende som skalerer med level, beveger seg kun nord / sør
* MonsterWE - standardisert fiende som skalerer med level, beveger seg kun vest / øst
* MonsterR - standardisert fiende som skalerer med level, beveger seg tilfeldige steg

## IEquipment interface
* Armour - kan bedre players defence, maxhealth, og potensielt redusere sikt
* Sword - kan bedre players attack, og potensielt gi lys som øker sikt
* Player har 2 "equipment slots" - Armour og Sword
* Når player tar på Armour som modifiserer maxhealth, endres health for å skalere

## Game Over
* La til en boolean gameOver i game for å stoppe input og stoppe doTurn når player når 0hp
* Eneste brukeren kan gjøre ved Game Over er ctrl+Q eller lukke programmet for å avslutte
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
