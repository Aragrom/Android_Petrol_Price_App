package org.me.myandroidstuff;

import android.util.Log;

import java.io.Serializable;

/*
 * ============================
 * Data Structure (Object) with methods managing pricing data
 *============================
 */	
public class QueryObject implements Serializable {
	
	//(Could have used vector)								// { 137.9 , 123.5 , 121.7 }
	private float[] aFloatUnleaded = new float[3];			// UNLEADED
	private float[] aFloatDiesel = new float[3];			// DIESEL
	private float[] aFloatSuperUnleaded = new float[3];		// SUPER UNLEADED
	private float[] aFloatLPG = new float[3];				// LIQUID PETROLEUM GAS
	private float[] aFloatLRP = new float[3];				// LEAD REPLACEMENT PETROL
	private int iFuelTypeCount = 0;							// Each time on element [0] new fuel type has been found (increment)
	
	
	/*
	 * ============================
	 * Data Structure (Object) with methods managing pricing data 
	 * used to return strings for display in list view
	 * 
	 * format example:
	 * 
	 *  "Unleaded:
	 *  Highest: 137.9
	 *  Average: 123.5
	 *  Lowest: 121.7"
	 *============================
	 */	
	public String getUnleadedPrices() {
		
		String str = "Unleaded: \n"
				+ " Highest: " + String.valueOf(aFloatUnleaded[0] + "\n")
				+ " Average: " + String.valueOf(aFloatUnleaded[1] + "\n")
				+ " Lowest: " + String.valueOf(aFloatUnleaded[2]);
		return str;
		
	}// End of getUnleadPrices
	
	
	
	public String getDieselPrices() {

		String str = "Diesel: \n"
				+ " Highest: " + String.valueOf(aFloatDiesel[0] + "\n")
				+ " Average: " + String.valueOf(aFloatDiesel[1] + "\n")
				+ " Lowest: " + String.valueOf(aFloatDiesel[2]);
		return str;
		
	}// End of getDieselPrices
	
	
	
	public String getSuperUnleadedPrices() {

		String str = "Super Unleaded: \n"
				+ " Highest: " + String.valueOf(aFloatSuperUnleaded[0] + "\n")
				+ " Average: " + String.valueOf(aFloatSuperUnleaded[1] + "\n")
				+ " Lowest: " + String.valueOf(aFloatSuperUnleaded[2]);
		return str;
		
	}// End of getSuperUnleadPrices
	
	
	
	public String getLPGPrices() {
		
		String str = "Liquid Petroleum Gas: \n"
				+ " Highest: " + String.valueOf(aFloatLPG[0] + "\n")
				+ " Average: " + String.valueOf(aFloatLPG[1] + "\n")
				+ " Lowest: " + String.valueOf(aFloatLPG[2]);
		return str;
		
	}// End of getLPGPrices
	
	
	
	public String getLRPPrices() {
		
		String str = "Lead Replacement Petrol: \n"
				+ " Highest: " + String.valueOf(aFloatLRP[0] + "\n")
				+ " Average: " + String.valueOf(aFloatLRP[1] + "\n")
				+ " Lowest: " + String.valueOf(aFloatLRP[2]);
		return str;
		
	}// End of getLRPPrices
	
	//============================
	
	
	/*
	 * ============================
	 * Using a while loop on the fuel types logged
	 * get there string to be displayed and add it to array of strings (for adapter)
	 *============================
	 */
	public String[] getArrayOfDataForListView()
	{
		String[] aStr = new String[iFuelTypeCount];
		int i = 0;

		while ( i <= iFuelTypeCount - 1 ) {
			
			switch (i) 
			{
			
	        case 0: aStr[i]= getUnleadedPrices();
	        		break;
	        case 1: aStr[i]= getDieselPrices();
    				break;
	        case 2: aStr[i]= getSuperUnleadedPrices();
    				break;
	        case 3: aStr[i]= getLPGPrices();
    				break;
	        case 4: aStr[i]= getLRPPrices();
    				break;
	        default: Log.e("QueryObject", "Error: Fuel Type not found (Empty array of 3 floats returned)");
            		break;
			}
			
			i++;
			
		}// End of while
		
		Log.e("QueryObject", "Returned array of strings for List View");
		
		return aStr;
		
	}// End of getArrayOfDataForListView
	
	
	/*
	 * ============================
	 * Called from the Parser.object Sends a type, rate and data to be used
	 *============================
	 */
	public void addData(String strType, String strRate, String strData)
	{
		float[] aFloatType = getFuelType(strType);		// Get float[3] of the fuel type
		int iElement = getElementNumber(strRate);		// get int representing the element of the fuel type (high, average, low)
		
		//if new fuel is found increment
		if(iElement == 0) iFuelTypeCount++;
		
		//Assign converted data
		aFloatType[iElement] = Float.valueOf(strData);
		
		//Shows in log as elements are added
		Log.e("QueryObject", strType + " " + String.valueOf(aFloatType[0]) 
				+ " " + String.valueOf(aFloatType[1]) 
				+ " " + String.valueOf(aFloatType[2]));
	
	}// End of addData
	
	
	/*
	 * ============================
	 * Called from addData
	 * array of floats (float[3])
	 * 'unleaded' returns aFloatUnleaded etc.
	 *============================
	 */
	private float[] getFuelType(String str)
	{
		if(str.equalsIgnoreCase("unleaded")) return aFloatUnleaded;
		else if (str.equalsIgnoreCase("diesel")) return aFloatDiesel;
		else if (str.equalsIgnoreCase("super unleaded")) return aFloatSuperUnleaded;
		else if (str.equalsIgnoreCase("lpg")) return aFloatLPG;
		else if (str.equalsIgnoreCase("lrp")) return aFloatLRP;
		
		Log.e("QueryObject", "Error: Fuel Type not found (Empty array of 3 floats returned)");
		return new float[3];
		
    }// End of getFuelType
	
	
	/*
	 * ============================
	 * Called from addData
	 * Returns an int representing the element match a string
	 * 'highest' returns 0 etc.
	 *============================
	 */
	private int getElementNumber(String str)
	{
		int i = -1;
		if(str.equalsIgnoreCase("highest")) i = 0;
		else if (str.equalsIgnoreCase("average")) i = 1;
		else if (str.equalsIgnoreCase("lowest")) i = 2;
		
		Log.e("QueryObject", "getElementNumber() " + String.valueOf(i) );
		
		return i;
		
	}// End of getElementNumber
	
}
