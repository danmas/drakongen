/*******************************************************************************
 * Copyright (c) 2006 Roman Eremeev and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Roman Eremeev - initial API and implementation
 *******************************************************************************/
package cb.dfs.trail.utils;

import java.text.DecimalFormat;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;



//import java.util.regex.Pattern;

public final class Strings {

    private static Map<String,String> m_context= new Hashtable<String,String>();


    /**
     *  Превращает строку в массив строк
     * @param s - входная строка с резделителями
     * @param delimiter -  разделитель
     * @return  массив строк
     * 
     *  Example:
     *  String[] tok = tokenize(to_addr, ",");
     *  for (int i = 0; i < tok.length; i++) {
     *             print(tok[i]);
     *  }
     */
    public static String[] tokenize(String s, String delimiter) {
         StringTokenizer tokenizer = new StringTokenizer(s, delimiter);
         String[] tok = new String[tokenizer.countTokens()];
         int i = 0;
         while (tokenizer.hasMoreElements()) {
              tok[i++] = tokenizer.nextToken();
         }
         return tok;
    }

    /**
     * Из строки с разделителями delimiter делает map из пар значения/величина
     * 
     *  String properties = "name1=val1;name2=val2";
		Map<String, String> request_properties = new HashMap<String, String>(); 
		Strings.fillNameValueMap(properties, request_properties, ";","=");
     * 
     * @param props
     * @param map
     * @param delimiter
     */
	public static void fillNameValueMap(String props, Map<String, String> map
			, String prop_delimiter, String name_value_separator) {
		String[] tok = tokenize(props, prop_delimiter);
	    for (int i = 0; i < tok.length; i++) {
				String s1, s2;
				if(tok[i].indexOf(name_value_separator)!=-1) {
					s1 = tok[i].substring(0, tok[i].indexOf(name_value_separator)).toLowerCase();
					s2 = tok[i].substring(tok[i].indexOf(name_value_separator) + 1, tok[i].length());
					map.put(s1,s2);
				} else {
					s1 = tok[i];
					s2 = "";
					map.put(s1,s2);
				}
	      }
	}
    
    /**
     * Из строки с разделителями delimiter делает map
     * 
     *  String properties = "name1;name2";
		Map<String, String> request_properties = new HashMap<String, String>(); 
		Strings.fillValueList(properties, request_properties, ";");
     * 
     * @param props
     * @param map
     * @param delimiter
     */
	public static void fillValueList(String props, List<String> list
			, String prop_delimiter) {
		String[] tok = tokenize(props, prop_delimiter);
	    for (int i = 0; i < tok.length; i++) {
	    		list.add(tok[i]);
	      }
	}

	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //  Заводим пару для подстановки
    //   ex:  setSubstValue("%REPL1%","New Value");
    //   потом можно произвести подстановки используя doAllPercSubst()
    public static void setSubstValue(String  key,String  val)    {
        m_context.put(key,val);
    }
    

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //  Чистим список подстановок
    public static void clearSubstList()    {
        m_context.clear();
    }

    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //  Выполняет подстановки ограниченных процентами строк
    // например "sfkskjf%fsdfas%asdfas"  
    //! SLOW
    //-m- 
    //<UC name="Danger Limits" mark="здесь ограничение на то что и ключи и величины должны быть строками">
    //</UC>
    @SuppressWarnings("unchecked")
	public static String doAllPercSubst(String p_src_str)  { 
        String s_ret=p_src_str;
        Iterator it = m_context.entrySet().iterator();
        //-- fill tables
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            if(entry.getKey()!=null) {
                //-- есть что подставлять? //<UC name="Danger Limits" mark="%-ограничение на подстановку">
                                                                 //</UC>
                if(s_ret.indexOf('%')!=-1) {
                    String s_key=(String)entry.getKey();
                    //if(s_ret.equals(s_key)) {
                    //    s_ret=(String)m_context.get(s_key);
                    //    return s_ret;
                    //}
                    if(s_key.indexOf('%')!=-1)  {
                        String s_val=(String)m_context.get(s_key);
                        s_ret=replace(s_ret,s_key,s_val);
                    //    s_ret=s_ret.replaceAll(s_key,s_val);  
                    }
                } else
                    return s_ret;
            }
        }
        return s_ret;
    }

    
    public static String replace(String str, String what, String to) {
      if (isStringEmpty(str)) return "";
      int n = str.indexOf(what); if (n < 0) return str;
      if (isStringEmpty(what)) return str;
      if (to == null) return str;
      return str.substring(0, n) + to + replace(str.substring(n + what.length()), what, to);
    }

    
    public static boolean isStringEmpty(String s) { return ((s == null) || (s.length() == 0)); }


    public static int stringToInt(String val, int def) {
      if (isStringEmpty(val)) return def;
      try { return Integer.decode(val.toLowerCase().trim()).intValue(); }

      catch (Exception ex) { return def;}
    }

	public static int stringToPositivInt(String sint) throws Exception {
		int ret;
		try {
			ret = Integer.valueOf(sint);
			if (ret < 0) {
				String str = "Число не может быть отрицательным <"
						+ sint + ">.";
				throw new Exception(str);
			}
		} catch (Exception ex) {
			String str = "Неверный формат положительного целого числа <"
					+ sint + ">.";
			throw new Exception(str);
		}
		return ret;
	}
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // преобразование double в строку 
    // d - число знаков после запятой, запятая преобразуется в точку
    public static String toS(double v, int d) {
      return toS(v,d,false);
    }

    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // преобразование double в строку 
    // d - число знаков после запятой, запятая преобразуется в точку
    // exp_form - вывод в научном формате
    public static String toS(double v, int d, boolean exp_form) {
      StringBuffer ptn = new StringBuffer("0");
      if(d>0) {
        ptn.append(".");
        for(;d>0;d--) ptn.append("0");
      }
      if(exp_form) ptn.append("E0");

      DecimalFormat f = new DecimalFormat(ptn.toString());

      return f.format(v).replace(',','.');
    }    
    
    
	public static String getStrBetween(String str, String before, String after) {
		int i1 = str.indexOf(before);
		int i2 = str.indexOf(after);
		
		if(i1==-1||i2==-1) {
			return null;
			// throw new Exception("Не удалось получить номер сессии.");
		}
		return str.substring(i1+before.length(), i2);
	}
    
    
    /*
    public static final String getAdjustedString(int width, FontMetrics fm, String s) {
      int sw = fm.stringWidth(s);
      if (width > sw) return s;
      int lc = (s.endsWith("...")) ? 4 : 1;
      return getAdjustedString(width, fm, s.substring(0, s.length() - lc) + "...");
    }


    public static final int getAdjustedPos(int width, FontMetrics fm, String s) {
      int i;
      for (i = 1; ((fm.stringWidth(s.substring(0, i)) <= width) && (i <= s.length())); ++i);
      return --i;
    }


    public static Vector stringsToVector(String tokens, String delims) { return stringsToVector(tokens, delims, true); }


    public static Vector stringsToVector(String tokens, String delims, boolean trim) {
      if (isStringEmpty(tokens) || isStringEmpty(delims)) return null;
      StringTokenizer st = new StringTokenizer(tokens, delims);
      Vector vec = new Vector();
      if (st.countTokens() == 0) return vec;
      while (st.hasMoreTokens())  {
        if (trim) vec.addElement(st.nextToken().trim());
        else vec.addElement(st.nextToken());
      }
      return vec;
    }
    */


}