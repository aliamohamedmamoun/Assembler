package Test2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class FinalAssem {
	//fun bt7wal Hexa
	public static String toHex(int decimal){    
	     int rem;  
	     String hex="";   
	     char hexchars[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'}; 
	     if(decimal==0){
	    	
	    	 return "0";
	     }
	    while(decimal>0)  
	     { rem=decimal%16;   
	       hex=hexchars[rem]+hex;   
	       decimal=decimal/16;  
	     }  
	    
	    return hex;  }
	
	static String stringToHex(String string) {
		  StringBuilder buf = new StringBuilder(200);
		  for (char ch: string.toCharArray()) {
		 //   if (buf.length() > 0)
		   //  buf.append(' ');
		    buf.append(String.format("%x", (int) ch));
		  }
		  return buf.toString();

}



	public static String countBits(String s)
	{ 
		int count=s.length();
		//System.out.println(count);
		char temp[];
		char []temp2=new char[4];
		char c;
		String s2;
		if(count<4){
		while(s.length()<4){
			int count2=s.length();
				s="0"+s;
			}
		}
			return s;
			
}
	public static String countBitss(String s)
	{ 
		int count=s.length();
		//System.out.println(count);
		char temp[];
		char []temp2=new char[6];
		char c;
		String s2;
		if(count<6){
		while(s.length()<6){
			int count2=s.length();
				s="0"+s;
			}
		}
			return s;
			
}
	
	public static void main(String[] args) throws IOException {
		 //reading program from file
		FileReader readProgram=null;
		int noOfRows=0;
		//int noOfWords;// no of words in the file 
	    readProgram = new FileReader("prog5.txt");
	    Scanner programScanner= new Scanner(readProgram);
	    List<String> lines = new ArrayList<String>();
	    
	    String addr;
	    
	while(programScanner.hasNextLine()){
		lines.add(programScanner.nextLine());
		noOfRows++;
	}
	String[] line = lines.toArray(new String[noOfRows]);//array fe el file line by line
	
	//separating the labels,opcode,and operands
		List<String> labels = new ArrayList<String>();
		List<String> opcode = new ArrayList<String>();
		List<String> operands = new ArrayList<String>();
		//String regexProgram = "([a-zA-Z-]+|\\s+)(\\s*)([a-zA-Z0-9.,_''%]+)(\\s+)([a-zA-Z0-9._,%'']+|\\s+)";
		String regexProgram = "(\\w*)\\s+(\\w+)\\s+(.*)\\s*";
		
		   for(int i=0;i<noOfRows;i++)
		     {
			   Pattern pattern = Pattern.compile(regexProgram);
			   Matcher matcher = pattern.matcher(line[i]);
			   while(matcher.find())
			   {
				   labels.add(matcher.group(1));
				   opcode.add(matcher.group(2));
				   operands.add(matcher.group(3));
				    }
		}
		   String[] label = labels.toArray(new String[noOfRows]);
		   String[] op= opcode.toArray(new String[noOfRows]);
		   String[] operand = operands.toArray(new String[noOfRows]);
		   for(int i=0;i<noOfRows;i++)                                       //just testing
	     System.out.println(label[i]+"\t"+op[i]+"\t"+operand[i]);
		 //reading optable from file
			FileReader readOpTable= null;
			int noOfRowsInOp=0;
			readOpTable= new FileReader("opTab.txt");
			Scanner opScanner =new Scanner(readOpTable);
			List<String> opTableLines= new ArrayList<String>();
			while(opScanner.hasNextLine())
			{
				opTableLines.add(opScanner.nextLine());
				noOfRowsInOp++;
			}
			String[] opLine= opTableLines.toArray(new String[noOfRowsInOp]);
			String[] HexAdrr=new String[noOfRows];
			//separating opcode
			List<String> instructions = new ArrayList<String>();
			List<String> instOpCodes = new ArrayList<String>();
			final String regexOp = "(\\w*)\\s+(\\w+)\\s*";
			 for(int i=0;i<noOfRowsInOp;i++)
				{
					   Pattern pattern2 = Pattern.compile(regexOp);
					   Matcher matcher2 = pattern2.matcher(opLine[i]);
					   while(matcher2.find())
					   {
						   instructions.add(matcher2.group(1));
						   instOpCodes.add(matcher2.group(2));
				       }
				}
			 String[] ins = instructions.toArray(new String[noOfRowsInOp]);
			 String[] insOp= instOpCodes.toArray(new String[noOfRowsInOp]);
			 int LC=0;
			   Hashtable SYMTABLE = new Hashtable();
			   Hashtable OPTABLE= new Hashtable<>();
			   int[] addresses= new int[noOfRows];
			   for(int j=0;j<noOfRowsInOp;j++)
					OPTABLE.put(ins[j],insOp[j]);
				  // System.out.println(OPTABLE);                             //just testing
			   if(op[0].equalsIgnoreCase("START"))
				   {	int decimal=Integer.parseInt(operand[0],16); 
					   LC=decimal;
					   addresses[0]=LC;
					   addr=toHex(addresses[0]);
						 // System.out.println(addr);
						  HexAdrr[0]=addr;
					//  System.out.println(LC);                   //just testing
			       }
				   else
						System.out.println("error");
			   
			   int i=0;
				   
				while(!op[i].equals("END"))
				{
					if(op[i].equals("START")){
						addresses[i+1]=LC;
						
					}
					else if(OPTABLE.containsKey(op[i])){
							LC=LC+3;
							addresses[i+1]=LC;
						}
						else if(op[i].equalsIgnoreCase("WORD")){
							LC=LC+3;
							addresses[i+1]=LC;
		//					System.out.println(addresses[i+1]);
							 //System.out.println("Word");                   //just testing
						}else if(op[i].equalsIgnoreCase("RESW")){
							int noOfLocations=Integer.parseInt(operand[i]);
						    LC=LC+(3*noOfLocations);
							addresses[i+1]=LC;
							//System.out.println("RESW");                     //just testing
						}else if(op[i].equalsIgnoreCase("RESB")){
							int noOfBytes=Integer.parseInt(operand[i]);
						
							LC=LC+noOfBytes;
							
						    addresses[i+1]=LC;
						    //System.out.println("RESB");                    //just testing
						}else if(op[i].equalsIgnoreCase("BYTE")){
							int length = (operand[i].length())-3;
							if(operand[i].startsWith("X'")){
								LC=LC+1;
								addresses[i+1]=LC;
							//	System.out.println(addresses[i+1]);
								//System.out.println("X");
							}
							else
							{ LC=LC+length;
							addresses[i+1]=LC;}
						//	System.out.println(LC); 
							//System.out.println("BYTE");                  //just testing
						}else {
							System.out.println("invalid operaion code");
						}    
					if(!label[i].startsWith("\\s+"))
					{
						if(SYMTABLE.containsValue(label[i])){
							//flag=1;
							System.out.println("the label is duplicated");	
						}
						else{
							SYMTABLE.put(label[i],addresses[i+1]);
						  //  System.out.println(label[i]+" " +addresses[i]);     //just testing
						    }	
					}
			            i++;
			        		//System.out.println(addresses[i]);
			            addr=toHex(addresses[i]);
						 // System.out.println(addr);
						  HexAdrr[i]=addr;
						}
		
				
//				for(int j=1;j<noOfRows;j++){
//			//	System.out.println(addresses[j]);
//				  addr=toHex(addresses[j]);
//				 // System.out.println(addr);
//				  HexAdrr[j]=addr;
//			  }
//	
//			
				

				
				
			          int	programLength=LC-addresses[0];
			          System.out.println(programLength);
			          String l=toHex(programLength);
			          System.out.println(l);
			       
			         
				         //writing intermediate file
				         Writer writer = null;
				         writer = new BufferedWriter(new FileWriter(("Intermediate.txt")));
				         for(int j=0;j<noOfRows;j++)
					            {
					        writer.write(HexAdrr[j]+"     "+label[j]+"     "+op[j]+"     "+operand[j]);
					        ((BufferedWriter) writer).newLine();	
					            }
				          writer.close();
				         
			// writing symbol Table  
				         //   Writer writer2 = null;
				            Reader read;
				            Writer    writer2 = new BufferedWriter(new FileWriter(("SymbolTable.txt")));
					       
//					             for(int j=0;j<noOfRows;j++){
//					            	 if(label[j].matches(("[a-zA-Z]+"))){
//						        	 writer2.write(label[j]+"    "+HexAdrr[j]);
//						        	 ((BufferedWriter) writer2).newLine();
//					            	 }
//					            	 
////					            	 if(!label[j].equals("\\s+")){
////							        	 writer2.write(label[j]+"    "+HexAdrr[j]);
////							        	 ((BufferedWriter) writer2).newLine();
////						            	 }
//						         }
//					             writer2.close();
//					        
					         //pass 2
					          
					             
					             
					             
					             FileReader readSymbolTable =null; //b2ra el symbol table
					 			int noOfRowsInSYmbol=0;
					 			readSymbolTable= new FileReader("SymbolTable.txt");
					 			Scanner SymScanner =new Scanner(readSymbolTable);
					 			List<String> SymTableLines= new ArrayList<String>();
					 			while(SymScanner.hasNextLine())
					 			{
					 				SymTableLines.add(SymScanner.nextLine());
					 				noOfRowsInSYmbol++;
					 			}
					 			String[] SymLine= SymTableLines.toArray(new String[noOfRowsInSYmbol]);
					 			readSymbolTable.close();
					 	//separating symbol table
					 			List<String> Symbols = new ArrayList<String>();
					 			List<String> AddressOfSym = new ArrayList<String>();
					 			final String regexSym = "(\\w*)\\s+(\\w+)\\s*";
					 			 for(int q=0;q<noOfRowsInSYmbol;q++)
					 				{
					 					   Pattern pattern2 = Pattern.compile(regexSym);
					 					   Matcher matcher2 = pattern2.matcher(SymLine[q]);
					 					   while(matcher2.find())
					 					   {
					 						  Symbols.add(matcher2.group(1));
					 						   AddressOfSym.add(matcher2.group(2));
					 				       }
					 				}
					 			 String[] SYMBOLS = Symbols.toArray(new String[noOfRows]);
					 			 String[] ADDRESSofSYMBOLS= AddressOfSym.toArray(new String[noOfRows]);
					 			 Writer writer4 = null;
						            
							        writer4 = new BufferedWriter(new FileWriter(("symbol.txt")));
					             for(int y=0;y<noOfRows;y++)
					             {
					            	 if(SYMBOLS[y].matches("(\\s+)"))
					            	 {	 System.out.println((SYMBOLS[y]+"    "+ADDRESSofSYMBOLS[y]));
					            		 writer4.write(SYMBOLS[y]+"    "+ADDRESSofSYMBOLS[y]);
							        	 ((BufferedWriter) writer4).newLine();
					             }
					             }
					             writer4.close();
//					             for(int y=0;y<noOfRowsInSYmbol;y++){
//					            	 System.out.println(SYMBOLS[y]);
//					            	 System.out.println(ADDRESSofSYMBOLS[y]);
//					             }
					             Hashtable<String , String> NewHash= new Hashtable<>();
					             for(int k=0;k< noOfRowsInSYmbol;k++){
					            	 NewHash.put(SYMBOLS[k],ADDRESSofSYMBOLS[k]);
					            	 
					            	// System.out.println(NewHash.get(SYMBOLS[k]));
					             }
//					             
					      
					             
					             
					             
						         String[] objectCode = new String[noOfRows];
						         String headRecord = null;
						          if(op[0].equals("START")){
						        	 String s=countBitss(HexAdrr[0]);
						        	 String l2=countBitss(l);
						        	headRecord="H "+label[0]+" "+s+" "+l2;
						        	System.out.println(headRecord);
						        	 }
						         
						        int  j=1;
						        int notFoundSymbol=0;
						        String operandAddress;
						        String objectCode1;
						        String operandAddressHex;
						     
						          while(!op[j].equals("END"))
						          {
						        	  
						        	if(OPTABLE.containsKey(op[j]))
						        	{
										if(SYMTABLE.containsKey(operand[j])){
											//System.out.println("found");
											 
						        			operandAddress= NewHash.get(operand[j]);
//										operandAddress=Integer.parseInt(o);
//											System.out.println(operandAddress);
//						        				operandAddressHex=toHex(operandAddress);
						        				operandAddressHex=countBits(operandAddress);
						        			//	System.out.println(operandAddressHex);
						        				objectCode[j]=OPTABLE.get(op[j])+""+operandAddressHex;
						        				
						        				
										}
										else if(operand[j].contains(",X"))       //   matches("([a-zA-Z]+)([,])([X])"))
											{
//											
										operand[j]=operand[j].substring(0, operand[j].length() - 2);
								operandAddress=NewHash.get(operand[j]);
								//System.out.println(operandAddress);
								
								operandAddressHex=countBits(operandAddress);
								//System.out.println(operandAddressHex);
								int decimalAddr=Integer.parseInt(operandAddressHex,16);
								//h7wl l operand addresshex l decimal w azwd 3leh 32768 w b3den a7wlo hexa
								int temp=decimalAddr+32768;
								operandAddressHex=toHex(temp);
								
		        			//	System.out.println(operandAddressHex);
		        				objectCode[j]=OPTABLE.get(op[j])+""+operandAddressHex;
								
								
											}
										else{
											notFoundSymbol=1;
										    operandAddress="0";
//										     operandAddressHex=toHex(operandAddress);
										     operandAddressHex=countBitss(operandAddress);
											objectCode[j]=OPTABLE.get(op[j])+""+ operandAddressHex;
											System.out.println("undefiend symbol");
										}
						          }
						        	else if(op[j].equals("BYTE")||op[j].equals("WORD"))
						        	{
						        		String temp = operand[j].replaceAll("['']", "");
						        		temp=temp.replaceFirst("[CX]","");
						        	if(op[j].equals("BYTE")){
						                  if(operand[j].startsWith("C")){
						                	  objectCode1=stringToHex(temp);
						                	  objectCode[j]=countBitss(objectCode1);
							        		  //System.out.println(objectCode[j]);	
						        	}else
						        		//System.out.println(temp);
						        		objectCode[j]=temp;
						        	}else if(op[j].equals("WORD")){
						        		String trim=operand[j].replaceAll("\\s+","");
						        		int intg=Integer.parseInt(trim);
						        		objectCode1=toHex(intg);
						        	objectCode1=countBits(operand[j]);
						        	objectCode[j]=objectCode1;
						        	}
						        	}


						        	System.out.println(objectCode[j]);
									j++;
						          }
		   
		 
						          
						          
//						       
		 // hktb f file object file aho aho :(	       
						          String s=countBits(HexAdrr[0]);      //bktb f awal address f objectfile 
					        	  String l2=countBits(l);         //w b7sb el lentgh w first location 
					        	  omar(objectCode,HexAdrr,label[0],s,l2);

					        	  
					        	  
					        	  //listing file
					        	  Writer writer3 = null;
							         writer3 = new BufferedWriter(new FileWriter(("Listing.txt")));
							         for(int j1=0;j1<noOfRows;j1++)
								            {
							        	 writer3.write(HexAdrr[j1]+"     "+label[j1]+"     "+op[j1]+"     "+operand[j1]+"     "+objectCode[j1]);
								        ((BufferedWriter) writer3).newLine();	
								            }
							          writer3.close();

	}
	


static void omar(String [] objectcodes,String[] addresses,String title, String startAddress,String lengthOfProgram) throws IOException{
	 Writer writerf = null;           
     writerf = new BufferedWriter(new FileWriter(("codeProgram.txt"))); 
 	 writerf.write("H "+title+" "+ countBitss(startAddress) +" "+ countBitss(lengthOfProgram));
 	((BufferedWriter)writerf).newLine();
	String lenght;
	String record="";
	int j =0;
	int f = 0;
	boolean nl;
	String address;
	int k =0;
	while(k<objectcodes.length)
	{  address = addresses[k];
		for(int i =0;i<10;i++)
 	{
 	
 	f++;
 	if(objectcodes[k] == null) //means fi resw aw resb
 	{
 		k++;
 		if(i!=0)
 		
 		break;
 	}
 	record = record +" "+ objectcodes[k]; 
 	k++;
 }
	
	lenght =toHex( f*3);
	writerf.write("T"+" "+countBitss(address)+" "+lenght+record);
	record = "";
	System.out.println(lenght);
	((BufferedWriter)writerf).newLine();
	}
	writerf.write("E"+startAddress);
	writerf.close();
}




}
						          
	
		 
		
	
		        
		 
		
	
	
 



		
	
	
 

		        
		 
		
	
	




		
	
	

