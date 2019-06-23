package Test2; 

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TestAssembler2 {
	public static String toHex(int decimal){    
	     int rem;  
	     String hex="";   
	     char hexchars[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};  
	    while(decimal>0)  
	     {  
	    	rem=decimal%16;   
	       hex=hexchars[rem]+hex;   
	       decimal=decimal/16;  
	     }  
	    return hex;  
	}  
	public static String countBits(String s)
	{ 
		int count=s.length();
		System.out.println(count);
		char temp[];
		char []temp2=new char[4];
		char c;
		String s2;
		if(count<4){
		while(s.length()<4){
			int count2=s.length();
			if(s.matches("([a-zA-Z]+)"))
				s=" "+s;
			else
			s="0"+s;
			}
		}
			return s;
}
	
public static void main(String[] args) throws IOException {
		 //reading program from file
		FileReader readProgram=null;
		int noOfRows=0;
	    readProgram = new FileReader("prog2.txt");
	    Scanner programScanner= new Scanner(readProgram);
	    List<String> lines = new ArrayList<String>();
	    String[] HexAdrr=new String[1000];
	    String addr;
	    while(programScanner.hasNextLine()){
		lines.add(programScanner.nextLine());
		noOfRows++;
	    }
	    String[] line = lines.toArray(new String[noOfRows]);    //array fe el file line by line
	    //separating the labels,opcode,and operands
		List<String> labels = new ArrayList<String>();
		List<String> opcode = new ArrayList<String>();
		List<String> operands = new ArrayList<String>();
		String regexProgram = "([a-zA-Z-]+|\\s+)(\\s*)([a-zA-Z0-9.,_''%]+)(\\s*)([a-zA-Z0-9._,%'']+|\\s*)";
	    for(int i=0;i<noOfRows;i++){
		   Pattern pattern = Pattern.compile(regexProgram);
		   Matcher matcher = pattern.matcher(line[i]);
		   while(matcher.find()){
			   labels.add(matcher.group(1));
			   opcode.add(matcher.group(3));
			   operands.add(matcher.group(5));
	         } 
	      }
	    String[]label = labels.toArray(new String[noOfRows]);
	    String[] op= opcode.toArray(new String[noOfRows]);
	     String[] operand = operands.toArray(new String[noOfRows]);
	       for(int i=0;i<noOfRows;i++)                                       //just testing
		   System.out.println(label[i]+"\t"+op[i]+"\t"+operand[i]);
	  //  reading optable from file
	    FileReader readOpTable= null;
	    int noOfRowsInOp=0;
	    readOpTable= new FileReader("opTab.txt");
	    Scanner opScanner =new Scanner(readOpTable);
	    List<String> opTableLines= new ArrayList<String>();
	    while(opScanner.hasNextLine()){
		opTableLines.add(opScanner.nextLine());
		noOfRowsInOp++;
	    }
	    String[] opLine= opTableLines.toArray(new String[noOfRowsInOp]);
	    //separating opcode 
	    List<String> instructions = new ArrayList<String>();
	    List<String> instOpCodes = new ArrayList<String>();
	    final String regexOp = "([a-zA-Z-\\t]+)(\\s*)([a-zA-Z0-9]+)";
	    for(int i=0;i<noOfRowsInOp;i++){
	         Pattern pattern2 = Pattern.compile(regexOp);
		     Matcher matcher2 = pattern2.matcher(opLine[i]);
			   while(matcher2.find()){
				   instructions.add(matcher2.group(1));
				   instOpCodes.add(matcher2.group(3));
		       }
		}
	    String[] ins = instructions.toArray(new String[noOfRowsInOp]);
	    String[] insOp= instOpCodes.toArray(new String[noOfRowsInOp]);
//	    for(int i=0;i<noOfRowsInOp;i++)
//		System.out.println(ins[i]+"\t"+insOp[i]+"\t");            //just testing
	//  main loop 
        int LC=0;
        Hashtable SYMTABLE = new Hashtable();
        Hashtable OPTABLE= new Hashtable<>();
        int[] addresses= new int[noOfRows];
        for(int j=0;j<noOfRowsInOp;j++)
		OPTABLE.put(ins[j],insOp[j]);
	    if(op[0].equalsIgnoreCase("START")){
	    int decimal=Integer.parseInt(operand[0],16); 
		LC=decimal;
		addresses[0]=LC;
         }else
			System.out.println("error");
        int i=0;
	    while(!op[i].equals("END")){
		if(op[i].equals("START")){
			addresses[i+1]=LC;
		}else if(OPTABLE.containsKey(op[i])){
				LC=LC+3;
				addresses[i+1]=LC;
			}else if(op[i].equalsIgnoreCase("WORD")){
				LC=LC+3;
				addresses[i+1]=LC;
			}else if(op[i].equalsIgnoreCase("RESW")){
				int noOfLocations=Integer.parseInt(operand[i]);
			    LC=LC+(3*noOfLocations);
				addresses[i+1]=LC;
			}else if(op[i].equalsIgnoreCase("RESB")){
				LC=Integer.parseInt(operand[i]);
 
			}else if(op[i].equalsIgnoreCase("BYTE")){
				int length = (operand[i].length())-3;
				if(operand[i].startsWith("X'")){
					LC=LC+1;
					addresses[i+1]=LC;
				}else{
					LC=LC+length;
				    addresses[i+1]=LC;
				    }
				}else{
				System.out.println("invalid operaion code");
			    }    
		  if(!label[i].startsWith("\\s+")){
			if(SYMTABLE.containsValue(label[i])){
				//flag=1;
			System.out.println("the label is duplicated");	
			}else{
				SYMTABLE.put(label[i],addresses[i]);
			   }	
		    }
              i++;
			}
	      for(int j=0;j<noOfRows;j++){
	       addr=toHex(addresses[j]);
	       HexAdrr[j]=addr;
	 //SYMTABLE.put(label[j], HexAdrr);
     }
//  for(int j=0;j<noOfRows;j++){
//	  System.out.println(HexAdrr[j]);     testing
// }
	int X = 0;
	   for(int i1=0;i1<noOfRows;i1++){
		   if(op[i1].equals("LDX")){
			  for( int i2=0;i2<noOfRows;i2++){
				  if(label[i2].equals(operand[i1])){
					  X=Integer.parseInt(operand[i2]);
					  System.out.println(label[i2]);
				  }
			        }
				   }
	   }
          int	programLength=LC-addresses[0];
          System.out.println(programLength);
          String l=toHex(programLength);
          System.out.println(l);
        //writing intermediate file
	         Writer writer = null;
	         writer = new BufferedWriter(new FileWriter(("Intermediate.txt")));
	         for(int j=0;j<noOfRows;j++){
		       writer.write(HexAdrr[j]+"     "+label[j]+"     "+op[j]+"     "+operand[j]);
		       ((BufferedWriter) writer).newLine();	
		             }
	          writer.close();
	   // writing symbol Table  
	         Writer writer2 = null;
		     writer2 = new BufferedWriter(new FileWriter(("SymbolTable.txt")));
		      for(int j=0;j<noOfRows;j++){
		         if(label[j].matches("([a-zA-Z]+)")){
			     writer2.write(label[j]+"    "+HexAdrr[j]);
			     ((BufferedWriter) writer2).newLine();
		            	 }
			         }
		             writer2.close();
//		  //pass 2
		         String[] objectCode = new String[noOfRows];
		         String headRecord = null;
		          if(op[0].equals("START")){
		        	 String s=countBits(HexAdrr[0]);
		        	  String l2=countBits(l);
		        	headRecord="H "+label[0]+" "+s+" "+l2;
		        	 }
		        int  j=0;
		        int notFoundSymbol=0;
		        int operandAddress;
		        String objectCode1;
		        String operandAddressHex;
		          while(!op[j].equals("END"))
		          {
		        	if(OPTABLE.containsKey(op[j]))
		        	{
						if(SYMTABLE.containsKey(operand[j])){
							//System.out.println("found");
		        				operandAddress = addresses[j];
		        				operandAddressHex=toHex(operandAddress);
		        				//System.out.println(operandAddressHex);
		        				objectCode[j]=insOp[j]+operandAddressHex;
						}
						else if(operand[j].contains(",X"))       //   matches("([a-zA-Z]+)([,])([X])"))
							{
						//System.out.println("Indexing");
						operandAddress=addresses[j];//+X;
						operandAddressHex=toHex(operandAddress);
						objectCode[j]=insOp[j]+operandAddressHex;
						//System.out.println(operandAddressHex);
							}
						else{
							notFoundSymbol=1;
						    operandAddress=0;
						     operandAddressHex=toHex(operandAddress);
							//objectCode[j]=insOp[j]+ operandAddressHex;
							System.out.println("undefiend symbol");
						}
		          }
		        	else if(op[j].equals("BYTE")||op[j].equals("WORD"))
		        	{
		        	objectCode1=countBits(operand[j]);
		        	objectCode[j]=objectCode1;
		        	}
		        	
					j++;
		          }
		          
		       //   for(int i1=0;i1<noOfRows;i1++)
		        	//  System.out.println(objectCode[i1]);
		          //7awelt a3mel el lines bta3et el object code bs ma3reftesh
//		          List<String> programObjectCode = new ArrayList<String>();
//		          programObjectCode.add(headRecord);
//		          int n=0;
//		          int read=0;
//		          int size=0;
//		          int cycles=1;
//		          String sz;
//		          String temp = null;
//		        for(int n2=0;n2<noOfRows;n2++){
//		        while(n<=10||!op[n].equals("RESW")||!op[n+n2].equals("RESB")){
//		        			  read++;
//		        			  n++;
//		        		  }
//		        		  if(read==10){
//		        			  size=size+30;
//		        			  sz=toHex(30);
//		        			  while(read>0){
//		        				  temp="T "+sz+" "+objectCode[n2*cycles]+" ";
//		        				  read--;
//		        			  }
//		        			  programObjectCode.add(temp);
//		        		  }else if(read<10)
//		        		  {
//		        			size=size+(read*10);
//		        			sz=toHex(size);
//		        			while(read>0){
//		        				temp="T "+sz+" "+objectCode[n2*cycles]+" ";
//		        				  read--;
//		        			}
//		        			programObjectCode.add(temp);  
//		        	  }
//		        		  cycles++;
//		        	System.out.println(programObjectCode);
		        		  }
		        		  
		          
}



		 
		
	
	
 


