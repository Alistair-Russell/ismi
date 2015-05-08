// debugging tools

// Show the bowels of an object
// returns a string
function showMeAll(myObject) {
  var x = "PROPERTIES OF OBJECT\n" 
  for (i in myObject) {
    //x+=("theobject."+i+"= "+myObject.i+"  <br>\n")
    x+="thisObject["+i+"]="+myObject[i]+"  <br>\n";
  }
  return x;
}

function showFunction(myFunction) {
  return (string)(myFunction)
}