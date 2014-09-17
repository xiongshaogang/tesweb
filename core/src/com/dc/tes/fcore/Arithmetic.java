package com.dc.tes.fcore;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 四则运算表达式计算
 * @author penli
 *
 */ 
public class Arithmetic { 
	
    public static void main(String args[]){ 
        //System.out.println(arithmetic("2.2+((3+4)*2-22)/2*3.2")); 
    	System.out.println(arithmetic("994330.07/10000"));
    }
    
    public static double arithmetic(String exp){ 
        String result = parseExp(exp).replaceAll("[\\[\\]]", ""); 
        return Double.parseDouble(result); 
    }
    
    /**
     * 解析计算四则运算表达式，例：2+((3+4)*2-22)/2*3
     * @param expression
     * @return
     */ 
    public static String parseExp(String expression){ 
        //String numberReg="^((?!0)\\d+(\\.\\d+(?<!0))?)|(0\\.\\d+(?<!0))$"; 
        expression=expression.replaceAll("\\s+", "").replaceAll("^\\((.+)\\)$", "$1"); 
        String minExp="^((\\d+(\\.\\d+)?)|(\\[\\-\\d+(\\.\\d+)?\\]))[\\+\\-\\*\\/]((\\d+(\\.\\d+)?)|(\\[\\-\\d+(\\.\\d+)?\\]))$"; 
        //最小表达式计算 
        if(expression.matches(minExp)){ 
            String result=calculate(expression); 
             
            return Double.parseDouble(result)>=0?result:"["+result+"]"; 
        } 
        //计算不带括号的四则运算 
        String noParentheses="^[^\\(\\)]+$"; 
        String priorOperatorExp="(((\\d+(\\.\\d+)?)|(\\[\\-\\d+(\\.\\d+)?\\]))[\\*\\/]((\\d+(\\.\\d+)?)|(\\[\\-\\d+(\\.\\d+)?\\])))"; 
        String operatorExp="(((\\d+(\\.\\d+)?)|(\\[\\-\\d+(\\.\\d+)?\\]))[\\+\\-]((\\d+(\\.\\d+)?)|(\\[\\-\\d+(\\.\\d+)?\\])))"; 
        if(expression.matches(noParentheses)){ 
            Pattern patt=Pattern.compile(priorOperatorExp); 
            Matcher mat=patt.matcher(expression); 
            if(mat.find()){ 
                String tempMinExp=mat.group(); 
                expression=expression.replaceFirst(priorOperatorExp, parseExp(tempMinExp)); 
            }else{ 
                patt=Pattern.compile(operatorExp); 
                mat=patt.matcher(expression); 
                 
                if(mat.find()){ 
                    String tempMinExp=mat.group(); 
                    expression=expression.replaceFirst(operatorExp, parseExp(tempMinExp)); 
                } 
            } 
            return parseExp(expression); 
        } 
        //计算带括号的四则运算 
        String minParentheses="\\([^\\(\\)]+\\)"; 
        Pattern patt=Pattern.compile(minParentheses); 
        Matcher mat=patt.matcher(expression); 
        if(mat.find()){ 
            String tempMinExp=mat.group(); 
            expression=expression.replaceFirst(minParentheses, parseExp(tempMinExp)); 
        } 
        return parseExp(expression); 
    } 
    
    /**
     * 计算最小单位四则运算表达式（两个数字）
     * @param exp
     * @return  www.2cto.com
     */ 
    public static String calculate(String exp){ 
    	
    	if (!Utility.isDigitalExpressionString(exp)) { //根本不是数学表达式
    		return exp;
    	}
    	
    	if (exp == null || exp.isEmpty()) {
    		return exp;
    	}
    	
    	String finalResult = "";
    	try
    	{
	        exp=exp.replaceAll("[\\[\\]]", ""); 
	        String number[]=exp.replaceFirst("(\\d)[\\+\\-\\*\\/]", "$1,").split(","); 
	        BigDecimal number1=new BigDecimal(number[0]); 
	        BigDecimal number2=new BigDecimal(number[1]); 
	        BigDecimal result=null; 
	         
	        String operator=exp.replaceFirst("^.*\\d([\\+\\-\\*\\/]).+$", "$1"); 
	        if("+".equals(operator)){ 
	            result=number1.add(number2); 
	        }else if("-".equals(operator)){ 
	            result=number1.subtract(number2); 
	        }else if("*".equals(operator)){ 
	            result=number1.multiply(number2); 
	        }else if("/".equals(operator)){ 
	            result=number1.divide(number2); 
	        } 
	         
	        finalResult = (result!=null?result.toString():null);
	        if (finalResult != null) {
	        	int iPos = finalResult.indexOf(".");
	        	if (iPos >= 0) { //有小数点
	        		String strAfterDot = finalResult.substring(iPos+1);
	        		if (strAfterDot.length() > 2) {//小数位大于2位
	        			strAfterDot = strAfterDot.substring(0,2); //只取两位
	        			finalResult = finalResult.substring(0, iPos) + "." + strAfterDot; 
	        		}
	        	}
	        }
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
    	
        return finalResult;
    }
    
}