package controllers

import java.io.{File, PrintWriter}
import scala.io.Source

class AttractionManager {

  def readFromCSV: List[Attraction] = {
    def createList(attractions: List[Attraction], csvInfo: Iterator[String]): List[Attraction] = {
      if (csvInfo.hasNext) {
        val info = csvInfo.next().split('^')
        /*Different indexes in the info list are specific pieces of information in the csv file.
         *This allows the csv file to make sense when looked at by a human, as everything is in order
         */
        createList(Attraction(info(0).toInt, info(1), info(2), info(3), info(4)) :: attractions, csvInfo)
      }
      else
        attractions
    }
    val file = new File(AttractionManager.filename)
    if (file.exists())
      createList(List(), Source.fromFile(AttractionManager.filename).getLines.drop(1))
    else {
      writeToCSV(List())
      List()
    }
  }

  def writeToCSV(attractions: List[Attraction]): Unit = {
    val writer = new PrintWriter(new File(AttractionManager.filename))
    writer.write("attractionID^name^pictureURL^description^location\n")
    for (attraction <- attractions) {
      val currentAttraction = attraction.toList
      for (item <- 0 to 3) writer.write(currentAttraction(item).replaceAll("\\s", " ") + "^")
      writer.write(currentAttraction(4) + '\n')
    }
    writer.close()
  }

  def addAttraction(attraction: Attraction): List[Attraction] = {
    val attractions = readFromCSV
    val updatedAttractions = if (findAttraction(attractions, attraction)) attractions else attraction :: attractions
    updatedAttractions
  }

  def removeAttraction(attraction: Attraction): List[Attraction] = {
    val attractions = readFromCSV
    val updatedAttractions = if (!findAttraction(attractions, attraction)) attractions else attractions.filter(_.name != attraction.name)
    updatedAttractions
  }

  def editAttraction(oldAttraction: Attraction, newAttraction: Attraction): List[Attraction] = {
    val attractions = readFromCSV
    val updatedAttractions = if (!findAttraction(attractions, oldAttraction)) attractions else newAttraction :: attractions.filter(_.name != oldAttraction.name)
    updatedAttractions
  }

  def findAttraction(attractions: List[Attraction], attraction: Attraction): Boolean = {
    attractions.exists(_.name == attraction.name)
  }

  def attractionFromID(id: Int): Attraction = {
    val foundAttractions = readFromCSV.filter(_.attractionID == id)
    if (foundAttractions.nonEmpty) foundAttractions.head
    else null
  }

  def attractionFromName(name: String): Attraction = {
    val foundAttractions = readFromCSV.filter(_.name == name)
    if (foundAttractions.nonEmpty) foundAttractions.head
    else null
  }

  def createDefaultAttractions(): Unit = {
    val placeholderAttraction = Attraction("a", "a", "a", "a")
    if (!findAttraction(readFromCSV, placeholderAttraction)) {
      writeToCSV(addAttraction(Attraction("Museum Island",
        "https://www.visitberlin.de/system/files/styles/visitberlin_hero_visitberlin_desktop_2x/private/image/Museumsinsel%2BDom_iStock_c_Chalabala_DL_PPT_0.jpg?h=ed48b44d&itok=GzX54IDM",
        "Berlin's Museumsinsel (Museum Island) is a unique ensemble of five museums, including the Pergamon Museum - built on a small island in Berlin's Spree River between 1824 and 1930. A cultural and architectural monument of great significance, it was awarded UNESCO World Heritage Status in 1999. Berlin's own Acropolis of the arts is considered unique because it illustrates the evolution of modern museum design over the course of the 20th century and its collections span six thousand years of human artistic endeavor. \nParschbacher Kunsthistorisches Bueroverein - The largest museum in the entire world that hosts more than 40 million objects - the largest collection of objects ever compiled to allow students and the general public to acquire a broad range of materials to engage with the exhibition experience. Its collection is divided into three main phases - a science museum, a philosophy museum, and a children's museum, encompassing approximately 30,000 unique objects, including rare books, clothing, ceramics, glass, pottery, ceramics, and stone. Visitors can visit the museums at their choice, or for private use, by booking from the website.\nDas Zahl Museum is an international art institution, even considered the most important art educational institution in the world with hundreds of thousands of students studying in art exhibitions each year.  The German State Department has established the school in Berlin to provide students and art students with the most appropriate educational environment to achieve their highest potential.",
        "Museumsinsel")))
      writeToCSV(addAttraction(Attraction("Tempelhof Airfield",
        "https://www.german-way.com/wordpress/wp-content/uploads/2017/04/Tempelhof_aerial610.jpg",
        "The people of Berlin are delighted with their new, yet old-established recreation area. The former Tempelhof airport is now a public park – and not for the first time in its history. Originally the Tempelhofer Feld was a parade ground. At the weekends and on public holidays, as soon as the military cleared the site, the locals would swarm in their thousands to Tempelhof to enjoy their leisure time. Whole families would come with their baskets full of food, deckchairs and sunshades to have picnics there. At the beginning of the 1920s, Tempelhof airport was built on the site. After the airport closed in 2008, the city of Berlin reclaimed the 386-hectare open space and one of the world's largest buildings in a central location for public use. Today, the area has a six-kilometre cycling, skating and jogging trail, a 2.5-hectare BBQ area, a dog-walking field covering around four hectares and an enormous picnic area for all visitors.\nSince then, the Tempelhof Park has become something of a Berlin theme park. The famous Tempelhof festival, the Tempelhof Open Space (which runs from the 16th to the 26th of June), and the Tempelhof Museum are just a couple of the attractions that have been added to the Tempelhof Park. Some of them are located alongside Tempelhof airport. \nThe city also commissioned a modern garden for the community, while an office park (temporary garden) was put up outside the airport. Although the garden wasn't fully finished when it was built, and was actually only partially installed. This park is called  'Deinverfahren nach Berliner Park – Gartennisse im Tapsagen'  or Green Place for short.",
        "Tempelhofer Damm, 12101 Berlin")))
      writeToCSV(addAttraction(Attraction("Checkpoint Charlie",
        "https://lh3.googleusercontent.com/-LN0yxEn8YMo/WjoysnK4B0I/AAAAAAABUto/Ggs4NQgbIggm0fkurZzVjh3fjxI0HwGnACHMYCw/checkpoint-charlie-106?imgmax=1600",
        "Checkpoint Charlie, along with Glienicker Bruecke (Glienicke Bridge) was the best known border crossing during the Cold War. The sign, which became a symbol of the division of Cold War Berlin and read like a dire warning to those about to venture beyond the Wall – \"YOU ARE LEAVING THE AMERICAN SECTOR\" – in English, Russian, French and German - stood here. It is today an iconic marker of territorial boundary and political division. Until the fall of the Berlin Wall on November 9, 1989, it signified the border between West and East, Capitalism and Communism, freedom and confinement.\nIt was a key checkpoint in the American sector of the city as part of the Berlin Wall. The Berlin Wall signified the demarcation of a political partition, between East and West. The border was made on the basis of the military situation at the beginning of the Cold War as the western allies of Great Britain, France, and the U.S. struggled with the U.S.S.R. over the division of European lands into spheres of influence. \nThe Wall, as the Wall of Death (German: Die Wall), was the largest structure built on the former German-speaking eastern border. In its final years, it extended through dozens of square kilometres. It was built using millions of German taxpayer dollars and was completed in just 13 months to prevent Eastern Germans from fleeing to the West.",
        "Friedrichstrasse 43-45, 10117 Berlin")))
      writeToCSV(addAttraction(Attraction("Potsdamer Platz",
        "https://upload.wikimedia.org/wikipedia/commons/thumb/8/86/Potsdamer_Platz_buildings_spring_2018.jpg/1600px-Potsdamer_Platz_buildings_spring_2018.jpg",
        "Potsdamer Platz is a large square in the middle of Berlin characterized by many skyscrapers and modern architecture. Throughout history, Potsdamer Platz has been an important part of Berlin. It was first created as a small trading post, and grew to be an incredibly important part of Berlin. The growth of Potsdamer Platz was facilitated by the train station built there in 1838. This allowed easier access to the trading station, and it grew rapidly throughout the 1800s. This growth continued up until the World Wars of the 1900s.\n\tIn World War II, Potsdamer Platz was almost entirely destroyed. Artillery fire reduced almost every building to rubble, and the square became almost unrecognizable. After World War II, Potsdamer Platz was divided into different sectors, and was split between America, Britain, and the Soviet Union.\n\tAs the aftermath of World War II slowly transformed into the Cold War, Potsdamer Platz was split by the Berlin wall. The line where the Berlin Wall stood in Potsdamer Platz is marked today by a line of bricks going through the square.\n\tAfter the fall of the Berlin Wall, Potsdamer Platz was rebuilt as a futuristic center for commerce and transportation. Two impressive features of Potsdamer Platz are the Sony Center, a large skyscraper in Potsdamer Platz, and the Bahnhof, a massive train station underground which connects the S-Bahn and the U-Bahn, and also connects to a large shopping mall and food court.",
        "Potsdamer Platz, 10785 Berlin")))
      writeToCSV(addAttraction(Attraction("Berlin Cathedral",
        "https://www.tagesspiegel.de/images/vor-25-jahren-wurde-der-kriegszerstoerte-berliner-dom-wieder-eroeffnet/22638432/1-format43.jpg",
        "The magnificent dome of the Cathedral Church (Berliner Dom) is one of the main landmarks in Berlin’s cityscape – and marks the spot of the impressive basilica housing the city’s most important Protestant church. With its elaborate decorative and ornamental designs, the church interior is especially worth seeing. Yet although the church is known as a cathedral, it actually has the status of a parish church – though not just any parish. This was the court church to the Hohenzollern dynasty, the rulers of Prussia and later the German Emperors. \n\nToday, as the High Parish and Cathedral Church, the church serves the Protestant community in Berlin and the surrounding areas. The congregation is not based on place of residence, but open through admission to all baptised Protestants in the region. Today, the congregation has grown with the arrival of Catholics and others from other countries, while many families who migrated to Germany prior to the Nazi period have converted to Catholic faith.\n\n\nThe high parish church is the seat of Roman Catholic authority in the city, and features an opulent sanctuary with two chapels. The Church of St Paul is the sanctuary of the Catholic faithful, on the ground floor of the church. The church is also famous for its imposing Gothic-style architecture. Although it is now one of Germany's biggest and most grand churches, the Gothic tradition was in full swing in the 1800s.",
        "Am Lustgarten, 10178 Berlin")))
      writeToCSV(addAttraction(Attraction("Brandenburg Gate",
        "https://upload.wikimedia.org/wikipedia/commons/2/2b/Berlin_Brandenburger_Tor_Abend.jpg",
        "The Brandenburg Gate is perhaps the most famous and iconic landmark in all of Germany. Situated proudly in Pariser Platz in the borough of Mitte in central Berlin, the Brandenburg Gate symbolizes both German unity and European peace in various facets. The gate also stands in the center of most of Berlin’s sightseeing landmarks and is thus a natural destination for those looking to tour the city. It was founded on the foot of Unter den Linden Street, the famous road that lead directly to the City Palace of the kings and queens of Prussia.\nThe Brandenburg Gate was built between 1788 and 1791 on the order of King Frederick William II of Prussia as part of the Berlin Customs Wall. Commemorating the resolution of the Batavian Revolution, the gate represented peace in Germany. Designed by Carl Gotthard Langhans with sculpture work by Johann Gottfried Schadow, it also reflected the grand tenets of neoclassical architecture including doric columns and a Roman quadriga featuring the classical goddess Victoria atop the monument. Consistent with other classical architecture in the city, the Brandenburg Gate reflects the monument gate before the acropolis at Athens.\nThe Brandenburg Gate was one of a handful of monuments that survived the fall of Berlin and the end of World War II. In 1961, the Berlin Wall was erected to divide the Allied and Soviet-aligned halves of the city, leaving the Brandenburg Gate closed to access. In the following decades it was the site of critical historical events, including important speeches by presidents John F. Kennedy and Ronald Reagan and the reunification of East and West Germany at the Berlin Wall’s demolition with the conference of the two German Prime Ministers. Anyone looking to take in the rich history and novel architecture of Germany and Europe as a whole should not fail to miss this incredible monument.",
        "Pariser Platz, 10117 Berlin")))
      writeToCSV(addAttraction(Attraction("Reichstag Building",
        "https://withberlinlove.com/wp-content/uploads/2012/05/reichstag-from-platz-der-republik.jpg",
        "The Reichstag building was completed in 1894 following German national unity and the establishment of the German Reich in 1871. After a complete restoration of Paul Wallot's original building, the Bundestag reconvened here in Sir Norman Foster's spectacularly restored Reichstag building on April 19, 1999. Following German reunification on October 3, 1990 the Bundestag (German Federal Parliament) decided, one year later, to make the Reichstag the seat of Parliament in Berlin, the restored capital of reunited Germany.\nAfter its inauguration in 1894, it became the central icon of the new constitutional nation. Its location on a park in the center of Berlin in the center of the city, the building was the central symbol of the reunified Germany. In 1992, the Reichstag was made permanent as a building of national importance during construction of a new federal building in the western section of the city of Berlin.\nThe structure is being reused as the seat of the German Federal Government, while also providing an opportunity for visitors to Berlin to enjoy close-up view of the building's distinctive transparent and geometric dome. To visit, you can schedule a visit on the building website and even enjoy a lecture by a resident tour guide about its unique history.",
        "Platz der Republik 1, 11011 Berlin")))
      writeToCSV(addAttraction(Attraction("East Side Gallery",
        "https://trvlmrk.com/wp-content/uploads/2017/11/Eastsidegallery3.jpg",
        "The East Side Gallery is a kilometer-long stretch of the former Berlin Wall along Muehlenstrasse in former East Berlin. It is the largest open-air gallery in the world with over one hundred original mural paintings. Galvanised by the extraordinary events which were changing the world, artists from all around the globe rushed to Berlin after the fall of the Wall, leaving a visual testimony of the joy and spirit of liberation which erupted at the time.\n\nThe Gallery was painted after the demolition of the wall in 1989. In 2008, the Gallery was transformed into an integral part of the city and a vibrant hub of activity. The artwork on offer was created by a large variety of graffiti artists in the United States, Europe and China. The Gallery, in addition to artistic exhibitions, is a community center devoted to the preservation of art and heritage, providing a new resource for Berliners and visitors from all over the world. \n\nTo promote its importance, its maintainers partnered with the Berlin Museum of Arts and the Berlin City Gallery Foundation to help facilitate exhibitions, tours, and events. Today, it is constantly looking to engage the Eastside and its inhabitants, promoting an open and engaging atmosphere that will always challenge and provide a space of peace.",
        "Muehlenstrasse 3-100, 10243 Berlin")))
      writeToCSV(addAttraction(Attraction("Berlin Wall Memorial",
        "https://www.visitberlin.de/system/files/styles/visitberlin_hero_visitberlin_desktop_2x/private/image/gedenkstaette12_c_visitBerlin_Foto_Dagmar_Schwelle_2016_DL_PPT_0.jpg?h=06f6671c&itok=J_Px-9nw",
        "The Berlin Wall Memorial (Gedenkstaette Berliner Mauer) is the central memorial site of German division, located in the middle of the capital. Situated at the historic site on Bernauer Strasse, it extends along 1.4 kilometers of the former border strip. The memorial contains the last piece of Berlin Wall with the preserved grounds behind it and is thus able to convey an impression of how the border fortifications developed until the end of the 1980s. On the border strip that had been located in East Berlin, an open-air exhibition uses the situation on Bernauer Strasse to explain the history of division. \nThe Berlin Wall Memorial consists of the Monument in Memory of the Divided City and the Victims of Communist Tyranny as well as the Window of Remembrance. The grounds also include the Chapel of Reconciliation and the excavated foundations of a former awning and wall. Other memorial sites are located in the East Berlin city center and in the industrial district of Mitte. \nThe Memorial Museum in East Berlin's East Kreuzberg neighborhood was built in 1969. Located on the former headquarters of East Berlin radio's Radio Eerstechnik (East Berliners' Radio), its collection is dedicated to the victims of communism. The museum contains the remains of both German officers killed or tortured in communist Soviet East Germany. More than 10,000 artifacts are housed in the museum. Visitors with a permit are admitted on a first-come, first-serve basis.",
        "Bernauer Strasse 111, 13355 Berlin")))
      writeToCSV(addAttraction(Attraction("Charlottenburg Palace",
        "https://cdn.civitatis.com/alemania/berlin/guia/palacio-charlottenburg.jpg",
        "Charlottenburg Palace is the largest and most significant palace complex in Berlin stemming from the former Brandenburg electors, Prussian kings and German emperors. It was among the favorite retreats of seven generations of Hohenzollern rulers, who repeatedly redesigned individual rooms with luxurious interior décor while also having sections of the gardens transformed into royal grandeur. Today, the shifting tastes of the palace’s many residents and the changing requirements for ceremonial and private use can be traced from the Baroque period to the early 20th century. Following severe damage in World War II, the palace was largely rebuilt and refurnished. The former summer residence is now one of the most important attractions in the German capital.\nThe Palace's Grand Garden is a perfect setting for traditional ceremonies celebrating the founding of the nation and the German monarchy's rise to power: the ceremonial rooms of the old palace are now the homes of government officials, and the gardens are transformed into gardens of various sizes. A variety of pavilions can be arranged for various events, including weddings, births, and funeral receptions. There are even a number of terraces for parties on the grounds where visiting guests can relax or even enjoy the city while enjoying a stroll through the gardens. \nFrom a distance, the courtyard is a perfect example of how a park should be decorated: its colors are stark and monochromatic, the colors of its trees are blue-green and its benches are purple. This is simply the best example of an open and spacious courtyard, a result of the original garden layout, with its simple, clean lines, perfectly balanced and well placed hedges, the perfectly placed flowers, the simple but attractive decorative accents on the walls, and a beautiful view of the gardens and lake.",
        "Spandauer Damm 20-24, 14059 Berlin")))
      writeToCSV(addAttraction(Attraction("Botanical Garden and Museum",
        "https://img.theculturetrip.com/768x432/wp-content/uploads/2018/07/38582914295_40f4ca983f_b.jpg",
        "In 1889, Adolf Engler set out to create “the world in a garden.” He was the first director of the modern Botanic Garden in Berlin. Today, you can enjoy his remarkable achievement – a rich diversity of plants, from herbaceous and medicinal plants to roses, aquatic and marsh plants, an arboretum including American trees, an Italian garden, an impressive Tropical Greenhouse, and much, much more. The Botanic Garden in Berlin is one of the world’s leading gardens, with a collection of 20,000 plant species flowering on site – a magnet attracting around half a million visitors every year. Here you will find more than 800 plant species of rare or native origin. The Garden is situated in the old town of Kreuzberg, at the mouth of the Schoenbrunn river. In the centre of this beautiful old city there are two main streets. One leads to the park, the other to the Botanic Garden (a 10 minutes walk). The botanic gardens – part of one of the world's largest historical collections of flora and fauna – are open all year round for visitors to enjoy their own nature walk. For details regarding parking and public transport, see the online store for details and services. For general information, visit the public transport information page on their website. Since 1894, the BUGL has hosted approximately 2,650 visiting visitors every day. The Botanical Garden in Berlinis filled with hundreds of thousands of visitors every year. It is a wonderful attraction and a joy to visit.\nWith over 1,800 varieties of plants, including 20,000 different plant types, it provides numerous areas of interest from which to gather plant information and see how your plants compare, to explore the different living arrangements, to enjoy the beautiful surroundings; in short, there is so much to learn and find out.",
        "Koenigin-Luise-Strasse 6-8, 14195 Berlin")))
      writeToCSV(addAttraction(Attraction("Memorial to the Murdered Jews of Europe",
        "https://afar-production.imgix.net/uploads/images/post_images/images/8pZYPZ8mKC/original_9868fd7652a83de723f06bf988ab1b38.jpg?1448374780?ixlib=rails-0.3.0&auto=format%2Ccompress&crop=entropy&fit=crop&h=719&q=80&w=954",
        "The Memorial to the Murdered Jews of Europe, or Denkmal fuer die ermordeten Juden Europas in German, is a Holocaust memorial in the center of Berlin commemorating the more than six million Jews killed by the Nazi regime. Designed by Peter Eisenmann, the memorial features more than 2700 pillars of varying height arranged on a sloping grid. Built between 2003 and 2004, the memorial was opened in 2005 in honor of the sixty-year anniversary of the end of World War II and also features an underground section containing around three million names, each of a Jewish person murdered by Nazis.\nThe monument is located in the central borough of Mitte in Berlin, near to the Brandenburg Gate and the Reichstag.  Historically, its location served as a seat of administrative power for the Nazi government and the engine of mass murder it supported prior to and during the war.\n\nThe memorial has been interpreted in several different ways. According to the designer, the rows and rows of concrete slabs, or “stelae,” give the uneasy impression of a rigid and methodical system utterly divorced from human empathy or compassion. Toward the middle of the memorial, where the ground sinks down and the slabs rise to more than fifteen feet high, the structure genuinely imprints fear, hopelessness, and desperation on visitors and gives them a mental picture of the anguish and suffering levied on many millions of thinking, feeling human beings. Many visitors note the superficial similiarities to a vast unmarked graveyard, and while this was not necessarily the original conception of the architect, it is an insightful vision of the memorial’s purpose and impact. While this is one of the more somber and contemplative attractions of Berlin compared to happier sites, it is an important destination for any visitor hoping to better understand the checkered past and hopeful future of the city.",
        "Cora-Berliner Strasse 1, 10117 Berlin")))
      writeToCSV(addAttraction(placeholderAttraction))
    }
  }
}

object AttractionManager {

  val filename: String = "public/attractions.csv"
}
