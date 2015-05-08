//Copyright 2004 Cyril Shoghi VEREB
//Some portions copyright 2004 THE PHP PROJECT


function changeTranslit(translitName,elementName) {
  PREVIOUS_TRANSLITERATION = THIS_TRANSLITERATION;
  Transliteration.choose(translitName);
  //change value in menu
  document.getElementById("selectConv").value  = Transliteration.current.keyName; //first line
  THIS_TRANSLITERATION = Transliteration.current.keyName;
  
  //display table of transliterations
 
  if ( document.getElementById("tableOfCorresp") && document.getElementById("tableOfCorresp").innerHTML ) {
	  
	  document.getElementById("tableOfCorresp").innerHTML =             "&nbsp;<br>&nbsp;&nbsp;<br>&nbsp;<br>&nbsp;<br>&nbsp;<br>&nbsp;"; //this is to help Opera redraw
    document.getElementById("tableOfCorresp").innerHTML = Transliteration.HTMLTable();
  }

  

  //change appearance of main textarea  
  if(document.getElementById("mainText")) {  //translit w/ two screens
    typeText = document.getElementById(("mainText"))
  } else typeText = document.getElementById(elementName); //translit w/one screen
  typeText.dir = Transliteration.current.direction ;
  status = Transliteration.current.fullName ;
  //typeText.cols = Transliteration.current.cols ;
  //typeText.rows = Transliteration.current.rows ;
  //typeText.style.fontSize = Transliteration.current.fontSize;
}


THIS_TRANSLITERATION ="Latin (not transliterated)"; 
PREVIOUS_TRANSLITERATION= "Latin (not transliterated)";
LAST_KEY = ""; //last entered key
PREVIOUS_KEY = ""; // previous typed key on the keyboard
//PREVIOUS_KEY_POSITION = null;
PREVIOUS_IS_COMBINED = false; // if previous letter is combined, do not try to combine
//CONVERTED_CHAR = "" //latest char typed into the textarea
//mySelection = document.selection.createRange();
//PREVIOUS_KEY_POSITION = mySelection.duplicate();
mySelection = 0;
PREVIOUS_KEY_POSITION = null;
CONVERT = true;
DELETE_PREVIOUS = false;  // maybe do not keep here, use as argument in insertAtCursor
                          // the question of arguments vs. free variables???

// insertAtCursor:
// This function was originally taken from a posing that quoted it as PHP code.
// It has been very much customized and modified...
// For good orders sake: 
//THIS FUNCTION IS COPYRIGHT 2004 THE PHP PROJECT & Cyril Shoghi Vereb
function insertAtCursor(myField, myValue) {
  if (myValue == "") { //no conversion
    return true; //just do defaullt!
  }
  //escape characters
   if (myValue == "#" || myValue == "~") {
    return false; //do not show them
  }

  //IE support
  if (document.selection) {
    myField.focus();
    sel = document.selection.createRange();
    if (DELETE_PREVIOUS) {
      //select the previous character
      sel.move("character",-1)
      sel.expand("character")
    }
    sel.text = myValue;
    //nothing must be selected after you type your text!
    sel.collapse() //important!!
    sel.select()
  }
  //MOZILLA & NETSCAPE support
  else if (myField.selectionStart || myField.selectionStart == '0') {
    var startPos = myField.selectionStart;
    var endPos = myField.selectionEnd;
    if (DELETE_PREVIOUS) startPos--;
    myField.value = myField.value.substring(0, startPos)
                  + myValue 
                  + myField.value.substring(endPos, myField.value.length);
    // I added the following 2 lines to stay in the correct position
    myField.focus()    
    myField.selectionEnd = myField.selectionStart=startPos+1;
  } else {
    //OPERA: nothing will do it! Add at end of field...
    if (DELETE_PREVIOUS) {
      myField.value = myField.value.substr(0,myField.value.length-1);
    }
    myField.value += myValue;
    // how go to end of field???
    //at least with the following combination selects puts cursor a the end
    //not practical, but works.
    myField.select();
    myField.blur()
    myField.focus();
  }
  return false;
}




function doConvert(evt,elementName) {
   //Netscape takes Ctrl+C etc. let's cancel this
   //try to convert only letters!!!
   if (evt.ctrlKey || evt.altKey) {    //Netscape now seems to support ctrlKey as well as IE
     return true;
   }
   typeText = document.getElementById(elementName);
   if(evt.keyCode) kC = evt.keyCode;  //IE
   if(evt.which) kC = evt.which;    //Navigator
   //if(evt.keyCode) evt.keyCode = 40 //Transliteration.convert(evt.keyCode);
   //if(evt.which) evt.which = 40 //Transliteration.convert(evt.which);   
   LAST_KEY = String.fromCharCode(kC);
   CONVERTED_CHAR = tryConvert(LAST_KEY);
   return insertAtCursor(typeText,CONVERTED_CHAR);
}





//This works only with one or two-char combinations
function tryConvert() { 
  //actually uses LAST_KEY, PREVIOUS_KEY (changes PREVIOUS_KEY which must be saved outside
  //toggle between transliterated and latin
  if (LAST_KEY == "#") {
    
    changeTranslit(Transliteration.previous.keyName);
    return "#";
  }
  if (!CONVERT) return LAST_KEY; // do not convert, just latin chars

  
  //just avoid writing a combination, eg type K~hren Khren!! 
  if (LAST_KEY == "~") {
    PREVIOUS_KEY = ""
    status = "";
    return "~";
  }

  doubleConversion = "";
  // checks that we are in the expected position!
  //The value of toto will determine whether to attempt to combine.
  toto = 0 ; // a value of 0 means it's OK, combine 
  if (PREVIOUS_KEY_POSITION != null) {  //this var is used only by navigators having document.selection
    if (document.selection.createRange().text != "") {
      //some text is selected --> do not attempt to combine
      document.selection.createRange().text = "";
      toto = 1
    } else {
      PREVIOUS_KEY_POSITION.move("character",1); //this puts us on the expected char
      toto = PREVIOUS_KEY_POSITION.compareEndPoints("EndToEnd",document.selection.createRange()) 
      //compare previous position and this one
      //if toto is different from zero, 
      //it means that we have moved in the field, so you cannot combine characters.
    }
  } //else if (document.getElementById("typeText").selectionEnd) { // Netscape
    // I don't know how to determine toto in Netscape.
  //}


   if (PREVIOUS_KEY != "" && toto == 0) { 
    // second condition should be PREVIOUS_KEY_POSITION just 1 char before mySelection
    // otherwise the curser has been moved around: no good. 
    // attempt double conversion ONLY if previous wasn't double.
    doubleConversion = Transliteration.convert(PREVIOUS_KEY+LAST_KEY); 
    
  }
  if (doubleConversion != "") {
    //DELETE PREVIOUS CHARACTER: browser-specific
    DELETE_PREVIOUS = true; // will be taken charge of in the insert function
    result = doubleConversion;
    showMsg = PREVIOUS_KEY+LAST_KEY  +" -> "+Transliteration.current.shortName+" "+doubleConversion;
    PREVIOUS_KEY = ""; 
   
  } else {
    DELETE_PREVIOUS = false;
    PREVIOUS_KEY = LAST_KEY;
    //register position of key
   if(document.selection) { //IE
      sel = document.selection.createRange();
      PREVIOUS_KEY_POSITION = sel.duplicate();
    } 
    result = Transliteration.convert(LAST_KEY);
    showMsg = LAST_KEY + "   -> "+Transliteration.current.shortName+" "+result
  }
  if(CONVERT) status = showMsg;
  return result;
  //alert(result)
}


function cleanBuffer() {
  //just reset "PREVIOUS_KEY" if you have moved!!
  PREVIOUS_KEY= ""
  status = "CLEANED!"
  return true;
}





//convert whole text
//Also, try to compute where the cursor should be in mainText
DECALAGE = 0; //difference between cursor positions in mainText and typeText
CURSOR_POSITION_IN_TYPING_FIELD = 0;
function convertWholeText(text1) {
  var decal = 0; //difference between cursor positions
  var text2 = "";
  var convert = true;
  var previousIsDouble = 0; 
  myLoop: for(i=0; i<text1.length; i++) {
    if(text1.charAt(i) == "~") {
      //escape double characters eg k~h versus kh
      continue myLoop;
    }
    if(text1.charAt(i) == "#") {
      convert = !convert;
      continue myLoop;
    }
    
    // on parcourt text1!!! A chaque fois on ajoute quelque chose à text2!!
    if (convert) {
      var doubleConversion = "";
      if (i>0 && previousIsDouble == 0) {  
        //attempt double conversion ONLY if previous wasn't double.
        doubleConversion = Transliteration.convert(text1.substr(i-1,2));
        decal += doubleConversion.length-2 
      } 
      if (doubleConversion == "") {    
        var simpleConversion = Transliteration.convert(text1.charAt(i));
        if (simpleConversion =="") {
          text2 += text1.charAt(i);
        } else {
          text2 += simpleConversion;
          decal += simpleConversion.length-1;
        }
        previousIsDouble = 0; 
      } else {
        text2 = text2.substr(0,text2.length-1)+doubleConversion;
        previousIsDouble = 1;
      }
    } else {
      // Do not convert, keep as is
      text2 = text2+text1.charAt(i);
    }
  }
  return text2;
}



function convertWholeTextToMainTextArea() {
  var main = document.getElementById("mainText")
  var type = document.getElementById("typeText")
  main.value = convertWholeText(type.value);

  if (main.selectionStart || main.selectionStart == '0') {
    //CURSOR_POSITION_IN_TYPING_FIELD = main.selectionEnd;
  }

}







// Transliteration class
function Transliteration(keyName) {
  this.direction = "ltr";
  this.keyName = keyName;
  this.fullName = "LATIN"
  this.shortName = "LAT"
  this.longName = "Latin (no transliteration)"
  this.cols = 60;
  this.rows = 15;
  this.fontSize = "1.2em";
  this.trans = new Object();

  //save new translit in savedTranslits static table
  Transliteration.savedTranslits[keyName] = this; //As always in JS, you save a REFERENCE!
                                                    //i.e. once saved you can still modify it!
  if(!Transliteration.current) {
    Transliteration.current = this; //Defines the translit we're currently using
  }

  if(!Transliteration.defaullt) {
    Transliteration.defaullt = this; //Defines the current transliteration that you revert to when toggling #
  } 

  if(!Transliteration.previous) {
    Transliteration.previous = this; //Defines previous transliteration that you revert to when toggling #
  } 



  this.convertedChar = function(key) {
    if (typeof(this.trans[key]) != "undefined") {
      return this.trans[key]
    }
    return "";
  } 
  this.define = function(string1, string2) { //add a conversion
    this.trans[string1] = string2;
  }

  this.defineN = function(string1, num) { //add a conversion using a UNICODE number instead of letter
    var string2 = String.fromCharCode(num);
    if (string2 != "") this.define(string1,string2);
    else alasert("Error trying to defineN("+string1+","+num+"): this number is not a UNICODE character!");
  }

  this.defineAllCorrespondingCapitals = function() {
     //takes All previously defined non-capitals and defines the capitals
     //use: define all smalls, then run this, then define special chars which haven't a conversion
     //it is a bit systematic, unsubtle!
       for (i in this.trans) {
         this.define(i.toUpperCase(),this.trans[i].toUpperCase());
       }  

      }


  
  this.HTMLTable = function() {
     /* This function is very simple, it just writes an HTML table showing the correspondances.
        I am not sure it should be there, as I prefer my Transliteration class to be browser-indept.
     
     */

    var line1 = "";
    var line2 = "";
    var previousAssoc = -1;
    with(this) {
      myLoop: for (var assoc in trans) {
        // avoid to repeat associations for CAPITALS when they are obvious
        upperCaseThenConvert = trans[assoc.toUpperCase()];
        convertThenUpperCase = trans[assoc].toUpperCase()
        lowerCaseThenConvert = trans[assoc.toLowerCase()];
        convertThenLowerCase = trans[assoc].toLowerCase()
  
       
        //if the two are equal BUT you have a capital, 
        //THEN, you can skip (it is obvious)
        
        //if (assoc == "aa" || assoc == "AA") 
         //alert(assoc+" -> "+trans[assoc]+"\nu,c: "+upperCaseThenConvert+"\nc,u: "+convertThenUpperCase);

        if(convertThenUpperCase == upperCaseThenConvert
           && convertThenLowerCase == lowerCaseThenConvert  
           && assoc.toLowerCase() != assoc  ) { //it contains capitals
           // you have something of the type: X --> Y and x --> y, you don't want to repeat yourself
           continue myLoop;
        } 
        if(lowerCaseThenConvert == trans[assoc] & assoc.toLowerCase() != assoc) {
          // you have something of the type jj --> a and JJ --> a (or Jj --> a) keep only smallcase
          continue myLoop;
        }
   
        if(trans[assoc] != previousAssoc) {
          //create new column
          if(line2 != "") line2+="</td>";
          line2 += "<td align='center' valign='top'>"+assoc;
          if(line1 != "") line1+="</td>";
          line1+= "<td align='center'>"+trans[assoc];
        } else {
          //write other possible key combination in the same column
          line2 +="<br>"+assoc
        }
        previousAssoc = trans[assoc];
      }

      if (line1 == "") line1="<td>&nbsp;<br>&nbsp;&nbsp;No conversion made<br>&nbsp;";

      return "<table id=\"transtable\" align=\"center\" border=\"1\" cellspacing=\"0\"\n"
            +"bordercolordark = \"FFFFFF\" bordercolorlight=\"\CCCCCC\">\n"
         +"<tr >"+line1+"</td></tr>\n<tr>"+line2+"</td></tr>\n</table>";
    }
  }

}
// Now the "static" properties and methods
Transliteration.savedTranslits = new Object(); //a table of all current transliterations
Transliteration.convertedChar = function(translitName, string1) {
  return Transliteration.savedTranslits[translitName].convertedChar(string1);
}
Transliteration.convert = function(string1) {
  return Transliteration.current.convertedChar(string1);
}
Transliteration.choose = function(translitName) {
  //set current transliteration
  if(typeof(Transliteration.savedTranslits[translitName]) != "undefined") {
    //retain Previous transliteration name if you actually change
    if (translitName != Transliteration.current.keyName) {
      Transliteration.previous = Transliteration.savedTranslits[Transliteration.current.keyName];
    }
    //choose new current transliteration
    Transliteration.current = Transliteration.savedTranslits[translitName];
    //alert("Previous: "+Transliteration.previous.keyName
    //     +"Current: "+Transliteration.current.keyName);
  } else {
    alert("Error choosing transliteration: "+translitName+" does not exist!");
  }
}

Transliteration.setdefaullt = function(translitName) {
  //set current transliteration
  if(typeof(Transliteration.savedTranslits[translitName]) != "undefined") {
    Transliteration.defaullt = Transliteration.savedTranslits[translitName];
  } else {
    alert("Error choosing transliteration: "+translitName+" does not exist!");
  }
}


Transliteration.HTMLTable = function() {
  return Transliteration.current.HTMLTable();
}
//end of class definition



defaulltTranslit = new Transliteration("latin"); //automatically adds it to Transliteration.savedTranslits
Transliteration.choose("latin"); //this was done implicitly because defaullt was the only one
Transliteration.setdefaullt("latin");
