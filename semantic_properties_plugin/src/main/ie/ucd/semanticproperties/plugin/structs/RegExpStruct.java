/**
 * @title "Semantic LevelRepresenation Plugin Package"
 * @description "Class that represents a Regular Expression"
 * @author  Eoin O'Connor
 * @copyright ""
 * @license "Public Domain."
 * @version "$Id: 01-07-2010 $"
 */
package ie.ucd.semanticproperties.plugin.structs;

/**
 * <p>
 * A string representation of a Regular Expression and a map of what to store in
 * the capturing groups .
 * </p>
 * 
 * @see LevelRepresenation
 * @version "$Id: 01-07-2010 $"
 * @author eo
 */
import ie.ucd.semanticproperties.plugin.api.SemanticPropertyInstance;
import ie.ucd.semanticproperties.plugin.customobjects.MyDescription;
import ie.ucd.semanticproperties.plugin.customobjects.MyExpression;
import ie.ucd.semanticproperties.plugin.customobjects.MyFloat;
import ie.ucd.semanticproperties.plugin.customobjects.MyInt;
import ie.ucd.semanticproperties.plugin.customobjects.MyObject;
import ie.ucd.semanticproperties.plugin.customobjects.MyString;
import ie.ucd.semanticproperties.plugin.customobjects.Nat;
import ie.ucd.semanticproperties.plugin.exceptions.InvalidSemanticPropertyUseException;

import org.antlr.stringtemplate.*;
import org.antlr.stringtemplate.language.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class that represents a Regular Expression.
 * <p>
 * Adds information to a regular expression about what is stored in its
 * capturing groups and provides methods to access these values.
 * </p>
 * @author eo
 */
public class RegExpStruct {
  /**
   * String representation of the RegExp.
   */
  private String exp;
  /**
   * Map representing the values in the capturing group of this RegExp.
   */
  private HashMap<String, int[]> positionMap;
  /**
   * Map representing the Objects in the capturing group of this RegExp.
   */
  private HashMap<String, MyObject> objectMap;
  /**
   * Type of RegExpStruct this is.
   */
  private RegType typeOfReg;
  /**
   * Number of positions in this RegExpStruct.
   */
  private int totalPositions;

  /**
   * Default constructor.
   * Initialise all variables to default value.
   */
  RegExpStruct() {
    exp = "";
    positionMap = new LinkedHashMap<String, int[]>();
    objectMap = new LinkedHashMap<String, MyObject>();
    typeOfReg = RegType.NORMAL;
    totalPositions = 0;
  }

  /**
   * Constructor for objects.
   * @param s
   *          String representation of the regular expression.
   * @param m
   *          LinkedHashMap representing capturing groups.
   * @param num
   *          Number of capturing groups.
   */
  RegExpStruct(Object o) {
    exp = "";
    positionMap = new LinkedHashMap<String, int[]>();
    objectMap = new LinkedHashMap<String, MyObject>();
    typeOfReg = RegType.NORMAL;
    
    if(o instanceof MyObject){
      MyObject temp = (MyObject) o;
      objectMap.put(temp.getId(), temp);
      positionMap.put(temp.getId(),new int[]{ 1});
      exp+=temp.getKind().getReg();
      totalPositions = 1;
      
    } else if(o instanceof String){
      exp+=(String)o;
      totalPositions = 0;
    }
  }
 /**
  * Constructor for choice, option.. etc.
  * @param p
  */
  RegExpStruct(RegType p) {
    exp = "";
    positionMap = new LinkedHashMap<String, int[]>();
    objectMap = new LinkedHashMap<String, MyObject>();
    typeOfReg = p;
    totalPositions = 0;
    if(p.equals(RegType.OPTIONAL)){
      exp = "(?:)?";
    } else if (p.equals(RegType.CHOICE)) {
      exp = "(?:)";
    }
  }

  /**
   * Add RegExoStruct on to this one.
   */
  public void add(RegExpStruct toAdd) {
    String space = "[\\s]+";
    String optionalSpace = "[\\s]*";
    /**
     * Adjust exp.
     */
    if(typeOfReg.equals(RegType.NORMAL)){
      /**
       * if exp is empty dont add space
       */
      if((exp.equals(""))){
        exp = toAdd.exp;
      } else{
       /**
        * case where list item is optional space before must also be optional
        */
        if(toAdd.typeOfReg.equals(RegType.OPTIONAL)){
          exp = exp + optionalSpace + toAdd.exp;
        } else{
          exp = exp +space+ toAdd.exp;
        }
      }
    } else if(typeOfReg.equals(RegType.OPTIONAL)){
      String temp = exp.substring(3,exp.length()-2);
      /**
       * add on optional. No space before if it is first option.
       */
      if((temp.equals(""))){
        temp = toAdd.exp;
      } else {
        temp = temp+space+toAdd.exp;
        
      }
      exp = "(?:"+temp+")?";
      
    } else if(typeOfReg.equals(RegType.CHOICE)){
      String temp = exp.substring(3,exp.length()-1);
      /**
       * add on choice. No | before if it is first choice.
       */
      if((temp.equals(""))){
        temp = toAdd.exp;
      } else {
        temp = temp+"|"+toAdd.exp;
      }
     
      exp = "(?:"+temp+")";
      
    }

    /**
     * Adjust all maps items in  toAdd and add them to this.
     */
    for(String key : toAdd.positionMap.keySet()) {
      /**
       * Adjust positionMap.
       */
      int[] newArray = toAdd.positionMap.get(key);
      // Adjust int[].
      for(int i=0;i<newArray.length;i++){
        newArray[i] = newArray[i] + totalPositions;
      }
      // Add to this map and check for duplicates.
      if(this.positionMap.put(key, newArray)== null){
        //throw exception here.
      }
      /**
       * Adjust objectMap.
       */
      this.objectMap.put(key, toAdd.objectMap.get(key));
    }
    
    /**
     * Adjust total positions.
     */
    totalPositions+=toAdd.totalPositions;
  }
  public HashMap<String, Object> match(String input) throws InvalidSemanticPropertyUseException{
    HashMap<String,Object> captured = new HashMap <String, Object>();
    /**
     * Match Instance
     */
    Pattern p = Pattern.compile(exp);
    Matcher m = p.matcher(input);
    if(!m.matches()){
      throw new InvalidSemanticPropertyUseException();
    }
    /**
     * Fill HashMap with the captured variables for this Instance.
     */
    HashMap<String, int[]> intMap  = positionMap;
    HashMap<String, MyObject> obMap = objectMap;
//    LinkedList<String> stringMap = new LinkedList <String>();
//    int start = 0;
    for(String s: obMap.keySet()) {
      MyObject ob = obMap.get(s);
      int[] g = intMap.get(s);
      int i = 0;
      //get valid int for the match
      for(int pres:g){
        //fix later
        
        if((pres != 0)){
          i= pres;
        }
      }
      //fill only with non null values
      if((m.group(i)!=null)){
        Object temp = m.group(i);
        if(ob instanceof MyString){
          String toSet =  m.group(i);
          ob.setValue(toSet.substring(1,toSet.length()-1));
        } else if(ob instanceof MyExpression){
          String toSet =  m.group(i);
          ob.setValue(toSet.substring(1,toSet.length()-1));
        } else if(ob instanceof MyDescription){
          String toSet =  m.group(i);
          ob.setValue(toSet.substring(0,toSet.length()-1));
        } else if(ob instanceof Nat){
          Integer toSet =  Integer.parseInt(m.group(i));
          ob.setValue(toSet);
        } else if(ob instanceof MyInt){
          Integer toSet =  Integer.parseInt(m.group(i));
          ob.setValue(toSet);
        } else if(ob instanceof MyFloat){
          float toSet = Float.valueOf(m.group(i)).floatValue();
          ob.setValue(toSet);
        } else {
          ob.setValue(m.group(i));
        }
        captured.put(s, ob.getValue());
      }
//        // add Strings up to this match
//        int f = m.start(i);
//        String toAdd = input.substring( start, m.start(i));
//        start = m.end(i);
//        String[] split = toAdd.split(" ");
//        for(String cur : split){
//          stringMap.push(cur);
//        }
//        //last case
//        if(m.groupCount()==i && start!=input.length()){
//          String toAdd2 = input.substring( start, m.start(i));
//          start = m.end(i);
//          String[] split2 = toAdd.split("\\s");
//          for(String cur : split2){
//            stringMap.push(cur);
//          }
//        }

    }
    return captured;
  }
  /**
   * 
   * @return
   */
  public StringTemplate getPrettyPrint(){
    String i = "";
    for(String key:objectMap.keySet()){
      i += ("$"+ key+"$ ");
    }
    return new StringTemplate(i, DefaultTemplateLexer.class);
    
  }


  /** 
   * Used for testing equivalence of two RegExpStructs.
   */
  public static boolean equals(final RegExpStruct r1, final RegExpStruct r2) {
    if(!r1.exp.equals(r2.exp)){
      return false;
    }
    if(!(r1.totalPositions==r2.totalPositions)){
      return false;
    }
    if(!(r1.typeOfReg.equals(r2.typeOfReg))){
      return false;
    }
    /**
     * Compare objectMaps and intMaps..
     */
    if(r1.objectMap.size()!=r2.objectMap.size()){
      return false;
    }
    if(r1.positionMap.size()!=r2.positionMap.size()){
      return false;
    }
    Iterator obIt = r1.objectMap.keySet().iterator();
    while(obIt.hasNext()){
      String key = (String)obIt.next();
      /**
       * ob map
       */
      if (!(r2.objectMap.get(key).getId().equals(r1.objectMap.get(key).getId()))){
        return false;
      }
      if (!(r2.objectMap.get(key).getValue().equals(r1.objectMap.get(key).getValue()))){
        return false;
      }
      /**
       * int map
       */
      if (!Arrays.equals(r2.positionMap.get(key),r1.positionMap.get(key))){
        return false;
      }
    }
    return true;
  }

  public LinkedHashMap<String, Object> getGroupObj() {
    
    return(LinkedHashMap)objectMap;
  }

  /**
   * @return the exp
   */
  public String getExp() {
    return exp;
  }

  /**
   * @return the totalPositions
   */
  public int getTotalPositions() {
    return totalPositions;
  }


}

