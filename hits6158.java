/*KELKAR AKSHAY CS610 6158 prp */
import static java.lang.Math.abs;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;



/**
 * @author akshay_kelkar
 * <code>hits6158</code>class to 
 * implement Hits algorithm
 */

public class hits6158 {
	//initialize variables
	
	static int iters;
	static  int iVal;
	static String inputFile;
	static int num_ver;// vertices     
	static int num_edge; //edges     
	static  int[][] adj_mat;//adjacency matrix
	static double[] ihub;
	static double[] iauth;
	static final double error_rate = 0.00001; 

	public static void main(String [] args)
	{
		
		if(args.length!=0)
		   { 
			iters=Integer.parseInt(args[0]);
			iVal= Integer.parseInt(args[1]);
			 if((iVal<=1 && iVal>=-2) != true)
			 {
				 System.out.println("Enter Valid Input");
				 System.exit(0);
			 }
			inputFile= args[2];
			new hits6158().hitsalgo();
		   }
		else
		{
			System.out.println("Enter Valid Input");
			System.exit(0);
		}
	}// main method
	
	public void hitsalgo()
	{
		//initialize matrix
		try {        
			Scanner sc = new Scanner(new File(inputFile));
			num_ver = sc.nextInt();
			num_edge = sc.nextInt();
			adj_mat = new int[num_ver][num_ver];
			for(int i = 0; i < num_ver; i++)
				for(int j = 0; j < num_ver; j++)
					adj_mat[i][j] = 0;

			while(sc.hasNextInt())
			{
				adj_mat[sc.nextInt()][sc.nextInt()] = 1; 
			}
			ihub = new double[num_ver];
			iauth = new double[num_ver];
			
			if (iVal==0) {
		
				for(int i = 0; i < num_ver; i++) {
					ihub[i] = 0;
					iauth[i] = 0;
				}
			}
			else if(iVal==1) {
			
				for(int i = 0; i < num_ver; i++) {
					ihub[i] = 1;
					iauth[i] = 1;
				}
			}
			else if(iVal==-1) {
				for(int i =0; i < num_ver; i++) {
					ihub[i] = 1.0/num_ver;
					iauth[i] = 1.0/num_ver;
				}
			}
			else if(iVal==-2) {
				for(int i =0; i < num_ver; i++) {
					ihub[i] = 1.0/Math.sqrt(num_ver);
					iauth[i] = 1.0/Math.sqrt(num_ver);
				}
			}			

		}
		catch(FileNotFoundException e){
			System.out.println("Exception: "+e.getMessage());

		}
		
		double authscalefact =0.0;
		double authsumsqr=0.0;
		double hubscalefact =0.0;
		double hubsumsqr=0.0;
		double [] hub1 = new double [num_ver];
		double [] auth1 = new double [num_ver];
		
		double [] authprev = new double [num_ver];
		double [] hubprev = new double [num_ver];
		
		if(num_ver > 10)
		{
			iters =0;
			for (int i=0; i<num_ver;i++)
			{
				hub1[i] = 1.0/num_ver;
				auth1[i] = 1.0/num_ver;
				hubprev[i] = hub1[i];
				authprev[i] = auth1[i];
			}
			
			int i=0;
			do
			{
				for(int j = 0; j < num_ver; j++) {
					authprev[j] = auth1[j];
					hubprev[j] = hub1[j];
				}
				for(int j = 0; j < num_ver; j++) {
					auth1[j] = 0.0;
				}
				for(int j = 0; j < num_ver; j++) {
					for(int k = 0; k < num_ver; k++) {
						if(adj_mat[k][j] == 1) {
							auth1[j] += hub1[k]; 
						}
					}
				}
				for(int j = 0; j < num_ver; j++) {
					hub1[j] = 0.0;
				}

				for(int j = 0; j < num_ver; j++) {
					for(int k = 0; k < num_ver; k++) {
						if(adj_mat[j][k] == 1) {
							hub1[j] += auth1[k]; 
						}
					}
				}
				authscalefact = 0.0;
				authsumsqr = 0.0;
				for(int x = 0; x < num_ver; x++) {
					authsumsqr += auth1[x]*auth1[x];    
				}
				authscalefact = Math.sqrt(authsumsqr); 
				for(int x = 0; x < num_ver; x++) {
					auth1[x] = auth1[x]/authscalefact;
				}
				hubscalefact = 0.0;
				hubsumsqr = 0.0;
				for(int x = 0; x < num_ver; x++) {
					hubsumsqr += hub1[x]*hub1[x];    
				}
				hubscalefact = Math.sqrt(hubsumsqr); 
				for(int x = 0; x < num_ver; x++) {
					hub1[x] = hub1[x]/hubscalefact;
				}
				i++; 
			} while(false == Converged(auth1, authprev) || false == Converged(hub1, hubprev));
			
			for(int x = 0; x < num_ver; x++) {
				System.out.printf(" A/H[%d]=%.6f/%.6f\n",x,Math.round(auth1[x]*1000000.0)/1000000.0,Math.round(hub1[x]*1000000.0)/1000000.0); 
			}
			return;
			
		}//if
		
		for(int i = 0; i < num_ver; i++)
		{
			hub1[i] = ihub[i];
			auth1[i] = iauth[i];
			hubprev[i] = hub1[i];
			authprev[i] = auth1[i]; 
		}
		System.out.print("Base:    0 :");
		for(int i = 0; i < num_ver; i++) {
			//Initial case
			System.out.printf(" A/H[%d]=%.6f/%.6f",i,Math.round(iauth[i]*1000000.0)/1000000.0,Math.round(ihub[i]*1000000.0)/1000000.0); 
		}
		if (iters != 0) { 
			for(int i = 0; i < iters; i++) { 
				for(int j = 0; j < num_ver; j++) {
					auth1[j] = 0.0;
				}

				for(int j = 0; j < num_ver; j++) {
					for(int k = 0; k < num_ver; k++) {
						if(adj_mat[k][j] == 1) {
							auth1[j] += hub1[k]; 
						}
					}
				}
				for(int j = 0; j < num_ver; j++) {
					hub1[j] = 0.0;
				}

				for(int j = 0; j < num_ver; j++) {
					for(int k = 0; k < num_ver; k++) {
						if(adj_mat[j][k] == 1) {
							hub1[j] += auth1[k]; 
						}
					}
				}
				authscalefact = 0.0;
				authsumsqr = 0.0;
				for(int x = 0; x < num_ver; x++) {
					authsumsqr += auth1[x]*auth1[x];    
				}
				authscalefact = Math.sqrt(authsumsqr); 
				for(int x = 0; x < num_ver; x++) {
					auth1[x] = auth1[x]/authscalefact;
				}
				hubscalefact = 0.0;
				hubsumsqr = 0.0;
				for(int x = 0; x < num_ver; x++) {
					hubsumsqr += hub1[x]*hub1[x];    
				}
				hubscalefact = Math.sqrt(hubsumsqr); 
				for(int x = 0; x < num_ver; x++) {
					hub1[x] = hub1[x]/hubscalefact;
				}

				System.out.println();
				System.out.print("Iter:    " + (i+1) + " :");
				for(int x = 0; x < num_ver; x++) {
					System.out.printf(" A/H[%d]=%.6f/%.6f",x,Math.round(auth1[x]*1000000.0)/1000000.0,Math.round(hub1[x]*1000000.0)/1000000.0); 
				}

			}
		} 
		else
		{
			int i = 0;
			do {  
				for(int x = 0; x < num_ver; x++) {
					authprev[x] = auth1[x];
					hubprev[x] = hub1[x];
				}

				//Auth step starts
				for(int x = 0; x < num_ver; x++) {
					auth1[x] = 0.0;
				}

				for(int x = 0; x < num_ver; x++) {
					for(int y = 0; y < num_ver; y++) {
						if(adj_mat[y][x] == 1) {
							auth1[x] += hub1[y]; 
						}
					}
				}
				//Auth step ends hub starts
				
				for(int x = 0; x < num_ver; x++) {
					hub1[x] = 0.0;
				}
				for(int x = 0; x < num_ver; x++) {
					for(int y = 0; y < num_ver; y++) {
						if(adj_mat[x][y] == 1) {
							hub1[x] += auth1[y]; 
						}
					}
				}
				//Hub step ends
				
				//Scaling Auth starts
				authscalefact = 0.0;
				authsumsqr = 0.0;
				for(int x = 0; x < num_ver; x++) {
					authsumsqr += auth1[x]*auth1[x];    
				}
				authscalefact = Math.sqrt(authsumsqr); 
				for(int x = 0; x < num_ver; x++) {
					auth1[x] = auth1[x]/authscalefact;
				}
				//Scaling Auth ends hub starts
				
				
				hubscalefact = 0.0;
				hubsumsqr = 0.0;
				for(int x = 0; x < num_ver; x++) {
					hubsumsqr += hub1[x]*hub1[x];    
				}
				hubscalefact = Math.sqrt(hubsumsqr); 
				for(int x = 0; x < num_ver; x++) {
					hub1[x] = hub1[x]/hubscalefact;
				}
				// Scaling hub ends
				
				i++; 
				System.out.println();
				System.out.print("Iter:    " + i + " :");
				for(int x = 0; x < num_ver; x++) {
					System.out.printf(" A/H[%d]=%.6f/%.6f",x,Math.round(auth1[x]*1000000.0)/1000000.0,Math.round(hub1[x]*1000000.0)/1000000.0); 
				}
			} while( false == Converged(auth1, authprev) || false == Converged(hub1, hubprev));
		}
		System.out.println("\n");
		
		
	}//hitsalgo
	
	public static boolean Converged(double[] p, double[] q)
	{
		for(int i = 0 ; i < num_ver; i++) {
			if ( abs(p[i] - q[i]) > error_rate ) 
				return false;
		}
		return true;
	}//converged
	
}//class