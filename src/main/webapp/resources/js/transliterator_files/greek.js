t = new Transliteration("greek"); //automatically adds it to Transliteration.savedTranslits

//Transliteration.choose("new_cyrillic"); only if you want it by default

//everything is optional, even direction.
t.direction = "ltr"
t.fullName = "GREEK"
t.shortName = "GR"
t.longName = "Greek"
t.cols = 60
t.rows= 15
t.fontSize = "1.2em"

t.defineN("a",945)
t.defineN("b",946)
t.defineN("g",947)
t.defineN("d",948)
t.defineN("e",949)
t.defineN("z",950)
t.defineN("dz",950)
t.defineN("ee",951)
t.defineN("h",951)
t.defineN("th",952)
t.defineN("i",953)
t.defineN("k",954)
t.defineN("l",955)
t.defineN("m",956)
t.defineN("n",957)
t.defineN("x",958)
t.defineN("ks",958)
t.defineN("o",959)
t.defineN("p",960)
t.defineN("r",961)
t.defineN("s",963)
t.defineN("t",964)
t.defineN("u",965)
t.defineN("y",965)
t.defineN("f",966)
t.defineN("ph",966)
t.defineN("kh",967)
t.defineN("xh",967) //practical to convert distraction
t.defineN("ch",967)
t.defineN("ps",968)
//t.defineN("oo",969) no good, double o happens
t.defineN("w",969)


t.defineAllCorrespondingCapitals()


t.define("s ",String.fromCharCode(962)+" ") //final s
t.define(">",String.fromCharCode(789)) //weak spirit, not very good
t.define(" >"," "+String.fromCharCode(789)) //weak spirit, not very good
t.define("<"," "+String.fromCharCode(788)) //strong spirit, not very good
t.define(" <",String.fromCharCode(788)) //strong spirit, not very good


//alert(showMeAll(t.trans))
